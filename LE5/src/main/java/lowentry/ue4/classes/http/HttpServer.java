package lowentry.ue4.classes.http;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Consumer;


public class HttpServer implements Iterable<HttpClient>
{
	private static final int NEW_CONNECTIONS_QUEUE_SIZE = 500;


	protected static final ThreadLocal<SimpleDateFormat> serverTimeFormat = createServerTimeFormat();


	protected final Thread networkThread;

	protected final boolean            secure;
	protected final int                port;
	protected final HttpServerListener listener;

	protected final ServerSocket           server;
	protected final Collection<HttpClient> clients       = new ArrayList<>();
	protected final Collection<HttpClient> removeClients = new ArrayList<>();

	protected final ByteBuffer networkBuffer = ByteBuffer.allocate(PyroSelector.BUFFER_SIZE);

	protected volatile boolean run = true;


	protected final SimpleBlockingQueue<Runnable> tasks = new SimpleBlockingQueue<>();


	public HttpServer(boolean secure, boolean acceptExternalConnections, int port, HttpServerListener listener) throws Exception
	{
		this.networkThread = Thread.currentThread();
		this.secure = secure;
		this.port = port;
		this.listener = listener;
		this.server = initialize(secure, acceptExternalConnections, port);
	}


	protected static ThreadLocal<SimpleDateFormat> createServerTimeFormat()
	{
		return ThreadLocal.withInitial(() ->
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
			dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			return dateFormat;
		});
	}


	protected ServerSocket initialize(boolean secure, boolean acceptExternalConnections, int port) throws Exception
	{
		ServerSocketFactory serverSocketFactory = (secure ? SSLServerSocketFactory.getDefault() : ServerSocketFactory.getDefault());
		ServerSocket server = serverSocketFactory.createServerSocket(port, NEW_CONNECTIONS_QUEUE_SIZE, (acceptExternalConnections ? null : InetAddress.getLoopbackAddress()));
		if(secure && (server instanceof SSLServerSocket))
		{
			SSLServerSocket serverssl = (SSLServerSocket) server;
			serverssl.setEnabledCipherSuites(serverssl.getSupportedCipherSuites());
			serverssl.setEnabledProtocols(serverssl.getSupportedProtocols());
		}
		server.setSoTimeout(100);
		return server;
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


	public final boolean isNetworkThread()
	{
		return (networkThread == Thread.currentThread());
	}

	public final Thread networkThread()
	{
		return networkThread;
	}

	public final void checkThread()
	{
		if(!isNetworkThread())
		{
			throw new PyroException("call from outside the network-thread, you must schedule tasks");
		}
	}


	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * Each listen takes between 0 and 100 milliseconds.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in!<br>
	 */
	public void listen()
	{
		checkThread();
		executePendingTasks();


		if(run)
		{
			{// remove disconnected clients >>
				List<HttpClient> removeClientsArray = null;
				synchronized(removeClients)
				{
					if(removeClients.size() > 0)
					{
						removeClientsArray = new ArrayList<>(removeClients);
						removeClients.clear();
					}
				}
				if(removeClientsArray != null)
				{
					synchronized(clients)
					{
						clients.removeAll(removeClientsArray);
					}
					for(HttpClient client : removeClientsArray)
					{
						internal_listenerClientDisconnected(client);
					}
				}
			}// remove disconnected clients <<

			{// tick clients >>
				// no synchronize needed here, since we don't make any changes here, and any changes made are in the same thread as this one or are placed in the removeClients collection
				long time = CachedTime.millisSinceStart();
				for(HttpClient client : clients)
				{
					try
					{
						client.listen(time);
					}
					catch(Exception e)
					{
						client.disconnect();
					}
				}
			}// tick clients <<

			{// remove disconnected clients >>
				List<HttpClient> removeClientsArray = null;
				synchronized(removeClients)
				{
					if(removeClients.size() > 0)
					{
						removeClientsArray = new ArrayList<>(removeClients);
						removeClients.clear();
					}
				}
				if(removeClientsArray != null)
				{
					synchronized(clients)
					{
						clients.removeAll(removeClientsArray);
					}
					for(HttpClient client : removeClientsArray)
					{
						internal_listenerClientDisconnected(client);
					}
				}
			}// remove disconnected clients <<

			{// accept new clients >>
				try
				{
					@SuppressWarnings("resource")
					Socket s = server.accept();
					try
					{
						HttpClient client = new HttpClient(this, s);
						internal_listenerClientConnected(client);
						synchronized(clients)
						{
							clients.add(client);
						}
					}
					catch(Exception e)
					{
						s.close();
					}
				}
				catch(Exception e)
				{
				}
			}// accept new clients <<
		}


		if(!run && !server.isClosed())
		{
			{// remove disconnected clients >>
				List<HttpClient> removeClientsArray;
				synchronized(removeClients)
				{
					removeClientsArray = new ArrayList<>(removeClients);
					removeClients.clear();
				}
				synchronized(clients)
				{
					clients.removeAll(removeClientsArray);
				}
				for(HttpClient client : removeClientsArray)
				{
					internal_listenerClientDisconnected(client);
				}
			}// remove disconnected clients <<

			{// disconnect connected clients >>
				List<HttpClient> removeClientsArray;
				synchronized(clients)
				{
					removeClientsArray = new ArrayList<>(clients);
					clients.clear();
				}
				for(HttpClient client : removeClientsArray)
				{
					client.internal_disconnect();
				}
				for(HttpClient client : removeClientsArray)
				{
					internal_listenerClientDisconnected(client);
				}
			}// disconnect connected clients <<

			{// close server >>
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
			}// close server <<
		}


		executePendingTasks();
	}

	protected void executePendingTasks()
	{
		while(true)
		{
			Runnable task = this.tasks.poll();
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


	protected static String internal_getServerTime()
	{
		return serverTimeFormat.get().format(Calendar.getInstance().getTime());
	}


	protected void internal_listenerClientConnected(final HttpClient client)
	{
		listener.clientConnected(this, client);
	}

	protected void internal_listenerClientDisconnected(final HttpClient client)
	{
		listener.clientDisconnected(this, client);
	}

	protected void internal_listenerReceivedRequest(final HttpClient client, final HttpRequest request, final HttpResponse response)
	{
		listener.receivedRequest(this, client, request, response);
	}


	protected void internal_removeClient(HttpClient client)
	{
		synchronized(removeClients)
		{
			removeClients.add(client);
		}
	}


	/**
	 * Returns an iterator to access all connected clients of this server.
	 */
	@Override
	public Iterator<HttpClient> iterator()
	{
		List<HttpClient> copy;
		synchronized(clients)
		{
			copy = new ArrayList<>(clients);
		}
		return copy.iterator();
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
		forEach(action);
	}

	/**
	 * Returns the amount of connected clients.
	 */
	public int getClientCount()
	{
		synchronized(clients)
		{
			return clients.size();
		}
	}


	/**
	 * Stops the server.
	 */
	public void terminate()
	{
		this.run = false;
	}


	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(Runnable runnable)
	{
		if(isNetworkThread())
		{
			runnable.run();
		}
		else
		{
			tasks.put(runnable);
		}
	}


	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + server.getClass().getName() + "[/" + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort() + "]]";
	}
}
