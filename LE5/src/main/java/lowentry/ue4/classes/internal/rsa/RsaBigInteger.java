package lowentry.ue4.classes.internal.rsa;


import java.security.SecureRandom;
import java.util.Arrays;


public class RsaBigInteger
{
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	
	public final int   signum;
	public final int[] mag;
	
	
	private int bitLength          = 0;
	private int lowestSetBit       = 0;
	private int firstNonzeroIntNum = 0;
	
	
	private final static long LONG_MASK                     = 0xffffffffL;
	private static final int  MAX_MAG_LENGTH                = (0x7fffffff / 32) + 1;
	private static final int  PRIME_SEARCH_BIT_LENGTH_LIMIT = 500000000;
	private static final int  KARATSUBA_THRESHOLD           = 80;
	private static final int  TOOM_COOK_THRESHOLD           = 240;
	private static final int  KARATSUBA_SQUARE_THRESHOLD    = 128;
	private static final int  TOOM_COOK_SQUARE_THRESHOLD    = 216;
	public static final  int  BURNIKEL_ZIEGLER_THRESHOLD    = 80;
	public static final  int  BURNIKEL_ZIEGLER_OFFSET       = 40;
	private static final int  MULTIPLY_SQUARE_THRESHOLD     = 20;
	
	
	private static final int DEFAULT_PRIME_CERTAINTY = 100;
	
	
	private static final RsaBigInteger[] cache_positive = new RsaBigInteger[256];
	private static final RsaBigInteger[] cache_negative = new RsaBigInteger[32];
	
	public static final RsaBigInteger ZERO         = new RsaBigInteger(new int[0], 0);
	public static final RsaBigInteger NEGATIVE_ONE = valueOf(-1);
	public static final RsaBigInteger ONE          = valueOf(1);
	public static final RsaBigInteger TWO          = valueOf(2);
	public static final RsaBigInteger TEN          = valueOf(10);
	
	
	private static final int[] bnExpModThreshTable = {7, 25, 81, 241, 673, 1793, 0x7fffffff};
	
	
	private RsaBigInteger(int[] val)
	{
		if((val == null) || (val.length == 0))
		{
			//throw new NumberFormatException("Zero length BigInteger");
			mag = new int[0];
			signum = 0;
			return;
		}
		
		if(val[0] < 0)
		{
			int[] newmag = makePositive(val);
			if(!checkRange(newmag))
			{
				signum = 0;
				mag = new int[0];
				return;
			}
			mag = newmag;
			signum = -1;
		}
		else
		{
			int[] newmag = trustedStripLeadingZeroInts(val);
			if(!checkRange(newmag))
			{
				signum = 0;
				mag = new int[0];
				return;
			}
			mag = newmag;
			signum = (mag.length == 0 ? 0 : 1);
		}
	}
	
	public RsaBigInteger(int signum, byte[] magnitude)
	{
		if((signum < -1) || (signum > 1))
		{
			//throw(new NumberFormatException("Invalid signum value"));
			this.mag = new int[0];
			this.signum = 0;
			return;
		}
		
		int[] mag = stripLeadingZeroBytes(magnitude);
		if(!checkRange(mag))
		{
			this.mag = new int[0];
			this.signum = 0;
			return;
		}
		
		if(mag.length == 0)
		{
			this.mag = mag;
			this.signum = 0;
		}
		else
		{
			if(signum == 0)
			{
				//throw(new NumberFormatException("signum-magnitude mismatch"));
				this.mag = new int[0];
				this.signum = 0;
				return;
			}
			this.mag = mag;
			this.signum = signum;
		}
	}
	
	private RsaBigInteger(int signum, int[] magnitude)
	{
		if((signum < -1) || (signum > 1))
		{
			//throw(new NumberFormatException("Invalid signum value"));
			this.mag = new int[0];
			this.signum = 0;
			return;
		}
		
		int[] mag = stripLeadingZeroInts(magnitude);
		if(!checkRange(mag))
		{
			this.mag = new int[0];
			this.signum = 0;
			return;
		}
		
		if(mag.length == 0)
		{
			this.mag = mag;
			this.signum = 0;
		}
		else
		{
			if(signum == 0)
			{
				//throw(new NumberFormatException("signum-magnitude mismatch"));
				this.mag = new int[0];
				this.signum = 0;
				return;
			}
			this.mag = mag;
			this.signum = signum;
		}
	}
	
	public RsaBigInteger(int[] magnitude, int signum)
	{
		if(!checkRange(magnitude))
		{
			this.signum = 0;
			this.mag = new int[0];
			return;
		}
		this.signum = (magnitude.length == 0 ? 0 : signum);
		this.mag = magnitude;
	}
	
	private RsaBigInteger(int numBits)
	{
		this(1, randomBits(numBits));
	}
	
	private RsaBigInteger(long val)
	{
		if(val < 0)
		{
			val = -val;
			signum = -1;
		}
		else
		{
			signum = 1;
		}
		
		int highWord = (int) sl(val, 32);
		if(highWord == 0)
		{
			mag = new int[1];
			mag[0] = (int) val;
		}
		else
		{
			mag = new int[2];
			mag[0] = highWord;
			mag[1] = (int) val;
		}
	}
	
	
	private static int si(int a, int b)
	{
		return (a >>> b);
	}
	private static long sl(long a, int b)
	{
		return (a >>> b);
	}
	
	
	private static void randomBytes(byte[] bytes)
	{
		SECURE_RANDOM.nextBytes(bytes);
	}
	
	private static byte[] randomBits(int numBits)
	{
		if(numBits < 0)
		{
			//throw new IllegalArgumentException("numBits must be non-negative");
			numBits = 0;
		}
		int numBytes = (int) (((long) numBits + 7) / 8);
		byte[] randomBits = new byte[numBytes];
		
		if(numBytes > 0)
		{
			randomBytes(randomBits);
			int excessBits = (8 * numBytes) - numBits;
			randomBits[0] &= (1 << (8 - excessBits)) - 1;
		}
		return randomBits;
	}
	
	public static RsaBigInteger probablePrime(int bitLength)
	{
		if(bitLength < 2)
		{
			//throw new ArithmeticException("bitLength < 2");
			return null;
		}
		return largePrime(bitLength, DEFAULT_PRIME_CERTAINTY);
	}
	
	
	private static RsaBigInteger largePrime(int bitLength, int certainty)
	{
		RsaBigInteger p;
		p = new RsaBigInteger(bitLength).setBit(bitLength - 1);
		if((p == null) || (p.mag.length <= 0))
		{
			return null;
		}
		p.mag[p.mag.length - 1] &= 0xfffffffe;
		
		if(bitLength > (PRIME_SEARCH_BIT_LENGTH_LIMIT + 1))
		{
			//throw new ArithmeticException("Prime search implementation restriction on bitLength");
			return null;
		}
		int searchLen = (bitLength / 20) * 64;
		
		RsaBitSieve searchSieve = new RsaBitSieve(p, searchLen);
		RsaBigInteger candidate = searchSieve.retrieve(p, certainty);
		
		while((candidate == null) || (candidate.bitLength() != bitLength))
		{
			p = p.add(RsaBigInteger.valueOf(2 * searchLen));
			if(p == null)
			{
				return null;
			}
			if(p.bitLength() != bitLength)
			{
				p = new RsaBigInteger(bitLength).setBit(bitLength - 1);
			}
			if((p == null) || (p.mag.length <= 0))
			{
				return null;
			}
			p.mag[p.mag.length - 1] &= 0xfffffffe;
			searchSieve = new RsaBitSieve(p, searchLen);
			candidate = searchSieve.retrieve(p, certainty);
		}
		return candidate;
	}
	
	
	public boolean primeToCertainty(int certainty)
	{
		int rounds;
		int n = (Math.min(certainty, 0x7fffffff - 1) + 1) / 2;
		
		int sizeInBits = this.bitLength();
		if(sizeInBits < 100)
		{
			rounds = 50;
			rounds = n < rounds ? n : rounds;
			return passesMillerRabin(rounds);
		}
		
		if(sizeInBits < 256)
		{
			rounds = 27;
		}
		else if(sizeInBits < 512)
		{
			rounds = 15;
		}
		else if(sizeInBits < 768)
		{
			rounds = 8;
		}
		else if(sizeInBits < 1024)
		{
			rounds = 4;
		}
		else
		{
			rounds = 2;
		}
		rounds = n < rounds ? n : rounds;
		
		return passesMillerRabin(rounds) && passesLucasLehmer();
	}
	
