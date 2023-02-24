package lowentry.ue4.classes.internal;


public class CachedTime
{
	private final static Thread thread;
	
	private static volatile long currentTimeMillis = System.currentTimeMillis();
	private static volatile long nanoTime          = System.nanoTime();
	private static volatile long millisSinceStart  = 0;
	
	
	static
	{
		thread = new Thread(() ->
		{
			long millisStartNanoTime = System.nanoTime();
			long millisTimeAdd = 0;
			while(true)
			{
				currentTimeMillis = System.currentTimeMillis();
				nanoTime = System.nanoTime();
				
				long millisTimeDif = (nanoTime - millisStartNanoTime) / 1000000L;
				if(millisTimeDif >= 86400000) // 24 * 60 * 60 * 1000 = 1 day in millis
				{
					millisStartNanoTime = nanoTime;
					millisTimeAdd += millisTimeDif;
					millisTimeDif = 0;
				}
				millisSinceStart = millisTimeAdd + millisTimeDif;
				
				try
				{
					Thread.sleep(10);
				}
				catch(InterruptedException e)
				{
				}
			}
		}, "Cached-Time-Updater-Thread");
		
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.setDaemon(true);
		thread.start();
	}
	
	
	/**
	 * Returns a cached System.currentTimeMillis() value.<br>
	 * <br>
	 * The cached value updates after every Thread.sleep(10).<br>
	 */
	public static long currentTimeMillis()
	{
		return currentTimeMillis;
	}
	
	/**
	 * Returns a cached System.nanoTime() value.<br>
	 * <br>
	 * The cached value updates after every Thread.sleep(10).<br>
	 */
	public static long nanoTime()
	{
		return nanoTime;
	}
	
	/**
	 * Returns the amount of milliseconds that have elapsed since the beginning of this application.<br>
	 * <br>
	 * This value is cached and updates after every Thread.sleep(10).
	 */
	public static long millisSinceStart()
	{
		return millisSinceStart;
	}
}
