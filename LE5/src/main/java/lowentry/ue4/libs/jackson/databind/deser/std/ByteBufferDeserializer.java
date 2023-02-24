package lowentry.ue4.libs.jackson.databind.deser.std;

import java.io.*;
import java.nio.ByteBuffer;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.databind.*;
import lowentry.ue4.libs.jackson.databind.util.ByteBufferBackedOutputStream;

@SuppressWarnings("all")
public class ByteBufferDeserializer extends StdScalarDeserializer<ByteBuffer>
{
    private static final long serialVersionUID = 1L;
    
    protected ByteBufferDeserializer() { super(ByteBuffer.class); }

    @Override
    public ByteBuffer deserialize(JsonParser parser, DeserializationContext cx) throws IOException {
        byte[] b = parser.getBinaryValue();
        return ByteBuffer.wrap(b);
    }

    @Override
    public ByteBuffer deserialize(JsonParser jp, DeserializationContext ctxt, ByteBuffer intoValue) throws IOException {
        // Let's actually read in streaming manner...
        OutputStream out = new ByteBufferBackedOutputStream(intoValue);
        jp.readBinaryValue(ctxt.getBase64Variant(), out);
        out.close();
        return intoValue;
    }
}
