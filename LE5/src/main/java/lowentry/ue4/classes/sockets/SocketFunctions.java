package lowentry.ue4.classes.sockets;


import java.nio.ByteBuffer;


public class SocketFunctions
{
	public static int uintByteCount(final int value)
	{
		return ((value <= 127) ? 1 : 4);
	}
	
	public static void putUint(final ByteBuffer buffer, final int value)
	{
		if(value <= 127)
		{
			buffer.put((byte) (value));
		}
		else
		{
			buffer.put((byte) ((value >> 24) | (1 << 7)));
			buffer.put((byte) (value >> 16));
			buffer.put((byte) (value >> 8));
			buffer.put((byte) (value));
		}
	}
	
	public static void putUint(final byte[] buffer, final int offset, final int value)
	{
		if(value <= 127)
		{
			buffer[offset] = (byte) (value);
		}
		else
		{
			buffer[offset] = (byte) ((value >> 24) | (1 << 7));
			buffer[offset + 1] = (byte) (value >> 16);
			buffer[offset + 2] = (byte) (value >> 8);
			buffer[offset + 3] = (byte) (value);
		}
	}
	
	
	public static int websocketSizeByteCount(final int value)
	{
		if(value <= 125)
		{
			return 1;
		}
		else if(value <= 65535)
		{
			return 3;
		}
		return 9;
	}
	
	public static void putWebsocketSizeBytes(final ByteBuffer buffer, final int value)
	{
		if(value <= 125)
		{
			buffer.put((byte) (value));
		}
		else if(value <= 65535)
		{
			buffer.put((byte) 126);
			buffer.put((byte) (value >> 8));
			buffer.put((byte) (value));
		}
		else
		{
			buffer.put((byte) 127);
			buffer.put((byte) ((long) value >> 56));
			buffer.put((byte) ((long) value >> 48));
			buffer.put((byte) ((long) value >> 40));
			buffer.put((byte) ((long) value >> 32));
			buffer.put((byte) ((long) value >> 24));
			buffer.put((byte) ((long) value >> 16));
			buffer.put((byte) ((long) value >> 8));
			buffer.put((byte) ((long) value));
		}
	}
	
	public static void putWebsocketSizeBytes(final byte[] buffer, final int offset, final int value)
	{
		if(value <= 125)
		{
			buffer[offset] = (byte) (value);
		}
		else if(value <= 65535)
		{
			buffer[offset] = (byte) 126;
			buffer[offset + 1] = (byte) (value >> 8);
			buffer[offset + 2] = (byte) (value);
		}
		else
		{
			buffer[offset] = (byte) 127;
			buffer[offset + 1] = (byte) ((long) value >> 56);
			buffer[offset + 2] = (byte) ((long) value >> 48);
			buffer[offset + 3] = (byte) ((long) value >> 40);
			buffer[offset + 4] = (byte) ((long) value >> 32);
			buffer[offset + 5] = (byte) ((long) value >> 24);
			buffer[offset + 6] = (byte) ((long) value >> 16);
			buffer[offset + 7] = (byte) ((long) value >> 8);
			buffer[offset + 8] = (byte) ((long) value);
		}
	}
}
