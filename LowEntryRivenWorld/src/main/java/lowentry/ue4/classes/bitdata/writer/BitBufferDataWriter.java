package lowentry.ue4.classes.bitdata.writer;


import lowentry.ue4.classes.BitDataWriter;

import java.nio.ByteBuffer;


public class BitBufferDataWriter extends BitDataWriter
{
	protected final ByteBuffer bytes;
	
	
	public BitBufferDataWriter(ByteBuffer buffer)
	{
		buffer.clear();
		this.bytes = buffer;
	}
	
	
	@Override
	protected byte[] getBytesImplementation()
	{
		ByteBuffer shallowCopy = getByteBufferImplementation();
		byte[] b = new byte[shallowCopy.remaining()];
		shallowCopy.get(b);
		return b;
	}
	
	@Override
	protected byte[] getBytesImplementation(byte addByteToEnd)
	{
		ByteBuffer shallowCopy = getByteBufferImplementation(addByteToEnd);
		byte[] b = new byte[shallowCopy.remaining()];
		shallowCopy.get(b);
		return b;
	}
	
	@Override
	public ByteBuffer getByteBufferImplementation()
	{
		ByteBuffer shallowCopy = bytes.duplicate();
		shallowCopy.flip();
		return shallowCopy;
	}
	
	/**
	 * NOTICE: Adding new data to this BitDataWriter will cause the returned ByteBuffer to no longer be valid, because the last byte of the returned ByteBuffer will be overwritten!
	 */
	@Override
	public ByteBuffer getByteBufferImplementation(byte addByteToEnd)
	{
		ByteBuffer shallowCopy = bytes.duplicate();
		shallowCopy.put(addByteToEnd);
		shallowCopy.flip();
		return shallowCopy;
	}
	
	
	@Override
	protected void resetImplementation()
	{
		bytes.clear();
	}
	
	
	@Override
	protected void addRawByteImplementation(byte value)
	{
		bytes.put(value);
	}
	
	@Override
	protected void addRawBytesImplementation(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.put(value);
	}
	
	
	@Override
	protected void addingUnsafeImplementation(int count)
	{
	}
	
	@Override
	protected void addRawByteUnsafeImplementation(byte value)
	{
		bytes.put(value);
	}
	
	@Override
	protected void addRawBytesUnsafeImplementation(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.put(value);
	}
}
