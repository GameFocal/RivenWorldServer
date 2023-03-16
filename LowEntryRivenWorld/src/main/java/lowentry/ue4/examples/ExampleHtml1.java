package lowentry.ue4.examples;


import lowentry.ue4.classes.http.HttpClient;
import lowentry.ue4.classes.http.HttpRequest;
import lowentry.ue4.classes.http.HttpResponse;
import lowentry.ue4.classes.http.HttpServer;
import lowentry.ue4.classes.http.HttpServerListener;


public class ExampleHtml1
{
	public static void main(final String[] args) throws Throwable
	{
		HttpServerListener listener = new HttpServerListener()
		{
			@Override
			public void clientConnected(HttpServer server, HttpClient client)
			{
				// this function is called when a client connects
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " clientConnected");
			}
			
			@Override
			public void clientDisconnected(HttpServer server, HttpClient client)
			{
				// this function is called after a client disconnects
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " clientDisconnected");
			}
			
			@Override
			public void receivedRequest(HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
			{
				// this function is called when you have received an HTTP request
				// interact with the response to send data back
				System.out.println(Thread.currentThread().getName() + " " + client.getIp() + " receivedRequest to " + request.getPath());
				response.addContent("<html>");
				response.addContent("<body>");
				response.addContent("Hello world!");
				response.addContent("</body>");
				response.addContent("</html>");
				response.done();
			}
		};
		
		HttpServer server = new HttpServer(false, true, 7783, listener); // change the second boolean to true to accept external connections (so it becomes new HttpServer(false, true, 7783, listener))
		System.out.println("Listening: " + server);
		
		while(true)
		{
			server.listen();
		}
	}
}
