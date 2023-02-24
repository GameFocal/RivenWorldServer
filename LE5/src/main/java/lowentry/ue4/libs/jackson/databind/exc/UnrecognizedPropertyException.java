package lowentry.ue4.libs.jackson.databind.exc;

import java.util.*;

import lowentry.ue4.libs.jackson.core.JsonLocation;
import lowentry.ue4.libs.jackson.core.JsonParser;
import lowentry.ue4.libs.jackson.databind.JsonMappingException;

/**
 * Specialized {@link JsonMappingException} sub-class specifically used
 * to indicate problems due to encountering a JSON property that could
 * not be mapped to an Object property (via getter, constructor argument
 * or field).
 */
@SuppressWarnings("all")
public class UnrecognizedPropertyException
    extends PropertyBindingException
{
    private static final long serialVersionUID = 1L;

    public UnrecognizedPropertyException(JsonParser p, String msg, JsonLocation loc,
            Class<?> referringClass, String propName,
            Collection<Object> propertyIds)
    {
        super(p, msg, loc, referringClass, propName, propertyIds);
    }

    /**
     * @deprecated Since 2.7
     */
    @Deprecated // since 2.7
    public UnrecognizedPropertyException(String msg, JsonLocation loc,
            Class<?> referringClass, String propName,
            Collection<Object> propertyIds)
    {
        super(msg, loc, referringClass, propName, propertyIds);
    }

    /**
     * Factory method used for constructing instances of this exception type.
     * 
     * @param p Underlying parser used for reading input being used for data-binding
     * @param fromObjectOrClass Reference to either instance of problematic type (
     *    if available), or if not, type itself
     * @param propertyName Name of unrecognized property
     * @param propertyIds (optional, null if not available) Set of properties that
     *    type would recognize, if completely known: null if set cannot be determined.
     */
    public static UnrecognizedPropertyException from(JsonParser p,
            Object fromObjectOrClass, String propertyName,
            Collection<Object> propertyIds)
    {
        Class<?> ref;
        if (fromObjectOrClass instanceof Class<?>) {
            ref = (Class<?>) fromObjectOrClass;
        } else {
            ref = fromObjectOrClass.getClass();
        }
        String msg = String.format("Unrecognized field \"%s\" (class %s), not marked as ignorable",
                propertyName, ref.getName());
        UnrecognizedPropertyException e = new UnrecognizedPropertyException(p, msg,
                p.getCurrentLocation(), ref, propertyName, propertyIds);
        // but let's also ensure path includes this last (missing) segment
        e.prependPath(fromObjectOrClass, propertyName);
        return e;
    }
}
