package lowentry.ue4.libs.pyronet.lowentry.pyronet.udp;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;
import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroSelector;
import lowentry.ue4.libs.pyronet.lowentry.pyronet.udp.event.PyroClientUdpListener;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;


public class PyroClientUdp
{
	protected final DatagramChannel       channel;
	private final   Thread                networkThread;
	private final   PyroClientUdpListener listener;
	
	
	public PyroClientUdp(SocketAddress end, PyroClientUdpListener listener) throws Exception
	{
		this.networkThread = Thread.currentThread();
		this.listener = listener;
		
		this.channel = DatagramChannel.open();
		this.channel.configureBlocking(false);
		this.channel.bind(null);
		this.channel.connect(end);
	}
	
	
	public void shutdown()
	{
		try
		{
			this.channel.disconnect();
		}
		catch(Exception e)
		{
		}
		
		try
		{
			this.channel.close();
		}
		catch(Exception e)
		{
		}
	}
	
	
	public final boolean isNetworkThread()
	{
		if(PyroSelector.DO_NOT_CHECK_NETWORK_THREAD)
		{
			return true;
		}
		return networkThread == Thread.currentThread();
	}
	
	public final Thread networkThread()
	{
		return networkThread;
	}
	
	public final void checkThread()
	{
		if(PyroSelector.DO_NOT_CHECK_NETWORK_THREAD)
		{
			return;
		}
		if(!isNetworkThread())
		{
			throw new PyroException("call from outside the network-thread, you must schedule tasks");
		}
	}
	
	
	/**
	 * Will block until the send has been completed.<br>
	 * <br>
	 * - Thread-safe.<br>
	 * - The buffer can be cleared and reused.
	 */
	public void write(ByteBuffer buffer)
	{
		if(buffer == null)
		{
			return;
		}
		int pos = buffer.position();
		try
		{
			channel.write(buffer);
		}
		catch(Exception e)
		{
			//e.printStackTrace();
		}
		buffer.position(pos);
	}
	
	
	/**
	 * Keeps reading and firing the listener events until no more data can be read.
	 */
	public void listen(ByteBuffer buffer)
	{
		while(receive(buffer))
		{
			listener.receivedDataUdp(buffer);
		}
	}
	/**
	 * Returns false on fail.
	 */
	private boolean receive(ByteBuffer buffer)
	{
		try
		{
			buffer.clear();
			int result = channel.read(buffer);
			if(result <= 0)
			{
				return false;
			}
			buffer.flip();
			return true;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * Retrieves the local port.<br>
	 * <br>
	 * Returns 0 if it is not valid.
	 */
	public int getLocalPort()
	{
		int port = channel.socket().getLocalPort();
		if(port <= 0)
		{
			return 0;
		}
		return port;
	}
	
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
	
	@SuppressWarnings("resource")
	public final String getAddressText()
	{
		DatagramSocket sockaddr = channel.socket();
		if(sockaddr == null)
		{
			if(!channel.isOpen())
			{
				return "closed";
			}
			return "connecting";
		}
		InetAddress inetaddr = sockaddr.getInetAddress();
		if(inetaddr == null)
		{
			if(!channel.isOpen())
			{
				return "closed";
			}
			return "connecting";
		}
		return inetaddr.getHostAddress() + ":" + sockaddr.getPort();
	}
}
