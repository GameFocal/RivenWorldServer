package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;

import java.io.IOException;
import java.nio.ByteBuffer;


public interface PyroClientListener
{
	void connectedClient(PyroClient client);
	
	void unconnectableClient(PyroClient client);
	
	void droppedClient(PyroClient client, IOException cause);
	
	void disconnectedClient(PyroClient client);
	
	//
	
	void receivedData(PyroClient client, ByteBuffer data);
	
	void sentData(PyroClient client, int bytes);
}
