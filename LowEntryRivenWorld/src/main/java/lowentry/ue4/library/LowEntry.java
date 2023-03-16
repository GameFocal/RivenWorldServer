package lowentry.ue4.library;


import lowentry.ue4.classes.AesKey;
import lowentry.ue4.classes.BitDataReader;
import lowentry.ue4.classes.BitDataWriter;
import lowentry.ue4.classes.ByteDataReader;
import lowentry.ue4.classes.ByteDataWriter;
import lowentry.ue4.classes.JsonArrayItem;
import lowentry.ue4.classes.JsonObjectItem;
import lowentry.ue4.classes.ParsedHashcash;
import lowentry.ue4.classes.RsaKeys;
import lowentry.ue4.classes.RsaPrivateKey;
import lowentry.ue4.classes.RsaPublicKey;
import lowentry.ue4.classes.SimpleByteDataReader;
import lowentry.ue4.classes.SimpleByteDataWriter;
import lowentry.ue4.classes.bitdata.reader.BitArrayDataReader;
import lowentry.ue4.classes.bitdata.reader.BitBufferDataReader;
import lowentry.ue4.classes.bitdata.reader.BitSubArrayDataReader;
import lowentry.ue4.classes.bitdata.reader.BitSubBufferDataReader;
import lowentry.ue4.classes.bitdata.writer.BitBufferDataWriter;
import lowentry.ue4.classes.bitdata.writer.BitStreamDataWriter;
import lowentry.ue4.classes.bytedata.reader.ByteArrayDataReader;
import lowentry.ue4.classes.bytedata.reader.ByteBufferDataReader;
import lowentry.ue4.classes.bytedata.reader.ByteSubArrayDataReader;
import lowentry.ue4.classes.bytedata.reader.ByteSubBufferDataReader;
import lowentry.ue4.classes.bytedata.reader.SimpleByteArrayDataReader;
import lowentry.ue4.classes.bytedata.reader.SimpleByteBufferDataReader;
import lowentry.ue4.classes.bytedata.reader.SimpleByteSubArrayDataReader;
import lowentry.ue4.classes.bytedata.reader.SimpleByteSubBufferDataReader;
import lowentry.ue4.classes.bytedata.writer.ByteBufferDataWriter;
import lowentry.ue4.classes.bytedata.writer.ByteStreamDataWriter;
import lowentry.ue4.classes.bytedata.writer.SimpleByteBufferDataWriter;
import lowentry.ue4.classes.bytedata.writer.SimpleByteStreamDataWriter;
import lowentry.ue4.classes.internal.CachedTime;
import lowentry.ue4.classes.internal.CompressionLzf;
import lowentry.ue4.classes.internal.EncryptionAes;
import lowentry.ue4.classes.internal.EncryptionRsa;
import lowentry.ue4.classes.internal.HashingBCrypt;
import lowentry.ue4.classes.internal.HashingHashcash;
import lowentry.ue4.classes.internal.HashingPearson;
import lowentry.ue4.libs.jackson.annotation.JsonInclude;
import lowentry.ue4.libs.jackson.core.JsonFactory;
import lowentry.ue4.libs.jackson.databind.JsonNode;
import lowentry.ue4.libs.jackson.databind.ObjectMapper;
import lowentry.ue4.libs.jackson.databind.ObjectReader;
import lowentry.ue4.libs.jackson.databind.ObjectWriter;
import lowentry.ue4.libs.jackson.databind.SerializationFeature;
import lowentry.ue4.libs.jackson.dataformat.yaml.YAMLFactory;
import lowentry.ue4.libs.jackson.dataformat.yaml.YAMLGenerator;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;


public class LowEntry
{
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	public static final ObjectReader JSON_READER;
	public static final ObjectWriter JSON_WRITER_PERFORMANCE;
	public static final ObjectWriter JSON_WRITER_READABILITY;
	
	public static final ObjectMapper YAML;
	
	public static final char[] HEX_CHARS          = "0123456789ABCDEF".toCharArray();
	public static final byte   BOOLEAN_TRUE_BYTE  = 0x01;
	public static final byte   BOOLEAN_FALSE_BYTE = 0x00;
	
	public static final boolean IS_WINDOWS;
	
	
	public static final ThreadLocal<DateFormat> dateformatIso8601;
	
	public static final ThreadLocal<MessageDigest> hashingDigestMd5;
	public static final ThreadLocal<MessageDigest> hashingDigestSha1;
	public static final ThreadLocal<MessageDigest> hashingDigestSha256;
	public static final ThreadLocal<MessageDigest> hashingDigestSha512;
	
	
	public static final class Millis
	{
		private static final double NANOSECOND = 0.000001;
		private static final long   SECOND     = 1000;
		private static final long   MINUTE     = SECOND * 60;
		private static final long   HOUR       = MINUTE * 60;
		private static final long   DAY        = HOUR * 24;
		private static final long   WEEK       = DAY * 7;
		
		public static long NANOSECONDS(long times)
		{
			return (long) (NANOSECOND * times);
		}
		public static long MILLISECONDS(long times)
		{
			return times;
		}
		public static long SECONDS(long times)
		{
			return SECOND * times;
		}
		public static long MINUTES(long times)
		{
			return MINUTE * times;
		}
		public static long HOURS(long times)
		{
			return HOUR * times;
		}
		public static long DAYS(long times)
		{
			return DAY * times;
		}
		public static long WEEKS(long times)
		{
			return WEEK * times;
		}
		
		public static double NANOSECONDS(double times)
		{
			return NANOSECOND * times;
		}
		public static double MILLISECONDS(double times)
		{
			return times;
		}
		public static double SECONDS(double times)
		{
			return SECOND * times;
		}
		public static double MINUTES(double times)
		{
			return MINUTE * times;
		}
		public static double HOURS(double times)
		{
			return HOUR * times;
		}
		public static double DAYS(double times)
		{
			return DAY * times;
		}
		public static double WEEKS(double times)
		{
			return WEEK * times;
		}
	}
	
	
	public static final class Nanos
	{
		private static final long MILLISECOND = 1000000;
		private static final long SECOND      = MILLISECOND * 1000;
		private static final long MINUTE      = SECOND * 60;
		private static final long HOUR        = MINUTE * 60;
		private static final long DAY         = HOUR * 24;
		private static final long WEEK        = DAY * 7;
		
		public static long NANOSECONDS(long times)
		{
			return times;
		}
		public static long MILLISECONDS(long times)
		{
			return MILLISECOND * times;
		}
		public static long SECONDS(long times)
		{
			return SECOND * times;
		}
		public static long MINUTES(long times)
		{
			return MINUTE * times;
		}
		public static long HOURS(long times)
		{
			return HOUR * times;
		}
		public static long DAYS(long times)
		{
			return DAY * times;
		}
		public static long WEEKS(long times)
		{
			return WEEK * times;
		}
		
		public static double NANOSECONDS(double times)
		{
			return times;
		}
		public static double MILLISECONDS(double times)
		{
			return MILLISECOND * times;
		}
		public static double SECONDS(double times)
		{
			return SECOND * times;
		}
		public static double MINUTES(double times)
		{
			return MINUTE * times;
		}
		public static double HOURS(double times)
		{
			return HOUR * times;
		}
		public static double DAYS(double times)
		{
			return DAY * times;
		}
		public static double WEEKS(double times)
		{
			return WEEK * times;
		}
	}
	
	
	static
	{
		IS_WINDOWS = System.getProperty("os.name", "").toLowerCase().contains("win");
		
		
		final ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
		JSON_READER = jsonMapper.reader();
		
		jsonMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
		jsonMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		
		jsonMapper.disable(SerializationFeature.INDENT_OUTPUT);
		JSON_WRITER_PERFORMANCE = jsonMapper.writer();
		
		jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
		JSON_WRITER_READABILITY = jsonMapper.writer();
		
		
		final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		yamlMapper.findAndRegisterModules();
		yamlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		yamlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		YAML = yamlMapper;
		
		
		dateformatIso8601 = ThreadLocal.withInitial(() ->
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss.SSS'Z'");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return dateFormat;
		});
		
		hashingDigestMd5 = ThreadLocal.withInitial(() ->
		{
			try
			{
				return MessageDigest.getInstance("MD5");
			}
			catch(NoSuchAlgorithmException e)
			{
				// can never happen
				throw new RuntimeException(e);
			}
		});
		
		hashingDigestSha1 = ThreadLocal.withInitial(() ->
		{
			try
			{
				return MessageDigest.getInstance("SHA1");
			}
			catch(NoSuchAlgorithmException e)
			{
				// can never happen
				throw new RuntimeException(e);
			}
		});
		
		hashingDigestSha256 = ThreadLocal.withInitial(() ->
		{
			try
			{
				return MessageDigest.getInstance("SHA-256");
			}
			catch(NoSuchAlgorithmException e)
			{
				// can never happen
				throw new RuntimeException(e);
			}
		});
		
