package lowentry.ue4.classes.internal;


import java.util.Arrays;


public class CompressionByteArrayBuffer
{
	protected byte[] buffer;
	protected int    length = 0;
	
	
	public CompressionByteArrayBuffer()
	{
		buffer = new byte[32];
	}
	
	public CompressionByteArrayBuffer(int initialSize)
	{
		buffer = new byte[(initialSize > 0) ? initialSize : 32];
	}
	
	public CompressionByteArrayBuffer(int initialSize, int maxInitialSize)
	{
		initialSize = (initialSize <= maxInitialSize) ? initialSize : maxInitialSize;
		buffer = new byte[(initialSize > 0) ? initialSize : 32];
	}
	
	
	protected void access(int index)
	{
		if(index >= buffer.length)
		{
			int newSize = buffer.length;
			while(index >= newSize)
			{
				newSize = (newSize >= 1073741823) ? 2147483647 : newSize * 2;
			}
			buffer = Arrays.copyOf(buffer, newSize);
		}
		if((index + 1) > length)
		{
			length = index + 1;
		}
	}
	
	
	public void set(int index, byte value)
	{
		access(index);
		buffer[index] = value;
	}
	
	public void set(int index, byte[] values, int valuesOffset, int length)
	{
		access(index + (length - 1));
		System.arraycopy(values, valuesOffset, buffer, index, length);
	}
	
	
	public byte get(int index)
	{
		if(index >= buffer.length)
		{
			return 0;
		}
		return buffer[index];
	}
	
	
	public byte[] getData()
	{
		return Arrays.copyOf(buffer, length);
	}
	
	public byte[] getData(int length)
	{
		//access(length - 1); // Java has this included in Arrays.copyOf()
		return Arrays.copyOf(buffer, length);
	}
}
