package lowentry.ue4.libs.pyronet.jawnae.pyronet;


import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroSelectorListener;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroServerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class PyroSelector
{
	public static final boolean DO_NOT_CHECK_NETWORK_THREAD = false;
	
	public static final int BUFFER_SIZE = 512 * 1024;
	
	private       Thread               networkThread;
	private final Selector             nioSelector;
	public final  ByteBuffer           networkBuffer;
	private final PyroSelectorListener listener;
	
	public PyroSelector()
	{
		this(null);
	}
	
	public PyroSelector(PyroSelectorListener listener)
	{
		this.listener = listener;
		this.networkBuffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
		
		try
		{
			this.nioSelector = Selector.open();
		}
		catch(IOException exc)
		{
			throw new PyroException("Failed to open a selector?!", exc);
		}
		
		this.networkThread = Thread.currentThread();
	}
	
	//
	
	public ByteBuffer malloc(byte[] array)
	{
		ByteBuffer copy = ByteBuffer.allocate(array.length);
		copy.put(array);
		copy.flip();
		return copy;
	}
	
	public ByteBuffer copy(ByteBuffer buffer)
	{
		int pos = buffer.position();
		ByteBuffer copy = ByteBuffer.allocate(buffer.remaining());
		copy.put(buffer);
		buffer.position(pos);
		copy.flip();
		return copy;
	}
	
	//
	
	public final boolean isNetworkThread()
	{
		if(DO_NOT_CHECK_NETWORK_THREAD)
		{
			return true;
		}
		
		return networkThread == Thread.currentThread();
	}
	
	public final Thread networkThread()
	{
		return this.networkThread;
	}
	
	public final void checkThread()
	{
		if(DO_NOT_CHECK_NETWORK_THREAD)
		{
			return;
		}
		
		if(!this.isNetworkThread())
		{
			throw new PyroException("call from outside the network-thread, you must schedule tasks");
		}
	}
	
	public PyroServer listen(InetSocketAddress end, int backlog, PyroServerListener listener) throws IOException
	{
		try
		{
			return new PyroServer(this, nioSelector, end, backlog, listener);
		}
		catch(IOException exc)
		{
			if(this.listener == null)
			{
				throw exc;
			}
			
			this.listener.serverBindFailed(exc);
			return null;
		}
	}
	
	public PyroServer listen(InetSocketAddress end, PyroServerListener listener) throws IOException
	{
		return this.listen(end, 50, listener);
	}
	
	public PyroServer listen(int port, PyroServerListener listener) throws IOException
	{
		return this.listen(new InetSocketAddress(InetAddress.getLocalHost(), port), listener);
	}
	
	public PyroServer listen(int port, int backlog, PyroServerListener listener) throws IOException
	{
		return this.listen(new InetSocketAddress(InetAddress.getLocalHost(), port), backlog, listener);
	}
	
	public PyroServer listen(boolean acceptExternalConnections, int port, PyroServerListener listener) throws IOException
	{
		return this.listen(new InetSocketAddress((acceptExternalConnections ? null : InetAddress.getLoopbackAddress()), port), listener);
	}
	
	public PyroServer listen(boolean acceptExternalConnections, int port, int backlog, PyroServerListener listener) throws IOException
	{
		return this.listen(new InetSocketAddress((acceptExternalConnections ? null : InetAddress.getLoopbackAddress()), port), backlog, listener);
	}
	
	public PyroClient connect(InetSocketAddress host, PyroClientListener listener) throws IOException
	{
		return this.connect(host, null, listener);
	}
	
	public PyroClient connect(InetSocketAddress host, InetSocketAddress bind, PyroClientListener listener) throws IOException
	{
		try
		{
			return new PyroClient(this, bind, host, listener);
		}
		catch(IOException exc)
		{
			if(this.listener == null)
			{
				throw exc;
			}
			
			this.listener.clientBindFailed(exc);
			return null;
		}
	}
	
	public void select(long eventTimeout)
	{
		//this.checkThread();
		
		this.executePendingTasks();
		this.performNioSelect(eventTimeout);
		this.handleSelectedKeys();
		this.executePendingTasks();
	}
	
	private void executePendingTasks()
	{
		while(true)
		{
			Runnable task = this.tasks.poll();
			if(task == null)
			{
				break;
			}
			
			if(this.listener != null)
			{
				this.listener.executingTask(task);
			}
			
			try
			{
				task.run();
			}
			catch(Exception cause)
			{
				if(this.listener != null)
				{
					this.listener.taskCrashed(task, cause);
				}
				else
				{
					cause.printStackTrace();
				}
			}
		}
	}
	
	private final void performNioSelect(long timeout)
	{
		int selected;
		try
		{
			selected = nioSelector.select(timeout);
		}
		catch(IOException exc)
		{
			if(this.listener != null)
			{
				this.listener.selectFailure(exc);
			}
			else
			{
				exc.printStackTrace();
			}
			return;
		}
		
		if(this.listener != null)
		{
			this.listener.selectedKeys(selected);
		}
	}
	
	private final void handleSelectedKeys()
	{
		Iterator<SelectionKey> keys = nioSelector.selectedKeys().iterator();
		
		while(keys.hasNext())
		{
			SelectionKey key = keys.next();
			keys.remove();
			
			if(key.channel() instanceof ServerSocketChannel)
			{
				PyroServer server = (PyroServer) key.attachment();
				if(this.listener != null)
				{
					this.listener.serverSelected(server);
				}
				server.onInterestOp();
			}
			
			if(key.channel() instanceof SocketChannel)
			{
				PyroClient client = (PyroClient) key.attachment();
				if(this.listener != null)
				{
					this.listener.clientSelected(client, key.readyOps());
				}
				client.onInterestOp();
			}
		}
	}
	
	
	public void spawnNetworkThread(final String name, final long eventTimeout)
	{
		// now no thread can access this selector
		//
		// N.B.
		// -- updating this non-volatile field is thread-safe
		// -- because the current thread can see it (causing it
		// -- to become UNACCESSIBLE), and all other threads
		// -- that might not see the change will
		// -- (continue to) block access to this selector
		this.networkThread = null;
		
		new Thread(() ->
		{
			// spawned thread can access this selector
			//
			// N.B.
			// -- updating this non-volatile field is thread-safe
			// -- because the current thread can see it (causing it
			// -- to become ACCESSIBLE), and all other threads
			// -- that might not see the change will
			// -- (continue to) block access to this selector
			PyroSelector.this.networkThread = Thread.currentThread();
			
			// start select-loop
			try
			{
				while(true)
				{
					PyroSelector.this.select(eventTimeout);
				}
			}
			catch(Exception exc)
			{
				// this never be caused by Pyro-code
				throw new IllegalStateException(exc);
			}
		}, name).start();
	}
	
	//
	
	private SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>();
	
	public void scheduleTask(Runnable task)
	{
		if(task == null)
		{
			throw new NullPointerException();
		}
		
		this.tasks.put(task);
		this.wakeup();
	}
	
	public void wakeup()
	{
		this.nioSelector.wakeup();
	}
	
	//
	
	final SelectionKey register(SelectableChannel channel, int ops) throws IOException
	{
		return channel.register(this.nioSelector, ops);
	}
	
	final boolean adjustInterestOp(SelectionKey key, int op, boolean state)
	{
		this.checkThread();
		
		try
		{
			int ops = key.interestOps();
			boolean changed = state != ((ops & op) == op);
			if(changed)
			{
				key.interestOps(state ? (ops | op) : (ops & ~op));
			}
			return changed;
		}
		catch(CancelledKeyException exc)
		{
			// ignore
			return false;
		}
	}
}
