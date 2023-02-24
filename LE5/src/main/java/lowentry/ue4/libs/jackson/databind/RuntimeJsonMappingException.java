package lowentry.ue4.libs.jackson.databind;

/**
 * Wrapper used when interface does not allow throwing a checked
 * {@link JsonMappingException}
 */
@SuppressWarnings("all")
public class RuntimeJsonMappingException extends RuntimeException
{
    public RuntimeJsonMappingException(JsonMappingException cause) {
        super(cause);
    }

    public RuntimeJsonMappingException(String message) {
        super(message);
    }

    public RuntimeJsonMappingException(String message, JsonMappingException cause) {
        super(message, cause);
    }
}
