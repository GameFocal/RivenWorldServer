package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.classes.sockets.SocketConnectionHandler.ConnectingStage;
import lowentry.ue4.classes.sockets.SocketTasks.CanceledLatentFunctionCallListener;
import lowentry.ue4.classes.sockets.SocketTasks.FailedFunctionCallListener;
import lowentry.ue4.classes.sockets.SocketTasks.FailedLatentFunctionCallListener;
import lowentry.ue4.classes.sockets.SocketTasks.ReceivedResponseFunctionCallListener;
import lowentry.ue4.classes.sockets.SocketTasks.ReceivedResponseLatentFunctionCallListener;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.craterstudio.util.concur.SimpleBlockingQueue;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.PyroClientUdp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class SocketConnection
{
	protected static class InternalFunctionCall
	{
		FunctionCallListener listener;
		long                 timeoutTime;
	}
	
	
	public interface FunctionCallListener
	{
		void receivedResponse(final SocketConnection connection, final byte[] bytes);
		void failed(final SocketConnection connection);
	}
	
	
	protected static class InternalLatentFunctionCall
	{
		LatentFunctionCall         latentAction;
		LatentFunctionCallListener listener;
		long                       timeoutTime;
	}
	
	
	public interface LatentFunctionCallListener
	{
		void receivedResponse(final SocketConnection connection, final byte[] bytes);
		void canceled(final SocketConnection connection);
		void failed(final SocketConnection connection);
	}
	
	
	protected final PyroSelector             selector;
	protected final InetSocketAddress        address;
	protected final InetSocketAddress        addressUdp;
	protected final SocketConnectionListener socketListener;
	
	protected final SimpleBlockingQueue<Runnable> tasks;
	
	protected          PyroClient connection;
	protected volatile Object     attachment;
	protected          boolean    isDisconnecting;
	
	protected       PyroClientUdp connectionUdp;
	protected final ByteBuffer    connectionUdpNetworkBuffer;
	
	protected       int                                   nextFunctionCallId = 0;
	protected final HashMap<Integer,InternalFunctionCall> functionCalls      = new HashMap<>();
	
	protected       int                                         nextLatentFunctionCallId = 0;
	protected final HashMap<Integer,InternalLatentFunctionCall> latentFunctionCalls      = new HashMap<>();
	
	protected long lastFunctionCallTimeoutCheck = CachedTime.millisSinceStart();
	
	
	public SocketConnection(final String host, final int portTcp, final SocketConnectionListener listener)
	{
		this.socketListener = listener;
		this.tasks = null;
		selector = new PyroSelector();
		address = new InetSocketAddress(host, portTcp);
		addressUdp = null;
		connectionUdpNetworkBuffer = null;
	}
	
	public SocketConnection(final InetSocketAddress endTcp, final SocketConnectionListener listener)
	{
		this.socketListener = listener;
		this.tasks = null;
		selector = new PyroSelector();
		address = endTcp;
		addressUdp = null;
		connectionUdpNetworkBuffer = null;
	}
	
	public SocketConnection(final String host, final int portTcp, final int portUdp, final SocketConnectionListener listener)
	{
		this.socketListener = listener;
		this.tasks = null;
		selector = new PyroSelector();
		address = new InetSocketAddress(host, portTcp);
		addressUdp = ((portUdp <= 0) ? null : new InetSocketAddress(host, portUdp));
		connectionUdpNetworkBuffer = ((addressUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
	}
	
	public SocketConnection(final InetSocketAddress endTcp, final InetSocketAddress endUdp, final SocketConnectionListener listener)
	{
		this.socketListener = listener;
		this.tasks = null;
		selector = new PyroSelector();
		address = endTcp;
		addressUdp = endUdp;
		connectionUdpNetworkBuffer = ((addressUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
	}
	
	
	public SocketConnection(final String host, final int portTcp, final SocketConnectionListener listener, final SimpleBlockingQueue<Runnable> tasks)
	{
		this.socketListener = listener;
		this.tasks = tasks;
		selector = new PyroSelector();
		address = new InetSocketAddress(host, portTcp);
		addressUdp = null;
		connectionUdpNetworkBuffer = null;
	}
	
	public SocketConnection(final InetSocketAddress endTcp, final SocketConnectionListener listener, final SimpleBlockingQueue<Runnable> tasks)
	{
		this.socketListener = listener;
		this.tasks = tasks;
		selector = new PyroSelector();
		address = endTcp;
		addressUdp = null;
		connectionUdpNetworkBuffer = null;
	}
	
	public SocketConnection(final String host, final int portTcp, final int portUdp, final SocketConnectionListener listener, final SimpleBlockingQueue<Runnable> tasks)
	{
		this.socketListener = listener;
		this.tasks = tasks;
		selector = new PyroSelector();
		address = new InetSocketAddress(host, portTcp);
		addressUdp = ((portUdp <= 0) ? null : new InetSocketAddress(host, portUdp));
		connectionUdpNetworkBuffer = ((addressUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
	}
	
	public SocketConnection(final InetSocketAddress endTcp, final InetSocketAddress endUdp, final SocketConnectionListener listener, final SimpleBlockingQueue<Runnable> tasks)
	{
		this.socketListener = listener;
		this.tasks = tasks;
		selector = new PyroSelector();
		address = endTcp;
		addressUdp = endUdp;
		connectionUdpNetworkBuffer = ((addressUdp == null) ? null : ByteBuffer.allocate(PyroSelector.BUFFER_SIZE));
	}
	
	
	/**
	 * Tries to connect, returns true if the connection was successful.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public boolean connect()
	{
		selector.checkThread();
		
		if(connectionUdp != null)
		{
			connectionUdp.shutdown();
			connectionUdp = null;
		}
		if(connection != null)
		{
			connection.shutdown();
			connection = null;
		}
		failAllFunctionCalls();
		
		try
		{
			isDisconnecting = false;
			
			SocketConnectionHandler listener = createListener();
			connection = selector.connect(address, listener);
			if(connection == null)
			{
				return false;
			}
			
			while(listener.connectingStage == ConnectingStage.WAITING)
			{
				listen(1);
			}
			if((listener.connectingStage == ConnectingStage.UNCONNECTABLE) || connection.isDisconnected())
			{
				connection.shutdown();
				connection = null;
				return false;
			}
			
			connection.write(ByteBuffer.wrap(new byte[]{13, 10, 13, 10})); // \r\n\r\n
			
			if(isConnected())
			{
				if(addressUdp != null)
				{
					connectionUdp = new PyroClientUdp(addressUdp, listener);
					connection.write(ByteBuffer.wrap(LowEntry.integerToBytes(addressUdp.getPort())));
					connection.write(ByteBuffer.wrap(LowEntry.integerToBytes(getLocalPortUdp())));
				}
				else
				{
					connection.write(ByteBuffer.wrap(new byte[8]));
				}
				
				listener.callConnected();
			}
			else
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			if(connectionUdp != null)
			{
				connectionUdp.shutdown();
				connectionUdp = null;
			}
			if(connection != null)
			{
				connection.shutdown();
				connection = null;
			}
			return false;
		}
		return true;
	}
	
	protected SocketConnectionHandler createListener()
	{
		return new SocketConnectionHandler(socketListener, this);
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
		selector.checkThread();
		
		if(eventTimeout <= 10) // too small for CachedTime.millisSinceStart()
		{
			pyroListen(eventTimeout);
			handleFunctionCallTimeouts();
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
		
		handleFunctionCallTimeouts();
	}
	
	private void pyroListen(final long eventTimeout)
	{
		if(connectionUdp != null)
		{
			for(long i = 1; i <= eventTimeout; i++)
			{
				try
				{
					connectionUdp.listen(connectionUdpNetworkBuffer);
				}
				catch(Exception e)
				{
				}
				
				try
				{
					selector.select(1);
				}
				catch(Exception e)
				{
				}
			}
		}
		else
		{
			for(long i = 1; i <= eventTimeout; i++)
			{
				try
				{
					selector.select(1);
				}
				catch(Exception e)
				{
				}
			}
		}
	}
	
	
	protected void threadSafeFailedFunctionCall(final FunctionCallListener listener)
	{
		if(tasks == null)
		{
			if(selector.isNetworkThread())
			{
				listener.failed(this);
			}
			else
			{
				selector.scheduleTask(new FailedFunctionCallListener(listener, this));
			}
		}
		else
		{
			tasks.put(new FailedFunctionCallListener(listener, this));
		}
	}
	protected void failedFunctionCall(final FunctionCallListener listener)
	{
		if(tasks == null)
		{
			listener.failed(this);
		}
		else
		{
			tasks.put(new FailedFunctionCallListener(listener, this));
		}
	}
	protected void receivedResponseFunctionCall(final FunctionCallListener listener, final byte[] bytes)
	{
		if(tasks == null)
		{
			listener.receivedResponse(this, bytes);
		}
		else
		{
			tasks.put(new ReceivedResponseFunctionCallListener(listener, this, bytes));
		}
	}
	
	
	protected void threadSafeFailedLatentFunctionCall(final LatentFunctionCallListener listener)
	{
		if(tasks == null)
		{
			if(selector.isNetworkThread())
			{
				listener.failed(this);
			}
			else
			{
				selector.scheduleTask(new FailedLatentFunctionCallListener(listener, this));
			}
		}
		else
		{
			tasks.put(new FailedLatentFunctionCallListener(listener, this));
		}
	}
	protected void threadSafeCanceledLatentFunctionCall(final LatentFunctionCallListener listener)
	{
		if(tasks == null)
		{
			if(selector.isNetworkThread())
			{
				listener.canceled(this);
			}
			else
			{
				selector.scheduleTask(new CanceledLatentFunctionCallListener(listener, this));
			}
		}
		else
		{
			tasks.put(new CanceledLatentFunctionCallListener(listener, this));
		}
	}
	protected void failedLatentFunctionCall(final LatentFunctionCallListener listener)
	{
		if(tasks == null)
		{
			listener.failed(this);
		}
		else
		{
			tasks.put(new FailedLatentFunctionCallListener(listener, this));
		}
	}
	protected void canceledLatentFunctionCall(final LatentFunctionCallListener listener)
	{
		if(tasks == null)
		{
			listener.canceled(this);
		}
		else
		{
			tasks.put(new CanceledLatentFunctionCallListener(listener, this));
		}
	}
	protected void receivedResponseLatentFunctionCall(final LatentFunctionCallListener listener, final byte[] bytes)
	{
		if(tasks == null)
		{
			listener.receivedResponse(this, bytes);
		}
		else
		{
			tasks.put(new ReceivedResponseLatentFunctionCallListener(listener, this, bytes));
		}
	}
	
	
	/**
	 * Takes care of the timeout functionality of the function calls.
	 */
	protected void handleFunctionCallTimeouts()
	{
		long time = CachedTime.millisSinceStart();
		if((time - lastFunctionCallTimeoutCheck) < 1000) // 1 second
		{
			return;
		}
		lastFunctionCallTimeoutCheck = time;
		
		
		{// function calls >>
			List<FunctionCallListener> functionCallListeners = new LinkedList<>();
			synchronized(functionCalls)
			{
				for(Iterator<Entry<Integer,InternalFunctionCall>> iterator = functionCalls.entrySet().iterator(); iterator.hasNext(); )
				{
					Entry<Integer,InternalFunctionCall> entry = iterator.next();
					InternalFunctionCall functionCall = entry.getValue();
					if((time - functionCall.timeoutTime) >= 0)
					{
						iterator.remove();
						if(functionCall.listener != null)
						{
							functionCallListeners.add(functionCall.listener);
						}
					}
				}
			}
			for(FunctionCallListener listener : functionCallListeners)
			{
				failedFunctionCall(listener);
			}
		}// function calls <<
		
		
		{// latent function calls >>
			List<InternalLatentFunctionCall> functionCallListeners = new LinkedList<>();
			synchronized(latentFunctionCalls)
			{
				for(Iterator<Entry<Integer,InternalLatentFunctionCall>> iterator = latentFunctionCalls.entrySet().iterator(); iterator.hasNext(); )
				{
					Entry<Integer,InternalLatentFunctionCall> entry = iterator.next();
					InternalLatentFunctionCall functionCall = entry.getValue();
					if((time - functionCall.timeoutTime) >= 0)
					{
						iterator.remove();
						if(functionCall.listener != null)
						{
							functionCallListeners.add(functionCall);
						}
					}
				}
			}
			for(InternalLatentFunctionCall functionCall : functionCallListeners)
			{
				if(functionCall.latentAction != null)
				{
					functionCall.latentAction.canceledByTimeout();
				}
				if(functionCall.listener != null)
				{
					failedLatentFunctionCall(functionCall.listener);
				}
			}
		}// latent function calls <<
	}
	
	/**
	 * Fails all remaining function calls.
	 */
	public void failAllFunctionCalls()
	{
		{// function calls >>
			List<FunctionCallListener> functionCallListeners = new LinkedList<>();
			synchronized(functionCalls)
			{
				for(Entry<Integer,InternalFunctionCall> entry : functionCalls.entrySet())
				{
					InternalFunctionCall functionCall = entry.getValue();
					if(functionCall.listener != null)
					{
						functionCallListeners.add(functionCall.listener);
					}
				}
				functionCalls.clear();
			}
			for(FunctionCallListener listener : functionCallListeners)
			{
				failedFunctionCall(listener);
			}
		}// function calls <<
		
		
		{// latent function calls >>
			List<InternalLatentFunctionCall> functionCallListeners = new LinkedList<>();
			synchronized(latentFunctionCalls)
			{
				for(Entry<Integer,InternalLatentFunctionCall> entry : latentFunctionCalls.entrySet())
				{
					InternalLatentFunctionCall functionCall = entry.getValue();
					if(functionCall.listener != null)
					{
						functionCallListeners.add(functionCall);
					}
				}
				latentFunctionCalls.clear();
			}
			for(InternalLatentFunctionCall functionCall : functionCallListeners)
			{
				if(functionCall.latentAction != null)
				{
					functionCall.latentAction.canceledByDisconnecting();
				}
				if(functionCall.listener != null)
				{
					failedLatentFunctionCall(functionCall.listener);
				}
			}
		}// latent function calls <<
	}
	
	
	/**
	 * Returns the next unused function call id.
	 */
	protected int reserveFunctionCallId(final InternalFunctionCall functionCall)
	{
		synchronized(functionCalls)
		{
			while(true)
			{
				if(nextFunctionCallId >= Integer.MAX_VALUE)
				{
					nextFunctionCallId = 0;
				}
				nextFunctionCallId++;
				if(!functionCalls.containsKey(nextFunctionCallId))
				{
					int functionCallId = nextFunctionCallId;
					functionCalls.put(functionCallId, functionCall);
					return functionCallId;
				}
			}
		}
	}
	
	/**
	 * Returns the next unused latent function call id.
	 */
	protected int reserveLatentFunctionCallId(final InternalLatentFunctionCall functionCall)
	{
		synchronized(latentFunctionCalls)
		{
			while(true)
			{
				if(nextLatentFunctionCallId >= Integer.MAX_VALUE)
				{
					nextLatentFunctionCallId = 0;
				}
				nextLatentFunctionCallId++;
				if(!latentFunctionCalls.containsKey(nextLatentFunctionCallId))
				{
					int functionCallId = nextLatentFunctionCallId;
					latentFunctionCalls.put(functionCallId, functionCall);
					return functionCallId;
				}
			}
		}
	}
	
	
	/**
	 * Attach any object to the connection, for example to store session information.<br>
	 * Only one object can be attached at a time!
	 */
	public void setAttachment(final Object attachment)
	{
		this.attachment = attachment;
	}
	
	/**
	 * Returns the previously attached object, or <code>null</code> if none is set.<br>
	 * Call this function like this:<br>
	 * <br>
	 * <code>YourClass object = connection.getAttachment();</code><br>
	 * <br>
	 * This will automatically take the type that the compiler thinks it has to be.<br>
	 * To control the type manually, call this function like this:<br>
	 * <br>
	 * <code>YourClass object = connection.&lt;YourClass&gt;getAttachment();</code>
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttachment()
	{
		return (T) attachment;
	}
	
	/**
	 * Returns true if the connection has an attachment.
	 */
	public boolean hasAttachment()
	{
		return (attachment != null);
	}
	
	
	/**
	 * Returns the local socket address (host and port), can return null.
	 */
	public InetSocketAddress getLocalAddress()
	{
		if(connection == null)
		{
			return null;
		}
		return connection.getLocalAddress();
	}
	
	/**
	 * Returns the remote socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddress()
	{
		if(connection == null)
		{
			return null;
		}
		return connection.getRemoteAddress();
	}
	
	/**
	 * Returns the local UDP port, returns 0 if there is no UDP connection.
	 */
	public int getLocalPortUdp()
	{
		if(connectionUdp == null)
		{
			return 0;
		}
		return connectionUdp.getLocalPort();
	}
	
	
	/**
	 * Will send the bytes immediately.
	 */
	public void sendUnreliableMessage(final byte[]... bytes)
	{
		sendUnreliableMessage(LowEntry.mergeBytes(bytes));
	}
	/**
	 * Will send the bytes immediately.
	 */
	public void sendUnreliableMessage(final byte[] bytes)
	{
		if(selector.isNetworkThread())
		{
			if(bytes == null)
			{
				sendUnreliableMessageCode(ByteBuffer.allocate(0), true);
			}
			else
			{
				sendUnreliableMessageCode(ByteBuffer.wrap(bytes), true);
			}
		}
		else
		{
			if(bytes == null)
			{
				selector.scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.allocate(0), true));
			}
			else
			{
				selector.scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.wrap(bytes), true));
			}
		}
	}
	/**
	 * Will send the bytes immediately, the ByteBuffer can be cleared and reused after calling this function.
	 */
	public void sendUnreliableMessage(final ByteBuffer bytes)
	{
		if(selector.isNetworkThread())
		{
			if(bytes == null)
			{
				sendUnreliableMessageCode(ByteBuffer.allocate(0), true);
			}
			else
			{
				sendUnreliableMessageCode(bytes, false);
			}
		}
		else
		{
			if(bytes == null)
			{
				selector.scheduleTask(() -> sendUnreliableMessageCode(ByteBuffer.allocate(0), true));
			}
			else
			{
				final ByteBuffer b = LowEntry.cloneByteBuffer(bytes, false);
				selector.scheduleTask(() -> sendUnreliableMessageCode(b, true));
			}
		}
	}
	protected void sendUnreliableMessageCode(final ByteBuffer bytes, final boolean clonedBytes)
	{
		if(!isConnectedUdp())
		{
			return;
		}
		
		connectionUdp.write(bytes);
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendMessage(final byte[]... bytes)
	{
		sendMessage(LowEntry.mergeBytes(bytes));
	}
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendMessage(final byte[] bytes)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(selector.isNetworkThread())
		{
			sendMessageCode(b);
		}
		else
		{
			selector.scheduleTask(() -> sendMessageCode(b));
		}
	}
	protected void sendMessageCode(final byte[] bytes)
	{
		if(!isConnected())
		{
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(bytes.length));
		buffer.put(SocketMessageType.MESSAGE);
		SocketFunctions.putUint(buffer, bytes.length);
		buffer.flip();
		
		try
		{
			connection.write(buffer);
			if(bytes.length > 0)
			{
				connection.write(ByteBuffer.wrap(bytes));
			}
		}
		catch(PyroException e)
		{
		}
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public void sendFunctionCall(final byte[] bytes, final FunctionCallListener functionCallListener)
	{
		sendFunctionCall(30, bytes, functionCallListener);
	}
	
	/**
	 * Will enqueue the bytes to send them.<br>
	 * <br>
	 * Timeout is the timeout in seconds.
	 */
	public void sendFunctionCall(final float timeout, final byte[] bytes, final FunctionCallListener functionCallListener)
	{
		final byte[] b = ((bytes == null) ? new byte[0] : bytes);
		if(selector.isNetworkThread())
		{
			sendFunctionCallCode(b, timeout, functionCallListener);
		}
		else
		{
			selector.scheduleTask(() -> sendFunctionCallCode(b, timeout, functionCallListener));
		}
	}
	protected void sendFunctionCallCode(final byte[] bytes, final float timeout, final FunctionCallListener functionCallListener)
	{
		if(!isConnected())
		{
			threadSafeFailedFunctionCall(functionCallListener);
			return;
		}
		
		
		InternalFunctionCall functionCall = new InternalFunctionCall();
		functionCall.listener = functionCallListener;
		functionCall.timeoutTime = CachedTime.millisSinceStart() + (long) (timeout * 1000.0);
		
		int functionCallId = reserveFunctionCallId(functionCall);
		
		
		ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId) + SocketFunctions.uintByteCount(bytes.length));
		buffer.put(SocketMessageType.FUNCTION_CALL);
		SocketFunctions.putUint(buffer, functionCallId);
		SocketFunctions.putUint(buffer, bytes.length);
		buffer.flip();
		
		try
		{
			connection.write(buffer);
			if(bytes.length > 0)
			{
				connection.write(ByteBuffer.wrap(bytes));
			}
		}
		catch(PyroException e)
		{
		}
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	public LatentFunctionCall sendLatentFunctionCall(final byte[] bytes, final LatentFunctionCallListener functionCallListener)
	{
		return sendLatentFunctionCall(30, bytes, functionCallListener);
	}
	
	/**
	 * Will enqueue the bytes to send them.<br>
	 * <br>
	 * Timeout is the timeout in seconds.
	 */
	public LatentFunctionCall sendLatentFunctionCall(final float timeout, final byte[] bytes, final LatentFunctionCallListener functionCallListener)
	{
		final LatentFunctionCall latentAction = new LatentFunctionCall(this);
		latentAction.listener = functionCallListener;
		
		InternalLatentFunctionCall functionCall = new InternalLatentFunctionCall();
		functionCall.latentAction = latentAction;
		functionCall.listener = functionCallListener;
		functionCall.timeoutTime = CachedTime.millisSinceStart() + (long) (timeout * 1000.0);
		
		latentAction.functionCallId = reserveLatentFunctionCallId(functionCall);
		
		{// send latent function call >>
			final byte[] b = ((bytes == null) ? new byte[0] : bytes);
			if(selector.isNetworkThread())
			{
				sendLatentFunctionCallCode(latentAction, b, timeout, functionCallListener);
			}
			else
			{
				selector.scheduleTask(() -> sendLatentFunctionCallCode(latentAction, b, timeout, functionCallListener));
			}
		}// send latent function call <<
		
		return latentAction;
	}
	protected void sendLatentFunctionCallCode(final LatentFunctionCall latentAction, final byte[] bytes, final float timeout, final LatentFunctionCallListener functionCallListener)
	{
		if(latentAction.isDone() || latentAction.isCanceled() || latentAction.isFailed())
		{
			return;
		}
		if(!isConnected())
		{
			threadSafeFailedLatentFunctionCall(functionCallListener);
			latentAction.canceledByDisconnecting();
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(latentAction.functionCallId) + SocketFunctions.uintByteCount(bytes.length));
		buffer.put(SocketMessageType.LATENT_FUNCTION_CALL);
		SocketFunctions.putUint(buffer, latentAction.functionCallId);
		SocketFunctions.putUint(buffer, bytes.length);
		buffer.flip();
		
		try
		{
			connection.write(buffer);
			if(bytes.length > 0)
			{
				connection.write(ByteBuffer.wrap(bytes));
			}
		}
		catch(PyroException e)
		{
		}
	}
	
	
	/**
	 * Will enqueue the bytes to send them.
	 */
	protected void sendLatentFunctionCallCancel(final int functionCallId)
	{
		if(selector.isNetworkThread())
		{
			sendLatentFunctionCallCancelCode(functionCallId);
		}
		else
		{
			selector.scheduleTask(() -> sendLatentFunctionCallCancelCode(functionCallId));
		}
	}
	protected void sendLatentFunctionCallCancelCode(final int functionCallId)
	{
		if(!isConnected())
		{
			return;
		}
		
		ByteBuffer buffer = ByteBuffer.allocate(1 + SocketFunctions.uintByteCount(functionCallId));
		buffer.put(SocketMessageType.LATENT_FUNCTION_CALL_CANCELED);
		SocketFunctions.putUint(buffer, functionCallId);
		buffer.flip();
		
		try
		{
			connection.write(buffer);
		}
		catch(PyroException e)
		{
		}
	}
	
	
	/**
	 * Gracefully shuts down the connection. The connection
	 * is closed after the last outbound bytes are sent.
	 * Enqueuing new bytes after shutdown will not do anything.
	 */
	public void disconnect()
	{
		if(selector.isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			selector.scheduleTask(() ->
			{
				if(connectionUdp != null)
				{
					connectionUdp.shutdown();
					connectionUdp = null;
				}
				if(connection != null)
				{
					connection.shutdown();
					connection = null;
				}
			});
		}
		else
		{
			selector.scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				if(connectionUdp != null)
				{
					connectionUdp.shutdown();
					connectionUdp = null;
				}
				if(connection != null)
				{
					connection.shutdown();
					connection = null;
				}
			});
		}
	}
	
	/**
	 * Immediately drop the connection, regardless of any
	 * pending outbound bytes. Actual behavior depends on
	 * the socket linger settings.
	 */
	public void disconnectImmediately()
	{
		if(selector.isNetworkThread())
		{
			if(isDisconnecting)
			{
				return;
			}
			isDisconnecting = true;
			
			selector.scheduleTask(() ->
			{
				if(connectionUdp != null)
				{
					connectionUdp.shutdown();
					connectionUdp = null;
				}
				if(connection != null)
				{
					connection.dropConnection();
					connection = null;
				}
			});
		}
		else
		{
			selector.scheduleTask(() ->
			{
				if(isDisconnecting)
				{
					return;
				}
				isDisconnecting = true;
				
				if(connectionUdp != null)
				{
					connectionUdp.shutdown();
					connectionUdp = null;
				}
				if(connection != null)
				{
					connection.dropConnection();
					connection = null;
				}
			});
		}
	}
	
	/**
	 * Returns true when the connection is open.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in! Use {@link #execute(Runnable)} in case of doubt.<br>
	 */
	public boolean isConnected()
	{
		selector.checkThread();
		return (!isDisconnecting && (connection != null) && !connection.isDisconnected());
	}
	
	/**
	 * Returns true when the connection is open (for UDP).
	 */
	protected boolean isConnectedUdp()
	{
		selector.checkThread();
		return (!isDisconnecting && (connectionUdp != null));
	}
	
	
	/**
	 * Returns the PyroNet object of this wrapper.
	 */
	public PyroClient pyro()
	{
		return connection;
	}
	
	/**
	 * Returns the selector.
	 */
	public PyroSelector selector()
	{
		return selector;
	}
	
	/**
	 * Returns the PyroNet UDP object of this wrapper.
	 */
	public PyroClientUdp pyroUdp()
	{
		return connectionUdp;
	}
	
	
	/**
	 * This function will execute the given runnable on the thread this object was created in.
	 */
	public void execute(Runnable runnable)
	{
		if(selector.isNetworkThread())
		{
			runnable.run();
		}
		else
		{
			selector.scheduleTask(runnable);
		}
	}
	
	/**
	 * Returns true if this is the thread this object was created in.
	 */
	public boolean isNetworkThread()
	{
		return selector.isNetworkThread();
	}
	
	
	public String getAddressText()
	{
		if(connection == null)
		{
			return "closed";
		}
		if(connectionUdp == null)
		{
			return connection.getAddressText();
		}
		return connection.getAddressText() + ", " + connectionUdp.getAddressText();
	}
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
}
