package lowentry.ue4.examples;


import lowentry.ue4.classes.RsaKeys;
import lowentry.ue4.classes.RsaPublicKey;
import lowentry.ue4.library.LowEntry;


public class ExampleEncryptionRsa
{
	public static void main(String[] args) throws Throwable
	{
		RsaKeys keysA = LowEntry.generateRsaKeys(1024);
		RsaKeys keysB = LowEntry.generateRsaKeys(1024);
		
		
		{// A sends something to B >>
			
			// B sends the public key
			byte[] publicKeyBytesB = LowEntry.rsaPublicKeyToBytes(keysB.publicKey);
			
			// A receives the public key
			RsaPublicKey publicKeyB = LowEntry.bytesToRsaPublicKey(publicKeyBytesB);
			
			// A encrypts data for B
			byte[] dataForB = LowEntry.stringToBytesUtf8("hello B!!");
			byte[] encryptedDataForB = LowEntry.encryptRsa(dataForB, publicKeyB);
			
			// B decrypts the data
			byte[] dataForMe = LowEntry.decryptRsa(encryptedDataForB, keysB.privateKey);
			
			// test if successful
			System.out.println(LowEntry.areBytesEqual(dataForB, dataForMe));
		}// A sends something to B <<
		
		
		{// B sends something to A >>
			
			// A sends the public key
			byte[] publicKeyBytesA = LowEntry.rsaPublicKeyToBytes(keysA.publicKey);
			
			// B receives the public key
			RsaPublicKey publicKeyA = LowEntry.bytesToRsaPublicKey(publicKeyBytesA);
			
			// B encrypts data for A
			byte[] dataForA = LowEntry.stringToBytesUtf8("hello a!!!!1!1");
			byte[] encryptedDataForA = LowEntry.encryptRsa(dataForA, publicKeyA);
			
			// A decrypts the data
			byte[] dataForMe = LowEntry.decryptRsa(encryptedDataForA, keysA.privateKey);
			
			// test if successful
			System.out.println(LowEntry.areBytesEqual(dataForA, dataForMe));
		}// B sends something to A <<
		
		
		System.out.println();
		
		
		{// print out test data for A >>
			
			System.out.println("Keys A:");
			
			byte[] data = LowEntry.stringToBytesUtf8("test 12345");
			System.out.println("data:        " + LowEntry.bytesToHex(data));
			
			byte[] encrypted = LowEntry.encryptRsa(data, keysA.publicKey);
			System.out.println("encrypted:   " + LowEntry.bytesToHex(encrypted));
			
			byte[] decrypted = LowEntry.decryptRsa(encrypted, keysA.privateKey);
			System.out.println("decrypted:   " + LowEntry.bytesToHex(decrypted));
			
			System.out.println("public key:  " + LowEntry.bytesToHex(LowEntry.rsaPublicKeyToBytes(keysA.publicKey)));
			System.out.println("private key: " + LowEntry.bytesToHex(LowEntry.rsaPrivateKeyToBytes(keysA.privateKey)));
			
		}// print out test data for A <<
		
		
		System.out.println();
		
		
		{// print out test data for B >>
			
			System.out.println("Keys B:");
			
			byte[] data = LowEntry.stringToBytesUtf8("test 12345");
			System.out.println("data:        " + LowEntry.bytesToHex(data));
			
			byte[] encrypted = LowEntry.encryptRsa(data, keysB.publicKey);
			System.out.println("encrypted:   " + LowEntry.bytesToHex(encrypted));
			
			byte[] decrypted = LowEntry.decryptRsa(encrypted, keysB.privateKey);
			System.out.println("decrypted:   " + LowEntry.bytesToHex(decrypted));
			
			System.out.println("public key:  " + LowEntry.bytesToHex(LowEntry.rsaPublicKeyToBytes(keysB.publicKey)));
			System.out.println("private key: " + LowEntry.bytesToHex(LowEntry.rsaPrivateKeyToBytes(keysB.privateKey)));
			
		}// print out test data for B <<
	}
}
