package lowentry.ue4.classes.sockets;


public interface SimpleSocketConnectionListener
{
	/**
	 * This function is called when the connection is opened.
	 */
	void connected(SimpleSocketConnection connection);
	
	/**
	 * This function is called after the connection is closed.
	 */
	void disconnected(SimpleSocketConnection connection);
	
	
	/**
	 * This function is called when you have received a packet.
	 */
	void receivedPacket(SimpleSocketConnection connection, byte[] bytes);
}
