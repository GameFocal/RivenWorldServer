package lowentry.ue4.libs.jackson.databind.deser.std;

import java.io.IOException;

import lowentry.ue4.libs.jackson.core.JsonParser;
import lowentry.ue4.libs.jackson.core.JsonToken;

import lowentry.ue4.libs.jackson.databind.DeserializationContext;
import lowentry.ue4.libs.jackson.databind.DeserializationFeature;

@SuppressWarnings("all")
public class StackTraceElementDeserializer
    extends StdScalarDeserializer<StackTraceElement>
{
    private static final long serialVersionUID = 1L;

    public StackTraceElementDeserializer() { super(StackTraceElement.class); }

    @Override
    public StackTraceElement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        JsonToken t = p.currentToken();
        // Must get an Object
        if (t == JsonToken.START_OBJECT) {
            String className = "", methodName = "", fileName = "";
            // Java 9 adds couple more things
            String moduleName = null, moduleVersion = null;
            String classLoaderName = null;
            int lineNumber = -1;

            while ((t = p.nextValue()) != JsonToken.END_OBJECT) {
                String propName = p.getCurrentName();
                // TODO: with Java 8, convert to switch
                if ("className".equals(propName)) {
                    className = p.getText();
                } else if ("classLoaderName".equals(propName)) {
                    classLoaderName = p.getText();
                } else if ("fileName".equals(propName)) {
                    fileName = p.getText();
                } else if ("lineNumber".equals(propName)) {
                    if (t.isNumeric()) {
                        lineNumber = p.getIntValue();
                    } else {
                        lineNumber = _parseIntPrimitive(p, ctxt);
                    }
                } else if ("methodName".equals(propName)) {
                    methodName = p.getText();
                } else if ("nativeMethod".equals(propName)) {
                    // no setter, not passed via constructor: ignore
                } else if ("moduleName".equals(propName)) {
                    moduleName = p.getText();
                } else if ("moduleVersion".equals(propName)) {
                    moduleVersion = p.getText();
                } else if ("declaringClass".equals(propName)
                        || "format".equals(propName)) {
                    // 01-Nov-2017: [databind#1794] Not sure if we should but... let's prune it for now
                    ;
                } else {
                    handleUnknownProperty(p, ctxt, _valueClass, propName);
                }
                p.skipChildren(); // just in case we might get structured values
            }
            return constructValue(ctxt, className, methodName, fileName, lineNumber,
                    moduleName, moduleVersion, classLoaderName);
        } else if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
            p.nextToken();
            final StackTraceElement value = deserialize(p, ctxt);
            if (p.nextToken() != JsonToken.END_ARRAY) {
                handleMissingEndArrayForSingle(p, ctxt);
            }
            return value;
        }
        return (StackTraceElement) ctxt.handleUnexpectedToken(_valueClass, p);
    }

    @Deprecated // since 2.9
    protected StackTraceElement constructValue(DeserializationContext ctxt,
            String className, String methodName, String fileName, int lineNumber,
            String moduleName, String moduleVersion) {
        return constructValue(ctxt, className, methodName, fileName, lineNumber,
                moduleName, moduleVersion, null);
    }

    /**
     * Overridable factory method used for constructing {@link StackTraceElement}s.
     *
     * @since 2.8
     */
    protected StackTraceElement constructValue(DeserializationContext ctxt,
            String className, String methodName, String fileName, int lineNumber,
            String moduleName, String moduleVersion, String classLoaderName)
    {
        // 21-May-2016, tatu: With Java 9, need to use different constructor, probably
        //   via different module, and throw exception here if extra args passed
        return new StackTraceElement(className, methodName, fileName, lineNumber);
    }
}
