package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;


public class ExampleSocket1
{
	public static void main(final String[] args) throws Throwable
	{
		SocketServerListener listener = new SocketServerListener()
		{
			@Override
			public void clientConnected(final SocketServer server, final SocketClient client)
			{
				// this function is called when a client connects
				System.out.println("[" + Thread.currentThread().getName() + "] ClientConnected: " + client);
				client.sendMessage(LowEntry.stringToBytesUtf8("hello newly connected client!"));
				
				for(SocketClient c : server)
				{
					if(client != c)
					{
						c.sendMessage(LowEntry.stringToBytesUtf8("another client connected!"));
					}
				}
			}
			
			@Override
			public void clientDisconnected(final SocketServer server, final SocketClient client)
			{
				// this function is called after a client disconnects
				System.out.println("[" + Thread.currentThread().getName() + "] ClientDisconnected: " + client);
				
				for(SocketClient c : server)
				{
					c.sendMessage(LowEntry.stringToBytesUtf8("another client disconnected!"));
				}
			}
			
			
			@Override
			public void receivedConnectionValidation(SocketServer server, SocketClient client)
			{
				// this function is called when a client is validating the connection
			}
			
			
			@Override
			public boolean startReceivingUnreliableMessage(SocketServer server, SocketClient client, int bytes)
			{
				// this function is called when you start receiving an unreliable (UDP) message packet
				// return true to start receiving the packet, return false to disconnect the client
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Unreliable Message");
				return (bytes <= 1024);
			}
			
			@Override
			public void receivedUnreliableMessage(SocketServer server, SocketClient client, ByteBuffer bytes)
			{
				// this function is called when you have received an unreliable (UDP) message packet
				// PS: normally you would read the data in with LowEntry.readByteData(bytes), for maximum performance
				System.out.println("[" + Thread.currentThread().getName() + "] Received Unreliable Message: \"" + LowEntry.bytesToStringUtf8(LowEntry.getBytesFromByteBuffer(bytes)) + "\"");
			}
			
			
			@Override
			public boolean startReceivingMessage(final SocketServer server, final SocketClient client, final int bytes)
			{
				// this function is called when you start receiving a message packet
				// return true to start receiving the packet, return false to disconnect the client
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Message");
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public void receivedMessage(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				// this function is called when you have received a message packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			
			@Override
			public boolean startReceivingFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				// this function is called when you start receiving a function call packet
				// return true to start receiving the packet, return false to disconnect the client
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Function Call");
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public byte[] receivedFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				// this function is called when you have received a function call packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
				return null;
			}
			
			
			@Override
			public boolean startReceivingLatentFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				// this function is called when you start receiving a latent function call packet
				// return true to start receiving the packet, return false to disconnect the client
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Latent Function Call");
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public void receivedLatentFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes, final LatentResponse response)
			{
				// this function is called when you have received a latent function call packet
				System.out.println("[" + Thread.currentThread().getName() + "] Received Latent Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
				response.done(null);
			}
		};
		
		//SocketServer.setDebuggingEnabled(); // uncomment to enable debugging
		
		SocketServer server = new SocketServer(false, 7780, listener); // change the boolean to true if you want to accept external connections (true, 7780, 7880, listener), remove the second port to not listen to UDP (false, 7780, listener), combinations of these 2 switches are possible
		System.out.println("Listening: " + server);
		
		while(true)
		{
			server.listen();
		}
	}
}
