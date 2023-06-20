package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import lowentry.ue4.libs.jackson.core.*;

import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;
import lowentry.ue4.libs.jackson.databind.JsonNode;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.annotation.JacksonStdImpl;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * This is the special serializer for regular {@link java.lang.String}s.
 *<p>
 * Since this is one of "native" types, no type information is ever
 * included on serialization (unlike for most scalar types as of 1.5)
 */
@JacksonStdImpl
@SuppressWarnings("all")
public final class StringSerializer
// NOTE: generic parameter changed from String to Object in 2.6, to avoid
//   use of bridge methods
// In 2.9, removed use of intermediate type `NonTypedScalarSerializerBase`
    extends StdScalarSerializer<Object>
{
    private static final long serialVersionUID = 1L;

    public StringSerializer() { super(String.class, false); }

    @Override
    public boolean isEmpty(SerializerProvider prov, Object value) {
        String str = (String) value;
        return str.length() == 0;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString((String) value);
    }

    @Override
    public final void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
            TypeSerializer typeSer) throws IOException
    {
        // no type info, just regular serialization
        gen.writeString((String) value);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        visitStringFormat(visitor, typeHint);
    }
}
