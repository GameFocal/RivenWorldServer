package lowentry.ue4.classes.http;


public interface HttpServerListener
{
	/**
	 * This function is called when a client connects.<br>
	 * <br>
	 * Note: The client is connected at this point, but the client hasn't been added to the client list yet.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(HttpClient c : server)</code><br>
	 * will not return this client yet until after this function.
	 */
	void clientConnected(final HttpServer server, final HttpClient client);
	
	/**
	 * This function is called after a client disconnects.<br>
	 * <br>
	 * Note: At this point, the client has already been removed from the client list.<br>
	 * <br>
	 * This means that code like this:<br>
	 * <code>for(HttpClient c : server)</code><br>
	 * will not return this client anymore.
	 */
	void clientDisconnected(final HttpServer server, final HttpClient client);
	
	/**
	 * This function is called when you have received a request.
	 */
	void receivedRequest(final HttpServer server, final HttpClient client, final HttpRequest request, final HttpResponse response);
}
