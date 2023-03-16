package lowentry.ue4.examples;


import lowentry.ue4.classes.AesKey;
import lowentry.ue4.library.LowEntry;


public class ExampleEncryptionAes
{
	public static void main(String[] args) throws Throwable
	{
		byte[] key = LowEntry.stringToBytesUtf8("key");
		System.out.println("key: " + LowEntry.bytesToHex(key));
		
		byte[] data = LowEntry.stringToBytesUtf8("very top secret data");
		System.out.println("data: (" + data.length + ") " + LowEntry.bytesToHex(data));
		
		
		System.out.println();
		
		
		// simple encryption and decryption
		
		byte[] encrypted = LowEntry.encryptAes(data, key);
		System.out.println("encrypted: (" + encrypted.length + ") " + LowEntry.bytesToHex(encrypted));
		
		byte[] decrypted = LowEntry.decryptAes(encrypted, key);
		System.out.println("decrypted: (" + decrypted.length + ") " + LowEntry.bytesToHex(decrypted));
		
		System.out.println("same: " + LowEntry.areBytesEqual(data, decrypted));
		
		
		System.out.println();
		
		
		// for better performance at the cost of increased memory usage, you can encrypt and decrypt with an AesKey
		
		AesKey key2 = LowEntry.createAesKey(key);
		
		byte[] encrypted2 = LowEntry.encryptAes(data, key2);
		System.out.println("encrypted2: (" + encrypted2.length + ") " + LowEntry.bytesToHex(encrypted2));
		
		byte[] decrypted2 = LowEntry.decryptAes(encrypted2, key2);
		System.out.println("decrypted2: (" + decrypted2.length + ") " + LowEntry.bytesToHex(decrypted2));
		
		System.out.println("same2: " + LowEntry.areBytesEqual(data, decrypted2));
		
		
		System.out.println();
		
		
		// you can also add a hash to the encrypted data, this will validate that the data (most likely) hasn't changed
		// if the data doesn't match the hash, decrypt will return an empty byte array
		
		byte[] encrypted3 = LowEntry.encryptAes(data, key, true);
		System.out.println("encrypted3: (" + encrypted3.length + ") " + LowEntry.bytesToHex(encrypted3));
		
		byte[] decrypted3 = LowEntry.decryptAes(encrypted3, key, true);
		System.out.println("decrypted3: (" + decrypted3.length + ") " + LowEntry.bytesToHex(decrypted3));
		
		System.out.println("same3: " + LowEntry.areBytesEqual(data, decrypted3));
	}
}
