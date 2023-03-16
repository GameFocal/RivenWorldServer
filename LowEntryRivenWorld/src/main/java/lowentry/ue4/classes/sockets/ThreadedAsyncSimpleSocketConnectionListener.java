package lowentry.ue4.classes.sockets;


public interface ThreadedAsyncSimpleSocketConnectionListener
{
	/**
	 * This function is called when the connection is opened.
	 */
	void connected(ThreadedAsyncSimpleSocketConnection threadedConnection, SimpleSocketConnection connection);
	
	/**
	 * This function is called after the connection is closed.
	 */
	void disconnected(ThreadedAsyncSimpleSocketConnection threadedConnection, SimpleSocketConnection connection);
	
	
	/**
	 * This function is called when you have received a packet.
	 */
	void receivedPacket(ThreadedAsyncSimpleSocketConnection threadedConnection, SimpleSocketConnection connection, byte[] bytes);
}
