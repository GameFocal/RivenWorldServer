package lowentry.ue4.classes.bytedata.writer;


import lowentry.ue4.classes.ByteDataWriter;
import lowentry.ue4.classes.internal.FastByteArrayOutputStream;


public class ByteStreamDataWriter extends ByteDataWriter
{
	protected final FastByteArrayOutputStream bytes = new FastByteArrayOutputStream();
	
	
	@Override
	public byte[] getBytes()
	{
		return bytes.toByteArray();
	}
	
	
	@Override
	public ByteStreamDataWriter reset()
	{
		bytes.reset();
		return this;
	}
	
	
	@Override
	protected void addRawByte(byte value)
	{
		bytes.write(value);
	}
	
	@Override
	protected void addRawBytes(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.write(value);
	}
	
	
	@Override
	protected void addingUnsafe(int count)
	{
		bytes.ensureAdditionalCapacity(count);
	}
	
	@Override
	protected void addRawByteUnsafe(byte value)
	{
		bytes.writeUnsafe(value);
	}
	
	@Override
	protected void addRawBytesUnsafe(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.writeUnsafe(value);
	}
}
