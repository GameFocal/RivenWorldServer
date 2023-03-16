package lowentry.ue4.examples;


import lowentry.ue4.classes.http.HttpClient;
import lowentry.ue4.classes.http.HttpRequest;
import lowentry.ue4.classes.http.HttpResponse;
import lowentry.ue4.classes.http.HttpServer;
import lowentry.ue4.classes.http.HttpServerListener;
import lowentry.ue4.classes.http.ThreadedHttpServer;
import lowentry.ue4.classes.sockets.LatentResponse;
import lowentry.ue4.classes.sockets.SocketClient;
import lowentry.ue4.classes.sockets.SocketServer;
import lowentry.ue4.classes.sockets.SocketServerListener;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;


public class ExampleHtmlAndSocket
{
	public static void main(final String[] args) throws Throwable
	{
		HttpServerListener httpListener = new HttpServerListener()
		{
			@Override
			public void clientConnected(HttpServer server, HttpClient client)
			{
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " clientConnected");
			}
			
			@Override
			public void clientDisconnected(HttpServer server, HttpClient client)
			{
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " clientDisconnected");
			}
			
			@Override
			public void receivedRequest(HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
			{
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " receivedRequest to " + request.getPath());
				response.addContent("<html>");
				response.addContent("<body>");
				response.addContent("Hello world!");
				response.addContent("</body>");
				response.addContent("</html>");
				response.done();
			}
		};
		
		
		SocketServerListener socketListener = new SocketServerListener()
		{
			@Override
			public void clientConnected(final SocketServer server, final SocketClient client)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] ClientConnected: " + client);
			}
			
			@Override
			public void clientDisconnected(final SocketServer server, final SocketClient client)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] ClientDisconnected: " + client);
			}
			
			
			@Override
			public void receivedConnectionValidation(SocketServer server, SocketClient client)
			{
			}
			
			
			@Override
			public boolean startReceivingUnreliableMessage(SocketServer server, SocketClient client, int bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Unreliable Message");
				return (bytes <= 1024);
			}
			
			@Override
			public void receivedUnreliableMessage(SocketServer server, SocketClient client, ByteBuffer bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Received Unreliable Message: \"" + LowEntry.bytesToStringUtf8(LowEntry.getBytesFromByteBuffer(bytes)) + "\"");
			}
			
			
			@Override
			public boolean startReceivingMessage(final SocketServer server, final SocketClient client, final int bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Message");
				return (bytes <= (10 * 1024));
			}
			
			@Override
			public void receivedMessage(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Received Message: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
			}
			
			
			@Override
			public boolean startReceivingFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Function Call");
				return (bytes <= (10 * 1024));
			}
			
			@Override
			public byte[] receivedFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Received Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
				return null;
			}
			
			
			@Override
			public boolean startReceivingLatentFunctionCall(final SocketServer server, final SocketClient client, final int bytes)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Start Receiving Latent Function Call");
				return (bytes <= (10 * 1024));
			}
			
			@Override
			public void receivedLatentFunctionCall(final SocketServer server, final SocketClient client, final byte[] bytes, final LatentResponse response)
			{
				System.out.println("[" + Thread.currentThread().getName() + "] Received Latent Function Call: \"" + LowEntry.bytesToStringUtf8(bytes) + "\"");
				response.done(null);
			}
		};
		
		
		ThreadedHttpServer httpServer = new ThreadedHttpServer(false, false, 7783, httpListener); // change the second boolean to true to accept external connections (so it becomes new ThreadedHttpServer(false, true, 7783, httpListener))
		System.out.println("Listening: " + httpServer);
		
		SocketServer socketServer = new SocketServer(false, 7780, socketListener); // change the boolean to true if you want to accept external connections (so it becomes new SocketServer(true, 7780, socketListener))
		System.out.println("Listening: " + socketServer);
		
		while(true)
		{
			socketServer.listen();
			httpServer.executePendingTasks();
		}
	}
}
