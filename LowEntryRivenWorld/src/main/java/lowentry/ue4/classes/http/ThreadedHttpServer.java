package lowentry.ue4.classes.http;


import lowentry.ue4.classes.utility.ThreadSleeper;
import lowentry.ue4.classes.http.HttpTasks.ClientConnectedThreadedHttpServer;
import lowentry.ue4.classes.http.HttpTasks.ClientDisconnectedThreadedHttpServer;
import lowentry.ue4.classes.http.HttpTasks.ReceivedRequestThreadedHttpServer;
import lowentry.ue4.classes.sockets.SocketConnection;
import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;


public class ThreadedHttpServer implements Iterable<HttpClient>
{
	protected static final Iterator<HttpClient> nullIterator;
	
	
	private static       int    ID             = 1;
	private static final Object idSynchronizer = new Object();
	
	protected final ThreadSleeper startSleeper = new ThreadSleeper();
	
	protected final Thread networkThread;
	
	protected final boolean                    secure;
	protected final boolean                    acceptExternalConnections;
	protected final int                        port;
	protected final ThreadedHttpServerListener listener;
	
	protected volatile HttpServer server;
	
	protected final    SimpleBlockingQueue<Runnable> tasks          = new SimpleBlockingQueue<>();
	protected volatile Exception                     startException = null;
	protected volatile boolean                       started        = false;
	
	
	static
	{
		nullIterator = new Iterator<>()
		{
			@Override
			public HttpClient next()
			{
				throw new NoSuchElementException();
			}
			@Override
			public boolean hasNext()
			{
				return false;
			}
		};
	}
	
	
	public ThreadedHttpServer(boolean secure, boolean acceptExternalConnections, int port, ThreadedHttpServerListener listener) throws Exception
	{
		this.networkThread = Thread.currentThread();
		this.secure = secure;
		this.listener = listener;
		this.acceptExternalConnections = acceptExternalConnections;
		this.port = port;
		start();
	}
	
	public ThreadedHttpServer(boolean secure, boolean acceptExternalConnections, int port, HttpServerListener listener) throws Exception
	{
		this.networkThread = Thread.currentThread();
		this.secure = secure;
		this.listener = getWrappedListener(listener);
		this.acceptExternalConnections = acceptExternalConnections;
		this.port = port;
		start();
	}
	
	
	public static ThreadedHttpServerListener getWrappedListener(final HttpServerListener listener)
	{
		return new ThreadedHttpServerListener()
		{
			@Override
			public void clientConnected(ThreadedHttpServer threadedServer, HttpServer server, HttpClient client)
			{
				listener.clientConnected(server, client);
			}
			@Override
			public void clientDisconnected(ThreadedHttpServer threadedServer, HttpServer server, HttpClient client)
			{
				listener.clientDisconnected(server, client);
			}
			@Override
			public void receivedRequest(ThreadedHttpServer threadedServer, HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
			{
				listener.receivedRequest(server, client, request, response);
			}
		};
	}
	
	
	/**
	 * Returns the port this server is listening to.
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * Returns true if it uses SSL/TLS (HTTPS), returns false otherwise (HTTP).
	 */
	public boolean isSecure()
	{
		return secure;
	}
	
	
	/**
	 * Starts the server.
	 */
	protected void start() throws Exception
	{
		Thread thread = new Thread(() ->
		{
			final HttpServerListener list = new HttpServerListener()
			{
				@Override
				public void clientConnected(HttpServer server, HttpClient client)
				{
					tasks.put(new ClientConnectedThreadedHttpServer(ThreadedHttpServer.this, listener, server, client));
				}
				
				@Override
				public void clientDisconnected(HttpServer server, HttpClient client)
				{
					tasks.put(new ClientDisconnectedThreadedHttpServer(ThreadedHttpServer.this, listener, server, client));
				}
				
				@Override
				public void receivedRequest(HttpServer server, HttpClient client, HttpRequest request, HttpResponse response)
				{
					tasks.put(new ReceivedRequestThreadedHttpServer(ThreadedHttpServer.this, listener, server, client, request, response));
				}
			};
			
			
			Exception exception = null;
			try
			{
				server = new HttpServer(secure, acceptExternalConnections, port, list);
			}
			catch(Exception e)
			{
				exception = e;
				startException = e;
			}
			
			started = true;
			startSleeper.wakeup();
			
			if(exception != null)
			{
				return;
			}
			
			
			while(server.run)
			{
				server.listen();
			}
		}, "Threaded-Http-Server-" + getNextId());
		thread.setDaemon(true);
		thread.start();
		
		waitTillStarted();
		
		if(startException != null)
		{
			throw new Exception(startException);
		}
	}
	protected void waitTillStarted()
	{
		while(!started)
		{
			startSleeper.sleep(50);
		}
	}
	
	
	/**
	 * Returns an iterator to access all connected clients of this server.
	 */
	@Override
	public Iterator<HttpClient> iterator()
	{
		final HttpServer finalServer = server;
		if(finalServer != null)
		{
			return finalServer.iterator();
		}
		return nullIterator;
	}
	
	/**
	 * Performs the given action for each element of the {@code Iterable} until
	 * all elements have been processed or the action throws an exception.
	 * Unless otherwise specified by the implementing class, actions are
	 * performed in the order of iteration (if an iteration order is
	 * specified). Exceptions thrown by the action are relayed to the caller.
	 */
	public void forEachClient(Consumer<HttpClient> action)
	{
		final HttpServer finalServer = server;
		if(finalServer != null)
		{
			finalServer.forEach(action);
		}
	}
	
	/**
	 * Returns the amount of connected clients.
	 */
	public int getClientCount()
	{
		final HttpServer finalServer = server;
		if(finalServer != null)
		{
			return finalServer.getClientCount();
		}
		return 0;
	}
	
	
	/**
	 * Stops the server.
	 */
	public void terminate()
	{
		final HttpServer finalServer = server;
		if(finalServer != null)
		{
			finalServer.terminate();
		}
	}
	
	
	/**
	 * Executes any events caused in the threaded {@link SocketConnection#listen()} loop.<br>
	 * <br>
	 * <b>WARNING:</b> Call this on the same thread this object was created in!
	 */
	public void executePendingTasks()
	{
		if(networkThread != Thread.currentThread())
		{
			throw new PyroException("call from outside the network-thread");
		}
		
		while(true)
		{
			Runnable task = tasks.poll();
			if(task == null)
			{
				break;
			}
			
			try
			{
				task.run();
			}
			catch(Exception cause)
			{
				cause.printStackTrace();
			}
		}
	}
	
	
	/**
	 * This will add a task that will be executed during the next {@link #executePendingTasks()}, <br>
	 * which will cause the task to be executed in the same thread this object was created in.
	 */
	public void addTask(Runnable task)
	{
		if(task != null)
		{
			tasks.put(task);
		}
	}
	
	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(Runnable runnable)
	{
		if(networkThread == Thread.currentThread())
		{
			runnable.run();
		}
		else
		{
			tasks.put(runnable);
		}
	}
	
	
	/**
	 * Returns the server, can be null if the thread hasn't started yet.
	 */
	public HttpServer server()
	{
		return server;
	}
	
	
	@Override
	public String toString()
	{
		final HttpServer finalServer = server;
		if(finalServer != null)
		{
			return finalServer.toString();
		}
		return super.toString();
	}
	
	
	private static int getNextId()
	{
		synchronized(idSynchronizer)
		{
			int id = ID;
			ID++;
			if(ID >= Integer.MAX_VALUE)
			{
				ID = 1;
			}
			return id;
		}
	}
}
