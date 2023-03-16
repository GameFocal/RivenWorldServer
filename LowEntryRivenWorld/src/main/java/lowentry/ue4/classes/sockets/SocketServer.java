package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroServerListener;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.PyroServerUdp;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event.PyroServerUdpListener;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;


public class SocketServer implements Iterable<SocketClient>
{
	public static boolean     IS_DEBUGGING          = false;
	public static PrintStream DEBUGGING_PRINTSTREAM = System.out;
	
	
	private static final int NEW_CONNECTIONS_QUEUE_SIZE = 500;
	
	
	protected final PyroServer               server;
	protected final Collection<SocketClient> clients = new LinkedHashSet<>();
	
	protected final PyroServerUdp                                    serverUdp;
	protected final HashMap<SocketAddress,SocketServerClientHandler> serverUdpClientHandlers = new HashMap<>();
	protected final ByteBuffer                                       serverUdpNetworkBuffer;
	
	protected final int serverPortTcp;
	protected final int serverPortUdp;
	
	protected final SocketServerListener socketListener;
	
	protected       long                                  lastHandshakingClientHandlerValidation = CachedTime.millisSinceStart();
	protected final Collection<SocketServerClientHandler> handshakingClientHandlers              = new LinkedHashSet<>();
	
	protected final ByteBuffer clientDataBuffer = ByteBuffer.allocateDirect(PyroSelector.BUFFER_SIZE);
	
	protected final String addressText;
	
	
	/**
	 * Causes debug messages to be printed.<br>
	 * <br>
	 * Only call this function if you have not yet created any SocketServers, and only call this function on the same thread you will create your SocketServers in, otherwise you might run into threading problems.
	 */
	public static void setDebuggingEnabled()
	{
		SocketServer.IS_DEBUGGING = true;
	}
	
	/**
	 * Causes debug messages to be printed to the given PrintStream.<br>
	 * <br>
	 * Only call this function if you have not yet created any SocketServers, and only call this function on the same thread you will create your SocketServers in, otherwise you might run into threading problems.
	 */
	public static void setDebuggingEnabled(final PrintStream printstream)
	{
		SocketServer.IS_DEBUGGING = true;
		SocketServer.DEBUGGING_PRINTSTREAM = printstream;
	}
	
	
	public SocketServer(final boolean acceptExternalConnections, final int portTcp, final SocketServerListener listener) throws Exception
	{
		this.serverPortTcp = portTcp;
		this.serverPortUdp = 0;
		
		this.socketListener = listener;
		PyroSelector selector = new PyroSelector();
		this.server = selector.listen(acceptExternalConnections, portTcp, NEW_CONNECTIONS_QUEUE_SIZE, createServerListener());
		this.serverUdp = null;
		this.serverUdpNetworkBuffer = null;
		this.addressText = getAddressText();
	}
	
