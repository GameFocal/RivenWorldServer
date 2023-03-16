package lowentry.ue4.classes.bytedata.reader;


import lowentry.ue4.classes.ByteDataReader;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;


public class ByteSubBufferDataReader extends ByteDataReader
{
	protected final ByteBuffer bytes;
	
	protected final int subIndex;
	protected final int subLength;
	
	
	public ByteSubBufferDataReader(ByteBuffer bytes, int index, int length)
	{
		this.bytes = bytes;
		if((bytes == null) || (length < 0))
		{
			this.subIndex = 0;
			this.subLength = 0;
		}
		else
		{
			this.subIndex = index;
			this.subLength = length;
		}
	}
	
	
	protected ByteSubBufferDataReader(ByteBuffer bytes, int subIndex, int subLength, int position)
	{
		this.bytes = bytes;
		this.subIndex = subIndex;
		this.subLength = subLength;
		this.position = position;
	}
	
	@Override
	public ByteDataReader getClone()
	{
		return new ByteSubBufferDataReader(bytes, subIndex, subLength, position);
	}
	
	
	@Override
	protected int getTotalCountImplementation()
	{
		return subLength;
	}
	
	@Override
	public byte getByteImplementation(int index)
	{
		return bytes.get(subIndex + index);
	}
	
	
	@Override
	protected String getStringUtf8Implementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesToStringUtf8(bytes.array(), bytes.arrayOffset() + subIndex + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, subIndex + index, length);
		return LowEntry.bytesToStringUtf8(b);
	}
	
	@Override
	protected String getStringLatin1Implementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesToStringLatin1(bytes.array(), bytes.arrayOffset() + subIndex + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, subIndex + index, length);
		return LowEntry.bytesToStringLatin1(b);
	}
	
	@Override
	protected byte[] getByteArrayImplementation(int index, int length)
	{
		if(bytes.hasArray())
		{
			return LowEntry.bytesSubArray(bytes.array(), bytes.arrayOffset() + subIndex + index, length);
		}
		byte[] b = new byte[length];
		bytes.get(b, subIndex + index, length);
		return b;
	}
}
