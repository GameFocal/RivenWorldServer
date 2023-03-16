package lowentry.ue4.classes.internal.aes;


import lowentry.ue4.classes.AesKey1D;


/**
 * Copied from Bouncy Castle.<br>
 * <br>
 * Made several changes to it to improve the performance and to implement CBC.<br>
 * <br>
 * Also made key generation faster (about twice as fast) at the cost of encryption and decryption speed (-10%).<br>
 * <br>
 * This class is optimized for Java.
 */
public class EncryptionAesFastEngine1D
{
	private final int   ROUNDS;
	private final int[] EW;
	private final int[] DW;
	
	public int C0 = 0;
	public int C1 = 0;
	public int C2 = 0;
	public int C3 = 0;
	
	public int C0store1 = 0;
	public int C1store1 = 0;
	public int C2store1 = 0;
	public int C3store1 = 0;
	
	public int C0store2 = 0;
	public int C1store2 = 0;
	public int C2store2 = 0;
	public int C3store2 = 0;
	
	
	public EncryptionAesFastEngine1D(final AesKey1D key)
	{
		ROUNDS = key.rounds;
		EW = key.encryptionW;
		DW = key.decryptionW;
	}
	
	
	// private static int s(final int a, final int b)
	// {
	// 	return (a >>> b);
	// }
	
	
	public static AesKey1D generateAesKey(final byte[] key)
	{
		return generateAesKeyCustom(key, true, true);
	}
	public static AesKey1D generateAesKeyCustom(final byte[] key, final boolean forEncryption, final boolean forDecryption)
	{
		if(key == null)
		{
			return null;
		}
		
		int keyLen = key.length;
		if((keyLen != 16) && ((keyLen != 24) & (keyLen != 32)))
		{
			throw new IllegalArgumentException("Key length not 128/192/256 bits (it is " + keyLen + ", it needs to be either 16 for AES-128, 24 for AES-192, or 32 for AES-256)");
			//return null;
		}
		
		int kc = (keyLen >>> 2);
		int rounds = kc + 6;
		int roundsx4 = rounds * 4;
		
		if(!forEncryption && !forDecryption)
		{
			return new AesKey1D(rounds, null, null);
		}
		
		int[] ew = new int[(rounds + 1) * 4];
		
		switch(kc)
		{
			case 4:
			{
				int t0 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 0);
				ew[0] = t0;
				int t1 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 4);
				ew[1] = t1;
				int t2 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 8);
				ew[2] = t2;
				int t3 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 12);
				ew[3] = t3;
				
				for(int i = 1; i <= 10; i++)
				{
					int ix = i * 4;
					int u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t3, 8)) ^ EncryptionAesFastEngineGeneral.rcon[i - 1];
					t0 ^= u;
					ew[ix] = t0;
					t1 ^= t0;
					ew[ix + 1] = t1;
					t2 ^= t1;
					ew[ix + 2] = t2;
					t3 ^= t2;
					ew[ix + 3] = t3;
				}
				
				break;
			}
			case 6:
			{
				int t0 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 0);
				ew[0] = t0;
				int t1 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 4);
				ew[1] = t1;
				int t2 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 8);
				ew[2] = t2;
				int t3 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 12);
				ew[3] = t3;
				int t4 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 16);
				ew[4] = t4;
				int t5 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 20);
				ew[5] = t5;
				
				int rcon = 1;
				int u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t5, 8)) ^ rcon;
				rcon <<= 1;
				t0 ^= u;
				ew[6] = t0;
				t1 ^= t0;
				ew[7] = t1;
				t2 ^= t1;
				ew[8] = t2;
				t3 ^= t2;
				ew[9] = t3;
				t4 ^= t3;
				ew[10] = t4;
				t5 ^= t4;
				ew[11] = t5;
				
				for(int i = 3; i < 12; i += 3)
				{
					int ix = i * 4;
					u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t5, 8)) ^ rcon;
					rcon <<= 1;
					t0 ^= u;
					ew[ix] = t0;
					t1 ^= t0;
					ew[ix + 1] = t1;
					t2 ^= t1;
					ew[ix + 2] = t2;
					t3 ^= t2;
					ew[ix + 3] = t3;
					t4 ^= t3;
					ew[ix + 4] = t4;
					t5 ^= t4;
					ew[ix + 5] = t5;
					u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t5, 8)) ^ rcon;
					rcon <<= 1;
					t0 ^= u;
					ew[ix + 6] = t0;
					t1 ^= t0;
					ew[ix + 7] = t1;
					t2 ^= t1;
					ew[ix + 8] = t2;
					t3 ^= t2;
					ew[ix + 9] = t3;
					t4 ^= t3;
					ew[ix + 10] = t4;
					t5 ^= t4;
					ew[ix + 11] = t5;
				}
				
				u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t5, 8)) ^ rcon;
				t0 ^= u;
				ew[48] = t0;
				t1 ^= t0;
				ew[49] = t1;
				t2 ^= t1;
				ew[50] = t2;
				t3 ^= t2;
				ew[51] = t3;
				
				break;
			}
			case 8:
			{
				int t0 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 0);
				ew[0] = t0;
				int t1 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 4);
				ew[1] = t1;
				int t2 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 8);
				ew[2] = t2;
				int t3 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 12);
				ew[3] = t3;
				int t4 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 16);
				ew[4] = t4;
				int t5 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 20);
				ew[5] = t5;
				int t6 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 24);
				ew[6] = t6;
				int t7 = EncryptionAesFastEngineGeneral.littleEndianToInt(key, 28);
				ew[7] = t7;
				
				int u;
				int rcon = 1;
				
				for(int i = 2; i < 14; i += 2)
				{
					int ix = i * 4;
					u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t7, 8)) ^ rcon;
					rcon <<= 1;
					t0 ^= u;
					ew[ix] = t0;
					t1 ^= t0;
					ew[ix + 1] = t1;
					t2 ^= t1;
					ew[ix + 2] = t2;
					t3 ^= t2;
					ew[ix + 3] = t3;
					u = EncryptionAesFastEngineGeneral.subWord(t3);
					t4 ^= u;
					ew[ix + 4] = t4;
					t5 ^= t4;
					ew[ix + 5] = t5;
					t6 ^= t5;
					ew[ix + 6] = t6;
					t7 ^= t6;
					ew[ix + 7] = t7;
				}
				
				u = EncryptionAesFastEngineGeneral.subWord(EncryptionAesFastEngineGeneral.shift(t7, 8)) ^ rcon;
				t0 ^= u;
				ew[56] = t0;
				t1 ^= t0;
				ew[57] = t1;
				t2 ^= t1;
				ew[58] = t2;
				t3 ^= t2;
				ew[59] = t3;
				
				break;
			}
			default:
			{
				throw new IllegalStateException("Should never get here");
				//return null;
			}
		}
		
		if(!forDecryption)
		{
			return new AesKey1D(rounds, ew, null);
		}
		else
		{
			if(!forEncryption)
			{
				//int[] dw = ew;
				//ew = null;
				
				for(int i = 4; i < roundsx4; i++)
				{
					ew[i] = EncryptionAesFastEngineGeneral.inv_mcol(ew[i]);
				}
				
				return new AesKey1D(rounds, null, ew);
			}
			else
			{
				int[] dw = new int[ew.length];
				
				dw[0] = ew[0];
				dw[1] = ew[1];
				dw[2] = ew[2];
				dw[3] = ew[3];
				
				dw[roundsx4] = ew[roundsx4];
				dw[roundsx4 + 1] = ew[roundsx4 + 1];
				dw[roundsx4 + 2] = ew[roundsx4 + 2];
				dw[roundsx4 + 3] = ew[roundsx4 + 3];
				
				for(int i = 4; i < roundsx4; i++)
				{
					dw[i] = EncryptionAesFastEngineGeneral.inv_mcol(ew[i]);
				}
				
				return new AesKey1D(rounds, ew, dw);
			}
		}
	}
	
	
	public void loadIv(final byte[] bytes)
	{
		unpackBlockStore1(bytes, 0);
	}
	
	public void processBlockEncryption(final byte[] in, final int inOff, final byte[] out, final int outOff)
	{
		unpackBlock(in, inOff);
		xorBlockByBlockStore1();
		encryptBlock();
		putBlockInBlockStore1();
		packBlock(out, outOff);
	}
	
	public void processBlockDecryption(final byte[] in, final int inOff, final byte[] out, final int outOff)
	{
		unpackBlock(in, inOff);
		putBlockInBlockStore2();
		decryptBlock();
		xorBlockByBlockStore1();
		putBlockStore2InBlockStore1();
		packBlock(out, outOff);
	}
	
	private void unpackBlock(final byte[] bytes, final int off)
	{
		C0 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off);
		C1 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 4);
		C2 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 8);
		C3 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 12);
	}
	private void unpackBlockStore1(final byte[] bytes, final int off)
	{
		C0store1 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off);
		C1store1 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 4);
		C2store1 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 8);
		C3store1 = EncryptionAesFastEngineGeneral.littleEndianToInt(bytes, off + 12);
	}
	
	private void packBlock(final byte[] bytes, final int off)
	{
		EncryptionAesFastEngineGeneral.intToLittleEndian(C0, bytes, off);
		EncryptionAesFastEngineGeneral.intToLittleEndian(C1, bytes, off + 4);
		EncryptionAesFastEngineGeneral.intToLittleEndian(C2, bytes, off + 8);
		EncryptionAesFastEngineGeneral.intToLittleEndian(C3, bytes, off + 12);
	}
	
	private void putBlockInBlockStore1()
	{
		C0store1 = C0;
		C1store1 = C1;
		C2store1 = C2;
		C3store1 = C3;
	}
	private void putBlockInBlockStore2()
	{
		C0store2 = C0;
		C1store2 = C1;
		C2store2 = C2;
		C3store2 = C3;
	}
	private void putBlockStore2InBlockStore1()
	{
		C0store1 = C0store2;
		C1store1 = C1store2;
		C2store1 = C2store2;
		C3store1 = C3store2;
	}
	
	private void xorBlockByBlockStore1()
	{
		C0 ^= C0store1;
		C1 ^= C1store1;
		C2 ^= C2store1;
		C3 ^= C3store1;
	}
	
	private void encryptBlock()
	{
		int t0 = C0 ^ EW[0];
		int t1 = C1 ^ EW[1];
		int t2 = C2 ^ EW[2];
		
		int r = 1;
		int rx = 4;
		int r0;
		int r1;
		int r2;
		int r3 = C3 ^ EW[3];
		int i0;
		int i1;
		int i2;
		int i3;
		
		while(r < (ROUNDS - 1))
		{
			i0 = t0;
			i1 = (t1 >>> 8);
			i2 = (t2 >>> 16);
			i3 = (r3 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r0 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx];
			
			i0 = t1;
			i1 = (t2 >>> 8);
			i2 = (r3 >>> 16);
			i3 = (t0 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r1 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 1];
			
			i0 = t2;
			i1 = (r3 >>> 8);
			i2 = (t0 >>> 16);
			i3 = (t1 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r2 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 2];
			
			i0 = r3;
			i1 = (t0 >>> 8);
			i2 = (t1 >>> 16);
			i3 = (t2 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r3 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 3];
			r++;
			rx += 4;
			
			i0 = r0;
			i1 = (r1 >>> 8);
			i2 = (r2 >>> 16);
			i3 = (r3 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t0 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx];
			
			i0 = r1;
			i1 = (r2 >>> 8);
			i2 = (r3 >>> 16);
			i3 = (r0 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t1 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 1];
			
			i0 = r2;
			i1 = (r3 >>> 8);
			i2 = (r0 >>> 16);
			i3 = (r1 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t2 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 2];
			
			i0 = r3;
			i1 = (r0 >>> 8);
			i2 = (r1 >>> 16);
			i3 = (r2 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r3 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 3];
			r++;
			rx += 4;
		}
		
		i0 = t0;
		i1 = (t1 >>> 8);
		i2 = (t2 >>> 16);
		i3 = (r3 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r0 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx];
		
		i0 = t1;
		i1 = (t2 >>> 8);
		i2 = (r3 >>> 16);
		i3 = (t0 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r1 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 1];
		
		i0 = t2;
		i1 = (r3 >>> 8);
		i2 = (t0 >>> 16);
		i3 = (t1 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r2 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 2];
		
		i0 = r3;
		i1 = (t0 >>> 8);
		i2 = (t1 >>> 16);
		i3 = (t2 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r3 = EncryptionAesFastEngineGeneral.T[i0] ^ EncryptionAesFastEngineGeneral.T[256 + i1] ^ EncryptionAesFastEngineGeneral.T[512 + i2] ^ EncryptionAesFastEngineGeneral.T[768 + i3] ^ EW[rx + 3];
		rx += 4;
		
		i0 = r0;
		i1 = (r1 >>> 8);
		i2 = (r2 >>> 16);
		i3 = (r3 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.S[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.S[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.S[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.S[i3 & 255] & 255;
		C0 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ EW[rx];
		
		i0 = r1;
		i1 = (r2 >>> 8);
		i2 = (r3 >>> 16);
		i3 = (r0 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.S[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.S[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.S[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.S[i3 & 255] & 255;
		C1 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ EW[rx + 1];
		
		i0 = r2;
		i1 = (r3 >>> 8);
		i2 = (r0 >>> 16);
		i3 = (r1 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.S[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.S[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.S[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.S[i3 & 255] & 255;
		C2 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ EW[rx + 2];
		
		i0 = r3;
		i1 = (r0 >>> 8);
		i2 = (r1 >>> 16);
		i3 = (r2 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.S[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.S[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.S[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.S[i3 & 255] & 255;
		C3 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ EW[rx + 3];
	}
	
	private void decryptBlock()
	{
		int ROUNDSx4 = ROUNDS * 4;
		
		int t0 = C0 ^ DW[ROUNDSx4];
		int t1 = C1 ^ DW[ROUNDSx4 + 1];
		int t2 = C2 ^ DW[ROUNDSx4 + 2];
		
		int r = ROUNDS - 1;
		int rx = ROUNDSx4 - 4;
		int r0;
		int r1;
		int r2;
		int r3 = C3 ^ DW[ROUNDSx4 + 3];
		int i0;
		int i1;
		int i2;
		int i3;
		
		while(r > 1)
		{
			i0 = t0;
			i1 = (r3 >>> 8);
			i2 = (t2 >>> 16);
			i3 = (t1 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r0 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx];
			
			i0 = t1;
			i1 = (t0 >>> 8);
			i2 = (r3 >>> 16);
			i3 = (t2 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r1 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 1];
			
			i0 = t2;
			i1 = (t1 >>> 8);
			i2 = (t0 >>> 16);
			i3 = (r3 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r2 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 2];
			
			i0 = r3;
			i1 = (t2 >>> 8);
			i2 = (t1 >>> 16);
			i3 = (t0 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r3 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 3];
			r--;
			rx -= 4;
			
			i0 = r0;
			i1 = (r3 >>> 8);
			i2 = (r2 >>> 16);
			i3 = (r1 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t0 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx];
			
			i0 = r1;
			i1 = (r0 >>> 8);
			i2 = (r3 >>> 16);
			i3 = (r2 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t1 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 1];
			
			i0 = r2;
			i1 = (r1 >>> 8);
			i2 = (r0 >>> 16);
			i3 = (r3 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			t2 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 2];
			
			i0 = r3;
			i1 = (r2 >>> 8);
			i2 = (r1 >>> 16);
			i3 = (r0 >>> 24);
			i0 &= 255;
			i1 &= 255;
			i2 &= 255;
			i3 &= 255;
			r3 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[rx + 3];
			r--;
			rx -= 4;
		}
		
		i0 = t0;
		i1 = (r3 >>> 8);
		i2 = (t2 >>> 16);
		i3 = (t1 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r0 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[4];
		
		i0 = t1;
		i1 = (t0 >>> 8);
		i2 = (r3 >>> 16);
		i3 = (t2 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r1 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[5];
		
		i0 = t2;
		i1 = (t1 >>> 8);
		i2 = (t0 >>> 16);
		i3 = (r3 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r2 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[6];
		
		i0 = r3;
		i1 = (t2 >>> 8);
		i2 = (t1 >>> 16);
		i3 = (t0 >>> 24);
		i0 &= 255;
		i1 &= 255;
		i2 &= 255;
		i3 &= 255;
		r3 = EncryptionAesFastEngineGeneral.Tinv[i0] ^ EncryptionAesFastEngineGeneral.Tinv[256 + i1] ^ EncryptionAesFastEngineGeneral.Tinv[512 + i2] ^ EncryptionAesFastEngineGeneral.Tinv[768 + i3] ^ DW[7];
		
		i0 = r0;
		i1 = (r3 >>> 8);
		i2 = (r2 >>> 16);
		i3 = (r1 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.Si[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.Si[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.Si[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.Si[i3 & 255] & 255;
		C0 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ DW[0];
		
		i0 = r1;
		i1 = (r0 >>> 8);
		i2 = (r3 >>> 16);
		i3 = (r2 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.Si[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.Si[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.Si[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.Si[i3 & 255] & 255;
		C1 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ DW[1];
		
		i0 = r2;
		i1 = (r1 >>> 8);
		i2 = (r0 >>> 16);
		i3 = (r3 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.Si[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.Si[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.Si[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.Si[i3 & 255] & 255;
		C2 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ DW[2];
		
		i0 = r3;
		i1 = (r2 >>> 8);
		i2 = (r1 >>> 16);
		i3 = (r0 >>> 24);
		i0 = EncryptionAesFastEngineGeneral.Si[i0 & 255] & 255;
		i1 = EncryptionAesFastEngineGeneral.Si[i1 & 255] & 255;
		i2 = EncryptionAesFastEngineGeneral.Si[i2 & 255] & 255;
		i3 = EncryptionAesFastEngineGeneral.Si[i3 & 255] & 255;
		C3 = i0 ^ (i1 << 8) ^ (i2 << 16) ^ (i3 << 24) ^ DW[3];
	}
}
