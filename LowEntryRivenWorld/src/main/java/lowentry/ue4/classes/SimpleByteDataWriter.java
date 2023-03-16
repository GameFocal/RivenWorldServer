package lowentry.ue4.classes;


import java.util.Collection;


/**
 * This class is NOT thread safe!<br>
 * <br>
 * Works the same as the {@link ByteDataWriter} class, except that the Simple class no longer compresses things down with bit-wise code. The code is a lot simpler and easier to implement, at the cost of the network packages being slightly larger.
 */
public abstract class SimpleByteDataWriter extends ByteDataWriter
{
	/**
	 * Adds a positive integer (for lengths).
	 */
	protected void addUinteger(int value)
	{
		if(value < 0)
		{
			value = 0;
		}
		addingUnsafe(4);
		addRawByteUnsafe((byte) (value >> 24));
		addRawByteUnsafe((byte) (value >> 16));
		addRawByteUnsafe((byte) (value >> 8));
		addRawByteUnsafe((byte) (value));
	}
	
	/**
	 * Adds a positive integer (for lengths).
	 */
	protected void addUintegerUnsafe(int value)
	{
		if(value < 0)
		{
			value = 0;
		}
		addRawByteUnsafe((byte) (value >> 24));
		addRawByteUnsafe((byte) (value >> 16));
		addRawByteUnsafe((byte) (value >> 8));
		addRawByteUnsafe((byte) (value));
	}
	
	
	/**
	 * Adds a boolean array.
	 */
	public SimpleByteDataWriter addBooleanArray(boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(boolean v : value)
		{
			addBoolean(v);
		}
		return this;
	}
	
	/**
	 * Adds a boolean array.
	 */
	public SimpleByteDataWriter addBooleanArray(Boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(boolean v : value)
		{
			addBoolean(v);
		}
		return this;
	}
	
	/**
	 * Adds a boolean array.
	 */
	public SimpleByteDataWriter addBooleanArray(Collection<Boolean> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(boolean v : value)
		{
			addBoolean(v);
		}
		return this;
	}
}
