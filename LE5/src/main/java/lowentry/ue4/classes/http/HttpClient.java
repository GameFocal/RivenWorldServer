package lowentry.ue4.classes.http;


import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.library.LowEntry;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;


public class HttpClient
{
	public static final long TIMEOUT_SECONDS = 30;
	public static final long TIMEOUT_MS      = TIMEOUT_SECONDS * 1000;
	
	public static final long MAX_CONTENT_LENGTH = 20 * 1024;
	
	public static final int DEFAULT_SO_TIMEOUT = 100;
	
	
	protected final HttpServer server;
	protected final Socket     client;
	
	protected final BufferedInputStream  input;
	protected final BufferedOutputStream output;
	
	protected final long creationTime = CachedTime.millisSinceStart();
	
	
	protected ByteArrayOutputStream headerBytes           = new ByteArrayOutputStream();
	protected byte                  headerBytesStage      = 0;
	protected ByteArrayOutputStream contentBytes          = null;
	protected int                   contentBytesRemaining = 0;
	protected HttpRequest           request;
	
	protected int currentSoTimeout;
	
	
	public HttpClient(HttpServer server, Socket client) throws Exception
	{
		this.server = server;
		this.client = client;
		
		this.request = new HttpRequest(server.secure);
		
		this.input = new BufferedInputStream(client.getInputStream());
		this.output = new BufferedOutputStream(client.getOutputStream());
		
		currentSoTimeout = client.getSoTimeout();
		setSoTimeout(DEFAULT_SO_TIMEOUT);
	}
	
	
	/**
	 * Executes pending tasks.<br>
	 * <br>
	 * <b>WARNING:</b> Only call this on the same thread this object was created in!<br>
	 */
	protected void listen(long time) throws Exception
	{
		if((time - creationTime) >= TIMEOUT_MS)
		{
			disconnect();
			return;
		}
		if(request == null)
		{
			if(input.read() < 0)
			{
				disconnect();
			}
			return;
		}
		
		{// receive bytes >>
			if(server.secure)
			{
				setSoTimeout(1);
				try
				{
					read(1);
				}
				catch(Exception e)
				{
					// no data available
					return;
				}
			}
			
			int available = input.available();
			if(available <= 0)
			{
				return;
			}
			
			if(server.secure)
			{
				setSoTimeout(DEFAULT_SO_TIMEOUT);
			}
			
			read(available);
		}// receive bytes <<
	}
	
	/**
	 * Sets the SO_TIMEOUT.
	 */
	protected void setSoTimeout(int timeout) throws Exception
	{
		if(currentSoTimeout == timeout)
		{
			return;
		}
		client.setSoTimeout(timeout);
		currentSoTimeout = timeout;
	}
	
	/**
	 * Tries to read from the socket.
	 */
	protected void read(int max) throws Exception
	{
		ByteBuffer buffer = server.networkBuffer;
		
		buffer.clear();
		int size = input.read(buffer.array(), 0, Math.min(max, buffer.remaining()));
		if(size <= 0)
		{
			if(size < 0)
			{
				disconnect();
			}
			return;
		}
		buffer.limit(size);
		
		receivedData(buffer);
	}
	
