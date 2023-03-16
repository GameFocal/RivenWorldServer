package lowentry.ue4.examples;


import lowentry.ue4.classes.ByteDataReader;
import lowentry.ue4.classes.ByteDataWriter;
import lowentry.ue4.library.LowEntry;


public class ExampleByteData
{
	public static void main(final String[] args) throws Throwable
	{
		ByteDataWriter writer = LowEntry.writeByteData();
		
		
		// checked type functions
		writer.addInteger(50);
		writer.addInteger(60);
		writer.addInteger(70);
		writer.addInteger(80);
		writer.addInteger(90);
		
		writer.addStringUtf8("test");
		writer.addStringUtf8("test2");
		writer.addStringUtf8("test3");
		
		writer.addBooleanArray(new boolean[]{true, true, false, false});
		
		writer.addStringUtf8Array(new String[]{"a1", "a2", "b1", "b2", "c1", "c2"});
		
		
		// ambiguous functions
		writer.add(50);
		writer.add(60);
		
		writer.add("test2");
		
		writer.add(new int[]{50, 60, 70, 80, 90, 100});
		
		
		// to bytes
		byte[] bytes = writer.getBytes();
		
		
		
		
		// shorter way of doing it
		byte[] bytes2 = LowEntry.writeByteData().add(50).add(60).add("test2").add(new int[]{50, 60, 70, 80, 90, 100}).getBytes();
		
		
		
		
		System.out.println("reading bytes:");
		ByteDataReader reader = LowEntry.readByteData(bytes);
		
		
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		
		System.out.println(reader.getStringUtf8());
		System.out.println(reader.getStringUtf8());
		System.out.println(reader.getStringUtf8());
		
		System.out.println(LowEntry.toJsonString(reader.getBooleanArray()));
		
		System.out.println(LowEntry.toJsonString(reader.getStringUtf8Array()));
		
		
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		
		System.out.println(reader.getStringUtf8());
		
		System.out.println(LowEntry.toJsonString(reader.getIntegerArray()));
		
		
		
		
		System.out.println();
		System.out.println("reading bytes 2:");
		reader = LowEntry.readByteData(bytes2);
		
		
		System.out.println(reader.getInteger());
		System.out.println(reader.getInteger());
		
		System.out.println(reader.getStringUtf8());
		
		System.out.println(LowEntry.toJsonString(reader.getIntegerArray()));
	}
}
