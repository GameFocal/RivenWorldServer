package lowentry.ue4.classes;


import lowentry.ue4.library.LowEntry;


/**
 * This class is NOT thread safe!
 */
public abstract class BitDataReader
{
	protected static final byte[] mask = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x03, (byte) 0x07, (byte) 0x0F, (byte) 0x1F, (byte) 0x3F, (byte) 0x7F, (byte) 0xFF};
	
	protected int position = 0;
	
	protected byte currentByte         = 0;
	protected int  currentBytePosition = 0;
	
	
	/**
	 * Creates a clone of this BitDataReader.<br>
	 * <br>
	 * Allows you to easily read and revert the position (by cloning, reading data with the clone, and then throwing the clone away).
	 */
	public abstract BitDataReader getClone();
	
	
	protected abstract int getTotalCountImplementation();
	protected abstract byte getByteImplementation(int index);
	
	
	/**
	 * Returns the current position.<br>
	 * <br>
	 * Because this data writer works with bits, this function will only work correctly till 268.435.455 bytes (256 MB), beyond that, please use getClone() to rollback the position instead.
	 */
	public int position()
	{
		return (position * 8) + currentBytePosition;
	}
	
	/**
	 * Sets the current position.<br>
	 * <br>
	 * Because this data writer works with bits, this function will only work correctly till 268.435.455 bytes (256 MB), beyond that, please use getClone() to rollback the position instead.
	 */
	public BitDataReader position(int position)
	{
		if(position < 0)
		{
			reset();
		}
		else
		{
			int count = getTotalCountImplementation();
			
			this.position = position / 8;
			currentBytePosition = position % 8;
			currentByte = 0;
			if(this.position > count)
			{
				this.position = count;
				currentBytePosition = 0;
			}
			else if(currentBytePosition > 0)
			{
				if(this.position == 0)
				{
					currentBytePosition = 0;
				}
				else
				{
					currentByte = getByteImplementation(this.position - 1);
				}
			}
		}
		return this;
	}
	
	/**
	 * Resets the current position to 0.
	 */
	public BitDataReader reset()
	{
		position = 0;
		currentBytePosition = 0;
		currentByte = 0;
		return this;
	}
	
	/**
	 * Causes remaining() to return 0.
	 */
	public BitDataReader empty()
	{
		position = getTotalCountImplementation();
		currentBytePosition = 0;
		currentByte = 0;
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
		int count = getTotalCountImplementation();
		
		if(currentBytePosition != 0)
		{
			return (count - position) + 1;
		}
		return count - position;
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
	
	
	protected boolean getRawBit()
	{
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(1);
			if(getTotalCountImplementation() <= pos)
			{
				return false;
			}
			currentByte = getByteImplementation(pos);
		}
		
		boolean bit = (((currentByte >> currentBytePosition) & 1) != 0);
		
		if(currentBytePosition == 7)
		{
			currentBytePosition = 0;
		}
		else
		{
			currentBytePosition++;
		}
		
		return bit;
	}
	
	protected byte getRawByte()
	{
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(1);
			if(getTotalCountImplementation() <= pos)
			{
				return 0;
			}
			return getByteImplementation(pos);
		}
		else
		{
			byte b = (byte) ((currentByte >> currentBytePosition) & mask[8 - currentBytePosition]);
			
			int pos = getAndIncreasePosition(1);
			if(getTotalCountImplementation() <= pos)
			{
				currentBytePosition = 0;
				return b;
			}
			currentByte = getByteImplementation(pos);
			
			return (byte) (b | (currentByte << (8 - currentBytePosition)));
		}
	}
	
	protected byte getPartialRawByte(int bits)
	{
		if(bits == 0)
		{
			return 0;
		}
		if((bits >= 8) || (bits <= -8))
		{
			return getRawByte();
		}
		
		int count = getTotalCountImplementation();
		
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(1);
			if(count <= pos)
			{
				return 0;
			}
			currentByte = getByteImplementation(pos);
		}
		
		boolean mostSignificantBits = false;
		if(bits < 0)
		{
			bits = -bits;
			mostSignificantBits = true;
		}
		
		byte b = (byte) ((currentByte >> currentBytePosition) & mask[8 - currentBytePosition]);
		currentBytePosition += bits;
		
		if(currentBytePosition >= 8)
		{
			int pos = getAndIncreasePosition(1);
			if(count <= pos)
			{
				currentBytePosition = 0;
				b = (byte) (b & mask[bits]);
				if(mostSignificantBits)
				{
					return (byte) (b << (8 - bits));
				}
				return b;
			}
			currentByte = getByteImplementation(pos);
			currentBytePosition -= 8;
			
			if(currentBytePosition != 0)
			{
				b = (byte) ((b | (currentByte << (bits - currentBytePosition))) & mask[bits]);
				if(mostSignificantBits)
				{
					return (byte) (b << (8 - bits));
				}
				return b;
			}
		}
		b = (byte) (b & mask[bits]);
		if(mostSignificantBits)
		{
			return (byte) (b << (8 - bits));
		}
		return b;
	}
	
	protected int getUinteger()
	{
		if(remaining() < 1)
		{
			return 0;
		}
		
		byte b1 = getRawByte();
		if(((b1 >> 7) & 1) == 0)
		{
			return (b1 & 0xFF);
		}
		
		if(remaining() < 3)
		{
			empty();
			return 0;
		}
		
		byte b2 = getRawByte();
		byte b3 = getRawByte();
		byte b4 = getRawByte();
		
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
		if(value < 128)
		{
			return 0;
		}
		return value;
	}
	
	
	/**
	 * Gets a bit.
	 */
	public boolean getBit()
	{
		return getRawBit();
	}
	
	/**
	 * Gets a byte, will only retrieve a certain amount of bits to form the byte.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public byte getByteLeastSignificantBits(int bitcount)
	{
		if(bitcount <= 0)
		{
			return 0;
		}
		return getPartialRawByte(bitcount);
	}
	
	/**
	 * Gets a byte, will only retrieve a certain amount of bits to form the byte.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public byte getByteMostSignificantBits(int bitcount)
	{
		if(bitcount <= 0)
		{
			return 0;
		}
		return getPartialRawByte(-bitcount);
	}
	
	/**
	 * Gets an integer, will only retrieve a certain amount of bits to form the integer.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public int getIntegerLeastSignificantBits(int bitcount)
	{
		if(bitcount <= 0)
		{
			return 0;
		}
		if(bitcount >= 32)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			return ((b4 & 0xFF) << 24) | ((b3 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else if(bitcount > 24)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getPartialRawByte(bitcount - 24);
			return ((b4 & 0xFF) << 24) | ((b3 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else if(bitcount > 16)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getPartialRawByte(bitcount - 16);
			return ((b3 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else if(bitcount > 8)
		{
			byte b1 = getRawByte();
			byte b2 = getPartialRawByte(bitcount - 8);
			return ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else
		{
			byte b1 = getPartialRawByte(bitcount);
			return (b1 & 0xFF);
		}
	}
	
	/**
	 * Gets an integer, will only retrieve a certain amount of bits to form the integer.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public int getIntegerMostSignificantBits(int bitcount)
	{
		if(bitcount <= 0)
		{
			return 0;
		}
		bitcount = -bitcount;
		if(bitcount <= -32)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			return ((b4 & 0xFF) << 24) | ((b3 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else if(bitcount < -24)
		{
			byte b1 = getPartialRawByte(bitcount + 24);
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			return ((b4 & 0xFF) << 24) | ((b3 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b1 & 0xFF);
		}
		else if(bitcount < -16)
		{
			byte b1 = getPartialRawByte(bitcount + 16);
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			return ((b3 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b1 & 0xFF) << 8);
		}
		else if(bitcount < -8)
		{
			byte b1 = getPartialRawByte(bitcount + 8);
			byte b2 = getRawByte();
			return ((b2 & 0xFF) << 24) | ((b1 & 0xFF) << 16);
		}
		else
		{
			byte b1 = getPartialRawByte(bitcount);
			return ((b1 & 0xFF) << 24);
		}
	}
	
	/**
	 * Gets a byte.
	 */
	public byte getByte()
	{
		return getRawByte();
	}
	
	/**
	 * Gets an integer.
	 */
	public int getInteger()
	{
		int remaining = remaining();
		if(remaining >= 4)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
		}
		if(remaining == 3)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			return ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
		}
		if(remaining == 2)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
		}
		if(remaining == 1)
		{
			return (getRawByte() & 0xFF);
		}
		return 0;
	}
	
	/**
	 * Gets a positive integer.
	 */
	public int getPositiveInteger1()
	{
		if(remaining() < 1)
		{
			return 0;
		}
		
		byte b1 = getRawByte();
		if(((b1 >> 7) & 1) == 0)
		{
			return (b1 & 0xFF);
		}
		
		if(remaining() < 3)
		{
			empty();
			return 0;
		}
		
		byte b2 = getRawByte();
		byte b3 = getRawByte();
		byte b4 = getRawByte();
		
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
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
		if(remaining() < 2)
		{
			empty();
			return 0;
		}
		
		byte b1 = getRawByte();
		byte b2 = getRawByte();
		if(((b1 >> 7) & 1) == 0)
		{
			return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
		}
		
		if(remaining() < 2)
		{
			empty();
			return 0;
		}
		
		byte b3 = getRawByte();
		byte b4 = getRawByte();
		
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
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
		if(remaining() < 3)
		{
			empty();
			return 0;
		}
		
		byte b1 = getRawByte();
		byte b2 = getRawByte();
		byte b3 = getRawByte();
		if(((b1 >> 7) & 1) == 0)
		{
			return ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
		}
		
		if(remaining() < 1)
		{
			empty();
			return 0;
		}
		
		byte b4 = getRawByte();
		
		int value = (((b1 & 0xFF) & ~(1 << 7)) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
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
		int remaining = remaining();
		if(remaining >= 8)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			byte b5 = getRawByte();
			byte b6 = getRawByte();
			byte b7 = getRawByte();
			byte b8 = getRawByte();
			return (((long) b1 & 0xFF) << 56) | (((long) b2 & 0xFF) << 48) | (((long) b3 & 0xFF) << 40) | (((long) b4 & 0xFF) << 32) | (((long) b5 & 0xFF) << 24) | (((long) b6 & 0xFF) << 16) | (((long) b7 & 0xFF) << 8) | ((long) b8 & 0xFF);
		}
		if(remaining == 7)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			byte b5 = getRawByte();
			byte b6 = getRawByte();
			byte b7 = getRawByte();
			return (((long) b1 & 0xFF) << 48) | (((long) b2 & 0xFF) << 40) | (((long) b3 & 0xFF) << 32) | (((long) b4 & 0xFF) << 24) | (((long) b5 & 0xFF) << 16) | (((long) b6 & 0xFF) << 8) | ((long) b7 & 0xFF);
		}
		if(remaining == 6)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			byte b5 = getRawByte();
			byte b6 = getRawByte();
			return (((long) b1 & 0xFF) << 40) | (((long) b2 & 0xFF) << 32) | (((long) b3 & 0xFF) << 24) | (((long) b4 & 0xFF) << 16) | (((long) b5 & 0xFF) << 8) | ((long) b6 & 0xFF);
		}
		if(remaining == 5)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			byte b5 = getRawByte();
			return (((long) b1 & 0xFF) << 32) | (((long) b2 & 0xFF) << 24) | (((long) b3 & 0xFF) << 16) | (((long) b4 & 0xFF) << 8) | ((long) b5 & 0xFF);
		}
		if(remaining == 4)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			byte b4 = getRawByte();
			return (((long) b1 & 0xFF) << 24) | (((long) b2 & 0xFF) << 16) | (((long) b3 & 0xFF) << 8) | ((long) b4 & 0xFF);
		}
		if(remaining == 3)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			byte b3 = getRawByte();
			return (((long) b1 & 0xFF) << 16) | (((long) b2 & 0xFF) << 8) | ((long) b3 & 0xFF);
		}
		if(remaining == 2)
		{
			byte b1 = getRawByte();
			byte b2 = getRawByte();
			return (((long) b1 & 0xFF) << 8) | ((long) b2 & 0xFF);
		}
		if(remaining == 1)
		{
			return ((long) getRawByte() & 0xFF);
		}
		return 0;
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
	 * Gets a boolean, this does the same as getting a bit.
	 */
	public boolean getBoolean()
	{
		return getRawBit();
	}
	
	/**
	 * Gets a String (UTF-8).
	 */
	public String getStringUtf8()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return "";
		}
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(length);
			return getStringUtf8Implementation(pos, length);
		}
		byte[] array = new byte[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getRawByte();
		}
		return LowEntry.bytesToStringUtf8(array);
	}
	protected abstract String getStringUtf8Implementation(int index, int length);
	
	/**
	 * Gets a String (Latin-1, ISO-8859-1).
	 */
	public String getStringLatin1()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return "";
		}
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(length);
			return getStringLatin1Implementation(pos, length);
		}
		byte[] array = new byte[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getRawByte();
		}
		return LowEntry.bytesToStringLatin1(array);
	}
	protected abstract String getStringLatin1Implementation(int index, int length);
	
	
	/**
	 * Gets a bit array.
	 */
	public boolean[] getBitArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return new boolean[0];
		}
		boolean[] array = new boolean[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getRawBit();
		}
		return array;
	}
	
	/**
	 * Gets a byte array, will only retrieve a certain amount of bits to form every byte.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public byte[] getByteArrayLeastSignificantBits(int bitcount)
	{
		int length = getUinteger();
		if(bitcount <= 0)
		{
			return new byte[0];
		}
		length = Math.min(length, (int) Math.round(Math.ceil(maxElementsRemaining(1) * (8.0 / bitcount))));
		if(length <= 0)
		{
			return new byte[0];
		}
		byte[] array = new byte[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getByteLeastSignificantBits(bitcount);
		}
		return array;
	}
	
	/**
	 * Gets a byte array, will only retrieve a certain amount of bits to form every byte.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 8, values higher or lower will be clamped to 0 to 8.
	 */
	public byte[] getByteArrayMostSignificantBits(int bitcount)
	{
		int length = getUinteger();
		if(bitcount <= 0)
		{
			return new byte[0];
		}
		length = Math.min(length, (int) Math.round(Math.ceil(maxElementsRemaining(1) * (8.0 / bitcount))));
		if(length <= 0)
		{
			return new byte[0];
		}
		byte[] array = new byte[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getByteMostSignificantBits(bitcount);
		}
		return array;
	}
	
	/**
	 * Gets an integer array, will only retrieve a certain amount of bits to form every integer.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public int[] getIntegerArrayLeastSignificantBits(int bitcount)
	{
		int length = getUinteger();
		if(bitcount <= 0)
		{
			return new int[0];
		}
		length = Math.min(length, (int) Math.round(Math.ceil(maxElementsRemaining(1) * (8.0 / bitcount))));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getIntegerLeastSignificantBits(bitcount);
		}
		return array;
	}
	
	/**
	 * Gets an integer array, will only retrieve a certain amount of bits to form every integer.<br>
	 * <br>
	 * The bitcount can be anything between 0 and 32, values higher or lower will be clamped to 0 to 32.
	 */
	public int[] getIntegerArrayMostSignificantBits(int bitcount)
	{
		int length = getUinteger();
		if(bitcount <= 0)
		{
			return new int[0];
		}
		length = Math.min(length, (int) Math.round(Math.ceil(maxElementsRemaining(1) * (8.0 / bitcount))));
		if(length <= 0)
		{
			return new int[0];
		}
		int[] array = new int[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getIntegerMostSignificantBits(bitcount);
		}
		return array;
	}
	
	/**
	 * Gets a byte array.
	 */
	public byte[] getByteArray()
	{
		int length = getUinteger();
		length = Math.min(length, maxElementsRemaining(1));
		if(length <= 0)
		{
			return new byte[0];
		}
		if(currentBytePosition == 0)
		{
			int pos = getAndIncreasePosition(length);
			return getByteArrayImplementation(pos, length);
		}
		byte[] array = new byte[length];
		for(int i = 0; i < length; i++)
		{
			array[i] = getRawByte();
		}
		return array;
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
	 * Gets a boolean array, this does the same as getting a bit array.
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
		for(int i = 0; i < length; i++)
		{
			array[i] = getBoolean();
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
