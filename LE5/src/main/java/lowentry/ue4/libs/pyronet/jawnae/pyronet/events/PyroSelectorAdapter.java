package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;

import java.io.IOException;


public class PyroSelectorAdapter implements PyroSelectorListener
{
	@Override
	public void executingTask(Runnable task)
	{
		//
	}
	
	@Override
	public void taskCrashed(Runnable task, Exception cause)
	{
		System.out.println(this.getClass().getSimpleName() + " caught exception: " + cause);
	}
	
	//
	
	@Override
	public void selectedKeys(int count)
	{
		//
	}
	
	@Override
	public void selectFailure(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + " caught exception: " + cause);
	}
	
	//
	
	@Override
	public void serverSelected(PyroServer server)
	{
		//
	}
	
	@Override
	public void clientSelected(PyroClient client, int readyOps)
	{
		//
	}
	
	//
	
	@Override
	public void serverBindFailed(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".serverBindFailed() caught exception: " + cause);
	}
	
	@Override
	public void clientBindFailed(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".serverBindFailed() caught exception: " + cause);
	}
}
