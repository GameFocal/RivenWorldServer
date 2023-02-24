package lowentry.ue4.classes.sockets;


import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;


public class SocketClient
{
	protected final SocketServer server;
	protected final PyroClient   client;
	
	protected InetSocketAddress clientUdpAddress;
	
	protected volatile Object attachment;
	
	protected boolean isDisconnecting = false;
	
	protected boolean            isHandshakeCompleted            = false;
	protected boolean            isWebsocket                     = false;
	protected Collection<byte[]> bufferedMessagesDuringHandshake = null;
	
	protected volatile String addressText = null;
	
	protected final int hashCode;

	protected DatagramPacket udpOutPacket;
	
	
	public SocketClient(final SocketServer server, final PyroClient client)
	{
		this.server = server;
		this.client = client;
		this.hashCode = super.hashCode();
	}
	
	
	/**
	 * Call this after receiving the remote UDP port.
	 */
	protected void setRemoteUdpPort(final int port)
	{
		if(port <= 0)
		{
			clientUdpAddress = null;
		}
		else
		{
			clientUdpAddress = new InetSocketAddress(client.getRemoteAddress().getAddress(), port);
		}
		
		if(SocketServer.IS_DEBUGGING)
		{
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " has send UDP port " + port + " which resulted in UDP address: " + clientUdpAddress);
		}
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
	 * Returns the remote UDP socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddressUdp()
	{
		return clientUdpAddress;
	}
	
