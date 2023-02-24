package lowentry.ue4.classes.sockets.simplification;


import lowentry.ue4.classes.internal.FilePath;
import lowentry.ue4.classes.sockets.SimpleSocketServer;
import lowentry.ue4.library.LowEntry;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicLong;


public final class SimpleSocketServerMain
{
	private final static long MAX_TICK_TIME                             = 100;
	private final static long TIME_BETWEEN_FILE_CHECKS                  = 1000;
	private final static long TIME_BETWEEN_DOTENV_FILE_TIMESTAMP_CHECKS = 5000;
	private final static long TIME_BETWEEN_DOTENV_FILE_HASH_CHECKS      = 60000;
	
	private volatile SimpleSocketServer serverVolatile = null;
	private volatile boolean            shouldExit     = false; // forcefully stops the server, doesn't run shutdown events
	private volatile boolean            shouldStop     = false; // gracefully stops the server
	
	
	private SimpleSocketServerMain()
	{
	}
	
	
	public static void run(final SimpleSocketServerMainListener listener, final int port) throws Throwable
	{
		SimpleSocketServerMain instance = new SimpleSocketServerMain();
		try
		{
			instance._run(listener, port);
		}
		finally
		{
			if(SimpleSocketServer.IS_DEBUGGING)
			{
				SimpleSocketServer.DEBUGGING_PRINTSTREAM.println("[DEBUG] " + instance + " shutting down...");
			}
			
			final Thread thread = new Thread(() ->
			{
				// force-quit after 3 seconds
				LowEntry.sleep(3000);
				System.exit(0);
			}, "main-forceful-shutdown-timer");
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	
	private void _run(final SimpleSocketServerMainListener listener, final int port) throws Throwable
	{
		if(!listener.isNetworkThread())
		{
			throw new PyroException("the listener was not created in the same thread as the thread that called SimpleSocketServerMain.run()");
		}
		
		final File FILE_STATUS_IS_RUNNING = FilePath.getAsFile(listener.getClass(), "_STATUS__IS_RUNNING_");
		final File FILE_ACTION_SHUTDOWN = FilePath.getAsFile(listener.getClass(), "_ACTION__SHUTDOWN_");
		final File FILE_DOTENV = FilePath.getAsFile(listener.getClass(), ".env");
		
		try
		{
			FILE_STATUS_IS_RUNNING.deleteOnExit();
			FILE_ACTION_SHUTDOWN.deleteOnExit();
			
			if(FILE_STATUS_IS_RUNNING.exists())
			{
				FILE_STATUS_IS_RUNNING.delete();
				LowEntry.sleep(TIME_BETWEEN_FILE_CHECKS * 6);
			}
			
			FILE_STATUS_IS_RUNNING.createNewFile();
			FILE_ACTION_SHUTDOWN.createNewFile();
			
			Files.writeString(FILE_STATUS_IS_RUNNING.toPath(), ProcessHandle.current().pid() + "");
			
			runThread("main-file-checker", () ->
			{
				LowEntry.sleep(TIME_BETWEEN_FILE_CHECKS);
				if(!FILE_STATUS_IS_RUNNING.exists())
				{
					shouldExit = true;
					shouldStop = true;
				}
				else if(!FILE_ACTION_SHUTDOWN.exists())
				{
					shouldStop = true;
				}
			});
			
			final byte[] FILE_DOTENV_MD5 = ((System.getenv("LOWENTRY_DOTENV_MD5") == null) ? null : LowEntry.hexToBytes(System.getenv("LOWENTRY_DOTENV_MD5").trim()));
			if(FILE_DOTENV_MD5 != null)
			{
				if(FILE_DOTENV_MD5.length == 16)
				{
					final AtomicLong FILE_DOTENV_LAST_MODIFIED = new AtomicLong(FILE_DOTENV.lastModified());
					runThread("main-dotenv-file-checker", () ->
					{
						LowEntry.sleep(TIME_BETWEEN_DOTENV_FILE_TIMESTAMP_CHECKS);
						try
						{
							if(!FILE_DOTENV.exists())
							{
								shouldStop = true;
								return;
							}
						}
						catch(Throwable e)
						{
						}
						try
						{
							long lastModified = FILE_DOTENV.lastModified();
							if(lastModified != FILE_DOTENV_LAST_MODIFIED.get())
							{
								FILE_DOTENV_LAST_MODIFIED.set(lastModified);
								if(!LowEntry.areBytesEqual(FILE_DOTENV_MD5, LowEntry.md5(Files.readAllBytes(FILE_DOTENV.toPath()))))
								{
									shouldStop = true;
								}
							}
						}
						catch(Throwable e)
						{
						}
					});
					
					runThread("main-dotenv-file-checker-fallback", () ->
					{
						LowEntry.sleep(TIME_BETWEEN_DOTENV_FILE_HASH_CHECKS);
						try
						{
							if(!LowEntry.areBytesEqual(FILE_DOTENV_MD5, LowEntry.md5(Files.readAllBytes(FILE_DOTENV.toPath()))))
							{
								shouldStop = true;
							}
						}
						catch(Throwable e)
						{
						}
					});
				}
				else
				{
					runThread("main-dotenv-file-checker", () ->
					{
						LowEntry.sleep(TIME_BETWEEN_DOTENV_FILE_TIMESTAMP_CHECKS);
						try
						{
							if(FILE_DOTENV.exists())
							{
								shouldStop = true;
							}
						}
						catch(Throwable e)
						{
						}
					});
				}
			}
			
			while(!shouldStop)
			{
				try
				{
					try
					{
						{
							final SimpleSocketServer server = serverVolatile;
							serverVolatile = null;
							if(server != null)
							{
								try
								{
									server.terminateImmediately();
								}
								catch(Exception e)
								{
								}
							}
						}
						
						if(shouldExit)
						{
							return;
						}
						
						final SimpleSocketServer server = new SimpleSocketServer(true, port, listener);
						serverVolatile = server;
						listener.setServer(server);
						listener.executePendingTasks();
						listener.hasStarted();
						
						if(shouldExit)
						{
							return;
						}
						
						long time = LowEntry.millis();
						long timeMsBeforeNextTick = listener.getTimeMsBeforeNextTick();
						long timeMsBeforeNextFileCheck = TIME_BETWEEN_FILE_CHECKS;
						while(true)
						{
							server.listen(Math.min(timeMsBeforeNextTick, MAX_TICK_TIME));
							listener.executePendingTasks();
							
							long time2 = LowEntry.millis();
							timeMsBeforeNextTick -= (time2 - time);
							timeMsBeforeNextFileCheck -= (time2 - time);
							time = time2;
							
							if(timeMsBeforeNextFileCheck <= 10)
							{
								timeMsBeforeNextFileCheck += TIME_BETWEEN_FILE_CHECKS;
								
								if(listener.getShouldStop())
								{
									shouldStop = true;
								}
								if(shouldStop || listener.getShouldRestart())
								{
									if(shouldExit)
									{
										return;
									}
									listener.beforeGracefulShutdown();
									server.close();
									break;
								}
							}
							
							if(timeMsBeforeNextTick <= 10)
							{
								if(shouldExit)
								{
									return;
								}
								
								try
								{
									listener.tick();
								}
								catch(Exception e)
								{
									listener.serverErrored(e);
								}
								timeMsBeforeNextTick += listener.getTimeMsBeforeNextTick();
								
								if(listener.getShouldStop())
								{
									shouldStop = true;
								}
								if(shouldStop || listener.getShouldRestart())
								{
									if(shouldExit)
									{
										return;
									}
									listener.beforeGracefulShutdown();
									server.close();
									break;
								}
							}
						}
						
						if(shouldExit)
						{
							return;
						}
						
						time = LowEntry.millis();
						timeMsBeforeNextTick = listener.getTimeMsBeforeNextTick();
						timeMsBeforeNextFileCheck = TIME_BETWEEN_FILE_CHECKS;
						long timeMsForGracefulShutdown = listener.getMaxTimeMsForGracefulShutdown();
						while(server.getClientCount() > 0)
						{
							server.listen(Math.min(timeMsBeforeNextTick, MAX_TICK_TIME));
							listener.executePendingTasks();
							
							long time2 = LowEntry.millis();
							timeMsBeforeNextTick -= (time2 - time);
							timeMsBeforeNextFileCheck -= (time2 - time);
							timeMsForGracefulShutdown -= (time2 - time);
							time = time2;
							
							if(timeMsBeforeNextFileCheck <= 10)
							{
								if(shouldExit)
								{
									return;
								}
								timeMsBeforeNextFileCheck += TIME_BETWEEN_FILE_CHECKS;
							}
							
							if(timeMsBeforeNextTick <= 10)
							{
								if(shouldExit)
								{
									return;
								}
								
								try
								{
									listener.tick();
								}
								catch(Exception e)
								{
									listener.serverErrored(e);
								}
								timeMsBeforeNextTick += listener.getTimeMsBeforeNextTick();
								
								if(shouldExit)
								{
									return;
								}
							}
							
							if(timeMsForGracefulShutdown <= 0)
							{
								if(shouldExit)
								{
									return;
								}
								server.terminateImmediately();
								break;
							}
						}
					}
					catch(Exception e)
					{
						if(shouldExit)
						{
							return;
						}
						listener.serverErrored(e);
					}
					
					if(shouldExit)
					{
						return;
					}
					{
						final SimpleSocketServer server = serverVolatile;
						serverVolatile = null;
						if(server != null)
						{
							try
							{
								server.terminateImmediately();
							}
							catch(Exception e)
							{
							}
						}
					}
					
					if(shouldExit)
					{
						return;
					}
					listener.executePendingTasks();
					
					if(shouldExit)
					{
						return;
					}
					try
					{
						listener.hasStopped();
					}
					catch(Exception e)
					{
						listener.serverErrored(e);
					}
					
					if(shouldExit)
					{
						return;
					}
					listener.executePendingTasks();
					
					for(int i = 1; i <= 10; i++)
					{
						if(shouldExit)
						{
							return;
						}
						LowEntry.sleep(100);
						System.gc();
					}
					
					if(shouldExit)
					{
						return;
					}
					listener.executePendingTasks();
				}
				catch(Exception e)
				{
					System.err.println("[ERROR] " + this + " errored outside of the server try-catch block:");
					System.err.println(LowEntry.getStackTrace(e));
				}
			}
			
			if(shouldExit)
			{
				return;
			}
			listener.executePendingTasks();
		}
		catch(Throwable e)
		{
			System.err.println("[ERROR] " + this + " FATAL ERROR:");
			System.err.println(LowEntry.getStackTrace(e));
			throw e;
		}
		finally
		{
			shouldExit = true;
			
			try
			{
				FILE_STATUS_IS_RUNNING.delete();
			}
			catch(Exception e)
			{
			}
			
			try
			{
				FILE_ACTION_SHUTDOWN.delete();
			}
			catch(Exception e)
			{
			}
		}
	}
	
	
	private void runThread(final String name, final Runnable runnable)
	{
		final Thread thread = new Thread(() ->
		{
			while(!shouldExit)
			{
				try
				{
					runnable.run();
				}
				catch(Throwable e)
				{
					shouldExit = true;
					shouldStop = true;
					System.err.println("[ERROR] " + this + " failed in a worker thread:");
					System.err.println(LowEntry.getStackTrace(e));
					return;
				}
			}
		}, name);
		thread.setDaemon(true);
		thread.start();
	}
}