	private boolean passesLucasLehmer()
	{
		RsaBigInteger thisPlusOne = this.add(ONE);
		if(thisPlusOne == null)
		{
			return true;
		}
		
		int d = 5;
		while(jacobiSymbol(d, this) != -1)
		{
			d = (d < 0) ? Math.abs(d) + 2 : -(d + 2);
		}
		
		RsaBigInteger u = lucasLehmerSequence(d, thisPlusOne, this);
		if(u == null)
		{
			return true;
		}
		
		RsaBigInteger umod = u.mod(this);
		if(umod == null)
		{
			return true;
		}
		
		return umod.equals(ZERO);
	}
	
	private static int jacobiSymbol(int p, RsaBigInteger n)
	{
		if((n == null) || (n.mag.length <= 0))
		{
			return -1;
		}
		
		if(p == 0)
		{
			return 0;
		}
		
		int j = 1;
		int u = n.mag[n.mag.length - 1];
		
		if(p < 0)
		{
			p = -p;
			int n8 = u & 7;
			if((n8 == 3) || (n8 == 7))
			{
				j = -j;
			}
		}
		
		while((p & 3) == 0)
		{
			p >>= 2;
		}
		if((p & 1) == 0)
		{
			p >>= 1;
			if(((u ^ (u >> 1)) & 2) != 0)
			{
				j = -j;
			}
		}
		if(p == 1)
		{
			return j;
		}
		
		if((p & u & 2) != 0)
		{
			j = -j;
		}
		
		RsaBigInteger uobj = n.mod(RsaBigInteger.valueOf(p));
		if(uobj == null)
		{
			return -1;
		}
		u = uobj.intValue();
		
		while(u != 0)
		{
			while((u & 3) == 0)
			{
				u >>= 2;
			}
			if((u & 1) == 0)
			{
				u >>= 1;
				if(((p ^ (p >> 1)) & 2) != 0)
				{
					j = -j;
				}
			}
			if(u == 1)
			{
				return j;
			}
			
			if(u < p)
			{
				return -1;
			}
			
			int t = u;
			u = p;
			p = t;
			if((u & p & 2) != 0)
			{
				j = -j;
			}
			
			u %= p;
		}
		return 0;
	}
	
	private static RsaBigInteger lucasLehmerSequence(int z, RsaBigInteger k, RsaBigInteger n)
	{
		if((k == null) || (n == null))
		{
			return null;
		}
		
		RsaBigInteger d = RsaBigInteger.valueOf(z);
		RsaBigInteger u = ONE;
		RsaBigInteger u2;
		RsaBigInteger v = ONE;
		RsaBigInteger v2;
		
		for(int i = k.bitLength() - 2; i >= 0; i--)
		{
			u2 = u.multiply(v);
			if(u2 == null)
			{
				return null;
			}
			
			u2 = u2.mod(n);
			if(u2 == null)
			{
				return null;
			}
			
			v2 = v.square();
			if(v2 == null)
			{
				return null;
			}
			
			RsaBigInteger usqr = u.square();
			if(usqr == null)
			{
				return null;
			}
			
			RsaBigInteger dmultUsql = d.multiply(usqr);
			if(dmultUsql == null)
			{
				return null;
			}
			
			v2 = v2.add(dmultUsql);
			if(v2 == null)
			{
				return null;
			}
			
			v2 = v2.mod(n);
			if(v2 == null)
			{
				return null;
			}
			
			if(v2.testBit(0))
			{
				v2 = v2.subtract(n);
				if(v2 == null)
				{
					return null;
				}
			}
			
			v2 = v2.shiftRight(1);
			if(v2 == null)
			{
				return null;
			}
			
			u = u2;
			v = v2;
			if(k.testBit(i))
			{
				u2 = u.add(v);
				if(u2 == null)
				{
					return null;
				}
				
				u2 = u2.mod(n);
				if(u2 == null)
				{
					return null;
				}
				
				if(u2.testBit(0))
				{
					u2 = u2.subtract(n);
					if(u2 == null)
					{
						return null;
					}
				}
				
				u2 = u2.shiftRight(1);
				if(u2 == null)
				{
					return null;
				}
				
				RsaBigInteger dmultU = d.multiply(u);
				if(dmultU == null)
				{
					return null;
				}
				
				v2 = v.add(dmultU);
				if(v2 == null)
				{
					return null;
				}
				
				v2 = v2.mod(n);
				if(v2 == null)
				{
					return null;
				}
				
				if(v2.testBit(0))
				{
					v2 = v2.subtract(n);
					if(v2 == null)
					{
						return null;
					}
				}
				
				v2 = v2.shiftRight(1);
				if(v2 == null)
				{
					return null;
				}
				
				u = u2;
				v = v2;
			}
		}
		return u;
	}
	
	private boolean passesMillerRabin(int iterations)
	{
		RsaBigInteger thisMinusOne = this.subtract(ONE);
		if(thisMinusOne == null)
		{
			return true;
		}
		RsaBigInteger m = thisMinusOne;
		int a = m.getLowestSetBit();
		m = m.shiftRight(a);
		if(m == null)
		{
			return true;
		}
		
		for(int i = 0; i < iterations; i++)
		{
			RsaBigInteger b = new RsaBigInteger(this.bitLength());
			while((b.compareTo(ONE) <= 0) || (b.compareTo(this) >= 0))
			{
				b = new RsaBigInteger(this.bitLength());
			}
			
			int j = 0;
			RsaBigInteger z = b.modPow(m, this);
			if(z == null)
			{
				return true;
			}
			
			while(!(((j == 0) && z.equals(ONE)) || z.equals(thisMinusOne)))
			{
				if(((j > 0) && z.equals(ONE)) || ((j + 1) == a))
				{
					return false;
				}
				j++;
				z = z.modPow(TWO, this);
				if(z == null)
				{
					return true;
				}
			}
		}
		return true;
	}
	
	private static boolean checkRange(int[] mag)
	{
		//throw new ArithmeticException("BigInteger would overflow supported range");
		return (mag != null) && (mag.length <= MAX_MAG_LENGTH) && ((mag.length != MAX_MAG_LENGTH) || (mag[0] >= 0));
	}
	
	
	public static RsaBigInteger valueOf(long val)
	{
		if(val == 0)
		{
			return ZERO;
		}
		if((val > 0) && (val <= cache_positive.length))
		{
			synchronized(cache_positive)
			{
				RsaBigInteger bigint = cache_positive[((int) val) - 1];
				if(bigint == null)
				{
					bigint = new RsaBigInteger(val);
					cache_positive[((int) val) - 1] = bigint;
				}
				return bigint;
			}
		}
		if((val < 0) && (-val <= cache_negative.length))
		{
			synchronized(cache_negative)
			{
				RsaBigInteger bigint = cache_negative[((int) -val) - 1];
				if(bigint == null)
				{
					bigint = new RsaBigInteger(val);
					cache_negative[((int) -val) - 1] = bigint;
				}
				return bigint;
			}
		}
		return new RsaBigInteger(val);
	}
	
	private static RsaBigInteger valueOf(int[] val)
	{
		if((val == null) || (val.length <= 0))
		{
			return ZERO;
		}
		return (val[0] > 0 ? new RsaBigInteger(val, 1) : new RsaBigInteger(val));
	}
	
	
	public RsaBigInteger add(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		if(val.signum == 0)
		{
			return this;
		}
		if(signum == 0)
		{
			return val;
		}
		if(val.signum == signum)
		{
			return new RsaBigInteger(add(mag, val.mag), signum);
		}
		
		int cmp = compareMagnitude(val);
		if(cmp == 0)
		{
			return ZERO;
		}
		int[] resultMag = (cmp > 0 ? subtract(mag, val.mag) : subtract(val.mag, mag));
		resultMag = trustedStripLeadingZeroInts(resultMag);
		
		return new RsaBigInteger(resultMag, cmp == signum ? 1 : -1);
	}
	
	private static int[] add(int[] x, int[] y)
	{
		if((x == null) || (y == null))
		{
			return new int[0];
		}
		
		if(x.length < y.length)
		{
			int[] tmp = x;
			//noinspection SuspiciousNameCombination
			x = y;
			y = tmp;
		}
		
		int xIndex = x.length;
		int yIndex = y.length;
		int[] result = new int[xIndex];
		long sum = 0;
		if(yIndex == 1)
		{
			xIndex--;
			sum = (x[xIndex] & LONG_MASK) + (y[0] & LONG_MASK);
			result[xIndex] = (int) sum;
		}
		else
		{
			while(yIndex > 0)
			{
				xIndex--;
				yIndex--;
				sum = (x[xIndex] & LONG_MASK) + (y[yIndex] & LONG_MASK) + sl(sum, 32);
				result[xIndex] = (int) sum;
			}
		}
		
		boolean carry = (sl(sum, 32) != 0);
		while((xIndex > 0) && carry)
		{
			xIndex--;
			carry = ((result[xIndex] = x[xIndex] + 1) == 0);
		}
		
		while(xIndex > 0)
		{
			xIndex--;
			result[xIndex] = x[xIndex];
		}
		
		if(carry)
		{
			int[] bigger = new int[result.length + 1];
			System.arraycopy(result, 0, bigger, 1, result.length);
			bigger[0] = 0x01;
			return bigger;
		}
		return result;
	}
	