	/**
	 * Process the received data.
	 */
	@SuppressWarnings("RedundantThrows")
	protected void receivedData(final ByteBuffer data) throws Exception
	{
		if(request == null)
		{
			return;
		}
		
		
		if(headerBytes != null)
		{
			while(data.hasRemaining())
			{
				{// wait for header end >>
					byte b = data.get();
					headerBytes.write(b);
					
					if(headerBytes.size() > 8192) // 8 KB
					{
						// handshake packet too big
						disconnect();
						return;
					}
					
					if(headerBytesStage == 0)
					{
						if(b == 13) // \r
						{
							headerBytesStage++;
						}
						continue;
					}
					if(headerBytesStage == 1)
					{
						if(b == 10) // \n
						{
							headerBytesStage++;
						}
						else
						{
							headerBytesStage = 0;
						}
						continue;
					}
					if(headerBytesStage == 2)
					{
						if(b == 13) // \r
						{
							headerBytesStage++;
						}
						else
						{
							headerBytesStage = 0;
						}
						continue;
					}
					if(headerBytesStage == 3)
					{
						headerBytesStage = 0;
						if(b == 10) // \n
						{
							// done
						}
						else
						{
							continue;
						}
					}
				}// wait for header end <<
				
				String header = LowEntry.bytesToStringLatin1(headerBytes.toByteArray()).trim();
				
				contentBytes = headerBytes;
				contentBytes.reset();
				headerBytes = null;
				
				{// parse header >>
					boolean first = true;
					for(String line : header.split("\r\n"))
					{
						if(first)
						{
							String[] meta = line.split(" ", 3);
							if(meta.length != 3)
							{
								disconnect();
								return;
							}
							request.internal_setMethod(meta[0]);
							request.internal_setPath(meta[1]);
							first = false;
							continue;
						}
						
						String[] headerparts = line.split(":", 2);
						request.internal_setHeader(headerparts[0], ((headerparts.length == 2) ? headerparts[1] : ""));
					}
				}// parse header <<
				
				contentBytesRemaining = request.getContentLength();
				if(contentBytesRemaining > MAX_CONTENT_LENGTH)
				{
					disconnect();
					return;
				}
				break;
			}
			
			if(headerBytes != null)
			{
				return;
			}
		}
		
		
		if(contentBytesRemaining > 0)
		{
			while(data.hasRemaining())
			{
				byte b = data.get();
				contentBytes.write(b);
				contentBytesRemaining--;
				
				if(contentBytesRemaining <= 0)
				{
					byte[] content = contentBytes.toByteArray();
					contentBytes = null;
					
					request.internal_setContent(content);
					break;
				}
			}
			
			if(contentBytesRemaining > 0)
			{
				return;
			}
		}
		
		
		request.internal_setRequest();
		server.internal_listenerReceivedRequest(this, request, new HttpResponse(this, request));
		request = null;
	}
	
	
	/**
	 * Sends the given bytes.
	 */
	public void send(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return;
		}
		
		try
		{
			output.write(bytes, 0, bytes.length);
		}
		catch(Exception e)
		{
		}
		
		try
		{
			output.flush();
		}
		catch(Exception e)
		{
		}
	}
	
	
	/**
	 * Returns the local socket address (host and port), can return null.
	 */
	public InetSocketAddress getLocalAddress()
	{
		return (InetSocketAddress) client.getLocalSocketAddress();
	}
	
	/**
	 * Returns the remote socket address (host and port), can return null.
	 */
	public InetSocketAddress getRemoteAddress()
	{
		return (InetSocketAddress) client.getRemoteSocketAddress();
	}
	
	/**
	 * Returns the IP, can return null.
	 */
	public InetAddress getIp()
	{
		return client.getInetAddress();
	}
	
	/**
	 * Returns the IP as a String, can return null.
	 */
	public String getIpString()
	{
		InetAddress ip = getIp();
		if(ip == null)
		{
			return null;
		}
		return ip.getHostAddress();
	}
	
	
	/**
	 * Disconnects the client.
	 */
	public void disconnect()
	{
		internal_disconnect();
		server.internal_removeClient(this);
	}
	
	/**
	 * Returns true if the client is still connected.
	 */
	public boolean isConnected()
	{
		return (client.isConnected() && !client.isClosed());
	}
	
	
	/**
	 * Disconnects the client (without removing the client from the server client list).
	 */
	protected void internal_disconnect()
	{
		try
		{
			output.flush();
		}
		catch(Exception e)
		{
		}
		
		try
		{
			client.close();
		}
		catch(Exception e)
		{
		}
	}
	
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + getAddressText() + "]";
	}
	
	private final String getAddressText()
	{
		if(!isConnected())
		{
			return "closed";
		}
		
		InetSocketAddress sockaddr = this.getRemoteAddress();
		if(sockaddr == null)
		{
			return "connecting";
		}
		InetAddress inetaddr = sockaddr.getAddress();
		return inetaddr.getHostAddress() + "@" + sockaddr.getPort();
	}
}
