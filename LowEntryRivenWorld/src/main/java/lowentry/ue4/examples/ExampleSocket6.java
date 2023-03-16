package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.LatentFunctionCall;
import lowentry.ue4.classes.sockets.SocketConnection;
import lowentry.ue4.classes.sockets.SocketConnection.FunctionCallListener;
import lowentry.ue4.classes.sockets.SocketConnection.LatentFunctionCallListener;
import lowentry.ue4.classes.sockets.ThreadedSocketConnection;
import lowentry.ue4.classes.sockets.ThreadedSocketConnectionListener;
import lowentry.ue4.library.LowEntry;


public class ExampleSocket6
{
	public static void main(final String[] args) throws Throwable
	{
		// the listener can be a normal SocketConnectionListener or a ThreadedSocketConnectionListener,
		// the ThreadedSocketConnectionListener will also give you a reference to the ThreadedSocketConnection in the listener methods
		ThreadedSocketConnectionListener listener = new ThreadedSocketConnectionListener()
		{
			@Override
			public void connected(final ThreadedSocketConnection threadedConnection, final SocketConnection connection)
			{
				// this function is called when the connection opens
				System.out.println("[" + Thread.currentThread().getName() + "] Connected: " + connection);
				connection.sendMessage(LowEntry.stringToBytesUtf8("hello server!"));
			}
			
			@Override
			public void disconnected(final ThreadedSocketConnection threadedConnection, final SocketConnection connection)
			{
				// this function is called after the connection closes
				System.out.println("[" + Thread.currentThread().getName() + "] Disconnected: " + connection);
			}
			
			@Override
			public void receivedUnreliableMessage(final ThreadedSocketConnection threadedConnection, SocketConnection connection, byte[] bytes)
			{
				// this function is called when you have received an unreliable (UDP message packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Unreliable Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			@Override
			public void receivedMessage(final ThreadedSocketConnection threadedConnection, final SocketConnection connection, final byte[] bytes)
			{
				// this function is called when you have received a message packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
		};
		
		
		final ThreadedSocketConnection connection = new ThreadedSocketConnection("localhost", 7780, 7880, listener);
		connection.start();
		if(!connection.isConnected())
		{
			System.out.println("Failed to connect");
			System.exit(1);
		}
		
		
		connection.sendMessage(LowEntry.stringToBytesUtf8("test message"));
		
		
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
			// this synchronizes all events with this thread, so all your code will be single-threaded and thread-safe
			// this should be called in a loop, like after a SocketServer.listen() for example
			LowEntry.sleep(100);
			connection.executePendingTasks();
		}
		
		
		// the threaded connection will automatically reconnect (after 500ms) after disconnecting
		// we don't care about that in this example, we just quit the application
		
		
		connection.stop(); // you have to call stop, else the application will never end
		System.out.println("called stop");
		
		connection.executePendingTasks(); // executing the last tasks
	}
}
