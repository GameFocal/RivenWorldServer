package lowentry.ue4.classes.utility;


import lowentry.ue4.library.LowEntry;

import java.util.Collection;


/**
 * Allows you to have a HashSet that you can easily clear of old values very efficiently.<br>
 * <br>
 * It works the same way as {@link FlippableSet}, except that you don't have to keep track of the time with this class, just call {@link #tryFlip()} constantly and it will automatically flip when enough time has went by.<br>
 */
public class TimedFlippableSet<T> extends FlippableSet<T>
{
	protected final long timeoutMs;
	
	protected long timeRemainingMs;
	protected long lastFlipTimeMs;
	
	
	/**
	 * @param timeoutMs The timeout in millis which will be the minimum amount of time an entry in this set will last before expiring. The maximum time it will take for an entry to expire is the given timeout multiplied by 2.
	 */
	public TimedFlippableSet(long timeoutMs)
	{
		this.timeoutMs = timeoutMs;
		
		this.timeRemainingMs = timeoutMs;
		this.lastFlipTimeMs = LowEntry.millis();
	}
	
	
	/**
	 * Keeps track of the time, if the given timeoutMs has passed, it will flip the sets, and return the expired set.<br>
	 * <br>
	 * Will return NULL if the sets haven't been flipped.
	 */
	public Collection<T> tryFlip()
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
	 * Flips the sets, returns the expired set.
	 */
	@Override
	public Collection<T> flip()
	{
		timeRemainingMs = timeoutMs;
		lastFlipTimeMs = LowEntry.millis();
		return super.flip();
	}
}
