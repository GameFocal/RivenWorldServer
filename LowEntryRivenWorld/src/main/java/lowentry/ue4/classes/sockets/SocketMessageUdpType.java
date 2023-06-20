package lowentry.ue4.classes.sockets;


public class SocketMessageUdpType
{
	public static final byte HANDSHAKE = 1;
	public static final byte MESSAGE   = 2;
	public static final byte PING      = 3;
	public static final byte PONG      = 4;
	
	public static final byte[] PING_BYTES = new byte[]{PING};
	public static final byte[] PONG_BYTES = new byte[]{PONG};
	
	
	public static String format(final byte type)
	{
		String text = "UNKNOWN_TYPE";
		switch(type)
		{
			case HANDSHAKE:
				text = "HANDSHAKE";
				break;
			case MESSAGE:
				text = "MESSAGE";
				break;
			case PING:
				text = "PING";
				break;
			case PONG:
				text = "PONG";
				break;
		}
		return type + " (" + text + ")";
	}
}
