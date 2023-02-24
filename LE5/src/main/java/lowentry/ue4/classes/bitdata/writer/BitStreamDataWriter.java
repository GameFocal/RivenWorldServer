package lowentry.ue4.classes.bitdata.writer;


import lowentry.ue4.classes.BitDataWriter;
import lowentry.ue4.classes.internal.FastByteArrayOutputStream;


public class BitStreamDataWriter extends BitDataWriter
{
	protected final FastByteArrayOutputStream bytes = new FastByteArrayOutputStream();
	
	
	@Override
	protected byte[] getBytesImplementation()
	{
		return bytes.toByteArray();
	}
	
	@Override
	protected byte[] getBytesImplementation(byte addByteToEnd)
	{
		return bytes.toByteArray(addByteToEnd);
	}
	
	
	@Override
	protected void resetImplementation()
	{
		bytes.reset();
	}
	
	
	@Override
	protected void addRawByteImplementation(byte value)
	{
		bytes.write(value);
	}
	
	@Override
	protected void addRawBytesImplementation(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.write(value);
	}
	
	
	@Override
	protected void addingUnsafeImplementation(int count)
	{
		bytes.ensureAdditionalCapacity(count);
	}
	
	@Override
	protected void addRawByteUnsafeImplementation(byte value)
	{
		bytes.writeUnsafe(value);
	}
	
	@Override
	protected void addRawBytesUnsafeImplementation(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		bytes.writeUnsafe(value);
	}
}
