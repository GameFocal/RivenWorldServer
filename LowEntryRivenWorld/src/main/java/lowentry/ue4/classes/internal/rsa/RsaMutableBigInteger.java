package lowentry.ue4.classes.internal.rsa;


import java.util.Arrays;


class RsaMutableBigInteger
{
	private int[] value;
	public  int   intLen;
	private int   offset = 0;
	
	private static final RsaMutableBigInteger ONE = new RsaMutableBigInteger(1);
	
	private static final int KNUTH_POW2_THRESH_LEN   = 6;
	private static final int KNUTH_POW2_THRESH_ZEROS = 3;
	
	private final static long LONG_MASK = 0xffffffffL;
	
	
	public RsaMutableBigInteger()
	{
		value = new int[1];
		intLen = 0;
	}
	
	public RsaMutableBigInteger(int val)
	{
		value = new int[1];
		intLen = 1;
		value[0] = val;
	}
	public RsaMutableBigInteger(int[] val)
	{
		if(val == null)
		{
			value = new int[1];
			intLen = 0;
			return;
		}
		value = val;
		intLen = val.length;
	}
	
	public RsaMutableBigInteger(RsaBigInteger b)
	{
		if(b == null)
		{
			value = new int[1];
			intLen = 0;
			return;
		}
		intLen = b.mag.length;
		value = Arrays.copyOf(b.mag, intLen);
	}
	
	@SuppressWarnings("CopyConstructorMissesField")
	public RsaMutableBigInteger(RsaMutableBigInteger val)
	{
		if(val == null)
		{
			value = new int[1];
			intLen = 0;
			return;
		}
		intLen = val.intLen;
		value = Arrays.copyOfRange(val.value, val.offset, val.offset + intLen);
	}
	
	private static int si(int a, int b)
	{
		return (a >>> b);
	}
	private static long sl(long a, int b)
	{
		return (a >>> b);
	}
	
	private void ones(int n)
	{
		if(n > value.length)
		{
			value = new int[n];
		}
		Arrays.fill(value, -1);
		offset = 0;
		intLen = n;
	}
	
	private int[] getMagnitudeArray()
	{
		if((offset > 0) || (value.length != intLen))
		{
			return Arrays.copyOfRange(value, offset, offset + intLen);
		}
		return value;
	}
	
	public RsaBigInteger toBigInteger(int sign)
	{
		if((intLen == 0) || (sign == 0))
		{
			return RsaBigInteger.ZERO;
		}
		return new RsaBigInteger(getMagnitudeArray(), sign);
	}
	
	public RsaBigInteger toBigInteger()
	{
		normalize();
		return toBigInteger(isZero() ? 0 : 1);
	}
	
	private void clear()
	{
		offset = intLen = 0;
		for(int index = 0, n = value.length; index < n; index++)
		{
			value[index] = 0;
		}
	}
	
	private void reset()
	{
		offset = intLen = 0;
	}
	
