package lowentry.ue4.libs.jackson.databind.jsonFormatVisitors;

import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;

/**
 * Interface {@link lowentry.ue4.libs.jackson.databind.JsonSerializer} implements
 * to allow for visiting type hierarchy.
 */
@SuppressWarnings("all")
public interface JsonFormatVisitable
{
    /**
     * Get the representation of the schema to which this serializer will conform.
     * 
     * @param typeHint Type of element (entity like property) being visited
     */
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
        throws JsonMappingException;
}
