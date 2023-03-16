package lowentry.ue4.libs.pyronet.jawnae.pyronet.traffic;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;

import java.nio.ByteBuffer;


public abstract class ByteSinkPacket24 implements ByteSink
{
	public static void sendTo(PyroClient client, byte[] payload)
	{
		if(payload.length > 0x00FFFFFF)
		{
			throw new IllegalStateException("packet bigger than 16M-1 bytes");
		}
		
		byte[] wrapped = new byte[3 + payload.length];
		wrapped[0] = (byte) (payload.length >> 16);
		wrapped[1] = (byte) (payload.length >> 8);
		wrapped[2] = (byte) (payload.length);
		System.arraycopy(payload, 0, wrapped, 3, payload.length);
		
		client.write(client.selector().malloc(wrapped));
	}
	
	//
	
	ByteSinkLength current;
	
	public ByteSinkPacket24()
	{
		this.reset();
	}
	
	@Override
	public void reset()
	{
		this.current = new ByteSinkLength(3)
		{
			@Override
			public void onReady(ByteBuffer buffer)
			{
				// header is received
				int len = ((buffer.getShort(0) & 0xFFFF) << 8) | (buffer.get(3) & 0xFF);
				
				current = new ByteSinkLength(len)
				{
					@Override
					public void onReady(ByteBuffer buffer)
					{
						// content is received
						ByteSinkPacket24.this.onReady(buffer);
						current = null;
					}
				};
			}
		};
	}
	
	@Override
	public int feed(byte b)
	{
		if(this.current == null)
		{
			throw new IllegalStateException();
		}
		
		int result = this.current.feed(b);
		
		if(result == FEED_ACCEPTED)
		{
			return result;
		}
		
		return feedb(result, b);
	}
	
	private int feedb(int result, byte b)
	{
		if(this.current == null)
		{
			return result;
		}
		
		return this.current.feed(b);
	}
}
