package lowentry.ue4.classes.internal;


/**
 * This class returns the same result as the build-in SHA-256 does.<br>
 * <br>
 * This class can be used as an example of how to implement SHA-256 in other languages.
 */
public class HashingSha256
{
	private static final int[] k          = new int[]{0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
	private static final int   BLOCK_SIZE = 64;
	
	
	private int[] w = null;
	
	private int h0 = 0x6a09e667;
	private int h1 = 0xbb67ae85;
	private int h2 = 0x3c6ef372;
	private int h3 = 0xa54ff53a;
	private int h4 = 0x510e527f;
	private int h5 = 0x9b05688c;
	private int h6 = 0x1f83d9ab;
	private int h7 = 0x5be0cd19;
	
	private int    count  = 0;
	private byte[] buffer = null;
	
	
	private void initialize()
	{
		w = new int[64];
		buffer = new byte[64];
	}
	
	private void update(byte[] b, int offset, int len)
	{
		int n = count % BLOCK_SIZE;
		count += len;
		int partLen = BLOCK_SIZE - n;
		int i = 0;
		
		if(len >= partLen)
		{
			System.arraycopy(b, offset, buffer, n, partLen);
			sha(buffer, 0);
			for(i = partLen; ((i + BLOCK_SIZE) - 1) < len; i += BLOCK_SIZE)
			{
				sha(b, offset + i);
			}
			n = 0;
		}
		
		if(i < len)
		{
			System.arraycopy(b, offset + i, buffer, n, len - i);
		}
	}
	
	private byte[] digest()
	{
		byte[] tail = padBuffer();
		update(tail, 0, tail.length);
		return getResult();
	}
	
	private byte[] padBuffer()
	{
		int n = count % BLOCK_SIZE;
		int padding = (n < 56) ? (56 - n) : (120 - n);
		byte[] result = new byte[padding + 8];
		//Arrays.fill(result, (byte) 0);
		result[0] = (byte) 0x80;
		long bits = count << 3;
		result[padding] = (byte) (bits >> 56);
		result[padding + 1] = (byte) (bits >> 48);
		result[padding + 2] = (byte) (bits >> 40);
		result[padding + 3] = (byte) (bits >> 32);
		result[padding + 4] = (byte) (bits >> 24);
		result[padding + 5] = (byte) (bits >> 16);
		result[padding + 6] = (byte) (bits >> 8);
		result[padding + 7] = (byte) bits;
		return result;
	}
	
	private byte[] getResult()
	{
		return new byte[]{(byte) (h0 >> 24), (byte) (h0 >> 16), (byte) (h0 >> 8), (byte) h0, (byte) (h1 >> 24), (byte) (h1 >> 16), (byte) (h1 >> 8), (byte) h1, (byte) (h2 >> 24), (byte) (h2 >> 16), (byte) (h2 >> 8), (byte) h2, (byte) (h3 >> 24), (byte) (h3 >> 16), (byte) (h3 >> 8), (byte) h3, (byte) (h4 >> 24), (byte) (h4 >> 16), (byte) (h4 >> 8), (byte) h4, (byte) (h5 >> 24), (byte) (h5 >> 16), (byte) (h5 >> 8), (byte) h5, (byte) (h6 >> 24), (byte) (h6 >> 16), (byte) (h6 >> 8), (byte) h6, (byte) (h7 >> 24), (byte) (h7 >> 16), (byte) (h7 >> 8), (byte) h7};
	}
	
	private void sha(byte[] in, int offset)
	{
		int A = h0;
		int B = h1;
		int C = h2;
		int D = h3;
		int E = h4;
		int F = h5;
		int G = h6;
		int H = h7;
		int T;
		int T2;
		int r;
		for(r = 0; r < 16; r++)
		{
			w[r] = ((in[offset] << 24) | ((in[offset + 1] & 0xFF) << 16) | ((in[offset + 2] & 0xFF) << 8) | (in[offset + 3] & 0xFF));
			offset += 4;
		}
		for(r = 16; r < 64; r++)
		{
			T = w[r - 2];
			T2 = w[r - 15];
			w[r] = (((s(T, 17) | (T << 15)) ^ (s(T, 19) | (T << 13)) ^ s(T, 10)) + w[r - 7] + ((s(T2, 7) | (T2 << 25)) ^ (s(T2, 18) | (T2 << 14)) ^ s(T2, 3)) + w[r - 16]);
		}
		for(r = 0; r < 64; r++)
		{
			T = (H + ((s(E, 6) | (E << 26)) ^ (s(E, 11) | (E << 21)) ^ (s(E, 25) | (E << 7))) + ((E & F) ^ (~E & G)) + k[r] + w[r]);
			T2 = (((s(A, 2) | (A << 30)) ^ (s(A, 13) | (A << 19)) ^ (s(A, 22) | (A << 10))) + ((A & B) ^ (A & C) ^ (B & C)));
			H = G;
			G = F;
			F = E;
			E = D + T;
			D = C;
			C = B;
			B = A;
			A = T + T2;
		}
		h0 += A;
		h1 += B;
		h2 += C;
		h3 += D;
		h4 += E;
		h5 += F;
		h6 += G;
		h7 += H;
	}
	
	private static int s(int a, int b)
	{
		return (a >>> b);
	}
	
	
	public static byte[] hash(byte[] bytes)
	{
		HashingSha256 instance = new HashingSha256();
		instance.initialize();
		if(bytes != null)
		{
			instance.update(bytes, 0, bytes.length);
		}
		return instance.digest();
	}
	
	public static byte[] hash(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			HashingSha256 instance = new HashingSha256();
			instance.initialize();
			return instance.digest();
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
			HashingSha256 instance = new HashingSha256();
			instance.initialize();
			return instance.digest();
		}
		
		HashingSha256 instance = new HashingSha256();
		instance.initialize();
		instance.update(bytes, index, length);
		return instance.digest();
	}
}
