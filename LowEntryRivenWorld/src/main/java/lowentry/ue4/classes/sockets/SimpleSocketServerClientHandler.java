package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.classes.sockets.SimpleSocketServerClientHandler.WebsocketReceivingData.WebsocketPacketType;
import lowentry.ue4.classes.sockets.SimpleSocketServerClientHandler.WebsocketReceivingData.WebsocketReceivingStage;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Locale;


public class SimpleSocketServerClientHandler implements PyroClientListener
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
		
		
		public       WebsocketReceivingStage receivingStage                 = WebsocketReceivingStage.RECEIVE_OPCODE;
		public       WebsocketPacketType     receivingType                  = WebsocketPacketType.DATA;
		public final byte[]                  sizePacket                     = new byte[8];
		public       byte                    sizePacketIndex                = 0;
		public       boolean                 containsMask                   = false;
		public final byte[]                  mask                           = new byte[4];
		public       byte                    maskIndex                      = 0;
		public       long                    payloadRemainingByteCount      = 0;
		public       boolean                 receivingContinuationPacket    = false;
		public       LinkedList<byte[]>      continuationPackets            = null;
		public       long                    continuationPacketsTotalLength = 0;
		public       ByteArrayOutputStream   pingPacket                     = null;
	}
	
	
	private static final String WEBSOCKET_KEY_STRING = "Sec-WebSocket-Key:".toLowerCase(Locale.ENGLISH);
	
	
	protected final SimpleSocketServerListener socketListener;
	protected final SimpleSocketServer         socketServer;
	protected final SimpleSocketClient         socketClient;
	
	protected boolean stopReceivingAnything = false;
	
	protected final long                  handshakingStartTime      = CachedTime.millisSinceStart();
	protected       boolean               handshakingPacketComplete = false;
	protected       ByteArrayOutputStream handshakingPacket         = null;
	protected       byte                  handshakingPacketStage    = 0;
	
	protected WebsocketReceivingData websocket = null;
	
	protected byte[] receivingPacket         = null;
	protected int    receivingPacketPosition = 0;
	
	protected ByteBuffer receivedIntegerBuffer = ByteBuffer.allocate(4);
	
	protected final int hashCode;
	
	
	public SimpleSocketServerClientHandler(final SimpleSocketServerListener socketListener, final SimpleSocketServer socketServer, final SimpleSocketClient socketClient)
	{
		this.socketListener = socketListener;
		this.socketServer = socketServer;
		this.socketClient = socketClient;
		this.hashCode = super.hashCode();
	}
	
	
	@Override
	public void unconnectableClient(final PyroClient client)
	{
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was unconnectable");
		}
	}
	
	@Override
	public void connectedClient(final PyroClient client)
	{
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			socketClient.saveAddressText();
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has connected");
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
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was dropped:");
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(cause));
		}
		hasDisconnected();
	}
	@Override
	public void disconnectedClient(final PyroClient client)
	{
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was disconnected");
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
		
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		websocket = null;
		
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
	
	private boolean checkWebsocketPacketLength()
	{
		long totalLength = websocket.continuationPacketsTotalLength + websocket.payloadRemainingByteCount;
		if(totalLength > Integer.MAX_VALUE)
		{
			// can't handle packet that large
			if(SimpleSocketServer.IS_DEBUGGING)
			{
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was prevented from sending a packet, packet size: " + totalLength + " bytes, it's larger than the maximum Java can handle (" + Integer.MAX_VALUE + ")");
			}
			disconnect();
			return true;
		}
		int receivingPacketSizeSoFar = (int) totalLength;
		if((receivingPacketSizeSoFar < 0) || !socketListener.canReceivePacket(socketServer, socketClient, receivingPacketSizeSoFar))
		{
			// it got declined
			if(SimpleSocketServer.IS_DEBUGGING)
			{
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was prevented from sending a packet, packet size: " + receivingPacketSizeSoFar + " bytes");
			}
			disconnect();
			return true;
		}
		receivingPacket = new byte[(int) websocket.payloadRemainingByteCount];
		return false;
	}
	
	private boolean receiveWebsocketPacket()
	{
		if(websocket.receivingContinuationPacket)
		{
			if(websocket.continuationPackets == null)
			{
				websocket.continuationPackets = new LinkedList<>();
			}
			websocket.continuationPackets.add(receivingPacket);
			websocket.continuationPacketsTotalLength += receivingPacket.length;
		}
		else
		{
			if(websocket.continuationPackets != null)
			{
				long totalLength = websocket.continuationPacketsTotalLength + receivingPacket.length;
				if(totalLength > Integer.MAX_VALUE)
				{
					// can't handle packet that large
					if(SimpleSocketServer.IS_DEBUGGING)
					{
						SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " was prevented from sending a packet, packet size: " + totalLength + " bytes, it's larger than the maximum Java can handle (" + Integer.MAX_VALUE + ")");
					}
					disconnect();
					return true;
				}
				websocket.continuationPackets.add(receivingPacket);
				receivingPacket = LowEntry.mergeBytes(websocket.continuationPackets);
				websocket.continuationPackets = null;
				websocket.continuationPacketsTotalLength = 0;
			}
			socketListener.receivedPacket(socketServer, socketClient, receivingPacket);
		}
		receivingPacket = null;
		receivingPacketPosition = 0;
		return false;
	}
	
	@Override
	public void receivedData(final PyroClient client, ByteBuffer data)
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
							if(SimpleSocketServer.IS_DEBUGGING)
							{
								SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a handshake that is too big, handshakes can't be over 8192 bytes");
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
					if(SimpleSocketServer.IS_DEBUGGING)
					{
						SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has completed the handshake: " + LowEntry.toJsonString("\r\n\r\n"));
						SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a regular socket");
					}
					socketClient.onHandshakeCompleted(false);
					break;
				}
				
				String header = LowEntry.bytesToStringLatin1(handshakingPacket.toByteArray());
				handshakingPacket = null;
				
				if(SimpleSocketServer.IS_DEBUGGING)
				{
					SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has completed the handshake: " + LowEntry.toJsonString(header));
				}
				
				int websocketKeyStartIndex = header.toLowerCase(Locale.ENGLISH).indexOf(WEBSOCKET_KEY_STRING);
				if(websocketKeyStartIndex < 0)
				{
					// regular socket
					if(SimpleSocketServer.IS_DEBUGGING)
					{
						SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a regular socket");
					}
					socketClient.onHandshakeCompleted(false);
					break;
				}
				
				// websocket
				if(SimpleSocketServer.IS_DEBUGGING)
				{
					SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " is a websocket");
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
						if(SimpleSocketServer.IS_DEBUGGING)
						{
							SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " can't be send a handshake response:");
							SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
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
		
		if(websocket != null)
		{
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
							if(SimpleSocketServer.IS_DEBUGGING)
							{
								SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent CLOSE (websocket)");
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
							websocket.receivingContinuationPacket = (opcode == 0);
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
								if(websocket.receivingType == WebsocketPacketType.DATA)
								{
									if(checkWebsocketPacketLength())
									{
										return;
									}
								}
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
							if(websocket.receivingType == WebsocketPacketType.DATA)
							{
								if(checkWebsocketPacketLength())
								{
									return;
								}
							}
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
							if(websocket.receivingType == WebsocketPacketType.DATA)
							{
								if(checkWebsocketPacketLength())
								{
									return;
								}
							}
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
									if(receiveWebsocketPacket())
									{
										return;
									}
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
										
										receivingPacket[receivingPacketPosition] = b;
										receivingPacketPosition++;
									}
									if(websocket.payloadRemainingByteCount <= 0)
									{
										websocket.maskIndex = 0;
										if(receiveWebsocketPacket())
										{
											return;
										}
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
										if(SimpleSocketServer.IS_DEBUGGING)
										{
											SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has sent a ping (websocket) that is too big, pings can't be over 8192 bytes");
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
										if(SimpleSocketServer.IS_DEBUGGING)
										{
											SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " can't be send data:");
											SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
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
		}
		else
		{
			while(data.hasRemaining())
			{
				if(receivingPacket == null)
				{
					while(data.hasRemaining() && (receivedIntegerBuffer.position() < 4))
					{
						receivedIntegerBuffer.put(data.get());
					}
					if(receivedIntegerBuffer.position() < 4)
					{
						// not enough bytes to get the integer
						return;
					}
					receivedIntegerBuffer.flip();
					byte b1 = receivedIntegerBuffer.get();
					byte b2 = receivedIntegerBuffer.get();
					byte b3 = receivedIntegerBuffer.get();
					byte b4 = receivedIntegerBuffer.get();
					receivedIntegerBuffer.clear();
					int receivingPacketSize = ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
					
					if((receivingPacketSize < 0) || !socketListener.canReceivePacket(socketServer, socketClient, receivingPacketSize))
					{
						// it got declined
						if(SimpleSocketServer.IS_DEBUGGING)
						{
							SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + socketClient + " has been prevented from sending a packet, packet size: " + receivingPacketSize + " bytes");
						}
						disconnect();
						return;
					}
					if(receivingPacketSize == 0)
					{
						// skip receiving empty packets
						continue;
					}
					receivingPacket = new byte[receivingPacketSize];
				}
				else
				{
					int packetBytesRemaining = receivingPacket.length - receivingPacketPosition;
					int length = data.remaining();
					if(length <= packetBytesRemaining)
					{
						data.get(receivingPacket, receivingPacketPosition, length);
						receivingPacketPosition += length;
					}
					else
					{
						data.get(receivingPacket, receivingPacketPosition, packetBytesRemaining);
						receivingPacketPosition += packetBytesRemaining;
					}
					
					if(receivingPacketPosition >= receivingPacket.length) // packetBytesRemaining <= 0
					{
						socketClient.sendPacket(new byte[0]);
						socketListener.receivedPacket(socketServer, socketClient, receivingPacket);
						receivingPacket = null;
						receivingPacketPosition = 0;
					}
				}
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
