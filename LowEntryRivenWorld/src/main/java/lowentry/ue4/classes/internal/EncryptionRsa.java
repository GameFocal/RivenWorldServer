package lowentry.ue4.classes.internal;


import lowentry.ue4.classes.RsaKeys;
import lowentry.ue4.classes.RsaPrivateKey;
import lowentry.ue4.classes.RsaPublicKey;
import lowentry.ue4.classes.internal.rsa.RsaBigInteger;

import java.security.SecureRandom;


public class EncryptionRsa
{
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	
	public static RsaKeys generateKeys(int bits)
	{
		for(int i = 1; i <= 100; i++)
		{
			RsaBigInteger p = RsaBigInteger.probablePrime(bits / 2);
			if(p == null)
			{
				return null;
			}
			
			RsaBigInteger q = RsaBigInteger.probablePrime(bits / 2);
			if(q == null)
			{
				return null;
			}
			
			RsaBigInteger n = p.multiply(q);
			if(n == null)
			{
				return null;
			}
			
			RsaBigInteger pm1 = p.subtract(RsaBigInteger.ONE);
			if(pm1 == null)
			{
				return null;
			}
			
			RsaBigInteger qm1 = q.subtract(RsaBigInteger.ONE);
			if(qm1 == null)
			{
				return null;
			}
			
			RsaBigInteger m = pm1.multiply(qm1);
			if(m == null)
			{
				return null;
			}
			
			RsaBigInteger e = RsaBigInteger.probablePrime(bits / 4);
			if(e == null)
			{
				return null;
			}
			
			while(true)
			{
				RsaBigInteger mgcde = m.gcd(e);
				if(mgcde == null)
				{
					return null;
				}
				
				if(mgcde.compareTo(RsaBigInteger.ONE) <= 0)
				{
					break;
				}
				if(e.compareTo(m) >= 0)
				{
					break;
				}
				
				e = e.add(RsaBigInteger.ONE);
				if(e == null)
				{
					return null;
				}
			}
			
			RsaBigInteger d = e.modInverse(m);
			if(d == null)
			{
				return null;
			}
			if(d.equals(RsaBigInteger.ZERO))
			{
				continue;
			}
			
			RsaPublicKey publicKey = new RsaPublicKey(n, e);
			RsaPrivateKey privateKey = new RsaPrivateKey(n, d, p, q);
			
			if((privateKey.c2 == null) || privateKey.dp.equals(RsaBigInteger.ZERO) || privateKey.dq.equals(RsaBigInteger.ZERO))
			{
				continue;
			}
			return new RsaKeys(publicKey, privateKey);
		}
		return null;
	}
	
	
	public static byte[] encrypt(byte[] bytes, RsaPublicKey publicKey)
	{
		if((bytes == null) || (publicKey == null) || (publicKey.e == null) || (publicKey.n == null))
		{
			return new byte[0];
		}
		byte[] padded = PaddingOaep.pad(bytes, (publicKey.n.bitLength() + 7) / 8);
		RsaBigInteger integer = new RsaBigInteger(1, padded);
		integer = integer.modPow(publicKey.e, publicKey.n);
		if(integer == null)
		{
			return new byte[0];
		}
		return integer.toByteArray();
	}
	
