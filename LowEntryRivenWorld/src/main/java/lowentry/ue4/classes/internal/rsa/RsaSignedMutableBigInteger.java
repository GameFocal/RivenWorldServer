package lowentry.ue4.classes.internal.rsa;


public class RsaSignedMutableBigInteger extends RsaMutableBigInteger
{
	public int sign = 1;
	
	
	public RsaSignedMutableBigInteger()
	{
		super();
	}
	
	public RsaSignedMutableBigInteger(int val)
	{
		super(val);
	}
	
	
	public void signedAdd(RsaSignedMutableBigInteger addend)
	{
		if(addend == null)
		{
			return;
		}
		
		if(sign == addend.sign)
		{
			add(addend);
		}
		else
		{
			sign = sign * subtract(addend);
		}
	}
	
	public void signedAdd(RsaMutableBigInteger addend)
	{
		if(addend == null)
		{
			return;
		}
		
		if(sign == 1)
		{
			add(addend);
		}
		else
		{
			sign = sign * subtract(addend);
		}
	}
	
	public void signedSubtract(RsaSignedMutableBigInteger addend)
	{
		if(addend == null)
		{
			return;
		}
		
		if(sign == addend.sign)
		{
			sign = sign * subtract(addend);
		}
		else
		{
			add(addend);
		}
	}
}
