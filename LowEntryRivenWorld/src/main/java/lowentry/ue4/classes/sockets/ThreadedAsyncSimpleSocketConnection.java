package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.utility.ThreadSleeper;
import lowentry.ue4.classes.sockets.SocketTasks.ConnectedThreadedAsyncSimpleSocketConnection;
import lowentry.ue4.classes.sockets.SocketTasks.DisconnectedThreadedAsyncSimpleSocketConnection;
import lowentry.ue4.classes.sockets.SocketTasks.ReceivedPacketThreadedAsyncSimpleSocketConnection;
import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Will call the given listener from within the thread of the connection, so you'll have to make sure your code is thread-safe.
 */
public class ThreadedAsyncSimpleSocketConnection
{
	private static       int    ID             = 1;
	private static final Object idSynchronizer = new Object();
	
	protected final Object startSynchronizer = new Object();
	
	protected final ThreadSleeper reconnectSleeper = new ThreadSleeper();
	protected final ThreadSleeper startSleeper     = new ThreadSleeper();
	protected final ThreadSleeper stopSleeper      = new ThreadSleeper();
	
	protected final String                                      host;
	protected final int                                         port;
	protected final ThreadedAsyncSimpleSocketConnectionListener listener;
	
	protected volatile SimpleSocketConnection connection;
	protected volatile Object                 attachment;
	protected volatile boolean                connected = false;
	protected volatile boolean                run       = true;
	
	protected final    SimpleBlockingQueue<Runnable> tasks   = new SimpleBlockingQueue<>();
	protected volatile boolean                       started = false;
	
	
	
	private static final class PrivateSimpleSocketConnectionListener implements SimpleSocketConnectionListener
	{
		public final ThreadedAsyncSimpleSocketConnection self;
		public final AtomicBoolean                       connectedWasCalled = new AtomicBoolean(false);
		
		
		public PrivateSimpleSocketConnectionListener(final ThreadedAsyncSimpleSocketConnection self)
		{
			this.self = self;
		}
		
		
		@Override
		public void connected(final SimpleSocketConnection connection)
		{
			if(!self.run)
			{
				return;
			}
			connectedWasCalled.set(true);
			self.tasks.put(new ConnectedThreadedAsyncSimpleSocketConnection(self, self.listener, connection));
		}
		
		@Override
		public void disconnected(final SimpleSocketConnection connection)
		{
			if(!self.run)
			{
				return;
			}
			if(!connectedWasCalled.getAndSet(false))
			{
				return;
			}
			self.tasks.put(new DisconnectedThreadedAsyncSimpleSocketConnection(self, self.listener, connection));
		}
		
		@Override
		public void receivedPacket(final SimpleSocketConnection connection, final byte[] bytes)
		{
			if(!self.run)
			{
				return;
			}
			self.tasks.put(new ReceivedPacketThreadedAsyncSimpleSocketConnection(self, self.listener, connection, bytes));
		}
	}
	
	
	
	public ThreadedAsyncSimpleSocketConnection(final String host, final int port, final ThreadedAsyncSimpleSocketConnectionListener listener)
	{
		this.host = host;
		this.port = port;
		this.listener = listener;
	}
	
