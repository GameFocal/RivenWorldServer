package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;


public class ExampleSocket5
{
	public static void main(final String[] args) throws Throwable
	{
		/*
		  This examples shows you how to set up latent function calls.
		 */
		
		SocketServerListener listener = new SocketServerListener()
		{
			@Override
			public void clientConnected(final SocketServer server, final SocketClient client)
			{
			}
			@Override
			public void clientDisconnected(final SocketServer server, final SocketClient client)
			{
			}
			
			
			@Override
			public void receivedConnectionValidation(SocketServer server, SocketClient client)
			{
			}
			
			
			@Override
			public boolean startReceivingUnreliableMessage(SocketServer server, SocketClient client, int bytes)
			{
				return false;
			}
			@Override
			public void receivedUnreliableMessage(SocketServer server, SocketClient client, ByteBuffer bytes)
			{
			}
			
			
			@Override
			public boolean startReceivingMessage(final SocketServer server, final SocketClient client, final int bytes)
			{
				return false;
			}
			@Override
			public void receivedMessage(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
			}
			
			
			@Override
			public boolean startReceivingFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				return false;
			}
			@Override
			public byte[] receivedFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				return null;
			}
			
			
			@Override
			public boolean startReceivingLatentFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				return true;
			}
			@Override
			public void receivedLatentFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes, final LatentResponse response)
			{
				System.out.println("received a latent function call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
				
				response.setOnCanceled(() ->
				{
					/*
					  This code will be executed if the latent function call gets canceled before response.done() has been called.
					 
					  When the latent function call has been canceled, response.done() won't do anything.
					 */
					
					System.out.println("the latent function call has been canceled!");
				});
				
				new Thread(() ->
				{
					/*
					  This thread will sleep for 5 seconds, then the server will send a reply to the client, existing of a string with the value "success!".
					 
					  This is just an example of a delayed response.done() action, it doesn't necessarily have to happen in another thread.
					  You could also, for example, run response.done() immediately, or in the main loop, etc.
					 
					  One last thing, canceling is primarily for the client side, so that clients can interrupt latent function calls.
					  Normally there is no reason to use this on the server side, since a latent function call either succeeds or fails then.
					  But you can, if you can find any reason to, cancel latent function calls on the server side as well, you can do this by calling response.cancel().
					 */
					
					LowEntry.sleep(5 * 1000); // 5 seconds
					response.done(LowEntry.stringToBytesUtf8("success!"));
					
					if(response.isDone())
					{
						System.out.println("the latent function call has been completed!");
					}
					else
					{
						System.out.println("the latent function call has already been canceled!");
					}
				}).start();
			}
		};
		
		SocketServer server = new SocketServer(false, 7780, 7880, listener);
		System.out.println("Listening: " + server);
		
		while(true)
		{
			server.listen();
		}
	}
}
