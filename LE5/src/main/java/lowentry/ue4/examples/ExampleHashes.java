package lowentry.ue4.examples;


import lowentry.ue4.classes.ParsedHashcash;
import lowentry.ue4.library.LowEntry;


public class ExampleHashes
{
	public static void main(String[] args) throws Throwable
	{
		String data = "example:data";
		byte[] bytes = LowEntry.stringToBytesUtf8(data);
		
		System.out.println("data:                " + data);
		System.out.println("bytes:               " + LowEntry.bytesToHex(bytes));
		
		
		System.out.println();
		
		
		byte[] md5 = LowEntry.md5(bytes);
		byte[] sha1 = LowEntry.sha1(bytes);
		byte[] sha256 = LowEntry.sha256(bytes);
		byte[] sha512 = LowEntry.sha512(bytes);
		
		System.out.println("md5:                 " + LowEntry.bytesToHex(md5));
		System.out.println("sha1:                " + LowEntry.bytesToHex(sha1));
		System.out.println("sha256:              " + LowEntry.bytesToHex(sha256));
		System.out.println("sha512:              " + LowEntry.bytesToHex(sha512));
		
		
		System.out.println();
		
		
		byte[] bcryptSalt = LowEntry.generateRandomBytes(16);
		byte[] bcrypt = LowEntry.bcrypt(bytes, bcryptSalt, 10);
		
		System.out.println("bcrypt salt:         " + LowEntry.bytesToHex(bcryptSalt));
		System.out.println("bcrypt:              " + LowEntry.bytesToHex(bcrypt));
		
		
		System.out.println();
		
		
		String hashcash = LowEntry.hashcash(data, 20);
		ParsedHashcash parsedHashcash = LowEntry.parseHashcash(hashcash);
		
		System.out.println("hashcash:            " + hashcash);
		System.out.println("parsed hashcash:     " + parsedHashcash);
	}
}
