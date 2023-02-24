package lowentry.ue4.examples;


import lowentry.ue4.classes.http.HttpClient;
import lowentry.ue4.classes.http.HttpRequest;
import lowentry.ue4.classes.http.HttpResponse;
import lowentry.ue4.classes.http.HttpServer;
import lowentry.ue4.classes.http.HttpServerListener;


public class ExampleHtml2
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
		
		
		// this shows you how to start an HTTPS (SSL) server
		
		
		// first of all, setup a keystore
		// short tutorial here (ignore the 'netty' specific information): https://maxrohde.com/2013/09/07/setting-up-ssl-with-netty/
		System.setProperty("javax.net.ssl.keyStore", "server.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "JsEkey@4");
		
		
		// then, start the server with the first parameter being true (that signals it should be an HTTPS server)
		HttpServer server = new HttpServer(true, false, 7784, listener);
		
		
		// that's it, you are now running an HTTPS server
		
		
		System.out.println("Listening: " + server);
		
		while(true)
		{
			server.listen();
		}
	}
}
