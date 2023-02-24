package lowentry.ue4.examples;


import lowentry.ue4.classes.sockets.SimpleSocketClient;
import lowentry.ue4.classes.sockets.SimpleSocketServer;
import lowentry.ue4.classes.sockets.simplification.SimpleSocketServerMain;
import lowentry.ue4.classes.sockets.simplification.SimpleSocketServerMainListener;
import lowentry.ue4.library.LowEntry;


/**
 * This is an example of the Simple Socket Server.
 *
 * It automatically creates two files, one signals that it's running ("_STATUS__IS_RUNNING_") which also contains the PID of the Java process, and the other file ("_ACTION__SHUTDOWN_") can be deleted to cause the server to gracefully stop (gracefully, meaning: no new connections will be accepted anymore, but the existing connections won't be forcefully terminated by the server unless the getMaxTimeMsForGracefulShutdown() amount of time has passed).
 *
 * These classes are great for micro services, since it's a very simple protocol, plus it supports both normal sockets as well as websockets.
 *
 * The protocol is:
 * 1) the client sends \r\n\r\n (or for websockets, the HTTP header ending with \r\n\r\n), based on this, the client will be treated as a normal tcp socket or as a websocket
 * 2a) if the client is a normal socket, then each packet (sent or received) will simply be the length of the bytes (int32, so 4 bytes, in big endian) plus of course the bytes
 * 2b) if the client is a websocket, then each packet is simply the bytes and nothing more than that, since the websocket protocol internally handles sending the length of the bytes
 */
public class ExampleSocket8 extends SimpleSocketServerMainListener
{
	public static void main(final String[] args) throws Throwable
	{
		SimpleSocketServer.setDebuggingEnabled(); // you probably want to comment this out
		SimpleSocketServerMain.run(new ExampleSocket8(), 7780);
	}
	
	public ExampleSocket8()
	{
		// allows you to add socket connections to other SimpleSocketServer servers
		// normally you only want to do this once, so we do this in the constructor
		
		// addSocketConnection("localhost", 7781, new ThreadedSimpleSocketConnectionListener()
		// {
		//     @Override
		//     public final void connected(final ThreadedSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection)
		//     {
		//     }
		//
		//     @Override
		//     public final void disconnected(final ThreadedSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection)
		//     {
		//     }
		//
		//     @Override
		//     public final void receivedPacket(final ThreadedSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection, final byte[] bytes)
		//     {
		//     }
		// });
	}
	
	
	// -------------------------------- //
	
	
	@Override
	public void hasStarted()
	{
		System.out.println("Server listening: " + this);
	}
	
	@Override
	public void beforeGracefulShutdown()
	{
		System.out.println("Server is gracefully stopping...");
	}
	
	@Override
	public long getMaxTimeMsForGracefulShutdown()
	{
		return LowEntry.Millis.MINUTES(10);
	}
	
	@Override
	public void hasStopped()
	{
		System.out.println("Server stopped");
	}
	
	@Override
	public void serverErrored(final Exception e)
	{
		System.out.println(LowEntry.getStackTrace(e));
	}
	
	@Override
	public void clientErrored(final Exception e)
	{
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			System.out.println(LowEntry.getStackTrace(e));
		}
	}
	
	
	// -------------------------------- //
	
	
	@Override
	public void clientConnected(final SimpleSocketClient client)
	{
		// client.setAttachment(new Session(client));
	}
	
	@Override
	public void clientDisconnected(final SimpleSocketClient client)
	{
	}
	
	
	// -------------------------------- //
	
	
	@Override
	public long getTimeMsBeforeNextTick()
	{
		return 300000; // 5 minutes
	}
	
	@Override
	public void tick()
	{
		// for(SimpleSocketClient client : this)
		// {
		//     client.sendPacket(LowEntry.stringToBytesUtf8("you're still connected huh"));
		// }
	}
	
	
	// -------------------------------- //
	
	
	@Override
	public boolean canReceivePacket(final SimpleSocketClient client, final int bytes)
	{
		return ((bytes >= 1) && (bytes <= 5000));
	}
	
	@Override
	public void receivedPacket(final SimpleSocketClient client, final byte[] bytes)
	{
		if(SimpleSocketServer.IS_DEBUGGING)
		{
			SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(" > " + client + " has send message: " + LowEntry.bytesToHex(bytes));
		}
		// Session session = client.getAttachment();
		// if(session == null)
		// {
		//     client.disconnect();
		//     return;
		// }
		// session.doSomethingWith(bytes);
	}
}
