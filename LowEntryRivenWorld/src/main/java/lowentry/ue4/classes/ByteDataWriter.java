package lowentry.ue4.classes;


import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;
import java.util.Collection;


/**
 * This class is NOT thread safe!
 */
public abstract class ByteDataWriter
{
	/**
	 * Returns the bytes that have been stored so far.
	 */
	public abstract byte[] getBytes();
	
	/**
	 * Returns the bytes that have been stored so far.
	 */
	public ByteBuffer getByteBuffer()
	{
		return ByteBuffer.wrap(getBytes());
	}
	
	
	/**
	 * Makes it possible to reuse a ByteDataWriter's allocated buffer.
	 */
	public abstract ByteDataWriter reset();
	
	
	protected abstract void addRawByte(byte value);
	protected abstract void addRawBytes(byte[] value);
	
	protected abstract void addingUnsafe(int count);
	protected abstract void addRawByteUnsafe(byte value);
	protected abstract void addRawBytesUnsafe(byte[] value);
	
	
	/**
	 * Adds a positive integer (for lengths).
	 */
	protected void addUinteger(int value)
	{
		if(value <= 0)
		{
			addRawByte((byte) 0);
		}
		else if(value < 128)
		{
			addRawByte((byte) (value));
		}
		else
		{
			addingUnsafe(4);
			addRawByteUnsafe((byte) ((value >> 24) | (1 << 7)));
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
	}
	
	/**
	 * Adds a positive integer (for lengths).
	 */
	protected void addUintegerUnsafe(int value)
	{
		if(value <= 0)
		{
			addRawByteUnsafe((byte) 0);
		}
		else if(value < 128)
		{
			addRawByteUnsafe((byte) (value));
		}
		else
		{
			addRawByteUnsafe((byte) ((value >> 24) | (1 << 7)));
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
	}
	
	
	/**
	 * Adds a byte.
	 */
	public ByteDataWriter add(byte value)
	{
		addByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public ByteDataWriter add(int value)
	{
		addInteger(value);
		return this;
	}
	/**
	 * Adds a long.
	 */
	public ByteDataWriter add(long value)
	{
		addLong(value);
		return this;
	}
	/**
	 * Adds a float.
	 */
	public ByteDataWriter add(float value)
	{
		addFloat(value);
		return this;
	}
	/**
	 * Adds a double.
	 */
	public ByteDataWriter add(double value)
	{
		addDouble(value);
		return this;
	}
	/**
	 * Adds a boolean.
	 */
	public ByteDataWriter add(boolean value)
	{
		addBoolean(value);
		return this;
	}
	/**
	 * Adds a String (UTF-8).
	 */
	public ByteDataWriter add(String value)
	{
		addStringUtf8(value);
		return this;
	}
	
	/**
	 * Adds a byte.
	 */
	public ByteDataWriter add(Byte value)
	{
		addByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public ByteDataWriter add(Integer value)
	{
		addInteger(value);
		return this;
	}
	/**
	 * Adds a long.
	 */
	public ByteDataWriter add(Long value)
	{
		addLong(value);
		return this;
	}
	/**
	 * Adds a float.
	 */
	public ByteDataWriter add(Float value)
	{
		addFloat(value);
		return this;
	}
	/**
	 * Adds a double.
	 */
	public ByteDataWriter add(Double value)
	{
		addDouble(value);
		return this;
	}
	/**
	 * Adds a boolean.
	 */
	public ByteDataWriter add(Boolean value)
	{
		addBoolean(value);
		return this;
	}
	
	/**
	 * Adds a byte array.
	 */
	public ByteDataWriter add(byte[] value)
	{
		addByteArray(value);
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public ByteDataWriter add(int[] value)
	{
		addIntegerArray(value);
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public ByteDataWriter add(long[] value)
	{
		addLongArray(value);
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public ByteDataWriter add(float[] value)
	{
		addFloatArray(value);
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public ByteDataWriter add(double[] value)
	{
		addDoubleArray(value);
		return this;
	}
	/**
	 * Adds a boolean array.
	 */
	public ByteDataWriter add(boolean[] value)
	{
		addBooleanArray(value);
		return this;
	}
	/**
	 * Adds a String (UTF-8) array.
	 */
	public ByteDataWriter add(String[] value)
	{
		addStringUtf8Array(value);
		return this;
	}
	
	/**
	 * Adds a byte array.
	 */
	public ByteDataWriter add(Byte[] value)
	{
		addByteArray(value);
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public ByteDataWriter add(Integer[] value)
	{
		addIntegerArray(value);
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public ByteDataWriter add(Long[] value)
	{
		addLongArray(value);
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public ByteDataWriter add(Float[] value)
	{
		addFloatArray(value);
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public ByteDataWriter add(Double[] value)
	{
		addDoubleArray(value);
		return this;
	}
	/**
	 * Adds a boolean array.
	 */
	public ByteDataWriter add(Boolean[] value)
	{
		addBooleanArray(value);
		return this;
	}
	
	/**
	 * It is highly recommended to use the fully named functions (like addByteArray(), addIntegerArray(), etc) for adding Collections.<br>
	 * <br>
	 * The reason for this is because of Java's type erasure, see this function's code to see what I mean.<br>
	 * <br>
	 * This function gets the type of the given Collection by retrieving the items and by then trying to identify what type it is.<br>
	 * <br>
	 * This method will perform unexpected behavior if the given Collection only contains null values, it will treat the Collection as empty then.
	 */
	@SuppressWarnings("unchecked")
	public ByteDataWriter add(Collection<?> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		for(Object object : value)
		{
			if(object == null)
			{
				continue;
			}
			
			if(object instanceof Byte)
			{
				addByteArray((Collection<Byte>) value);
			}
			else if(object instanceof Integer)
			{
				addIntegerArray((Collection<Integer>) value);
			}
			else if(object instanceof Long)
			{
				addLongArray((Collection<Long>) value);
			}
			else if(object instanceof Float)
			{
				addFloatArray((Collection<Float>) value);
			}
			else if(object instanceof Double)
			{
				addDoubleArray((Collection<Double>) value);
			}
			else if(object instanceof Boolean)
			{
				addBooleanArray((Collection<Boolean>) value);
			}
			else if(object instanceof String)
			{
				addStringUtf8Array((Collection<String>) value);
			}
			else
			{
				addUinteger(0);
			}
			return this;
		}
		
		addUinteger(0);
		return this;
	}
	
	
	/**
	 * Adds a byte.
	 */
	public ByteDataWriter addByte(byte value)
	{
		addRawByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public ByteDataWriter addInteger(int value)
	{
		addingUnsafe(4);
		addRawByteUnsafe((byte) (value >> 24));
		addRawByteUnsafe((byte) (value >> 16));
		addRawByteUnsafe((byte) (value >> 8));
		addRawByteUnsafe((byte) (value));
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 128 in 1 byte, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger1(int value)
	{
		if(value <= 0)
		{
			addRawByte((byte) 0);
		}
		else if(value < 128)
		{
			addRawByte((byte) (value));
		}
		else
		{
			addingUnsafe(4);
			addRawByteUnsafe((byte) ((value >> 24) | (1 << 7)));
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 32.768 in 2 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger2(int value)
	{
		if(value <= 0)
		{
			addRawByte((byte) 0);
		}
		else if(value < 32768)
		{
			addingUnsafe(2);
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
		else
		{
			addingUnsafe(4);
			addRawByteUnsafe((byte) ((value >> 24) | (1 << 7)));
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 8.388.608 in 3 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger3(int value)
	{
		if(value <= 0)
		{
			addRawByte((byte) 0);
		}
		else if(value < 8388608)
		{
			addingUnsafe(3);
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
		else
		{
			addingUnsafe(4);
			addRawByteUnsafe((byte) ((value >> 24) | (1 << 7)));
			addRawByteUnsafe((byte) (value >> 16));
			addRawByteUnsafe((byte) (value >> 8));
			addRawByteUnsafe((byte) (value));
		}
		return this;
	}
	/**
	 * Adds a long.
	 */
	public ByteDataWriter addLong(long value)
	{
		addingUnsafe(8);
		addRawByteUnsafe((byte) (value >> 56));
		addRawByteUnsafe((byte) (value >> 48));
		addRawByteUnsafe((byte) (value >> 40));
		addRawByteUnsafe((byte) (value >> 32));
		addRawByteUnsafe((byte) (value >> 24));
		addRawByteUnsafe((byte) (value >> 16));
		addRawByteUnsafe((byte) (value >> 8));
		addRawByteUnsafe((byte) (value));
		return this;
	}
	/**
	 * Adds a float.
	 */
	public ByteDataWriter addFloat(float value)
	{
		addInteger(Float.floatToIntBits(value));
		return this;
	}
	/**
	 * Adds a double.
	 */
	public ByteDataWriter addDouble(double value)
	{
		addLong(Double.doubleToLongBits(value));
		return this;
	}
	/**
	 * Adds a boolean.
	 */
	public ByteDataWriter addBoolean(boolean value)
	{
		if(value)
		{
			addRawByte(LowEntry.BOOLEAN_TRUE_BYTE);
		}
		else
		{
			addRawByte(LowEntry.BOOLEAN_FALSE_BYTE);
		}
		return this;
	}
	/**
	 * Adds a String (UTF-8).
	 */
	public ByteDataWriter addStringUtf8(String value)
	{
		addByteArray(LowEntry.stringToBytesUtf8(value));
		return this;
	}
	/**
	 * Adds a String (Latin-1, ISO-8859-1).
	 */
	public ByteDataWriter addStringLatin1(String value)
	{
		addByteArray(LowEntry.stringToBytesLatin1(value));
		return this;
	}
	
	
	/**
	 * Adds a byte.
	 */
	public ByteDataWriter addByte(Byte value)
	{
		if(value == null)
		{
			addByte((byte) 0);
		}
		else
		{
			addByte(value.byteValue());
		}
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public ByteDataWriter addInteger(Integer value)
	{
		if(value == null)
		{
			addInteger(0);
		}
		else
		{
			addInteger(value.intValue());
		}
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 128 in 1 byte, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger1(Integer value)
	{
		if(value == null)
		{
			addPositiveInteger1(0);
		}
		else
		{
			addPositiveInteger1(value.intValue());
		}
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 32.768 in 2 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger2(Integer value)
	{
		if(value == null)
		{
			addPositiveInteger2(0);
		}
		else
		{
			addPositiveInteger2(value.intValue());
		}
		return this;
	}
	/**
	 * Adds a positive integer.<br>
	 * <br>
	 * Will store values below 8.388.608 in 3 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integer has to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger3(Integer value)
	{
		if(value == null)
		{
			addPositiveInteger3(0);
		}
		else
		{
			addPositiveInteger3(value.intValue());
		}
		return this;
	}
	/**
	 * Adds a long.
	 */
	public ByteDataWriter addLong(Long value)
	{
		if(value == null)
		{
			addLong(0);
		}
		else
		{
			addLong(value.longValue());
		}
		return this;
	}
	/**
	 * Adds a float.
	 */
	public ByteDataWriter addFloat(Float value)
	{
		if(value == null)
		{
			addFloat(0);
		}
		else
		{
			addFloat(value.floatValue());
		}
		return this;
	}
	/**
	 * Adds a double.
	 */
	public ByteDataWriter addDouble(Double value)
	{
		if(value == null)
		{
			addDouble(0);
		}
		else
		{
			addDouble(value.doubleValue());
		}
		return this;
	}
	/**
	 * Adds a boolean.
	 */
	public ByteDataWriter addBoolean(Boolean value)
	{
		if(value == null)
		{
			addBoolean(false);
		}
		else
		{
			addBoolean(value.booleanValue());
		}
		return this;
	}
	
	
	/**
	 * Adds a byte array.
	 */
	public ByteDataWriter addByteArray(byte[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		addRawBytes(value);
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public ByteDataWriter addIntegerArray(int[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addInteger(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 128 in 1 byte, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger1Array(int[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addPositiveInteger1(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 32.768 in 2 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger2Array(int[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addPositiveInteger2(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 8.388.608 in 3 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger3Array(int[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addPositiveInteger3(v);
		}
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public ByteDataWriter addLongArray(long[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(long v : value)
		{
			addLong(v);
		}
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public ByteDataWriter addFloatArray(float[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(float v : value)
		{
			addFloat(v);
		}
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public ByteDataWriter addDoubleArray(double[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(double v : value)
		{
			addDouble(v);
		}
		return this;
	}
	/**
	 * Adds a boolean array.
	 */
	public ByteDataWriter addBooleanArray(boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		byte b = 0;
		int bIndex = 0;
		for(boolean v : value)
		{
			if(v)
			{
				b |= (1 << (7 - bIndex));
			}
			bIndex++;
			if(bIndex == 8)
			{
				addRawByte(b);
				b = 0;
				bIndex = 0;
			}
		}
		if(bIndex > 0)
		{
			addRawByte(b);
		}
		return this;
	}
	/**
	 * Adds a String (UTF-8) array.
	 */
	public ByteDataWriter addStringUtf8Array(String[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(String v : value)
		{
			addStringUtf8(v);
		}
		return this;
	}
	/**
	 * Adds a String (Latin-1, ISO-8859-1) array.
	 */
	public ByteDataWriter addStringLatin1Array(String[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(String v : value)
		{
			addStringLatin1(v);
		}
		return this;
	}
	
	
	/**
	 * Adds a byte array.
	 */
	public ByteDataWriter addByteArray(Byte[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Byte v : value)
		{
			addByte(v);
		}
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public ByteDataWriter addIntegerArray(Integer[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addInteger(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 128 in 1 byte, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger1Array(Integer[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addPositiveInteger1(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 32.768 in 2 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger2Array(Integer[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addPositiveInteger2(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 8.388.608 in 3 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger3Array(Integer[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addPositiveInteger3(v);
		}
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public ByteDataWriter addLongArray(Long[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Long v : value)
		{
			addLong(v);
		}
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public ByteDataWriter addFloatArray(Float[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Float v : value)
		{
			addFloat(v);
		}
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public ByteDataWriter addDoubleArray(Double[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Double v : value)
		{
			addDouble(v);
		}
		return this;
	}
	/**
	 * Adds a boolean array.
	 */
	public ByteDataWriter addBooleanArray(Boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		byte b = 0;
		int bIndex = 0;
		for(Boolean v : value)
		{
			if(v)
			{
				b |= (1 << (7 - bIndex));
			}
			bIndex++;
			if(bIndex == 8)
			{
				addRawByte(b);
				b = 0;
				bIndex = 0;
			}
		}
		if(bIndex > 0)
		{
			addRawByte(b);
		}
		return this;
	}
	
	
	/**
	 * Adds a byte array.
	 */
	public ByteDataWriter addByteArray(Collection<Byte> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Byte v : value)
		{
			addByte(v);
		}
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public ByteDataWriter addIntegerArray(Collection<Integer> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addInteger(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 128 in 1 byte, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger1Array(Collection<Integer> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addPositiveInteger1(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 32.768 in 2 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger2Array(Collection<Integer> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addPositiveInteger2(v);
		}
		return this;
	}
	/**
	 * Adds a positive integer array.<br>
	 * <br>
	 * Will store values below 8.388.608 in 3 bytes, higher values will be stored in 4 bytes.<br>
	 * <br>
	 * The given integers have to be 0 or higher, values under 0 will be changed to 0.
	 */
	public ByteDataWriter addPositiveInteger3Array(Collection<Integer> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addPositiveInteger3(v);
		}
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public ByteDataWriter addLongArray(Collection<Long> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Long v : value)
		{
			addLong(v);
		}
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public ByteDataWriter addFloatArray(Collection<Float> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Float v : value)
		{
			addFloat(v);
		}
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public ByteDataWriter addDoubleArray(Collection<Double> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Double v : value)
		{
			addDouble(v);
		}
		return this;
	}
	/**
	 * Adds a boolean array.
	 */
	public ByteDataWriter addBooleanArray(Collection<Boolean> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		byte b = 0;
		int bIndex = 0;
		for(Boolean v : value)
		{
			if(v)
			{
				b |= (1 << (7 - bIndex));
			}
			bIndex++;
			if(bIndex == 8)
			{
				addRawByte(b);
				b = 0;
				bIndex = 0;
			}
		}
		if(bIndex > 0)
		{
			addRawByte(b);
		}
		return this;
	}
	/**
	 * Adds a String (UTF-8) array.
	 */
	public ByteDataWriter addStringUtf8Array(Collection<String> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(String v : value)
		{
			addStringUtf8(v);
		}
		return this;
	}
	/**
	 * Adds a String (Latin-1, ISO-8859-1) array.
	 */
	public ByteDataWriter addStringLatin1Array(Collection<String> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(String v : value)
		{
			addStringLatin1(v);
		}
		return this;
	}
}
