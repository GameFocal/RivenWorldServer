package lowentry.ue4.classes;


import lowentry.ue4.classes.internal.rsa.RsaBigInteger;


public class RsaPrivateKey
{
	public final RsaBigInteger n;
	public final RsaBigInteger d;
	
	public final RsaBigInteger p;
	public final RsaBigInteger q;
	public final RsaBigInteger dp;
	public final RsaBigInteger dq;
	public final RsaBigInteger c2;
	
	
	public RsaPrivateKey(RsaBigInteger n, RsaBigInteger d, RsaBigInteger p, RsaBigInteger q)
	{
		this.n = n;
		this.d = d;
		this.p = p;
		this.q = q;
		
		if((p != null) && (q != null))
		{
			RsaBigInteger pm1 = p.subtract(RsaBigInteger.ONE);
			if(pm1 != null)
			{
				RsaBigInteger qm1 = q.subtract(RsaBigInteger.ONE);
				if(qm1 != null)
				{
					RsaBigInteger dp = d.remainder(pm1);
					if(dp != null)
					{
						RsaBigInteger dq = d.remainder(qm1);
						if(dq != null)
						{
							RsaBigInteger c2 = p.modInverse(q);
							if(c2 != null)
							{
								this.dp = dp;
								this.dq = dq;
								this.c2 = c2;
								return;
							}
						}
					}
				}
			}
		}
		
		this.dp = null;
		this.dq = null;
		this.c2 = null;
	}
}