	public RsaBigInteger subtract(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		if(val.signum == 0)
		{
			return this;
		}
		if(signum == 0)
		{
			return val.negate();
		}
		if(val.signum != signum)
		{
			return new RsaBigInteger(add(mag, val.mag), signum);
		}
		
		int cmp = compareMagnitude(val);
		if(cmp == 0)
		{
			return ZERO;
		}
		int[] resultMag = (cmp > 0 ? subtract(mag, val.mag) : subtract(val.mag, mag));
		resultMag = trustedStripLeadingZeroInts(resultMag);
		return new RsaBigInteger(resultMag, cmp == signum ? 1 : -1);
	}
	
	private static int[] subtract(int[] big, int[] little)
	{
		if((big == null) || (little == null))
		{
			return new int[0];
		}
		
		int bigIndex = big.length;
		int[] result = new int[bigIndex];
		int littleIndex = little.length;
		long difference = 0;
		
		while(littleIndex > 0)
		{
			bigIndex--;
			littleIndex--;
			difference = ((big[bigIndex] & LONG_MASK) - (little[littleIndex] & LONG_MASK)) + (difference >> 32);
			result[bigIndex] = (int) difference;
		}
		
		boolean borrow = ((difference >> 32) != 0);
		while((bigIndex > 0) && borrow)
		{
			bigIndex--;
			borrow = ((result[bigIndex] = big[bigIndex] - 1) == -1);
		}
		
		while(bigIndex > 0)
		{
			bigIndex--;
			result[bigIndex] = big[bigIndex];
		}
		return result;
	}
	
	public RsaBigInteger multiply(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		
		if((val.signum == 0) || (signum == 0))
		{
			return ZERO;
		}
		
		int xlen = mag.length;
		
		if((val == this) && (xlen > MULTIPLY_SQUARE_THRESHOLD))
		{
			return square();
		}
		
		int ylen = val.mag.length;
		
		if((xlen < KARATSUBA_THRESHOLD) || (ylen < KARATSUBA_THRESHOLD))
		{
			int resultSign = signum == val.signum ? 1 : -1;
			if(val.mag.length == 1)
			{
				return multiplyByInt(mag, val.mag[0], resultSign);
			}
			if(mag.length == 1)
			{
				return multiplyByInt(val.mag, mag[0], resultSign);
			}
			int[] result = multiplyToLen(mag, xlen, val.mag, ylen, null);
			result = trustedStripLeadingZeroInts(result);
			return new RsaBigInteger(result, resultSign);
		}
		else
		{
			if((xlen < TOOM_COOK_THRESHOLD) && (ylen < TOOM_COOK_THRESHOLD))
			{
				return multiplyKaratsuba(this, val);
			}
			else
			{
				return multiplyToomCook3(this, val);
			}
		}
	}
	
	private static int integerBitCount(int i)
	{
		i = i - (si(i, 1) & 0x55555555);
		i = (i & 0x33333333) + (si(i, 2) & 0x33333333);
		i = (i + si(i, 4)) & 0x0f0f0f0f;
		i = i + si(i, 8);
		i = i + si(i, 16);
		return (i & 0x3f);
	}
	
	private static int integerNumberOfTrailingZeros(int i)
	{
		int y;
		if(i == 0)
		{
			return 32;
		}
		int n = 31;
		y = i << 16;
		if(y != 0)
		{
			n = n - 16;
			i = y;
		}
		y = i << 8;
		if(y != 0)
		{
			n = n - 8;
			i = y;
		}
		y = i << 4;
		if(y != 0)
		{
			n = n - 4;
			i = y;
		}
		y = i << 2;
		if(y != 0)
		{
			n = n - 2;
			i = y;
		}
		return (n - si((i << 1), 31));
	}
	
	private static int integerNumberOfLeadingZeros(int i)
	{
		if(i == 0)
		{
			return 32;
		}
		int n = 1;
		if(si(i, 16) == 0)
		{
			n += 16;
			i <<= 16;
		}
		if(si(i, 24) == 0)
		{
			n += 8;
			i <<= 8;
		}
		if(si(i, 28) == 0)
		{
			n += 4;
			i <<= 4;
		}
		if(si(i, 30) == 0)
		{
			n += 2;
			i <<= 2;
		}
		n -= si(i, 31);
		return n;
	}
	
	private static RsaBigInteger multiplyByInt(int[] x, int y, int sign)
	{
		if(x == null)
		{
			return null;
		}
		if(integerBitCount(y) == 1)
		{
			return new RsaBigInteger(shiftLeft(x, integerNumberOfTrailingZeros(y)), sign);
		}
		int xlen = x.length;
		int[] rmag = new int[xlen + 1];
		long carry = 0;
		long yl = y & LONG_MASK;
		int rstart = rmag.length - 1;
		for(int i = xlen - 1; i >= 0; i--)
		{
			long product = ((x[i] & LONG_MASK) * yl) + carry;
			rmag[rstart] = (int) product;
			rstart--;
			carry = sl(product, 32);
		}
		if(carry == 0L)
		{
			rmag = Arrays.copyOfRange(rmag, 1, rmag.length);
		}
		else
		{
			rmag[rstart] = (int) carry;
		}
		return new RsaBigInteger(rmag, sign);
	}
	
	private int[] multiplyToLen(int[] x, int xlen, int[] y, int ylen, int[] z)
	{
		if((x == null) || (y == null))
		{
			return new int[0];
		}
		
		int xstart = xlen - 1;
		int ystart = ylen - 1;
		
		if((z == null) || (z.length < (xlen + ylen)))
		{
			z = new int[xlen + ylen];
		}
		
		long carry = 0;
		for(int j = ystart, k = ystart + 1 + xstart; j >= 0; j--, k--)
		{
			long product = ((y[j] & LONG_MASK) * (x[xstart] & LONG_MASK)) + carry;
			z[k] = (int) product;
			carry = sl(product, 32);
		}
		z[xstart] = (int) carry;
		
		for(int i = xstart - 1; i >= 0; i--)
		{
			carry = 0;
			for(int j = ystart, k = ystart + 1 + i; j >= 0; j--, k--)
			{
				long product = ((y[j] & LONG_MASK) * (x[i] & LONG_MASK)) + (z[k] & LONG_MASK) + carry;
				z[k] = (int) product;
				carry = sl(product, 32);
			}
			z[i] = (int) carry;
		}
		return z;
	}
	
	private static RsaBigInteger multiplyKaratsuba(RsaBigInteger x, RsaBigInteger y)
	{
		if((x == null) || (y == null))
		{
			return null;
		}
		
		int xlen = x.mag.length;
		int ylen = y.mag.length;
		
		int half = (Math.max(xlen, ylen) + 1) / 2;
		
		RsaBigInteger xl = x.getLower(half);
		if(xl == null)
		{
			return null;
		}
		
		RsaBigInteger xh = x.getUpper(half);
		if(xh == null)
		{
			return null;
		}
		
		RsaBigInteger yl = y.getLower(half);
		if(yl == null)
		{
			return null;
		}
		
		RsaBigInteger yh = y.getUpper(half);
		if(yh == null)
		{
			return null;
		}
		
		
		RsaBigInteger p1 = xh.multiply(yh);
		if(p1 == null)
		{
			return null;
		}
		
		RsaBigInteger p2 = xl.multiply(yl);
		if(p2 == null)
		{
			return null;
		}
		
		
		RsaBigInteger p3 = xh.add(xl);
		if(p3 == null)
		{
			return null;
		}
		
		RsaBigInteger yhAddY1 = yh.add(yl);
		if(yhAddY1 == null)
		{
			return null;
		}
		
		p3 = p3.multiply(yhAddY1);
		if(p3 == null)
		{
			return null;
		}
		
		RsaBigInteger p3sub = p3.subtract(p1);
		if(p3sub == null)
		{
			return null;
		}
		p3sub = p3sub.subtract(p2);
		if(p3sub == null)
		{
			return null;
		}
		
		RsaBigInteger result = p1.shiftLeft(32 * half);
		if(result == null)
		{
			return null;
		}
		result = result.add(p3sub);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(32 * half);
		if(result == null)
		{
			return null;
		}
		result = result.add(p2);
		if(result == null)
		{
			return null;
		}
		
		if(x.signum != y.signum)
		{
			return result.negate();
		}
		else
		{
			return result;
		}
	}
	
