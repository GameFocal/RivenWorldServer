package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;

import java.nio.ByteBuffer;


public class ExampleSocket2
{
	/**
	 * This is the class of the object that will be attached to every client.
	 * With this you can keep track of data specific to each client.
	 *
	 * Usually you would put this in a separate file by the way,
	 * and usually you would use getters and setters instead of public variables.
	 */
	public static class ClientSession
	{
		public boolean isAuthenticated = false;
	}
	
	
	public static void main(final String[] args) throws Throwable
	{
		SocketServerListener listener = new SocketServerListener()
		{
			@Override
			public void clientConnected(final SocketServer server, final SocketClient client)
			{
				// initialize the attachment object
				client.setAttachment(new ClientSession());
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
				// don't allow UDP packets
				return false;
			}
			
			@Override
			public void receivedUnreliableMessage(SocketServer server, SocketClient client, ByteBuffer bytes)
			{
			}
			
			
			@Override
			public boolean startReceivingMessage(final SocketServer server, final SocketClient client, final int bytes)
			{
				// only allow 200B and less when the client hasn't authenticated yet
				
				ClientSession session = client.getAttachment();
				if(!session.isAuthenticated)
				{
					return (bytes <= 200); // this will only allow packets of 200B and less
				}
				
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public void receivedMessage(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
			}
			
			
			@Override
			public boolean startReceivingFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				// only allow 200B and less when the client hasn't authenticated yet
				
				ClientSession session = client.getAttachment();
				if(!session.isAuthenticated)
				{
					return (bytes <= 200); // this will only allow packets of 200B and less
				}
				
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public byte[] receivedFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				return null;
			}
			
			
			@Override
			public boolean startReceivingLatentFunctionCall(SocketServer server, SocketClient client, int bytes)
			{
				// only allow 200B and less when the client hasn't authenticated yet
				
				ClientSession session = client.getAttachment();
				if(!session.isAuthenticated)
				{
					return (bytes <= 200); // this will only allow packets of 200B and less
				}
				
				return (bytes <= (10 * 1024)); // this will only allow packets of 10KB and less
			}
			
			@Override
			public void receivedLatentFunctionCall(SocketServer server, SocketClient client, byte[] bytes, final LatentResponse response)
			{
			}
		};
		
		SocketServer server = new SocketServer(false, 7780, listener); // only the TCP port has been given, so the server won't listen to UDP
		System.out.println("Listening: " + server);
		
		while(true)
		{
			server.listen();
		}
	}
}
