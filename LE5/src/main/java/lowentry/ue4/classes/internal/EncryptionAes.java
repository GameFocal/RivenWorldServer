package lowentry.ue4.classes.internal;


import lowentry.ue4.classes.AesKey;
import lowentry.ue4.classes.AesKey1D;
import lowentry.ue4.classes.internal.aes.EncryptionAesFastEngine;
import lowentry.ue4.classes.internal.aes.EncryptionAesFastEngine1D;

import java.util.Arrays;
import java.util.Random;


/**
 * AES CBC implementation, with some modifications:<br>
 * <br>
 * - Added a random 4 byte IV at the start. This way encryptions using the same key and data won't return the same bytes.<br>
 * <br>
 * - The padding has been changed to random bytes, with the last 4 bits (that have a value of 0 to 15) containing the length of the padding. This way incorrect decryptions will return a random amount of bytes, instead of almost always returning a multiple of 16 minus 1. Besides that, this padding can't be analyzed anymore to detect a potential correct decryption.<br>
 * <br>
 * - Added an optional validation hash to determine the data was (probably) not modified.
 */
public class EncryptionAes
{
	private final static Random RANDOM = new Random();
	
	
	private static final int IV_LENGTH   = 4;
	private static final int HASH_LENGTH = 2;
	
	private static final byte[] pearson = new byte[]{(byte) 0x62, (byte) 0x06, (byte) 0x55, (byte) 0x96, (byte) 0x24, (byte) 0x17, (byte) 0x70, (byte) 0xA4, (byte) 0x87, (byte) 0xCF, (byte) 0xA9, (byte) 0x05, (byte) 0x1A, (byte) 0x40, (byte) 0xA5, (byte) 0xDB, (byte) 0x3D, (byte) 0x14, (byte) 0x44, (byte) 0x59, (byte) 0x82, (byte) 0x3F, (byte) 0x34, (byte) 0x66, (byte) 0x18, (byte) 0xE5, (byte) 0x84, (byte) 0xF5, (byte) 0x50, (byte) 0xD8, (byte) 0xC3, (byte) 0x73, (byte) 0x5A, (byte) 0xA8, (byte) 0x9C, (byte) 0xCB, (byte) 0xB1, (byte) 0x78, (byte) 0x02, (byte) 0xBE, (byte) 0xBC, (byte) 0x07, (byte) 0x64, (byte) 0xB9, (byte) 0xAE, (byte) 0xF3, (byte) 0xA2, (byte) 0x0A, (byte) 0xED, (byte) 0x12, (byte) 0xFD, (byte) 0xE1, (byte) 0x08, (byte) 0xD0, (byte) 0xAC, (byte) 0xF4, (byte) 0xFF, (byte) 0x7E, (byte) 0x65, (byte) 0x4F, (byte) 0x91, (byte) 0xEB, (byte) 0xE4, (byte) 0x79, (byte) 0x7B, (byte) 0xFB, (byte) 0x43, (byte) 0xFA, (byte) 0xA1, (byte) 0x00, (byte) 0x6B, (byte) 0x61, (byte) 0xF1, (byte) 0x6F, (byte) 0xB5, (byte) 0x52, (byte) 0xF9, (byte) 0x21, (byte) 0x45, (byte) 0x37, (byte) 0x3B, (byte) 0x99, (byte) 0x1D, (byte) 0x09, (byte) 0xD5, (byte) 0xA7, (byte) 0x54, (byte) 0x5D, (byte) 0x1E, (byte) 0x2E, (byte) 0x5E, (byte) 0x4B, (byte) 0x97, (byte) 0x72, (byte) 0x49, (byte) 0xDE, (byte) 0xC5, (byte) 0x60, (byte) 0xD2, (byte) 0x2D, (byte) 0x10, (byte) 0xE3, (byte) 0xF8, (byte) 0xCA, (byte) 0x33, (byte) 0x98, (byte) 0xFC, (byte) 0x7D, (byte) 0x51, (byte) 0xCE, (byte) 0xD7, (byte) 0xBA, (byte) 0x27, (byte) 0x9E, (byte) 0xB2, (byte) 0xBB, (byte) 0x83, (byte) 0x88, (byte) 0x01, (byte) 0x31, (byte) 0x32, (byte) 0x11, (byte) 0x8D, (byte) 0x5B, (byte) 0x2F, (byte) 0x81, (byte) 0x3C, (byte) 0x63, (byte) 0x9A, (byte) 0x23, (byte) 0x56, (byte) 0xAB, (byte) 0x69, (byte) 0x22, (byte) 0x26, (byte) 0xC8, (byte) 0x93, (byte) 0x3A, (byte) 0x4D, (byte) 0x76, (byte) 0xAD, (byte) 0xF6, (byte) 0x4C, (byte) 0xFE, (byte) 0x85, (byte) 0xE8, (byte) 0xC4, (byte) 0x90, (byte) 0xC6, (byte) 0x7C, (byte) 0x35, (byte) 0x04, (byte) 0x6C, (byte) 0x4A, (byte) 0xDF, (byte) 0xEA, (byte) 0x86, (byte) 0xE6, (byte) 0x9D, (byte) 0x8B, (byte) 0xBD, (byte) 0xCD, (byte) 0xC7, (byte) 0x80, (byte) 0xB0, (byte) 0x13, (byte) 0xD3, (byte) 0xEC, (byte) 0x7F, (byte) 0xC0, (byte) 0xE7, (byte) 0x46, (byte) 0xE9, (byte) 0x58, (byte) 0x92, (byte) 0x2C, (byte) 0xB7, (byte) 0xC9, (byte) 0x16, (byte) 0x53, (byte) 0x0D, (byte) 0xD6, (byte) 0x74, (byte) 0x6D, (byte) 0x9F, (byte) 0x20, (byte) 0x5F, (byte) 0xE2, (byte) 0x8C, (byte) 0xDC, (byte) 0x39, (byte) 0x0C, (byte) 0xDD, (byte) 0x1F, (byte) 0xD1, (byte) 0xB6, (byte) 0x8F, (byte) 0x5C, (byte) 0x95, (byte) 0xB8, (byte) 0x94, (byte) 0x3E, (byte) 0x71, (byte) 0x41, (byte) 0x25, (byte) 0x1B, (byte) 0x6A, (byte) 0xA6, (byte) 0x03, (byte) 0x0E, (byte) 0xCC, (byte) 0x48, (byte) 0x15, (byte) 0x29, (byte) 0x38, (byte) 0x42, (byte) 0x1C, (byte) 0xC1, (byte) 0x28, (byte) 0xD9, (byte) 0x19, (byte) 0x36, (byte) 0xB3, (byte) 0x75, (byte) 0xEE, (byte) 0x57, (byte) 0xF0, (byte) 0x9B, (byte) 0xB4, (byte) 0xAA, (byte) 0xF2, (byte) 0xD4, (byte) 0xBF, (byte) 0xA3, (byte) 0x4E, (byte) 0xDA, (byte) 0x89, (byte) 0xC2, (byte) 0xAF, (byte) 0x6E, (byte) 0x2B, (byte) 0x77, (byte) 0xE0, (byte) 0x47, (byte) 0x7A, (byte) 0x8E, (byte) 0x2A, (byte) 0xA0, (byte) 0x68, (byte) 0x30, (byte) 0xF7, (byte) 0x67, (byte) 0x0F, (byte) 0x0B, (byte) 0x8A, (byte) 0xEF};
	
	
	private static byte[] generateValidationHash(final byte[] data)
	{
		if(data.length <= 0)
		{
			return new byte[0];
		}
		byte[] hh = new byte[HASH_LENGTH];
		for(int j = 0; j < hh.length; j++)
		{
			byte h = pearson[(data[0] + j) & 0xff];
			for(int i = 1; i < data.length; i++)
			{
				h = pearson[(h ^ data[i & 0xff]) & 0xff];
			}
			hh[j] = h;
		}
		return hh;
	}
	