	private static RsaBigInteger multiplyToomCook3(RsaBigInteger a, RsaBigInteger b)
	{
		if((a == null) || (b == null))
		{
			return null;
		}
		
		int alen = a.mag.length;
		int blen = b.mag.length;
		
		int largest = Math.max(alen, blen);
		
		int k = (largest + 2) / 3;
		
		int r = largest - (2 * k);
		
		
		RsaBigInteger a0, a1, a2, b0, b1, b2;
		
		a2 = a.getToomSlice(k, r, 0, largest);
		if(a2 == null)
		{
			return null;
		}
		
		a1 = a.getToomSlice(k, r, 1, largest);
		if(a1 == null)
		{
			return null;
		}
		
		a0 = a.getToomSlice(k, r, 2, largest);
		if(a0 == null)
		{
			return null;
		}
		
		b2 = b.getToomSlice(k, r, 0, largest);
		if(b2 == null)
		{
			return null;
		}
		
		b1 = b.getToomSlice(k, r, 1, largest);
		if(b1 == null)
		{
			return null;
		}
		
		b0 = b.getToomSlice(k, r, 2, largest);
		if(b0 == null)
		{
			return null;
		}
		
		
		RsaBigInteger v0, v1, v2, vm1, vinf, t1, t2, tm1, da1, db1;
		
		v0 = a0.multiply(b0);
		if(v0 == null)
		{
			return null;
		}
		
		da1 = a2.add(a0);
		if(da1 == null)
		{
			return null;
		}
		
		db1 = b2.add(b0);
		if(db1 == null)
		{
			return null;
		}
		
		RsaBigInteger db1SubB1 = db1.subtract(b1);
		if(db1SubB1 == null)
		{
			return null;
		}
		
		vm1 = da1.subtract(a1);
		if(vm1 == null)
		{
			return null;
		}
		vm1 = vm1.multiply(db1SubB1);
		if(vm1 == null)
		{
			return null;
		}
		
		da1 = da1.add(a1);
		if(da1 == null)
		{
			return null;
		}
		
		db1 = db1.add(b1);
		if(db1 == null)
		{
			return null;
		}
		
		v1 = da1.multiply(db1);
		if(v1 == null)
		{
			return null;
		}
		
		RsaBigInteger v2inner = db1.add(b2);
		if(v2inner == null)
		{
			return null;
		}
		v2inner = v2inner.shiftLeft(1);
		if(v2inner == null)
		{
			return null;
		}
		v2inner = v2inner.subtract(b0);
		if(v2inner == null)
		{
			return null;
		}
		
		v2 = da1.add(a2);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.shiftLeft(1);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.subtract(a0);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.multiply(v2inner);
		if(v2 == null)
		{
			return null;
		}
		
		vinf = a2.multiply(b2);
		if(vinf == null)
		{
			return null;
		}
		
		
		t2 = v2.subtract(vm1);
		if(t2 == null)
		{
			return null;
		}
		t2 = t2.exactDivideBy3();
//		if(t2 == null)
//		{
//			return null;
//		}
		
		tm1 = v1.subtract(vm1);
		if(tm1 == null)
		{
			return null;
		}
		tm1 = v1.shiftRight(1);
		if(tm1 == null)
		{
			return null;
		}
		
		t1 = v1.subtract(v0);
		if(t1 == null)
		{
			return null;
		}
		
		t2 = t2.subtract(t1);
		if(t2 == null)
		{
			return null;
		}
		t2 = t2.shiftRight(1);
		if(t2 == null)
		{
			return null;
		}
		
		t1 = t1.subtract(tm1);
		if(t1 == null)
		{
			return null;
		}
		t1 = t1.subtract(vinf);
		if(t1 == null)
		{
			return null;
		}
		
		RsaBigInteger vinfShiftleft = vinf.shiftLeft(1);
		if(vinfShiftleft == null)
		{
			return null;
		}
		
		t2 = t2.subtract(vinfShiftleft);
		if(t2 == null)
		{
			return null;
		}
		
		tm1 = tm1.subtract(t2);
		if(tm1 == null)
		{
			return null;
		}
		
		int ss = k * 32;
		
		RsaBigInteger result = vinf.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(t2);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(t1);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(tm1);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(v0);
		if(result == null)
		{
			return null;
		}
		
		if(a.signum != b.signum)
		{
			return result.negate();
		}
		else
		{
			return result;
		}
	}
	
	private RsaBigInteger getToomSlice(int lowerSize, int upperSize, int slice, int fullsize)
	{
		int start, end, sliceSize, len, offset;
		
		len = mag.length;
		offset = fullsize - len;
		
		if(slice == 0)
		{
			start = -offset;
			end = upperSize - 1 - offset;
		}
		else
		{
			start = (upperSize + ((slice - 1) * lowerSize)) - offset;
			end = (start + lowerSize) - 1;
		}
		
		if(start < 0)
		{
			start = 0;
		}
		if(end < 0)
		{
			return ZERO;
		}
		
		sliceSize = (end - start) + 1;
		
		if(sliceSize <= 0)
		{
			return ZERO;
		}
		
		if((start == 0) && (sliceSize >= len))
		{
			return this.abs();
		}
		
		int[] intSlice = new int[sliceSize];
		System.arraycopy(mag, start, intSlice, 0, sliceSize);
		
		return new RsaBigInteger(trustedStripLeadingZeroInts(intSlice), 1);
	}
	
	private RsaBigInteger exactDivideBy3()
	{
		int len = mag.length;
		int[] result = new int[len];
		long x, w, q, borrow;
		borrow = 0L;
		for(int i = len - 1; i >= 0; i--)
		{
			x = (mag[i] & LONG_MASK);
			w = x - borrow;
			if(borrow > x)
			{
				borrow = 1L;
			}
			else
			{
				borrow = 0L;
			}
			
			q = (w * 0xAAAAAAABL) & LONG_MASK;
			result[i] = (int) q;
			
			if(q >= 0x55555556L)
			{
				borrow++;
				if(q >= 0xAAAAAAABL)
				{
					borrow++;
				}
			}
		}
		result = trustedStripLeadingZeroInts(result);
		return new RsaBigInteger(result, signum);
	}
	
	private RsaBigInteger getLower(int n)
	{
		int len = mag.length;
		
		if(len <= n)
		{
			return abs();
		}
		
		int[] lowerInts = new int[n];
		System.arraycopy(mag, len - n, lowerInts, 0, n);
		
		return new RsaBigInteger(trustedStripLeadingZeroInts(lowerInts), 1);
	}
	
	private RsaBigInteger getUpper(int n)
	{
		int len = mag.length;
		
		if(len <= n)
		{
			return ZERO;
		}
		
		int upperLen = len - n;
		int[] upperInts = new int[upperLen];
		System.arraycopy(mag, 0, upperInts, 0, upperLen);
		
		return new RsaBigInteger(trustedStripLeadingZeroInts(upperInts), 1);
	}
	
	
	private RsaBigInteger square()
	{
		if(signum == 0)
		{
			return ZERO;
		}
		int len = mag.length;
		
		if(len < KARATSUBA_SQUARE_THRESHOLD)
		{
			int[] z = squareToLen(mag, len, null);
			return new RsaBigInteger(trustedStripLeadingZeroInts(z), 1);
		}
		else
		{
			if(len < TOOM_COOK_SQUARE_THRESHOLD)
			{
				return squareKaratsuba();
			}
			else
			{
				return squareToomCook3();
			}
		}
	}
	
	private static final int[] squareToLen(int[] x, int len, int[] z)
	{
		if(x == null)
		{
			return new int[0];
		}
		
		int zlen = len << 1;
		if((z == null) || (z.length < zlen))
		{
			z = new int[zlen];
		}
		
		int lastProductLowWord = 0;
		for(int j = 0, i = 0; j < len; j++)
		{
			long piece = (x[j] & LONG_MASK);
			long product = piece * piece;
			z[i] = (lastProductLowWord << 31) | (int) sl(product, 33);
			i++;
			z[i] = (int) sl(product, 1);
			i++;
			lastProductLowWord = (int) product;
		}
		
		for(int i = len, offset = 1; i > 0; i--, offset += 2)
		{
			int t = x[i - 1];
			t = mulAdd(z, x, offset, i - 1, t);
			addOne(z, offset - 1, i, t);
		}
		
		primitiveLeftShift(z, zlen, 1);
		z[zlen - 1] |= x[len - 1] & 1;
		
		return z;
	}
	
