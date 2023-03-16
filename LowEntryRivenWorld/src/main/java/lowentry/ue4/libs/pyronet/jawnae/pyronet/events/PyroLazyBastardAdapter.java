package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;


public class PyroLazyBastardAdapter implements PyroSelectorListener, PyroServerListener, PyroClientListener
{
	// --------------- PyroSelectorListener
	
	@Override
	public void executingTask(Runnable task)
	{
		//
	}
	
	@Override
	public void taskCrashed(Runnable task, Exception cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".taskCrashed() caught exception:");
		cause.printStackTrace();
	}
	
	@Override
	public void selectedKeys(int count)
	{
		//
	}
	
	@Override
	public void selectFailure(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".selectFailure() caught exception:");
		cause.printStackTrace();
	}
	
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
	
	// ------------- PyroServerListener
	
	@Override
	public void acceptedClient(PyroClient client)
	{
		//
	}
	
	// ------------- PyroClientListener
	
	@Override
	public void connectedClient(PyroClient client)
	{
		//
	}
	
	@Override
	public void unconnectableClient(PyroClient client)
	{
		System.out.println(this.getClass().getSimpleName() + ".unconnectableClient()");
	}
	
	@Override
	public void droppedClient(PyroClient client, IOException cause)
	{
		if((cause != null) && !(cause instanceof EOFException))
		{
			System.out.println(this.getClass().getSimpleName() + ".droppedClient() caught exception: " + cause);
		}
	}
	
	@Override
	public void disconnectedClient(PyroClient client)
	{
		//
	}
	
	//
	
	@Override
	public void receivedData(PyroClient client, ByteBuffer data)
	{
		//
	}
	
	@Override
	public void sentData(PyroClient client, int bytes)
	{
		//
	}
	
	@Override
	public void serverBindFailed(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".serverBindFailed() caught exception:");
		cause.printStackTrace();
	}
	
	@Override
	public void clientBindFailed(IOException cause)
	{
		System.out.println(this.getClass().getSimpleName() + ".serverBindFailed() caught exception:");
		cause.printStackTrace();
	}
}
