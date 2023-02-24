package lowentry.ue4.libs.pyronet.jawnae.pyronet;


public class PyroException extends RuntimeException
{
	private static final long serialVersionUID = -9006868696301737207L;
	
	public PyroException()
	{
		super();
	}
	
	public PyroException(String msg)
	{
		super(msg);
	}
	
	public PyroException(String msg, Exception cause)
	{
		super(msg, cause);
	}
}
