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
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * Intermediate base class that serves as base for standard {@link ToStringSerializer}
 * as well as for custom subtypes that want to add processing for converting from
 * value to output into its {@code String} representation (whereas standard version
 * simply calls value object's {@code toString()} method).
 *
 * @since 2.10
 */
@SuppressWarnings("all")
public abstract class ToStringSerializerBase
    extends StdSerializer<Object>
{
    public ToStringSerializerBase(Class<?> handledType) {
        super(handledType, false);
    }

    @Override
    public boolean isEmpty(SerializerProvider prov, Object value) {
        return valueToString(value).isEmpty();
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
        throws IOException
    {
        gen.writeString(valueToString(value));
    }

    /**
     * Default implementation will write type prefix, call regular serialization
     * method (since assumption is that value itself does not need JSON
     * Array or Object start/end markers), and then write type suffix.
     * This should work for most cases; some sub-classes may want to
     * change this behavior.
     */
    @Override
    public void serializeWithType(Object value, JsonGenerator g, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException
    {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g,
                typeSer.typeId(value, JsonToken.VALUE_STRING));
        serialize(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return createSchemaNode("string", true);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
    {
        visitStringFormat(visitor, typeHint);
    }

    public abstract String valueToString(Object value);
}