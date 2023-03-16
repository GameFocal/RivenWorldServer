package lowentry.ue4.classes.sockets;


import java.util.HashMap;


public class LatentResponseImplementation implements LatentResponse
{
	protected final HashMap<Integer,LatentResponse> latentResponses;
	protected final SocketClient                    socketClient;
	protected final int                             functionCallId;
	
	protected final Object synchronizer = new Object();
	
	protected boolean canceled = false;
	protected boolean done     = false;
	
	protected volatile Runnable onCanceled = null;
	
	
	public LatentResponseImplementation(final HashMap<Integer,LatentResponse> latentResponses, final SocketClient socketClient, final int functionCallId)
	{
		this.latentResponses = latentResponses;
		this.socketClient = socketClient;
		this.functionCallId = functionCallId;
		
		addToHashMap();
	}
	
	
	protected void addToHashMap()
	{
		synchronized(latentResponses)
		{
			latentResponses.put(functionCallId, this);
		}
	}
	
	protected void removeFromHashMap()
	{
		synchronized(latentResponses)
		{
			latentResponses.remove(functionCallId);
		}
	}
	
	
	@Override
	public void cancel()
	{
		boolean changed = false;
		synchronized(synchronizer)
		{
			if(!done && !canceled)
			{
				canceled = true;
				changed = true;
			}
		}
		if(changed)
		{
			removeFromHashMap();
			socketClient.sendLatentFunctionCallCancel(functionCallId);
			try
			{
				Runnable code = onCanceled;
				if(code != null)
				{
					socketClient.execute(code);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sends a return packet.
	 */
	@Override
	public void done(final byte[] bytes)
	{
		boolean changed = false;
		synchronized(synchronizer)
		{
			if(!done && !canceled)
			{
				done = true;
				changed = true;
			}
		}
		if(changed)
		{
			removeFromHashMap();
			socketClient.sendLatentFunctionCallResponse(functionCallId, bytes);
		}
	}
	
	
	/**
	 * Cancels the latent function call.<br>
	 * <br>
	 * WARNING: Don't call this method yourself.
	 */
	@Override
	public void canceledByClient()
	{
		boolean changed = false;
		synchronized(synchronizer)
		{
			if(!done && !canceled)
			{
				canceled = true;
				changed = true;
			}
		}
		if(changed)
		{
			try
			{
				Runnable code = onCanceled;
				if(code != null)
				{
					socketClient.execute(code);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Cancels the latent function call.<br>
	 * <br>
	 * WARNING: Don't call this method yourself.
	 */
	@Override
	public void canceledByDisconnecting()
	{
		boolean changed = false;
		synchronized(synchronizer)
		{
			if(!done && !canceled)
			{
				canceled = true;
				changed = true;
			}
		}
		if(changed)
		{
			try
			{
				Runnable code = onCanceled;
				if(code != null)
				{
					socketClient.execute(code);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Returns true if a cancel packet has been sent, or if a cancel packet has been received.
	 */
	@Override
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
	@Override
	public boolean isDone()
	{
		synchronized(synchronizer)
		{
			return done;
		}
	}
	
	
	/**
	 * Execute the given Runnable when the latent function call is canceled.
	 */
	@Override
	public void setOnCanceled(final Runnable code)
	{
		this.onCanceled = code;
	}
}