	public SocketServer(final boolean acceptExternalConnections, final int portTcp, final int portUdp, final SocketServerListener listener) throws Exception
	{
		this.serverPortTcp = portTcp;
		this.serverPortUdp = ((portUdp <= 0) ? 0 : portUdp);
		
		this.socketListener = listener;
		PyroSelector selector = new PyroSelector();
		this.server = selector.listen(acceptExternalConnections, portTcp, NEW_CONNECTIONS_QUEUE_SIZE, createServerListener());
		this.serverUdp = ((portUdp <= 0) ? null : new PyroServerUdp(acceptExternalConnections, portUdp, createServerUdpListener()));
		this.serverUdpNetworkBuffer = ((serverUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
		this.addressText = getAddressText();
	}
	
	
	public SocketServer(final String bindAddress, final int portTcp, final SocketServerListener listener) throws Exception
	{
		this.serverPortTcp = portTcp;
		this.serverPortUdp = 0;
		
		this.socketListener = listener;
		PyroSelector selector = new PyroSelector();
		this.server = selector.listen(new InetSocketAddress(bindAddress, portTcp), NEW_CONNECTIONS_QUEUE_SIZE, createServerListener());
		this.serverUdp = null;
		this.serverUdpNetworkBuffer = null;
		this.addressText = getAddressText();
	}
	
	public SocketServer(final String bindAddress, final int portTcp, final int portUdp, final SocketServerListener listener) throws Exception
	{
		this.serverPortTcp = portTcp;
		this.serverPortUdp = ((portUdp <= 0) ? 0 : portUdp);
		
		this.socketListener = listener;
		PyroSelector selector = new PyroSelector();
		this.server = selector.listen(new InetSocketAddress(bindAddress, portTcp), NEW_CONNECTIONS_QUEUE_SIZE, createServerListener());
		this.serverUdp = ((portUdp <= 0) ? null : new PyroServerUdp(new InetSocketAddress(bindAddress, portUdp), createServerUdpListener()));
		this.serverUdpNetworkBuffer = ((serverUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
		this.addressText = getAddressText();
	}
	
	
	protected PyroServerListener createServerListener()
	{
		return client ->
		{
			SocketServer socketServer = SocketServer.this;
			SocketClient socketClient = new SocketClient(socketServer, client);
			SocketServerClientHandler clientHandler = new SocketServerClientHandler(socketListener, socketServer, socketClient);
			client.setListener(clientHandler);
			clientHandler.connectedClient(client);
		};
	}
	
	protected PyroServerUdpListener createServerUdpListener()
	{
		return (client, data) ->
		{
			SocketServerClientHandler clientHandler = serverUdpClientHandlers.get(client);
			if(clientHandler != null)
			{
				clientHandler.receivedDataUdp(client, data);
			}
		};
	}
	
	
	protected void addUdpClient(final SocketServerClientHandler clientHandler)
	{
		SocketAddress clientUdp = clientHandler.socketClient.clientUdpAddress;
		if(clientUdp != null)
		{
			serverUdpClientHandlers.put(clientUdp, clientHandler);
		}
	}
	
	protected void removeUdpClient(final SocketServerClientHandler clientHandler)
	{
		SocketAddress clientUdp = clientHandler.socketClient.clientUdpAddress;
		if(clientUdp != null)
		{
			serverUdpClientHandlers.remove(clientUdp);
		}
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
		if(serverUdp != null)
		{
			for(long i = 1; i <= eventTimeout; i++)
			{
				try
				{
					serverUdp.listen(serverUdpNetworkBuffer);
				}
				catch(Exception e)
				{
					if(SocketServer.IS_DEBUGGING)
					{
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " UDP listen caused an exception:");
						SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
					}
				}
				
				try
				{
					server.selector().select(1);
				}
				catch(Exception e)
				{
					if(SocketServer.IS_DEBUGGING)
					{
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " listen caused an exception:");
						SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
					}
				}
			}
		}
		else
		{
			for(long i = 1; i <= eventTimeout; i++)
			{
				try
				{
					server.selector().select(1);
				}
				catch(Exception e)
				{
					if(SocketServer.IS_DEBUGGING)
					{
						SocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + this + " listen caused an exception:");
						SocketServer.DEBUGGING_PRINTSTREAM.println(LowEntry.getStackTrace(e));
					}
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
		
		for(Iterator<SocketServerClientHandler> iterator = handshakingClientHandlers.iterator(); iterator.hasNext(); )
		{
			SocketServerClientHandler clientHandler = iterator.next();
			if((time - clientHandler.handshakingStartTime) < 15000) // 15 seconds
			{
				// this is the oldest clientHandler
				// if this didn't timeout, the others didn't either
				return;
			}
			if(SocketServer.IS_DEBUGGING)
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
				serverUdp.shutdown();
			}
			catch(Exception e)
			{
			}
			
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
					serverUdp.shutdown();
				}
				catch(Exception e)
				{
				}
				
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
	 * Closes the server socket. Calls {@link SocketClient#disconnect()} on every open connection.
	 */
	public void terminate()
	{
		if(server.selector().isNetworkThread())
		{
			try
			{
				serverUdp.shutdown();
			}
			catch(Exception e)
			{
			}
			
			try
			{
				server.close();
			}
			catch(Exception e)
			{
			}
			
			for(SocketClient client : this)
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
					serverUdp.shutdown();
				}
				catch(Exception e)
				{
				}
				
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
				
				for(SocketClient client : SocketServer.this)
				{
					client.disconnect();
				}
			});
		}
	}
	
	/**
	 * Closes the server socket. Calls {@link SocketClient#disconnectImmediately()} on every open connection.
	 */
	public void terminateImmediately()
	{
		if(server.selector().isNetworkThread())
		{
			try
			{
				serverUdp.shutdown();
			}
			catch(Exception e)
			{
			}
			
			try
			{
				server.close();
			}
			catch(Exception e)
			{
			}
			
			for(SocketClient client : this)
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
					serverUdp.shutdown();
				}
				catch(Exception e)
				{
				}
				
				try
				{
					server.close();
				}
				catch(Exception e)
				{
				}
				
				for(SocketClient client : SocketServer.this)
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
	public Iterator<SocketClient> iterator()
	{
		List<SocketClient> copy;
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
	public void forEachClient(final Consumer<SocketClient> action)
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
	 * Returns the PyroNet UDP object of this wrapper.
	 */
	public PyroServerUdp pyroUdp()
	{
		return serverUdp;
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
	 * Returns the TCP port this server listens to.
	 */
	public int getPortTcp()
	{
		return serverPortTcp;
	}
	
	/**
	 * Returns the UDP port this server listens to.<br>
	 * <br>
	 * Returns 0 if the server isn't listening for UDP.
	 */
	public int getPortUdp()
	{
		return serverPortUdp;
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
		if(serverUdp == null)
		{
			return server.getAddressText();
		}
		return server.getAddressText() + ", " + serverUdp.getAddressText();
	}
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
}
