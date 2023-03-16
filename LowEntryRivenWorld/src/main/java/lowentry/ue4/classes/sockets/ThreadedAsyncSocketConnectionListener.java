package lowentry.ue4.classes.sockets;


public interface ThreadedAsyncSocketConnectionListener
{
	/**
	 * This function is called when the connection is opened.
	 */
	void connected(ThreadedAsyncSocketConnection threadedConnection, SocketConnection connection);
	
	/**
	 * This function is called after the connection is closed.
	 */
	void disconnected(ThreadedAsyncSocketConnection threadedConnection, SocketConnection connection);
	
	
	/**
	 * This function is called when you have received an unreliable (UDP) message packet.<br>
	 * <br>
	 * This function can every only be called if the UDP port has been given to the connection (otherwise the connection won't listen to UDP packets).<br>
	 */
	void receivedUnreliableMessage(ThreadedAsyncSocketConnection threadedConnection, SocketConnection connection, byte[] bytes);
	
	/**
	 * This function is called when you have received a message packet.
	 */
	void receivedMessage(ThreadedAsyncSocketConnection threadedConnection, SocketConnection connection, byte[] bytes);
}
