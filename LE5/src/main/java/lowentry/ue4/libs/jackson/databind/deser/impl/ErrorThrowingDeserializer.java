package lowentry.ue4.libs.jackson.databind.deser.impl;

import java.io.IOException;

import lowentry.ue4.libs.jackson.core.JsonParser;
import lowentry.ue4.libs.jackson.databind.DeserializationContext;
import lowentry.ue4.libs.jackson.databind.JsonDeserializer;

/**
 * A deserializer that stores an {@link Error} caught during constructing
 * of the deserializer, which needs to be deferred and only during actual
 * attempt to deserialize a value of given type.
 * Note that null and empty values can be deserialized without error.
 * 
 * @since 2.9 Note: prior to this version was named <code>NoClassDefFoundDeserializer</code>
 */
@SuppressWarnings("all")
public class ErrorThrowingDeserializer extends JsonDeserializer<Object>
{
    private final Error _cause;

    public ErrorThrowingDeserializer(NoClassDefFoundError cause) {
        _cause = cause;
    }

    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        throw _cause;
    }
}
