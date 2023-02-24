package lowentry.ue4.libs.pyronet.jawnae.pyronet.traffic;


import java.nio.ByteBuffer;


public interface ByteSink
{
	int FEED_ACCEPTED      = 1;
	int FEED_ACCEPTED_LAST = 2;
	int FEED_REJECTED      = 3;
	
	/**
	 * determines what to do with the specified byte: accept, accept as final byte, reject
	 */
	
	int feed(byte b);
	
	/**
	 * Resets the state of this ByteSink, allowing it to be enqueued again
	 */
	
	void reset();
	
	/**
	 * Called by the client when this ByteSink is complete
	 */
	
	void onReady(ByteBuffer buffer);
}