	private RsaBigInteger squareKaratsuba()
	{
		int half = (mag.length + 1) / 2;
		
		RsaBigInteger xl = getLower(half);
		if(xl == null)
		{
			return null;
		}
		
		RsaBigInteger xh = getUpper(half);
		if(xh == null)
		{
			return null;
		}
		
		RsaBigInteger xhs = xh.square();
		if(xhs == null)
		{
			return null;
		}
		
		RsaBigInteger xls = xl.square();
		if(xls == null)
		{
			return null;
		}
		
		RsaBigInteger resultA = xhs.add(xls);
		if(resultA == null)
		{
			return null;
		}
		
		RsaBigInteger resultB = xl.add(xh);
		if(resultB == null)
		{
			return null;
		}
		resultB = resultB.square();
		if(resultB == null)
		{
			return null;
		}
		resultB = resultB.subtract(resultA);
		if(resultB == null)
		{
			return null;
		}
		
		RsaBigInteger result = xhs.shiftLeft(half * 32);
		if(result == null)
		{
			return null;
		}
		result = result.add(resultB);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(half * 32);
		if(result == null)
		{
			return null;
		}
		result = result.add(xls);
		return result;
	}
	
	private RsaBigInteger squareToomCook3()
	{
		int len = mag.length;
		int k = (len + 2) / 3;
		int r = len - (2 * k);
		
		
		RsaBigInteger a0, a1, a2;
		
		a2 = getToomSlice(k, r, 0, len);
		if(a2 == null)
		{
			return null;
		}
		
		a1 = getToomSlice(k, r, 1, len);
		if(a1 == null)
		{
			return null;
		}
		
		a0 = getToomSlice(k, r, 2, len);
		if(a0 == null)
		{
			return null;
		}
		
		
		RsaBigInteger v0, v1, v2, vm1, vinf, t1, t2, tm1, da1;
		
		v0 = a0.square();
		if(v0 == null)
		{
			return null;
		}
		
		da1 = a2.add(a0);
		if(da1 == null)
		{
			return null;
		}
		
		vm1 = da1.subtract(a1);
		if(vm1 == null)
		{
			return null;
		}
		vm1 = vm1.square();
		if(vm1 == null)
		{
			return null;
		}
		
		da1 = da1.add(a1);
		if(da1 == null)
		{
			return null;
		}
		
		v1 = da1.square();
		if(v1 == null)
		{
			return null;
		}
		
		vinf = a2.square();
		if(vinf == null)
		{
			return null;
		}
		
		v2 = da1.add(a2);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.shiftLeft(1);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.subtract(a0);
		if(v2 == null)
		{
			return null;
		}
		v2 = v2.square();
		if(v2 == null)
		{
			return null;
		}
		
		
		t2 = v2.subtract(vm1);
		if(t2 == null)
		{
			return null;
		}
		t2 = t2.exactDivideBy3();
//		if(t2 == null)
//		{
//			return null;
//		}
		
		tm1 = v1.subtract(vm1);
		if(tm1 == null)
		{
			return null;
		}
		tm1 = tm1.shiftRight(1);
		if(tm1 == null)
		{
			return null;
		}
		
		t1 = v1.subtract(v0);
		if(t1 == null)
		{
			return null;
		}
		
		t2 = t2.subtract(t1);
		if(t2 == null)
		{
			return null;
		}
		t2 = t2.shiftRight(1);
		if(t2 == null)
		{
			return null;
		}
		
		t1 = t1.subtract(tm1);
		if(t1 == null)
		{
			return null;
		}
		t1 = t1.subtract(vinf);
		if(t1 == null)
		{
			return null;
		}
		
		RsaBigInteger vinfShiftLeft = vinf.shiftLeft(1);
		if(vinfShiftLeft == null)
		{
			return null;
		}
		
		t2 = t2.subtract(vinfShiftLeft);
		if(t2 == null)
		{
			return null;
		}
		
		tm1 = tm1.subtract(t2);
		if(tm1 == null)
		{
			return null;
		}
		
		int ss = k * 32;
		
		RsaBigInteger result = vinf.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(t2);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(t1);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(tm1);
		if(result == null)
		{
			return null;
		}
		result = result.shiftLeft(ss);
		if(result == null)
		{
			return null;
		}
		result = result.add(v0);
		return result;
	}
	
	
	public RsaBigInteger remainder(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		if((val.mag.length < BURNIKEL_ZIEGLER_THRESHOLD) || ((mag.length - val.mag.length) < BURNIKEL_ZIEGLER_OFFSET))
		{
			return remainderKnuth(val);
		}
		else
		{
			return remainderBurnikelZiegler(val);
		}
	}
	
	private RsaBigInteger remainderKnuth(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		RsaMutableBigInteger q = new RsaMutableBigInteger();
		RsaMutableBigInteger a = new RsaMutableBigInteger(this.mag);
		RsaMutableBigInteger b = new RsaMutableBigInteger(val.mag);
		RsaMutableBigInteger result = a.divideKnuth(b, q);
		if(result == null)
		{
			return null;
		}
		return result.toBigInteger(this.signum);
	}
	
	private RsaBigInteger remainderBurnikelZiegler(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		RsaBigInteger[] result = divideAndRemainderBurnikelZiegler(val);
		if(/* (result == null) || */ result.length <= 1)
		{
			return null;
		}
		return result[1];
	}
	
	private RsaBigInteger[] divideAndRemainderBurnikelZiegler(RsaBigInteger val)
	{
		if(val == null)
		{
			return new RsaBigInteger[0];
		}
		RsaMutableBigInteger q = new RsaMutableBigInteger();
		RsaMutableBigInteger r = new RsaMutableBigInteger(this).divideAndRemainderBurnikelZiegler(new RsaMutableBigInteger(val), q);
		if(r == null)
		{
			return new RsaBigInteger[0];
		}
		RsaBigInteger qBigInt = q.isZero() ? ZERO : q.toBigInteger(signum * val.signum);
		RsaBigInteger rBigInt = r.isZero() ? ZERO : r.toBigInteger(signum);
		return new RsaBigInteger[]{qBigInt, rBigInt};
	}
	
	
	public RsaBigInteger gcd(RsaBigInteger val)
	{
		if(val == null)
		{
			return null;
		}
		if(val.signum == 0)
		{
			return this.abs();
		}
		else if(this.signum == 0)
		{
			return val.abs();
		}
		
		RsaMutableBigInteger a = new RsaMutableBigInteger(this);
		RsaMutableBigInteger b = new RsaMutableBigInteger(val);
		
		RsaMutableBigInteger result = a.hybridGCD(b);
		if(result == null)
		{
			return null;
		}
		
		return result.toBigInteger(1);
	}
	
	public static int bitLengthForInt(int n)
	{
		return 32 - integerNumberOfLeadingZeros(n);
	}
	
	private static int[] leftShift(int[] a, int len, int n)
	{
		if((a == null) || (a.length <= 1))
		{
			return new int[0];
		}
		
		if(len > a.length)
		{
			len = a.length;
		}
		
		int nInts = si(n, 5);
		int nBits = n & 0x1F;
		int bitsInHighWord = bitLengthForInt(a[0]);
		
		if(n <= (32 - bitsInHighWord))
		{
			primitiveLeftShift(a, len, nBits);
			return a;
		}
		else
		{
			if(nBits <= (32 - bitsInHighWord))
			{
				int[] result = new int[nInts + len];
				System.arraycopy(a, 0, result, 0, len);
				primitiveLeftShift(result, result.length, nBits);
				return result;
			}
			else
			{
				int[] result = new int[nInts + len + 1];
				System.arraycopy(a, 0, result, 0, len);
				primitiveRightShift(result, result.length, 32 - nBits);
				return result;
			}
		}
	}
	
	private static void primitiveRightShift(int[] a, int len, int n)
	{
		if((a == null) || (a.length <= 1))
		{
			return;
		}
		
		if(len > a.length)
		{
			len = a.length;
		}
		
		int n2 = 32 - n;
		for(int i = len - 1, c = a[i]; i > 0; i--)
		{
			int b = c;
			c = a[i - 1];
			a[i] = (c << n2) | si(b, n);
		}
		a[0] = si(a[0], n);
	}
	
	private static void primitiveLeftShift(int[] a, int len, int n)
	{
		if((a == null) || (a.length <= 1))
		{
			return;
		}
		
		if(len > a.length)
		{
			len = a.length;
		}
		
		if((len == 0) || (n == 0))
		{
			return;
		}
		
		int n2 = 32 - n;
		for(int i = 0, c = a[i], m = (i + len) - 1; i < m; i++)
		{
			int b = c;
			c = a[i + 1];
			a[i] = (b << n) | si(c, n2);
		}
		a[len - 1] <<= n;
	}
	
	private static int bitLength(int[] val, int len)
	{
		if((val == null) || (val.length <= 1))
		{
			return 0;
		}
		if(len == 0)
		{
			return 0;
		}
		return ((len - 1) << 5) + bitLengthForInt(val[0]);
	}
	
	private RsaBigInteger abs()
	{
		return (signum >= 0 ? this : this.negate());
	}
	
