package lowentry.ue4.libs.pyronet.jawnae.pyronet.traffic;


import lowentry.ue4.libs.pyronet.jawnae.pyronet.PyroException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class ByteStream
{
	private final List<ByteBuffer> queue;
	
	public ByteStream()
	{
		// the queue is expected to be relatively small, and iterated often.
		// hence removing the first element will be fast, even when using an ArrayList
		this.queue = new ArrayList<>();
	}
	
	/**
	 * Appends the ByteBuffer instance to the ByteStream. The
	 * bytes are not copied, so do not modify the contents of
	 * the ByteBuffer.
	 */
	
	public void append(ByteBuffer buf)
	{
		if(buf == null)
		{
			throw new NullPointerException();
		}
		this.queue.add(buf);
	}
	
	/**
	 * Returns whether there are any bytes pending in this stream
	 */
	
	public boolean hasData()
	{
		for(ByteBuffer byteBuffer : this.queue)
		{
			if(byteBuffer.hasRemaining())
			{
				return true;
			}
		}
		return false;
	}
	
	public int getByteCount()
	{
		int sum = 0;
		for(ByteBuffer byteBuffer : this.queue)
		{
			sum += byteBuffer.remaining();
		}
		return sum;
	}
	
	/**
	 * Fills the specified buffer with as much bytes as
	 * possible. When N bytes are read, the buffer position
	 * will be increased by N
	 */
	
	public void get(ByteBuffer dst)
	{
		if(dst == null)
		{
			throw new NullPointerException();
		}
		
		for(ByteBuffer data : this.queue)
		{
			// data pos/lim must not be modified
			data = data.slice();
			
			if(data.remaining() > dst.remaining())
			{
				data.limit(dst.remaining());
				dst.put(data);
				break;
			}
			
			dst.put(data);
			
			if(!dst.hasRemaining())
			{
				break;
			}
		}
	}
	
	/**
	 * Discards the specified amount of bytes from the stream.
	 *
	 * @throws PyroException if it failed to discard the
	 *                       specified number of bytes
	 */
	
	public void discard(int count)
	{
		int original = count;
		
		while(count > 0)
		{
			// peek at the first buffer
			ByteBuffer data = this.queue.get(0);
			
			if(count < data.remaining())
			{
				// discarding less bytes than remaining in buffer
				data.position(data.position() + count);
				count = 0;
				break;
			}
			
			// discard the first buffer
			this.queue.remove(0);
			count -= data.remaining();
		}
		
		if(count != 0)
		{
			// apparantly we cannot discard the amount of bytes
			// the user demanded, this is a bug in other code
			throw new PyroException("discarded " + (original - count) + "/" + original + " bytes");
		}
	}
}
