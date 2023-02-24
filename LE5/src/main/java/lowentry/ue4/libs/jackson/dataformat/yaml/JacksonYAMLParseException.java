package lowentry.ue4.libs.jackson.dataformat.yaml;

import lowentry.ue4.libs.jackson.core.JsonParseException;
import lowentry.ue4.libs.jackson.core.JsonParser;

/**
 * @since 2.8
 */
@SuppressWarnings("all")
public class JacksonYAMLParseException extends JsonParseException
{
    private static final long serialVersionUID = 1L;

    public JacksonYAMLParseException(JsonParser p, String msg, Exception e) {
        super(p, msg, e);
    }
}
