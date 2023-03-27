package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.classes.sockets.SocketServerClientHandler.WebsocketReceivingData.WebsocketPacketType;
import lowentry.ue4.classes.sockets.SocketServerClientHandler.WebsocketReceivingData.WebsocketReceivingStage;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Locale;


public class SocketServerClientHandler implements PyroClientListener
{
	protected static class WebsocketReceivingData
	{
		protected enum WebsocketReceivingStage
		{
			RECEIVE_OPCODE, RECEIVE_LENGTH_1, RECEIVE_LENGTH_2, RECEIVE_LENGTH_8, RECEIVE_MASK, RECEIVE_PAYLOAD
		}
		
		
		protected enum WebsocketPacketType
		{
			DATA, PING, PONG
		}
		
		
		public       WebsocketReceivingStage receivingStage            = WebsocketReceivingStage.RECEIVE_OPCODE;
		public       WebsocketPacketType     receivingType             = WebsocketPacketType.DATA;
		public final byte[]                  sizePacket                = new byte[8];
		public       byte                    sizePacketIndex           = 0;
		public       boolean                 containsMask              = false;
		public final byte[]                  mask                      = new byte[4];
		public       byte                    maskIndex                 = 0;
		public       long                    payloadRemainingByteCount = 0;
		public       ByteArrayOutputStream   pingPacket                = null;
	}
	
	
	protected enum ReceivingStage
	{
		RECEIVE_TYPE, RECEIVE_FUNCTION_CALL_ID, RECEIVE_PACKET_SIZE, RECEIVE_PACKET
	}
	
	
	private static final String WEBSOCKET_KEY_STRING = "Sec-WebSocket-Key:".toLowerCase(Locale.ENGLISH);
	
	
	protected final SocketServerListener socketListener;
	protected final SocketServer         socketServer;
	protected final SocketClient         socketClient;
	
	protected final HashMap<Integer,LatentResponse> latentResponses = new HashMap<>();
	
	protected boolean stopReceivingAnything = false;
	
	protected final long                  handshakingStartTime      = CachedTime.millisSinceStart();
	protected       boolean               handshakingPacketComplete = false;
	protected       ByteArrayOutputStream handshakingPacket         = null;
	protected       byte                  handshakingPacketStage    = 0;
	
	protected WebsocketReceivingData websocket = null;
	
	protected boolean hasReceivedServerUdpPort = false;
	protected boolean hasReceivedClientUdpPort = false;
	
	protected ReceivingStage receivingStage          = ReceivingStage.RECEIVE_TYPE;
	protected byte           receivingType           = 0;
	protected int            receivingFunctionCallId = 0;
	protected int            receivingPacketSize     = 0;
	protected byte[]         receivingPacket         = null;
	protected int            receivingPacketPosition = 0;
	
	protected ByteBuffer receivedIntegerBuffer = ByteBuffer.allocate(4);
	
	protected final int hashCode;
	
	
	public SocketServerClientHandler(final SocketServerListener socketListener, final SocketServer socketServer, final SocketClient socketClient)
	{
		this.socketListener = socketListener;
		this.socketServer = socketServer;
		this.socketClient = socketClient;
		this.hashCode = super.hashCode();
	}

	public SocketClient getSocketClient() {
		return socketClient;
	}

	@Override
	public void unconnectableClient(final PyroClient client)
	{
		if(SocketServer.IS_DEBUGGING)
		{
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was unconnectable");
		}
	}
	
	@Override
	public void connectedClient(final PyroClient client)
	{
		if(SocketServer.IS_DEBUGGING)
		{
			socketClient.saveAddressText();
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has connected");
		}
		synchronized(socketServer.clients)
		{
			socketServer.clients.add(socketClient);
		}
		socketServer.handshakingClientHandlers.add(this);
		socketListener.clientConnected(socketServer, socketClient);
	}
	
