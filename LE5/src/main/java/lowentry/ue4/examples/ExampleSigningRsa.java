package lowentry.ue4.examples;


import lowentry.ue4.classes.RsaKeys;
import lowentry.ue4.classes.RsaPublicKey;
import lowentry.ue4.library.LowEntry;


public class ExampleSigningRsa
{
	public static void main(String[] args) throws Throwable
	{
		RsaKeys keysA = LowEntry.generateRsaKeys(1024);
		
		
		{// A signs a document, B verifies it >>
			
			// both sides have access to the document's data
			byte[] documentBytes = LowEntry.stringToBytesUtf8("the document's data");
			
			// A signs the document
			byte[] signature = LowEntry.signRsa(LowEntry.sha256(documentBytes), keysA.privateKey);
			
			// A sends B the public key (along with the signature)
			byte[] publicKeyBytesA = LowEntry.rsaPublicKeyToBytes(keysA.publicKey);
			
			// B receives the public key (along with the signature)
			RsaPublicKey publicKeyA = LowEntry.bytesToRsaPublicKey(publicKeyBytesA);
			
			// B verifies the signature
			System.out.println(LowEntry.verifySignatureRsa(signature, LowEntry.sha256(documentBytes), publicKeyA));
			
		}// A signs a document, B verifies it >>
	}
}
