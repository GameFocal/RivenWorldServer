package lowentry.ue4.classes.http;


public class HttpTasks
{
	public static class ClientConnectedHttpServer implements Runnable
	{
		private final HttpServerListener listener;
		private final HttpServer         server;
		private final HttpClient         client;
		
		public ClientConnectedHttpServer(HttpServerListener listener, HttpServer server, HttpClient client)
		{
			this.listener = listener;
			this.server = server;
			this.client = client;
		}
		
		@Override
		public void run()
		{
			listener.clientConnected(server, client);
		}
	}
	
	
	public static class ClientDisconnectedHttpServer implements Runnable
	{
		private final HttpServerListener listener;
		private final HttpServer         server;
		private final HttpClient         client;
		
		public ClientDisconnectedHttpServer(HttpServerListener listener, HttpServer server, HttpClient client)
		{
			this.listener = listener;
			this.server = server;
			this.client = client;
		}
		
		@Override
		public void run()
		{
			listener.clientDisconnected(server, client);
		}
	}
	
	
	public static class ReceivedRequestHttpServer implements Runnable
	{
		private final HttpServerListener listener;
		private final HttpServer         server;
		private final HttpClient         client;
		private final HttpRequest        request;
		private final HttpResponse       response;
		
		public ReceivedRequestHttpServer(HttpServerListener listener, HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
		{
			this.listener = listener;
			this.server = server;
			this.client = client;
			this.request = request;
			this.response = response;
		}
		
		@Override
		public void run()
		{
			listener.receivedRequest(server, client, request, response);
		}
	}
	
	
	
	public static class ClientConnectedThreadedHttpServer implements Runnable
	{
		private final ThreadedHttpServer         thread;
		private final ThreadedHttpServerListener listener;
		private final HttpServer                 server;
		private final HttpClient                 client;
		
		public ClientConnectedThreadedHttpServer(ThreadedHttpServer thread, ThreadedHttpServerListener listener, HttpServer server, HttpClient client)
		{
			this.thread = thread;
			this.listener = listener;
			this.server = server;
			this.client = client;
		}
		
		@Override
		public void run()
		{
			listener.clientConnected(thread, server, client);
		}
	}
	
	
	public static class ClientDisconnectedThreadedHttpServer implements Runnable
	{
		private final ThreadedHttpServer         thread;
		private final ThreadedHttpServerListener listener;
		private final HttpServer                 server;
		private final HttpClient                 client;
		
		public ClientDisconnectedThreadedHttpServer(ThreadedHttpServer thread, ThreadedHttpServerListener listener, HttpServer server, HttpClient client)
		{
			this.thread = thread;
			this.listener = listener;
			this.server = server;
			this.client = client;
		}
		
		@Override
		public void run()
		{
			listener.clientDisconnected(thread, server, client);
		}
	}
	
	
	public static class ReceivedRequestThreadedHttpServer implements Runnable
	{
		private final ThreadedHttpServer         thread;
		private final ThreadedHttpServerListener listener;
		private final HttpServer                 server;
		private final HttpClient                 client;
		private final HttpRequest                request;
		private final HttpResponse               response;
		
		public ReceivedRequestThreadedHttpServer(ThreadedHttpServer thread, ThreadedHttpServerListener listener, HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
		{
			this.thread = thread;
			this.listener = listener;
			this.server = server;
			this.client = client;
			this.request = request;
			this.response = response;
		}
		
		@Override
		public void run()
		{
			listener.receivedRequest(thread, server, client, request, response);
		}
	}
}
