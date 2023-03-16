package lowentry.ue4.libs.jackson.databind.introspect;

import lowentry.ue4.libs.jackson.core.Version;

import lowentry.ue4.libs.jackson.databind.*;

/**
 * Dummy, "no-operation" implementation of {@link AnnotationIntrospector}.
 * Can be used as is to suppress handling of annotations; or as a basis
 * for simple configuration overrides (whether based on annotations or not).
 */
@SuppressWarnings("all")
public abstract class NopAnnotationIntrospector
    extends AnnotationIntrospector
    implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * Static immutable and shareable instance that can be used as
     * "null" introspector: one that never finds any annotation
     * information.
     */
    public final static NopAnnotationIntrospector instance = new NopAnnotationIntrospector() {
        private static final long serialVersionUID = 1L;

        @Override
        public Version version() {
            return lowentry.ue4.libs.jackson.databind.cfg.PackageVersion.VERSION;
        }
    };

    @Override
    public Version version() {
        return Version.unknownVersion();
    }
}
