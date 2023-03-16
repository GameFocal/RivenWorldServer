package lowentry.ue4.libs.jackson.databind.deser.std;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import lowentry.ue4.libs.jackson.core.JsonParser;
import lowentry.ue4.libs.jackson.databind.DeserializationContext;

@SuppressWarnings("all")
public class AtomicBooleanDeserializer extends StdScalarDeserializer<AtomicBoolean>
{
    private static final long serialVersionUID = 1L;

    public AtomicBooleanDeserializer() { super(AtomicBoolean.class); }

    @Override
    public AtomicBoolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new AtomicBoolean(_parseBooleanPrimitive(jp, ctxt));
    }
}