	public ThreadedAsyncSimpleSocketConnection(final String host, final int port, final SimpleSocketConnectionListener listener)
	{
		this.host = host;
		this.port = port;
		this.listener = getWrappedListener(listener);
	}
	
	
	public static ThreadedAsyncSimpleSocketConnectionListener getWrappedListener(final SimpleSocketConnectionListener listener)
	{
		return new ThreadedAsyncSimpleSocketConnectionListener()
		{
			@Override
			public void connected(final ThreadedAsyncSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection)
			{
				listener.connected(connection);
			}
			@Override
			public void disconnected(final ThreadedAsyncSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection)
			{
				listener.disconnected(connection);
			}
			@Override
			public void receivedPacket(final ThreadedAsyncSimpleSocketConnection threadedConnection, final SimpleSocketConnection connection, final byte[] bytes)
			{
				listener.receivedPacket(connection, bytes);
			}
		};
	}
	
	
	/**
	 * Starts connecting to the server.<br>
	 * <br>
	 * This method will not wait till a connection attempt has been made, it will return pretty much immediately.<br>
	 * <br>
	 * When there is no connection, this class will keep attempting to connect once every 1000 milliseconds.
	 */
	public void startAsync()
	{
		startAsync(Thread.NORM_PRIORITY);
	}
	/**
	 * Starts connecting to the server.<br>
	 * <br>
	 * This method will not wait till a connection attempt has been made, it will return pretty much immediately.<br>
	 * <br>
	 * When there is no connection, this class will keep attempting to connect once every 1000 milliseconds.
	 */
	public void startAsync(final int threadPriority)
	{
		start(false, threadPriority);
	}
	/**
	 * Starts connecting to the server.<br>
	 * <br>
	 * This method will wait till a connection attempt has been made.<br>
	 * After this method, {@link #isConnected()} will return true or false depending on whether the first connection attempt was successful.<br>
	 * <br>
	 * When there is no connection, this class will keep attempting to connect once every 1000 milliseconds.
	 */
	public void start()
	{
		start(Thread.NORM_PRIORITY);
	}
	/**
	 * Starts connecting to the server.<br>
	 * <br>
	 * This method will wait till a connection attempt has been made.<br>
	 * After this method, {@link #isConnected()} will return true or false depending on whether the first connection attempt was successful.<br>
	 * <br>
	 * When there is no connection, this class will keep attempting to connect once every 1000 milliseconds.
	 */
	public void start(final int threadPriority)
	{
		start(true, threadPriority);
	}
	/**
	 * Starts connecting to the server.<br>
	 * <br>
	 * This method will wait till a connection attempt has been made if the given waitForStart is true.<br>
	 * After this method, {@link #isConnected()} will return true or false depending on whether the first connection attempt was successful.<br>
	 * <br>
	 * When there is no connection, this class will keep attempting to connect once every 1000 milliseconds.
	 */
	protected void start(final boolean waitForStart, final int threadPriority)
	{
		synchronized(startSynchronizer)
		{
			if(connection != null)
			{
				stop();
			}
			
			run = true;
			
			started = false;
			Thread thread = new Thread(() ->
			{
				PrivateSimpleSocketConnectionListener listenerWrapper = new PrivateSimpleSocketConnectionListener(ThreadedAsyncSimpleSocketConnection.this);
				connection = new SimpleSocketConnection(host, port, listenerWrapper);
				connected = connection.connect();
				started = true;
				startSleeper.wakeup();
				
				while(run)
				{
					if(!connected)
					{
						reconnectSleeper.sleep(1000);
						if(!run)
						{
							break;
						}
						connected = connection.connect();
					}
					else
					{
						if(connection.isConnected())
						{
							connection.listen();
						}
						else
						{
							connected = false;
						}
					}
					executePendingTasks();
				}
				
				while(connection.isConnected() && connection.pyro().hasDataEnqueued())
				{
					connection.listen(10);
				}
				executePendingTasks();
				
				if(connected)
				{
					connection.disconnect();
					
					connected = false;
					if(listenerWrapper.connectedWasCalled.getAndSet(false))
					{
						listener.disconnected(ThreadedAsyncSimpleSocketConnection.this, connection);
					}
				}
				
				executePendingTasks();
				
				connection = null;
				stopSleeper.wakeup();
			}, "Threaded-Simple-Socket-Connection-Async-" + getNextId());
			thread.setDaemon(true);
			thread.setPriority(threadPriority);
			thread.start();
			
			if(waitForStart)
			{
				waitTillStarted();
			}
		}
	}
	/**
	 * Waits till a connection attempt has been made.
	 */
	public void waitTillStarted()
	{
		while(!started)
		{
			startSleeper.sleep(50);
		}
	}
	
	/**
	 * Returns true if there is a connection, returns false otherwise.
	 */
	public boolean isConnected()
	{
		return connected;
	}
	/**
	 * Returns true if start has been called, and stop hasn't been called yet.
	 */
	public boolean isRunning()
	{
		synchronized(startSynchronizer)
		{
			return run;
		}
	}
	
	/**
	 * Disconnects and stops automatically reconnecting.<br>
	 * <br>
	 * Does not wait till the connection has been broken.
	 */
	public void stopAsync()
	{
		synchronized(startSynchronizer)
		{
			run = false;
			wakeup();
		}
	}
	/**
	 * Disconnects and stops automatically reconnecting.<br>
	 * <br>
	 * Waits till the connection has been broken.
	 */
	public void stop()
	{
		synchronized(startSynchronizer)
		{
			run = false;
			wakeup();
			waitTillStopped();
		}
	}
	/**
	 * Waits till the connection has been broken.
	 */
	public void waitTillStopped()
	{
		while(connection != null)
		{
			stopSleeper.sleep(50);
		}
	}
	
	
	/**
	 * Causes the threaded connection to check the run state again.
	 */
	protected void wakeup()
	{
		reconnectSleeper.wakeup();
		
		final SimpleSocketConnection finalConnection = connection;
		if(finalConnection != null)
		{
			try
			{
				finalConnection.selector().wakeup();
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	/**
	 * Executes the given events in the threaded {@link SimpleSocketConnection#listen()} loop.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread the connection is running in!
	 */
	private void executePendingTasks()
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
	}
	
	
	/**
	 * This will add a task that will be executed during the next {@link #executePendingTasks()}, <br>
	 * which will cause the task to be executed in the same thread the connection runs in.
	 */
	public void addTask(final Runnable task)
	{
		if(task != null)
		{
			tasks.put(task);
		}
	}
	
	
	/**
	 * Returns the local socket address (host and port), can return null.
	 */
	public InetSocketAddress getLocalAddress()
	{
		final SimpleSocketConnection finalConnection = connection;
		if(finalConnection == null)
		{
			return null;
		}
		return finalConnection.getLocalAddress();
	}
	
	/**
	 * Returns the remote socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddress()
	{
		final SimpleSocketConnection finalConnection = connection;
		if(finalConnection == null)
		{
			return null;
		}
		return finalConnection.getRemoteAddress();
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendPacket(final byte[]... bytes)
	{
		final SimpleSocketConnection finalConnection = connection;
		if(!connected || (finalConnection == null))
		{
			return;
		}
		finalConnection.sendPacket(bytes);
	}
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendPacket(final byte[] bytes)
	{
		final SimpleSocketConnection finalConnection = connection;
		if(!connected || (finalConnection == null))
		{
			return;
		}
		finalConnection.sendPacket(bytes);
	}
	
	
	/**
	 * Returns the current connection, can be null.
	 */
	public SimpleSocketConnection connection()
	{
		return connection;
	}
	
	
	@Override
	public String toString()
	{
		final SimpleSocketConnection finalConnection = connection;
		if(finalConnection != null)
		{
			return finalConnection.toString();
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
