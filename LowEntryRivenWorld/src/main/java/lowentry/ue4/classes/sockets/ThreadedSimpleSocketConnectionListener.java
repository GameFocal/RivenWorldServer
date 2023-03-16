package lowentry.ue4.classes.sockets;


public interface ThreadedSimpleSocketConnectionListener
{
	/**
	 * This function is called when the connection is opened.
	 */
	void connected(ThreadedSimpleSocketConnection threadedConnection, SimpleSocketConnection connection);
	
	/**
	 * This function is called after the connection is closed.
	 */
	void disconnected(ThreadedSimpleSocketConnection threadedConnection, SimpleSocketConnection connection);
	
	
	/**
	 * This function is called when you have received a packet.
	 */
	void receivedPacket(ThreadedSimpleSocketConnection threadedConnection, SimpleSocketConnection connection, byte[] bytes);
}
