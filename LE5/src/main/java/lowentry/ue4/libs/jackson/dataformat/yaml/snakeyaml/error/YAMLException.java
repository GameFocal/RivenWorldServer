package lowentry.ue4.libs.jackson.dataformat.yaml.snakeyaml.error;

import lowentry.ue4.libs.jackson.core.JsonParser;
import lowentry.ue4.libs.jackson.dataformat.yaml.JacksonYAMLParseException;

/**
 * Replacement for formerly shaded exception type from SnakeYAML; included
 * in 2.8 solely for backwards compatibility: new code that relies on Jackson 2.8
 * and after should NOT use this type but only base type {@link JacksonYAMLParseException}.
 *
 * @deprecated Since 2.8
 */
@Deprecated
@SuppressWarnings("all")
public class YAMLException extends JacksonYAMLParseException
{
    private static final long serialVersionUID = 1L;

    public YAMLException(JsonParser p,
            lowentry.ue4.libs.snakeyaml.error.YAMLException src) {
        super(p, src.getMessage(), src);
    }

    public static YAMLException from(JsonParser p,
            lowentry.ue4.libs.snakeyaml.error.YAMLException src) {
        return new YAMLException(p, src);
    }
}
