package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.databind.*;

/**
 * Specialized serializer that can be used as the generic key serializer,
 * when serializing {@link java.util.Map}s to JSON Objects.
 *
 * @deprecated Since 2.8, use {@link StdKeySerializers.Default} instead.
 */
@Deprecated // since 2.8,
@SuppressWarnings("all")
public class StdKeySerializer extends StdSerializer<Object>
{
    public StdKeySerializer() { super(Object.class); }

    @Override
    public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
        // 19-Oct-2016, tatu: Simplified to bare essentials since this is deprecated
        g.writeFieldName(value.toString());
    }
}
