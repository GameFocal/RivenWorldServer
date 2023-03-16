package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroServerListener;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;


public class SimpleSocketServer implements Iterable<SimpleSocketClient>
{
	public static boolean     IS_DEBUGGING          = false;
	public static PrintStream DEBUGGING_PRINTSTREAM = System.out;
	
	
	private static final int NEW_CONNECTIONS_QUEUE_SIZE = 500;
	
	
	protected final PyroServer                     server;
	protected final Collection<SimpleSocketClient> clients = new LinkedHashSet<>();
	
	protected final int serverPort;
	
	protected final SimpleSocketServerListener socketListener;
	
	protected       long                                        lastHandshakingClientHandlerValidation = CachedTime.millisSinceStart();
	protected final Collection<SimpleSocketServerClientHandler> handshakingClientHandlers              = new LinkedHashSet<>();
	
	protected final String addressText;
	
	
	/**
	 * Causes debug messages to be printed.<br>
	 * <br>
	 * Only call this function if you have not yet created any SocketServers, and only call this function on the same thread you will create your SocketServers in, otherwise you might run into threading problems.
	 */
	public static void setDebuggingEnabled()
	{
		SimpleSocketServer.IS_DEBUGGING = true;
	}
	
	/**
	 * Causes debug messages to be printed to the given PrintStream.<br>
	 * <br>
	 * Only call this function if you have not yet created any SocketServers, and only call this function on the same thread you will create your SocketServers in, otherwise you might run into threading problems.
	 */
	public static void setDebuggingEnabled(final PrintStream printstream)
	{
		SimpleSocketServer.IS_DEBUGGING = true;
		SimpleSocketServer.DEBUGGING_PRINTSTREAM = printstream;
	}
	
	
	public SimpleSocketServer(final boolean acceptExternalConnections, final int port, final SimpleSocketServerListener listener) throws Exception
	{
		this.serverPort = port;
		
		this.socketListener = listener;
		PyroSelector selector = new PyroSelector();
		this.server = selector.listen(acceptExternalConnections, port, NEW_CONNECTIONS_QUEUE_SIZE, createServerListener());
		this.addressText = getAddressText();
	}
	
	
	protected PyroServerListener createServerListener()
	{
		return client ->
		{
			SimpleSocketServer socketServer = SimpleSocketServer.this;
			SimpleSocketClient socketClient = new SimpleSocketClient(socketServer, client);
			SimpleSocketServerClientHandler clientHandler = new SimpleSocketServerClientHandler(socketListener, socketServer, socketClient);
			client.setListener(clientHandler);
			clientHandler.connectedClient(client);
		};
	}
	
	
	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public void listen()
	{
		listen(100);
	}
	
	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public void listen(final long eventTimeout)
	{
		server.selector().checkThread();
		
		if(eventTimeout <= 10) // too small for CachedTime.millisSinceStart()
		{
			pyroListen(eventTimeout);
			handleHandshakingTimeouts();
			return;
		}
		
		long startTime = CachedTime.millisSinceStart();
		pyroListen(eventTimeout);
		long lastTime = CachedTime.millisSinceStart();
		long timeSpend = (lastTime - startTime);
		while(timeSpend < (eventTimeout - 10))
		{
			pyroListen(eventTimeout - timeSpend);
			long newTime = CachedTime.millisSinceStart();
			if(newTime != lastTime)
			{
				lastTime = newTime;
				timeSpend = (lastTime - startTime);
			}
		}
		
		handleHandshakingTimeouts();
	}
	
