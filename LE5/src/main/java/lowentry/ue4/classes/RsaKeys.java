package lowentry.ue4.classes;


public class RsaKeys
{
	public final RsaPublicKey  publicKey;
	public final RsaPrivateKey privateKey;
	
	
	public RsaKeys(RsaPublicKey publicKey, RsaPrivateKey privateKey)
	{
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
}
