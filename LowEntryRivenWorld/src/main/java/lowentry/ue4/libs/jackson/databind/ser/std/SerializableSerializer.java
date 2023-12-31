package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;
import lowentry.ue4.libs.jackson.databind.JsonSerializable;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.annotation.JacksonStdImpl;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * Generic handler for types that implement {@link JsonSerializable}.
 *<p>
 * Note: given that this is used for anything that implements
 * interface, cannot be checked for direct class equivalence.
 */
@JacksonStdImpl
@SuppressWarnings("all")
public class SerializableSerializer
    extends StdSerializer<JsonSerializable>
{
    public final static SerializableSerializer instance = new SerializableSerializer();

    protected SerializableSerializer() { super(JsonSerializable.class); }

    @Override
    public boolean isEmpty(SerializerProvider serializers, JsonSerializable value) {
        if (value instanceof JsonSerializable.Base) {
            return ((JsonSerializable.Base) value).isEmpty(serializers);
        }
        return false;
    }

    @Override
    public void serialize(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        value.serialize(gen, serializers);
    }

    @Override
    public final void serializeWithType(JsonSerializable value, JsonGenerator gen, SerializerProvider serializers,
            TypeSerializer typeSer) throws IOException {
        value.serializeWithType(gen, serializers, typeSer);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException
    {
        visitor.expectAnyFormat(typeHint);
    }
}
