package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.sockets.SocketConnection.InternalFunctionCall;
import lowentry.ue4.classes.sockets.SocketConnection.InternalLatentFunctionCall;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event.PyroClientUdpListener;

import java.io.IOException;
import java.nio.ByteBuffer;


public class SocketConnectionHandler implements PyroClientListener, PyroClientUdpListener
{
	protected enum ConnectingStage
	{
		WAITING_FOR_CONNECTION, UNCONNECTABLE, WAITING_FOR_UDP_HANDSHAKE_ID, WAITING_FOR_UDP_HANDSHAKE_RESPONSE, CONNECTED
	}
	
	
	protected enum ReceivingStage
	{
		RECEIVE_TYPE, RECEIVE_FUNCTION_CALL_ID, RECEIVE_PACKET_SIZE, RECEIVE_PACKET
	}
	
	
	protected final SocketConnectionListener socketListener;
	protected final SocketConnection         connection;
	
	protected ConnectingStage connectingStage               = ConnectingStage.WAITING_FOR_CONNECTION;
	protected ByteBuffer      receivingHandshakeUdpId       = null;
	protected byte[]          handshakeUdpId                = null;
	protected boolean         socketListenerCalledConnected = false;
	
	protected boolean stopReceivingAnything = false;
	
	protected ReceivingStage receivingStage          = ReceivingStage.RECEIVE_TYPE;
	protected byte           receivingType           = 0;
	protected int            receivingFunctionCallId = 0;
	protected int            receivingPacketSize     = 0;
	protected byte[]         receivingPacket         = null;
	protected int            receivingPacketPosition = 0;
	
	protected ByteBuffer receivedIntegerBuffer = ByteBuffer.allocate(4);
	
	
	public SocketConnectionHandler(final SocketConnectionListener socketListener, final SocketConnection connection)
	{
		this.socketListener = socketListener;
		this.connection = connection;
	}
	
	
	@Override
	public void unconnectableClient(final PyroClient client)
	{
		if(connectingStage == ConnectingStage.WAITING_FOR_CONNECTION)
		{
			connectingStage = ConnectingStage.UNCONNECTABLE;
		}
	}
	
	@Override
	public void connectedClient(final PyroClient client)
	{
		if(connectingStage == ConnectingStage.WAITING_FOR_CONNECTION)
		{
			connectingStage = ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_ID;
		}
	}
	
	public void skipUdpHandshake()
	{
		if(connectingStage == ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_ID)
		{
			connectingStage = ConnectingStage.CONNECTED;
		}
	}
	
	public void callConnected()
	{
		if(!socketListenerCalledConnected)
		{
			socketListenerCalledConnected = true;
			socketListener.connected(connection);
		}
	}
	
	@Override
	public void droppedClient(final PyroClient client, final IOException cause)
	{
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		receivingHandshakeUdpId = null;
		connectingStage = ConnectingStage.UNCONNECTABLE;
		if(socketListenerCalledConnected)
		{
			socketListener.disconnected(connection);
		}
		connection.failAllFunctionCalls();
	}
	
	@Override
	public void disconnectedClient(final PyroClient client)
	{
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		receivingHandshakeUdpId = null;
		connectingStage = ConnectingStage.UNCONNECTABLE;
		if(socketListenerCalledConnected)
		{
			socketListener.disconnected(connection);
		}
		connection.failAllFunctionCalls();
	}
	
	@Override
	public void sentData(final PyroClient client, final int bytes)
	{
	}
	
	public void disconnect()
	{
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		receivingHandshakeUdpId = null;
		connectingStage = ConnectingStage.UNCONNECTABLE;
		connection.disconnect();
	}
	
