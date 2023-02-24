package lowentry.ue4.classes.bitdata.reader;


import lowentry.ue4.classes.BitDataReader;
import lowentry.ue4.library.LowEntry;


public class BitArrayDataReader extends BitDataReader
{
	protected final byte[] bytes;
	
	protected final int subLength;
	
	
	public BitArrayDataReader(byte[] bytes)
	{
		this.bytes = bytes;
		if(bytes == null)
		{
			this.subLength = 0;
		}
		else
		{
			this.subLength = bytes.length;
		}
	}
	public BitArrayDataReader(byte[] bytes, int length)
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
	
	
	protected BitArrayDataReader(byte[] bytes, int subLength, int position, byte currentByte, int currentBytePosition)
	{
		this.bytes = bytes;
		this.subLength = subLength;
		this.position = position;
		this.currentByte = currentByte;
		this.currentBytePosition = currentBytePosition;
	}
	
	@Override
	public BitArrayDataReader getClone()
	{
		return new BitArrayDataReader(bytes, subLength, position, currentByte, currentBytePosition);
	}
	
	
	@Override
	protected int getTotalCountImplementation()
	{
		return subLength;
	}
	
	@Override
	public byte getByteImplementation(int index)
	{
		return bytes[index];
	}
	
	
	@Override
	protected String getStringUtf8Implementation(int index, int length)
	{
		return LowEntry.bytesToStringUtf8(bytes, index, length);
	}
	
	@Override
	protected String getStringLatin1Implementation(int index, int length)
	{
		return LowEntry.bytesToStringLatin1(bytes, index, length);
	}
	
	@Override
	protected byte[] getByteArrayImplementation(int index, int length)
	{
		return LowEntry.bytesSubArray(bytes, index, length);
	}
}
