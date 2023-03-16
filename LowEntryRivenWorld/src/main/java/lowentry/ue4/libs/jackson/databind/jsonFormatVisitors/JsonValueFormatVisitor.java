package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

import java.util.Set;

@SuppressWarnings("all")
public interface JsonValueFormatVisitor {
    /**
     * Method called to indicate configured format for value type being visited.
     */
    void format(JsonValueFormat format);

    /**
     * Method called to indicate enumerated (String) values type being visited
     * can take as values.
     */
    void enumTypes(Set<String> enums);

    /**
     * Default "empty" implementation, useful as the base to start on;
     * especially as it is guaranteed to implement all the method
     * of the interface, even if new methods are getting added.
     */
@SuppressWarnings("all")
    public static class Base implements JsonValueFormatVisitor {
        @Override
        public void format(JsonValueFormat format) { }
        @Override
        public void enumTypes(Set<String> enums) { }
    }
}