	private final int compare(RsaMutableBigInteger b)
	{
		if(b == null)
		{
			return 0;
		}
		
		int blen = b.intLen;
		if(intLen < blen)
		{
			return -1;
		}
		if(intLen > blen)
		{
			return 1;
		}
		
		int[] bval = b.value;
		for(int i = offset, j = b.offset; i < (intLen + offset); i++, j++)
		{
			int b1 = addSignedInts(value[i], 0x80000000);
			int b2 = addSignedInts(bval[j], 0x80000000);
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
	
	private int compareShifted(RsaMutableBigInteger b, int ints)
	{
		if(b == null)
		{
			return 0;
		}
		
		int blen = b.intLen;
		int alen = intLen - ints;
		if(alen < blen)
		{
			return -1;
		}
		if(alen > blen)
		{
			return 1;
		}
		
		int[] bval = b.value;
		for(int i = offset, j = b.offset; i < (alen + offset); i++, j++)
		{
			int b1 = addSignedInts(value[i], 0x80000000);
			int b2 = addSignedInts(bval[j], 0x80000000);
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
	
	private final int getLowestSetBit()
	{
		if(intLen == 0)
		{
			return -1;
		}
		int j = intLen - 1;
		while((j > 0) && (value[j + offset] == 0))
		{
			j--;
		}
		int b = value[j + offset];
		if(b == 0)
		{
			return -1;
		}
		return ((intLen - 1 - j) << 5) + integerNumberOfTrailingZeros(b);
	}
	
	private final void normalize()
	{
		if(intLen == 0)
		{
			offset = 0;
			return;
		}
		
		int index = offset;
		if(value[index] != 0)
		{
			return;
		}
		
		int indexBound = index + intLen;
		index++;
		while((index < indexBound) && (value[index] == 0))
		{
			index++;
		}
		
		int numZeros = index - offset;
		intLen -= numZeros;
		offset = (intLen == 0 ? 0 : offset + numZeros);
	}
	
	public int[] toIntArray()
	{
		int[] result = new int[intLen];
		//noinspection ManualArrayCopy
		for(int i = 0; i < intLen; i++)
		{
			result[i] = value[offset + i];
		}
		return result;
	}
	
	private void setValue(int[] val, int length)
	{
		if((val == null) || (val.length <= 0))
		{
			val = new int[1];
		}
		value = val;
		intLen = length;
		offset = 0;
	}
	
	private void copyValue(RsaMutableBigInteger src)
	{
		if(src == null)
		{
			return;
		}
		int len = src.intLen;
		if(value.length < len)
		{
			value = new int[len];
		}
		System.arraycopy(src.value, src.offset, value, 0, len);
		intLen = len;
		offset = 0;
	}
	
	private boolean isOne()
	{
		return (intLen == 1) && (value[offset] == 1);
	}
	
	public boolean isZero()
	{
		return (intLen == 0);
	}
	
	private boolean isEven()
	{
		return (intLen == 0) || ((value[(offset + intLen) - 1] & 1) == 0);
	}
	
	private boolean isOdd()
	{
		return !isZero() && ((value[(offset + intLen) - 1] & 1) == 1);
	}
	
	private void safeRightShift(int n)
	{
		if((n / 32) >= intLen)
		{
			reset();
		}
		else
		{
			rightShift(n);
		}
	}
	
	private void rightShift(int n)
	{
		if(intLen == 0)
		{
			return;
		}
		int nInts = si(n, 5);
		int nBits = n & 0x1F;
		this.intLen -= nInts;
		if(nBits == 0)
		{
			return;
		}
		int bitsInHighWord = bitLengthForInt(value[offset]);
		if(nBits >= bitsInHighWord)
		{
			this.primitiveLeftShift(32 - nBits);
			this.intLen--;
		}
		else
		{
			primitiveRightShift(nBits);
		}
	}
	
	private void safeLeftShift(int n)
	{
		if(n > 0)
		{
			leftShift(n);
		}
	}
	
	public void leftShift(int n)
	{
		if(intLen == 0)
		{
			return;
		}
		int nInts = si(n, 5);
		int nBits = n & 0x1F;
		int bitsInHighWord = bitLengthForInt(value[offset]);
		
		if(n <= (32 - bitsInHighWord))
		{
			primitiveLeftShift(nBits);
			return;
		}
		
		int newLen = intLen + nInts + 1;
		if(nBits <= (32 - bitsInHighWord))
		{
			newLen--;
		}
		if(value.length < newLen)
		{
			int[] result = new int[newLen];
			//noinspection ManualArrayCopy
			for(int i = 0; i < intLen; i++)
			{
				result[i] = value[offset + i];
			}
			setValue(result, newLen);
		}
		else if((value.length - offset) >= newLen)
		{
			for(int i = 0; i < (newLen - intLen); i++)
			{
				value[offset + intLen + i] = 0;
			}
		}
		else
		{
			//noinspection ManualArrayCopy
			for(int i = 0; i < intLen; i++)
			{
				value[i] = value[offset + i];
			}
			for(int i = intLen; i < newLen; i++)
			{
				value[i] = 0;
			}
			offset = 0;
		}
		intLen = newLen;
		if(nBits == 0)
		{
			return;
		}
		if(nBits <= (32 - bitsInHighWord))
		{
			primitiveLeftShift(nBits);
		}
		else
		{
			primitiveRightShift(32 - nBits);
		}
	}
	
	private int divadd(int[] a, int[] result, int offset)
	{
		if((a == null) || (result == null))
		{
			return 0;
		}
		
		long carry = 0;
		
		for(int j = a.length - 1; j >= 0; j--)
		{
			long sum = (a[j] & LONG_MASK) + (result[j + offset] & LONG_MASK) + carry;
			result[j + offset] = (int) sum;
			carry = sl(sum, 32);
		}
		return (int) carry;
	}
	
	private int mulsub(int[] q, int[] a, int x, int len, int offset)
	{
		if((q == null) || (a == null))
		{
			return 0;
		}
		
		long xLong = x & LONG_MASK;
		long carry = 0;
		offset += len;
		
		for(int j = len - 1; j >= 0; j--)
		{
			long product = ((a[j] & LONG_MASK) * xLong) + carry;
			long difference = q[offset] - product;
			q[offset] = (int) difference;
			offset--;
			carry = sl(product, 32) + (((difference & LONG_MASK) > (((~(int) product) & LONG_MASK))) ? 1 : 0);
		}
		return (int) carry;
	}
	
	private int mulsubBorrow(int[] q, int[] a, int x, int len, int offset)
	{
		if((q == null) || (a == null))
		{
			return 0;
		}
		
		long xLong = x & LONG_MASK;
		long carry = 0;
		offset += len;
		for(int j = len - 1; j >= 0; j--)
		{
			long product = ((a[j] & LONG_MASK) * xLong) + carry;
			long difference = q[offset] - product;
			offset--;
			carry = sl(product, 32) + (((difference & LONG_MASK) > (((~(int) product) & LONG_MASK))) ? 1 : 0);
		}
		return (int) carry;
	}
	
	private final void primitiveRightShift(int n)
	{
		int n2 = 32 - n;
		for(int i = (offset + intLen) - 1, c = value[i]; i > offset; i--)
		{
			int b = c;
			c = value[i - 1];
			value[i] = (c << n2) | si(b, n);
		}
		value[offset] = si(value[offset], n);
	}
	
	private final void primitiveLeftShift(int n)
	{
		int n2 = 32 - n;
		for(int i = offset, c = value[i], m = (i + intLen) - 1; i < m; i++)
		{
			int b = c;
			c = value[i + 1];
			value[i] = (b << n) | si(c, n2);
		}
		value[(offset + intLen) - 1] <<= n;
	}
	
	private RsaBigInteger getLower(int n)
	{
		if(isZero())
		{
			return RsaBigInteger.ZERO;
		}
		else if(intLen < n)
		{
			return toBigInteger(1);
		}
		else
		{
			int len = n;
			while((len > 0) && (value[(offset + intLen) - len] == 0))
			{
				len--;
			}
			int sign = len > 0 ? 1 : 0;
			return new RsaBigInteger(Arrays.copyOfRange(value, (offset + intLen) - len, offset + intLen), sign);
		}
	}
	
	private void keepLower(int n)
	{
		if(intLen >= n)
		{
			offset += intLen - n;
			intLen = n;
		}
	}
	
	public void add(RsaMutableBigInteger addend)
	{
		if(addend == null)
		{
			return;
		}
		
		int x = intLen;
		int y = addend.intLen;
		int resultLen = (intLen > addend.intLen ? intLen : addend.intLen);
		int[] result = (value.length < resultLen ? new int[resultLen] : value);
		
		int rstart = result.length - 1;
		long sum;
		long carry = 0;
		
		while((x > 0) && (y > 0))
		{
			x--;
			y--;
			sum = (value[x + offset] & LONG_MASK) + (addend.value[y + addend.offset] & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		
		while(x > 0)
		{
			x--;
			if((carry == 0) && (result == value) && (rstart == (x + offset)))
			{
				return;
			}
			sum = (value[x + offset] & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		while(y > 0)
		{
			y--;
			sum = (addend.value[y + addend.offset] & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		
		if(carry > 0)
		{
			resultLen++;
			if(result.length < resultLen)
			{
				int[] temp = new int[resultLen];
				System.arraycopy(result, 0, temp, 1, result.length);
				temp[0] = 1;
				result = temp;
			}
			else
			{
				result[rstart] = 1;
			}
		}
		
		value = result;
		intLen = resultLen;
		offset = result.length - resultLen;
	}
	
	private void addShifted(RsaMutableBigInteger addend, int n)
	{
		if(addend == null)
		{
			return;
		}
		
		if(addend.isZero())
		{
			return;
		}
		
		int x = intLen;
		int y = addend.intLen + n;
		int resultLen = (intLen > y ? intLen : y);
		int[] result = (value.length < resultLen ? new int[resultLen] : value);
		
		int rstart = result.length - 1;
		long sum;
		long carry = 0;
		
		while((x > 0) && (y > 0))
		{
			x--;
			y--;
			int bval = (y + addend.offset) < addend.value.length ? addend.value[y + addend.offset] : 0;
			sum = (value[x + offset] & LONG_MASK) + (bval & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		
		while(x > 0)
		{
			x--;
			if((carry == 0) && (result == value) && (rstart == (x + offset)))
			{
				return;
			}
			sum = (value[x + offset] & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		while(y > 0)
		{
			y--;
			int bval = (y + addend.offset) < addend.value.length ? addend.value[y + addend.offset] : 0;
			sum = (bval & LONG_MASK) + carry;
			result[rstart] = (int) sum;
			rstart--;
			carry = sl(sum, 32);
		}
		
		if(carry > 0)
		{
			resultLen++;
			if(result.length < resultLen)
			{
				int[] temp = new int[resultLen];
				System.arraycopy(result, 0, temp, 1, result.length);
				temp[0] = 1;
				result = temp;
			}
			else
			{
				result[rstart] = 1;
			}
		}
		
		value = result;
		intLen = resultLen;
		offset = result.length - resultLen;
	}
	
	private void addDisjoint(RsaMutableBigInteger addend, int n)
	{
		if(addend == null)
		{
			return;
		}
		
		if(addend.isZero())
		{
			return;
		}
		
		int x = intLen;
		int y = addend.intLen + n;
		int resultLen = (intLen > y ? intLen : y);
		int[] result;
		if(value.length < resultLen)
		{
			result = new int[resultLen];
		}
		else
		{
			result = value;
			Arrays.fill(value, offset + intLen, value.length, 0);
		}
		
		int rstart = result.length - 1;
		
		System.arraycopy(value, offset, result, (rstart + 1) - x, x);
		//noinspection SuspiciousNameCombination
		y -= x;
		rstart -= x;
		
		int len = Math.min(y, addend.value.length - addend.offset);
		System.arraycopy(addend.value, addend.offset, result, (rstart + 1) - y, len);
		
		for(int i = ((rstart + 1) - y) + len; i < (rstart + 1); i++)
		{
			result[i] = 0;
		}
		
		value = result;
		intLen = resultLen;
		offset = result.length - resultLen;
	}
	
	private void addLower(RsaMutableBigInteger addend, int n)
	{
		if(addend == null)
		{
			return;
		}
		
		RsaMutableBigInteger a = new RsaMutableBigInteger(addend);
		if((a.offset + a.intLen) >= n)
		{
			a.offset = (a.offset + a.intLen) - n;
			a.intLen = n;
		}
		a.normalize();
		add(a);
	}
	
	public int subtract(RsaMutableBigInteger b)
	{
		if(b == null)
		{
			return 0;
		}
		
		RsaMutableBigInteger a = this;
		
		int[] result = value;
		int sign = a.compare(b);
		
		if(sign == 0)
		{
			reset();
			return 0;
		}
		if(sign < 0)
		{
			RsaMutableBigInteger tmp = a;
			a = b;
			b = tmp;
		}
		
		int resultLen = a.intLen;
		if(result.length < resultLen)
		{
			result = new int[resultLen];
		}
		
		long diff = 0;
		int x = a.intLen;
		int y = b.intLen;
		int rstart = result.length - 1;
		
		while(y > 0)
		{
			x--;
			y--;
			
			diff = (a.value[x + a.offset] & LONG_MASK) - (b.value[y + b.offset] & LONG_MASK) - ((int) -(diff >> 32));
			result[rstart] = (int) diff;
			rstart--;
		}
		while(x > 0)
		{
			x--;
			diff = (a.value[x + a.offset] & LONG_MASK) - ((int) -(diff >> 32));
			result[rstart] = (int) diff;
			rstart--;
		}
		
		value = result;
		intLen = resultLen;
		offset = value.length - resultLen;
		normalize();
		return sign;
	}
	
	private int difference(RsaMutableBigInteger b)
	{
		if(b == null)
		{
			return 0;
		}
		
		RsaMutableBigInteger a = this;
		int sign = a.compare(b);
		if(sign == 0)
		{
			return 0;
		}
		if(sign < 0)
		{
			RsaMutableBigInteger tmp = a;
			a = b;
			b = tmp;
		}
		
		long diff = 0;
		int x = a.intLen;
		int y = b.intLen;
		
		while(y > 0)
		{
			x--;
			y--;
			diff = (a.value[a.offset + x] & LONG_MASK) - (b.value[b.offset + y] & LONG_MASK) - ((int) -(diff >> 32));
			a.value[a.offset + x] = (int) diff;
		}
		while(x > 0)
		{
			x--;
			diff = (a.value[a.offset + x] & LONG_MASK) - ((int) -(diff >> 32));
			a.value[a.offset + x] = (int) diff;
		}
		
		a.normalize();
		return sign;
	}
	
	public void multiply(RsaMutableBigInteger y, RsaMutableBigInteger z)
	{
		if((y == null) || (z == null))
		{
			return;
		}
		
		int xLen = intLen;
		int yLen = y.intLen;
		int newLen = xLen + yLen;
		
		if(z.value.length < newLen)
		{
			z.value = new int[newLen];
		}
		z.offset = 0;
		z.intLen = newLen;
		
		long carry = 0;
		for(int j = yLen - 1, k = (yLen + xLen) - 1; j >= 0; j--, k--)
		{
			long product = ((y.value[j + y.offset] & LONG_MASK) * (value[(xLen - 1) + offset] & LONG_MASK)) + carry;
			z.value[k] = (int) product;
			carry = sl(product, 32);
		}
		z.value[xLen - 1] = (int) carry;
		
		for(int i = xLen - 2; i >= 0; i--)
		{
			carry = 0;
			for(int j = yLen - 1, k = yLen + i; j >= 0; j--, k--)
			{
				long product = ((y.value[j + y.offset] & LONG_MASK) * (value[i + offset] & LONG_MASK)) + (z.value[k] & LONG_MASK) + carry;
				z.value[k] = (int) product;
				carry = sl(product, 32);
			}
			z.value[i] = (int) carry;
		}
		
		z.normalize();
	}
	
	private void mul(int y, RsaMutableBigInteger z)
	{
		if(z == null)
		{
			return;
		}
		
		if(y == 1)
		{
			z.copyValue(this);
			return;
		}
		
		if(y == 0)
		{
			z.clear();
			return;
		}
		
		long ylong = y & LONG_MASK;
		int[] zval = (z.value.length < (intLen + 1) ? new int[intLen + 1] : z.value);
		long carry = 0;
		for(int i = intLen - 1; i >= 0; i--)
		{
			long product = (ylong * (value[i + offset] & LONG_MASK)) + carry;
			zval[i + 1] = (int) product;
			carry = sl(product, 32);
		}
		
		if(carry == 0)
		{
			z.offset = 1;
			z.intLen = intLen;
		}
		else
		{
			z.offset = 0;
			z.intLen = intLen + 1;
			zval[0] = (int) carry;
		}
		z.value = zval;
	}
	
	public static int bitLengthForInt(int n)
	{
		return 32 - integerNumberOfLeadingZeros(n);
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
	
	public int divideOneWord(int divisor, RsaMutableBigInteger quotient)
	{
		if(quotient == null)
		{
			return 0;
		}
		
		long divisorLong = divisor & LONG_MASK;
		
		if(intLen == 1)
		{
			long dividendValue = value[offset] & LONG_MASK;
			int q = (int) (dividendValue / divisorLong);
			int r = (int) (dividendValue - (q * divisorLong));
			quotient.value[0] = q;
			quotient.intLen = (q == 0) ? 0 : 1;
			quotient.offset = 0;
			return r;
		}
		
		if(quotient.value.length < intLen)
		{
			quotient.value = new int[intLen];
		}
		quotient.offset = 0;
		quotient.intLen = intLen;
		
		int shift = integerNumberOfLeadingZeros(divisor);
		
		int rem = value[offset];
		long remLong = rem & LONG_MASK;
		if(remLong < divisorLong)
		{
			quotient.value[0] = 0;
		}
		else
		{
			quotient.value[0] = (int) (remLong / divisorLong);
			rem = (int) (remLong - (quotient.value[0] * divisorLong));
			remLong = rem & LONG_MASK;
		}
		int xlen = intLen;
		xlen--;
		while(xlen > 0)
		{
			long dividendEstimate = (remLong << 32) | (value[(offset + intLen) - xlen] & LONG_MASK);
			int q;
			if(dividendEstimate >= 0)
			{
				q = (int) (dividendEstimate / divisorLong);
				rem = (int) (dividendEstimate - (q * divisorLong));
			}
			else
			{
				long tmp = divWord(dividendEstimate, divisor);
				q = (int) (tmp & LONG_MASK);
				rem = (int) sl(tmp, 32);
			}
			quotient.value[intLen - xlen] = q;
			remLong = rem & LONG_MASK;
			xlen--;
		}
		
		quotient.normalize();
		if(shift > 0)
		{
			return rem % divisor;
		}
		else
		{
			return rem;
		}
	}
	
	public RsaMutableBigInteger divide(RsaMutableBigInteger b, RsaMutableBigInteger quotient)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		return divide(b, quotient, true);
	}
	
	private RsaMutableBigInteger divide(RsaMutableBigInteger b, RsaMutableBigInteger quotient, boolean needRemainder)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		if((b.intLen < RsaBigInteger.BURNIKEL_ZIEGLER_THRESHOLD) || ((intLen - b.intLen) < RsaBigInteger.BURNIKEL_ZIEGLER_OFFSET))
		{
			return divideKnuth(b, quotient, needRemainder);
		}
		else
		{
			return divideAndRemainderBurnikelZiegler(b, quotient);
		}
	}
	
	public RsaMutableBigInteger divideKnuth(RsaMutableBigInteger b, RsaMutableBigInteger quotient)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		return divideKnuth(b, quotient, true);
	}
	
	private RsaMutableBigInteger divideKnuth(RsaMutableBigInteger b, RsaMutableBigInteger quotient, boolean needRemainder)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		if(b.intLen == 0)
		{
			//throw new ArithmeticException("BigInteger divide by zero");
			return null;
		}
		
		if(intLen == 0)
		{
			quotient.intLen = 0;
			quotient.offset = 0;
			return needRemainder ? new RsaMutableBigInteger() : null;
		}
		
		int cmp = compare(b);
		if(cmp < 0)
		{
			quotient.intLen = 0;
			quotient.offset = 0;
			return needRemainder ? new RsaMutableBigInteger(this) : null;
		}
		if(cmp == 0)
		{
			quotient.value[0] = 1;
			quotient.intLen = 1;
			quotient.offset = 0;
			return needRemainder ? new RsaMutableBigInteger() : null;
		}
		
		quotient.clear();
		if(b.intLen == 1)
		{
			int r = divideOneWord(b.value[b.offset], quotient);
			if(needRemainder)
			{
				if(r == 0)
				{
					return new RsaMutableBigInteger();
				}
				return new RsaMutableBigInteger(r);
			}
			else
			{
				return null;
			}
		}
		
		if(intLen >= KNUTH_POW2_THRESH_LEN)
		{
			int trailingZeroBits = Math.min(getLowestSetBit(), b.getLowestSetBit());
			if(trailingZeroBits >= (KNUTH_POW2_THRESH_ZEROS * 32))
			{
				RsaMutableBigInteger a = new RsaMutableBigInteger(this);
				b = new RsaMutableBigInteger(b);
				a.rightShift(trailingZeroBits);
				b.rightShift(trailingZeroBits);
				RsaMutableBigInteger r = a.divideKnuth(b, quotient);
				if(r == null)
				{
					return null;
				}
				r.leftShift(trailingZeroBits);
				return r;
			}
		}
		
		return divideMagnitude(b, quotient, needRemainder);
	}
	
	public RsaMutableBigInteger divideAndRemainderBurnikelZiegler(RsaMutableBigInteger b, RsaMutableBigInteger quotient)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		
		int r = intLen;
		int s = b.intLen;
		
		quotient.offset = quotient.intLen = 0;
		
		if(r < s)
		{
			return this;
		}
		else
		{
			int m = 1 << (32 - integerNumberOfLeadingZeros(s / RsaBigInteger.BURNIKEL_ZIEGLER_THRESHOLD));
			
			int j = ((s + m) - 1) / m;
			int n = j * m;
			long n32 = 32L * n;
			int sigma = (int) Math.max(0, n32 - b.bitLength());
			RsaMutableBigInteger bShifted = new RsaMutableBigInteger(b);
			bShifted.safeLeftShift(sigma);
			RsaMutableBigInteger aShifted = new RsaMutableBigInteger(this);
			aShifted.safeLeftShift(sigma);
			
			int t = (int) ((aShifted.bitLength() + n32) / n32);
			if(t < 2)
			{
				t = 2;
			}
			
			RsaMutableBigInteger a1 = aShifted.getBlock(t - 1, t, n);
//			if(a1 == null)
//			{
//				return null;
//			}
			
			RsaMutableBigInteger z = aShifted.getBlock(t - 2, t, n);
//			if(z == null)
//			{
//				return null;
//			}
			z.addDisjoint(a1, n);
			
			RsaMutableBigInteger qi = new RsaMutableBigInteger();
			RsaMutableBigInteger ri;
			for(int i = t - 2; i > 0; i--)
			{
				ri = z.divide2n1n(bShifted, qi);
				if(ri == null)
				{
					return null;
				}
				
				z = aShifted.getBlock(i - 1, t, n);
//				if(z == null)
//				{
//					return null;
//				}
				z.addDisjoint(ri, n);
				quotient.addShifted(qi, i * n);
			}
			ri = z.divide2n1n(bShifted, qi);
			if(ri == null)
			{
				return null;
			}
			quotient.add(qi);
			
			ri.rightShift(sigma);
			return ri;
		}
	}
	
	private RsaMutableBigInteger divide2n1n(RsaMutableBigInteger b, RsaMutableBigInteger quotient)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		
		int n = b.intLen;
		
		if(((n % 2) != 0) || (n < RsaBigInteger.BURNIKEL_ZIEGLER_THRESHOLD))
		{
			return divideKnuth(b, quotient);
		}
		
		RsaMutableBigInteger aUpper = new RsaMutableBigInteger(this);
		aUpper.safeRightShift(32 * (n / 2));
		keepLower(n / 2);
		
		RsaMutableBigInteger q1 = new RsaMutableBigInteger();
		RsaMutableBigInteger r1 = aUpper.divide3n2n(b, q1);
		if(r1 == null)
		{
			return null;
		}
		
		addDisjoint(r1, n / 2);
		RsaMutableBigInteger r2 = divide3n2n(b, quotient);
		if(r2 == null)
		{
			return null;
		}
		
		quotient.addDisjoint(q1, n / 2);
		return r2;
	}
	
	private RsaMutableBigInteger divide3n2n(RsaMutableBigInteger b, RsaMutableBigInteger quotient)
	{
		if((b == null) || (quotient == null))
		{
			return null;
		}
		
		int n = b.intLen / 2;
		
		RsaMutableBigInteger a12 = new RsaMutableBigInteger(this);
		a12.safeRightShift(32 * n);
		
		RsaMutableBigInteger b1 = new RsaMutableBigInteger(b);
		b1.safeRightShift(n * 32);
		RsaBigInteger b2 = b.getLower(n);
		if(b2 == null)
		{
			return null;
		}
		
		RsaMutableBigInteger r;
		RsaMutableBigInteger d;
		if(compareShifted(b, n) < 0)
		{
			r = a12.divide2n1n(b1, quotient);
			if(r == null)
			{
				return null;
			}
			RsaBigInteger qbigint = quotient.toBigInteger();
			if(qbigint == null)
			{
				return null;
			}
			qbigint = qbigint.multiply(b2);
			if(qbigint == null)
			{
				return null;
			}
			d = new RsaMutableBigInteger(qbigint);
		}
		else
		{
			quotient.ones(n);
			a12.add(b1);
			b1.leftShift(32 * n);
			a12.subtract(b1);
			r = a12;
			
			d = new RsaMutableBigInteger(b2);
			d.leftShift(32 * n);
			d.subtract(new RsaMutableBigInteger(b2));
		}
		
		r.leftShift(32 * n);
		r.addLower(this, n);
		
		while(r.compare(d) < 0)
		{
			r.add(b);
			quotient.subtract(RsaMutableBigInteger.ONE);
		}
		r.subtract(d);
		
		return r;
	}
	
	private RsaMutableBigInteger getBlock(int index, int numBlocks, int blockLength)
	{
		int blockStart = index * blockLength;
		if(blockStart >= intLen)
		{
			return new RsaMutableBigInteger();
		}
		
		int blockEnd;
		if(index == (numBlocks - 1))
		{
			blockEnd = intLen;
		}
		else
		{
			blockEnd = (index + 1) * blockLength;
		}
		if(blockEnd > intLen)
		{
			return new RsaMutableBigInteger();
		}
		
		int[] newVal = Arrays.copyOfRange(value, (offset + intLen) - blockEnd, (offset + intLen) - blockStart);
		return new RsaMutableBigInteger(newVal);
	}
	
	private long bitLength()
	{
		if(intLen == 0)
		{
			return 0;
		}
		return (intLen * 32L) - integerNumberOfLeadingZeros(value[offset]);
	}
	
	private static void copyAndShift(int[] src, int srcFrom, int srcLen, int[] dst, int dstFrom, int shift)
	{
		if((src == null) || (dst == null))
		{
			return;
		}
		int n2 = 32 - shift;
		int c = src[srcFrom];
		for(int i = 0; i < (srcLen - 1); i++)
		{
			int b = c;
			srcFrom++;
			c = src[srcFrom];
			dst[dstFrom + i] = (b << shift) | si(c, n2);
		}
		dst[(dstFrom + srcLen) - 1] = c << shift;
	}
	
	private RsaMutableBigInteger divideMagnitude(RsaMutableBigInteger div, RsaMutableBigInteger quotient, boolean needRemainder)
	{
		if((div == null) || (quotient == null))
		{
			return null;
		}
		int shift = integerNumberOfLeadingZeros(div.value[div.offset]);
		final int dlen = div.intLen;
		int[] divisor;
		RsaMutableBigInteger rem;
		if(shift > 0)
		{
			divisor = new int[dlen];
			copyAndShift(div.value, div.offset, dlen, divisor, 0, shift);
			if(integerNumberOfLeadingZeros(value[offset]) >= shift)
			{
				int[] remarr = new int[intLen + 1];
				copyAndShift(value, offset, intLen, remarr, 1, shift);
				rem = new RsaMutableBigInteger(remarr);
				rem.intLen = intLen;
				rem.offset = 1;
			}
			else
			{
				int[] remarr = new int[intLen + 2];
				int rFrom = offset;
				int c = 0;
				int n2 = 32 - shift;
				for(int i = 1; i < (intLen + 1); i++, rFrom++)
				{
					int b = c;
					c = value[rFrom];
					remarr[i] = (b << shift) | si(c, n2);
				}
				remarr[intLen + 1] = c << shift;
				rem = new RsaMutableBigInteger(remarr);
				rem.intLen = intLen + 1;
				rem.offset = 1;
			}
		}
		else
		{
			divisor = Arrays.copyOfRange(div.value, div.offset, div.offset + div.intLen);
			rem = new RsaMutableBigInteger(new int[intLen + 1]);
			System.arraycopy(value, offset, rem.value, 1, intLen);
			rem.intLen = intLen;
			rem.offset = 1;
		}
		
		int nlen = rem.intLen;
		
		final int limit = (nlen - dlen) + 1;
		if(quotient.value.length < limit)
		{
			quotient.value = new int[limit];
			quotient.offset = 0;
		}
		quotient.intLen = limit;
		int[] q = quotient.value;
		
		
		//if(rem.intLen == nlen)
		{
			rem.offset = 0;
			rem.value[0] = 0;
			rem.intLen++;
		}
		
		int dh = divisor[0];
		long dhLong = dh & LONG_MASK;
		int dl = divisor[1];
		
		for(int j = 0; j < (limit - 1); j++)
		{
			int qhat;
			int qrem;
			boolean skipCorrection = false;
			int nh = rem.value[j + rem.offset];
			int nh2 = addSignedInts(nh, 0x80000000);
			int nm = rem.value[j + 1 + rem.offset];
			
			if(nh == dh)
			{
				qhat = ~0;
				qrem = nh + nm;
				skipCorrection = addSignedInts(qrem, 0x80000000) < nh2;
			}
			else
			{
				long nChunk = (((long) nh) << 32) | (nm & LONG_MASK);
				if(nChunk >= 0)
				{
					qhat = (int) (nChunk / dhLong);
					qrem = (int) (nChunk - (qhat * dhLong));
				}
				else
				{
					long tmp = divWord(nChunk, dh);
					qhat = (int) (tmp & LONG_MASK);
					qrem = (int) sl(tmp, 32);
				}
			}
			
			if(qhat == 0)
			{
				continue;
			}
			
			if(!skipCorrection)
			{
				long nl = rem.value[j + 2 + rem.offset] & LONG_MASK;
				long rs = ((qrem & LONG_MASK) << 32) | nl;
				long estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);
				
				if(unsignedLongCompare(estProduct, rs))
				{
					qhat--;
					qrem = (int) ((qrem & LONG_MASK) + dhLong);
					if((qrem & LONG_MASK) >= dhLong)
					{
						estProduct -= (dl & LONG_MASK);
						rs = ((qrem & LONG_MASK) << 32) | nl;
						if(unsignedLongCompare(estProduct, rs))
						{
							qhat--;
						}
					}
				}
			}
			
			rem.value[j + rem.offset] = 0;
			int borrow = mulsub(rem.value, divisor, qhat, dlen, j + rem.offset);
			
			if(addSignedInts(borrow, 0x80000000) > nh2)
			{
				divadd(divisor, rem.value, j + 1 + rem.offset);
				qhat--;
			}
			
			q[j] = qhat;
		}
		int qhat;
		int qrem;
		boolean skipCorrection = false;
		int nh = rem.value[(limit - 1) + rem.offset];
		int nh2 = addSignedInts(nh, 0x80000000);
		int nm = rem.value[limit + rem.offset];
		
		if(nh == dh)
		{
			qhat = ~0;
			qrem = nh + nm;
			skipCorrection = addSignedInts(qrem, 0x80000000) < nh2;
		}
		else
		{
			long nChunk = (((long) nh) << 32) | (nm & LONG_MASK);
			if(nChunk >= 0)
			{
				qhat = (int) (nChunk / dhLong);
				qrem = (int) (nChunk - (qhat * dhLong));
			}
			else
			{
				long tmp = divWord(nChunk, dh);
				qhat = (int) (tmp & LONG_MASK);
				qrem = (int) sl(tmp, 32);
			}
		}
		if(qhat != 0)
		{
			if(!skipCorrection)
			{
				long nl = rem.value[limit + 1 + rem.offset] & LONG_MASK;
				long rs = ((qrem & LONG_MASK) << 32) | nl;
				long estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);
				
				if(unsignedLongCompare(estProduct, rs))
				{
					qhat--;
					qrem = (int) ((qrem & LONG_MASK) + dhLong);
					if((qrem & LONG_MASK) >= dhLong)
					{
						estProduct -= (dl & LONG_MASK);
						rs = ((qrem & LONG_MASK) << 32) | nl;
						if(unsignedLongCompare(estProduct, rs))
						{
							qhat--;
						}
					}
				}
			}
			
			int borrow;
			rem.value[(limit - 1) + rem.offset] = 0;
			if(needRemainder)
			{
				borrow = mulsub(rem.value, divisor, qhat, dlen, (limit - 1) + rem.offset);
			}
			else
			{
				borrow = mulsubBorrow(rem.value, divisor, qhat, dlen, (limit - 1) + rem.offset);
			}
			
			if(addSignedInts(borrow, 0x80000000) > nh2)
			{
				if(needRemainder)
				{
					divadd(divisor, rem.value, (limit - 1) + 1 + rem.offset);
				}
				qhat--;
			}
			
			q[(limit - 1)] = qhat;
		}
		
		if(needRemainder)
		{
			if(shift > 0)
			{
				rem.rightShift(shift);
			}
			rem.normalize();
		}
		quotient.normalize();
		return needRemainder ? rem : null;
	}
	
	private static boolean unsignedLongCompare(long one, long two)
	{
		return (one + Long.MIN_VALUE) > (two + Long.MIN_VALUE);
	}
	
	private static int addSignedInts(int one, int two)
	{
		return one + two;
	}
	
	private static long divWord(long n, int d)
	{
		long dLong = d & LONG_MASK;
		long r;
		long q;
		if(dLong == 1)
		{
			q = (int) n;
			r = 0;
			return (r << 32) | (q & LONG_MASK);
		}
		
		q = sl(n, 1) / sl(dLong, 1);
		r = n - (q * dLong);
		
		while(r < 0)
		{
			r += dLong;
			q--;
		}
		while(r >= dLong)
		{
			r -= dLong;
			q++;
		}
		return (r << 32) | (q & LONG_MASK);
	}
	
	public RsaMutableBigInteger hybridGCD(RsaMutableBigInteger b)
	{
		if(b == null)
		{
			return null;
		}
		
		RsaMutableBigInteger a = this;
		RsaMutableBigInteger q = new RsaMutableBigInteger();
		
		while(b.intLen != 0)
		{
			if(Math.abs(a.intLen - b.intLen) < 2)
			{
				return a.binaryGCD(b);
			}
			
			RsaMutableBigInteger r = a.divide(b, q);
			if(r == null)
			{
				return null;
			}
			
			a = b;
			b = r;
		}
		return a;
	}
	
	private RsaMutableBigInteger binaryGCD(RsaMutableBigInteger v)
	{
		if(v == null)
		{
			return null;
		}
		
		RsaMutableBigInteger u = this;
		RsaMutableBigInteger r = new RsaMutableBigInteger();
		
		int s1 = u.getLowestSetBit();
		int s2 = v.getLowestSetBit();
		int k = (s1 < s2) ? s1 : s2;
		if(k != 0)
		{
			u.rightShift(k);
			v.rightShift(k);
		}
		
		boolean uOdd = (k == s1);
		RsaMutableBigInteger t = uOdd ? v : u;
		int tsign = uOdd ? -1 : 1;
		
		int lb = t.getLowestSetBit();
		while(lb >= 0)
		{
			t.rightShift(lb);
			if(tsign > 0)
			{
				//noinspection ConstantConditions
				u = t;
			}
			else
			{
				//noinspection ConstantConditions
				v = t;
			}
			
			if((u.intLen < 2) && (v.intLen < 2))
			{
				int x = u.value[u.offset];
				int y = v.value[v.offset];
				x = binaryGcd(x, y);
				r.value[0] = x;
				r.intLen = 1;
				r.offset = 0;
				if(k > 0)
				{
					r.leftShift(k);
				}
				return r;
			}
			
			tsign = u.difference(v);
			if(tsign == 0)
			{
				break;
			}
			t = (tsign >= 0) ? u : v;
			lb = t.getLowestSetBit();
		}
		
		if(k > 0)
		{
			u.leftShift(k);
		}
		return u;
	}
	
	private static int binaryGcd(int a, int b)
	{
		if(b == 0)
		{
			return a;
		}
		if(a == 0)
		{
			return b;
		}
		
		int aZeros = integerNumberOfTrailingZeros(a);
		int bZeros = integerNumberOfTrailingZeros(b);
		a = si(a, aZeros);
		b = si(b, bZeros);
		
		int t = (aZeros < bZeros ? aZeros : bZeros);
		
		while(a != b)
		{
			if(addSignedInts(a, 0x80000000) > addSignedInts(b, 0x80000000))
			{
				a -= b;
				a = si(a, integerNumberOfTrailingZeros(a));
			}
			else
			{
				b -= a;
				b = si(b, integerNumberOfTrailingZeros(b));
			}
		}
		return a << t;
	}
	
	public RsaMutableBigInteger mutableModInverse(RsaMutableBigInteger p)
	{
		if(p == null)
		{
			return null;
		}
		
		if(p.isOdd())
		{
			return modInverse(p);
		}
		
		if(isEven())
		{
			//throw new ArithmeticException("BigInteger not invertible.");
			return null;
		}
		
		int powersOf2 = p.getLowestSetBit();
		
		RsaMutableBigInteger oddMod = new RsaMutableBigInteger(p);
		oddMod.rightShift(powersOf2);
		
		if(oddMod.isOne())
		{
			return modInverseMP2(powersOf2);
		}
		
		RsaMutableBigInteger oddPart = modInverse(oddMod);
		if(oddPart == null)
		{
			return null;
		}
		
		RsaMutableBigInteger evenPart = modInverseMP2(powersOf2);
		if(evenPart == null)
		{
			return null;
		}
		
		RsaMutableBigInteger y1 = modInverseBP2(oddMod, powersOf2);
		if(y1 == null)
		{
			return null;
		}
		RsaMutableBigInteger y2 = oddMod.modInverseMP2(powersOf2);
		if(y2 == null)
		{
			return null;
		}
		
		RsaMutableBigInteger temp1 = new RsaMutableBigInteger();
		RsaMutableBigInteger temp2 = new RsaMutableBigInteger();
		RsaMutableBigInteger result = new RsaMutableBigInteger();
		
		oddPart.leftShift(powersOf2);
		oddPart.multiply(y1, result);
		
		evenPart.multiply(oddMod, temp1);
		temp1.multiply(y2, temp2);
		
		result.add(temp2);
		return result.divide(p, temp1);
	}
	
	private RsaMutableBigInteger modInverseMP2(int k)
	{
		if(isEven())
		{
			//throw new ArithmeticException("Non-invertible. (GCD != 1)");
			return null;
		}
		
		if(k > 64)
		{
			return euclidModInverse(k);
		}
		
		int t = inverseMod32(value[(offset + intLen) - 1]);
		
		if(k < 33)
		{
			t = (k == 32 ? t : t & ((1 << k) - 1));
			return new RsaMutableBigInteger(t);
		}
		
		long pLong = (value[(offset + intLen) - 1] & LONG_MASK);
		if(intLen > 1)
		{
			pLong |= ((long) value[(offset + intLen) - 2] << 32);
		}
		long tLong = t & LONG_MASK;
		tLong = tLong * (2 - (pLong * tLong));
		tLong = (k == 64 ? tLong : tLong & ((1L << k) - 1));
		
		RsaMutableBigInteger result = new RsaMutableBigInteger(new int[2]);
		result.value[0] = (int) sl(tLong, 32);
		result.value[1] = (int) tLong;
		result.intLen = 2;
		result.normalize();
		return result;
	}
	
	public static int inverseMod32(int val)
	{
		int t = val;
		t *= 2 - (val * t);
		t *= 2 - (val * t);
		t *= 2 - (val * t);
		t *= 2 - (val * t);
		return t;
	}
	
	private static RsaMutableBigInteger modInverseBP2(RsaMutableBigInteger mod, int k)
	{
		if(mod == null)
		{
			return null;
		}
		return fixup(new RsaMutableBigInteger(1), new RsaMutableBigInteger(mod), k);
	}
	
	private RsaMutableBigInteger modInverse(RsaMutableBigInteger mod)
	{
		if(mod == null)
		{
			return null;
		}
		RsaMutableBigInteger p = new RsaMutableBigInteger(mod);
		RsaMutableBigInteger f = new RsaMutableBigInteger(this);
		RsaMutableBigInteger g = new RsaMutableBigInteger(p);
		RsaSignedMutableBigInteger c = new RsaSignedMutableBigInteger(1);
		RsaSignedMutableBigInteger d = new RsaSignedMutableBigInteger();
		RsaMutableBigInteger temp;
		RsaSignedMutableBigInteger sTemp;
		
		int k = 0;
		if(f.isEven())
		{
			int trailingZeros = f.getLowestSetBit();
			f.rightShift(trailingZeros);
			d.leftShift(trailingZeros);
			k = trailingZeros;
		}
		
		while(!f.isOne())
		{
			if(f.isZero())
			{
				//throw new ArithmeticException("BigInteger not invertible.");
				return null;
			}
			
			if(f.compare(g) < 0)
			{
				temp = f;
				f = g;
				g = temp;
				sTemp = d;
				d = c;
				c = sTemp;
			}
			
			if(((f.value[(f.offset + f.intLen) - 1] ^ g.value[(g.offset + g.intLen) - 1]) & 3) == 0)
			{
				f.subtract(g);
				c.signedSubtract(d);
			}
			else
			{
				f.add(g);
				c.signedAdd(d);
			}
			
			int trailingZeros = f.getLowestSetBit();
			f.rightShift(trailingZeros);
			d.leftShift(trailingZeros);
			k += trailingZeros;
		}
		
		while(c.sign < 0)
		{
			c.signedAdd(p);
		}
		
		return fixup(c, p, k);
	}
	
	private static RsaMutableBigInteger fixup(RsaMutableBigInteger c, RsaMutableBigInteger p, int k)
	{
		if((c == null) || (p == null))
		{
			return null;
		}
		
		RsaMutableBigInteger temp = new RsaMutableBigInteger();
		int r = -inverseMod32(p.value[(p.offset + p.intLen) - 1]);
		
		for(int i = 0, numWords = k >> 5; i < numWords; i++)
		{
			int v = r * c.value[(c.offset + c.intLen) - 1];
			p.mul(v, temp);
			c.add(temp);
			c.intLen--;
		}
		int numBits = k & 0x1f;
		if(numBits != 0)
		{
			int v = r * c.value[(c.offset + c.intLen) - 1];
			v &= ((1 << numBits) - 1);
			p.mul(v, temp);
			c.add(temp);
			c.rightShift(numBits);
		}
		
		while(c.compare(p) >= 0)
		{
			c.subtract(p);
		}
		
		return c;
	}
	
	private RsaMutableBigInteger euclidModInverse(int k)
	{
		RsaMutableBigInteger b = new RsaMutableBigInteger(1);
		b.leftShift(k);
		RsaMutableBigInteger mod = new RsaMutableBigInteger(b);
		
		RsaMutableBigInteger a = new RsaMutableBigInteger(this);
		RsaMutableBigInteger q = new RsaMutableBigInteger();
		b = b.divide(a, q);
		
		RsaMutableBigInteger t1 = new RsaMutableBigInteger(q);
		RsaMutableBigInteger t0 = new RsaMutableBigInteger(1);
		RsaMutableBigInteger temp = new RsaMutableBigInteger();
		
		RsaMutableBigInteger swapper;
		RsaMutableBigInteger r;
		while(!b.isOne())
		{
			r = a.divide(b, q);
			if(r == null)
			{
				return null;
			}
			
			if(r.intLen == 0)
			{
				//throw new ArithmeticException("BigInteger not invertible.");
				return null;
			}
			
			a = r;
			
			if(q.intLen == 1)
			{
				t1.mul(q.value[q.offset], temp);
			}
			else
			{
				q.multiply(t1, temp);
			}
			swapper = q;
			q = temp;
			temp = swapper;
			t0.add(q);
			
			if(a.isOne())
			{
				return t0;
			}
			
			r = b.divide(a, q);
			if(r == null)
			{
				return null;
			}
			
			if(r.intLen == 0)
			{
				//throw new ArithmeticException("BigInteger not invertible.");
				return null;
			}
			
			b = r;
			
			if(q.intLen == 1)
			{
				t0.mul(q.value[q.offset], temp);
			}
			else
			{
				q.multiply(t0, temp);
			}
			swapper = q;
			q = temp;
			temp = swapper;
			
			t1.add(q);
		}
		mod.subtract(t1);
		return mod;
	}
}
