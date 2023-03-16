package lowentry.ue4.classes.utility;


/**
 * Makes it easier to sleep and wakeup threads.<br>
 * <br>
 * PS: Everything in this class is thread-safe.
 */
public class ThreadSleeper
{
	public final Object object = new Object();
	
	
	/**
	 * Sleeps until wakeup has been called.
	 */
	public void sleep()
	{
		try
		{
			synchronized(object)
			{
				object.wait();
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sleeps for the given ms, or until wakeup has been called.
	 */
	public void sleep(long ms)
	{
		try
		{
			synchronized(object)
			{
				object.wait(ms);
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Sleeps for the given ms and nanos, or until wakeup has been called.
	 */
	public void sleep(long ms, int nanos)
	{
		try
		{
			synchronized(object)
			{
				object.wait(ms, nanos);
			}
		}
		catch(Exception e)
		{
		}
	}
	
	
	/**
	 * Wakes up every sleep.
	 */
	public void wakeup()
	{
		try
		{
			synchronized(object)
			{
				object.notifyAll();
			}
		}
		catch(Exception e)
		{
		}
	}
	
	/**
	 * Wakes up a single sleep.
	 */
	public void wakeupOnlyOne()
	{
		try
		{
			synchronized(object)
			{
				object.notify();
			}
		}
		catch(Exception e)
		{
		}
	}
}
