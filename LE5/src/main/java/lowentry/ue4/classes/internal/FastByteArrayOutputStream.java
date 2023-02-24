package lowentry.ue4.classes.internal;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;


public class FastByteArrayOutputStream
{
	/**
	 * The maximum size of array to allocate.
	 * Some VMs reserve some header words in an array.
	 * Attempts to allocate larger arrays may result in
	 * OutOfMemoryError: Requested array size exceeds VM limit
	 */
	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	
	public byte[] buf;
	public int    count;
	
	
	/**
	 * Creates a new byte array output stream. The buffer capacity is
	 * initially 32 bytes, though its size increases if necessary.
	 */
	public FastByteArrayOutputStream()
	{
		this(32);
	}
	
	/**
	 * Creates a new byte array output stream, with a buffer capacity of
	 * the specified size, in bytes.
	 */
	public FastByteArrayOutputStream(int size)
	{
		if(size < 0)
		{
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}
	
	
	/**
	 * Increases the capacity if necessary to ensure that it can hold
	 * at least the number of elements specified by the minimum
	 * capacity argument.
	 */
	public void ensureAdditionalCapacity(int count)
	{
		// overflow-conscious code
		ensureCapacity(this.count + count);
	}
	
	/**
	 * Increases the capacity if necessary to ensure that it can hold
	 * at least the number of elements specified by the minimum
	 * capacity argument.
	 */
	public void ensureCapacity(int minCapacity)
	{
		// overflow-conscious code
		if((minCapacity - buf.length) > 0)
		{
			grow(minCapacity);
		}
	}
	
	/**
	 * Increases the capacity to ensure that it can hold at least the
	 * number of elements specified by the minimum capacity argument.
	 */
	private void grow(int minCapacity)
	{
		// overflow-conscious code
		int oldCapacity = buf.length;
		int newCapacity = oldCapacity << 1;
		if((newCapacity - minCapacity) < 0)
		{
			newCapacity = minCapacity;
		}
		if((newCapacity - MAX_ARRAY_SIZE) > 0)
		{
			newCapacity = hugeCapacity(minCapacity);
		}
		buf = Arrays.copyOf(buf, newCapacity);
	}
	private static int hugeCapacity(int minCapacity)
	{
		if(minCapacity < 0)
		{
			throw new OutOfMemoryError();
		}
		return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
	}
	
	
	/**
	 * Writes the specified byte to this byte array output stream.
	 */
	public void write(byte b)
	{
		ensureCapacity(count + 1);
		buf[count] = b;
		count += 1;
	}
	
	/**
	 * Writes <code>b.length</code> bytes from the specified byte array
	 * to this output stream. The general contract for <code>write(b)</code> is that it should have exactly the same effect as the call <code>write(b, 0, b.length)</code>.
	 */
	public void write(byte[] b)
	{
		ensureCapacity(count + b.length);
		System.arraycopy(b, 0, buf, count, b.length);
		count += b.length;
	}
	
	/**
	 * Writes the specified byte to this byte array output stream.
	 * public void write(byte b)
	 * {
	 * ensureCapacity(count + 1);
	 * buf[count] = b;
	 * count += 1;
	 * }
	 *
	 * /**
	 * Writes <code>len</code> bytes from the specified byte array
	 * starting at offset <code>off</code> to this byte array output stream.
	 */
	public void write(byte[] b, int off, int len)
	{
		if((off < 0) || (off > b.length) || (len < 0) || (((off + len) - b.length) > 0))
		{
			throw new IndexOutOfBoundsException();
		}
		ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}
	
	
	/**
	 * Writes the specified byte to this byte array output stream.
	 */
	public void writeUnsafe(byte b)
	{
		//ensureCapacity(count + 1);
		buf[count] = b;
		count += 1;
	}
	
	/**
	 * Writes <code>b.length</code> bytes from the specified byte array
	 * to this output stream. The general contract for <code>write(b)</code> is that it should have exactly the same effect as the call <code>write(b, 0, b.length)</code>.
	 */
	public void writeUnsafe(byte[] b)
	{
		//ensureCapacity(count + b.length);
		System.arraycopy(b, 0, buf, count, b.length);
		count += b.length;
	}
	
	/**
	 * Writes the specified byte to this byte array output stream.
	 * public void write(byte b)
	 * {
	 * ensureCapacity(count + 1);
	 * buf[count] = b;
	 * count += 1;
	 * }
	 *
	 * /**
	 * Writes <code>len</code> bytes from the specified byte array
	 * starting at offset <code>off</code> to this byte array output stream.
	 */
	public void writeUnsafe(byte[] b, int off, int len)
	{
		if((off < 0) || (off > b.length) || (len < 0) || (((off + len) - b.length) > 0))
		{
			throw new IndexOutOfBoundsException();
		}
		//ensureCapacity(count + len);
		System.arraycopy(b, off, buf, count, len);
		count += len;
	}
	
	
	/**
	 * Writes the complete contents of this byte array output stream to
	 * the specified output stream argument, as if by calling the output
	 * stream's write method using <code>out.write(buf, 0, count)</code>.
	 */
	public void writeTo(OutputStream out) throws IOException
	{
		out.write(buf, 0, count);
	}
	
	
	/**
	 * Resets the <code>count</code> field of this byte array output
	 * stream to zero, so that all currently accumulated output in the
	 * output stream is discarded. The output stream can be used again,
	 * reusing the already allocated buffer space.
	 *
	 * @see java.io.ByteArrayInputStream#count
	 */
	public void reset()
	{
		count = 0;
	}
	
	
	/**
	 * Creates a newly allocated byte array. Its size is the current
	 * size of this output stream and the valid contents of the buffer
	 * have been copied into it.
	 *
	 * @return the current contents of this output stream, as a byte array.
	 * @see java.io.ByteArrayOutputStream#size()
	 */
	public byte[] toByteArray()
	{
		return Arrays.copyOf(buf, count);
	}
	/**
	 * Creates a newly allocated byte array. Its size is the current
	 * size of this output stream and the valid contents of the buffer
	 * have been copied into it.
	 *
	 * @return the current contents of this output stream, as a byte array.
	 * @see java.io.ByteArrayOutputStream#size()
	 */
	
	public byte[] toByteArray(byte currentByte)
	{
		byte[] data = Arrays.copyOf(buf, count + 1);
		data[count] = currentByte;
		return data;
	}
	
	/**
	 * Returns the current size of the buffer.
	 */
	public int size()
	{
		return count;
	}
}
