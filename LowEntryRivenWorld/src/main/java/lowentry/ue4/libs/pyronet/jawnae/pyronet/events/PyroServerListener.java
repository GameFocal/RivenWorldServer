package lowentry.ue4.libs.pyronet.jawnae.pyronet.events;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;


public interface PyroServerListener
{
	/**
	 * Note: invoked from the PyroSelector-thread that created this PyroClient
	 */
	void acceptedClient(PyroClient client);
}
