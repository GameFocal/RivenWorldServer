package lowentry.ue4.classes.internal;


import lowentry.ue4.classes.ParsedHashcash;
import lowentry.ue4.library.LowEntry;

import java.nio.ByteBuffer;
import java.security.DigestException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;


public class HashingHashcash
{
	private static final int    VERSION              = 1;
	private static final int    MAX_COUNTER          = 1048576;
	private static final String DATE_FORMAT_STRING   = "yyMMddHHmmss";
	private static final String DATE_FORMAT_STRING_B = "yyMMdd";
	private static final String DATE_FORMAT_STRING_C = "yyMMddHHmm";
	
	private static final byte[][] BASE_64_CACHE = new byte[MAX_COUNTER][];
	
	private static final ThreadLocal<DateFormat> dateFormat;
	private static final ThreadLocal<DateFormat> dateFormatB;
	private static final ThreadLocal<DateFormat> dateFormatC;
	
	
	static
	{
		for(int i = 0; i < BASE_64_CACHE.length; i++)
		{
			BASE_64_CACHE[i] = base64WithoutLeadingZeroBytes(i);
		}
		
		dateFormat = ThreadLocal.withInitial(() ->
		{
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateFormat.setLenient(false);
			return dateFormat;
		});
		
		dateFormatB = ThreadLocal.withInitial(() ->
		{
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING_B);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateFormat.setLenient(false);
			return dateFormat;
		});
		
		dateFormatC = ThreadLocal.withInitial(() ->
		{
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING_C);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			dateFormat.setLenient(false);
			return dateFormat;
		});
	}
	
	
	public static String[] hash(String[] resource, int bits)
	{
		return hash(resource, null, bits);
	}
	public static String[] hash(String[] resources, Date date, int bits)
	{
		if(resources == null)
		{
			return new String[0];
		}
		if(date == null)
		{
			date = new Date(CachedTime.currentTimeMillis());
		}
		String[] result = new String[resources.length];
		for(int i = 0; i < resources.length; i++)
		{
			result[i] = hash(resources[i], date, bits);
		}
		return result;
	}
	
	public static String hash(String resource, int bits)
	{
		return hash(resource, null, bits);
	}
	public static String hash(String resource, Date date, int bits)
	{
		if(resource == null)
		{
			resource = "";
		}
		if(date == null)
		{
			date = new Date(CachedTime.currentTimeMillis());
		}
		
		MessageDigest hasher = LowEntry.hashingDigestSha1.get();
		byte[] hashBuffer = new byte[160];
		
		String dataPrefix = VERSION + ":" + bits + ":" + getDateString(date) + ":" + resource + "::";
		byte[] dataPrefixBytes = LowEntry.stringToBytesUtf8(dataPrefix);
		
		ByteBuffer buffer = ByteBuffer.allocate(dataPrefixBytes.length + 17 + 8);
		buffer.put(dataPrefixBytes);
		
		int prefixpos = buffer.position();
		
		while(true)
		{
			buffer.position(prefixpos);
			buffer.put(LowEntry.stringToBytesUtf8(LowEntry.bytesToBase64(LowEntry.generateRandomBytesFast(12)) + ":"));
			
			for(byte[] base64counter : BASE_64_CACHE)
			{
				try
				{
					hasher.reset();
					hasher.update(buffer.array(), buffer.arrayOffset(), buffer.position());
					hasher.update(base64counter);
					hasher.digest(hashBuffer, 0, hashBuffer.length);
				}
				catch(DigestException e)
				{
					// can never happen
					throw new RuntimeException(e);
				}
				
				int leadingZeroBits = countLeadingZeroBits(hashBuffer);
				if(leadingZeroBits >= bits)
				{
					buffer.put(base64counter);
					return LowEntry.bytesToStringUtf8(buffer.array(), buffer.arrayOffset(), buffer.position());
				}
			}
		}
	}
	
	
	public static ParsedHashcash[] parse(String[] hashes)
	{
		if(hashes == null)
		{
			return new ParsedHashcash[0];
		}
		ParsedHashcash[] result = new ParsedHashcash[hashes.length];
		for(int i = 0; i < hashes.length; i++)
		{
			result[i] = parse(hashes[i]);
		}
		return result;
	}
	
	public static ParsedHashcash parse(String hash)
	{
		if(hash != null)
		{
			String[] parts = hash.split(":");
			if(parts.length >= 6)
			{
				String version = parts[0];
				if("0".equals(version))
				{
					Date date = parseDateString(parts[1]);
					if(date != null)
					{
						int bits = countLeadingZeroBits(LowEntry.sha1(LowEntry.stringToBytesUtf8(hash)));
						StringBuilder resource = new StringBuilder(parts[2]);
						for(int i = 3; i < (parts.length - 3); i++)
						{
							resource.append(':');
							resource.append(parts[i]);
						}
						return new ParsedHashcash(true, resource.toString(), date, bits);
					}
				}
				else if("1".equals(version))
				{
					if(parts.length >= 7)
					{
						Date date = parseDateString(parts[2]);
						if(date != null)
						{
							int bits = Integer.parseInt(parts[1]);
							if(countLeadingZeroBits(LowEntry.sha1(LowEntry.stringToBytesUtf8(hash))) >= bits)
							{
								StringBuilder resource = new StringBuilder(parts[3]);
								for(int i = 4; i < (parts.length - 3); i++)
								{
									resource.append(':');
									resource.append(parts[i]);
								}
								return new ParsedHashcash(true, resource.toString(), date, bits);
							}
						}
					}
				}
			}
		}
		return new ParsedHashcash(false, null, null, 0);
	}
	
	
	private static byte[] base64WithoutLeadingZeroBytes(int value)
	{
		byte[] bytes;
		if(value < 0)
		{
			bytes = new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
		}
		else if(value < 256)
		{
			bytes = new byte[]{(byte) (value)};
		}
		else if(value < 65536)
		{
			bytes = new byte[]{(byte) (value >> 8), (byte) (value)};
		}
		else if(value < 16777216)
		{
			bytes = new byte[]{(byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
		}
		else
		{
			bytes = new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
		}
		return Base64.getEncoder().encode(bytes);
	}
	
	
	private static String getDateString(Date date)
	{
		return dateFormat.get().format(date);
	}
	
	private static Date parseDateString(String date)
	{
		try
		{
			return dateFormat.get().parse(date);
		}
		catch(ParseException e)
		{
			try
			{
				return dateFormatB.get().parse(date);
			}
			catch(ParseException e2)
			{
				try
				{
					return dateFormatC.get().parse(date);
				}
				catch(ParseException e3)
				{
					return null;
				}
			}
		}
	}
	
	
	private static int countLeadingZeroBits(byte[] values)
	{
		int total = 0;
		for(byte value : values)
		{
			byte ofbyte = countLeadingZeroBits(value);
			total += ofbyte;
			if(ofbyte != 8)
			{
				break;
			}
		}
		return total;
	}
	
	private static byte countLeadingZeroBits(byte v)
	{
		if(v < 0)
		{
			return 0;
		}
		if(v < 1)
		{
			return 8;
		}
		if(v < 2)
		{
			return 7;
		}
		if(v < 4)
		{
			return 6;
		}
		if(v < 8)
		{
			return 5;
		}
		if(v < 16)
		{
			return 4;
		}
		if(v < 32)
		{
			return 3;
		}
		if(v < 64)
		{
			return 2;
		}
		return 1;
	}
}
