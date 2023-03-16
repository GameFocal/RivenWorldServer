package lowentry.ue4.classes.internal;


import lowentry.ue4.library.LowEntry;

import java.security.SecureRandom;
import java.util.Random;


public class PaddingOaep
{
	private static final Random       RANDOM        = new Random();
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	
	private static byte[] hash(byte[] input)
	{
		return LowEntry.sha256(input);
	}
	
	private static int hashSize()
	{
		return 32;
	}
	
	
	private static void random(byte[] bytes)
	{
		RANDOM.nextBytes(bytes);
	}
	
	private static void randomSecure(byte[] bytes)
	{
		SECURE_RANDOM.nextBytes(bytes);
	}
	
	private static byte random()
	{
		return (byte) RANDOM.nextInt();
	}
	
	
	private static byte[] mfg1(byte[] seed, int seedOffset, int seedLength, int desiredLength)
	{
		int hLen = 32;
		int offset = 0;
		int i = 0;
		byte[] mask = new byte[desiredLength];
		//Arrays.fill(mask, (byte) 0);
		byte[] temp = new byte[seedLength + 4];
		System.arraycopy(seed, seedOffset, temp, 4, seedLength);
		while(offset < desiredLength)
		{
			temp[0] = (byte) (i >> 24);
			temp[1] = (byte) (i >> 16);
			temp[2] = (byte) (i >> 8);
			temp[3] = (byte) i;
			int remaining = desiredLength - offset;
			System.arraycopy(hash(temp), 0, mask, offset, ((remaining < hLen) ? remaining : hLen));
			offset = offset + hLen;
			i = i + 1;
		}
		return mask;
	}
	
	
	public static byte[] pad(byte[] message, int length)
	{
		length -= 1;
		int mLen = message.length;
		int hLen = hashSize();
		if(mLen > (length - hLen - 1))
		{
			mLen = length - hLen - 1;
		}
		if(mLen <= 0)
		{
			return new byte[0];
		}
		byte[] dataBlock = new byte[length - hLen];
		random(dataBlock);
		int padlength = (length - mLen - hLen - 1);
		if(padlength <= 225)
		{
			dataBlock[0] = (byte) padlength;
		}
		else if(padlength <= 65535)
		{
			dataBlock[0] = (byte) ((((random() & 0xff) / 256.0) * 15.0) + 226);
			dataBlock[1] = (byte) (padlength >> 8);
			dataBlock[2] = (byte) (padlength);
		}
		else
		{
			dataBlock[0] = (byte) ((((random() & 0xff) / 256.0) * 15.0) + 241);
			dataBlock[1] = (byte) (padlength >> 24);
			dataBlock[2] = (byte) (padlength >> 16);
			dataBlock[3] = (byte) (padlength >> 8);
			dataBlock[4] = (byte) (padlength);
		}
		System.arraycopy(message, 0, dataBlock, (dataBlock.length - mLen), mLen);
		byte[] seed = new byte[hLen];
		randomSecure(seed);
		byte[] dataBlockMask = mfg1(seed, 0, hLen, dataBlock.length);
		for(int i = 0; i < dataBlock.length; i++)
		{
			dataBlock[i] ^= dataBlockMask[i];
		}
		byte[] seedMask = mfg1(dataBlock, 0, dataBlock.length, hLen);
		for(int i = 0; i < hLen; i++)
		{
			seed[i] ^= seedMask[i];
		}
		byte[] padded = new byte[length + 1];
		byte paddedbyte = 0x00;
		while(paddedbyte == 0x00)
		{
			paddedbyte = (byte) (random() & 0x3f);
		}
		padded[0] = paddedbyte;
		System.arraycopy(seed, 0, padded, 1, hLen);
		System.arraycopy(dataBlock, 0, padded, 1 + hLen, dataBlock.length);
		return padded;
	}
	
	public static byte[] unpad(byte[] message)
	{
		int mLen = message.length - 1;
		int hLen = hashSize();
		if(mLen <= (hLen + 1))
		{
			return new byte[0];
		}
		if((message[0] == 0x00) || ((message[0] & ~0x3f) != 0x00))
		{
			return new byte[0];
		}
		byte[] copy = new byte[mLen];
		System.arraycopy(message, 1, copy, 0, mLen);
		byte[] seedMask = mfg1(copy, hLen, mLen - hLen, hLen);
		for(int i = 0; i < hLen; i++)
		{
			copy[i] ^= seedMask[i];
		}
		byte[] dataBlockMask = mfg1(copy, 0, hLen, mLen - hLen);
		for(int i = hLen; i < mLen; i++)
		{
			copy[i] ^= dataBlockMask[i - hLen];
		}
		int padlength = copy[hLen] & 0xff;
		if(padlength > 225)
		{
			if(padlength <= 240)
			{
				if((hLen + 2) >= copy.length)
				{
					return new byte[0];
				}
				padlength = ((copy[hLen + 1] & 0xFF) << 8) | (copy[hLen + 2] & 0xFF);
				if(padlength <= 225)
				{
					return new byte[0];
				}
			}
			else
			{
				if((hLen + 4) >= copy.length)
				{
					return new byte[0];
				}
				padlength = ((copy[hLen + 1] & 0xFF) << 24) | ((copy[hLen + 2] & 0xFF) << 16) | ((copy[hLen + 3] & 0xFF) << 8) | (copy[hLen + 4] & 0xFF);
				if(padlength <= 65535)
				{
					return new byte[0];
				}
			}
		}
		if(mLen <= (hLen + 1 + padlength))
		{
			return new byte[0];
		}
		byte[] unpadded = new byte[mLen - hLen - 1 - padlength];
		System.arraycopy(copy, (hLen + 1 + padlength), unpadded, 0, (mLen - hLen - 1 - padlength));
		return unpadded;
	}
}
