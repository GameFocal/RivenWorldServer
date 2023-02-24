package lowentry.ue4.libs.jackson.dataformat.yaml.snakeyaml.error;

/**
 * Placeholder for shaded <code>lowentry.ue4.libs.snakeyaml.error.Mark</code>
 *
 * @since 2.8 (as non-shaded); earlier shaded in
 *
 * @deprecated Should use basic {@link lowentry.ue4.libs.jackson.core.JsonLocation} instead
 */
@Deprecated // since 2.8
@SuppressWarnings("all")
public class Mark
{
    protected final lowentry.ue4.libs.snakeyaml.error.Mark _source;

    protected Mark(lowentry.ue4.libs.snakeyaml.error.Mark src) {
        _source = src;
    }
    
    public static Mark from(lowentry.ue4.libs.snakeyaml.error.Mark src) {
        return (src == null)  ? null : new Mark(src);
    }

    public String getName() {
        return _source.getName();
    }
    
    public String get_snippet() {
        return _source.get_snippet();
    }

    public String get_snippet(int indent, int max_length) {
        return _source.get_snippet(indent, max_length);
    }

    public int getColumn() {
        return _source.getColumn();
    }

    public int getLine() {
        return _source.getLine();
    }

    public int getIndex() {
        return _source.getIndex();
    }
}
