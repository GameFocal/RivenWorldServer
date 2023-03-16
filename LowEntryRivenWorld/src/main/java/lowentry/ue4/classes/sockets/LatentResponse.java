package lowentry.ue4.classes.sockets;


/**
 * For the implementation, see: {@link LatentResponseImplementation}
 */
public interface LatentResponse
{
	/**
	 * Cancels the latent function call.
	 */
	void cancel();
	
	/**
	 * Sends a return packet.
	 */
	void done(byte[] bytes);
	
	
	/**
	 * Cancels the latent function call.<br>
	 * <br>
	 * WARNING: Don't call this method yourself.
	 */
	void canceledByClient();
	
	/**
	 * Cancels the latent function call.<br>
	 * <br>
	 * WARNING: Don't call this method yourself.
	 */
	void canceledByDisconnecting();
	
	
	/**
	 * Returns true if a cancel packet has been sent, or if a cancel packet has been received.
	 */
	boolean isCanceled();
	
	/**
	 * Returns true if a return packet has been sent.
	 */
	boolean isDone();
	
	
	/**
	 * Execute the given Runnable when the latent function call is canceled.
	 */
	void setOnCanceled(Runnable code);
}
