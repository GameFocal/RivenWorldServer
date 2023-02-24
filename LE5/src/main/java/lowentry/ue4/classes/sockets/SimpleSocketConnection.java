package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.classes.sockets.SimpleSocketConnectionHandler.ConnectingStage;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


public class SimpleSocketConnection
{
	protected final PyroSelector                   selector;
	protected final InetSocketAddress              address;
	protected final SimpleSocketConnectionListener socketListener;
	
	protected          PyroClient connection;
	protected volatile Object     attachment;
	protected          boolean    isDisconnecting;
	
	
	public SimpleSocketConnection(final String host, final int port, final SimpleSocketConnectionListener listener)
	{
		this.socketListener = listener;
		selector = new PyroSelector();
		address = new InetSocketAddress(host, port);
	}
	
	public SimpleSocketConnection(final InetSocketAddress end, final SimpleSocketConnectionListener listener)
	{
		this.socketListener = listener;
		selector = new PyroSelector();
		address = end;
	}
	
	
	/**
	 * Tries to connect, returns true if the connection was successful.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public boolean connect()
	{
		selector.checkThread();
		
		if(connection != null)
		{
			connection.shutdown();
			connection = null;
		}
		
		try
		{
			isDisconnecting = false;
			
			SimpleSocketConnectionHandler listener = createListener();
			connection = selector.connect(address, listener);
			if(connection == null)
			{
				return false;
			}
			
			while(listener.connectingStage == ConnectingStage.WAITING)
			{
				listen(1);
			}
			if((listener.connectingStage == ConnectingStage.UNCONNECTABLE) || connection.isDisconnected())
			{
				connection.shutdown();
				connection = null;
				return false;
			}
			
			connection.write(ByteBuffer.wrap(new byte[]{13, 10, 13, 10})); // \r\n\r\n
			
			if(isConnected())
			{
				listener.callConnected();
			}
			else
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			if(connection != null)
			{
				connection.shutdown();
				connection = null;
			}
			return false;
		}
		return true;
	}
	
	protected SimpleSocketConnectionHandler createListener()
	{
		return new SimpleSocketConnectionHandler(socketListener, this);
	}
	
	
	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public void listen()
	{
		listen(100);
	}
	
	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public void listen(final long eventTimeout)
	{
		selector.checkThread();
		
		if(eventTimeout <= 10) // too small for CachedTime.millisSinceStart()
		{
			pyroListen(eventTimeout);
			return;
		}
		
		long startTime = CachedTime.millisSinceStart();
		pyroListen(eventTimeout);
		long lastTime = CachedTime.millisSinceStart();
		long timeSpend = (lastTime - startTime);
		while(timeSpend < (eventTimeout - 10))
		{
			pyroListen(eventTimeout - timeSpend);
			long newTime = CachedTime.millisSinceStart();
			if(newTime != lastTime)
			{
				lastTime = newTime;
				timeSpend = (lastTime - startTime);
			}
		}
	}
	
	private void pyroListen(final long eventTimeout)
	{
		for(long i = 1; i <= eventTimeout; i++)
		{
			try
			{
				selector.select(1);
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	/**
	 * Attach any object to the connection, for example to store session information.<br>
	 * Only one object can be attached at a time!
	 */
	public void setAttachment(final Object attachment)
	{
		this.attachment = attachment;
	}
	
	/**
	 * Returns the previously attached object, or <code>null</code> if none is set.<br>
	 * Call this function like this:<br>
	 * <br>
	 * <code>YourClass object = connection.getAttachment();</code><br>
	 * <br>
	 * This will automatically take the type that the compiler thinks it has to be.<br>
	 * To control the type manually, call this function like this:<br>
	 * <br>
	 * <code>YourClass object = connection.&lt;YourClass&gt;getAttachment();</code>
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttachment()
	{
		return (T) attachment;
	}
	
	/**
	 * Returns true if the connection has an attachment.
	 */
	public boolean hasAttachment()
	{
		return (attachment != null);
	}
	
	
	/**
	 * Returns the local socket address (host and port), can return null.
	 */
	public InetSocketAddress getLocalAddress()
	{
		if(connection == null)
		{
			return null;
		}
		return connection.getLocalAddress();
	}
	
	/**
	 * Returns the remote socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddress()
	{
		if(connection == null)
		{
			return null;
		}
		return connection.getRemoteAddress();
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendPacket(final byte[]... bytes)
	{
		sendPacket(LowEntry.mergeBytes(bytes));
	}
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendPacket(final byte[] bytes)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(selector.isNetworkThread())
		{
			sendPacketCode(b);
		}
		else
		{
			selector.scheduleTask(() -> sendPacketCode(b));
		}
	}
	protected void sendPacketCode(final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.put((byte) (bytes.length >> 24));
		buffer.put((byte) (bytes.length >> 16));
		buffer.put((byte) (bytes.length >> 8));
		buffer.put((byte) (bytes.length));
		buffer.flip();
		
		try
		{
			connection.write(buffer);
			if(bytes.length > 0)
			{
				connection.write(ByteBuffer.wrap(bytes));
			}
		}
		catch(PyroException e)
		{
		}
	}
	
	
	/**
	 * Gracefully shuts down the connection. The connection
	 * is closed after the last outbound bytes are sent.
	 * Enqueuing new bytes after shutdown will not do anything.
	 */
	public void disconnect()
	{
		if(selector.isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			selector.scheduleTask(() ->
			{
				if(connection != null)
				{
					connection.shutdown();
					connection = null;
				}
			});
		}
		else
		{
			selector.scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				if(connection != null)
				{
					connection.shutdown();
					connection = null;
				}
			});
		}
	}
	
	/**
	 * Immediately drop the connection, regardless of any
	 * pending outbound bytes. Actual behavior depends on
	 * the socket linger settings.
	 */
	public void disconnectImmediately()
	{
		if(selector.isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			selector.scheduleTask(() ->
			{
				if(connection != null)
				{
					connection.dropConnection();
					connection = null;
				}
			});
		}
		else
		{
			selector.scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				if(connection != null)
				{
					connection.dropConnection();
					connection = null;
				}
			});
		}
	}
	
	/**
	 * Returns true when the connection is open.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public boolean isConnected()
	{
		selector.checkThread();
		return (!isDisconnecting && (connection != null) && !connection.isDisconnected());
	}
	
	
	/**
	 * Returns the PyroNet object of this wrapper.
	 */
	public PyroClient pyro()
	{
		return connection;
	}
	
	/**
	 * Returns the selector.
	 */
	public PyroSelector selector()
	{
		return selector;
	}
	
	
	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(final Runnable runnable)
	{
		if(selector.isNetworkThread())
		{
			runnable.run();
		}
		else
		{
			selector.scheduleTask(runnable);
		}
	}
	
	/**
	 * Returns true if this is the thread this object was created in.
	 */
	public boolean isNetworkThread()
	{
		return selector.isNetworkThread();
	}
	
	
	public String getAddressText()
	{
		if(connection == null)
		{
			return "closed";
		}
		return connection.getAddressText();
	}
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
}
