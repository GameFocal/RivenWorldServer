package lowentry.ue4.classes;


import lowentry.ue4.classes.internal.rsa.RsaBigInteger;


public class RsaPublicKey
{
	public final RsaBigInteger n;
	public final RsaBigInteger e;
	
	
	public RsaPublicKey(RsaBigInteger n, RsaBigInteger e)
	{
		this.n = n;
		this.e = e;
	}
}