	public static byte[] decrypt(byte[] bytes, RsaPrivateKey privateKey)
	{
		if((bytes == null) || (privateKey == null) || (privateKey.d == null) || (privateKey.n == null))
		{
			return new byte[0];
		}
		RsaBigInteger integer = new RsaBigInteger(1, bytes);
		if((privateKey.c2 == null) || privateKey.dp.equals(RsaBigInteger.ZERO) || privateKey.dq.equals(RsaBigInteger.ZERO))
		{
			integer = integer.modPow(privateKey.d, privateKey.n);
		}
		else
		{
			RsaBigInteger cdp = integer.modPow(privateKey.dp, privateKey.p);
			if(cdp == null)
			{
				return new byte[0];
			}
			RsaBigInteger cdq = integer.modPow(privateKey.dq, privateKey.q);
			if(cdq == null)
			{
				return new byte[0];
			}
			RsaBigInteger u = cdq.subtract(cdp);
			if(u == null)
			{
				return new byte[0];
			}
			u = u.multiply(privateKey.c2);
			if(u == null)
			{
				return new byte[0];
			}
			u = u.remainder(privateKey.q);
			if(u == null)
			{
				return new byte[0];
			}
			if(u.compareTo(RsaBigInteger.ZERO) < 0)
			{
				u = u.add(privateKey.q);
				if(u == null)
				{
					return new byte[0];
				}
			}
			integer = u.multiply(privateKey.p);
			if(integer == null)
			{
				return new byte[0];
			}
			integer = cdp.add(integer);
		}
		if(integer == null)
		{
			return new byte[0];
		}
		byte[] padded = integer.toByteArray();
		return PaddingOaep.unpad(padded);
	}
	
	
	public static byte[] sign(byte[] hash, RsaPrivateKey privateKey)
	{
		if((hash == null) || (privateKey == null) || (privateKey.d == null) || (privateKey.n == null))
		{
			return new byte[0];
		}
		int lengthN = (privateKey.n.bitLength() + 7) / 8;
		int randomBytesCount = lengthN - (hash.length + 3);
		if(randomBytesCount < (lengthN / 2))
		{
			// the given hash was too large for the given private key
			return new byte[0];
		}
		byte[] randomBytes = new byte[randomBytesCount];
		SECURE_RANDOM.nextBytes(randomBytes);
		byte[] paddedHash = mergeBytes(new byte[]{0, 2}, randomBytes, new byte[]{0}, hash);
		RsaBigInteger signature = (new RsaBigInteger(1, paddedHash)).modPow(privateKey.d, privateKey.n);
		if(signature == null)
		{
			return new byte[0];
		}
		return signature.toByteArray();
	}
	
	public static boolean verifySignature(byte[] signature, byte[] expectedHash, RsaPublicKey publicKey)
	{
		if((signature == null) || (expectedHash == null) || (publicKey == null) || (publicKey.e == null) || (publicKey.n == null))
		{
			return false;
		}
		RsaBigInteger signaturePaddedHash = (new RsaBigInteger(1, signature)).modPow(publicKey.e, publicKey.n);
		if(signaturePaddedHash == null)
		{
			return false;
		}
		byte[] signaturePaddedHashBytes = signaturePaddedHash.toByteArray();
		byte[] signatureHash = bytesSubArray(signaturePaddedHashBytes, signaturePaddedHashBytes.length - expectedHash.length, expectedHash.length);
		return areBytesEqual(signatureHash, expectedHash);
	}
	
	
	public static byte[] publicKeyToBytes(RsaPublicKey publicKey)
	{
		if((publicKey == null) || (publicKey.e == null) || (publicKey.n == null))
		{
			return new byte[0];
		}
		byte[] n = publicKey.n.toByteArray();
		byte[] e = publicKey.e.toByteArray();
		return mergeBytes(uintegerToBytes(n.length), n, e);
	}
	
	public static RsaPublicKey bytesToPublicKey(byte[] bytes)
	{
		if(bytes == null)
		{
			return null;
		}
		int n = bytesToUinteger(bytes);
		if(n <= 0)
		{
			return null;
		}
		int sn = ((n <= 127) ? 1 : 4);
		int e = bytes.length - (sn + n);
		return new RsaPublicKey(new RsaBigInteger(1, bytesSubArray(bytes, sn, n)), new RsaBigInteger(1, bytesSubArray(bytes, sn + n, e)));
	}
	
	public static RsaPublicKey bytesToPublicKey(byte[] bytes, int index, int length)
	{
		return bytesToPublicKey(bytesSubArray(bytes, index, length));
	}
	
	
	public static byte[] privateKeyToBytes(RsaPrivateKey privateKey)
	{
		if((privateKey == null) || (privateKey.d == null) || (privateKey.n == null) || (privateKey.p == null) || (privateKey.q == null))
		{
			return new byte[0];
		}
		byte[] n = privateKey.n.toByteArray();
		byte[] d = privateKey.d.toByteArray();
		byte[] p = privateKey.p.toByteArray();
		byte[] q = privateKey.q.toByteArray();
		return mergeBytes(uintegerToBytes(n.length), n, uintegerToBytes(d.length), d, uintegerToBytes(p.length), p, q);
	}
	
