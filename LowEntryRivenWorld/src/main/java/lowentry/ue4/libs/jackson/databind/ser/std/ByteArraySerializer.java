package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import lowentry.ue4.libs.jackson.core.JsonGenerator;
import lowentry.ue4.libs.jackson.core.JsonToken;
import lowentry.ue4.libs.jackson.core.type.WritableTypeId;
import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;
import lowentry.ue4.libs.jackson.databind.JsonNode;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.annotation.JacksonStdImpl;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;
import lowentry.ue4.libs.jackson.databind.node.ObjectNode;

/**
 * Unlike other integral number array serializers, we do not just print out byte values
 * as numbers. Instead, we assume that it would make more sense to output content
 * as base64 encoded bytes (using default base64 encoding).
 *<p>
 * NOTE: since it is NOT serialized as an array, cannot use AsArraySerializer as base
 *<p>
 * NOTE: since 2.6, has been a main-level class; earlier was embedded in
 * {@link StdArraySerializers}.
 */
@JacksonStdImpl
@SuppressWarnings("all")
public class ByteArraySerializer extends StdSerializer<byte[]>
{
    private static final long serialVersionUID = 1L;

    public ByteArraySerializer() {
        super(byte[].class);
    }
    
    @Override
    public boolean isEmpty(SerializerProvider prov, byte[] value) {
        return value.length == 0;
    }
    
    @Override
    public void serialize(byte[] value, JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
        g.writeBinary(provider.getConfig().getBase64Variant(),
                value, 0, value.length);
    }

    @Override
    public void serializeWithType(byte[] value, JsonGenerator g, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException
    {
        // most likely scalar
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(value, JsonToken.VALUE_EMBEDDED_OBJECT));
        g.writeBinary(provider.getConfig().getBase64Variant(),
                value, 0, value.length);
        typeSer.writeTypeSuffix(g, typeIdDef);

        /* OLD impl
        typeSer.writeTypePrefixForScalar(value, g);
        g.writeBinary(provider.getConfig().getBase64Variant(),
                value, 0, value.length);
        typeSer.writeTypeSuffixForScalar(value, g);
        */
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        ObjectNode o = createSchemaNode("array", true);
        ObjectNode itemSchema = createSchemaNode("byte"); //binary values written as strings?
        return o.set("items", itemSchema);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException
    {
        // 14-Mar-2016, tatu: while logically (and within JVM) binary, gets encoded as Base64 String,
        // let's try to indicate it is array of Bytes... difficult, thanks to JSON Schema's
        // lackluster listing of types
        //
        // TODO: for 2.8, make work either as String/base64, or array of numbers,
        //   with a qualifier that can be used to determine it's byte[]
        JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
        if (v2 != null) {
            v2.itemsFormat(JsonFormatTypes.INTEGER);
        }
    }
}
