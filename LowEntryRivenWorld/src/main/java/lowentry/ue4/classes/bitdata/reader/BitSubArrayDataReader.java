package lowentry.ue4.classes.bitdata.reader;


import lowentry.ue4.classes.BitDataReader;
import lowentry.ue4.library.LowEntry;


public class BitSubArrayDataReader extends BitDataReader
{
	protected final byte[] bytes;
	
	protected final int subIndex;
	protected final int subLength;
	
	
	public BitSubArrayDataReader(byte[] bytes, int index, int length)
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
	
	
	protected BitSubArrayDataReader(byte[] bytes, int subIndex, int subLength, int position, byte currentByte, int currentBytePosition)
	{
		this.bytes = bytes;
		this.subIndex = subIndex;
		this.subLength = subLength;
		this.position = position;
		this.currentByte = currentByte;
		this.currentBytePosition = currentBytePosition;
	}
	
	@Override
	public BitSubArrayDataReader getClone()
	{
		return new BitSubArrayDataReader(bytes, subIndex, subLength, position, currentByte, currentBytePosition);
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
