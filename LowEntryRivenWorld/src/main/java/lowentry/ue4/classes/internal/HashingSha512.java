package lowentry.ue4.classes.internal;


/**
 * This class returns the same result as the build-in SHA-512 does.<br>
 * <br>
 * This class can be used as an example of how to implement SHA-512 in other languages.
 */
public class HashingSha512
{
	private static final long[] k          = new long[]{0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL, 0x3956c25bf348b538L, 0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L, 0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L, 0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L, 0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L, 0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL, 0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL, 0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L, 0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L, 0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL, 0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL, 0xca273eceea26619cL, 0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL, 0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L};
	private static final int    BLOCK_SIZE = 128;
	
	
	private long[] w = null;
	
	private long h0 = 0x6a09e667f3bcc908L;
	private long h1 = 0xbb67ae8584caa73bL;
	private long h2 = 0x3c6ef372fe94f82bL;
	private long h3 = 0xa54ff53a5f1d36f1L;
	private long h4 = 0x510e527fade682d1L;
	private long h5 = 0x9b05688c2b3e6c1fL;
	private long h6 = 0x1f83d9abfb41bd6bL;
	private long h7 = 0x5be0cd19137e2179L;
	
	private int    count  = 0;
	private byte[] buffer = null;
	
	
	private void initialize()
	{
		w = new long[80];
		buffer = new byte[128];
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
		int padding = (n < 112) ? (112 - n) : (240 - n);
		byte[] result = new byte[padding + 16];
		//Arrays.fill(result, (byte) 0);
		result[0] = (byte) 0x80;
		long bits = count << 3;
		padding += 8;
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
		return new byte[]{(byte) (h0 >> 56), (byte) (h0 >> 48), (byte) (h0 >> 40), (byte) (h0 >> 32), (byte) (h0 >> 24), (byte) (h0 >> 16), (byte) (h0 >> 8), (byte) h0, (byte) (h1 >> 56), (byte) (h1 >> 48), (byte) (h1 >> 40), (byte) (h1 >> 32), (byte) (h1 >> 24), (byte) (h1 >> 16), (byte) (h1 >> 8), (byte) h1, (byte) (h2 >> 56), (byte) (h2 >> 48), (byte) (h2 >> 40), (byte) (h2 >> 32), (byte) (h2 >> 24), (byte) (h2 >> 16), (byte) (h2 >> 8), (byte) h2, (byte) (h3 >> 56), (byte) (h3 >> 48), (byte) (h3 >> 40), (byte) (h3 >> 32), (byte) (h3 >> 24), (byte) (h3 >> 16), (byte) (h3 >> 8), (byte) h3, (byte) (h4 >> 56), (byte) (h4 >> 48), (byte) (h4 >> 40), (byte) (h4 >> 32), (byte) (h4 >> 24), (byte) (h4 >> 16), (byte) (h4 >> 8), (byte) h4, (byte) (h5 >> 56), (byte) (h5 >> 48), (byte) (h5 >> 40), (byte) (h5 >> 32), (byte) (h5 >> 24), (byte) (h5 >> 16), (byte) (h5 >> 8), (byte) h5, (byte) (h6 >> 56), (byte) (h6 >> 48), (byte) (h6 >> 40), (byte) (h6 >> 32), (byte) (h6 >> 24), (byte) (h6 >> 16), (byte) (h6 >> 8), (byte) h6, (byte) (h7 >> 56), (byte) (h7 >> 48), (byte) (h7 >> 40), (byte) (h7 >> 32), (byte) (h7 >> 24), (byte) (h7 >> 16), (byte) (h7 >> 8), (byte) h7};
	}
	
	private void sha(byte[] in, int offset)
	{
		long A = h0;
		long B = h1;
		long C = h2;
		long D = h3;
		long E = h4;
		long F = h5;
		long G = h6;
		long H = h7;
		long T;
		long T2;
		int r;
		for(r = 0; r < 16; r++)
		{
			w[r] = ((long) in[offset] << 56) | (((long) in[offset + 1] & 0xFF) << 48) | (((long) in[offset + 2] & 0xFF) << 40) | (((long) in[offset + 3] & 0xFF) << 32) | (((long) in[offset + 4] & 0xFF) << 24) | (((long) in[offset + 5] & 0xFF) << 16) | (((long) in[offset + 6] & 0xFF) << 8) | ((long) in[offset + 7] & 0xFF);
			offset += 8;
		}
		for(r = 16; r < 80; r++)
		{
			T = w[r - 2];
			T2 = w[r - 15];
			w[r] = ((s(T, 19) | (T << 45)) ^ (s(T, 61) | (T << 3)) ^ s(T, 6)) + w[r - 7] + ((s(T2, 1) | (T2 << 63)) ^ (s(T2, 8) | (T2 << 56)) ^ s(T2, 7)) + w[r - 16];
		}
		for(r = 0; r < 80; r++)
		{
			T = H + ((s(E, 14) | (E << 50)) ^ (s(E, 18) | (E << 46)) ^ (s(E, 41) | (E << 23))) + ((E & F) ^ ((~E) & G)) + k[r] + w[r];
			T2 = ((s(A, 28) | (A << 36)) ^ (s(A, 34) | (A << 30)) ^ (s(A, 39) | (A << 25))) + ((A & B) ^ (A & C) ^ (B & C));
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
	
	private static long s(long a, int b)
	{
		return (a >>> b);
	}
	
	
	public static byte[] hash(byte[] bytes)
	{
		HashingSha512 instance = new HashingSha512();
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
			HashingSha512 instance = new HashingSha512();
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
			HashingSha512 instance = new HashingSha512();
			instance.initialize();
			return instance.digest();
		}
		
		HashingSha512 instance = new HashingSha512();
		instance.initialize();
		instance.update(bytes, index, length);
		return instance.digest();
	}
}
