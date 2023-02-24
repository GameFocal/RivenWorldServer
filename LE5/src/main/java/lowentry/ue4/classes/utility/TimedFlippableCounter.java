package lowentry.ue4.classes.utility;


import lowentry.ue4.library.LowEntry;

import java.util.Map;


/**
 * Allows you to have a HashMap of Longs that you can easily clear of old values very efficiently.<br>
 * <br>
 * It works the same way as {@link FlippableCounter}, except that you don't have to keep track of the time with this class, just call {@link #tryFlip()} constantly and it will automatically flip when enough time has went by.<br>
 */
public class TimedFlippableCounter<K> extends FlippableCounter<K>
{
	protected final long timeoutMs;
	
	protected long timeRemainingMs;
	protected long lastFlipTimeMs;
	
	
	/**
	 * @param timeoutMs The timeout in millis which will be the minimum amount of time an entry in this set will last before expiring. The maximum time it will take for an entry to expire is the given timeout multiplied by 2.
	 */
	public TimedFlippableCounter(long timeoutMs)
	{
		this.timeoutMs = timeoutMs;
		
		this.timeRemainingMs = timeoutMs;
		this.lastFlipTimeMs = LowEntry.millis();
	}
	
	
	/**
	 * Keeps track of the time, if the given timeoutMs has passed, it will flip the maps, and return the expired map.<br>
	 * <br>
	 * Will return NULL if the maps haven't been flipped.
	 */
	public Map<K,Long> tryFlip()
	{
		long timeMs = LowEntry.millis();
		timeRemainingMs -= (timeMs - lastFlipTimeMs);
		lastFlipTimeMs = timeMs;
		if(timeRemainingMs > 0)
		{
			return null;
		}
		timeRemainingMs += timeoutMs;
		return super.flip();
	}
	
	/**
	 * Flips the sets, returns the expired map.
	 */
	@Override
	public Map<K,Long> flip()
	{
		timeRemainingMs = timeoutMs;
		lastFlipTimeMs = LowEntry.millis();
		return super.flip();
	}
}