	/**
	 * Returns the remote UDP port, returns 0 if there is no UDP connection.
	 */
	public int getRemotePortUdp()
	{
		if(clientUdpAddress == null)
		{
			return 0;
		}
		return clientUdpAddress.getPort();
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
		
		if(bufferedMessagesDuringHandshake != null)
		{
			for(byte[] message : bufferedMessagesDuringHandshake)
			{
				sendMessage(message);
			}
			bufferedMessagesDuringHandshake = null;
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
	 * Will send the bytes immediately.
	 */
	public void sendUnreliableMessage(final byte[]... bytes)
	{
		sendUnreliableMessage(LowEntry.mergeBytes(bytes));
	}
	/**
	 * Will send the bytes immediately.
	 */
	public void sendUnreliableMessage(final byte[] bytes)
	{
		if(client.selector().isNetworkThread())
		{
			if(bytes == null)
			{
				sendUnreliableMessageCode(ByteBuffer.allocate(0), true);
			}
			else
			{
				sendUnreliableMessageCode(ByteBuffer.wrap(bytes), true);
			}
		}
		else
		{
			if(bytes == null)
			{
				client.selector().scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.allocate(0), true));
			}
			else
			{
				client.selector().scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.wrap(bytes), true));
			}
		}
	}
	/**
	 * Will send the bytes immediately, the ByteBuffer can be cleared and reused after calling this function.
	 */
	public void sendUnreliableMessage(final ByteBuffer bytes)
	{
		if(client.selector().isNetworkThread())
		{
			if(bytes == null)
			{
				sendUnreliableMessageCode(ByteBuffer.allocate(0), true);
			}
			else
			{
				sendUnreliableMessageCode(bytes, false);
			}
		}
		else
		{
			if(bytes == null)
			{
				client.selector().scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.allocate(0), true));
			}
			else
			{
				final ByteBuffer b = LowEntry.cloneByteBuffer(bytes, false);
				client.selector().scheduleTask(() -> sendUnreliableMessageCode(b, true));
			}
		}
	}
	protected void sendUnreliableMessageCode(final ByteBuffer bytes, final boolean clonedBytes)
	{
		if(server.serverUdp == null)
		{
			return;
		}
		
		if(isWebsocket())
		{
			if(!isConnected())
			{
				return;
			}
			
			if(!isHandshakeCompleted())
			{
				return;
			}
			
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(bytes.remaining()) + bytes.remaining());
			
			ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(bytes.remaining()));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
			buffer.put(SocketMessageType.SIMULATED_UNRELIABLE_MESSAGE);
			SocketFunctions.putUint(buffer, bytes.remaining());
			
			buffer.flip();
			
			try
			{
				client.write(buffer);
				if(bytes.remaining() > 0)
				{
					if(clonedBytes)
					{
						client.write(bytes);
					}
					else
					{
						client.writeCopy(bytes);
					}
				}
			}
			catch(PyroException e)
			{
				if(SocketServer.IS_DEBUGGING)
				{
					SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send an unreliable message:");
					SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
				}
			}
		}
		else
		{
			if(!isConnectedUdp())
			{
				return;
			}
			
			if(!isHandshakeCompleted())
			{
				return;
			}
			
			if(clientUdpAddress == null)
			{
				return;
			}
			
			server.serverUdp.write(bytes, clientUdpAddress);
		}
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendMessage(final byte[]... bytes)
	{
		sendMessage(LowEntry.mergeBytes(bytes));
	}
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendMessage(final byte[] bytes)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(client.selector().isNetworkThread())
		{
			sendMessageCode(b);
		}
		else
		{
			client.selector().scheduleTask(() -> sendMessageCode(b));
		}
	}
	protected void sendMessageCode(final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			if(bufferedMessagesDuringHandshake == null)
			{
				bufferedMessagesDuringHandshake = new ArrayList<>();
			}
			bufferedMessagesDuringHandshake.add(bytes);
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(bytes.length) + bytes.length);
			
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(bytes.length));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
		}
		else
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(bytes.length));
		}
		buffer.put(SocketMessageType.MESSAGE);
		SocketFunctions.putUint(buffer, bytes.length);
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
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a message:");
				SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	protected void sendFunctionCallResponse(final int functionCallId, final byte[] bytes)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(client.selector().isNetworkThread())
		{
			sendFunctionCallResponseCode(functionCallId, b);
		}
		else
		{
			client.selector().scheduleTask(() -> sendFunctionCallResponseCode(functionCallId, b));
		}
	}
	protected void sendFunctionCallResponseCode(final int functionCallId, final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			// not possible
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length) + bytes.length);
			
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
		}
		else
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length));
		}
		buffer.put(SocketMessageType.FUNCTION_CALL_RESPONSE);
		SocketFunctions.putUint(buffer, functionCallId);
		SocketFunctions.putUint(buffer, bytes.length);
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
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a function call response:");
				SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	protected void sendLatentFunctionCallResponse(final int functionCallId, final byte[] bytes)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(client.selector().isNetworkThread())
		{
			sendLatentFunctionCallResponseCode(functionCallId, b);
		}
		else
		{
			client.selector().scheduleTask(() -> sendLatentFunctionCallResponseCode(functionCallId, b));
		}
	}
	protected void sendLatentFunctionCallResponseCode(final int functionCallId, final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			// not possible
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length) + bytes.length);
			
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
		}
		else
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length));
		}
		buffer.put(SocketMessageType.LATENT_FUNCTION_CALL_RESPONSE);
		SocketFunctions.putUint(buffer, functionCallId);
		SocketFunctions.putUint(buffer, bytes.length);
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
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a latent function call response:");
				SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	protected void sendLatentFunctionCallCancel(final int functionCallId)
	{
		if(client.selector().isNetworkThread())
		{
			sendLatentFunctionCallCancelCode(functionCallId);
		}
		else
		{
			client.selector().scheduleTask(() -> sendLatentFunctionCallCancelCode(functionCallId));
		}
	}
	protected void sendLatentFunctionCallCancelCode(final int functionCallId)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			// not possible
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(functionCallId));
			
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(functionCallId));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
		}
		else
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId));
		}
		buffer.put(SocketMessageType.LATENT_FUNCTION_CALL_CANCELED);
		SocketFunctions.putUint(buffer, functionCallId);
		buffer.flip();
		
		try
		{
			client.write(buffer);
		}
		catch(PyroException e)
		{
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a latent function call cancel:");
				SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
			}
		}
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	protected void sendConnectionValidationResponse(final int functionCallId)
	{
		if(client.selector().isNetworkThread())
		{
			sendConnectionValidationResponseCode(functionCallId);
		}
		else
		{
			client.selector().scheduleTask(() -> sendConnectionValidationResponseCode(functionCallId));
		}
	}
	protected void sendConnectionValidationResponseCode(final int functionCallId)
	{
		if(!isConnected())
		{
			return;
		}
		
		if(!isHandshakeCompleted())
		{
			// not possible
			return;
		}
		
		ByteBuffer buffer;
		if(isWebsocket())
		{
			byte opcode = -128 | 2; // FIN + BINARY DATA
			int size = (1 + SocketFunctions.uintByteCount(functionCallId));
			
			buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + 1 + SocketFunctions.uintByteCount(functionCallId));
			buffer.put(opcode);
			SocketFunctions.putWebsocketSizeBytes(buffer, size);
		}
		else
		{
			buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId));
		}
		buffer.put(SocketMessageType.CONNECTION_VALIDATION_RESPONSE);
		SocketFunctions.putUint(buffer, functionCallId);
		buffer.flip();
		
		try
		{
			client.write(buffer);
		}
		catch(PyroException e)
		{
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send a connection validation response:");
				SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
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
					SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " can't be send FIN + CLOSE (websocket):");
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
	 * Returns true when the client is connected (ment for UDP methods).
	 */
	protected boolean isConnectedUdp()
	{
		return !isDisconnecting;
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
	public SocketServer server()
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
	public void execute(Runnable runnable)
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
