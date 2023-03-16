package lowentry.ue4.classes.bytedata.reader;


import lowentry.ue4.classes.ByteDataReader;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;


public class ByteBufferDataReader extends ByteDataReader
{
	protected final ByteBuffer bytes;
	
	protected final int subLength;
	
	
	public ByteBufferDataReader(ByteBuffer bytes)
	{
		this.bytes = bytes;
		if(bytes == null)
		{
			this.subLength = 0;
		}
		else
		{
			this.subLength = bytes.remaining();
		}
	}
	public ByteBufferDataReader(ByteBuffer bytes, int length)
	{
		this.bytes = bytes;
		if((bytes == null) || (length < 0))
		{
			this.subLength = 0;
		}
		else
		{
			this.subLength = length;
		}
	}
	
	
	protected ByteBufferDataReader(ByteBuffer bytes, int subLength, int position)
	{
		this.bytes = bytes;
		this.subLength = subLength;
		this.position = position;
	}
	
	@Override
	public ByteDataReader getClone()
	{
		return new ByteBufferDataReader(bytes, subLength, position);
	}
	
	
	@Override
	protected int getTotalCountImplementation()
	{
		return subLength;
	}
	
	@Override
	public byte getByteImplementation(int index)
	{
		return bytes.get(index);
	}
	
	
	@Override
	protected String getStringUtf8Implementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesToStringUtf8(bytes.array(), bytes.arrayOffset() + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, index, length);
		return LowEntry.bytesToStringUtf8(b);
	}
	
	@Override
	protected String getStringLatin1Implementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesToStringLatin1(bytes.array(), bytes.arrayOffset() + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, index, length);
		return LowEntry.bytesToStringLatin1(b);
	}
	
	@Override
	protected byte[] getByteArrayImplementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesSubArray(bytes.array(), bytes.arrayOffset() + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, index, length);
		return b;
	}
}
