package lowentry.ue4.libs.pyronet.jawnae.pyronet;


import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.traffic.ByteStream;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class PyroClient
{
	protected final PyroSelector selector;
	protected final SelectionKey key;
	protected final ByteStream   outbound;
	
	private PyroClientListener listener;
	
	
	// called by PyroSelector.connect()
	PyroClient(PyroSelector selector, InetSocketAddress bind, InetSocketAddress host, PyroClientListener listener) throws IOException
	{
		this(selector, PyroClient.bindAndConfigure(selector, SocketChannel.open(), bind), listener);
		
		((SocketChannel) this.key.channel()).connect(host);
	}
	
	// called by PyroClient and PyroServer
	PyroClient(PyroSelector selector, SelectionKey key, PyroClientListener listener)
	{
		this.listener = listener;
		this.selector = selector;
		this.selector.checkThread();
		
		this.key = key;
		this.key.attach(this);
		
		this.outbound = new ByteStream();
	}
	
	public void setListener(PyroClientListener listener)
	{
		this.selector.checkThread();
		
		this.listener = listener;
	}
	
	/**
	 * Returns the PyroSelector that created this client
	 */
	public PyroSelector selector()
	{
		return this.selector;
	}
	
	//
	
	/**
	 * Returns the local socket address (host+port)
	 */
	@SuppressWarnings("resource")
	public InetSocketAddress getLocalAddress()
	{
		Socket s = ((SocketChannel) key.channel()).socket();
		return (InetSocketAddress) s.getLocalSocketAddress();
	}
	
	/**
	 * Returns the remote socket address (host+port)
	 */
	@SuppressWarnings("resource")
	public InetSocketAddress getRemoteAddress()
	{
		Socket s = ((SocketChannel) key.channel()).socket();
		return (InetSocketAddress) s.getRemoteSocketAddress();
	}
	
	/**
	 * Returns the ip address
	 */
	@SuppressWarnings("resource")
	public InetAddress getInetAddress()
	{
		Socket s = ((SocketChannel) key.channel()).socket();
		return s.getInetAddress();
	}
	
	//
	
	public void setTimeout(int ms) throws IOException
	{
		this.selector.checkThread();
		
		((SocketChannel) key.channel()).socket().setSoTimeout(ms);
	}
	
	public void setLinger(boolean enabled, int seconds) throws IOException
	{
		this.selector.checkThread();
		
		((SocketChannel) key.channel()).socket().setSoLinger(enabled, seconds);
	}
	
	public void setKeepAlive(boolean enabled) throws IOException
	{
		this.selector.checkThread();
		
		((SocketChannel) key.channel()).socket().setKeepAlive(enabled);
	}
	
	//
	
	private boolean doEagerWrite = false;
	
	/**
	 * If enabled, causes calls to write() to make an attempt to write the bytes,
	 * without waiting for the selector to signal writable state.
	 */
	public void setEagerWrite(boolean enabled)
	{
		this.doEagerWrite = enabled;
	}
	
	//
	
	public void writeCopy(ByteBuffer data) throws PyroException
	{
		this.write(this.selector.copy(data));
	}
	
	/**
	 * Will enqueue the bytes to send them<br>
	 * 1. when the selector is ready to write, if eagerWrite is disabled (default)<br>
	 * 2. immediately, if eagerWrite is enabled<br>
	 * The ByteBuffer instance is kept, not copied, and thus should not be modified
	 *
	 * @throws PyroException when shutdown() has been called.
	 */
	public void write(ByteBuffer data) throws PyroException
	{
		this.selector.checkThread();
		
		if(!this.key.isValid())
		{
			// graceful, as this is meant to be async
			return;
		}
		
		if(this.doShutdown)
		{
			throw new PyroException("shutting down");
		}
		
		this.outbound.append(data);
		
		if(this.doEagerWrite)
		{
			try
			{
				this.onReadyToWrite();
			}
			catch(NotYetConnectedException exc)
			{
				this.adjustWriteOp();
			}
			catch(IOException exc)
			{
				this.onConnectionError(exc);
				key.cancel();
			}
		}
		else
		{
			this.adjustWriteOp();
		}
	}
	
	/**
	 * Writes as many as possible bytes to the socket buffer
	 */
	public int flush()
	{
		int total = 0;
		
		while(this.outbound.hasData())
		{
			int written;
			
			try
			{
				written = this.onReadyToWrite();
			}
			catch(IOException exc)
			{
				written = 0;
			}
			
			if(written == 0)
			{
				break;
			}
			
			total += written;
		}
		
		return total;
	}
	
	/**
	 * Makes an attempt to write all outbound
	 * bytes, fails on failure.
	 *
	 * @throws PyroException on failure
	 */
	public int flushOrDie() throws PyroException
	{
		int total = 0;
		
		while(this.outbound.hasData())
		{
			int written;
			
			try
			{
				written = this.onReadyToWrite();
			}
			catch(IOException exc)
			{
				written = 0;
			}
			
			if(written == 0)
			{
				throw new PyroException("failed to flush, wrote " + total + " bytes");
			}
			
			total += written;
		}
		
		return total;
	}
	
	/**
	 * Returns whether there are bytes left in the
	 * outbound queue.
	 */
	public boolean hasDataEnqueued()
	{
		this.selector.checkThread();
		
		return this.outbound.hasData();
	}
	
	private boolean doShutdown = false;
	
	/**
	 * Gracefully shuts down the connection. The connection
	 * is closed after the last outbound bytes are sent.
	 * Enqueuing new bytes after shutdown, is not allowed
	 * and will throw an exception
	 */
	public void shutdown()
	{
		this.selector.checkThread();
		
		this.doShutdown = true;
		
		if(!this.hasDataEnqueued())
		{
			this.dropConnection();
		}
	}
	
	/**
	 * Immediately drop the connection, regardless of any
	 * pending outbound bytes. Actual behaviour depends on
	 * the socket linger settings.
	 */
	public void dropConnection()
	{
		this.selector.checkThread();
		
		if(this.isDisconnected())
		{
			return;
		}
		
		Runnable drop = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if(key.channel().isOpen())
					{
						key.channel().close();
					}
				}
				catch(IOException exc)
				{
					selector().scheduleTask(this);
				}
			}
		};
		
		drop.run();
		
		this.onConnectionError("local");
	}
	
	/**
	 * Returns whether the connection is connected to a remote client.
	 */
	public boolean isDisconnected()
	{
		this.selector.checkThread();
		
		return !this.key.channel().isOpen();
	}
	
	//
	
	void onInterestOp()
	{
		if(!key.isValid())
		{
			this.onConnectionError("remote");
		}
		else
		{
			try
			{
				if(key.isConnectable())
				{
					this.onReadyToConnect();
				}
				if(key.isReadable())
				{
					this.onReadyToRead();
				}
				if(key.isWritable())
				{
					this.onReadyToWrite();
				}
			}
			catch(IOException exc)
			{
				this.onConnectionError(exc);
				key.cancel();
			}
		}
	}
	
	private void onReadyToConnect() throws IOException
	{
		this.selector.checkThread();
		
		this.selector.adjustInterestOp(key, SelectionKey.OP_CONNECT, false);
		((SocketChannel) key.channel()).finishConnect();
		
		listener.connectedClient(this);
	}
	
	@SuppressWarnings("resource")
	private void onReadyToRead() throws IOException
	{
		this.selector.checkThread();
		
		SocketChannel channel = (SocketChannel) key.channel();
		
		ByteBuffer buffer = this.selector.networkBuffer;
		
		// read from channel
		buffer.clear();
		int bytes = channel.read(buffer);
		if(bytes == -1)
		{
			throw new EOFException();
		}
		buffer.flip();
		
		listener.receivedData(this, buffer);
	}
	
	private int onReadyToWrite() throws IOException
	{
		this.selector.checkThread();
		
		int sent = 0;
		
		// copy outbound bytes into network buffer
		ByteBuffer buffer = this.selector.networkBuffer;
		buffer.clear();
		this.outbound.get(buffer);
		buffer.flip();
		
		// write to channel
		if(buffer.hasRemaining())
		{
			@SuppressWarnings("resource")
			SocketChannel channel = (SocketChannel) key.channel();
			sent = channel.write(buffer);
		}
		
		if(sent > 0)
		{
			this.outbound.discard(sent);
		}
		
		listener.sentData(this, sent);
		
		this.adjustWriteOp();
		
		if(this.doShutdown && !this.outbound.hasData())
		{
			this.dropConnection();
		}
		
		return sent;
	}
	
	void onConnectionError(final Object cause)
	{
		this.selector.checkThread();
		
		try
		{
			// if the key is invalid, the channel may remain open!!
			this.key.channel().close();
		}
		catch(IOException exc)
		{
			// type: java.io.IOException
			// message: "A non-blocking socket operation could not be completed immediately"
			
			// try again later
			this.selector.scheduleTask(() -> PyroClient.this.onConnectionError(cause));
			
			return;
		}
		
		if(SocketServer.IS_DEBUGGING && (cause instanceof Throwable))
		{
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] Client received a connection error:");
			SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace((Throwable) cause));
		}
		
		if(cause instanceof ConnectException)
		{
			listener.unconnectableClient(this);
		}
		else if(cause instanceof EOFException) // after read=-1
		{
			listener.disconnectedClient(this);
		}
		else if(cause instanceof IOException)
		{
			listener.droppedClient(this, (IOException) cause);
		}
		else if(!(cause instanceof String))
		{
			throw new IllegalStateException((Exception) cause);
		}
		else if(cause.equals("local"))
		{
			listener.disconnectedClient(this);
		}
		else if(cause.equals("remote"))
		{
			listener.droppedClient(this, null);
		}
		else
		{
			throw new IllegalStateException("illegal cause: " + cause);
		}
	}
	
	//
	
	void adjustWriteOp()
	{
		this.selector.checkThread();
		
		boolean interested = this.outbound.hasData();
		
		this.selector.adjustInterestOp(this.key, SelectionKey.OP_WRITE, interested);
	}
	
	static final SelectionKey bindAndConfigure(PyroSelector selector, SocketChannel channel, InetSocketAddress bind) throws IOException
	{
		selector.checkThread();
		
		channel.socket().bind(bind);
		
		return configure(selector, channel, true);
	}
	
	static final SelectionKey configure(PyroSelector selector, SocketChannel channel, boolean connect) throws IOException
	{
		selector.checkThread();
		
		channel.configureBlocking(false);
		channel.socket().setSoLinger(false, 0);
		channel.socket().setReuseAddress(true);
		channel.socket().setKeepAlive(true);
		channel.socket().setTcpNoDelay(true);
		channel.socket().setReceiveBufferSize(PyroSelector.BUFFER_SIZE);
		channel.socket().setSendBufferSize(PyroSelector.BUFFER_SIZE);
		
		int ops = SelectionKey.OP_READ;
		if(connect)
		{
			ops |= SelectionKey.OP_CONNECT;
		}
		
		return selector.register(channel, ops);
	}
	
	//
	
	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.getAddressText() + "]";
	}
	
	public final String getAddressText()
	{
		InetSocketAddress sockaddr = this.getRemoteAddress();
		if(sockaddr == null)
		{
			if(!this.key.channel().isOpen())
			{
				return "closed";
			}
			return "connecting";
		}
		InetAddress inetaddr = sockaddr.getAddress();
		if(inetaddr == null)
		{
			if(!this.key.channel().isOpen())
			{
				return "closed";
			}
			return "connecting";
		}
		return inetaddr.getHostAddress() + ":" + sockaddr.getPort();
	}
}
