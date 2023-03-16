package lowentry.ue4.libs.pyronet.jawnae.pyronet;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.addon.PyroSelectorProvider;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.events.PyroServerListener;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class PyroServer
{
	protected final PyroSelector       selector;
	public final    SelectionKey       serverKey;
	private final   PyroServerListener listener;
	
	
	@SuppressWarnings("resource")
	PyroServer(PyroSelector selector, Selector nioSelector, InetSocketAddress endpoint, int backlog, PyroServerListener listener) throws IOException
	{
		this.listener = listener;
		
		this.selector = selector;
		this.selector.checkThread();
		
		ServerSocketChannel ssc;
		ssc = ServerSocketChannel.open();
		ssc.socket().bind(endpoint, backlog);
		ssc.configureBlocking(false);
		
		this.serverKey = ssc.register(nioSelector, SelectionKey.OP_ACCEPT);
		this.serverKey.attach(this);
	}
	
	
	/**
	 * Returns the network that created this server
	 */
	public PyroSelector selector()
	{
		return this.selector;
	}
	
	//
	
	/**
	 * Returns the local socket address (host+port)
	 */
	@SuppressWarnings("resource")
	public InetSocketAddress getLocalAddress()
	{
		ServerSocket s = ((ServerSocketChannel) serverKey.channel()).socket();
		return (InetSocketAddress) s.getLocalSocketAddress();
	}
	
	//
	
	private PyroSelectorProvider selectorProvider;
	
	/**
	 * By installing a PyroSelectorProvider you alter the
	 * PyroSelector used to do the I/O of a PyroClient.
	 * This can be used for multi-threading your network code
	 */
	public void installSelectorProvider(PyroSelectorProvider selectorProvider)
	{
		this.selector().checkThread();
		
		this.selectorProvider = selectorProvider;
	}
	
	void onInterestOp()
	{
		if(!serverKey.isValid())
		{
			throw new PyroException("invalid selection key");
		}
		
		try
		{
			if(serverKey.isAcceptable())
			{
				this.onReadyToAccept();
			}
		}
		catch(IOException exc)
		{
			throw new IllegalStateException(exc);
		}
	}
	
	/**
	 * Closes the server socket. Any current connections will continue.
	 */
	public void close() throws IOException
	{
		this.selector.checkThread();
		
		this.serverKey.channel().close();
	}
	
	private void onReadyToAccept() throws IOException
	{
		this.selector.checkThread();
		
		@SuppressWarnings("resource")
		final SocketChannel channel = ((ServerSocketChannel) serverKey.channel()).accept();
		
		final PyroSelector acceptedClientSelector;
		{
			if(this.selectorProvider == null)
			{
				acceptedClientSelector = this.selector;
			}
			else
			{
				acceptedClientSelector = this.selectorProvider.provideFor(channel);
			}
		}
		
		if(acceptedClientSelector == this.selector)
		{
			SelectionKey clientKey = PyroClient.configure(acceptedClientSelector, channel, false);
			PyroClient client = new PyroClient(acceptedClientSelector, clientKey, null);
			this.fireAcceptedClient(client);
		}
		else
		{
			// create client in PyroClient-selector thread
			acceptedClientSelector.scheduleTask(() ->
			{
				SelectionKey clientKey;
				try
				{
					clientKey = PyroClient.configure(acceptedClientSelector, channel, false);
				}
				catch(IOException exc)
				{
					throw new IllegalStateException(exc);
				}
				final PyroClient client = new PyroClient(acceptedClientSelector, clientKey, null);
				PyroServer.this.fireAcceptedClient(client);
			});
			acceptedClientSelector.wakeup();
		}
	}
	
	void fireAcceptedClient(PyroClient client)
	{
		listener.acceptedClient(client);
	}
	
	//
	
	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + "[" + this.getAddressText() + "]";
	}
	
	public final String getAddressText()
	{
		if(!this.serverKey.channel().isOpen())
		{
			return "closed";
		}
		
		InetSocketAddress sockaddr = this.getLocalAddress();
		if(sockaddr == null)
		{
			return "connecting";
		}
		InetAddress inetaddr = sockaddr.getAddress();
		if(inetaddr == null)
		{
			return "connecting";
		}
		return inetaddr.getHostAddress() + ":" + sockaddr.getPort();
	}
}