	@Override
	public void receivedDataUdp(ByteBuffer data)
	{
		if(stopReceivingAnything)
		{
			return;
		}
		
		if(!data.hasRemaining())
		{
			return;
		}
		byte packetType = data.get();
		switch(packetType)
		{
			case SocketMessageUdpType.HANDSHAKE:
			{
				if(connectingStage != ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_RESPONSE)
				{
					return;
				}
				
				byte[] id = new byte[data.remaining() + 1];
				id[0] = SocketMessageUdpType.HANDSHAKE;
				data.get(id, 1, data.remaining());
				
				if(LowEntry.areBytesEqual(id, handshakeUdpId))
				{
					handshakeUdpId = null;
					connectingStage = ConnectingStage.CONNECTED;
				}
				return;
			}
			
			case SocketMessageUdpType.MESSAGE:
			{
				if(!socketListenerCalledConnected || (connection.connectionUdp == null))
				{
					return;
				}
				byte[] bytes = new byte[data.remaining()];
				data.get(bytes);
				socketListener.receivedUnreliableMessage(connection, bytes);
				return;
			}
			
			case SocketMessageUdpType.PING:
			{
				if(connection.connectionUdp == null)
				{
					return;
				}
				connection.connectionUdp.write(ByteBuffer.wrap(SocketMessageUdpType.PONG_BYTES));
				return;
			}
			
			case SocketMessageUdpType.PONG:
			{
				return;
			}
			
			default:
			{
			}
		}
	}
	
	/**
	 * Returns -1 if it failed.
	 */
	protected int receiveUint(final ByteBuffer data)
	{
		if(!data.hasRemaining())
		{
			return -1;
		}
		if(receivedIntegerBuffer.position() == 0)
		{
			byte firstByte = data.get();
			if(((firstByte >> 7) & 1) == 0)
			{
				return (firstByte & 0xFF);
			}
			receivedIntegerBuffer.put(firstByte);
		}
		while(data.hasRemaining() && (receivedIntegerBuffer.position() < 4))
		{
			receivedIntegerBuffer.put(data.get());
		}
		if(receivedIntegerBuffer.position() < 4)
		{
			// not enough bytes to get the integer
			return -1;
		}
		receivedIntegerBuffer.flip();
		byte b1 = receivedIntegerBuffer.get();
		byte b2 = receivedIntegerBuffer.get();
		byte b3 = receivedIntegerBuffer.get();
		byte b4 = receivedIntegerBuffer.get();
		receivedIntegerBuffer.clear();
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
		if(value <= 127)
		{
			// should not be possible
			disconnect();
			return -1;
		}
		return value;
	}
	
	@Override
	public void receivedData(final PyroClient client, final ByteBuffer data)
	{
		if(stopReceivingAnything)
		{
			return;
		}
		while(data.hasRemaining())
		{
			{// still verifying the UDP connection >>
				if(connectingStage == ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_ID)
				{
					if(receivingHandshakeUdpId == null)
					{
						receivingHandshakeUdpId = ByteBuffer.allocate(data.get() & 0xff);
					}
					
					while(data.hasRemaining() && receivingHandshakeUdpId.hasRemaining())
					{
						receivingHandshakeUdpId.put(data.get());
					}
					
					if(!receivingHandshakeUdpId.hasRemaining())
					{
						receivingHandshakeUdpId.flip();
						handshakeUdpId = new byte[receivingHandshakeUdpId.remaining()];
						receivingHandshakeUdpId.get(handshakeUdpId);
						receivingHandshakeUdpId = null;
						
						connectingStage = ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_RESPONSE;
					}
					continue;
				}
				if(connectingStage == ConnectingStage.WAITING_FOR_UDP_HANDSHAKE_RESPONSE)
				{
					// don't allow any TCP communication until the UDP handshake is fully completed
					disconnect();
					return;
				}
			}// still verifying the UDP connection <<
			
			{// get type >>
				if(receivingStage == ReceivingStage.RECEIVE_TYPE)
				{
					receivingType = data.get();
					
					if(receivingType == SocketMessageType.MESSAGE)
					{
						receivingStage = ReceivingStage.RECEIVE_PACKET_SIZE;
					}
					else if(receivingType == SocketMessageType.FUNCTION_CALL_RESPONSE)
					{
						receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
					}
					else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_RESPONSE)
					{
						receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
					}
					else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_CANCELED)
					{
						receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
					}
					else
					{
						// received invalid type
						disconnect();
						return;
					}
				}
			}// get type <<
			
