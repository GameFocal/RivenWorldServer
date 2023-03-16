package lowentry.ue4.libs.pyronet.jawnae.pyronet.addon;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import java.nio.channels.SocketChannel;


public class PyroSingletonSelectorProvider implements PyroSelectorProvider
{
	private final PyroSelector selector;
	
	public PyroSingletonSelectorProvider(PyroSelector selector)
	{
		this.selector = selector;
	}
	
	@Override
	public PyroSelector provideFor(SocketChannel channel)
	{
		return this.selector;
	}
}