	private RsaBigInteger negate()
	{
		return new RsaBigInteger(this.mag, -this.signum);
	}
	
	
	private RsaBigInteger mod(RsaBigInteger m)
	{
		if(m == null)
		{
			return null;
		}
		if(m.signum <= 0)
		{
			//throw new ArithmeticException("BigInteger: modulus not positive");
			return null;
		}
		
		RsaBigInteger result = this.remainder(m);
		if(result == null)
		{
			return null;
		}
		return (result.signum >= 0 ? result : result.add(m));
	}
	
	public RsaBigInteger modPow(RsaBigInteger exponent, RsaBigInteger m)
	{
		if((exponent == null) || (m == null))
		{
			return null;
		}
		if(m.signum <= 0)
		{
			//throw new ArithmeticException("BigInteger: modulus not positive");
			return null;
		}
		
		if(exponent.signum == 0)
		{
			return (m.equals(ONE) ? ZERO : ONE);
		}
		
		if(this.equals(ONE))
		{
			return (m.equals(ONE) ? ZERO : ONE);
		}
		
		if(this.equals(ZERO) && (exponent.signum >= 0))
		{
			return ZERO;
		}
		
		if(this.equals(NEGATIVE_ONE) && (!exponent.testBit(0)))
		{
			return (m.equals(ONE) ? ZERO : ONE);
		}
		
		boolean invertResult = (exponent.signum < 0);
		if(invertResult)
		{
			exponent = exponent.negate();
//			if(exponent == null)
//			{
//				return null;
//			}
		}
		
		RsaBigInteger base = ((this.signum < 0) || (this.compareTo(m) >= 0) ? this.mod(m) : this);
		if(base == null)
		{
			return null;
		}
		RsaBigInteger result;
		if(m.testBit(0))
		{
			result = base.oddModPow(exponent, m);
			if(result == null)
			{
				return null;
			}
		}
		else
		{
			int p = m.getLowestSetBit();
			
			RsaBigInteger m1 = m.shiftRight(p);
			if(m1 == null)
			{
				return null;
			}
			
			RsaBigInteger m2 = ONE.shiftLeft(p);
			if(m2 == null)
			{
				return null;
			}
			
			RsaBigInteger base2 = ((this.signum < 0) || (this.compareTo(m1) >= 0) ? this.mod(m1) : this);
			if(base2 == null)
			{
				return null;
			}
			
			RsaBigInteger a1 = (m1.equals(ONE) ? ZERO : base2.oddModPow(exponent, m1));
			if(a1 == null)
			{
				return null;
			}
			
			RsaBigInteger a2 = base.modPow2(exponent, p);
			if(a2 == null)
			{
				return null;
			}
			
			RsaBigInteger y1 = m2.modInverse(m1);
			if(y1 == null)
			{
				return null;
			}
			
			RsaBigInteger y2 = m1.modInverse(m2);
			if(y2 == null)
			{
				return null;
			}
			
			if(m.mag.length < (MAX_MAG_LENGTH / 2))
			{
				RsaBigInteger resultInner = a2.multiply(m1);
				if(resultInner == null)
				{
					return null;
				}
				resultInner = resultInner.multiply(y2);
				if(resultInner == null)
				{
					return null;
				}
				
				result = a1.multiply(m2);
				if(result == null)
				{
					return null;
				}
				result = result.multiply(y1);
				if(result == null)
				{
					return null;
				}
				result = result.add(resultInner);
				if(result == null)
				{
					return null;
				}
				result = result.mod(m);
			}
			else
			{
				RsaMutableBigInteger t1 = new RsaMutableBigInteger();
				new RsaMutableBigInteger(a1.multiply(m2)).multiply(new RsaMutableBigInteger(y1), t1);
				RsaMutableBigInteger t2 = new RsaMutableBigInteger();
				new RsaMutableBigInteger(a2.multiply(m1)).multiply(new RsaMutableBigInteger(y2), t2);
				t1.add(t2);
				RsaMutableBigInteger q = new RsaMutableBigInteger();
				RsaMutableBigInteger r = t1.divide(new RsaMutableBigInteger(m), q);
				if(r == null)
				{
					return null;
				}
				result = r.toBigInteger();
				if(result == null)
				{
					return null;
				}
			}
		}
		return (invertResult ? result.modInverse(m) : result);
	}
	
	private RsaBigInteger oddModPow(RsaBigInteger y, RsaBigInteger z)
	{
		if((y == null) || (z == null) || (y.mag.length <= 0) || (z.mag.length <= 0))
		{
			return null;
		}
		
		if(y.equals(ONE))
		{
			return this;
		}
		
		if(signum == 0)
		{
			return ZERO;
		}
		
		int[] base = mag.clone();
		int[] exp = y.mag;
		int[] mod = z.mag;
		int modLen = mod.length;
		
		int wbits = 0;
		int ebits = bitLength(exp, exp.length);
		
		if(ebits <= 0)
		{
			return null;
		}
		
		if((ebits != 17) || (exp[0] != 65537))
		{
			while(ebits > bnExpModThreshTable[wbits])
			{
				wbits++;
			}
		}
		
		int tblmask = 1 << wbits;
		
		int[][] table = new int[tblmask][];
		for(int i = 0; i < tblmask; i++)
		{
			table[i] = new int[modLen];
		}
		
		int inv = -RsaMutableBigInteger.inverseMod32(mod[modLen - 1]);
		int[] a = leftShift(base, base.length, modLen << 5);
		
		RsaMutableBigInteger q = new RsaMutableBigInteger(), a2 = new RsaMutableBigInteger(a), b2 = new RsaMutableBigInteger(mod);
		RsaMutableBigInteger r = a2.divide(b2, q);
		if(r == null)
		{
			return null;
		}
		table[0] = r.toIntArray();
		
		if(table[0].length < modLen)
		{
			int offset = modLen - table[0].length;
			int[] t2 = new int[modLen];
			//noinspection ManualArrayCopy
			for(int i = 0; i < table[0].length; i++)
			{
				t2[i + offset] = table[0][i];
			}
			table[0] = t2;
		}
		
		int[] b = squareToLen(table[0], modLen, null);
		b = montReduce(b, mod, modLen, inv);
		
		int[] t = Arrays.copyOf(b, modLen);
		
		for(int i = 1; i < tblmask; i++)
		{
			int[] prod = multiplyToLen(t, modLen, table[i - 1], modLen, null);
			table[i] = montReduce(prod, mod, modLen, inv);
		}
		
		int bitpos = 1 << ((ebits - 1) & (32 - 1));
		
		int buf = 0;
		int elen = exp.length;
		int eIndex = 0;
		for(int i = 0; i <= wbits; i++)
		{
			buf = (buf << 1) | (((exp[eIndex] & bitpos) != 0) ? 1 : 0);
			bitpos = si(bitpos, 1);
			if(bitpos == 0)
			{
				eIndex++;
				bitpos = 1 << (32 - 1);
				elen--;
			}
		}
		
		ebits--;
		boolean isone = true;
		
		int multpos = ebits - wbits;
		if(buf == 0)
		{
			return null;
		}
		while((buf & 1) == 0)
		{
			buf = si(buf, 1);
			multpos++;
		}
		
		int[] mult = table[si(buf, 1)];
		
		buf = 0;
		if(multpos == ebits)
		{
			isone = false;
		}
		
		while(true)
		{
			ebits--;
			
			buf <<= 1;
			
			if(elen != 0)
			{
				buf |= ((exp[eIndex] & bitpos) != 0) ? 1 : 0;
				bitpos = si(bitpos, 1);
				if(bitpos == 0)
				{
					eIndex++;
					bitpos = 1 << (32 - 1);
					elen--;
				}
			}
			
			if((buf & tblmask) != 0)
			{
				multpos = ebits - wbits;
				if(buf == 0)
				{
					return null;
				}
				while((buf & 1) == 0)
				{
					buf = si(buf, 1);
					multpos++;
				}
				mult = table[si(buf, 1)];
				buf = 0;
			}
			
			if(ebits == multpos)
			{
				if(isone)
				{
					b = mult.clone();
					isone = false;
				}
				else
				{
					t = b;
					a = multiplyToLen(t, modLen, mult, modLen, a);
					a = montReduce(a, mod, modLen, inv);
					t = a;
					a = b;
					b = t;
				}
			}
			
			if(ebits <= 0)
			{
				break;
			}
			
			if(!isone)
			{
				t = b;
				a = squareToLen(t, modLen, a);
				a = montReduce(a, mod, modLen, inv);
				t = a;
				a = b;
				b = t;
			}
		}
		
		int[] t2 = new int[2 * modLen];
		System.arraycopy(b, 0, t2, modLen, modLen);
		
		b = montReduce(t2, mod, modLen, inv);
		
		t2 = Arrays.copyOf(b, modLen);
		
		return new RsaBigInteger(1, t2);
	}
	
