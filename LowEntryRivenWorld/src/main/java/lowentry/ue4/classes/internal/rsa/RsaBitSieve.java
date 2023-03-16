package lowentry.ue4.classes.internal.rsa;


class RsaBitSieve
{
	private long[] bits;
	private int    length;
	
	private static final RsaBitSieve smallSieve = new RsaBitSieve();
	
	
	private RsaBitSieve()
	{
		length = 150 * 64;
		bits = new long[(unitIndex(length - 1) + 1)];
		
		set(0);
		int nextIndex = 1;
		int nextPrime = 3;
		
		do
		{
			sieveSingle(length, nextIndex + nextPrime, nextPrime);
			nextIndex = sieveSearch(length, nextIndex + 1);
			nextPrime = (2 * nextIndex) + 1;
		}
		while((nextIndex > 0) && (nextPrime < length));
	}
	
	public RsaBitSieve(RsaBigInteger base, int searchLen)
	{
		if(base == null)
		{
			bits = new long[0];
			length = 0;
			return;
		}
		
		bits = new long[(unitIndex(searchLen - 1) + 1)];
		length = searchLen;
		int start = 0;
		
		int step = smallSieve.sieveSearch(smallSieve.length, start);
		int convertedStep = (step * 2) + 1;
		
		RsaMutableBigInteger b = new RsaMutableBigInteger(base);
		RsaMutableBigInteger q = new RsaMutableBigInteger();
		do
		{
			start = b.divideOneWord(convertedStep, q);
			
			start = convertedStep - start;
			if((start % 2) == 0)
			{
				start += convertedStep;
			}
			sieveSingle(searchLen, (start - 1) / 2, convertedStep);
			
			step = smallSieve.sieveSearch(smallSieve.length, step + 1);
			convertedStep = (step * 2) + 1;
		}
		while(step > 0);
	}
	
	private static int si(int a, int b)
	{
		return (a >>> b);
	}
	private static long sl(long a, int b)
	{
		return (a >>> b);
	}
	
	private static int unitIndex(int bitIndex)
	{
		return si(bitIndex, 6);
	}
	
	private static long bit(int bitIndex)
	{
		return 1L << (bitIndex & ((1 << 6) - 1));
	}
	
	private boolean get(int bitIndex)
	{
		int unitIndex = unitIndex(bitIndex);
		if(unitIndex >= bits.length)
		{
			return false;
		}
		return ((bits[unitIndex] & bit(bitIndex)) != 0);
	}
	
	private void set(int bitIndex)
	{
		int unitIndex = unitIndex(bitIndex);
		if(unitIndex >= bits.length)
		{
			return;
		}
		bits[unitIndex] |= bit(bitIndex);
	}
	
	private int sieveSearch(int limit, int start)
	{
		if(start >= limit)
		{
			return -1;
		}
		
		int index = start;
		do
		{
			if(!get(index))
			{
				return index;
			}
			index++;
		}
		while(index < (limit - 1));
		return -1;
	}
	
	private void sieveSingle(int limit, int start, int step)
	{
		if(step <= 0)
		{
			return;
		}
		
		while(start < limit)
		{
			set(start);
			start += step;
		}
	}
	
	public RsaBigInteger retrieve(RsaBigInteger initValue, int certainty)
	{
		if(initValue == null)
		{
			return null;
		}
		
		long offset = 1;
		//noinspection ForLoopReplaceableByForEach
		for(int i = 0; i < bits.length; i++)
		{
			long nextLong = ~bits[i];
			for(int j = 0; j < 64; j++)
			{
				if((nextLong & 1) == 1)
				{
					RsaBigInteger candidate = initValue.add(RsaBigInteger.valueOf(offset));
					if(candidate == null)
					{
						return null;
					}
					if(candidate.primeToCertainty(certainty))
					{
						return candidate;
					}
				}
				nextLong = sl(nextLong, 1);
				offset += 2;
			}
		}
		return null;
	}
}
