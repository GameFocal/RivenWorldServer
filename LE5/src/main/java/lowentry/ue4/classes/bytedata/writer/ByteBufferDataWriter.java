package lowentry.ue4.classes.bytedata.writer;


import lowentry.ue4.classes.ByteDataWriter;

import java.nio.ByteBuffer;


public class ByteBufferDataWriter extends ByteDataWriter
{
	protected final ByteBuffer bytes;
	
	
	public ByteBufferDataWriter(ByteBuffer buffer)
	{
		buffer.clear();
		this.bytes = buffer;
	}
	
	
	@Override
	public byte[] getBytes()
	{
		ByteBuffer shallowCopy = getByteBuffer();
		byte[] b = new byte[shallowCopy.remaining()];
		shallowCopy.get(b);
		return b;
	}
	
	@Override
	public ByteBuffer getByteBuffer()
	{
		ByteBuffer shallowCopy = bytes.duplicate();
		shallowCopy.flip();
		return shallowCopy;
	}
	
	
	@Override
	public ByteBufferDataWriter reset()
	{
		bytes.clear();
		return this;
	}
	
	
	@Override
	protected void addRawByte(byte value)
	{
		bytes.put(value);
	}
	
	@Override
	protected void addRawBytes(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.put(value);
	}
	
	
	@Override
	protected void addingUnsafe(int count)
	{
	}
	
	@Override
	protected void addRawByteUnsafe(byte value)
	{
		bytes.put(value);
	}
	
	@Override
	protected void addRawBytesUnsafe(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.put(value);
	}
}