	private static int[] montReduce(int[] n, int[] mod, int mlen, int inv)
	{
		if((n == null) || (mod == null))
		{
			return new int[0];
		}
		
		int c = 0;
		int len = mlen;
		int offset = 0;
		
		do
		{
			int nEnd = n[n.length - 1 - offset];
			int carry = mulAdd(n, mod, offset, mlen, inv * nEnd);
			c += addOne(n, offset, mlen, carry);
			offset++;
			len--;
		}
		while(len > 0);
		
		while(c > 0)
		{
			int add = subN(n, mod, mlen);
			if(add == 0)
			{
				return new int[0];
			}
			c += add;
		}
		
		while(intArrayCmpToLen(n, mod, mlen) >= 0)
		{
			subN(n, mod, mlen);
		}
		
		return n;
	}
	
	private static int intArrayCmpToLen(int[] arg1, int[] arg2, int len)
	{
		if((arg1 == null) || (arg2 == null) || (len > arg1.length) || (len > arg2.length))
		{
			return 0;
		}
		
		for(int i = 0; i < len; i++)
		{
			long b1 = arg1[i] & LONG_MASK;
			long b2 = arg2[i] & LONG_MASK;
			if(b1 < b2)
			{
				return -1;
			}
			if(b1 > b2)
			{
				return 1;
			}
		}
		return 0;
	}
	
	private static int subN(int[] a, int[] b, int len)
	{
		if((a == null) || (b == null) || (len > a.length) || (len > b.length))
		{
			return 0;
		}
		
		long sum = 0;
		len--;
		while(len >= 0)
		{
			sum = ((a[len] & LONG_MASK) - (b[len] & LONG_MASK)) + (sum >> 32);
			a[len] = (int) sum;
			len--;
		}
		return (int) (sum >> 32);
	}
	
	private static int mulAdd(int[] out, int[] in, int offset, int len, int k)
	{
		if((out == null) || (in == null) || (len > out.length) || (len > in.length))
		{
			return 0;
		}
		
		long kLong = k & LONG_MASK;
		long carry = 0;
		
		offset = out.length - offset - 1;
		for(int j = len - 1; j >= 0; j--)
		{
			long product = ((in[j] & LONG_MASK) * kLong) + (out[offset] & LONG_MASK) + carry;
			out[offset] = (int) product;
			offset--;
			carry = sl(product, 32);
		}
		return (int) carry;
	}
	
	private static int addOne(int[] a, int offset, int mlen, int carry)
	{
		if((a == null) || ((a.length - 1 - mlen - offset) >= a.length) || ((a.length - 1 - mlen - offset) < 0))
		{
			return 0;
		}
		
		offset = a.length - 1 - mlen - offset;
		long t = (a[offset] & LONG_MASK) + (carry & LONG_MASK);
		
		a[offset] = (int) t;
		if(sl(t, 32) == 0)
		{
			return 0;
		}
		mlen--;
		while(mlen >= 0)
		{
			offset--;
			if(offset < 0)
			{
				return 1;
			}
			else
			{
				a[offset] = a[offset] + 1;
				if(a[offset] != 0)
				{
					return 0;
				}
			}
			mlen--;
		}
		return 1;
	}
	
	private RsaBigInteger modPow2(RsaBigInteger exponent, int p)
	{
		if(exponent == null)
		{
			return null;
		}
		
		RsaBigInteger result = ONE;
		RsaBigInteger baseToPow2 = this.mod2(p);
		if(baseToPow2 == null)
		{
			return null;
		}
		
		int expOffset = 0;
		int limit = exponent.bitLength();
		
		if(this.testBit(0))
		{
			limit = (p - 1) < limit ? (p - 1) : limit;
		}
		
		while(expOffset < limit)
		{
			if(exponent.testBit(expOffset))
			{
				result = result.multiply(baseToPow2);
				if(result == null)
				{
					return null;
				}
				result = result.mod2(p);
				if(result == null)
				{
					return null;
				}
			}
			expOffset++;
			if(expOffset < limit)
			{
				baseToPow2 = baseToPow2.square();
				if(baseToPow2 == null)
				{
					return null;
				}
				baseToPow2 = baseToPow2.mod2(p);
				if(baseToPow2 == null)
				{
					return null;
				}
			}
		}
		
		return result;
	}
	
	private RsaBigInteger mod2(int p)
	{
		if(bitLength() <= p)
		{
			return this;
		}
		
		int numInts = si((p + 31), 5);
		if(numInts > this.mag.length)
		{
			return null;
		}
		
		int[] mag = new int[numInts];
		System.arraycopy(this.mag, (this.mag.length - numInts), mag, 0, numInts);
		
		int excessBits = (numInts << 5) - p;
		mag[0] &= (1L << (32 - excessBits)) - 1;
		
		return (mag[0] == 0 ? new RsaBigInteger(1, mag) : new RsaBigInteger(mag, 1));
	}
	
	public RsaBigInteger modInverse(RsaBigInteger m)
	{
		if(m == null)
		{
			return null;
		}
		if(m.signum != 1)
		{
			//throw new ArithmeticException("BigInteger: modulus not positive");
			return null;
		}
		if(m.equals(ONE))
		{
			return ZERO;
		}
		
		RsaBigInteger modVal = this;
		if((signum < 0) || (this.compareMagnitude(m) >= 0))
		{
			modVal = this.mod(m);
			if(modVal == null)
			{
				return null;
			}
		}
		
		if(modVal.equals(ONE))
		{
			return ONE;
		}
		
		RsaMutableBigInteger a = new RsaMutableBigInteger(modVal);
		RsaMutableBigInteger b = new RsaMutableBigInteger(m);
		
		RsaMutableBigInteger result = a.mutableModInverse(b);
		if(result == null)
		{
			return null;
		}
		return result.toBigInteger(1);
	}
	
	private RsaBigInteger shiftLeft(int n)
	{
		if(signum == 0)
		{
			return ZERO;
		}
		if(n > 0)
		{
			return new RsaBigInteger(shiftLeft(mag, n), signum);
		}
		else if(n == 0)
		{
			return this;
		}
		else
		{
			return shiftRightImpl(-n);
		}
	}
	
	private static int[] shiftLeft(int[] mag, int n)
	{
		if(mag == null)
		{
			return new int[0];
		}
		
		int nInts = si(n, 5);
		int nBits = n & 0x1f;
		int magLen = mag.length;
		int[] newMag;
		
		if(nBits == 0)
		{
			newMag = new int[magLen + nInts];
			System.arraycopy(mag, 0, newMag, 0, magLen);
		}
		else
		{
			if(mag.length <= 0)
			{
				return new int[0];
			}
			
			int i = 0;
			int nBits2 = 32 - nBits;
			int highBits = si(mag[0], nBits2);
			if(highBits != 0)
			{
				newMag = new int[magLen + nInts + 1];
				newMag[i] = highBits;
				i++;
			}
			else
			{
				newMag = new int[magLen + nInts];
			}
			int j = 0;
			while(j < (magLen - 1))
			{
				newMag[i] = (mag[j] << nBits) | si(mag[j + 1], nBits2);
				i++;
				j++;
			}
			newMag[i] = mag[j] << nBits;
		}
		return newMag;
	}
	
	private RsaBigInteger shiftRight(int n)
	{
		if(signum == 0)
		{
			return ZERO;
		}
		if(n > 0)
		{
			return shiftRightImpl(n);
		}
		else if(n == 0)
		{
			return this;
		}
		else
		{
			return new RsaBigInteger(shiftLeft(mag, -n), signum);
		}
	}
	
	private RsaBigInteger shiftRightImpl(int n)
	{
		int nInts = si(n, 5);
		int nBits = n & 0x1f;
		int magLen = mag.length;
		int[] newMag;
		
		if(nInts >= magLen)
		{
			return (signum >= 0 ? ZERO : NEGATIVE_ONE);
		}
		
		if(nBits == 0)
		{
			int newMagLen = magLen - nInts;
			newMag = Arrays.copyOf(mag, newMagLen);
		}
		else
		{
			int i = 0;
			int highBits = si(mag[0], nBits);
			if(highBits != 0)
			{
				newMag = new int[magLen - nInts];
				newMag[i] = highBits;
				i++;
			}
			else
			{
				newMag = new int[magLen - nInts - 1];
			}
			
			int nBits2 = 32 - nBits;
			int j = 0;
			while(j < (magLen - nInts - 1))
			{
				newMag[i] = (mag[j] << nBits2) | si(mag[j + 1], nBits);
				i++;
				j++;
			}
		}
		
		if(signum < 0)
		{
			boolean onesLost = false;
			for(int i = magLen - 1, j = magLen - nInts; (i >= j) && !onesLost; i--)
			{
				onesLost = (mag[i] != 0);
			}
			if(!onesLost && (nBits != 0))
			{
				onesLost = ((mag[magLen - nInts - 1] << (32 - nBits)) != 0);
			}
			
			if(onesLost)
			{
				newMag = javaIncrement(newMag);
			}
		}
		
		return new RsaBigInteger(newMag, signum);
	}
	