	@Override
	public void droppedClient(final PyroClient client, final IOException cause)
	{
		if(SocketServer.IS_DEBUGGING)
		{
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was dropped:");
			SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(cause));
		}
		hasDisconnected();
	}
	@Override
	public void disconnectedClient(final PyroClient client)
	{
		if(SocketServer.IS_DEBUGGING)
		{
			SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was disconnected");
		}
		hasDisconnected();
	}
	public void hasDisconnected()
	{
		synchronized(socketServer.clients)
		{
			socketServer.clients.remove(socketClient);
		}
		if(handshakingPacket != null)
		{
			socketServer.handshakingClientHandlers.remove(this);
		}
		socketServer.removeUdpClient(this);
		
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		websocket = null;
		
		{// canceling all latent function calls >>
			LatentResponse[] latentResponsesArray = null;
			synchronized(latentResponses)
			{
				if(latentResponses.size() > 0)
				{
					latentResponsesArray = new LatentResponse[latentResponses.size()];
					latentResponsesArray = latentResponses.values().toArray(latentResponsesArray);
					latentResponses.clear();
				}
			}
			if(latentResponsesArray != null)
			{
				for(LatentResponse response : latentResponsesArray)
				{
					response.canceledByDisconnecting();
				}
			}
		}// canceling all latent function calls <<
		
		socketListener.clientDisconnected(socketServer, socketClient);
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
		websocket = null;
		socketClient.disconnect();
	}
	
	public void receivedDataUdp(final SocketAddress client, final ByteBuffer data)
	{
		if(stopReceivingAnything)
		{
			return;
		}

		socketClient.setLastUdpsocketAddress(client);
		
		if(socketListener.startReceivingUnreliableMessage(socketServer, socketClient, data.remaining()))
		{
			socketListener.receivedUnreliableMessage(socketServer, socketClient, data);
		}
		else
		{
			// it got declined
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was prevented from sending a packet, packet type: " + SocketMessageType.format(SocketMessageType.UNRELIABLE_MESSAGE) + ", packet size: " + data.remaining() + " bytes");
			}
			disconnect();
		}
	}
	
	/**
	 * Returns null if it failed.
	 */
	protected Integer receiveInt(final ByteBuffer data)
	{
		if(!data.hasRemaining())
		{
			return null;
		}
		while(data.hasRemaining() && (receivedIntegerBuffer.position() < 4))
		{
			receivedIntegerBuffer.put(data.get());
		}
		if(receivedIntegerBuffer.position() < 4)
		{
			// not enough bytes to get the integer
			return null;
		}
		receivedIntegerBuffer.flip();
		byte b1 = receivedIntegerBuffer.get();
		byte b2 = receivedIntegerBuffer.get();
		byte b3 = receivedIntegerBuffer.get();
		byte b4 = receivedIntegerBuffer.get();
		receivedIntegerBuffer.clear();
		return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
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
		if(value < 128)
		{
			// should not be possible
			if(SocketServer.IS_DEBUGGING)
			{
				SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid uint, uints of 4 bytes can't be below 128, uint value: " + value);
			}
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
		
		if(!handshakingPacketComplete)
		{
			while(data.hasRemaining())
			{
				{// wait for header end >>
					byte b = data.get();
					if(handshakingPacket == null)
					{
						if(handshakingPacketStage == 0)
						{
							if(b == 13) // \r
							{
								handshakingPacketStage++;
							}
							else
							{
								handshakingPacket = new ByteArrayOutputStream(256);
								handshakingPacket.write(b);
							}
							continue;
						}
						if(handshakingPacketStage == 1)
						{
							if(b == 10) // \n
							{
								handshakingPacketStage++;
							}
							else
							{
								handshakingPacketStage = 0;
								handshakingPacket = new ByteArrayOutputStream(256);
								handshakingPacket.write(b);
							}
							continue;
						}
						if(handshakingPacketStage == 2)
						{
							if(b == 13) // \r
							{
								handshakingPacketStage++;
							}
							else
							{
								handshakingPacketStage = 0;
								handshakingPacket = new ByteArrayOutputStream(256);
								handshakingPacket.write(b);
							}
							continue;
						}
						if(handshakingPacketStage == 3)
						{
							handshakingPacketStage = 0;
							if(b == 10) // \n
							{
								// done
							}
							else
							{
								handshakingPacket = new ByteArrayOutputStream(256);
								handshakingPacket.write(b);
								continue;
							}
						}
					}
					else
					{
						handshakingPacket.write(b);
						if(handshakingPacket.size() > 8192) // 8 KB
						{
							// handshake packet too big
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a handshake that is too big, handshakes can't be over 8192 bytes");
							}
							disconnect();
							return;
						}
						
						if(handshakingPacketStage == 0)
						{
							if(b == 13) // \r
							{
								handshakingPacketStage++;
							}
							continue;
						}
						if(handshakingPacketStage == 1)
						{
							if(b == 10) // \n
							{
								handshakingPacketStage++;
							}
							else
							{
								handshakingPacketStage = 0;
							}
							continue;
						}
						if(handshakingPacketStage == 2)
						{
							if(b == 13) // \r
							{
								handshakingPacketStage++;
							}
							else
							{
								handshakingPacketStage = 0;
							}
							continue;
						}
						if(handshakingPacketStage == 3)
						{
							handshakingPacketStage = 0;
							if(b == 10) // \n
							{
								// done
							}
							else
							{
								continue;
							}
						}
					}
				}// wait for header end <<
				
				handshakingPacketComplete = true;
				socketServer.handshakingClientHandlers.remove(this);
				
				if(handshakingPacket == null)
				{
					// regular socket
					if(SocketServer.IS_DEBUGGING)
					{
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has completed the handshake: " + LowEntry.toJsonString("\r\n\r\n"));
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a regular socket");
					}
					socketClient.onHandshakeCompleted(false);
					break;
				}
				
				String header = LowEntry.bytesToStringLatin1(handshakingPacket.toByteArray());
				handshakingPacket = null;
				
				if(SocketServer.IS_DEBUGGING)
				{
					SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has completed the handshake: " + LowEntry.toJsonString(header));
				}
				
				int websocketKeyStartIndex = header.toLowerCase(Locale.ENGLISH).indexOf(WEBSOCKET_KEY_STRING);
				if(websocketKeyStartIndex < 0)
				{
					// regular socket
					if(SocketServer.IS_DEBUGGING)
					{
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a regular socket");
					}
					socketClient.onHandshakeCompleted(false);
				}
				
				// websocket
				if(SocketServer.IS_DEBUGGING)
				{
					SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a websocket");
				}
				
				String key;
				
				{// get key >>
					websocketKeyStartIndex += WEBSOCKET_KEY_STRING.length();
					
					int websocketKeyEndIndex = header.indexOf('\r', websocketKeyStartIndex);
					if(websocketKeyEndIndex < 0)
					{
						websocketKeyEndIndex = header.length();
					}
					
					key = header.substring(websocketKeyStartIndex, websocketKeyEndIndex).trim();
				}// get key <<
				
				{// send response >>
					String responsekey = LowEntry.bytesToBase64(LowEntry.sha1(LowEntry.stringToBytesLatin1(key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11")));
					String response = "HTTP/1.1 101 Switching Protocols\r\nUpgrade: Websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: " + responsekey + (header.contains("Sec-WebSocket-Protocol:") ? "\r\nSec-WebSocket-Protocol: binary" : "") + "\r\n\r\n";
					
					try
					{
						client.write(ByteBuffer.wrap(LowEntry.stringToBytesLatin1(response)));
					}
					catch(PyroException e)
					{
						// failed to write
						if(SocketServer.IS_DEBUGGING)
						{
							SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " can't be send a handshake response:");
							SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
						}
						disconnect();
						return;
					}
				}// send response <<
				
				websocket = new WebsocketReceivingData();
				socketClient.onHandshakeCompleted(true);
				break;
			}
		}
		
		while(data.hasRemaining())
		{
			if(websocket != null)
			{
				socketServer.clientDataBuffer.clear();
				
				while(data.hasRemaining())
				{
					{// get opcode >>
						if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_OPCODE)
						{
							byte opcode = data.get();
							opcode = LowEntry.getByteWithBitSet(opcode, 8, false); // clear FIN
							opcode = LowEntry.getByteWithBitSet(opcode, 7, false); // clear RSV1
							opcode = LowEntry.getByteWithBitSet(opcode, 6, false); // clear RSV2
							opcode = LowEntry.getByteWithBitSet(opcode, 5, false); // clear RSV3
							
							if(opcode == 8)
							{
								// CLOSE received
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent CLOSE (websocket)");
								}
								disconnect();
								return;
							}
							else if(opcode == 9)
							{
								websocket.pingPacket = new ByteArrayOutputStream();
								websocket.receivingType = WebsocketPacketType.PING;
							}
							else if(opcode == 10)
							{
								websocket.receivingType = WebsocketPacketType.PONG;
							}
							else
							{
								// 0 (continuation), 1 (text) or 2 (binary)
								websocket.receivingType = WebsocketPacketType.DATA;
							}
							
							websocket.receivingStage = WebsocketReceivingStage.RECEIVE_LENGTH_1;
							
							continue;
						}
					}// get opcode <<
					
					{// get size >>
						{// 1 byte >>
							if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_LENGTH_1)
							{
								byte b = data.get();
								
								websocket.containsMask = LowEntry.isBitSet(b, 8);
								if(websocket.containsMask)
								{
									b = LowEntry.getByteWithBitSet(b, 8, false); // clear MASK
								}
								
								websocket.sizePacket[0] = b;
								long size = LowEntry.bytesToLong(websocket.sizePacket, 0, 1);
								if(size == 126)
								{
									websocket.receivingStage = WebsocketReceivingStage.RECEIVE_LENGTH_2;
								}
								else if(size == 127)
								{
									websocket.receivingStage = WebsocketReceivingStage.RECEIVE_LENGTH_8;
								}
								else
								{
									websocket.payloadRemainingByteCount = size;
									websocket.receivingStage = WebsocketReceivingStage.RECEIVE_MASK;
								}
								
								continue;
							}
						}// 1 byte <<
						
						{// 2 bytes >>
							if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_LENGTH_2)
							{
								if((websocket.sizePacketIndex == 0) && (data.remaining() >= 2))
								{
									data.get(websocket.sizePacket, 0, 2);
								}
								else
								{
									while(data.hasRemaining() && (websocket.sizePacketIndex < 2))
									{
										websocket.sizePacket[websocket.sizePacketIndex] = data.get();
										websocket.sizePacketIndex++;
									}
									if(websocket.sizePacketIndex < 2)
									{
										return;
									}
								}
								
								websocket.payloadRemainingByteCount = LowEntry.bytesToLong(websocket.sizePacket, 0, 2);
								websocket.sizePacketIndex = 0;
								websocket.receivingStage = WebsocketReceivingStage.RECEIVE_MASK;
								
								continue;
							}
						}// 2 bytes <<
						
						{// 8 bytes >>
							if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_LENGTH_8)
							{
								if((websocket.sizePacketIndex == 0) && (data.remaining() >= 8))
								{
									data.get(websocket.sizePacket, 0, 8);
								}
								else
								{
									while(data.hasRemaining() && (websocket.sizePacketIndex < 8))
									{
										websocket.sizePacket[websocket.sizePacketIndex] = data.get();
										websocket.sizePacketIndex++;
									}
									if(websocket.sizePacketIndex < 8)
									{
										return;
									}
								}
								
								websocket.payloadRemainingByteCount = LowEntry.bytesToLong(websocket.sizePacket, 0, 8);
								websocket.sizePacketIndex = 0;
								websocket.receivingStage = WebsocketReceivingStage.RECEIVE_MASK;
								
								continue;
							}
						}// 8 bytes <<
					}// get size <<
					
					{// get mask >>
						if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_MASK)
						{
							if(websocket.containsMask)
							{
								if((websocket.maskIndex == 0) && (data.remaining() >= 4))
								{
									data.get(websocket.mask, 0, 4);
								}
								else
								{
									while(data.hasRemaining() && (websocket.maskIndex < 4))
									{
										websocket.mask[websocket.maskIndex] = data.get();
										websocket.maskIndex++;
									}
									if(websocket.maskIndex < 4)
									{
										return;
									}
								}
								websocket.maskIndex = 0;
							}
							websocket.receivingStage = WebsocketReceivingStage.RECEIVE_PAYLOAD;
							
							continue;
						}
					}// get mask <<
					
					{// get payload >>
						if(websocket.receivingStage == WebsocketReceivingStage.RECEIVE_PAYLOAD)
						{
							{// data >>
								if(websocket.receivingType == WebsocketPacketType.DATA)
								{
									if(websocket.payloadRemainingByteCount <= 0)
									{
										websocket.receivingStage = WebsocketReceivingStage.RECEIVE_OPCODE;
									}
									else
									{
										while(data.hasRemaining() && (websocket.payloadRemainingByteCount > 0))
										{
											byte b = data.get();
											websocket.payloadRemainingByteCount--;
											
											if(websocket.containsMask)
											{
												b ^= websocket.mask[websocket.maskIndex];
												
												if(websocket.maskIndex == 3)
												{
													websocket.maskIndex = 0;
												}
												else
												{
													websocket.maskIndex++;
												}
											}
											
											socketServer.clientDataBuffer.put(b);
										}
										if(websocket.payloadRemainingByteCount <= 0)
										{
											websocket.maskIndex = 0;
											websocket.receivingStage = WebsocketReceivingStage.RECEIVE_OPCODE;
										}
										break;
									}
									
									continue;
								}
							}// data <<
							
							{// ping >>
								if(websocket.receivingType == WebsocketPacketType.PING)
								{
									while(data.hasRemaining() && (websocket.payloadRemainingByteCount > 0))
									{
										byte b = data.get();
										websocket.payloadRemainingByteCount--;
										
										if(websocket.containsMask)
										{
											b ^= websocket.mask[websocket.maskIndex];
											
											if(websocket.maskIndex == 3)
											{
												websocket.maskIndex = 0;
											}
											else
											{
												websocket.maskIndex++;
											}
										}
										
										websocket.pingPacket.write(b);
										
										if(websocket.pingPacket.size() > 8192) // 8 KB
										{
											// ping packet too big
											if(SocketServer.IS_DEBUGGING)
											{
												SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a ping (websocket) that is too big, pings can't be over 8192 bytes");
											}
											disconnect();
											return;
										}
									}
									if(websocket.payloadRemainingByteCount <= 0)
									{
										byte[] packet = websocket.pingPacket.toByteArray();
										int size = packet.length;
										byte opcode = -128 | 10; // FIN + PONG
										
										ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.websocketSizeByteCount(size) + packet.length);
										buffer.put(opcode);
										SocketFunctions.putWebsocketSizeBytes(buffer, size);
										buffer.put(packet);
										
										try
										{
											client.write(buffer);
										}
										catch(PyroException e)
										{
											// failed to write
											if(SocketServer.IS_DEBUGGING)
											{
												SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " can't be send data:");
												SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
											}
											disconnect();
											return;
										}
										
										websocket.pingPacket = null;
										websocket.maskIndex = 0;
										websocket.receivingStage = WebsocketReceivingStage.RECEIVE_OPCODE;
									}
									
									continue;
								}
							}// ping <<
							
							{// pong and everything else >>
								//if(websocket.receivingType == WebsocketPacketType.PONG)
								{
									while(data.hasRemaining() && (websocket.payloadRemainingByteCount > 0))
									{
										data.get();
										websocket.payloadRemainingByteCount--;
									}
									if(websocket.payloadRemainingByteCount <= 0)
									{
										websocket.receivingStage = WebsocketReceivingStage.RECEIVE_OPCODE;
									}
								}
							}// pong and everything else <<
						}
					}// get payload <<
				}
				
				socketServer.clientDataBuffer.flip();
				if(!socketServer.clientDataBuffer.hasRemaining())
				{
					return;
				}
			}
			
			
			final ByteBuffer d = ((websocket == null) ? data : socketServer.clientDataBuffer);
			
			
			while(d.hasRemaining())
			{
				{// get server UDP port >>
					if(!hasReceivedServerUdpPort)
					{
						Integer receivedInt = receiveInt(d);
						if(receivedInt == null)
						{
							return;
						}
						hasReceivedServerUdpPort = true;
						
						if(receivedInt < 0)
						{
							// received invalid UDP port
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid server UDP port: " + receivedInt);
							}
							disconnect();
							return;
						}
						
						if((receivedInt == 0) && (socketServer.serverUdp != null))
						{
							// client didn't try to connect over UDP, even though the server is listening for UDP
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " didn't try to connect over UDP, even though the server is listening for UDP");
							}
							disconnect();
							return;
						}
						else if((receivedInt > 0) && (socketServer.serverUdp == null))
						{
							// client tries to connect over UDP, even though the server isn't listening for UDP
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " tried to connect over UDP, even though the server isn't listening for UDP");
							}
							disconnect();
							return;
						}
						else if(receivedInt != socketServer.getPortUdp())
						{
							// client tries to connect over UDP, but connected to the wrong port
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " tried to connect over UDP, but tried to connect to the wrong port (expected " + socketServer.getPortUdp() + ", received " + receivedInt + ")");
							}
							disconnect();
							return;
						}
					}
				}// get server UDP port <<
				
				{// get client UDP port >>
					if(!hasReceivedClientUdpPort)
					{
						Integer receivedInt = receiveInt(d);
						if(receivedInt == null)
						{
							return;
						}
						hasReceivedClientUdpPort = true;
						
						if(receivedInt < 0)
						{
							// received invalid UDP port
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid client UDP port: " + receivedInt);
							}
							disconnect();
							return;
						}
						
						if(websocket != null)
						{
							socketClient.setRemoteUdpPort(0);
						}
						else
						{
							if((receivedInt == 0) && (socketServer.serverUdp != null))
							{
								// client didn't try to connect over UDP, even though the server is listening for UDP
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " didn't try to connect over UDP, even though the server is listening for UDP");
								}
								disconnect();
								return;
							}
							else if((receivedInt > 0) && (socketServer.serverUdp == null))
							{
								// client tries to connect over UDP, even though the server isn't listening for UDP
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " tried to connect over UDP, even though the server isn't listening for UDP");
								}
								disconnect();
								return;
							}
							socketClient.setRemoteUdpPort(receivedInt);
						}
						
