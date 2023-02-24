package lowentry.ue4.classes.internal;


public final class CompressionLzf
{
	private static final int SKIP_LENGTH = 15;
	
	private static final int HASH_SIZE   = 1 << 16;
	private static final int MAX_LITERAL = 1 << 5;
	private static final int MAX_OFF     = 1 << 13;
	private static final int MAX_REF     = (1 << 8) + (1 << 3);
	
	private static final Object COMPRESS_SYNCHRONIZER = new Object();
	
	private static int[] hashTab;
	
	
	private static int uintByteCount(final int value)
	{
		return ((value <= 127) ? 1 : 4);
	}
	
	private static void uintToBytes(CompressionByteArrayBuffer array, int arrayOffset, int value)
	{
		if(value <= 127)
		{
			array.set(arrayOffset, (byte) (value));
		}
		else
		{
			array.set(arrayOffset, (byte) ((value >> 24) | (1 << 7)));
			array.set(arrayOffset + 1, (byte) (value >> 16));
			array.set(arrayOffset + 2, (byte) (value >> 8));
			array.set(arrayOffset + 3, (byte) (value));
		}
	}
	
	/**
	 * Returns -1 if invalid.
	 */
	private static int bytesToUint(byte[] array, int arrayOffset)
	{
		if((array.length - 1) < arrayOffset)
		{
			return -1;
		}
		byte b = array[arrayOffset];
		if(((b >> 7) & 1) == 0)
		{
			return (b & 0xFF);
		}
		if((array.length - 4) < arrayOffset)
		{
			return -1;
		}
		int value = (((b & 0xFF) & ~(1 << 7)) << 24) | ((array[arrayOffset + 1] & 0xFF) << 16) | ((array[arrayOffset + 2] & 0xFF) << 8) | (array[arrayOffset + 3] & 0xFF);
		if(value <= 127)
		{
			return -1;
		}
		return value;
	}
	
	
	public static byte[] compress(byte[] bytes)
	{
		int inLen = bytes.length;
		if(inLen < SKIP_LENGTH)
		{
			byte[] result = new byte[inLen + 1];
			result[0] = 0;
			System.arraycopy(bytes, 0, result, 1, inLen);
			return result;
		}
		
		CompressionByteArrayBuffer out = new CompressionByteArrayBuffer(inLen + 20, 512);
		out.set(0, (byte) 1);
		uintToBytes(out, 1, inLen);
		int outPos = 2 + uintByteCount(inLen);
		int inPos = 0;
		int literals = 0;
		int future = (bytes[0] << 8) | (bytes[1] & 255);
		
		synchronized(COMPRESS_SYNCHRONIZER)
		{
			if(hashTab == null)
			{
				hashTab = new int[HASH_SIZE];
			}
			while(inPos < (inLen - 4))
			{
				byte p2 = bytes[inPos + 2];
				future = (future << 8) + (p2 & 255);
				int off = ((future * 2777) >> 9) & (HASH_SIZE - 1);
				int ref = hashTab[off];
				hashTab[off] = inPos;
				if((ref < inPos) && (ref > 0) && ((off = inPos - ref - 1) < MAX_OFF) && (bytes[ref + 2] == p2) && (bytes[ref + 1] == (byte) (future >> 8)) && (bytes[ref] == (byte) (future >> 16)))
				{
					int maxLen = inLen - inPos - 2;
					if(maxLen > MAX_REF)
					{
						maxLen = MAX_REF;
					}
					if(literals == 0)
					{
						outPos--;
					}
					else
					{
						out.set(outPos - literals - 1, (byte) (literals - 1));
						literals = 0;
					}
					int len = 3;
					while((len < maxLen) && (bytes[ref + len] == bytes[inPos + len]))
					{
						len++;
					}
					len -= 2;
					if(len < 7)
					{
						out.set(outPos++, (byte) ((off >> 8) + (len << 5)));
					}
					else
					{
						out.set(outPos++, (byte) ((off >> 8) + (7 << 5)));
						out.set(outPos++, (byte) (len - 7));
					}
					out.set(outPos++, (byte) off);
					outPos++;
					inPos += len;
					future = (bytes[inPos] << 8) | (bytes[inPos + 1] & 255);
					future = (future << 8) | (bytes[inPos + 2] & 255);
					hashTab[((future * 2777) >> 9) & (HASH_SIZE - 1)] = inPos++;
					future = (future << 8) | (bytes[inPos + 2] & 255);
					hashTab[((future * 2777) >> 9) & (HASH_SIZE - 1)] = inPos++;
				}
				else
				{
					out.set(outPos++, bytes[inPos++]);
					literals++;
					if(literals == MAX_LITERAL)
					{
						out.set(outPos - literals - 1, (byte) (literals - 1));
						literals = 0;
						outPos++;
					}
				}
			}
		}
		
		while(inPos < inLen)
		{
			out.set(outPos++, bytes[inPos++]);
			literals++;
			if(literals == MAX_LITERAL)
			{
				out.set(outPos - literals - 1, (byte) (literals - 1));
				literals = 0;
				outPos++;
			}
		}
		out.set(outPos - literals - 1, (byte) (literals - 1));
		if(literals == 0)
		{
			outPos--;
		}
		
		if(outPos >= inLen)
		{
			byte[] result = new byte[inLen + 1];
			result[0] = 0;
			System.arraycopy(bytes, 0, result, 1, inLen);
			return result;
		}
		return out.getData(outPos);
	}
	
	
	public static byte[] decompress(byte[] bytes)
	{
		int inLen = bytes.length;
		if(inLen < 2) // bool (1 byte), (optional: size (minimum of 1 byte)), data (minimum of 1 byte)
		{
			return new byte[0];
		}
		if(bytes[0] == 0)
		{
			byte[] result = new byte[inLen - 1];
			System.arraycopy(bytes, 1, result, 0, inLen - 1);
			return result;
		}
		if(bytes[0] != 1)
		{
			return new byte[0];
		}
		
		int outLen = bytesToUint(bytes, 1);
		if(outLen <= 0)
		{
			return new byte[0];
		}
		int inPos = 1 + uintByteCount(outLen);
		CompressionByteArrayBuffer out = new CompressionByteArrayBuffer(outLen, 512);
		int outPos = 0;
		
		do
		{
			if(inPos >= inLen)
			{
				return new byte[0];
			}
			int ctrl = bytes[inPos++] & 255;
			if(ctrl < MAX_LITERAL)
			{
				ctrl++;
				if((inLen - inPos) < ctrl)
				{
					return new byte[0];
				}
				out.set(outPos, bytes, inPos, ctrl);
				outPos += ctrl;
				inPos += ctrl;
			}
			else
			{
				int len = ctrl >> 5;
				if(len == 7)
				{
					if(inPos >= inLen)
					{
						return new byte[0];
					}
					len += bytes[inPos++] & 255;
				}
				len += 2;
				ctrl = -((ctrl & 0x1f) << 8) - 1;
				if(inPos >= inLen)
				{
					return new byte[0];
				}
				ctrl -= bytes[inPos++] & 255;
				ctrl += outPos;
				for(int i = 0; i < len; i++)
				{
					if((outPos < 0) || (outPos >= outLen))
					{
						return new byte[0];
					}
					if((ctrl < 0) || (ctrl >= outLen))
					{
						return new byte[0];
					}
					out.set(outPos++, out.get(ctrl++));
				}
			}
		}
		while(outPos < outLen);
		return out.getData();
	}
}
