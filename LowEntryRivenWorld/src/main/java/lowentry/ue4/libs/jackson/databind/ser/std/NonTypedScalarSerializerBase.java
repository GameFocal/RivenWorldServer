package lowentry.ue4.libs.jackson.databind.ser.std;

import java.io.IOException;

import lowentry.ue4.libs.jackson.core.*;
import lowentry.ue4.libs.jackson.databind.SerializerProvider;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeSerializer;

/**
 * Intermediate base class for limited number of scalar types
 * that should never include type information. These are "native"
 * types that are default mappings for corresponding JSON scalar
 * types: {@link String}, {@link Integer},
 * {@link Double} and {@link Boolean}.
 */
@Deprecated // since 2.9
@SuppressWarnings("all")
public abstract class NonTypedScalarSerializerBase<T>
    extends StdScalarSerializer<T>
{
    protected NonTypedScalarSerializerBase(Class<T> t) {
        super(t);
    }

    protected NonTypedScalarSerializerBase(Class<?> t, boolean bogus) {
        super(t, bogus);
    }

    @Override
    public final void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider,
            TypeSerializer typeSer) throws IOException
    {
        // no type info, just regular serialization
        serialize(value, gen, provider);            
    }
}
