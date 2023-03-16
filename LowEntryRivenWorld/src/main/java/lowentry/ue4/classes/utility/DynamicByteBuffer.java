package lowentry.ue4.classes.utility;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;


public class DynamicByteBuffer
{
	protected static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	protected byte[] buf;
	protected int    maxErased;
	protected int    erased = 0; // offset
	protected int    count  = 0; // length
	
	
	public DynamicByteBuffer()
	{
		this(32);
	}
	
	public DynamicByteBuffer(int size)
	{
		if(size < 0)
		{
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
		maxErased = size / 3;
	}
	
	
	protected void ensureCapacity(int minCapacity)
	{
		// overflow-conscious code
		if(minCapacity - buf.length > 0)
		{
			if((erased > maxErased) && (((minCapacity - erased) - buf.length) <= 0))
			{
				cleanupOffset();
			}
			else
			{
				grow(minCapacity);
			}
		}
	}
	
	protected void grow(int minCapacity)
	{
		// overflow-conscious code
		int oldCapacity = buf.length;
		int newCapacity = oldCapacity << 1;
		if(newCapacity - minCapacity < 0)
		{
			newCapacity = minCapacity;
		}
		if(newCapacity - MAX_ARRAY_SIZE > 0)
		{
			newCapacity = hugeCapacity(minCapacity);
		}
		buf = Arrays.copyOf(buf, newCapacity);
		maxErased = buf.length / 3;
	}
	
	protected static int hugeCapacity(int minCapacity)
	{
		if(minCapacity < 0) // overflow
		{
			throw new OutOfMemoryError();
		}
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}
	
	
	protected void cleanupOffset()
	{
		if(erased <= 0)
		{
			return;
		}
		if((count - erased) <= 0)
		{
			reset();
			return;
		}
		int newCount = count - erased;
		System.arraycopy(buf, erased, buf, 0, newCount);
		erased = 0;
		count = newCount;
	}
	
	public void erase(int len)
	{
		Objects.checkFromIndexSize(0, len, (count - erased));
		if(len <= 0)
		{
			return;
		}
		if(len >= (count - erased))
		{
			reset();
			return;
		}
		erased += len;
	}
	
	public void erase(int off, int len)
	{
		Objects.checkFromIndexSize(off, len, (count - erased));
		if(off <= 0)
		{
			erase(len);
			return;
		}
		if(len <= 0)
		{
			return;
		}
		if(off >= (count - erased))
		{
			return;
		}
		if(off + len > (count - erased))
		{
			len = (count - erased) - off;
		}
		int newCount = count - len;
		System.arraycopy(buf, erased + off + len, buf, erased + off, newCount - off);
		count = newCount;
	}
	
	
	public void put(int b)
	{
		ensureCapacity(count + 1);
		buf[count] = (byte) b;
		count++;
	}
	
	public void put(byte[] b)
	{
		if(b.length <= 0)
		{
			return;
		}
		ensureCapacity(count + b.length);
		System.arraycopy(b, 0, buf, count, b.length);
		count += b.length;
	}
	
	public void put(byte[] b, int off, int len)
	{
		Objects.checkFromIndexSize(off, len, b.length);
		if(len <= 0)
		{
			return;
		}
		ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}
	
	public void put(ByteBuffer b)
	{
		if(!b.hasRemaining())
		{
			return;
		}
		if(b.hasArray())
		{
			ensureCapacity(count + b.remaining());
			System.arraycopy(b.array(), b.arrayOffset() + b.position(), buf, count, b.remaining());
			count += b.remaining();
			b.position(b.limit());
		}
		else
		{
			ensureCapacity(count + b.remaining());
			while(b.hasRemaining())
			{
				buf[count] = b.get();
				count++;
			}
		}
	}
	
	
	public void reset()
	{
		erased = 0;
		count = 0;
	}
	
	public int remaining()
	{
		return count - erased;
	}
	
	public boolean hasRemaining()
	{
		return (count > erased);
	}
	
	
	public void writeToStream(OutputStream out) throws IOException
	{
		out.write(buf, erased, count - erased);
	}
	
	public byte[] toByteArray()
	{
		return Arrays.copyOfRange(buf, erased, count);
	}
	
	public ByteBuffer toByteBuffer()
	{
		return ByteBuffer.wrap(buf, erased, count - erased);
	}
}