//						socketServer.addUdpClient(this);
					}
				}// get client UDP port <<
				
				{// get type >>
					if(receivingStage == ReceivingStage.RECEIVE_TYPE)
					{
						if(!d.hasRemaining())
						{
							return;
						}
						receivingType = d.get();
						
						if(receivingType == SocketMessageType.SIMULATED_UNRELIABLE_MESSAGE)
						{
							if(websocket == null)
							{
								// received simulated unreliable message when not needed
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a simulated unreliable message while not being a websocket");
								}
								disconnect();
								return;
							}
							if(socketServer.serverUdp == null)
							{
								// received simulated unreliable message when not listening for UDP
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a simulated unreliable message while the server isn't listening for UDP");
								}
								disconnect();
								return;
							}
							receivingStage = ReceivingStage.RECEIVE_PACKET_SIZE;
						}
						else if(receivingType == SocketMessageType.MESSAGE)
						{
							receivingStage = ReceivingStage.RECEIVE_PACKET_SIZE;
						}
						else if(receivingType == SocketMessageType.FUNCTION_CALL)
						{
							receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
						}
						else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL)
						{
							receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
						}
						else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_CANCELED)
						{
							receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
						}
						else if(receivingType == SocketMessageType.CONNECTION_VALIDATION)
						{
							receivingStage = ReceivingStage.RECEIVE_FUNCTION_CALL_ID;
						}
						else
						{
							// received invalid type
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid packet type, receiving stage: RECEIVE_TYPE, packet type: " + SocketMessageType.format(receivingType));
							}
							disconnect();
							return;
						}
					}
				}// get type <<
				
				{// get function call id >>
					if(receivingStage == ReceivingStage.RECEIVE_FUNCTION_CALL_ID)
					{
						int receivedUint = receiveUint(d);
						if(receivedUint < 0)
						{
							return;
						}
						receivingFunctionCallId = receivedUint;
						
						if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL_CANCELED)
						{
							{// cancel latent response >>
								LatentResponse response;
								synchronized(latentResponses)
								{
									response = latentResponses.remove(receivingFunctionCallId);
								}
								if(response != null)
								{
									response.canceledByClient();
								}
							}// cancel latent response <<
							
							receivingStage = ReceivingStage.RECEIVE_TYPE;
						}
						else if(receivingType == SocketMessageType.CONNECTION_VALIDATION)
						{
							socketClient.sendConnectionValidationResponse(receivingFunctionCallId);
							socketListener.receivedConnectionValidation(socketServer, socketClient);
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
						int receivedUint = receiveUint(d);
						if(receivedUint < 0)
						{
							return;
						}
						receivingPacketSize = receivedUint;
						
						boolean startReceiving;
						if(receivingType == SocketMessageType.SIMULATED_UNRELIABLE_MESSAGE)
						{
							startReceiving = socketListener.startReceivingUnreliableMessage(socketServer, socketClient, receivingPacketSize);
						}
						else if(receivingType == SocketMessageType.MESSAGE)
						{
							startReceiving = socketListener.startReceivingMessage(socketServer, socketClient, receivingPacketSize);
						}
						else if(receivingType == SocketMessageType.FUNCTION_CALL)
						{
							startReceiving = socketListener.startReceivingFunctionCall(socketServer, socketClient, receivingPacketSize);
						}
						else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL)
						{
							startReceiving = socketListener.startReceivingLatentFunctionCall(socketServer, socketClient, receivingPacketSize);
						}
						else
						{
							// received invalid type
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid packet type, receiving stage: RECEIVE_PACKET_SIZE, packet type: " + SocketMessageType.format(receivingType));
							}
							disconnect();
							return;
						}
						
						if(!startReceiving)
						{
							// it got declined
							if(SocketServer.IS_DEBUGGING)
							{
								SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has been prevented from sending a packet, packet type: " + SocketMessageType.format(receivingType) + ", packet size: " + receivingPacketSize + " bytes");
							}
							disconnect();
							return;
						}
						
						receivingPacket = new byte[receivingPacketSize];
						receivingStage = ReceivingStage.RECEIVE_PACKET;
					}
				}// get packet size <<
				
				{// receiving packet >>
					if(receivingStage == ReceivingStage.RECEIVE_PACKET)
					{
						if(!d.hasRemaining())
						{
							return;
						}
						
						int packetBytesRemaining = receivingPacketSize - receivingPacketPosition;
						int length = d.remaining();
						if(length <= packetBytesRemaining)
						{
							d.get(receivingPacket, receivingPacketPosition, length);
							receivingPacketPosition += length;
						}
						else
						{
							d.get(receivingPacket, receivingPacketPosition, packetBytesRemaining);
							receivingPacketPosition += packetBytesRemaining;
						}
						
						if(receivingPacketPosition >= receivingPacketSize) // packetBytesRemaining <= 0
						{
							if(receivingType == SocketMessageType.SIMULATED_UNRELIABLE_MESSAGE)
							{
								socketListener.receivedUnreliableMessage(socketServer, socketClient, ByteBuffer.wrap(receivingPacket));
							}
							else if(receivingType == SocketMessageType.MESSAGE)
							{
								socketListener.receivedMessage(socketServer, socketClient, receivingPacket);
							}
							else if(receivingType == SocketMessageType.FUNCTION_CALL)
							{
								byte[] returnBytes = socketListener.receivedFunctionCall(socketServer, socketClient, receivingPacket);
								socketClient.sendFunctionCallResponse(receivingFunctionCallId, returnBytes);
							}
							else if(receivingType == SocketMessageType.LATENT_FUNCTION_CALL)
							{
								socketListener.receivedLatentFunctionCall(socketServer, socketClient, receivingPacket, new LatentResponseImplementation(latentResponses, socketClient, receivingFunctionCallId));
							}
							else
							{
								// received invalid type
								if(SocketServer.IS_DEBUGGING)
								{
									SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent an invalid packet type, receiving stage: RECEIVE_PACKET, packet type: " + SocketMessageType.format(receivingType));
								}
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
