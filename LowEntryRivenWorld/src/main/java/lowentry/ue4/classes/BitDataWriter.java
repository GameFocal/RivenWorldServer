package lowentry.ue4.classes;


import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;
import java.util.Collection;


/**
 * This class is NOT thread safe!
 */
public abstract class BitDataWriter
{
	protected static final byte[] mask = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0x07, (byte) 0x0F, (byte) 0x1F, (byte) 0x3F, (byte) 0x7F, (byte) 0xFF};
	
	protected byte currentByte         = 0;
	protected int  currentBytePosition = 0;
	
	
	protected abstract byte[] getBytesImplementation();
	protected abstract byte[] getBytesImplementation(byte addByteToEnd);
	
	protected ByteBuffer getByteBufferImplementation()
	{
		return ByteBuffer.wrap(getBytesImplementation());
	}
	protected ByteBuffer getByteBufferImplementation(byte addByteToEnd)
	{
		return ByteBuffer.wrap(getBytesImplementation(addByteToEnd));
	}
	
	
	protected abstract void resetImplementation();
	
	
	protected abstract void addRawByteImplementation(byte value);
	protected abstract void addRawBytesImplementation(byte[] value);
	
	protected abstract void addingUnsafeImplementation(int count);
	protected abstract void addRawByteUnsafeImplementation(byte value);
	protected abstract void addRawBytesUnsafeImplementation(byte[] value);
	
	
	/**
	 * Returns the bytes that have been stored so far.
	 */
	public byte[] getBytes()
	{
		if(currentBytePosition == 0)
		{
			return getBytesImplementation();
		}
		else
		{
			return getBytesImplementation(currentByte);
		}
	}
	
	/**
	 * Returns the bytes that have been stored so far.
	 */
	public ByteBuffer getByteBuffer()
	{
		if(currentBytePosition == 0)
		{
			return getByteBufferImplementation();
		}
		else
		{
			return getByteBufferImplementation(currentByte);
		}
	}
	
	/**
	 * Makes it possible to reuse a BitDataWriter's allocated buffer.
	 */
	public BitDataWriter reset()
	{
		resetImplementation();
		currentByte = 0;
		currentBytePosition = 0;
		return this;
	}
	
	
	protected void addRawBit(boolean bit)
	{
		if(bit)
		{
			currentByte |= (1 << currentBytePosition);
		}
		
		if(currentBytePosition == 7)
		{
			addRawByteImplementation(currentByte);
			currentByte = 0;
			currentBytePosition = 0;
		}
		else
		{
			currentBytePosition++;
		}
	}
	
	protected void addRawByte(byte b)
	{
		if(currentBytePosition == 0)
		{
			addRawByteImplementation(b);
		}
		else
		{
			currentByte |= (b << currentBytePosition);
			addRawByteImplementation(currentByte);
			currentByte = (byte) ((b >> (8 - currentBytePosition)) & mask[currentBytePosition]);
		}
	}
	
	protected void addPartialRawByte(byte b, int bits)
	{
		if(bits == 0)
		{
			return;
		}
		if((bits >= 8) || (bits <= -8))
		{
			addRawByte(b);
			return;
		}
		
		if(bits < 0)
		{
			bits = -bits;
			b >>= (8 - bits);
		}
		
		b &= mask[bits];
		
		currentByte |= (b << currentBytePosition);
		currentBytePosition += bits;
		
		if(currentBytePosition >= 8)
		{
			addRawByteImplementation(currentByte);
			currentBytePosition -= 8;
			if(currentBytePosition == 0)
			{
				currentByte = 0;
			}
			else
			{
				currentByte = (byte) (b >> (bits - currentBytePosition)); // b is already masked
			}
		}
	}
	
	protected void addRawBytes(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		
		if(currentBytePosition == 0)
		{
			addRawBytesImplementation(value);
		}
		else
		{
			addingUnsafe(value.length);
			for(byte b : value)
			{
				addRawByteUnsafe(b);
			}
		}
	}
	
	
	protected void addingUnsafe(int count)
	{
		addingUnsafeImplementation(count);
	}
	
	protected void addRawBitUnsafe(boolean bit)
	{
		if(bit)
		{
			currentByte |= (1 << currentBytePosition);
		}
		
		if(currentBytePosition == 7)
		{
			addRawByteUnsafeImplementation(currentByte);
			currentByte = 0;
			currentBytePosition = 0;
		}
		else
		{
			currentBytePosition++;
		}
	}
	
	protected void addRawByteUnsafe(byte b)
	{
		if(currentBytePosition == 0)
		{
			addRawByteUnsafeImplementation(b);
		}
		else
		{
			currentByte |= (b << currentBytePosition);
			addRawByteUnsafeImplementation(currentByte);
			currentByte = (byte) ((b >> (8 - currentBytePosition)) & mask[currentBytePosition]);
		}
	}
	
	protected void addPartialRawByteUnsafe(byte b, int bits)
	{
		if(bits == 0)
		{
			return;
		}
		if((bits >= 8) || (bits <= -8))
		{
			addRawByteUnsafe(b);
			return;
		}
		
		if(bits < 0)
		{
			bits = -bits;
			b >>= (8 - bits);
		}
		
		b &= mask[bits];
		
		currentByte |= (b << currentBytePosition);
		currentBytePosition += bits;
		
		if(currentBytePosition >= 8)
		{
			addRawByteUnsafeImplementation(currentByte);
			currentBytePosition -= 8;
			if(currentBytePosition == 0)
			{
				currentByte = 0;
			}
			else
			{
				currentByte = (byte) (b >> (bits - currentBytePosition)); // b is already masked
			}
		}
	}
	
	protected void addRawBytesUnsafe(byte[] value)
	{
		if(value == null)
		{
			return;
		}
		
		if(currentBytePosition == 0)
		{
			addRawBytesUnsafeImplementation(value);
		}
		else
		{
			for(byte b : value)
			{
				addRawByteUnsafe(b);
			}
		}
	}
	
	
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
	public BitDataWriter add(byte value)
	{
		addByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public BitDataWriter add(int value)
	{
		addInteger(value);
		return this;
	}
	/**
	 * Adds a long.
	 */
	public BitDataWriter add(long value)
	{
		addLong(value);
		return this;
	}
	/**
	 * Adds a float.
	 */
	public BitDataWriter add(float value)
	{
		addFloat(value);
		return this;
	}
	/**
	 * Adds a double.
	 */
	public BitDataWriter add(double value)
	{
		addDouble(value);
		return this;
	}
	/**
	 * Adds a boolean, this does the same as adding a bit.
	 */
	public BitDataWriter add(boolean value)
	{
		addBoolean(value);
		return this;
	}
	/**
	 * Adds a String (UTF-8).
	 */
	public BitDataWriter add(String value)
	{
		addStringUtf8(value);
		return this;
	}
	
	/**
	 * Adds a byte.
	 */
	public BitDataWriter add(Byte value)
	{
		addByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public BitDataWriter add(Integer value)
	{
		addInteger(value);
		return this;
	}
	/**
	 * Adds a long.
	 */
	public BitDataWriter add(Long value)
	{
		addLong(value);
		return this;
	}
	/**
	 * Adds a float.
	 */
	public BitDataWriter add(Float value)
	{
		addFloat(value);
		return this;
	}
	/**
	 * Adds a double.
	 */
	public BitDataWriter add(Double value)
	{
		addDouble(value);
		return this;
	}
	/**
	 * Adds a boolean, this does the same as adding a bit.
	 */
	public BitDataWriter add(Boolean value)
	{
		addBoolean(value);
		return this;
	}
	
	/**
	 * Adds a byte array.
	 */
	public BitDataWriter add(byte[] value)
	{
		addByteArray(value);
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public BitDataWriter add(int[] value)
	{
		addIntegerArray(value);
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public BitDataWriter add(long[] value)
	{
		addLongArray(value);
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public BitDataWriter add(float[] value)
	{
		addFloatArray(value);
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public BitDataWriter add(double[] value)
	{
		addDoubleArray(value);
		return this;
	}
	/**
	 * Adds a boolean array, this does the same as adding a bit array.
	 */
	public BitDataWriter add(boolean[] value)
	{
		addBooleanArray(value);
		return this;
	}
	/**
	 * Adds a String (UTF-8) array.
	 */
	public BitDataWriter add(String[] value)
	{
		addStringUtf8Array(value);
		return this;
	}
	
	/**
	 * Adds a byte array.
	 */
	public BitDataWriter add(Byte[] value)
	{
		addByteArray(value);
		return this;
	}
	/**
	 * Adds an integer array.
	 */
	public BitDataWriter add(Integer[] value)
	{
		addIntegerArray(value);
		return this;
	}
	/**
	 * Adds a long array.
	 */
	public BitDataWriter add(Long[] value)
	{
		addLongArray(value);
		return this;
	}
	/**
	 * Adds a float array.
	 */
	public BitDataWriter add(Float[] value)
	{
		addFloatArray(value);
		return this;
	}
	/**
	 * Adds a double array.
	 */
	public BitDataWriter add(Double[] value)
	{
		addDoubleArray(value);
		return this;
	}
	/**
	 * Adds a boolean array, this does the same as adding a bit array.
	 */
	public BitDataWriter add(Boolean[] value)
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
	public BitDataWriter add(Collection<?> value)
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
	 * Adds a bit.
	 */
	public BitDataWriter addBit(boolean value)
	{
		addRawBit(value);
		return this;
	}
	/**
	 * Adds a byte, will only add a certain amount of bits from the given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value 63 and bitcount 4, only the lowest 4 bits will be added, meaning only 1111 will be added, which will then have a value of 15 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteLeastSignificantBits(byte value, int bitcount)
	{
		if(bitcount > 0)
		{
			addPartialRawByte(value, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte, will only add a certain amount of bits from the given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value 63 and bitcount 4, only the highest 4 bits will be added, meaning only 0011 will be added, which will then have a value of 48 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteMostSignificantBits(byte value, int bitcount)
	{
		if(bitcount > 0)
		{
			addPartialRawByte(value, -bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer, will only add a certain amount of bits from the given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value 268435471 and bitcount 4, only the lowest 4 bits will be added, meaning only 1111 will be added, which will then have a value of 15 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerLeastSignificantBits(int value, int bitcount)
	{
		if(bitcount > 0)
		{
			if(bitcount >= 32)
			{
				addingUnsafe(4);
				addRawByteUnsafe((byte) (value));
				addRawByteUnsafe((byte) (value >> 8));
				addRawByteUnsafe((byte) (value >> 16));
				addRawByteUnsafe((byte) (value >> 24));
			}
			else if(bitcount > 24)
			{
				addingUnsafe(4);
				addRawByteUnsafe((byte) (value));
				addRawByteUnsafe((byte) (value >> 8));
				addRawByteUnsafe((byte) (value >> 16));
				addPartialRawByteUnsafe((byte) (value >> 24), (bitcount - 24));
			}
			else if(bitcount > 16)
			{
				addingUnsafe(3);
				addRawByteUnsafe((byte) (value));
				addRawByteUnsafe((byte) (value >> 8));
				addPartialRawByteUnsafe((byte) (value >> 16), (bitcount - 16));
			}
			else if(bitcount > 8)
			{
				addingUnsafe(2);
				addRawByteUnsafe((byte) (value));
				addPartialRawByteUnsafe((byte) (value >> 8), (bitcount - 8));
			}
			else
			{
				addPartialRawByte((byte) (value), bitcount);
			}
		}
		return this;
	}
	/**
	 * Adds an integer, will only add a certain amount of bits from the given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value 268435471 and bitcount 4, only the highest 4 bits will be added, meaning only 0001 will be added, which will then have a value of 268435456 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerMostSignificantBits(int value, int bitcount)
	{
		if(bitcount > 0)
		{
			bitcount = -bitcount;
			if(bitcount <= -32)
			{
				addingUnsafe(4);
				addRawByteUnsafe((byte) (value));
				addRawByteUnsafe((byte) (value >> 8));
				addRawByteUnsafe((byte) (value >> 16));
				addRawByteUnsafe((byte) (value >> 24));
			}
			else if(bitcount < -24)
			{
				addingUnsafe(4);
				addPartialRawByteUnsafe((byte) (value), (bitcount + 24));
				addRawByteUnsafe((byte) (value >> 8));
				addRawByteUnsafe((byte) (value >> 16));
				addRawByteUnsafe((byte) (value >> 24));
			}
			else if(bitcount < -16)
			{
				addingUnsafe(3);
				addPartialRawByteUnsafe((byte) (value >> 8), (bitcount + 16));
				addRawByteUnsafe((byte) (value >> 16));
				addRawByteUnsafe((byte) (value >> 24));
			}
			else if(bitcount < -8)
			{
				addingUnsafe(2);
				addPartialRawByteUnsafe((byte) (value >> 16), (bitcount + 8));
				addRawByteUnsafe((byte) (value >> 24));
			}
			else
			{
				addPartialRawByte((byte) (value >> 24), bitcount);
			}
		}
		return this;
	}
	/**
	 * Adds a byte.
	 */
	public BitDataWriter addByte(byte value)
	{
		addRawByte(value);
		return this;
	}
	/**
	 * Adds an integer.
	 */
	public BitDataWriter addInteger(int value)
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
	public BitDataWriter addPositiveInteger1(int value)
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
	public BitDataWriter addPositiveInteger2(int value)
	{
		if(value <= 0)
		{
			addingUnsafe(2);
			addRawByteUnsafe((byte) 0);
			addRawByteUnsafe((byte) 0);
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
	public BitDataWriter addPositiveInteger3(int value)
	{
		if(value <= 0)
		{
			addingUnsafe(3);
			addRawByteUnsafe((byte) 0);
			addRawByteUnsafe((byte) 0);
			addRawByteUnsafe((byte) 0);
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
	public BitDataWriter addLong(long value)
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
	public BitDataWriter addFloat(float value)
	{
		addInteger(Float.floatToIntBits(value));
		return this;
	}
	/**
	 * Adds a double.
	 */
	public BitDataWriter addDouble(double value)
	{
		addLong(Double.doubleToLongBits(value));
		return this;
	}
	/**
	 * Adds a boolean, this does the same as adding a bit.
	 */
	public BitDataWriter addBoolean(boolean value)
	{
		addRawBit(value);
		return this;
	}
	/**
	 * Adds a String (UTF-8).
	 */
	public BitDataWriter addStringUtf8(String value)
	{
		addByteArray(LowEntry.stringToBytesUtf8(value));
		return this;
	}
	/**
	 * Adds a String (Latin-1, ISO-8859-1).
	 */
	public BitDataWriter addStringLatin1(String value)
	{
		addByteArray(LowEntry.stringToBytesLatin1(value));
		return this;
	}
	
	
	/**
	 * Adds a bit.
	 */
	public BitDataWriter addBit(Boolean value)
	{
		if(value == null)
		{
			addBit(false);
		}
		else
		{
			addBit(value.booleanValue());
		}
		return this;
	}
	/**
	 * Adds a byte, will only add a certain amount of bits from the given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value 63 and bitcount 4, only the lowest 4 bits will be added, meaning only 1111 will be added, which will then have a value of 15 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteLeastSignificantBits(Byte value, int bitcount)
	{
		if(value == null)
		{
			addByteLeastSignificantBits((byte) 0, bitcount);
		}
		else
		{
			addByteLeastSignificantBits(value.byteValue(), bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte, will only add a certain amount of bits from the given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value 63 and bitcount 4, only the highest 4 bits will be added, meaning only 0011 will be added, which will then have a value of 48 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteMostSignificantBits(Byte value, int bitcount)
	{
		if(value == null)
		{
			addByteMostSignificantBits((byte) 0, bitcount);
		}
		else
		{
			addByteMostSignificantBits(value.byteValue(), bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer, will only add a certain amount of bits from the given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value 268435471 and bitcount 4, only the lowest 4 bits will be added, meaning only 1111 will be added, which will then have a value of 15 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerLeastSignificantBits(Integer value, int bitcount)
	{
		if(value == null)
		{
			addIntegerLeastSignificantBits(0, bitcount);
		}
		else
		{
			addIntegerLeastSignificantBits(value.intValue(), bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer, will only add a certain amount of bits from the given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value 268435471 and bitcount 4, only the highest 4 bits will be added, meaning only 0001 will be added, which will then have a value of 268435456 when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerMostSignificantBits(Integer value, int bitcount)
	{
		if(value == null)
		{
			addIntegerMostSignificantBits(0, bitcount);
		}
		else
		{
			addIntegerMostSignificantBits(value.intValue(), bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte.
	 */
	public BitDataWriter addByte(Byte value)
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
	public BitDataWriter addInteger(Integer value)
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
	public BitDataWriter addPositiveInteger1(Integer value)
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
	public BitDataWriter addPositiveInteger2(Integer value)
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
	public BitDataWriter addPositiveInteger3(Integer value)
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
	public BitDataWriter addLong(Long value)
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
	public BitDataWriter addFloat(Float value)
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
	public BitDataWriter addDouble(Double value)
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
	 * Adds a boolean, this does the same as adding a bit.
	 */
	public BitDataWriter addBoolean(Boolean value)
	{
		if(value == null)
		{
			addRawBit(false);
		}
		else
		{
			addRawBit(value);
		}
		return this;
	}
	
	
	/**
	 * Adds a bit array.
	 */
	public BitDataWriter addBitArray(boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(boolean v : value)
		{
			addBit(v);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the lowest 4 bits of each byte will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayLeastSignificantBits(byte[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		bitcount = Math.max(0, Math.min(8, bitcount));
		
		addUinteger(value.length);
		for(byte v : value)
		{
			addByteLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the highest 4 bits of each byte will be added, meaning only 0011 0011 0011 will be added, which will then have a value of 48 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayMostSignificantBits(byte[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(byte v : value)
		{
			addByteMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the lowest 4 bits of each integer will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayLeastSignificantBits(int[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addIntegerLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the highest 4 bits of each integer will be added, meaning only 0001 0001 0001 will be added, which will then have a value of 268435456 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayMostSignificantBits(int[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(int v : value)
		{
			addIntegerMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array.
	 */
	public BitDataWriter addByteArray(byte[] value)
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
	public BitDataWriter addIntegerArray(int[] value)
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
	public BitDataWriter addPositiveInteger1Array(int[] value)
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
	public BitDataWriter addPositiveInteger2Array(int[] value)
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
	public BitDataWriter addPositiveInteger3Array(int[] value)
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
	public BitDataWriter addLongArray(long[] value)
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
	public BitDataWriter addFloatArray(float[] value)
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
	public BitDataWriter addDoubleArray(double[] value)
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
	 * Adds a boolean array, this does the same as adding a bit array.
	 */
	public BitDataWriter addBooleanArray(boolean[] value)
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
	 * Adds a String (UTF-8) array.
	 */
	public BitDataWriter addStringUtf8Array(String[] value)
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
	public BitDataWriter addStringLatin1Array(String[] value)
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
	 * Adds a bit array.
	 */
	public BitDataWriter addBitArray(Boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Boolean v : value)
		{
			addBit(v);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the lowest 4 bits of each byte will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayLeastSignificantBits(Byte[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Byte v : value)
		{
			addByteLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the highest 4 bits of each byte will be added, meaning only 0011 0011 0011 will be added, which will then have a value of 48 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayMostSignificantBits(Byte[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Byte v : value)
		{
			addByteMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the lowest 4 bits of each integer will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayLeastSignificantBits(Integer[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addIntegerLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the highest 4 bits of each integer will be added, meaning only 0001 0001 0001 will be added, which will then have a value of 268435456 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayMostSignificantBits(Integer[] value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Integer v : value)
		{
			addIntegerMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array.
	 */
	public BitDataWriter addByteArray(Byte[] value)
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
	public BitDataWriter addIntegerArray(Integer[] value)
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
	public BitDataWriter addPositiveInteger1Array(Integer[] value)
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
	public BitDataWriter addPositiveInteger2Array(Integer[] value)
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
	public BitDataWriter addPositiveInteger3Array(Integer[] value)
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
	public BitDataWriter addLongArray(Long[] value)
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
	public BitDataWriter addFloatArray(Float[] value)
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
	public BitDataWriter addDoubleArray(Double[] value)
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
	 * Adds a boolean array, this does the same as adding a bit array.
	 */
	public BitDataWriter addBooleanArray(Boolean[] value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.length);
		for(Boolean v : value)
		{
			addBoolean(v);
		}
		return this;
	}
	
	
	/**
	 * Adds a bit array.
	 */
	public BitDataWriter addBitArray(Collection<Boolean> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Boolean v : value)
		{
			addBit(v);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the lowest 4 bits of each byte will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayLeastSignificantBits(Collection<Byte> value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Byte v : value)
		{
			addByteLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array, will only add a certain amount of bits from every given byte.<br>
	 * <br>
	 * For example, take 63 as the value (bitwise this is: 0011 1111).<br>
	 * If you use this function with value {63, 63, 63} and bitcount 4, only the highest 4 bits of each byte will be added, meaning only 0011 0011 0011 will be added, which will then have a value of 48 for each byte when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public BitDataWriter addByteArrayMostSignificantBits(Collection<Byte> value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Byte v : value)
		{
			addByteMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the lowest 4 bits of each integer will be added, meaning only 1111 1111 1111 will be added, which will then have a value of 15 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayLeastSignificantBits(Collection<Integer> value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addIntegerLeastSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds an integer array, will only add a certain amount of bits from every given integer.<br>
	 * <br>
	 * For example, take 268435471 as the value (bitwise this is: 00010000 00000000 00000000 00001111).<br>
	 * If you use this function with value {268435471, 268435471, 268435471} and bitcount 4, only the highest 4 bits of each integer will be added, meaning only 0001 0001 0001 will be added, which will then have a value of 268435456 for each integer when read again.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public BitDataWriter addIntegerArrayMostSignificantBits(Collection<Integer> value, int bitcount)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Integer v : value)
		{
			addIntegerMostSignificantBits(v, bitcount);
		}
		return this;
	}
	/**
	 * Adds a byte array.
	 */
	public BitDataWriter addByteArray(Collection<Byte> value)
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
	public BitDataWriter addIntegerArray(Collection<Integer> value)
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
	public BitDataWriter addPositiveInteger1Array(Collection<Integer> value)
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
	public BitDataWriter addPositiveInteger2Array(Collection<Integer> value)
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
	public BitDataWriter addPositiveInteger3Array(Collection<Integer> value)
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
	public BitDataWriter addLongArray(Collection<Long> value)
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
	public BitDataWriter addFloatArray(Collection<Float> value)
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
	public BitDataWriter addDoubleArray(Collection<Double> value)
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
	 * Adds a boolean array, this does the same as adding a bit array.
	 */
	public BitDataWriter addBooleanArray(Collection<Boolean> value)
	{
		if(value == null)
		{
			addUinteger(0);
			return this;
		}
		
		addUinteger(value.size());
		for(Boolean v : value)
		{
			addBoolean(v);
		}
		return this;
	}
	/**
	 * Adds a String (UTF-8) array.
	 */
	public BitDataWriter addStringUtf8Array(Collection<String> value)
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
	public BitDataWriter addStringLatin1Array(Collection<String> value)
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
