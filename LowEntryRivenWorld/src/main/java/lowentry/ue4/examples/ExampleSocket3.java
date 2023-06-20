package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.LatentFunctionCall;
import lowentry.ue4.classes.sockets.SocketConnection;
import lowentry.ue4.classes.sockets.SocketConnection.FunctionCallListener;
import lowentry.ue4.classes.sockets.SocketConnection.LatentFunctionCallListener;
import lowentry.ue4.classes.sockets.SocketConnectionListener;
import lowentry.ue4.library.LowEntry;


public class ExampleSocket3
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
				connection.sendMessage(LowEntry.stringToBytesUtf8("hello server!"));
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
		
		
		final SocketConnection connection = new SocketConnection("localhost", 7780, 7880, listener); // you can remove the 2nd port (which is the UDP port) to not connect to the server over UDP. By the way, know that you always have to connect over UDP if the server listens for UDP, otherwise the server will disconnect you.
		if(!connection.connect())
		{
			System.out.println("Failed to connect");
			System.exit(1);
		}
		
		
		connection.sendMessage(LowEntry.stringToBytesUtf8("test message"));
		connection.sendUnreliableMessage(LowEntry.stringToBytesUtf8("test udp message"));
		
		
		connection.sendFunctionCall(LowEntry.stringToBytesUtf8("test function call"), new FunctionCallListener()
		{
			@Override
			public void receivedResponse(final SocketConnection connection, final byte[] bytes)
			{
				// this function is called when your function call receives a response
				System.out.println("[" + Thread.currentThread().getName() + "] Received Function Call Response: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			@Override
			public void failed(final SocketConnection connection)
			{
				// this function is called when your function call fails, by for example timing out or by the connection closing
				System.out.println("[" + Thread.currentThread().getName() + "] Function Call failed");
			}
		});
		
		
		final LatentFunctionCall latentFunctionCall = connection.sendLatentFunctionCall(LowEntry.stringToBytesUtf8("test latent function call"), new LatentFunctionCallListener()
		{
			@Override
			public void receivedResponse(final SocketConnection connection, final byte[] bytes)
			{
				// this function is called when your function call receives a response
				System.out.println("[" + Thread.currentThread().getName() + "] Received Latent Function Call Response: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			@Override
			public void canceled(final SocketConnection connection)
			{
				// this function is called when your function call has been canceled
				System.out.println("[" + Thread.currentThread().getName() + "] Latent Function Call canceled");
			}
			
			@Override
			public void failed(final SocketConnection connection)
			{
				// this function is called when your function call fails, by for example timing out or by the connection closing
				System.out.println("[" + Thread.currentThread().getName() + "] Latent Function Call failed");
			}
		});
		
		new Thread(() ->
		{
			LowEntry.sleep(10 * 1000); // 10 seconds
			latentFunctionCall.cancel();
		}).start();
		
		
		while(connection.isConnected())
		{
			connection.listen();
		}
	}
}
