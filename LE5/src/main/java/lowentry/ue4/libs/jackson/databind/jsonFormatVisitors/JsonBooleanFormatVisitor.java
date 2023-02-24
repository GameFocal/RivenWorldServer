package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

@SuppressWarnings("all")
public interface JsonBooleanFormatVisitor extends JsonValueFormatVisitor
{
    /**
     * Default "empty" implementation, useful as the base to start on;
     * especially as it is guaranteed to implement all the method
     * of the interface, even if new methods are getting added.
     */
@SuppressWarnings("all")
    public static class Base extends JsonValueFormatVisitor.Base
        implements JsonBooleanFormatVisitor { }
}
