package lowentry.ue4.examples;


import lowentry.ue4.library.LowEntry;


public class ExampleCompression
{
	public static void main(String[] args) throws Throwable
	{
		byte[] data = LowEntry.stringToBytesUtf8("test aaaaaaaadsfdsfgaaaaaaaa test aaaaaaaaaaaaaaedsfdsfgaaaaasdgsdgaaaaaa");
		System.out.println("data (" + data.length + "): " + LowEntry.bytesToHex(data));
		
		byte[] compressed = LowEntry.compressLzf(data);
		System.out.println("compressed (" + compressed.length + "): " + LowEntry.bytesToHex(compressed));
		
		byte[] decompressed = LowEntry.decompressLzf(compressed);
		System.out.println("decompressed (" + decompressed.length + "): " + LowEntry.bytesToHex(decompressed));
	}
}
