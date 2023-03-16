package lowentry.ue4.classes;


import lowentry.ue4.library.LowEntry;


/**
 * This class is NOT thread safe!
 */
public abstract class ByteDataReader
{
	protected int position = 0;
	
	
	/**
	 * Creates a clone of this ByteDataReader.<br>
	 * <br>
	 * Allows you to easily read and revert the position (by cloning, reading data with the clone, and then throwing the clone away).
	 */
	public abstract ByteDataReader getClone();
	
	
	protected abstract int getTotalCountImplementation();
	protected abstract byte getByteImplementation(int index);
	
	
	/**
	 * Returns the current position.
	 */
	public int position()
	{
		return position;
	}
	
	/**
	 * Sets the current position.
	 */
	public ByteDataReader position(int position)
	{
		this.position = Math.max(0, position);
		return this;
	}
	
	/**
	 * Resets the current position to 0.
	 */
	public ByteDataReader reset()
	{
		position = 0;
		return this;
	}
	
	/**
	 * Causes remaining() to return 0.
	 */
	public ByteDataReader empty()
	{
		position = getTotalCountImplementation();
		return this;
	}
	
	
	/**
	 * Increases the position and returns the previous position.
	 */
	protected int getAndIncreasePosition(int increasement)
	{
		int count = getTotalCountImplementation();
		
		int pos = position;
		if((count - increasement) <= position)
		{
			position = count;
		}
		else
		{
			position += increasement;
		}
		return pos;
	}
	
	/**
	 * Returns the amount of bytes left.
	 */
	public int remaining()
	{
		return getTotalCountImplementation() - position;
	}
	
	/**
	 * Calculates how many elements can possible be remaining.
	 */
	protected int maxElementsRemaining(int minimumSizePerElement)
	{
		int remaining = remaining();
		if(remaining <= 0)
		{
			return 0;
		}
		if(minimumSizePerElement <= 1)
		{
			return remaining;
		}
		return (remaining / minimumSizePerElement) + 1;
	}
	
	
	protected int getUinteger()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(1);
		if(count <= pos)
		{
			return 0;
		}
		byte b = getByteImplementation(pos);
		if(((b >> 7) & 1) == 0)
		{
			return (b & 0xFF);
		}
		
