package lowentry.ue4.libs.pyronet.jawnae.pyronet.addon;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;

import java.nio.channels.SocketChannel;


public interface PyroSelectorProvider
{
	PyroSelector provideFor(SocketChannel channel);
}