	private void pyroListen(final long eventTimeout)
	{
		for(long i = 1; i <= eventTimeout; i++)
		{
			try
			{
				server.selector().select(1);
			}
			catch(Exception e)
			{
				if(SimpleSocketServer.IS_DEBUGGING)
				{
					SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " listen caused an exception:");
					SimpleSocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
				}
			}
		}
	}
	
	
	/**
	 * Takes care of clients that won't handshake.
	 */
	protected void handleHandshakingTimeouts()
	{
		long time = CachedTime.millisSinceStart();
		if((time - lastHandshakingClientHandlerValidation) < 5000) // 5 seconds
		{
			return;
		}
		lastHandshakingClientHandlerValidation = time;
		
		for(Iterator<SimpleSocketServerClientHandler> iterator = handshakingClientHandlers.iterator(); iterator.hasNext(); )
		{
			SimpleSocketServerClientHandler clientHandler = iterator.next();
			if((time - clientHandler.handshakingStartTime) < 15000) // 15 seconds
			{
				// this is the oldest clientHandler
				// if this didn't timeout, the others didn't either
				return;
			}
			if(SimpleSocketServer.IS_DEBUGGING)
			{
				byte[] handshake = (clientHandler.handshakingPacket == null) ? null : clientHandler.handshakingPacket.toByteArray();
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + clientHandler.socketClient + " was disconnected because the handshake took too long, maximum time for a handshake is 15 seconds" + ((handshake == null) ? "" : ", handshake so far was: " + LowEntry.bytesToHex(handshake) + " => " + LowEntry.bytesToStringLatin1(handshake).replaceAll("[\\p{C}]", "?")));
			}
			iterator.remove();
			clientHandler.disconnect();
		}
	}
	
	
	/**
	 * Closes the server socket. This will only prevent new connections from being made, the existing connections will stay open.
	 */
	public void close()
	{
		if(server.selector().isNetworkThread())
		{
			try
			{
				server.close();
			}
			catch(Exception e)
			{
			}
		}
		else
		{
			server.selector().scheduleTask(() ->
			{
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
			});
		}
	}
	
	/**
	 * Closes the server socket. Calls {@link SimpleSocketClient#disconnect()} on every open connection.
	 */
	public void terminate()
	{
		if(server.selector().isNetworkThread())
		{
			try
			{
				server.close();
			}
			catch(Exception e)
			{
			}
			
			for(SimpleSocketClient client : this)
			{
				client.disconnect();
			}
		}
		else
		{
			server.selector().scheduleTask(() ->
			{
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
				
				for(SimpleSocketClient client : SimpleSocketServer.this)
				{
					client.disconnect();
				}
			});
		}
	}
	
	/**
	 * Closes the server socket. Calls {@link SimpleSocketClient#disconnectImmediately()} on every open connection.
	 */
	public void terminateImmediately()
	{
		if(server.selector().isNetworkThread())
		{
			try
			{
				server.close();
			}
			catch(Exception e)
			{
			}
			
			for(SimpleSocketClient client : this)
			{
				client.disconnectImmediately();
			}
		}
		else
		{
			server.selector().scheduleTask(() ->
			{
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
				
				for(SimpleSocketClient client : SimpleSocketServer.this)
				{
					client.disconnectImmediately();
				}
			});
		}
	}
	
	
	/**
	 * Returns an iterator to access all connected clients of this server.
	 */
	@Override
	public Iterator<SimpleSocketClient> iterator()
	{
		List<SimpleSocketClient> copy;
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
	public void forEachClient(final Consumer<SimpleSocketClient> action)
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
	 * Returns the PyroNet object of this wrapper.
	 */
	public PyroServer pyro()
	{
		return server;
	}
	
	/**
	 * Returns the selector.
	 */
	public PyroSelector selector()
	{
		return server.selector();
	}
	
	
	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(final Runnable runnable)
	{
		if(server.selector().isNetworkThread())
		{
			runnable.run();
		}
		else
		{
			server.selector().scheduleTask(runnable);
		}
	}
	
	
	/**
	 * Returns the port this server listens to.
	 */
	public int getPort()
	{
		return serverPort;
	}
	
	
	public String getAddressText()
	{
		if(addressText != null)
		{
			return addressText;
		}
		if(server == null)
		{
			return "closed";
		}
		return server.getAddressText();
	}
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
}
