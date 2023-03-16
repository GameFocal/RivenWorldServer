package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

import lowentry.ue4.libs.jackson.core.JsonParser;

@SuppressWarnings("all")
public interface JsonNumberFormatVisitor extends JsonValueFormatVisitor
{
    /**
     * Method called to provide more exact type of number being serialized
     * (regardless of logical type, which may be {@link java.util.Date} or
     * {@link Enum}, in addition to actual numeric types like
     * {@link Integer}).
     */
    public void numberType(JsonParser.NumberType type);

    /**
     * Default "empty" implementation, useful as the base to start on;
     * especially as it is guaranteed to implement all the method
     * of the interface, even if new methods are getting added.
     */
@SuppressWarnings("all")
    public static class Base extends JsonValueFormatVisitor.Base
        implements JsonNumberFormatVisitor {
        @Override
        public void numberType(JsonParser.NumberType type) { }
    }
}
