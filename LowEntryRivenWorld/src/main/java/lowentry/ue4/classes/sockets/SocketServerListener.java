package lowentry.ue4.classes.sockets;


import java.nio.ByteBuffer;


public interface SocketServerListener
{
	/**
	 * This function is called when a client connects.<br>
	 * <br>
	 * Note: The client is connected at this point, but the client hasn't been added to the client list yet.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(SocketClient c : server)</code><br>
	 * will not return this client yet until after this function.
	 */
	void clientConnected(SocketServer server, SocketClient client);
	
	/**
	 * This function is called after a client disconnects.<br>
	 * <br>
	 * Note: At this point, the client has already been removed from the client list.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(SocketClient c : server)</code><br>
	 * will not return this client anymore.
	 */
	void clientDisconnected(SocketServer server, SocketClient client);
	
	
	/**
	 * This function is called after a connection validation has been received and responded to.
	 */
	void receivedConnectionValidation(SocketServer server, SocketClient client);
	
	
	/**
	 * This function is called when you start receiving an unreliable message packet.<br>
	 * Return true to start receiving the packet, return false to disconnect the client.
	 */
	boolean startReceivingUnreliableMessage(SocketServer server, SocketClient client, int bytes);
	
	/**
	 * This function is called when you have received an unreliable (UDP) message packet.<br>
	 * <br>
	 * This function can every only be called if the UDP port has been given to the server (otherwise the server won't listen to UDP packets).<br>
	 * <br>
	 * This function is called with a ByteBuffer instead of a byte array for maximum performance (since UDP packets are always received in its entirety).<br>
	 * The buffer will only be valid to be used inside this function, you can't store the buffer for later.<br>
	 * If you want to use the bytes later, retrieve the bytes out of the ByteBuffer and use that later.
	 */
	void receivedUnreliableMessage(SocketServer server, SocketClient client, ByteBuffer bytes);
	
	
	/**
	 * This function is called when you start receiving a message packet.<br>
	 * Return true to start receiving the packet, return false to disconnect the client.
	 */
	boolean startReceivingMessage(SocketServer server, SocketClient client, int bytes);
	
	/**
	 * This function is called when you have received a message packet.
	 */
	void receivedMessage(SocketServer server, SocketClient client, byte[] bytes);
	
	
	/**
	 * This function is called when you start receiving a function call packet.<br>
	 * Return true to start receiving the packet, return false to disconnect the client.
	 */
	boolean startReceivingFunctionCall(SocketServer server, SocketClient client, int bytes);
	
	/**
	 * This function is called when you have received a function call packet.
	 */
	byte[] receivedFunctionCall(SocketServer server, SocketClient client, byte[] bytes);
	
	
	/**
	 * This function is called when you start receiving a latent function call packet.<br>
	 * Return true to start receiving the packet, return false to disconnect the client.
	 */
	boolean startReceivingLatentFunctionCall(SocketServer server, SocketClient client, int bytes);
	
	/**
	 * This function is called when you have received a latent function call packet.
	 */
	void receivedLatentFunctionCall(SocketServer server, SocketClient client, byte[] bytes, LatentResponse response);
}
