package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import lowentry.ue4.libs.jackson.core.JsonGenerator;

import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;
import lowentry.ue4.libs.jackson.databind.JsonNode;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

/**
 * Also: default bean access will not do much good with Class.class. But
 * we can just serialize the class name and that should be enough.
 */
@SuppressWarnings("all")
public class ClassSerializer
    extends StdScalarSerializer<Class<?>>
{
    public ClassSerializer() { super(Class.class, false); }

    @Override
    public void serialize(Class<?> value, JsonGenerator g, SerializerProvider provider) throws IOException
    {
        g.writeString(value.getName());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint)
    {
        return createSchemaNode("string", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException
    {
        visitStringFormat(visitor, typeHint);
    }
}