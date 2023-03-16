package lowentry.ue4.libs.pyronet.jawnae.pyronet.traffic;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroClient;

import java.nio.ByteBuffer;


public abstract class ByteSinkPacket32 implements ByteSink
{
	public static void sendTo(PyroClient client, byte[] payload)
	{
		boolean isExtreme = (payload.length > (0x7FFFFFFF - 4));
		
		byte[] wrapped = new byte[4 + (isExtreme ? 0 : payload.length)];
		wrapped[0] = (byte) (payload.length >> 24);
		wrapped[1] = (byte) (payload.length >> 16);
		wrapped[2] = (byte) (payload.length >> 8);
		wrapped[3] = (byte) (payload.length);
		
		if(!isExtreme)
		{
			System.arraycopy(payload, 0, wrapped, 4, payload.length);
		}
		
		client.write(client.selector().malloc(wrapped));
		
		if(isExtreme)
		{
			client.write(client.selector().malloc(payload));
		}
	}
	
	//
	
	ByteSinkLength current;
	
	public ByteSinkPacket32()
	{
		this.reset();
	}
	
	@Override
	public void reset()
	{
		this.current = new ByteSinkLength(4)
		{
			@Override
			public void onReady(ByteBuffer buffer)
			{
				// header is received
				int len = buffer.getInt(0);
				
				current = new ByteSinkLength(len)
				{
					@Override
					public void onReady(ByteBuffer buffer)
					{
						// content is received
						ByteSinkPacket32.this.onReady(buffer);
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
