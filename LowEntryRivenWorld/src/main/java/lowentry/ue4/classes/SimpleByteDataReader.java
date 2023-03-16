package lowentry.ue4.classes;


/**
 * This class is NOT thread safe!<br>
 * <br>
 * Works the same as the {@link ByteDataReader} class, except that the Simple class no longer compresses things down with bit-wise code. The code is a lot simpler and easier to implement, at the cost of the network packages being slightly larger.
 */
public abstract class SimpleByteDataReader extends ByteDataReader
{
	protected int getUinteger()
	{
		int value = _getUinteger();
		if(value < 0)
		{
			value = 0;
		}
		return value;
	}
	private int _getUinteger()
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
	 * Gets a boolean array.
	 */
	public boolean[] getBooleanArray()
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
			array[i] = getBoolean();
		}
		return array;
	}
}