			{// get function call id >>
				if(receivingStage == ReceivingStage.RECEIVE_FUNCTION_CALL_ID)
				{
					int receivedUint = receiveUint(data);
					if(receivedUint < 0)
					{
						return;
					}
					receivingFunctionCallId = receivedUint;
					
					if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_CANCELED)
					{
						InternalLatentFunctionCall functionCall;
						synchronized(connection.latentFunctionCalls)
						{
							functionCall = connection.latentFunctionCalls.remove(receivingFunctionCallId);
						}
						if(functionCall != null)
						{
							if(functionCall.latentAction != null)
							{
								functionCall.latentAction.canceledByServer();
							}
							if(functionCall.listener != null)
							{
								connection.canceledLatentFunctionCall(functionCall.listener);
							}
						}
						
						receivingStage = ReceivingStage.RECEIVE_TYPE;
					}
					else
					{
						receivingStage = ReceivingStage.RECEIVE_PACKET_SIZE;
					}
				}
			}// get function call id <<
			
			{// get packet size >>
				if(receivingStage == ReceivingStage.RECEIVE_PACKET_SIZE)
				{
					int receivedUint = receiveUint(data);
					if(receivedUint < 0)
					{
						return;
					}
					receivingPacketSize = receivedUint;
					
					receivingPacket = new byte[receivingPacketSize];
					receivingStage = ReceivingStage.RECEIVE_PACKET;
				}
			}// get packet size <<
			
			{// receiving packet >>
				if(receivingStage == ReceivingStage.RECEIVE_PACKET)
				{
					if(!data.hasRemaining())
					{
						return;
					}
					
					int packetBytesRemaining = receivingPacketSize - receivingPacketPosition;
					if(data.remaining() <= packetBytesRemaining)
					{
						int length = data.remaining();
						data.get(receivingPacket, receivingPacketPosition, length);
						receivingPacketPosition += length;
					}
					else
					{
						data.get(receivingPacket, receivingPacketPosition, packetBytesRemaining);
						receivingPacketPosition += packetBytesRemaining;
					}
					
					if(receivingPacketPosition >= receivingPacketSize) // packetBytesRemaining <= 0
					{
						if(receivingType == SocketMessageType.MESSAGE)
						{
							socketListener.receivedMessage(connection, receivingPacket);
						}
						else if(receivingType == SocketMessageType.FUNCTION_CALL_RESPONSE)
						{
							InternalFunctionCall functionCall;
							synchronized(connection.functionCalls)
							{
								functionCall = connection.functionCalls.remove(receivingFunctionCallId);
							}
							if((functionCall != null) && (functionCall.listener != null))
							{
								connection.receivedResponseFunctionCall(functionCall.listener, receivingPacket);
							}
						}
						else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_RESPONSE)
						{
							InternalLatentFunctionCall functionCall;
							synchronized(connection.latentFunctionCalls)
							{
								functionCall = connection.latentFunctionCalls.remove(receivingFunctionCallId);
							}
							if(functionCall != null)
							{
								if(functionCall.latentAction != null)
								{
									functionCall.latentAction.done();
								}
								if(functionCall.listener != null)
								{
									connection.receivedResponseLatentFunctionCall(functionCall.listener, receivingPacket);
								}
							}
						}
						else
						{
							// received invalid type
							disconnect();
							return;
						}
						
						receivingStage = ReceivingStage.RECEIVE_TYPE;
						receivingPacket = null;
						receivingPacketPosition = 0;
					}
				}
			}// receiving packet <<
		}
	}
}
