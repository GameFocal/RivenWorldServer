package lowentry.ue4.classes.sockets;


public interface SimpleSocketServerListener
{
	/**
	 * This function is called when a client connects.<br>
	 * <br>
	 * Note: The client is connected at this point, but the client hasn't been added to the client list yet.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(SimpleSocketClient c : server)</code><br>
	 * will not return this client yet until after this function.
	 */
	void clientConnected(SimpleSocketServer server, SimpleSocketClient client);
	
	/**
	 * This function is called after a client disconnects.<br>
	 * <br>
	 * Note: At this point, the client has already been removed from the client list.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(SimpleSocketClient c : server)</code><br>
	 * will not return this client anymore.
	 */
	void clientDisconnected(SimpleSocketServer server, SimpleSocketClient client);
	
	
	/**
	 * This function is called when you start receiving a packet.<br>
	 * For websockets this function can be called several times for a single packet, since websockets can divide packets into multiple parts, the full size of the packet won't be known until the last part starts being received. So in order to make it more useful, this function will be called at the beginning of every part, with the size of all the previous parts of the packet received so far added up to it.<br>
	 * Return true to start receiving the packet, return false to disconnect the client.
	 */
	boolean canReceivePacket(SimpleSocketServer server, SimpleSocketClient client, int bytes);
	
	/**
	 * This function is called when you have received a message packet.
	 */
	void receivedPacket(SimpleSocketServer server, SimpleSocketClient client, byte[] bytes);
}