	public static RsaPrivateKey bytesToPrivateKey(byte[] bytes)
	{
		if(bytes == null)
		{
			return null;
		}
		
		int n = bytesToUinteger(bytes);
		if(n <= 0)
		{
			return null;
		}
		int sn = ((n <= 127) ? 1 : 4);
		
		int d = bytesToUinteger(bytes, sn + n);
		if(d <= 0)
		{
			return null;
		}
		int sd = ((d <= 127) ? 1 : 4);
		
		int p = bytesToUinteger(bytes, sn + n + sd + d);
		if(p <= 0)
		{
			return null;
		}
		int sp = ((p <= 127) ? 1 : 4);
		
		int q = bytes.length - (sn + n + sd + d + sp + p);
		
		return new RsaPrivateKey(new RsaBigInteger(1, bytesSubArray(bytes, sn, n)), new RsaBigInteger(1, bytesSubArray(bytes, sn + n + sd, d)), new RsaBigInteger(1, bytesSubArray(bytes, sn + n + sd + d + sp, p)), new RsaBigInteger(1, bytesSubArray(bytes, sn + n + sd + d + sp + p, q)));
	}
	
	public static RsaPrivateKey bytesToPrivateKey(byte[] bytes, int index, int length)
	{
		return bytesToPrivateKey(bytesSubArray(bytes, index, length));
	}
	
	
	private static byte[] uintegerToBytes(int value)
	{
		if(value <= 127)
		{
			return new byte[]{(byte) value};
		}
		else
		{
			return new byte[]{(byte) ((value >> 24) | (1 << 7)), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
		}
	}
	
	private static int bytesToUinteger(byte[] bytes)
	{
		if(bytes.length <= 0)
		{
			return 0;
		}
		byte b = bytes[0];
		if(((b >> 7) & 1) == 0)
		{
			return (b & 0xFF);
		}
		
		if(bytes.length <= 3)
		{
			return 0;
		}
		int value = (((b & 0xFF) & ~(1 << 7)) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
		if(value <= 127)
		{
			return 0;
		}
		return value;
	}
	
	private static int bytesToUinteger(byte[] bytes, int offset)
	{
		if(bytes.length <= offset)
		{
			return 0;
		}
		byte b = bytes[offset];
		if(((b >> 7) & 1) == 0)
		{
			return (b & 0xFF);
		}
		
		if(bytes.length <= (offset + 3))
		{
			return 0;
		}
		int value = (((b & 0xFF) & ~(1 << 7)) << 24) | ((bytes[offset + 1] & 0xFF) << 16) | ((bytes[offset + 2] & 0xFF) << 8) | (bytes[offset + 3] & 0xFF);
		if(value <= 127)
		{
			return 0;
		}
		return value;
	}
	
	private static byte[] bytesSubArray(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return new byte[0];
		}
		
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (bytes.length - index))
		{
			length = bytes.length - index;
		}
		if(length <= 0)
		{
			return new byte[0];
		}
		
		if((index == 0) && (length == bytes.length))
		{
			return bytes;
		}
		byte[] c = new byte[length];
		System.arraycopy(bytes, index, c, 0, c.length);
		return c;
	}
	
	private static byte[] mergeBytes(byte[]... arrays)
	{
		if((arrays == null) || (arrays.length <= 0))
		{
			return new byte[0];
		}
		if(arrays.length == 1)
		{
			byte[] array = arrays[0];
			if(array == null)
			{
				return new byte[0];
			}
			return array;
		}
		int length = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				length += array.length;
			}
		}
		if(length <= 0)
		{
			return new byte[0];
		}
		byte[] merged = new byte[length];
		int index = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				System.arraycopy(array, 0, merged, index, array.length);
				index += array.length;
			}
		}
		return merged;
	}
	
	private static boolean areBytesEqual(byte[] a, byte[] b)
	{
		if((a == null) || (b == null))
		{
			return false;
		}
		if(a == b)
		{
			return true;
		}
		if(a.length != b.length)
		{
			return false;
		}
		
		for(int i = 0; i < a.length; i++)
		{
			if(a[i] != b[i])
			{
				return false;
			}
		}
		return true;
	}
}
