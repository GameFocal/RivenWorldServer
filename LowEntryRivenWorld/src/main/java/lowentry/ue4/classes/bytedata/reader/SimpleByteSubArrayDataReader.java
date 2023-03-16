package lowentry.ue4.classes.bytedata.reader;


import lowentry.ue4.classes.SimpleByteDataReader;
import lowentry.ue4.library.LowEntry;


public class SimpleByteSubArrayDataReader extends SimpleByteDataReader
{
	protected final byte[] bytes;
	
	protected final int subIndex;
	protected final int subLength;
	
	
	public SimpleByteSubArrayDataReader(byte[] bytes, int index, int length)
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
	
	
	protected SimpleByteSubArrayDataReader(byte[] bytes, int subIndex, int subLength, int position)
	{
		this.bytes = bytes;
		this.subIndex = subIndex;
		this.subLength = subLength;
		this.position = position;
	}
	
	@Override
	public SimpleByteDataReader getClone()
	{
		return new SimpleByteSubArrayDataReader(bytes, subIndex, subLength, position);
	}
	
	
	@Override
	protected int getTotalCountImplementation()
	{
		return subLength;
	}
	
	@Override
	public byte getByteImplementation(int index)
	{
		return bytes[subIndex + index];
	}
	
	
	@Override
	protected String getStringUtf8Implementation(int index, int length)
	{
		return LowEntry.bytesToStringUtf8(bytes, subIndex + index, length);
	}
	
	@Override
	protected String getStringLatin1Implementation(int index, int length)
	{
		return LowEntry.bytesToStringLatin1(bytes, subIndex + index, length);
	}
	
	@Override
	protected byte[] getByteArrayImplementation(int index, int length)
	{
		return LowEntry.bytesSubArray(bytes, subIndex + index, length);
	}
}
