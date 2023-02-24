package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroServer;

import java.io.IOException;


public interface PyroSelectorListener
{
	void executingTask(Runnable task);
	
	void taskCrashed(Runnable task, Exception cause);
	
	//
	
	void selectedKeys(int count);
	
	void selectFailure(IOException cause);
	
	//
	
	void serverSelected(PyroServer server);
	
	void clientSelected(PyroClient client, int readyOps);
	
	//
	
	void serverBindFailed(IOException cause);
	
	void clientBindFailed(IOException cause);
}
