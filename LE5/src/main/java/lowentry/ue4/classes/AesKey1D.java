package lowentry.ue4.classes;


public class AesKey1D
{
	public final int   rounds;
	public final int[] encryptionW;
	public final int[] decryptionW;
	
	
	public AesKey1D(int rounds, int[] encryptionW, int[] decryptionW)
	{
		this.rounds = rounds;
		this.encryptionW = encryptionW;
		this.decryptionW = decryptionW;
	}
}
