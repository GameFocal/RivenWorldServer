package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.SocketConnection;
import lowentry.ue4.classes.sockets.SocketConnectionListener;
import lowentry.ue4.library.LowEntry;


public class ExampleSocket7
{
	public static void main(final String[] args) throws Throwable
	{
		SocketConnectionListener listener = new SocketConnectionListener()
		{
			@Override
			public void connected(final SocketConnection connection)
			{
				// this function is called when the connection opens
				System.out.println("[" + Thread.currentThread().getName() + "] Connected: " + connection);
			}
			
			@Override
			public void disconnected(final SocketConnection connection)
			{
				// this function is called after the connection closes
				System.out.println("[" + Thread.currentThread().getName() + "] Disconnected: " + connection);
			}
			
			@Override
			public void receivedUnreliableMessage(final SocketConnection connection, final byte[] bytes)
			{
				// this function is called when you have received a message packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Unreliable Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			@Override
			public void receivedMessage(final SocketConnection connection, final byte[] bytes)
			{
				// this function is called when you have received a message packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
		};
		
		
		final SocketConnection connection = new SocketConnection("localhost", 7780, 7880, listener);
		if(!connection.connect())
		{
			System.out.println("Failed to connect");
			System.exit(1);
		}
		
		
		Thread.sleep(1000); // it usually takes some time before UDP becomes ready
		
		
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 1"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 2"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 3"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 4"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 5"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message 6"));
		
		
		long startTime = LowEntry.millis();
		while(connection.isConnected())
		{
			connection.listen();
			
			if((LowEntry.millis() - startTime) >= 5000) // disconnect after 5000 ms
			{
				connection.disconnect();
			}
		}
	}
}
