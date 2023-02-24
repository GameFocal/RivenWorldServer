package lowentry.ue4.classes.sockets;


public interface ThreadedSocketConnectionListener
{
	/**
	 * This function is called when the connection is opened.
	 */
	void connected(ThreadedSocketConnection threadedConnection, SocketConnection connection);
	
	/**
	 * This function is called after the connection is closed.
	 */
	void disconnected(ThreadedSocketConnection threadedConnection, SocketConnection connection);
	
	
	/**
	 * This function is called when you have received an unreliable (UDP) message packet.<br>
	 * <br>
	 * This function can every only be called if the UDP port has been given to the connection (otherwise the connection won't listen to UDP packets).<br>
	 */
	void receivedUnreliableMessage(ThreadedSocketConnection threadedConnection, SocketConnection connection, byte[] bytes);
	
	/**
	 * This function is called when you have received a message packet.
	 */
	void receivedMessage(ThreadedSocketConnection threadedConnection, SocketConnection connection, byte[] bytes);
}