	private int[] javaIncrement(int[] val)
	{
		if(val == null)
		{
			return new int[0];
		}
		
		int lastSum = 0;
		for(int i = val.length - 1; (i >= 0) && (lastSum == 0); i--)
		{
			lastSum = (val[i] += 1);
		}
		if(lastSum == 0)
		{
			val = new int[val.length + 1];
			val[0] = 1;
		}
		return val;
	}
	
	
	private boolean testBit(int n)
	{
		if(n < 0)
		{
			//throw new ArithmeticException("Negative bit address");
			return false;
		}
		
		return (getInt(si(n, 5)) & (1 << (n & 31))) != 0;
	}
	
	private RsaBigInteger setBit(int n)
	{
		if(n < 0)
		{
			//throw new ArithmeticException("Negative bit address");
			return null;
		}
		
		int intNum = si(n, 5);
		int[] result = new int[Math.max(intLength(), intNum + 2)];
		
		for(int i = 0; i < result.length; i++)
		{
			result[result.length - i - 1] = getInt(i);
		}
		
		result[result.length - intNum - 1] |= (1 << (n & 31));
		
		return valueOf(result);
	}
	
	private int getLowestSetBit()
	{
		int lsb = lowestSetBit - 2;
		if(lsb == -2)
		{
			lsb = 0;
			if(signum == 0)
			{
				lsb -= 1;
			}
			else
			{
				int i, b;
				for(i = 0; (b = getInt(i)) == 0; i++)
				{
					// run till b != 0
				}
				lsb += (i << 5) + integerNumberOfTrailingZeros(b);
			}
			lowestSetBit = lsb + 2;
		}
		return lsb;
	}
	
	public int bitLength()
	{
		int n = bitLength - 1;
		if(n == -1)
		{
			int len = mag.length;
			if(len == 0)
			{
				n = 0;
			}
			else
			{
				int magBitLength = ((len - 1) << 5) + bitLengthForInt(mag[0]);
				if(signum < 0)
				{
					boolean pow2 = (integerBitCount(mag[0]) == 1);
					for(int i = 1; (i < len) && pow2; i++)
					{
						pow2 = (mag[i] == 0);
					}
					
					n = (pow2 ? magBitLength - 1 : magBitLength);
				}
				else
				{
					n = magBitLength;
				}
			}
			bitLength = n + 1;
		}
		return n;
	}
	
	
	public int compareTo(RsaBigInteger val)
	{
		if(val == null)
		{
			return 0;
		}
		if(signum == val.signum)
		{
			switch(signum)
			{
				case 1:
					return compareMagnitude(val);
				case -1:
					return val.compareMagnitude(this);
				default:
					return 0;
			}
		}
		return signum > val.signum ? 1 : -1;
	}
	
	private final int compareMagnitude(RsaBigInteger val)
	{
		if(val == null)
		{
			return 0;
		}
		int[] m1 = mag;
		int len1 = m1.length;
		int[] m2 = val.mag;
		int len2 = m2.length;
		if(len1 < len2)
		{
			return -1;
		}
		if(len1 > len2)
		{
			return 1;
		}
		for(int i = 0; i < len1; i++)
		{
			int a = m1[i];
			int b = m2[i];
			if(a != b)
			{
				return ((a & LONG_MASK) < (b & LONG_MASK)) ? -1 : 1;
			}
		}
		return 0;
	}
	
	public byte[] toByteArray()
	{
		int byteLen = (bitLength() / 8) + 1;
		byte[] byteArray = new byte[byteLen];
		
		for(int i = byteLen - 1, bytesCopied = 4, nextInt = 0, intIndex = 0; i >= 0; i--)
		{
			if(bytesCopied == 4)
			{
				nextInt = getInt(intIndex);
				intIndex++;
				bytesCopied = 1;
			}
			else
			{
				nextInt = si(nextInt, 8);
				bytesCopied++;
			}
			byteArray[i] = (byte) nextInt;
		}
		
		if(byteArray[0] == 0)
		{
			byte[] tmp = new byte[byteArray.length - 1];
			System.arraycopy(byteArray, 1, tmp, 0, tmp.length);
			byteArray = tmp;
		}
		return byteArray;
	}
	
	public int intValue()
	{
		int result;
		result = getInt(0);
		return result;
	}
	
	
	private static int[] stripLeadingZeroInts(int[] val)
	{
		if(val == null)
		{
			return new int[0];
		}
		
		int vlen = val.length;
		int keep;
		
		for(keep = 0; (keep < vlen) && (val[keep] == 0); keep++)
		{
			// run till keep finds a non-zero value
		}
		return Arrays.copyOfRange(val, keep, vlen);
	}
	
	private static int[] trustedStripLeadingZeroInts(int[] val)
	{
		if(val == null)
		{
			return new int[0];
		}
		
		int vlen = val.length;
		int keep;
		
		for(keep = 0; (keep < vlen) && (val[keep] == 0); keep++)
		{
			// run till keep finds a non-zero value
		}
		return keep == 0 ? val : Arrays.copyOfRange(val, keep, vlen);
	}
	
	private static int[] stripLeadingZeroBytes(byte[] a)
	{
		if(a == null)
		{
			return new int[0];
		}
		
		int byteLength = a.length;
		int keep;
		
		for(keep = 0; (keep < byteLength) && (a[keep] == 0); keep++)
		{
			// run till keep finds a non-zero value
		}
		
		int intLength = si(((byteLength - keep) + 3), 2);
		int[] result = new int[intLength];
		int b = byteLength - 1;
		for(int i = intLength - 1; i >= 0; i--)
		{
			result[i] = a[b] & 0xff;
			b--;
			int bytesRemaining = (b - keep) + 1;
			int bytesToTransfer = Math.min(3, bytesRemaining);
			for(int j = 8; j <= (bytesToTransfer << 3); j += 8)
			{
				result[i] |= ((a[b] & 0xff) << j);
				b--;
			}
		}
		return result;
	}
	
	private static int[] makePositive(int[] a)
	{
		if(a == null)
		{
			return new int[0];
		}
		
		int keep, j;
		
		for(keep = 0; (keep < a.length) && (a[keep] == -1); keep++)
		{
			// run till keep finds a non-minus-one value
		}
		
		for(j = keep; (j < a.length) && (a[j] == 0); j++)
		{
			// run till j finds a non-zero value
		}
		int extraInt = (j == a.length ? 1 : 0);
		int[] result = new int[(a.length - keep) + extraInt];
		
		for(int i = keep; i < a.length; i++)
		{
			result[(i - keep) + extraInt] = ~a[i];
		}
		
		int i = result.length - 1;
		result[i] = result[i] + 1;
		while(result[i] == 0)
		{
			i--;
			result[i] = result[i] + 1;
		}
		
		return result;
	}
	
	
	private int intLength()
	{
		return si(bitLength(), 5) + 1;
	}
	
	private int signInt()
	{
		return signum < 0 ? -1 : 0;
	}
	
	private int getInt(int n)
	{
		if(n < 0)
		{
			return 0;
		}
		if(n >= mag.length)
		{
			return signInt();
		}
		
		int index = mag.length - n - 1;
		if(/* (index < 0) || */ index >= mag.length)
		{
			return 0;
		}
		
		int magInt = mag[index];
		
		return (signum >= 0 ? magInt : (n <= firstNonzeroIntNum() ? -magInt : ~magInt));
	}
	
	private int firstNonzeroIntNum()
	{
		int fn = firstNonzeroIntNum - 2;
		if(fn == -2)
		{
			int i;
			int mlen = mag.length;
			for(i = mlen - 1; (i >= 0) && (mag[i] == 0); i--)
			{
				// run till i finds a non-zero value
			}
			fn = mlen - i - 1;
			firstNonzeroIntNum = fn + 2;
		}
		return fn;
	}
	
	
	@Override
	public boolean equals(Object x)
	{
		if(x == null)
		{
			return false;
		}
		
		if(x == this)
		{
			return true;
		}
		
		if(!(x instanceof RsaBigInteger))
		{
			return false;
		}
		
		RsaBigInteger xInt = (RsaBigInteger) x;
		if(xInt.signum != signum)
		{
			return false;
		}
		
		int[] m = mag;
		int len = m.length;
		int[] xm = xInt.mag;
		if(len != xm.length)
		{
			return false;
		}
		
		for(int i = 0; i < len; i++)
		{
			if(xm[i] != m[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		int hashCode = 0;
		//noinspection ForLoopReplaceableByForEach
		for(int i = 0; i < mag.length; i++)
		{
			hashCode = (int) ((31 * hashCode) + (mag[i] & LONG_MASK));
		}
		return hashCode * signum;
	}
}
