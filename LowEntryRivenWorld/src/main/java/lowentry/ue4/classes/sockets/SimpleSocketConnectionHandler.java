package lowentry.ue4.classes.sockets;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroClientListener;

import java.io.IOException;
import java.nio.ByteBuffer;


public class SimpleSocketConnectionHandler implements PyroClientListener
{
	protected enum ConnectingStage
	{
		WAITING, UNCONNECTABLE, CONNECTED
	}
	
	
	protected final SimpleSocketConnectionListener socketListener;
	protected final SimpleSocketConnection         connection;
	
	protected ConnectingStage connectingStage = ConnectingStage.WAITING;
	
	
	protected boolean socketListenerCalledConnected = false;
	
	
	protected boolean stopReceivingAnything = false;
	
	protected byte[] receivingPacket         = null;
	protected int    receivingPacketPosition = 0;
	
	protected ByteBuffer receivedIntegerBuffer = ByteBuffer.allocate(4);
	
	
	public SimpleSocketConnectionHandler(final SimpleSocketConnectionListener socketListener, final SimpleSocketConnection connection)
	{
		this.socketListener = socketListener;
		this.connection = connection;
	}
	
	
	@Override
	public void unconnectableClient(final PyroClient client)
	{
		if(connectingStage == ConnectingStage.WAITING)
		{
			connectingStage = ConnectingStage.UNCONNECTABLE;
		}
	}
	
	@Override
	public void connectedClient(final PyroClient client)
	{
		if(connectingStage == ConnectingStage.WAITING)
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
		if(socketListenerCalledConnected)
		{
			socketListener.disconnected(connection);
		}
	}
	
	@Override
	public void disconnectedClient(final PyroClient client)
	{
		stopReceivingAnything = true;
		receivingPacket = null;
		receivedIntegerBuffer = null;
		if(socketListenerCalledConnected)
		{
			socketListener.disconnected(connection);
		}
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
		connection.disconnect();
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
				
				if(receivingPacketSize < 0)// || !socketListener.canReceivePacket(connection, receivingPacketSize))
				{
					// it got declined
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
					socketListener.receivedPacket(connection, receivingPacket);
					receivingPacket = null;
					receivingPacketPosition = 0;
				}
			}
		}
	}
}
