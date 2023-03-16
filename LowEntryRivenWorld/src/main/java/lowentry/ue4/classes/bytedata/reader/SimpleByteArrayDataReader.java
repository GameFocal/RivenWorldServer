package lowentry.ue4.classes.bytedata.reader;


import lowentry.ue4.classes.SimpleByteDataReader;
import lowentry.ue4.library.LowEntry;


public class SimpleByteArrayDataReader extends SimpleByteDataReader
{
	protected final byte[] bytes;
	
	protected final int subLength;
	
	
	public SimpleByteArrayDataReader(byte[] bytes)
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
	public SimpleByteArrayDataReader(byte[] bytes, int length)
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
	
	
	protected SimpleByteArrayDataReader(byte[] bytes, int subLength, int position)
	{
		this.bytes = bytes;
		this.subLength = subLength;
		this.position = position;
	}
	
	@Override
	public SimpleByteArrayDataReader getClone()
	{
		return new SimpleByteArrayDataReader(bytes, subLength, position);
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
