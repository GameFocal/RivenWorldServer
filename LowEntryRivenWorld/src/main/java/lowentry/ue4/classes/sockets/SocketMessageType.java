package lowentry.ue4.classes.sockets;


public class SocketMessageType
{
	public static final byte MESSAGE                        = 1;
	public static final byte FUNCTION_CALL                  = 2;
	public static final byte FUNCTION_CALL_RESPONSE         = 3;
	public static final byte LATENT_FUNCTION_CALL           = 4;
	public static final byte LATENT_FUNCTION_CALL_RESPONSE  = 5;
	public static final byte LATENT_FUNCTION_CALL_CANCELED  = 6;
	public static final byte CONNECTION_VALIDATION          = 7;
	public static final byte CONNECTION_VALIDATION_RESPONSE = 8;
	public static final byte UNRELIABLE_MESSAGE             = 9;
	public static final byte SIMULATED_UNRELIABLE_MESSAGE   = 10;
	
	
	public static String format(final byte type)
	{
		String text = "UNKNOWN_TYPE";
		switch(type)
		{
			case MESSAGE:
				text = "MESSAGE";
				break;
			case FUNCTION_CALL:
				text = "FUNCTION_CALL";
				break;
			case FUNCTION_CALL_RESPONSE:
				text = "FUNCTION_CALL_RESPONSE";
				break;
			case LATENT_FUNCTION_CALL:
				text = "LATENT_FUNCTION_CALL";
				break;
			case LATENT_FUNCTION_CALL_RESPONSE:
				text = "LATENT_FUNCTION_CALL_RESPONSE";
				break;
			case LATENT_FUNCTION_CALL_CANCELED:
				text = "LATENT_FUNCTION_CALL_CANCELED";
				break;
			case CONNECTION_VALIDATION:
				text = "CONNECTION_VALIDATION";
				break;
			case CONNECTION_VALIDATION_RESPONSE:
				text = "CONNECTION_VALIDATION_RESPONSE";
				break;
			case UNRELIABLE_MESSAGE:
				text = "UNRELIABLE_MESSAGE";
				break;
			case SIMULATED_UNRELIABLE_MESSAGE:
				text = "SIMULATED_UNRELIABLE_MESSAGE";
				break;
		}
		return type + " (" + text + ")";
	}
}
