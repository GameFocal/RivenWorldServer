package lowentry.ue4.classes.sockets;


import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;


public class SimpleSocketClient
{
	protected final SimpleSocketServer server;
	protected final PyroClient         client;
	
	protected volatile Object attachment;
	
	protected boolean isDisconnecting = false;
	
	protected boolean            isHandshakeCompleted           = false;
	protected boolean            isWebsocket                    = false;
	protected Collection<byte[]> bufferedPacketsDuringHandshake = null;
	
	protected volatile String addressText = null;
	
	protected final int hashCode;
	
	
	public SimpleSocketClient(final SimpleSocketServer server, final PyroClient client)
	{
		this.server = server;
		this.client = client;
		this.hashCode = super.hashCode();
	}
	
	
	/**
	 * Attach any object to a client, for example to store session information.<br>
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
	 * <code>YourClass object = client.getAttachment();</code><br>
	 * <br>
	 * This will automatically take the type that the compiler thinks it has to be.<br>
	 * To control the type manually, call this function like this:<br>
	 * <br>
	 * <code>YourClass object = client.&lt;YourClass&gt;getAttachment();</code>
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttachment()
	{
		return (T) attachment;
	}
	
	/**
	 * Returns true if the client has an attachment.
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
		return client.getLocalAddress();
	}
	
	/**
	 * Returns the remote socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddress()
	{
		return client.getRemoteAddress();
	}
	
	/**
	 * Returns the IP, can return null.
	 */
	public InetAddress getIp()
	{
		return client.getInetAddress();
	}
	
	/**
	 * Returns the IP as a String, can return null.
	 */
	public String getIpString()
	{
		InetAddress ip = getIp();
		if(ip == null)
		{
			return null;
		}
		return ip.getHostAddress();
	}
	
	
	/**
	 * Will set flags and send pending data.
	 */
	protected void onHandshakeCompleted(final boolean isWebsocket)
	{
		if(this.isHandshakeCompleted)
		{
			return;
		}
		
		this.isHandshakeCompleted = true;
		this.isWebsocket = isWebsocket;
		
		if(bufferedPacketsDuringHandshake != null)
		{
			for(byte[] packet : bufferedPacketsDuringHandshake)
			{
				sendPacket(packet);
			}
			bufferedPacketsDuringHandshake = null;
		}
	}
	
	/**
	 * Returns true if a handshaking message has been received and has been responded to.
	 */
	protected boolean isHandshakeCompleted()
	{
		return isHandshakeCompleted;
	}
	
	/**
	 * Returns true if this client uses a websocket.<br>
	 * <br>
	 * NOTE: only works after handshaking has completed.
	 */
	protected boolean isWebsocket()
	{
		return isWebsocket;
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
		if(client.selector().isNetworkThread())
		{
			sendPacketCode(b);
		}
		else
		{
			client.selector().scheduleTask(() -> sendPacketCode(b));
		}
	}
	protected void sendPacketCode(final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			if(bufferedPacketsDuringHandshake == null)
			{
				bufferedPacketsDuringHandshake = new ArrayList<>();
			}
			bufferedPacketsDuringHandshake.add(bytes);
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(bytes.length));
			buffer.put((byte) (-128 | 2)); // FIN + BINARY DATA
			SocketFunctions.putWebsocketSizeBytes(buffer, bytes.length);
		}
		else
		{
			buffer = ByteBuffer.allocate(4);
			buffer.put((byte) (bytes.length >> 24));
			buffer.put((byte) (bytes.length >> 16));
			buffer.put((byte) (bytes.length >> 8));
			buffer.put((byte) (bytes.length));
		}
		buffer.flip();
		
		try
		{
			client.write(buffer);
			if(bytes.length > 0)
			{
				client.write(ByteBuffer.wrap(bytes));
			}
		}
		catch(PyroException e)
		{
			if(SimpleSocketServer.IS_DEBUGGING)
			{
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a packet:");
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
			}
		}
	}
	
	
	/**
	 * Gracefully shuts down the connection. The connection is closed after the last outbound bytes are sent. Enqueuing new bytes after shutdown will not do anything.
	 */
	public void disconnect()
	{
		if(client.selector().isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			sendCloseMessage();
			client.shutdown();
		}
		else
		{
			client.selector().scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				sendCloseMessage();
				client.shutdown();
			});
		}
	}
	
	/**
	 * Immediately drop the connection, regardless of any pending outbound bytes. Actual behavior depends on the socket linger settings.
	 */
	public void disconnectImmediately()
	{
		if(client.selector().isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			client.dropConnection();
		}
		else
		{
			client.selector().scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				client.dropConnection();
			});
		}
	}
	
	/**
	 * Sends a close message to the client.<br>
	 * <br>
	 * This is required to do so when closing the clients connection.
	 */
	protected void sendCloseMessage()
	{
		if(!isHandshakeCompleted())
		{
			return;
		}
		
		if(isWebsocket())
		{
			try
			{
				client.write(ByteBuffer.wrap(new byte[]{-128 | 8})); // FIN + CLOSE
			}
			catch(PyroException e)
			{
				if(SocketServer.IS_DEBUGGING)
				{
					SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't send be send FIN + CLOSE (websocket):");
					SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
				}
			}
		}
	}
	
	
	/**
	 * Returns true when the client is connected.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public boolean isConnected()
	{
		client.selector().checkThread();
		return (!isDisconnecting && !client.isDisconnected());
	}
	
	
	/**
	 * Returns the PyroNet object of this wrapper.
	 */
	public PyroClient pyro()
	{
		return client;
	}
	
	/**
	 * Returns the server this client is connected to.
	 */
	public SimpleSocketServer server()
	{
		return server;
	}
	
	/**
	 * Returns the selector.
	 */
	public PyroSelector selector()
	{
		return client.selector();
	}
	
	
	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(final Runnable runnable)
	{
		if(client.selector().isNetworkThread())
		{
			runnable.run();
		}
		else
		{
			client.selector().scheduleTask(runnable);
		}
	}
	
	/**
	 * Returns true if this is the thread this object was created in.
	 */
	public boolean isNetworkThread()
	{
		return client.selector().isNetworkThread();
	}
	
	
	protected void saveAddressText()
	{
		if(client == null)
		{
			addressText = "closed";
			return;
		}
		addressText = client.getAddressText();
	}
	public String getAddressText()
	{
		final String addressText = this.addressText;
		if(addressText != null)
		{
			return addressText;
		}
		if(client == null)
		{
			return "closed";
		}
		return client.getAddressText();
	}
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	@Override
	public boolean equals(Object o)
	{
		return (this == o);
	}
}
