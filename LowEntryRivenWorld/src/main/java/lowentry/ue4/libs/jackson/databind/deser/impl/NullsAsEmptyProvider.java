package lowentry.ue4.libs.jackson.databind.deser.impl;

import lowentry.ue4.libs.jackson.databind.*;
import lowentry.ue4.libs.jackson.databind.deser.NullValueProvider;
import lowentry.ue4.libs.jackson.databind.exc.InvalidNullException;
import lowentry.ue4.libs.jackson.databind.util.AccessPattern;

/**
 * Simple {@link NullValueProvider} that will always throw a
 * {@link InvalidNullException} when a null is encountered.
 */
@SuppressWarnings("all")
public class NullsAsEmptyProvider
    implements NullValueProvider, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    protected final JsonDeserializer<?> _deserializer;

    public NullsAsEmptyProvider(JsonDeserializer<?> deser) {
        _deserializer = deser;
    }

    @Override
    public AccessPattern getNullAccessPattern() {
        return AccessPattern.DYNAMIC;
    }

    @Override
    public Object getNullValue(DeserializationContext ctxt)
            throws JsonMappingException {
        return _deserializer.getEmptyValue(ctxt);
    }
}