		pos = getAndIncreasePosition(3);
		if(count <= (pos + 2))
		{
			return 0;
		}
		int value = (((b & 0xFF) & ~(1 << 7)) << 24) | ((getByteImplementation(pos) & 0xFF) << 16) | ((getByteImplementation(pos + 1) & 0xFF) << 8) | (getByteImplementation(pos + 2) & 0xFF);
		if(value < 128)
		{
			return 0;
		}
		return value;
	}
	
	
	/**
	 * Gets a byte.
	 */
	public byte getByte()
	{
		int pos = getAndIncreasePosition(1);
		if(getTotalCountImplementation() <= pos)
		{
			return 0;
		}
		return getByteImplementation(pos);
	}
	
	/**
	 * Gets an integer.
	 */
	public int getInteger()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(4);
		if(count <= pos)
		{
			return 0;
		}
		if(count <= (pos + 1))
		{
			return (getByteImplementation(pos) & 0xFF);
		}
		if(count <= (pos + 2))
		{
			return ((getByteImplementation(pos) & 0xFF) << 8) | (getByteImplementation(pos + 1) & 0xFF);
		}
		if(count <= (pos + 3))
		{
			return ((getByteImplementation(pos) & 0xFF) << 16) | ((getByteImplementation(pos + 1) & 0xFF) << 8) | (getByteImplementation(pos + 2) & 0xFF);
		}
		return ((getByteImplementation(pos) & 0xFF) << 24) | ((getByteImplementation(pos + 1) & 0xFF) << 16) | ((getByteImplementation(pos + 2) & 0xFF) << 8) | (getByteImplementation(pos + 3) & 0xFF);
	}
	
	/**
	 * Gets a positive integer.
	 */
	public int getPositiveInteger1()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(1);
		if(count <= pos)
		{
			return 0;
		}
		byte b = getByteImplementation(pos);
		if(((b >> 7) & 1) == 0)
		{
			return (b & 0xFF);
		}
		
		pos = getAndIncreasePosition(3);
		if(count <= (pos + 2))
		{
			return 0;
		}
		int value = (((b & 0xFF) & ~(1 << 7)) << 24) | ((getByteImplementation(pos) & 0xFF) << 16) | ((getByteImplementation(pos + 1) & 0xFF) << 8) | (getByteImplementation(pos + 2) & 0xFF);
		if(value < 128)
		{
			return 0;
		}
		return value;
	}
	
	/**
	 * Gets a positive integer.
	 */
	public int getPositiveInteger2()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(2);
		if(count <= (pos + 1))
		{
			return 0;
		}
		byte b1 = getByteImplementation(pos);
		byte b2 = getByteImplementation(pos + 1);
		if(((b1 >> 7) & 1) == 0)
		{
			return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
		}
		
		pos = getAndIncreasePosition(2);
		if(count <= (pos + 1))
		{
			return 0;
		}
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((getByteImplementation(pos) & 0xFF) << 8) | (getByteImplementation(pos + 1) & 0xFF);
		if(value < 32768)
		{
			return 0;
		}
		return value;
	}
	
	/**
	 * Gets a positive integer.
	 */
	public int getPositiveInteger3()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(3);
		if(count <= (pos + 2))
		{
			return 0;
		}
		byte b1 = getByteImplementation(pos);
		byte b2 = getByteImplementation(pos + 1);
		byte b3 = getByteImplementation(pos + 2);
		if(((b1 >> 7) & 1) == 0)
		{
			return ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
		}
		
		pos = getAndIncreasePosition(1);
		if(count <= pos)
		{
			return 0;
		}
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (getByteImplementation(pos) & 0xFF);
		if(value < 8388608)
		{
			return 0;
		}
		return value;
	}
	
	/**
	 * Gets a long.
	 */
	public long getLong()
	{
		int count = getTotalCountImplementation();
		
		int pos = getAndIncreasePosition(8);
		if(count <= pos)
		{
			return 0;
		}
		if(count <= (pos + 1))
		{
			return ((long) getByteImplementation(pos) & 0xFF);
		}
		if(count <= (pos + 2))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 8) | ((long) getByteImplementation(pos + 1) & 0xFF);
		}
		if(count <= (pos + 3))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 16) | (((long) getByteImplementation(pos + 1) & 0xFF) << 8) | ((long) getByteImplementation(pos + 2) & 0xFF);
		}
		if(count <= (pos + 4))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 24) | (((long) getByteImplementation(pos + 1) & 0xFF) << 16) | (((long) getByteImplementation(pos + 2) & 0xFF) << 8) | ((long) getByteImplementation(pos + 3) & 0xFF);
		}
		if(count <= (pos + 5))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 32) | (((long) getByteImplementation(pos + 1) & 0xFF) << 24) | (((long) getByteImplementation(pos + 2) & 0xFF) << 16) | (((long) getByteImplementation(pos + 3) & 0xFF) << 8) | ((long) getByteImplementation(pos + 4) & 0xFF);
		}
		if(count <= (pos + 6))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 40) | (((long) getByteImplementation(pos + 1) & 0xFF) << 32) | (((long) getByteImplementation(pos + 2) & 0xFF) << 24) | (((long) getByteImplementation(pos + 3) & 0xFF) << 16) | (((long) getByteImplementation(pos + 4) & 0xFF) << 8) | ((long) getByteImplementation(pos + 5) & 0xFF);
		}
		if(count <= (pos + 7))
		{
			return (((long) getByteImplementation(pos) & 0xFF) << 48) | (((long) getByteImplementation(pos + 1) & 0xFF) << 40) | (((long) getByteImplementation(pos + 2) & 0xFF) << 32) | (((long) getByteImplementation(pos + 3) & 0xFF) << 24) | (((long) getByteImplementation(pos + 4) & 0xFF) << 16) | (((long) getByteImplementation(pos + 5) & 0xFF) << 8) | ((long) getByteImplementation(pos + 6) & 0xFF);
		}
		return (((long) getByteImplementation(pos) & 0xFF) << 56) | (((long) getByteImplementation(pos + 1) & 0xFF) << 48) | (((long) getByteImplementation(pos + 2) & 0xFF) << 40) | (((long) getByteImplementation(pos + 3) & 0xFF) << 32) | (((long) getByteImplementation(pos + 4) & 0xFF) << 24) | (((long) getByteImplementation(pos + 5) & 0xFF) << 16) | (((long) getByteImplementation(pos + 6) & 0xFF) << 8) | ((long) getByteImplementation(pos + 7) & 0xFF);
	}
	
	/**
	 * Gets a float.
	 */
	public float getFloat()
	{
		return Float.intBitsToFloat(getInteger());
	}
	
	/**
	 * Gets a double.
	 */
	public double getDouble()
	{
		return Double.longBitsToDouble(getLong());
	}
	
	/**
	 * Gets a boolean.
	 */
	public boolean getBoolean()
	{
		int pos = getAndIncreasePosition(1);
		if(getTotalCountImplementation() <= pos)
		{
			return false;
		}
		return (getByteImplementation(pos) == LowEntry.BOOLEAN_TRUE_BYTE);
	}
	
	/**
	 * Gets a String (UTF-8).
	 */
	public String getStringUtf8()
	{
		int count = getTotalCountImplementation();
		
		int length = getUinteger();
		if(length <= 0)
		{
			return "";
		}
		int pos = getAndIncreasePosition(length);
		if(count <= pos)
		{
			return "";
		}
		int maxLength = count - pos;
		if(length > maxLength)
		{
			length = maxLength;
		}
		return getStringUtf8Implementation(pos, length);
	}
	protected abstract String getStringUtf8Implementation(int index, int length);
	
	/**
	 * Gets a String (Latin-1, ISO-8859-1).
	 */
	public String getStringLatin1()
	{
		int count = getTotalCountImplementation();
		
		int length = getUinteger();
		if(length <= 0)
		{
			return "";
		}
		int pos = getAndIncreasePosition(length);
		if(count <= pos)
		{
			return "";
		}
		int maxLength = count - pos;
		if(length > maxLength)
		{
			length = maxLength;
		}
		return getStringLatin1Implementation(pos, length);
	}
	protected abstract String getStringLatin1Implementation(int index, int length);
	
	
	/**
	 * Gets a byte array.
	 */
	public byte[] getByteArray()
	{
		int count = getTotalCountImplementation();
		
		int length = getUinteger();
		if(length <= 0)
		{
			return new byte[0];
		}
		int pos = getAndIncreasePosition(length);
		if(count <= pos)
		{
			return new byte[0];
		}
		int maxLength = count - pos;
		if(length > maxLength)
		{
			length = maxLength;
		}
		return getByteArrayImplementation(pos, length);
	}
	protected abstract byte[] getByteArrayImplementation(int index, int length);
	
	/**
	 * Gets an integer array.
	 */
	public int[] getIntegerArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(4));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getInteger();
		}
		return array;
	}
	
	/**
	 * Gets a positive integer array.
	 */
	public int[] getPositiveInteger1Array()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getPositiveInteger1();
		}
		return array;
	}
	
	/**
	 * Gets a positive integer array.
	 */
	public int[] getPositiveInteger2Array()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(2));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getPositiveInteger2();
		}
		return array;
	}
	
	/**
	 * Gets a positive integer array.
	 */
	public int[] getPositiveInteger3Array()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(3));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getPositiveInteger3();
		}
		return array;
	}
	
	/**
	 * Gets a long array.
	 */
	public long[] getLongArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(8));
		if(length <= 0)
		{
			return new long[0];
		}
		long[] array = new long[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getLong();
		}
		return array;
	}
	
	/**
	 * Gets a float array.
	 */
	public float[] getFloatArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(4));
		if(length <= 0)
		{
			return new float[0];
		}
		float[] array = new float[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getFloat();
		}
		return array;
	}
	
	/**
	 * Gets a double array.
	 */
	public double[] getDoubleArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(8));
		if(length <= 0)
		{
			return new double[0];
		}
		double[] array = new double[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getDouble();
		}
		return array;
	}
	
	/**
	 * Gets a boolean array.
	 */
	public boolean[] getBooleanArray()
	{
		int length = getUinteger();
		length = Math.min(length, safeMultiply(maxElementsRemaining(1), 8));
		if(length <= 0)
		{
			return new boolean[0];
		}
		boolean[] array = new boolean[length];
		for(int i = 0; i < length; i += 8)
		{
			byte b = getByte();
			for(int bIndex = 0; bIndex < 8; bIndex++)
			{
				int index = i + bIndex;
				if(index >= length)
				{
					return array;
				}
				array[index] = (((b >> (7 - bIndex)) & 1) != 0);
			}
		}
		return array;
	}
	
	/**
	 * Gets a String (UTF-8) array.
	 */
	public String[] getStringUtf8Array()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return new String[0];
		}
		String[] array = new String[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getStringUtf8();
		}
		return array;
	}
	
	/**
	 * Gets a String (Latin-1, ISO-8859-1) array.
	 */
	public String[] getStringLatin1Array()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return new String[0];
		}
		String[] array = new String[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getStringLatin1();
		}
		return array;
	}
	
	
	protected static int safeMultiply(int a, int b)
	{
		long result = (long) a * (long) b;
		if(result > Integer.MAX_VALUE)
		{
			return Integer.MAX_VALUE;
		}
		if(result < Integer.MIN_VALUE)
		{
			return Integer.MIN_VALUE;
		}
		return (int) result;
	}
}
