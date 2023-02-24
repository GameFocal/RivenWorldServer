package lowentry.ue4.examples;


import lowentry.ue4.classes.http.HttpClient;
import lowentry.ue4.classes.http.HttpRequest;
import lowentry.ue4.classes.http.HttpResponse;
import lowentry.ue4.classes.http.HttpServer;
import lowentry.ue4.classes.http.HttpServerListener;
import lowentry.ue4.classes.http.ThreadedHttpServer;


public class ExampleHtml3
{
	public static void main(final String[] args) throws Throwable
	{
		HttpServerListener listener = new HttpServerListener()
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
				response.addContent("<html><body>Hello world 2!</body></html>");
				response.done();
			}
		};
		
		
		// this is how you run both an HTTP and an HTTPS server at the same time
		// normally HTTP uses port 80, while HTTPS uses 443
		
		
		System.setProperty("javax.net.ssl.keyStore", "server.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "JsEkey@4");
		
		
		HttpServer httpServer = new HttpServer(false, false, 7783, listener);
		System.out.println("Listening: " + httpServer);
		
		ThreadedHttpServer httpsServer = new ThreadedHttpServer(true, false, 7784, listener);
		System.out.println("Listening: " + httpsServer);
		
		
		while(true)
		{
			httpServer.listen();
			httpsServer.executePendingTasks();
		}
	}
}
