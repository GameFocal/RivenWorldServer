package lowentry.ue4.libs.jackson.databind.introspect;

@SuppressWarnings("all")
public interface WithMember<T>
{
    public T withMember(AnnotatedMember member);
}