		hashingDigestSha512 = ThreadLocal.withInitial(() ->
		{
			try
			{
				return MessageDigest.getInstance("SHA-512");
			}
			catch(NoSuchAlgorithmException e)
			{
				// can never happen
				throw new RuntimeException(e);
			}
		});
	}
	
	
	/**
	 * Prints the stack trace from the Exception as one text, instead of printing each line async.
	 */
	public static void printStackTrace(Throwable e)
	{
		if(e == null)
		{
			return;
		}
		System.err.print(getStackTrace(e));
	}
	
	/**
	 * Gets the stack trace from the Exception.
	 */
	public static String getStackTrace(Throwable e)
	{
		if(e == null)
		{
			return "";
		}
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos))
		{
			e.printStackTrace(ps);
			return baos.toString();
		}
		catch(Exception e2)
		{
			return "";
		}
	}
	
	
	/**
	 * Calls Thread.sleep(millis) and handles any thrown Exceptions.
	 */
	public static void sleep(double millis)
	{
		sleep((long) millis);
	}
	
	/**
	 * Calls Thread.sleep(millis) and handles any thrown Exceptions.
	 */
	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(Exception e)
		{
		}
	}
	
	
	/**
	 * Returns a cached System.currentTimeMillis() value.<br>
	 * <br>
	 * The cached value updates after Thread.sleep(10).<br>
	 */
	public static long cachedCurrentTimeMillis()
	{
		return CachedTime.currentTimeMillis();
	}
	
	/**
	 * Returns a cached System.nanoTime() value.<br>
	 * <br>
	 * The cached value updates after Thread.sleep(10).<br>
	 */
	public static long cachedNanoTime()
	{
		return CachedTime.nanoTime();
	}
	
	/**
	 * Returns the amount of milliseconds that have elapsed since the beginning of this application.<br>
	 * <br>
	 * This value is cached and updates after every Thread.sleep(10).<br>
	 * <br>
	 * PS: does the same as {@link LowEntry#millis()}, this is just a more descriptive function name.
	 */
	public static long millisSinceStart()
	{
		return CachedTime.millisSinceStart();
	}
	
	/**
	 * Returns the amount of milliseconds that have elapsed since the beginning of this application.<br>
	 * <br>
	 * This value is cached and updates after every Thread.sleep(10).<br>
	 * <br>
	 * PS: does the same as {@link LowEntry#millisSinceStart()}, this is just a shorter function name.
	 */
	public static long millis()
	{
		return CachedTime.millisSinceStart();
	}
	
	
	/**
	 * Returns true if the two arrays contain the same data.<br>
	 * <br>
	 * Will always return false when one or both arrays are null.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean areBytesEqual(byte[] a, byte[] b)
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
	
	/**
	 * Returns true if the two arrays contain the same data.<br>
	 * <br>
	 * Will always return false when one or both arrays are null.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean areBytesEqual(byte[] a, int indexA, int lengthA, byte[] b, int indexB, int lengthB)
	{
		if((a == null) || (b == null))
		{
			return false;
		}
		
		if(indexA < 0)
		{
			lengthA += indexA;
			indexA = 0;
		}
		if(lengthA > (a.length - indexA))
		{
			lengthA = a.length - indexA;
		}
		if(lengthA < 0)
		{
			lengthA = 0;
		}
		
		if(indexB < 0)
		{
			lengthB += indexB;
			indexB = 0;
		}
		if(lengthB > (b.length - indexB))
		{
			lengthB = b.length - indexB;
		}
		if(lengthB < 0)
		{
			lengthB = 0;
		}
		
		if(lengthA != lengthB)
		{
			return false;
		}
		
		for(int i = 0; i < lengthA; i++)
		{
			if(a[indexA + i] != b[indexB + i])
			{
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Splits the given bytes into two byte arrays.<br>
	 * <br>
	 * Access the return values as such:<br>
	 * <code>
	 * byte[][] split = LowEntry.splitBytes(bytes, 4);<br>
	 * byte[] a = split[0];<br>
	 * byte[] b = split[1];<br>
	 * </code><br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[][] splitBytes(byte[] bytes, int firstArrayLength)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return new byte[][]{new byte[0], new byte[0]};
		}
		if(firstArrayLength <= 0)
		{
			return new byte[][]{new byte[0], bytes};
		}
		if(firstArrayLength >= bytes.length)
		{
			return new byte[][]{bytes, new byte[0]};
		}
		
		byte[] a = new byte[firstArrayLength];
		byte[] b = new byte[bytes.length - firstArrayLength];
		System.arraycopy(bytes, 0, a, 0, a.length);
		System.arraycopy(bytes, a.length, b, 0, b.length);
		return new byte[][]{a, b};
	}
	
	/**
	 * Merges byte arrays into one.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] mergeBytes(byte[]... arrays)
	{
		if((arrays == null) || (arrays.length <= 0))
		{
			return new byte[0];
		}
		if(arrays.length == 1)
		{
			byte[] array = arrays[0];
			if(array == null)
			{
				return new byte[0];
			}
			return array;
		}
		int length = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				length += array.length;
			}
		}
		if(length <= 0)
		{
			return new byte[0];
		}
		byte[] merged = new byte[length];
		int index = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				System.arraycopy(array, 0, merged, index, array.length);
				index += array.length;
			}
		}
		return merged;
	}
	/**
	 * Merges byte arrays into one.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] mergeBytes(Collection<byte[]> arrays)
	{
		if((arrays == null) || (arrays.size() <= 0))
		{
			return new byte[0];
		}
		if(arrays.size() == 1)
		{
			byte[] array = arrays.iterator().next();
			if(array == null)
			{
				return new byte[0];
			}
			return array;
		}
		int length = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				length += array.length;
			}
		}
		if(length <= 0)
		{
			return new byte[0];
		}
		byte[] merged = new byte[length];
		int index = 0;
		for(byte[] array : arrays)
		{
			if(array != null)
			{
				System.arraycopy(array, 0, merged, index, array.length);
				index += array.length;
			}
		}
		return merged;
	}
	
	/**
	 * Returns the values of the given bytes of the given index and length.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] bytesSubArray(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return new byte[0];
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
			return new byte[0];
		}
		
		if((index == 0) && (length == bytes.length))
		{
			return bytes;
		}
		byte[] c = new byte[length];
		System.arraycopy(bytes, index, c, 0, c.length);
		return c;
	}
	
	
	/**
	 * Returns random bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] generateRandomBytes(int length)
	{
		if(length <= 0)
		{
			return new byte[0];
		}
		
		byte[] bytes = new byte[length];
		SECURE_RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	/**
	 * Returns random bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] generateRandomBytes(int minLength, int maxLength)
	{
		if(minLength < 0)
		{
			minLength = 0;
		}
		if(maxLength < 0)
		{
			maxLength = 0;
		}
		
		int diff;
		int min;
		if(maxLength >= minLength)
		{
			diff = maxLength - minLength;
			min = minLength;
		}
		else
		{
			diff = minLength - maxLength;
			min = maxLength;
		}
//		if(min < 0)
//		{
//			min = 0;
//		}
//		if(diff < 0)
//		{
//			diff = 0;
//		}
		
		if((min == 0) && (diff == 0))
		{
			return new byte[0];
		}
		
		int length = SECURE_RANDOM.nextInt(diff + 1) + min;
		if(length <= 0)
		{
			return new byte[0];
		}
		
		byte[] bytes = new byte[length];
		SECURE_RANDOM.nextBytes(bytes);
		return bytes;
	}
	
	
	/**
	 * Returns random bytes, this function generates not cryptographically secure random bytes.
	 */
	public static byte[] generateRandomBytesFast(int length)
	{
		if(length <= 0)
		{
			return new byte[0];
		}
		
		byte[] bytes = new byte[length];
		ThreadLocalRandom.current().nextBytes(bytes);
		return bytes;
	}
	
	/**
	 * Returns random bytes, this function generates not cryptographically secure random bytes.
	 */
	public static byte[] generateRandomBytesFast(int minLength, int maxLength)
	{
		if(minLength < 0)
		{
			minLength = 0;
		}
		if(maxLength < 0)
		{
			maxLength = 0;
		}
		
		int diff;
		int min;
		if(maxLength >= minLength)
		{
			diff = maxLength - minLength;
			min = minLength;
		}
		else
		{
			diff = minLength - maxLength;
			min = maxLength;
		}
//		if(min < 0)
//		{
//			min = 0;
//		}
//		if(diff < 0)
//		{
//			diff = 0;
//		}
		
		if((min == 0) && (diff == 0))
		{
			return new byte[0];
		}
		
		ThreadLocalRandom random = ThreadLocalRandom.current();
		
		int length = random.nextInt(diff + 1) + min;
		if(length <= 0)
		{
			return new byte[0];
		}
		
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}
	
	
	/**
	 * Gets the bytes out of a ByteBuffer, without changing the ByteBuffer in any way.
	 */
	public static byte[] getBytesFromByteBuffer(ByteBuffer buffer)
	{
		if(buffer == null)
		{
			return new byte[0];
		}
		byte[] bytes = new byte[buffer.remaining()];
		int pos = buffer.position();
		buffer.get(bytes);
		buffer.position(pos);
		return bytes;
	}
	
	/**
	 * Creates a copy of the ByteBuffer (including the contents), without changing the ByteBuffer in any way.
	 */
	public static ByteBuffer cloneByteBuffer(ByteBuffer buffer)
	{
		if(buffer == null)
		{
			return null;
		}
		return cloneByteBuffer(buffer, buffer.isDirect());
	}
	
	/**
	 * Creates a copy of the ByteBuffer (including the contents), without changing the ByteBuffer in any way.
	 */
	public static ByteBuffer cloneByteBuffer(ByteBuffer buffer, boolean shouldCopyUseAllocateDirect)
	{
		if(buffer == null)
		{
			return null;
		}
		int pos = buffer.position();
		ByteBuffer copy;
		if(shouldCopyUseAllocateDirect)
		{
			copy = ByteBuffer.allocateDirect(buffer.remaining());
		}
		else
		{
			copy = ByteBuffer.allocate(buffer.remaining());
		}
		copy.put(buffer);
		buffer.position(pos);
		copy.flip();
		return copy;
	}
	
	
	/**
	 * Tries to convert the given bytes into a string, returns an empty string if it fails.<br>
	 * This method will fail if the given bytes are not a UTF-8 encoded string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToStringUtf8(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		try
		{
			return new String(bytes, StandardCharsets.UTF_8);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	/**
	 * Tries to convert the given bytes into a string, returns an empty string if it fails.<br>
	 * This method will fail if the given bytes are not a UTF-8 encoded string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToStringUtf8(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		try
		{
			return new String(bytes, index, length, StandardCharsets.UTF_8);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * Converts the given string into bytes, using the UTF-8 encoding.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] stringToBytesUtf8(String string)
	{
		if(string == null)
		{
			return new byte[0];
		}
		try
		{
			return string.getBytes(StandardCharsets.UTF_8);
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	
	/**
	 * Tries to convert the given bytes into a string, returns an empty string if it fails.<br>
	 * This method will fail if the given bytes are not a Latin-1 (ISO-8859-1) encoded string.
	 */
	public static String bytesToStringLatin1(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		try
		{
			return new String(bytes, StandardCharsets.ISO_8859_1);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	/**
	 * Tries to convert the given bytes into a string, returns an empty string if it fails.<br>
	 * This method will fail if the given bytes are not a Latin-1 (ISO-8859-1) encoded string.
	 */
	public static String bytesToStringLatin1(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		try
		{
			return new String(bytes, index, length, StandardCharsets.ISO_8859_1);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * Converts the given string into bytes, using the Latin-1 (ISO-8859-1) encoding.
	 */
	public static byte[] stringToBytesLatin1(String string)
	{
		if(string == null)
		{
			return new byte[0];
		}
		try
		{
			return string.getBytes(StandardCharsets.ISO_8859_1);
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	
	/**
	 * Converts the given bytes into a Base64 string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBase64(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		return Base64.getEncoder().encodeToString(bytes);
	}
	/**
	 * Converts the given bytes into a Base64 string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBase64(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		if((index == 0) && (length == bytes.length))
		{
			return Base64.getEncoder().encodeToString(bytes);
		}
		return Base64.getEncoder().encodeToString(bytesSubArray(bytes, index, length));
	}
	
	/**
	 * Tries to convert the given Base64 string into bytes, returns an empty array if it fails.<br>
	 * This method will fail if the given string is not a Base64 string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] base64ToBytes(String base64)
	{
		if(base64 == null)
		{
			return new byte[0];
		}
		try
		{
			return Base64.getDecoder().decode(base64);
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	
	/**
	 * Converts the given bytes into a Hexadecimal (Base16) string, without spaces.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToHex(byte[] bytes)
	{
		return bytesToHex(bytes, false);
	}
	/**
	 * Converts the given bytes into a Hexadecimal (Base16) string.<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToHex(byte[] bytes, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		
		char[] hexChars = new char[bytes.length * 2];
		for(int i = 0; i < bytes.length; i++)
		{
			int v = bytes[i] & 0xFF;
			hexChars[i * 2] = HEX_CHARS[v >>> 4];
			hexChars[(i * 2) + 1] = HEX_CHARS[v & 0x0F];
		}
		String hex = new String(hexChars);
		
		if(!addSpaces)
		{
			return hex;
		}
		return padHexString(hex);
	}
	/**
	 * Converts the given bytes into a Hexadecimal (Base16) string, without spaces.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToHex(byte[] bytes, int index, int length)
	{
		return bytesToHex(bytes, index, length, false);
	}
	/**
	 * Converts the given bytes into a Hexadecimal (Base16) string.<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToHex(byte[] bytes, int index, int length, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		if((index != 0) || (length != bytes.length))
		{
			bytes = bytesSubArray(bytes, index, length);
		}
		
		char[] hexChars = new char[bytes.length * 2];
		for(int i = 0; i < bytes.length; i++)
		{
			int v = bytes[i] & 0xFF;
			hexChars[i * 2] = HEX_CHARS[v >>> 4];
			hexChars[(i * 2) + 1] = HEX_CHARS[v & 0x0F];
		}
		String hex = new String(hexChars);
		
		if(!addSpaces)
		{
			return hex;
		}
		return padHexString(hex);
	}
	protected static String padHexString(String hex)
	{
		StringBuilder stringBuilder = new StringBuilder((hex.length() / 2) * 3);
		char[] chars = hex.toCharArray();
		for(int i = 1; i <= chars.length; i++)
		{
			stringBuilder.append(chars[i - 1]);
			if(((i % 2) == 0) && (i < (chars.length - 1)))
			{
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Tries to convert the given Hexadecimal (Base16) string into bytes, returns an empty array if it fails.<br>
	 * This method will fail if the given string is not a Hexadecimal (Base16) string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] hexToBytes(String hex)
	{
		if((hex == null) || (hex.length() <= 0))
		{
			return new byte[0];
		}
		try
		{
			hex = hex.replaceAll(" ", "");
			int len = hex.length();
			byte[] bytes = new byte[len / 2];
			for(int i = 0; i < len; i += 2)
			{
				bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
			}
			return bytes;
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	
	/**
	 * Converts the given bytes into a Binary (Base2) string, without spaces.<br>
	 * <br>
	 * Prints the bits in this order: 87654321<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBinary(byte[] bytes)
	{
		return bytesToBinary(bytes, false);
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string.<br>
	 * <br>
	 * Prints the bits in this order: 87654321<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBinary(byte[] bytes, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder(bytes.length * (addSpaces ? 9 : 8));
		for(int i = 1; i <= bytes.length; i++)
		{
			for(int j = 7; j >= 0; j--)
			{
				if((bytes[i - 1] & (1 << j)) > 0)
				{
					stringBuilder.append("1");
				}
				else
				{
					stringBuilder.append("0");
				}
			}
			if(addSpaces && (i < bytes.length))
			{
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string, without spaces.<br>
	 * <br>
	 * Prints the bits in this order: 87654321<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBinary(byte[] bytes, int index, int length)
	{
		return bytesToBinary(bytes, index, length, false);
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string.<br>
	 * <br>
	 * Prints the bits in this order: 87654321<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBinary(byte[] bytes, int index, int length, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder(length * (addSpaces ? 9 : 8));
		for(int i = 1; i <= length; i++)
		{
			for(int j = 7; j >= 0; j--)
			{
				if((bytes[index + (i - 1)] & (1 << j)) > 0)
				{
					stringBuilder.append("1");
				}
				else
				{
					stringBuilder.append("0");
				}
			}
			if(addSpaces && (i < length))
			{
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Tries to convert the given Binary (Base2) string into bytes, returns an empty array if it fails.<br>
	 * This method will fail if the given string is not a Binary (Base2) string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] binaryToBytes(String binary)
	{
		if((binary == null) || (binary.length() <= 0))
		{
			return new byte[0];
		}
		binary = binary.replaceAll(" ", "");
		if((binary.length() <= 0) || ((binary.length() % 8) != 0))
		{
			return new byte[0];
		}
		char[] bits = binary.toCharArray();
		byte[] bytes = new byte[binary.length() / 8];
		int index = 0;
		for(int i = 0; i < binary.length(); i += 8)
		{
			byte b = 0;
			for(int j = 0; j < 8; j++)
			{
				if(bits[i + j] == '0')
				{
//					continue;
				}
				else if(bits[i + j] == '1')
				{
					b += (1 << (7 - j));
				}
				else
				{
					// encountered invalid character
					return new byte[0];
				}
			}
			bytes[index] = b;
			index++;
		}
		return bytes;
	}
	
	
	/**
	 * Converts the given bytes into a Binary (Base2) string, with each byte reversed, and without spaces.<br>
	 * <br>
	 * Prints the bits in this order: 12345678<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBitString(byte[] bytes)
	{
		return bytesToBitString(bytes, false);
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string, with each byte reversed.<br>
	 * <br>
	 * Prints the bits in this order: 12345678<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBitString(byte[] bytes, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder(bytes.length * (addSpaces ? 9 : 8));
		for(int i = 1; i <= bytes.length; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if((bytes[i - 1] & (1 << j)) > 0)
				{
					stringBuilder.append("1");
				}
				else
				{
					stringBuilder.append("0");
				}
			}
			if(addSpaces && (i < bytes.length))
			{
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string, with each byte reversed, and without spaces.<br>
	 * <br>
	 * Prints the bits in this order: 12345678<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBitString(byte[] bytes, int index, int length)
	{
		return bytesToBitString(bytes, index, length, false);
	}
	/**
	 * Converts the given bytes into a Binary (Base2) string, with each byte reversed.<br>
	 * <br>
	 * Prints the bits in this order: 12345678<br>
	 * <br>
	 * If addSpaces is true, it will add a space after every byte (except the last one).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String bytesToBitString(byte[] bytes, int index, int length, boolean addSpaces)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return "";
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
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder(length * (addSpaces ? 9 : 8));
		for(int i = 1; i <= length; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if((bytes[index + (i - 1)] & (1 << j)) > 0)
				{
					stringBuilder.append("1");
				}
				else
				{
					stringBuilder.append("0");
				}
			}
			if(addSpaces && (i < length))
			{
				stringBuilder.append(" ");
			}
		}
		return stringBuilder.toString();
	}
	
	/**
	 * Tries to convert the given Binary (Base2) string (with each byte reversed) into bytes, returns an empty array if it fails.<br>
	 * This method will fail if the given string is not a Binary (Base2) string.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] bitStringToBytes(String bits)
	{
		if((bits == null) || (bits.length() <= 0))
		{
			return new byte[0];
		}
		bits = bits.replaceAll(" ", "");
		if((bits.length() <= 0) || ((bits.length() % 8) != 0))
		{
			return new byte[0];
		}
		char[] bitss = bits.toCharArray();
		byte[] bytes = new byte[bits.length() / 8];
		int index = 0;
		for(int i = 0; i < bits.length(); i += 8)
		{
			byte b = 0;
			for(int j = 0; j < 8; j++)
			{
				if(bitss[i + j] == '0')
				{
//					continue;
				}
				else if(bitss[i + j] == '1')
				{
					b += (1 << j);
				}
				else
				{
					// encountered invalid character
					return new byte[0];
				}
			}
			bytes[index] = b;
			index++;
		}
		return bytes;
	}
	
	
	/**
	 * Convert the given bytes into a boolean (00000001 will return true, everything else will return false).<br>
	 * If there is more than 1 byte given, it will only convert the first byte to a boolean.<br>
	 * If there are no bytes given, it will return false.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean bytesToBoolean(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return false;
		}
		return (bytes[0] == BOOLEAN_TRUE_BYTE);
	}
	/**
	 * Convert the given bytes into a boolean (00000001 will return true, everything else will return false).<br>
	 * If there is more than 1 byte given, it will only convert the first byte to a boolean.<br>
	 * If there are no bytes given, it will return false.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean bytesToBoolean(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return false;
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
			return false;
		}
		
		return (bytes[index] == BOOLEAN_TRUE_BYTE);
	}
	
	/**
	 * Converts the given boolean to 1 byte.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] booleanToBytes(boolean value)
	{
		if(value)
		{
			return new byte[]{BOOLEAN_TRUE_BYTE};
		}
		else
		{
			return new byte[]{BOOLEAN_FALSE_BYTE};
		}
	}
	
	
	/**
	 * Convert the given byte into a boolean (00000001 will return true, everything else will return false).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean byteToBoolean(byte bytes)
	{
		return (bytes == BOOLEAN_TRUE_BYTE);
	}
	
	/**
	 * Converts the given boolean to a byte.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte booleanToByte(boolean value)
	{
		if(value)
		{
			return BOOLEAN_TRUE_BYTE;
		}
		else
		{
			return BOOLEAN_FALSE_BYTE;
		}
	}
	
	
	/**
	 * Convert the given bytes to an integer.<br>
	 * If there are more than 4 bytes given, it will only convert the first 4 bytes to an integer.<br>
	 * If there are less than 4 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 turns into 00000000 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static int bytesToInteger(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
		}
		if(bytes.length <= 1)
		{
			return (bytes[0] & 0xFF);
		}
		if(bytes.length <= 2)
		{
			return ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
		}
		if(bytes.length <= 3)
		{
			return ((bytes[0] & 0xFF) << 16) | ((bytes[1] & 0xFF) << 8) | (bytes[2] & 0xFF);
		}
		return ((bytes[0] & 0xFF) << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
	}
	/**
	 * Convert the given bytes to an integer.<br>
	 * If there are more than 4 bytes given, it will only convert the first 4 bytes to an integer.<br>
	 * If there are less than 4 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 turns into 00000000 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static int bytesToInteger(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
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
			return 0;
		}
		
		if(length <= 1)
		{
			return (bytes[index] & 0xFF);
		}
		if(length <= 2)
		{
			return ((bytes[index] & 0xFF) << 8) | (bytes[index + 1] & 0xFF);
		}
		if(length <= 3)
		{
			return ((bytes[index] & 0xFF) << 16) | ((bytes[index + 1] & 0xFF) << 8) | (bytes[index + 2] & 0xFF);
		}
		return ((bytes[index] & 0xFF) << 24) | ((bytes[index + 1] & 0xFF) << 16) | ((bytes[index + 2] & 0xFF) << 8) | (bytes[index + 3] & 0xFF);
	}
	
	/**
	 * Converts the given integer to 4 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] integerToBytes(int value)
	{
		return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
	}
	
	
	/**
	 * Convert the given bytes to a float.<br>
	 * If there are more than 4 bytes given, it will only convert the first 4 bytes to a float.<br>
	 * If there are less than 4 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 turns into 00000000 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static float bytesToFloat(byte[] bytes)
	{
		return Float.intBitsToFloat(bytesToInteger(bytes));
	}
	/**
	 * Convert the given bytes to a float.<br>
	 * If there are more than 4 bytes given, it will only convert the first 4 bytes to a float.<br>
	 * If there are less than 4 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 turns into 00000000 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static float bytesToFloat(byte[] bytes, int index, int length)
	{
		return Float.intBitsToFloat(bytesToInteger(bytes, index, length));
	}
	
	/**
	 * Converts the given float to 4 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] floatToBytes(float value)
	{
		return integerToBytes(Float.floatToIntBits(value));
	}
	
	
	/**
	 * Convert the given bytes to a long.<br>
	 * If there are more than 8 bytes given, it will only convert the first 8 bytes to a long.<br>
	 * If there are less than 8 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 01010101 01010101 01010101 turns into 00000000 00000000 01010101 01010101 01010101 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.
	 */
	public static long bytesToLong(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
		}
		if(bytes.length <= 1)
		{
			return ((long) bytes[0] & 0xFF);
		}
		if(bytes.length <= 2)
		{
			return (((long) bytes[0] & 0xFF) << 8) | ((long) bytes[1] & 0xFF);
		}
		if(bytes.length <= 3)
		{
			return (((long) bytes[0] & 0xFF) << 16) | (((long) bytes[1] & 0xFF) << 8) | ((long) bytes[2] & 0xFF);
		}
		if(bytes.length <= 4)
		{
			return (((long) bytes[0] & 0xFF) << 24) | (((long) bytes[1] & 0xFF) << 16) | (((long) bytes[2] & 0xFF) << 8) | ((long) bytes[3] & 0xFF);
		}
		if(bytes.length <= 5)
		{
			return (((long) bytes[0] & 0xFF) << 32) | (((long) bytes[1] & 0xFF) << 24) | (((long) bytes[2] & 0xFF) << 16) | (((long) bytes[3] & 0xFF) << 8) | ((long) bytes[4] & 0xFF);
		}
		if(bytes.length <= 6)
		{
			return (((long) bytes[0] & 0xFF) << 40) | (((long) bytes[1] & 0xFF) << 32) | (((long) bytes[2] & 0xFF) << 24) | (((long) bytes[3] & 0xFF) << 16) | (((long) bytes[4] & 0xFF) << 8) | ((long) bytes[5] & 0xFF);
		}
		if(bytes.length <= 7)
		{
			return (((long) bytes[0] & 0xFF) << 48) | (((long) bytes[1] & 0xFF) << 40) | (((long) bytes[2] & 0xFF) << 32) | (((long) bytes[3] & 0xFF) << 24) | (((long) bytes[4] & 0xFF) << 16) | (((long) bytes[5] & 0xFF) << 8) | ((long) bytes[6] & 0xFF);
		}
		return (((long) bytes[0] & 0xFF) << 56) | (((long) bytes[1] & 0xFF) << 48) | (((long) bytes[2] & 0xFF) << 40) | (((long) bytes[3] & 0xFF) << 32) | (((long) bytes[4] & 0xFF) << 24) | (((long) bytes[5] & 0xFF) << 16) | (((long) bytes[6] & 0xFF) << 8) | ((long) bytes[7] & 0xFF);
	}
	/**
	 * Convert the given bytes to a long.<br>
	 * If there are more than 8 bytes given, it will only convert the first 8 bytes to a long.<br>
	 * If there are less than 8 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 01010101 01010101 01010101 turns into 00000000 00000000 01010101 01010101 01010101 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.
	 */
	public static long bytesToLong(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
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
			return 0;
		}
		
		if(length <= 1)
		{
			return ((long) bytes[index] & 0xFF);
		}
		if(length <= 2)
		{
			return (((long) bytes[index] & 0xFF) << 8) | ((long) bytes[index + 1] & 0xFF);
		}
		if(length <= 3)
		{
			return (((long) bytes[index] & 0xFF) << 16) | (((long) bytes[index + 1] & 0xFF) << 8) | ((long) bytes[index + 2] & 0xFF);
		}
		if(length <= 4)
		{
			return (((long) bytes[index] & 0xFF) << 24) | (((long) bytes[index + 1] & 0xFF) << 16) | (((long) bytes[index + 2] & 0xFF) << 8) | ((long) bytes[index + 3] & 0xFF);
		}
		if(length <= 5)
		{
			return (((long) bytes[index] & 0xFF) << 32) | (((long) bytes[index + 1] & 0xFF) << 24) | (((long) bytes[index + 2] & 0xFF) << 16) | (((long) bytes[index + 3] & 0xFF) << 8) | ((long) bytes[index + 4] & 0xFF);
		}
		if(length <= 6)
		{
			return (((long) bytes[index] & 0xFF) << 40) | (((long) bytes[index + 1] & 0xFF) << 32) | (((long) bytes[index + 2] & 0xFF) << 24) | (((long) bytes[index + 3] & 0xFF) << 16) | (((long) bytes[index + 4] & 0xFF) << 8) | ((long) bytes[index + 5] & 0xFF);
		}
		if(length <= 7)
		{
			return (((long) bytes[index] & 0xFF) << 48) | (((long) bytes[index + 1] & 0xFF) << 40) | (((long) bytes[index + 2] & 0xFF) << 32) | (((long) bytes[index + 3] & 0xFF) << 24) | (((long) bytes[index + 4] & 0xFF) << 16) | (((long) bytes[index + 5] & 0xFF) << 8) | ((long) bytes[index + 6] & 0xFF);
		}
		return (((long) bytes[index] & 0xFF) << 56) | (((long) bytes[index + 1] & 0xFF) << 48) | (((long) bytes[index + 2] & 0xFF) << 40) | (((long) bytes[index + 3] & 0xFF) << 32) | (((long) bytes[index + 4] & 0xFF) << 24) | (((long) bytes[index + 5] & 0xFF) << 16) | (((long) bytes[index + 6] & 0xFF) << 8) | ((long) bytes[index + 7] & 0xFF);
	}
	
	/**
	 * Converts the given long to 8 bytes.
	 */
	public static byte[] longToBytes(long value)
	{
		return new byte[]{(byte) (value >> 56), (byte) (value >> 48), (byte) (value >> 40), (byte) (value >> 32), (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) (value)};
	}
	
	
	/**
	 * Convert the given bytes to a double.<br>
	 * If there are more than 8 bytes given, it will only convert the first 8 bytes to a double.<br>
	 * If there are less than 8 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 01010101 01010101 01010101 turns into 00000000 00000000 01010101 01010101 01010101 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.
	 */
	public static double bytesToDouble(byte[] bytes)
	{
		return Double.longBitsToDouble(bytesToLong(bytes));
	}
	/**
	 * Convert the given bytes to a double.<br>
	 * If there are more than 8 bytes given, it will only convert the first 8 bytes to a double.<br>
	 * If there are less than 8 bytes given, it will prefix the bytes with 0 value bytes (so 01010101 01010101 01010101 01010101 01010101 01010101 turns into 00000000 00000000 01010101 01010101 01010101 01010101 01010101 01010101).<br>
	 * If there are no bytes given, it will return 0.
	 */
	public static double bytesToDouble(byte[] bytes, int index, int length)
	{
		return Double.longBitsToDouble(bytesToLong(bytes, index, length));
	}
	
	/**
	 * Converts the given double to 8 bytes.
	 */
	public static byte[] doubleToBytes(double value)
	{
		return longToBytes(Double.doubleToLongBits(value));
	}
	
	
	/**
	 * Returns the first byte, or 0 if there are no bytes given.
	 */
	public static byte bytesToByte(byte[] bytes)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
		}
		return bytes[0];
	}
	/**
	 * Returns the first byte, or 0 if there are no bytes given.
	 */
	public static byte bytesToByte(byte[] bytes, int index, int length)
	{
		if((bytes == null) || (bytes.length <= 0))
		{
			return 0;
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
			return 0;
		}
		
		return bytes[index];
	}
	
	/**
	 * Converts the given value into a byte array.
	 */
	public static byte[] byteToBytes(byte value)
	{
		return new byte[]{value};
	}
	
	
	/**
	 * Converts bits into a byte.
	 */
	public static byte bitsToByte(boolean bit8, boolean bit7, boolean bit6, boolean bit5, boolean bit4, boolean bit3, boolean bit2, boolean bit1)
	{
		byte b = 0;
		if(bit8)
		{
			b |= (1 << 7);
		}
		if(bit7)
		{
			b |= (1 << 6);
		}
		if(bit6)
		{
			b |= (1 << 5);
		}
		if(bit5)
		{
			b |= (1 << 4);
		}
		if(bit4)
		{
			b |= (1 << 3);
		}
		if(bit3)
		{
			b |= (1 << 2);
		}
		if(bit2)
		{
			b |= (1 << 1);
		}
		if(bit1)
		{
			b |= 1;
		}
		return b;
	}
	
	/**
	 * Converts bits into a byte.<br>
	 * <br>
	 * The bits are expected to be represented as such:<br>
	 * [0] = bit 8<br>
	 * [1] = bit 7<br>
	 * [2] = bit 6<br>
	 * [3] = bit 5<br>
	 * [4] = bit 4<br>
	 * [5] = bit 3<br>
	 * [6] = bit 2<br>
	 * [7] = bit 1
	 */
	public static byte bitsToByte(boolean[] bits)
	{
		byte b = 0;
		if(bits == null)
		{
			return b;
		}
		if((bits.length >= 1) && bits[0])
		{
			b |= (1 << 7);
		}
		if((bits.length >= 2) && bits[1])
		{
			b |= (1 << 6);
		}
		if((bits.length >= 3) && bits[2])
		{
			b |= (1 << 5);
		}
		if((bits.length >= 4) && bits[3])
		{
			b |= (1 << 4);
		}
		if((bits.length >= 5) && bits[4])
		{
			b |= (1 << 3);
		}
		if((bits.length >= 6) && bits[5])
		{
			b |= (1 << 2);
		}
		if((bits.length >= 7) && bits[6])
		{
			b |= (1 << 1);
		}
		if((bits.length >= 8) && bits[7])
		{
			b |= 1;
		}
		return b;
	}
	/**
	 * Converts bits into a byte.<br>
	 * <br>
	 * The bits are expected to be represented as such:<br>
	 * [0] = bit 8<br>
	 * [1] = bit 7<br>
	 * [2] = bit 6<br>
	 * [3] = bit 5<br>
	 * [4] = bit 4<br>
	 * [5] = bit 3<br>
	 * [6] = bit 2<br>
	 * [7] = bit 1
	 */
	public static byte bitsToByte(boolean[] bits, int index, int length)
	{
		if((bits == null) || (bits.length <= 0))
		{
			return 0;
		}
		
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (bits.length - index))
		{
			length = bits.length - index;
		}
		if(length <= 0)
		{
			return 0;
		}
		
		byte b = 0;
		if(bits[index])
		{
			b |= (1 << 7);
		}
		if((length >= 2) && bits[index + 1])
		{
			b |= (1 << 6);
		}
		if((length >= 3) && bits[index + 2])
		{
			b |= (1 << 5);
		}
		if((length >= 4) && bits[index + 3])
		{
			b |= (1 << 4);
		}
		if((length >= 5) && bits[index + 4])
		{
			b |= (1 << 3);
		}
		if((length >= 6) && bits[index + 5])
		{
			b |= (1 << 2);
		}
		if((length >= 7) && bits[index + 6])
		{
			b |= (1 << 1);
		}
		if((length >= 8) && bits[index + 7])
		{
			b |= 1;
		}
		return b;
	}
	
	/**
	 * Converts a byte into bits.<br>
	 * <br>
	 * The bits will be returned as such:<br>
	 * [0] = bit 8<br>
	 * [1] = bit 7<br>
	 * [2] = bit 6<br>
	 * [3] = bit 5<br>
	 * [4] = bit 4<br>
	 * [5] = bit 3<br>
	 * [6] = bit 2<br>
	 * [7] = bit 1
	 */
	public static boolean[] byteToBits(byte b)
	{
		boolean[] bits = new boolean[8];
		bits[0] = (((b >> 7) & 1) != 0);
		bits[1] = (((b >> 6) & 1) != 0);
		bits[2] = (((b >> 5) & 1) != 0);
		bits[3] = (((b >> 4) & 1) != 0);
		bits[4] = (((b >> 3) & 1) != 0);
		bits[5] = (((b >> 2) & 1) != 0);
		bits[6] = (((b >> 1) & 1) != 0);
		bits[7] = ((b & 1) != 0);
		return bits;
	}
	
	
	/**
	 * Returns true if the bit is 1, returns false if the bit is 0.<br>
	 * <br>
	 * Bytes start with bit 8, ending with bit 1, as such: 87654321
	 */
	public static boolean isBitSet(byte b, int bit)
	{
		bit -= 1;
		return (((b >> bit) & 1) != 0);
	}
	
	/**
	 * Sets a bit 1 or 0, depending on the given boolean.<br>
	 * <br>
	 * Bytes start with bit 8, ending with bit 1, as such: 87654321
	 */
	public static byte getByteWithBitSet(byte b, int bit, boolean value)
	{
		bit -= 1;
		if(value)
		{
			return (byte) (b | (1 << bit));
		}
		else
		{
			return (byte) (b & ~(1 << bit));
		}
	}
	
	
	/**
	 * Returns true if the bit is 1, returns false if the bit is 0.<br>
	 * <br>
	 * Bytes start with bit 8, ending with bit 1, as such: 87654321
	 */
	public static boolean isBitSet(byte[] b, int bit)
	{
		if((b == null) || (b.length == 0))
		{
			return false;
		}
		bit -= 1;
		int bindex = (bit / 8);
		if(b.length <= bindex)
		{
			return false;
		}
		bit %= 8;
		return (((b[bindex] >> bit) & 1) != 0);
	}
	
	/**
	 * Sets a bit 1 or 0, depending on the given boolean.<br>
	 * <br>
	 * Bytes start with bit 8, ending with bit 1, as such: 87654321
	 */
	public static void setBit(byte[] b, int bit, boolean value)
	{
		if((b == null) || (b.length == 0))
		{
			return;
		}
		bit -= 1;
		int bindex = (bit / 8);
		if(b.length <= bindex)
		{
			return;
		}
		bit %= 8;
		if(value)
		{
			b[bindex] = (byte) (b[bindex] | (1 << bit));
		}
		else
		{
			b[bindex] = (byte) (b[bindex] & ~(1 << bit));
		}
	}
	
	
	/**
	 * Reverses all bits in the byte.<br>
	 * <br>
	 * 12345678 becomes 87654321<br>
	 * 10100000 becomes 00000101
	 */
	public static byte getByteWithReverseBits(byte b)
	{
		byte b2 = 0;
		for(int position = 7; position >= 0; position--)
		{
			b2 += ((b & 1) << position);
			b >>= 1;
		}
		return b2;
	}
	
	/**
	 * Reverses every bit of every byte (per byte).<br>
	 * <br>
	 * 12345678 90111213 becomes 87654321 31211109<br>
	 * 10100000 00110000 becomes 00000101 00001100
	 */
	public static void reverseBitsIndividually(byte[] b)
	{
		if((b == null) || (b.length == 0))
		{
			return;
		}
		for(int i = 0; i < b.length; i++)
		{
			byte b2 = 0;
			for(int position = 7; position >= 0; position--)
			{
				b2 += ((b[i] & 1) << position);
				b[i] >>= 1;
			}
			b[i] = b2;
		}
	}
	/**
	 * Reverses every bit of every byte (per byte).<br>
	 * <br>
	 * 12345678 90111213 becomes 87654321 31211109<br>
	 * 10100000 00110000 becomes 00000101 00001100
	 */
	public static void reverseBitsIndividually(byte[] b, int index, int length)
	{
		if((b == null) || (b.length == 0))
		{
			return;
		}
		
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (b.length - index))
		{
			length = b.length - index;
		}
		if(length <= 0)
		{
			return;
		}
		
		for(int i = index; i < (index + length); i++)
		{
			byte b2 = 0;
			for(int position = 7; position >= 0; position--)
			{
				b2 += ((b[i] & 1) << position);
				b[i] >>= 1;
			}
			b[i] = b2;
		}
	}
	
	/**
	 * Reverses every bit (as all bytes as a whole).<br>
	 * <br>
	 * 12345678 90111213 becomes 31211109 87654321<br>
	 * 10100000 00110000 becomes 00001100 00000101
	 */
	public static void reverseBitsCombined(byte[] b)
	{
		if((b == null) || (b.length == 0))
		{
			return;
		}
		reverseBitsIndividually(b);
		int i = 0;
		int j = b.length - 1;
		byte tmp;
		while(j > i)
		{
			tmp = b[j];
			b[j] = b[i];
			b[i] = tmp;
			j--;
			i++;
		}
	}
	/**
	 * Reverses every bit (as all bytes as a whole).<br>
	 * <br>
	 * 12345678 90111213 becomes 31211109 87654321<br>
	 * 10100000 00110000 becomes 00001100 00000101
	 */
	public static void reverseBitsCombined(byte[] b, int index, int length)
	{
		if((b == null) || (b.length == 0))
		{
			return;
		}
		
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (b.length - index))
		{
			length = b.length - index;
		}
		if(length <= 0)
		{
			return;
		}
		
		reverseBitsIndividually(b, index, length);
		
		int i = index;
		int j = length - 1;
		byte tmp;
		while(j > i)
		{
			tmp = b[j];
			b[j] = b[i];
			b[i] = tmp;
			j--;
			i++;
		}
	}
	
	
	/**
	 * Generates a Pearson hash, returns the given hashLength number of bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] pearson(byte[] bytes, int hashLength)
	{
		return HashingPearson.generatePearsonHash(bytes, hashLength);
	}
	/**
	 * Generates a Pearson hash, returns the given hashLength number of bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] pearson(byte[] bytes, int index, int length, int hashLength)
	{
		return HashingPearson.generatePearsonHash(bytes, index, length, hashLength);
	}
	
	/**
	 * Generates a MD5 hash, always returns 16 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] md5(byte[] bytes)
	{
		return hash(hashingDigestMd5.get(), bytes);
	}
	/**
	 * Generates a MD5 hash, always returns 16 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] md5(byte[] bytes, int index, int length)
	{
		return hash(hashingDigestMd5.get(), bytes, index, length);
	}
	
	/**
	 * Generates a SHA1 hash, always returns 20 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha1(byte[] bytes)
	{
		return hash(hashingDigestSha1.get(), bytes);
	}
	/**
	 * Generates a SHA1 hash, always returns 20 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha1(byte[] bytes, int index, int length)
	{
		return hash(hashingDigestSha1.get(), bytes, index, length);
	}
	
	/**
	 * Generates a SHA-256 hash, always returns 32 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha256(byte[] bytes)
	{
		return hash(hashingDigestSha256.get(), bytes);
	}
	/**
	 * Generates a SHA-256 hash, always returns 32 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha256(byte[] bytes, int index, int length)
	{
		return hash(hashingDigestSha256.get(), bytes, index, length);
	}
	
	/**
	 * Generates a SHA-512 hash, always returns 64 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha512(byte[] bytes)
	{
		return hash(hashingDigestSha512.get(), bytes);
	}
	/**
	 * Generates a SHA-512 hash, always returns 64 bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] sha512(byte[] bytes, int index, int length)
	{
		return hash(hashingDigestSha512.get(), bytes, index, length);
	}
	
	/**
	 * Generates a BCrypt hash, always returns 24 bytes.<br>
	 * <br>
	 * If the given bytes contain more than 72 bytes, only the first 72 bytes will be used.<br>
	 * <br>
	 * The given salt needs to be 16 bytes.<br>
	 * The given strength needs to be between 4 and 30, a strength between 10 and 12 is recommended.<br>
	 * <br>
	 * If these conditions aren't met, this function will return an empty byte array.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] bcrypt(byte[] bytes, byte[] salt, int strength)
	{
		return new HashingBCrypt().hash(bytes, salt, strength);
	}
	/**
	 * Generates a BCrypt hash, always returns 24 bytes.<br>
	 * <br>
	 * If the given bytes contain more than 72 bytes, only the first 72 bytes will be used.<br>
	 * <br>
	 * The given salt needs to be 16 bytes.<br>
	 * The given strength needs to be between 4 and 30, a strength between 10 and 12 is recommended.<br>
	 * <br>
	 * If these conditions aren't met, this function will return an empty byte array.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] bcrypt(byte[] bytes, int index, int length, byte[] salt, int strength)
	{
		return new HashingBCrypt().hash(bytesSubArray(bytes, index, length), salt, strength);
	}
	
	/**
	 * Creates Hashcash hashes, each will have a variable amount of characters, this function will never return null.<br>
	 * <br>
	 * The strength (or value) of the Hashcash hashes is determined by the amount of given bits.<br>
	 * <br>
	 * 20 is an average amount of bits.<br>
	 * 22 is a good amount of bits.<br>
	 * 24 is a very good amount of bits.<br>
	 * <br>
	 * The given 'resources' are basically IDs of the service you are 'buying' with this Hashcash, like actions or an email addresses or whatever, something that is unique-ish but not necessarily unique.<br>
	 * <br>
	 * Hashcash hashes are only valid for a certain amount of time (depending on the receiver of the Hashcash), this function uses the current date and time as the creation date of the Hashcash hashes (which is what you normally want).<br>
	 * <br>
	 * You can validate Hashcash hashes with {@link #parseHashcash(String[] hashcashes)}.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String[] hashcash(String[] resources, int bits)
	{
		return HashingHashcash.hash(resources, null, bits);
	}
	/**
	 * Creates Hashcash hashes, each will have a variable amount of characters, this function will never return null.<br>
	 * <br>
	 * The strength (or value) of the Hashcash hashes is determined by the amount of given bits.<br>
	 * <br>
	 * 20 is an average amount of bits.<br>
	 * 22 is a good amount of bits.<br>
	 * 24 is a very good amount of bits.<br>
	 * <br>
	 * The given 'resources' are basically IDs of the service you are 'buying' with this Hashcash, like actions or an email addresses or whatever, something that is unique-ish but not necessarily unique.<br>
	 * <br>
	 * Hashcash hashes are only valid for a certain amount of time (depending on the receiver of the Hashcash), the given Date is used as the creation date of the Hashcash hashes.<br>
	 * <br>
	 * You can validate Hashcash hashes with {@link #parseHashcash(String[] hashcashes)}.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String[] hashcash(String[] resources, Date date, int bits)
	{
		return HashingHashcash.hash(resources, date, bits);
	}
	
	/**
	 * Creates a Hashcash hash, returns a variable amount of characters, will never return null.<br>
	 * <br>
	 * The strength (or value) of the Hashcash hash is determined by the amount of given bits.<br>
	 * <br>
	 * 20 is an average amount of bits.<br>
	 * 22 is a good amount of bits.<br>
	 * 24 is a very good amount of bits.<br>
	 * <br>
	 * The given 'resource' is basically an ID of the service you are 'buying' with this Hashcash, like an action or an email address or whatever, something that is unique-ish but not necessarily unique.<br>
	 * <br>
	 * Hashcash hashes are only valid for a certain amount of time (depending on the receiver of the Hashcash), this function uses the current date and time as the creation date of the Hashcash hash (which is what you normally want).<br>
	 * <br>
	 * You can validate a Hashcash hash with {@link #parseHashcash(String hashcash)}.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String hashcash(String resource, int bits)
	{
		return HashingHashcash.hash(resource, null, bits);
	}
	/**
	 * Creates Hashcash hashes, returns a variable amount of characters, will never return null.<br>
	 * <br>
	 * The strength (or value) of the Hashcash hash is determined by the amount of given bits.<br>
	 * <br>
	 * 20 is an average amount of bits.<br>
	 * 22 is a good amount of bits.<br>
	 * 24 is a very good amount of bits.<br>
	 * <br>
	 * The given 'resource' is basically an ID of the service you are 'buying' with this Hashcash, like an action or an email address or whatever, something that is unique-ish but not necessarily unique.<br>
	 * <br>
	 * Hashcash hashes are only valid for a certain amount of time (depending on the receiver of the Hashcash), the given Date is used as the creation date of the Hashcash hash.<br>
	 * <br>
	 * You can validate a Hashcash hash with {@link #parseHashcash(String hashcash)}.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String hashcash(String resource, Date date, int bits)
	{
		return HashingHashcash.hash(resource, date, bits);
	}
	
	/**
	 * Parses and validates a Hashcash hash, will never return null.<br>
	 * <br>
	 * To successfully validate a Hashcash hash, do the following:<br>
	 * 1) call this method to parse the hashes<br>
	 * 2) check if the returned ParsedHashcashes return true for isValid()<br>
	 * 3) check if the amount of bits of the returned ParsedHashcashes are of a desired number<br>
	 * 4) check if the resources of the returned ParsedHashcashes matches the expected string<br>
	 * 5) check if the dates of the returned ParsedHashcashes are not in the future and are not too long ago<br>
	 * 6) check if the hash hasn't been used already (save the used Hashcash hashes in a HashSet or in a database for example)<br>
	 * <br>
	 * You can change the order of actions if desired.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static ParsedHashcash[] parseHashcash(String[] hashes)
	{
		return HashingHashcash.parse(hashes);
	}
	/**
	 * Parses and validates a Hashcash hash, will never return null.<br>
	 * <br>
	 * To successfully validate a Hashcash hash, do the following:<br>
	 * 1) call this method to parse the hash<br>
	 * 2) check if the returned ParsedHashcash returns true for isValid()<br>
	 * 3) check if the amount of bits of the returned ParsedHashcash is of a desired number<br>
	 * 4) check if the resource of the returned ParsedHashcash matches the expected string<br>
	 * 5) check if the date of the returned ParsedHashcash is not in the future and is not too long ago<br>
	 * 6) check if the hash hasn't been used already (save the used Hashcash hashes in a HashSet or in a database for example)<br>
	 * <br>
	 * You can change the order of actions if desired.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static ParsedHashcash parseHashcash(String hash)
	{
		return HashingHashcash.parse(hash);
	}
	
	/**
	 * Generates a hash, returns 0 bytes on failure.<br>
	 * <br>
	 * It is advised to use {@link #md5(byte[])}, {@link #sha1(byte[])} or {@link #bcrypt(byte[], byte[], int)} instead, since they should always work.
	 */
	public static byte[] hash(String algorithm, byte[] bytes)
	{
		try
		{
			return hash(MessageDigest.getInstance(algorithm), bytes);
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	/**
	 * Generates a hash, returns 0 bytes on failure.<br>
	 * <br>
	 * It is advised to use {@link #md5(byte[])}, {@link #sha1(byte[])} or {@link #bcrypt(byte[], byte[], int)} instead, since they should always work.
	 */
	public static byte[] hash(MessageDigest hasher, byte[] bytes)
	{
		try
		{
			hasher.reset();
			if((bytes != null) && (bytes.length > 0))
			{
				hasher.update(bytes);
			}
			return hasher.digest();
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	/**
	 * Generates a hash, returns 0 bytes on failure.<br>
	 * <br>
	 * It is advised to use {@link #md5(byte[], int, int)} or {@link #sha1(byte[], int, int)} instead, since they should always work.
	 */
	public static byte[] hash(String algorithm, byte[] bytes, int index, int length)
	{
		try
		{
			return hash(MessageDigest.getInstance(algorithm), bytes, index, length);
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	/**
	 * Generates a hash, returns 0 bytes on failure.<br>
	 * <br>
	 * It is advised to use {@link #md5(byte[], int, int)} or {@link #sha1(byte[], int, int)} instead, since they should always work.
	 */
	public static byte[] hash(MessageDigest hasher, byte[] bytes, int index, int length)
	{
		if(bytes == null)
		{
			length = 0;
		}
		else
		{
			if(index < 0)
			{
				length += index;
				index = 0;
			}
			if(length > (bytes.length - index))
			{
				length = bytes.length - index;
			}
		}
		
		try
		{
			hasher.reset();
			if(length > 0)
			{
				hasher.update(bytes, index, length);
			}
			return hasher.digest();
		}
		catch(Exception e)
		{
			return new byte[0];
		}
	}
	
	
	/**
	 * Creates a string with the given length.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static String createString(int length, String filler)
	{
		if(length <= 0)
		{
			return "";
		}
		
		int steps = filler.length();
		if(steps <= 0)
		{
			filler = " ";
			steps = filler.length();
		}
		int overflow = (steps - (length % steps)) % steps;
		
		StringBuilder stringBuilder = new StringBuilder();
		if(overflow <= 0)
		{
			for(int i = 0; i < length; i += steps)
			{
				stringBuilder.append(filler);
			}
		}
		else
		{
			for(int i = steps; i < length; i += steps)
			{
				stringBuilder.append(filler);
			}
			stringBuilder.append(filler, 0, (steps - overflow));
		}
		return stringBuilder.toString();
	}
	
	
	/**
	 * Creates a new SimpleByteDataReader.
	 */
	public static SimpleByteDataReader readSimpleByteData(byte[] bytes)
	{
		return new SimpleByteArrayDataReader(bytes);
	}
	
	/**
	 * Creates a new SimpleByteDataReader.
	 */
	public static SimpleByteDataReader readSimpleByteData(byte[] bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new SimpleByteArrayDataReader(null);
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
		if(index == 0)
		{
			return new SimpleByteArrayDataReader(bytes, length);
		}
		return new SimpleByteSubArrayDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new SimpleByteDataReader.
	 */
	public static SimpleByteDataReader readSimpleByteData(ByteBuffer bytes)
	{
		if((bytes == null) || (bytes.position() <= 0))
		{
			return new SimpleByteBufferDataReader(bytes);
		}
		return new SimpleByteSubBufferDataReader(bytes, bytes.position(), bytes.remaining());
	}
	
	/**
	 * Creates a new SimpleByteDataReader.
	 */
	public static SimpleByteDataReader readSimpleByteData(ByteBuffer bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new SimpleByteBufferDataReader(null);
		}
		index += bytes.position();
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (bytes.remaining() - index))
		{
			length = bytes.remaining() - index;
		}
		if(index == 0)
		{
			return new SimpleByteBufferDataReader(bytes, length);
		}
		return new SimpleByteSubBufferDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new SimpleByteDataWriter.
	 */
	public static SimpleByteDataWriter writeSimpleByteData()
	{
		return new SimpleByteStreamDataWriter();
	}
	
	/**
	 * Creates a new SimpleByteDataWriter.
	 */
	public static SimpleByteDataWriter writeSimpleByteData(ByteBuffer buffer)
	{
		return new SimpleByteBufferDataWriter(buffer);
	}
	
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static ByteDataReader readByteData(byte[] bytes)
	{
		return new ByteArrayDataReader(bytes);
	}
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static ByteDataReader readByteData(byte[] bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new ByteArrayDataReader(null);
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
		if(index == 0)
		{
			return new ByteArrayDataReader(bytes, length);
		}
		return new ByteSubArrayDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static ByteDataReader readByteData(ByteBuffer bytes)
	{
		if((bytes == null) || (bytes.position() <= 0))
		{
			return new ByteBufferDataReader(bytes);
		}
		return new ByteSubBufferDataReader(bytes, bytes.position(), bytes.remaining());
	}
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static ByteDataReader readByteData(ByteBuffer bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new ByteBufferDataReader(null);
		}
		index += bytes.position();
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (bytes.remaining() - index))
		{
			length = bytes.remaining() - index;
		}
		if(index == 0)
		{
			return new ByteBufferDataReader(bytes, length);
		}
		return new ByteSubBufferDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new ByteDataWriter.
	 */
	public static ByteDataWriter writeByteData()
	{
		return new ByteStreamDataWriter();
	}
	
	/**
	 * Creates a new ByteDataWriter.
	 */
	public static ByteDataWriter writeByteData(ByteBuffer buffer)
	{
		return new ByteBufferDataWriter(buffer);
	}
	
	
	/**
	 * Creates a new BitDataReader.
	 */
	public static BitDataReader readBitData(byte[] bytes)
	{
		return new BitArrayDataReader(bytes);
	}
	
	/**
	 * Creates a new BitDataReader.
	 */
	public static BitDataReader readBitData(byte[] bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new BitArrayDataReader(null);
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
		if(index == 0)
		{
			return new BitArrayDataReader(bytes, length);
		}
		return new BitSubArrayDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static BitDataReader readBitData(ByteBuffer bytes)
	{
		if((bytes == null) || (bytes.position() <= 0))
		{
			return new BitBufferDataReader(bytes);
		}
		return new BitSubBufferDataReader(bytes, bytes.position(), bytes.remaining());
	}
	
	/**
	 * Creates a new ByteDataReader.
	 */
	public static BitDataReader readBitData(ByteBuffer bytes, int index, int length)
	{
		if(bytes == null)
		{
			return new BitBufferDataReader(null);
		}
		index += bytes.position();
		if(index < 0)
		{
			length += index;
			index = 0;
		}
		if(length > (bytes.remaining() - index))
		{
			length = bytes.remaining() - index;
		}
		if(index == 0)
		{
			return new BitBufferDataReader(bytes, length);
		}
		return new BitSubBufferDataReader(bytes, index, length);
	}
	
	/**
	 * Creates a new BitDataWriter.<br>
	 * <br>
	 * This is basically a ByteDataWriter, except that it adds certain methods, like methods to add individual bits, and methods to only add a certain number of bits of a given byte or integer.<br>
	 * <br>
	 * Booleans are also always stored in 1 bit with a BitDataWriter, with a ByteDataWriter it will take 1 full byte to store a boolean.<br>
	 * <br>
	 * This allows you to create smaller results than you can with the ByteDataWriter, but it costs a little bit more processing power to write data with the BitDataWriter, and it also costs a little bit more processing power to read data with the BitDataReader.
	 */
	public static BitDataWriter writeBitData()
	{
		return new BitStreamDataWriter();
	}
	
	/**
	 * Creates a new BitDataWriter.<br>
	 * <br>
	 * This is basically a ByteDataWriter, except that it adds certain methods, like methods to add individual bits, and methods to only add a certain number of bits of a given byte or integer.<br>
	 * <br>
	 * Booleans are also always stored in 1 bit with a BitDataWriter, with a ByteDataWriter it will take 1 full byte to store a boolean.<br>
	 * <br>
	 * This allows you to create smaller results than you can with the ByteDataWriter, but it costs a little bit more processing power to write data with the BitDataWriter, and it also costs a little bit more processing power to read data with the BitDataReader.
	 */
	public static BitDataWriter writeBitData(ByteBuffer buffer)
	{
		return new BitBufferDataWriter(buffer);
	}
	
	
	/**
	 * Parses the given JSON string into a JsonNode, which can be used to traverse the JSON data.<br>
	 * <br>
	 * Returns null on failure.
	 */
	public static JsonNode parseJsonString(String json)
	{
		try
		{
			return JSON_READER.readTree(json);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Serializes the given object into a JSON string.<br>
	 * The given object could be anything, usually it is a HashMap or an array.<br>
	 * <br>
	 * Returns an empty string on failure.
	 */
	public static String toJsonString(Object object)
	{
		return toJsonString(object, false);
	}
	/**
	 * Serializes the given object into a JSON string.<br>
	 * The given object could be anything, usually it is a HashMap or an array.<br>
	 * <br>
	 * Returns an empty string on failure.
	 */
	public static String toJsonString(Object object, boolean prettyPrint)
	{
		try
		{
			if(prettyPrint)
			{
				return JSON_WRITER_READABILITY.writeValueAsString(object);
			}
			else
			{
				return JSON_WRITER_PERFORMANCE.writeValueAsString(object);
			}
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	/**
	 * Turns the child nodes into JsonObjectItems.
	 */
	public static ArrayList<JsonObjectItem> getObjectNodes(JsonNode node)
	{
		ArrayList<JsonObjectItem> list = new ArrayList<>();
		if(node == null)
		{
			return list;
		}
		if(node.isObject())
		{
			Iterator<Entry<String,JsonNode>> iterator = node.fields();
			while(iterator.hasNext())
			{
				Entry<String,JsonNode> entry = iterator.next();
				list.add(new JsonObjectItem(entry.getKey(), entry.getValue()));
			}
		}
		else
		{
			int i = 0;
			for(JsonNode subnode : node)
			{
				list.add(new JsonObjectItem("" + i, subnode));
				i++;
			}
		}
		return list;
	}
	
	/**
	 * Turns the child nodes into JsonArrayItems.
	 */
	public static ArrayList<JsonArrayItem> getArrayNodes(JsonNode node)
	{
		ArrayList<JsonArrayItem> list = new ArrayList<>();
		if(node == null)
		{
			return list;
		}
		int i = 0;
		for(JsonNode subnode : node)
		{
			list.add(new JsonArrayItem(i, subnode));
			i++;
		}
		return list;
	}
	
	/**
	 * Prints the entire node tree, for debugging purposes.
	 */
	public static void printNodeTree(JsonNode node)
	{
		System.out.println(toJsonString(node, true));
	}
	
	
	/**
	 * Parses YAML into an object.
	 */
	public static <T> T parseYamlStringIntoObject(String yaml, Class<T> objectType) throws Exception
	{
		return YAML.readValue(yaml, objectType);
	}
	
	/**
	 * Serializes the given object into a YAML string.<br>
	 * <br>
	 * Returns an empty string on failure.
	 */
	public static String toYamlString(Object object)
	{
		try
		{
			return YAML.writeValueAsString(object);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	
	
	/**
	 * Encrypts the given bytes with AES, without a validation hash.<br>
	 * <br>
	 * AES-128 will be used if the given key is &lt;= 16 bytes (the given key will be converted to 16 bytes then).<br>
	 * AES-192 will be used if the given key is &gt; 16 bytes and &lt;= 24 bytes (the given key will be converted to 24 bytes then).<br>
	 * AES-256 will be used if the given key is &gt; 25 bytes (the given key will be converted to 32 bytes then).<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] encryptAes(byte[] data, byte[] key)
	{
		return EncryptionAes.encryptBytes(data, key, false);
	}
	/**
	 * Encrypts the given bytes with AES.<br>
	 * <br>
	 * AES-128 will be used if the given key is &lt;= 16 bytes (the given key will be converted to 16 bytes then).<br>
	 * AES-192 will be used if the given key is &gt; 16 bytes and &lt;= 24 bytes (the given key will be converted to 24 bytes then).<br>
	 * AES-256 will be used if the given key is &gt; 25 bytes (the given key will be converted to 32 bytes then).<br>
	 * <br>
	 * You can choose to add a hash to the encrypted data. With it, the decryption function can determine if the data was (probably) not modified.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] encryptAes(byte[] data, byte[] key, boolean addValidationHash)
	{
		return EncryptionAes.encryptBytes(data, key, addValidationHash);
	}
	
	/**
	 * Decrypts the given bytes with AES, without a validation hash.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] decryptAes(byte[] data, byte[] key)
	{
		return EncryptionAes.decryptBytes(data, key, false);
	}
	/**
	 * Decrypts the given bytes with AES.<br>
	 * <br>
	 * If (during encryption) you've added a hash to the data, you'll have to give pass true for 'addedValidationHash'.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] decryptAes(byte[] data, byte[] key, boolean addedValidationHash)
	{
		return EncryptionAes.decryptBytes(data, key, addedValidationHash);
	}
	
	/**
	 * Creates an AES key, which can be used to encrypt and decrypt faster.<br>
	 * <br>
	 * This is a tradeoff for CPU vs RAM.<br>
	 * <br>
	 * Encrypting and decrypting with an AesKey will be 10% to 50% faster, depending on the side of the data to encrypt or decrypt.<br>
	 * Doing small data encrypts and decrypts (under 128 bytes) with AesKeys get the maximum performance increasement, and it is recommended to use AesKeys then.<br>
	 * Doing big data encrypts and decrypts (1024+ bytes) with AesKeys yields almost no performance increasement, and it is not recommended to spend memory on storing AesKeys then.<br>
	 * <br>
	 * An AES-128 key will be created if the given key is &lt;= 16 bytes (the given key will be converted to 16 bytes then).<br>
	 * An AES-192 key will be created if the given key is &gt; 16 bytes and &lt;= 24 bytes (the given key will be converted to 24 bytes then).<br>
	 * An AES-256 key will be created if the given key is &gt; 25 bytes (the given key will be converted to 32 bytes then).<br>
	 */
	public static AesKey createAesKey(byte[] key)
	{
		return EncryptionAes.createKey(key);
	}
	
	/**
	 * Encrypts the given bytes with AES, without a validation hash.<br>
	 * <br>
	 * Create an AES key with {@link #createAesKey(byte[] key)}<br>
	 * <br>
	 * What AES it will use depends on the given key, see {@link #createAesKey(byte[] key)}.
	 */
	public static byte[] encryptAes(byte[] data, AesKey key)
	{
		return EncryptionAes.encrypt(data, key, false);
	}
	/**
	 * Encrypts the given bytes with AES.<br>
	 * <br>
	 * Create an AES key with {@link #createAesKey(byte[] key)}<br>
	 * <br>
	 * What AES it will use depends on the given key, see {@link #createAesKey(byte[] key)}.<br>
	 * <br>
	 * You can choose to add a hash to the encrypted data. With it, the decryption function can determine if the data was (probably) not modified.
	 */
	public static byte[] encryptAes(byte[] data, AesKey key, boolean addValidationHash)
	{
		return EncryptionAes.encrypt(data, key, addValidationHash);
	}
	
	/**
	 * Decrypts the given bytes with AES, without a validation hash.<br>
	 * <br>
	 * Create an AES key with {@link #createAesKey(byte[] key)}
	 */
	public static byte[] decryptAes(byte[] data, AesKey key)
	{
		return EncryptionAes.decrypt(data, key, false);
	}
	/**
	 * Decrypts the given bytes with AES.<br>
	 * <br>
	 * Create an AES key with {@link #createAesKey(byte[] key)}<br>
	 * <br>
	 * If (during encryption) you've added a hash to the data, you'll have to give pass true for 'addedValidationHash'.
	 */
	public static byte[] decryptAes(byte[] data, AesKey key, boolean addedValidationHash)
	{
		return EncryptionAes.decrypt(data, key, addedValidationHash);
	}
	
	
	/**
	 * Generates a public and private key for RSA.<br>
	 * <br>
	 * The amount if bits determine the maximum amount of data you can encrypt with the generated keys.<br>
	 * The maximum amount of bytes you can encrypt is: (bits / 8) - 34.<br>
	 * <br>
	 * 512 bits = max 30 bytes of data<br>
	 * 1024 bits = max 94 bytes of data<br>
	 * 2048 bits = max 222 bytes of data<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static RsaKeys generateRsaKeys(int bits)
	{
		return EncryptionRsa.generateKeys(bits);
	}
	
	/**
	 * Encrypts the given bytes with the RSA public key.<br>
	 * <br>
	 * The maximum amount of bytes the given data can have is: (rsa keys bits / 8) - 34.<br>
	 * If the given data is longer, it will be cut off to the maximum amount of bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] encryptRsa(byte[] data, RsaPublicKey publicKey)
	{
		return EncryptionRsa.encrypt(data, publicKey);
	}
	
	/**
	 * Decrypts the given bytes with the RSA private key.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] decryptRsa(byte[] data, RsaPrivateKey privateKey)
	{
		return EncryptionRsa.decrypt(data, privateKey);
	}
	
	/**
	 * Returns a signature of the given hash with the RSA private key.<br>
	 * <br>
	 * The maximum amount of bytes the given hash can have is: (rsa keys bits / 16) - 3.<br>
	 * If the given hash is longer, this function will fail, causing it to return an empty byte array.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] signRsa(byte[] hash, RsaPrivateKey privateKey)
	{
		return EncryptionRsa.sign(hash, privateKey);
	}
	
	/**
	 * Verifies that the given signature can be decoded with the RSA public key, and that it has the expected hash in it.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static boolean verifySignatureRsa(byte[] signature, byte[] expectedHash, RsaPublicKey publicKey)
	{
		return EncryptionRsa.verifySignature(signature, expectedHash, publicKey);
	}
	
	/**
	 * Converts the given RSA public key to (variable amount) of bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] rsaPublicKeyToBytes(RsaPublicKey publicKey)
	{
		return EncryptionRsa.publicKeyToBytes(publicKey);
	}
	
	/**
	 * Converts the given bytes to a RSA public key.<br>
	 * <br>
	 * Can return null if it fails.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static RsaPublicKey bytesToRsaPublicKey(byte[] bytes)
	{
		return EncryptionRsa.bytesToPublicKey(bytes);
	}
	
	/**
	 * Converts the given bytes to a RSA public key.<br>
	 * <br>
	 * Can return null if it fails.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static RsaPublicKey bytesToRsaPublicKey(byte[] bytes, int index, int length)
	{
		return EncryptionRsa.bytesToPublicKey(bytes, index, length);
	}
	
	/**
	 * Converts the given RSA private key to (variable amount) of bytes.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] rsaPrivateKeyToBytes(RsaPrivateKey privateKey)
	{
		return EncryptionRsa.privateKeyToBytes(privateKey);
	}
	
	/**
	 * Converts the given bytes to a RSA public key.<br>
	 * <br>
	 * Can return null if it fails.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static RsaPrivateKey bytesToRsaPrivateKey(byte[] bytes)
	{
		return EncryptionRsa.bytesToPrivateKey(bytes);
	}
	
	/**
	 * Converts the given bytes to a RSA public key.<br>
	 * <br>
	 * Can return null if it fails.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static RsaPrivateKey bytesToRsaPrivateKey(byte[] bytes, int index, int length)
	{
		return EncryptionRsa.bytesToPrivateKey(bytes, index, length);
	}
	
	
	/**
	 * Compresses the given bytes with LZF.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] compressLzf(byte[] data)
	{
		if(data == null)
		{
			return new byte[0];
		}
		return CompressionLzf.compress(data);
	}
	
	/**
	 * Tries to decompress the given bytes with LZF, returns an empty array if it fails.<br>
	 * This method will fail if the given bytes are not compressed with LZF.<br>
	 * <br>
	 * Mimics the UE4 blueprint.
	 */
	public static byte[] decompressLzf(byte[] data)
	{
		if(data == null)
		{
			return new byte[0];
		}
		return CompressionLzf.decompress(data);
	}
	
	
	/**
	 * Deals with unboxing.
	 */
	public static byte[] toByteArray(Collection<Byte> collection)
	{
		if(collection == null)
		{
			return new byte[0];
		}
		byte[] array = new byte[collection.size()];
		int i = 0;
		for(Byte value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static byte[] toByteArray(Byte[] collection)
	{
		if(collection == null)
		{
			return new byte[0];
		}
		byte[] array = new byte[collection.length];
		int i = 0;
		for(Byte value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static short[] toShortArray(Collection<Short> collection)
	{
		if(collection == null)
		{
			return new short[0];
		}
		short[] array = new short[collection.size()];
		int i = 0;
		for(Short value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static short[] toShortArray(Short[] collection)
	{
		if(collection == null)
		{
			return new short[0];
		}
		short[] array = new short[collection.length];
		int i = 0;
		for(Short value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static int[] toIntArray(Collection<Integer> collection)
	{
		if(collection == null)
		{
			return new int[0];
		}
		int[] array = new int[collection.size()];
		int i = 0;
		for(Integer value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static int[] toIntArray(Integer[] collection)
	{
		if(collection == null)
		{
			return new int[0];
		}
		int[] array = new int[collection.length];
		int i = 0;
		for(Integer value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static long[] toLongArray(Collection<Long> collection)
	{
		if(collection == null)
		{
			return new long[0];
		}
		long[] array = new long[collection.size()];
		int i = 0;
		for(Long value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static long[] toLongArray(Long[] collection)
	{
		if(collection == null)
		{
			return new long[0];
		}
		long[] array = new long[collection.length];
		int i = 0;
		for(Long value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static float[] toFloatArray(Collection<Float> collection)
	{
		if(collection == null)
		{
			return new float[0];
		}
		float[] array = new float[collection.size()];
		int i = 0;
		for(Float value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static float[] toFloatArray(Float[] collection)
	{
		if(collection == null)
		{
			return new float[0];
		}
		float[] array = new float[collection.length];
		int i = 0;
		for(Float value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static double[] toDoubleArray(Collection<Double> collection)
	{
		if(collection == null)
		{
			return new double[0];
		}
		double[] array = new double[collection.size()];
		int i = 0;
		for(Double value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static double[] toDoubleArray(Double[] collection)
	{
		if(collection == null)
		{
			return new double[0];
		}
		double[] array = new double[collection.length];
		int i = 0;
		for(Double value : collection)
		{
			array[i] = ((value != null) ? value : 0);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static boolean[] toBooleanArray(Collection<Boolean> collection)
	{
		if(collection == null)
		{
			return new boolean[0];
		}
		boolean[] array = new boolean[collection.size()];
		int i = 0;
		for(Boolean value : collection)
		{
			array[i] = ((value != null) ? value : false);
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static boolean[] toBooleanArray(Boolean[] collection)
	{
		if(collection == null)
		{
			return new boolean[0];
		}
		boolean[] array = new boolean[collection.length];
		int i = 0;
		for(Boolean value : collection)
		{
			array[i] = ((value != null) ? value : false);
			i++;
		}
		return array;
	}
	
	/**
	 * Deals with unboxing.
	 */
	public static char[] toCharArray(Collection<Character> collection)
	{
		if(collection == null)
		{
			return new char[0];
		}
		char[] array = new char[collection.size()];
		int i = 0;
		for(Character value : collection)
		{
			array[i] = ((value != null) ? value : '\0');
			i++;
		}
		return array;
	}
	/**
	 * Deals with unboxing.
	 */
	public static char[] toCharArray(Character[] collection)
	{
		if(collection == null)
		{
			return new char[0];
		}
		char[] array = new char[collection.length];
		int i = 0;
		for(Character value : collection)
		{
			array[i] = ((value != null) ? value : '\0');
			i++;
		}
		return array;
	}
	/**
	 * Might as well, for consistency.
	 */
	public static String[] toStringArray(Collection<String> collection)
	{
		if(collection == null)
		{
			return new String[0];
		}
		return collection.toArray(new String[0]);
	}
	/**
	 * Might as well, for consistency.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<? extends T> collection, Class<T> theClass)
	{
		if(collection == null)
		{
			return (T[]) Array.newInstance(theClass, 0);
		}
		return collection.toArray((T[]) Array.newInstance(theClass, collection.size()));
	}
	
	
	/**
	 * Reads the external IP address from http://checkip.amazonaws.com.<br>
	 * <br>
	 * It's probably best to cache this value whenever possible.<br>
	 * <br>
	 * Returns null if the external IP address could not be determined.
	 */
	public static String getExternalIp()
	{
		try
		{
			URLConnection connection = new URL("http://checkip.amazonaws.com").openConnection();
			try(InputStream in = connection.getInputStream(); InputStreamReader isr = new InputStreamReader(in); BufferedReader br = new BufferedReader(isr))
			{
				return br.readLine();
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Tries to download the URLs content.<br>
	 * <br>
	 * Returns null on fail.
	 */
	public static String getHttpContent(String url)
	{
		return getHttpContent(url, "\r\n"); // System.lineSeparator()
	}
	
	/**
	 * Tries to download the URLs content.<br>
	 * <br>
	 * Returns null on fail.
	 */
	public static String getHttpContent(String url, String lineSeperator)
	{
		try
		{
			URLConnection connection = new URL(url).openConnection();
			try(InputStream in = connection.getInputStream(); InputStreamReader isr = new InputStreamReader(in); BufferedReader br = new BufferedReader(isr))
			{
				int contentLength = connection.getContentLength();
				if(contentLength < 0)
				{
					contentLength = 0;
				}
				
				StringBuilder content = new StringBuilder(contentLength + 32);
				String inputLine = br.readLine();
				if(inputLine != null)
				{
					content.append(inputLine);
				}
				while((inputLine = br.readLine()) != null)
				{
					content.append(lineSeperator);
					content.append(inputLine);
				}
				return content.toString();
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	/**
	 * Converts the given date into a date string in ISO-8601.
	 */
	public static String getDateStringIso(Date date)
	{
		if(date == null)
		{
			date = new Date(CachedTime.currentTimeMillis());
		}
		return dateformatIso8601.get().format(date);
	}
	
	/**
	 * Converts the given time in milliseconds into a date string in ISO-8601.
	 */
	public static String getDateStringIso(long time)
	{
		return dateformatIso8601.get().format(new Date(time));
	}
	
	
	/**
	 * Returns the number of times the given substring can be found in the given string.
	 */
	public static int stringCountOccurrences(String string, String substring)
	{
		int index = string.indexOf(substring);
		if(index < 0)
		{
			return 0;
		}
		int length = substring.length();
		int maxIndex = string.length() - substring.length();
		int count = 0;
		while((index >= 0) && (index <= maxIndex))
		{
			count++;
			index += length;
			index = string.indexOf(substring, index);
		}
		return count;
	}
	
	
	/**
	 * Allows you to increase a numeric string by one.
	 * This allows you to increase a number without it ever running into a limit (a limit like a maximum value, like integers and longs have).
	 */
	public static String increaseNumericStringByOne(String value)
	{
		char[] chars = value.toCharArray();
		for(int i = chars.length - 1; i >= 0; i--)
		{
			int c = Character.getNumericValue(chars[i]);
			if(c < 9)
			{
				chars[i]++;
				break;
			}
			chars[i] = '0';
		}
		if(chars[0] == '0')
		{
			return "1" + new String(chars);
		}
		return new String(chars);
	}
}
