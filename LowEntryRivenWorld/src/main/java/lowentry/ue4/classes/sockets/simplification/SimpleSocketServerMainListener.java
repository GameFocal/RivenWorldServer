package lowentry.ue4.classes.sockets.simplification;


import lowentry.ue4.classes.sockets.SimpleSocketClient;
import lowentry.ue4.classes.sockets.SimpleSocketServer;
import lowentry.ue4.classes.sockets.SimpleSocketServerListener;
import lowentry.ue4.classes.sockets.ThreadedSimpleSocketConnection;
import lowentry.ue4.classes.sockets.ThreadedSimpleSocketConnectionListener;
import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;


public abstract class SimpleSocketServerMainListener implements SimpleSocketServerListener, Iterable<SimpleSocketClient>
{
	private final Thread                                    networkThread     = Thread.currentThread();
	private final SimpleBlockingQueue<Runnable>             tasks             = new SimpleBlockingQueue<>();
	private final ArrayList<ThreadedSimpleSocketConnection> socketConnections = new ArrayList<>();
	
	private volatile SimpleSocketServer server;
	private volatile boolean            shouldRestart = false;
	private volatile boolean            shouldStop    = false;
	
	
	// -------------------------------- //
	
	
	@Override
	public final void clientConnected(final SimpleSocketServer server, final SimpleSocketClient client)
	{
		try
		{
			clientConnected(client);
		}
		catch(Exception e)
		{
			client.disconnect();
			clientErrored(e);
		}
	}
	
	@Override
	public final void clientDisconnected(final SimpleSocketServer server, final SimpleSocketClient client)
	{
		try
		{
			clientDisconnected(client);
		}
		catch(Exception e)
		{
			clientErrored(e);
		}
	}
	
	@Override
	public final boolean canReceivePacket(final SimpleSocketServer server, final SimpleSocketClient client, final int bytes)
	{
		try
		{
			return canReceivePacket(client, bytes);
		}
		catch(Exception e)
		{
			client.disconnect();
			clientErrored(e);
			return false;
		}
	}
	
	@Override
	public final void receivedPacket(final SimpleSocketServer server, final SimpleSocketClient client, final byte[] bytes)
	{
		try
		{
			receivedPacket(client, bytes);
		}
		catch(Exception e)
		{
			client.disconnect();
			clientErrored(e);
		}
	}
	
	
	public abstract void hasStarted();
	public abstract void beforeGracefulShutdown();
	public abstract long getMaxTimeMsForGracefulShutdown();
	public abstract void hasStopped();
	public abstract void serverErrored(Exception e);
	public abstract void clientErrored(Exception e);
	
	public abstract void clientConnected(SimpleSocketClient client);
	public abstract void clientDisconnected(SimpleSocketClient client);
	
	public abstract long getTimeMsBeforeNextTick();
	public abstract void tick();
	
	public abstract boolean canReceivePacket(SimpleSocketClient client, int bytes);
	public abstract void receivedPacket(SimpleSocketClient client, byte[] bytes);
	
	
	// -------------------------------- //
	
	
	public final boolean isNetworkThread()
	{
		return (Thread.currentThread() == networkThread);
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
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public final void execute(final Runnable runnable)
	{
		if(runnable == null)
		{
			throw new NullPointerException();
		}
		
		if(isNetworkThread())
		{
			try
			{
				runnable.run();
			}
			catch(Exception cause)
			{
				cause.printStackTrace();
			}
		}
		else
		{
			tasks.put(runnable);
		}
	}
	
	
	protected final void executePendingTasks()
	{
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
		
		for(ThreadedSimpleSocketConnection connection : socketConnections)
		{
			connection.executePendingTasks();
		}
		
		try
		{
			executePendingUserTasks();
		}
		catch(Exception cause)
		{
			cause.printStackTrace();
		}
	}
	
	protected void executePendingUserTasks()
	{
		// you can override this function to add your own tasks, these tasks will be executed on the network thread
		// this function will run at least about once every 100 milliseconds, but it can run far more often than that (so make sure it's not too costly to run, else compare times yourself to figure out if to run it or not)
	}
	
	
	// -------------------------------- //
	
	
	public final ThreadedSimpleSocketConnection addSocketConnection(final String host, final int port, final ThreadedSimpleSocketConnectionListener listener)
	{
		checkThread();
		ThreadedSimpleSocketConnection connection = new ThreadedSimpleSocketConnection(host, port, listener);
		connection.startAsync();
		socketConnections.add(connection);
		return connection;
	}
	
	public final void removeSocketConnection(final ThreadedSimpleSocketConnection connection)
	{
		checkThread();
		if(connection == null)
		{
			return;
		}
		connection.stop();
		connection.executePendingTasks();
		socketConnections.remove(connection);
	}
	
	
	// -------------------------------- //
	
	
	protected final void setServer(final SimpleSocketServer server)
	{
		this.server = server;
		this.shouldRestart = false;
	}
	
	
	public final void restart()
	{
		shouldRestart = true;
	}
	
	public final void stop()
	{
		shouldStop = true;
	}
	
	
	public boolean getShouldRestart()
	{
		return shouldRestart;
	}
	
	public boolean getShouldStop()
	{
		return shouldStop;
	}
	
	
	// -------------------------------- //
	
	
	/**
	 * Returns an iterator to access all connected clients of this server.
	 */
	@Override
	public Iterator<SimpleSocketClient> iterator()
	{
		return server.iterator();
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
		server.forEach(action);
	}
	
	/**
	 * Returns the amount of connected clients.
	 */
	public int getClientCount()
	{
		return server.getClientCount();
	}
	
	
	/**
	 * Returns the PyroNet object of this wrapper.
	 */
	public PyroServer pyro()
	{
		return server.pyro();
	}
	
	/**
	 * Returns the selector.
	 */
	public PyroSelector selector()
	{
		return server.selector();
	}
	
	
	/**
	 * Returns the port this server listens to.
	 */
	public int getPort()
	{
		return server.getPort();
	}
	
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + server + "]";
	}
}
