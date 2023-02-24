package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;

import java.io.IOException;
import java.nio.ByteBuffer;


public class PyroClientAdapter implements PyroClientListener
{
	public void connectedClient(PyroClient client)
	{
		//
	}
	
	public void unconnectableClient(PyroClient client)
	{
		System.out.println("unconnectable");
	}
	
	public void droppedClient(PyroClient client, IOException cause)
	{
		if(cause != null)
		{
			System.out.println(this.getClass().getSimpleName() + ".droppedClient() caught exception: " + cause);
		}
	}
	
	public void disconnectedClient(PyroClient client)
	{
		//
	}
	
	//
	
	public void receivedData(PyroClient client, ByteBuffer data)
	{
		//
	}
	
	public void sentData(PyroClient client, int bytes)
	{
		//
	}
}
