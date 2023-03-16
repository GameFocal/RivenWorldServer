package lowentry.ue4.classes.sockets;


import lowentry.ue4.classes.sockets.SocketConnection.LatentFunctionCallListener;


public class LatentFunctionCall
{
	protected final SocketConnection socketConnection;
	
	protected LatentFunctionCallListener listener = null;
	
	protected int functionCallId;
	
	protected final Object synchronizer = new Object();
	
	protected boolean failed   = false;
	protected boolean canceled = false;
	protected boolean done     = false;
	
	
	public LatentFunctionCall(final SocketConnection socketConnection)
	{
		this.socketConnection = socketConnection;
	}
	
	public LatentFunctionCall(final SocketConnection socketConnection, final boolean failed)
	{
		this.socketConnection = socketConnection;
		this.failed = failed;
	}
	
	
	/**
	 * Cancels the latent function call.
	 */
	public void cancel()
	{
		boolean changed = false;
		synchronized(synchronizer)
		{
			if(!done && !canceled && !failed)
			{
				canceled = true;
				changed = true;
			}
		}
		if(changed)
		{
			if(socketConnection != null)
			{
				socketConnection.sendLatentFunctionCallCancel(functionCallId);
				if(listener != null)
				{
					socketConnection.threadSafeCanceledLatentFunctionCall(listener);
				}
			}
		}
	}
	
	
	/**
	 * A return packet has been sent.
	 */
	protected void done()
	{
		synchronized(synchronizer)
		{
			if(!done && !canceled && !failed)
			{
				done = true;
			}
		}
	}
	
	/**
	 * Cancels the latent function call.
	 */
	protected void canceledByServer()
	{
		synchronized(synchronizer)
		{
			if(!done && !canceled && !failed)
			{
				canceled = true;
			}
		}
	}
	
	/**
	 * Cancels the latent function call.
	 */
	protected void canceledByTimeout()
	{
		synchronized(synchronizer)
		{
			if(!done && !canceled && !failed)
			{
				failed = true;
			}
		}
	}
	
	/**
	 * Cancels the latent function call.
	 */
	protected void canceledByDisconnecting()
	{
		synchronized(synchronizer)
		{
			if(!done && !canceled && !failed)
			{
				failed = true;
			}
		}
	}
	
	
	/**
	 * Returns true if the connection has been broken.
	 */
	public boolean isFailed()
	{
		synchronized(synchronizer)
		{
			return failed;
		}
	}
	
	/**
	 * Returns true if a cancel packet has been sent, or if a cancel packet has been received.
	 */
	public boolean isCanceled()
	{
		synchronized(synchronizer)
		{
			return canceled;
		}
	}
	
	/**
	 * Returns true if a return packet has been sent.
	 */
	public boolean isDone()
	{
		synchronized(synchronizer)
		{
			return done;
		}
	}
}
