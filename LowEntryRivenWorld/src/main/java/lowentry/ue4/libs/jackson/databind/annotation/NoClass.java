package lowentry.ue4.libs.jackson.databind.annotation;

/**
 * Marker class used with annotations to indicate "no class". This is
 * a silly but necessary work-around -- annotations cannot take nulls
 * as either default or explicit values. Hence for class values we must
 * explicitly use a bogus placeholder to denote equivalent of
 * "no class" (for which 'null' is usually the natural choice).
 *<p>
 * Note that since 2.4, most (but not all!
 * {@link lowentry.ue4.libs.jackson.annotation.JsonTypeInfo#defaultImpl} is
 * a notable exception}) usage should start using
 * {@link Void} instead as the "not defined" marker.
 */
@SuppressWarnings("all")
public final class NoClass
{
    private NoClass() { }
}
