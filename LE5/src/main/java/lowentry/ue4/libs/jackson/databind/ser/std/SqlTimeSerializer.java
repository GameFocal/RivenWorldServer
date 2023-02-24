package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;

import lowentry.ue4.libs.jackson.core.JsonGenerator;
import lowentry.ue4.libs.jackson.databind.*;
import lowentry.ue4.libs.jackson.databind.annotation.JacksonStdImpl;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import lowentry.ue4.libs.jackson.databind.jsonFormatVisitors.JsonValueFormat;

@JacksonStdImpl
@SuppressWarnings("all")
public class SqlTimeSerializer
    extends StdScalarSerializer<java.sql.Time>
{
    public SqlTimeSerializer() { super(java.sql.Time.class); }

    @Override
    public void serialize(java.sql.Time value, JsonGenerator g, SerializerProvider provider) throws IOException
    {
        g.writeString(value.toString());
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
        return createSchemaNode("string", true);
    }
    
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException
    {
        visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
    }
}