	private static byte[] deletePadding(final byte[] input, final boolean addedValidationHash)
	{
		byte paddingLength = (byte) (input[input.length - 1] & 0x0f);
		int newSize = input.length - paddingLength - 1;
		if(addedValidationHash)
		{
			newSize -= HASH_LENGTH;
		}
		if(newSize <= 0)
		{
			return new byte[0];
		}
		
		if(addedValidationHash)
		{
			byte[] output = new byte[newSize];
			System.arraycopy(input, HASH_LENGTH, output, 0, newSize);
			return output;
		}
		else
		{
			return Arrays.copyOf(input, newSize);
		}
	}
	
	private static byte[] expandIv(final byte[] bytes)
	{
		byte[] expanded = new byte[16];
		
		if((IV_LENGTH <= 0) || (bytes.length < IV_LENGTH))
		{
			return expanded;
		}
		
		int ivi = 0;
		int expi = 0;
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				expanded[expi] = bytes[ivi];
				expi++;
				ivi++;
				if(ivi == IV_LENGTH)
				{
					ivi = 0;
				}
			}
			ivi--;
			if(ivi == -1)
			{
				ivi += IV_LENGTH;
			}
		}
		return expanded;
	}
	
	private static byte[] randomBytes(final int length)
	{
		byte[] bytes = new byte[length];
		RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	private static boolean areBytesEqual(final byte[] a, final byte[] b)
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
	
	private static byte[] fixKey(final byte[] key)
	{
		if(key == null)
		{
			return null;
		}
		int keyLen = key.length;
		if(keyLen == 0)
		{
			return new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		}
		if((keyLen == 16) || (keyLen == 24) || (keyLen == 32))
		{
			return key;
		}
		
		int newSize;
		if(keyLen <= 16)
		{
			newSize = 16;
		}
		else if(keyLen <= 24)
		{
			newSize = 24;
		}
		else
		{
			newSize = 32;
		}
		
		byte[] fixedKey = new byte[newSize];
		int keyi = 0;
		for(int i = 0; i < newSize; i++)
		{
			fixedKey[i] = key[keyi];
			
			keyi++;
			if(keyi >= keyLen)
			{
				keyi = 0;
			}
		}
		return fixedKey;
	}
	
	
	public static byte[] encrypt(final byte[] in, final AesKey key, final boolean addValidationHash)
	{
		if((in == null) || (key == null) || (key.rounds <= 0) || (key.encryptionW == null))
		{
			return new byte[0];
		}
		
		byte[] iv = randomBytes(IV_LENGTH);
		
		int hashLength = (addValidationHash ? HASH_LENGTH : 0);
		
		int length = 16 - ((in.length + hashLength) % 16);
		byte[] padding = randomBytes(length);
		padding[padding.length - 1] = (byte) ((length - 1) | (padding[padding.length - 1] << 4));
		
		byte[] tmp = new byte[IV_LENGTH + in.length + hashLength + length];
		byte[] bloc = new byte[16];
		
		System.arraycopy(iv, 0, tmp, 0, IV_LENGTH);
		
		if(addValidationHash)
		{
			byte[] hash = generateValidationHash(in);
			System.arraycopy(hash, 0, bloc, 0, HASH_LENGTH);
		}
		
		EncryptionAesFastEngine engine = new EncryptionAesFastEngine(key);
		engine.loadIv(expandIv(iv));
		
		int i;
		int padi = 0;
		int im16 = (hashLength % 16);
		for(i = hashLength; i < (in.length + length + hashLength); i++)
		{
			if((i > 0) && (im16 == 0))
			{
				engine.processBlockEncryption(bloc, 0, tmp, ((i - 16) + IV_LENGTH));
			}
			if((i - hashLength) < in.length)
			{
				bloc[im16] = in[i - hashLength];
			}
			else
			{
				bloc[im16] = padding[padi];
				padi++;
			}
			
			im16++;
			if(im16 == 16)
			{
				im16 = 0;
			}
		}
		engine.processBlockEncryption(bloc, 0, tmp, ((i - 16) + IV_LENGTH));
		return tmp;
	}
	
	public static byte[] decrypt(final byte[] in, final AesKey key, final boolean addedValidationHash)
	{
		if((in == null) || (key == null) || (key.rounds <= 0) || (key.decryptionW == null))
		{
			return new byte[0];
		}
		if((in.length - IV_LENGTH) < 16)
		{
			return new byte[0];
		}
		if(((in.length - IV_LENGTH) % 16) != 0)
		{
			return new byte[0];
		}
		
		byte[] tmp = new byte[in.length - IV_LENGTH];
		byte[] bloc = new byte[16];
		
		EncryptionAesFastEngine engine = new EncryptionAesFastEngine(key);
		engine.loadIv(expandIv(in));
		
		int i;
		int im16 = 0;
		for(i = 0; i < tmp.length; i++)
		{
			if((i > 0) && (im16 == 0))
			{
				engine.processBlockDecryption(bloc, 0, tmp, (i - 16));
			}
			//if(i < tmp.length)
			{
				bloc[im16] = in[i + IV_LENGTH];
			}
			
			im16++;
			if(im16 == 16)
			{
				im16 = 0;
			}
		}
		engine.processBlockDecryption(bloc, 0, tmp, (i - 16));
		
		if(addedValidationHash)
		{
			byte[] hash = new byte[HASH_LENGTH];
			System.arraycopy(tmp, 0, hash, 0, HASH_LENGTH);
			
			tmp = deletePadding(tmp, true);
			
			byte[] generatedHash = generateValidationHash(tmp);
			if(!areBytesEqual(hash, generatedHash))
			{
				return new byte[0];
			}
			return tmp;
		}
		
		return deletePadding(tmp, false);
	}
	
	
	private static byte[] encrypt1D(final byte[] in, final AesKey1D key, final boolean addValidationHash)
	{
		if((in == null) || (key == null) || (key.rounds <= 0) || (key.encryptionW == null))
		{
			return new byte[0];
		}
		
		byte[] iv = randomBytes(IV_LENGTH);
		
		int hashLength = (addValidationHash ? HASH_LENGTH : 0);
		
		int length = 16 - ((in.length + hashLength) % 16);
		byte[] padding = randomBytes(length);
		padding[padding.length - 1] = (byte) ((length - 1) | (padding[padding.length - 1] << 4));
		
		byte[] tmp = new byte[IV_LENGTH + in.length + hashLength + length];
		byte[] bloc = new byte[16];
		
		System.arraycopy(iv, 0, tmp, 0, IV_LENGTH);
		
		if(addValidationHash)
		{
			byte[] hash = generateValidationHash(in);
			System.arraycopy(hash, 0, bloc, 0, HASH_LENGTH);
		}
		
		EncryptionAesFastEngine1D engine = new EncryptionAesFastEngine1D(key);
		engine.loadIv(expandIv(iv));
		
		int i;
		int padi = 0;
		int im16 = (hashLength % 16);
		for(i = hashLength; i < (in.length + length + hashLength); i++)
		{
			if((i > 0) && (im16 == 0))
			{
				engine.processBlockEncryption(bloc, 0, tmp, ((i - 16) + IV_LENGTH));
			}
			if((i - hashLength) < in.length)
			{
				bloc[im16] = in[i - hashLength];
			}
			else
			{
				bloc[im16] = padding[padi];
				padi++;
			}
			
			im16++;
			if(im16 == 16)
			{
				im16 = 0;
			}
		}
		engine.processBlockEncryption(bloc, 0, tmp, ((i - 16) + IV_LENGTH));
		return tmp;
	}
	
	private static byte[] decrypt1D(final byte[] in, final AesKey1D key, final boolean addedValidationHash)
	{
		if((in == null) || (key == null) || (key.rounds <= 0) || (key.decryptionW == null))
		{
			return new byte[0];
		}
		if((in.length - IV_LENGTH) < 16)
		{
			return new byte[0];
		}
		if(((in.length - IV_LENGTH) % 16) != 0)
		{
			return new byte[0];
		}
		
		byte[] tmp = new byte[in.length - IV_LENGTH];
		byte[] bloc = new byte[16];
		
		EncryptionAesFastEngine1D engine = new EncryptionAesFastEngine1D(key);
		engine.loadIv(expandIv(in));
		
		int i;
		int im16 = 0;
		for(i = 0; i < tmp.length; i++)
		{
			if((i > 0) && (im16 == 0))
			{
				engine.processBlockDecryption(bloc, 0, tmp, (i - 16));
			}
			//if(i < tmp.length)
			{
				bloc[im16] = in[i + IV_LENGTH];
			}
			
			im16++;
			if(im16 == 16)
			{
				im16 = 0;
			}
		}
		engine.processBlockDecryption(bloc, 0, tmp, (i - 16));
		
		if(addedValidationHash)
		{
			byte[] hash = new byte[HASH_LENGTH];
			System.arraycopy(tmp, 0, hash, 0, HASH_LENGTH);
			
			tmp = deletePadding(tmp, true);
			
			byte[] generatedHash = generateValidationHash(tmp);
			if(!areBytesEqual(hash, generatedHash))
			{
				return new byte[0];
			}
			return tmp;
		}
		
		return deletePadding(tmp, false);
	}
	
	
	public static AesKey createKey(final byte[] key)
	{
		return EncryptionAesFastEngine.generateAesKey(fixKey(key));
	}
	
	private static AesKey createKeyCustom(final byte[] key, final boolean forEncryption, final boolean forDecryption)
	{
		return EncryptionAesFastEngine.generateAesKeyCustom(fixKey(key), forEncryption, forDecryption);
	}
	private static AesKey1D createKey1DCustom(final byte[] key, final boolean forEncryption, final boolean forDecryption)
	{
		return EncryptionAesFastEngine1D.generateAesKeyCustom(fixKey(key), forEncryption, forDecryption);
	}
	
	
	public static byte[] encryptBytes(final byte[] data, final byte[] key, final boolean addValidationHash)
	{
		if(data == null)
		{
			return new byte[0];
		}
		if(data.length <= 2400)
		{
			return encrypt1D(data, createKey1DCustom(key, true, false), addValidationHash);
		}
		else
		{
			return encrypt(data, createKeyCustom(key, true, false), addValidationHash);
		}
	}
	public static byte[] decryptBytes(final byte[] data, final byte[] key, final boolean addedValidationHash)
	{
		if(data == null)
		{
			return new byte[0];
		}
		if(data.length <= 650)
		{
			return decrypt1D(data, createKey1DCustom(key, false, true), addedValidationHash);
		}
		else
		{
			return decrypt(data, createKeyCustom(key, false, true), addedValidationHash);
		}
	}
}
