package lowentry.ue4.libs.jackson.databind.ext;

import lowentry.ue4.libs.jackson.databind.PropertyName;
import lowentry.ue4.libs.jackson.databind.introspect.Annotated;
import lowentry.ue4.libs.jackson.databind.introspect.AnnotatedParameter;
import lowentry.ue4.libs.jackson.databind.util.ClassUtil;

/**
 * To support Java7-incomplete platforms, we will offer support for JDK 7
 * annotations through this class, loaded dynamically; if loading fails,
 * support will be missing. This class is the non-JDK-7-dependent API,
 * and {@link Java7SupportImpl} is JDK7-dependent implementation of
 * functionality.
 */
@SuppressWarnings("all")
public abstract class Java7Support
{
    private final static Java7Support IMPL;

    static {
        Java7Support impl = null;
        try {
            Class<?> cls = Class.forName("lowentry.ue4.libs.jackson.databind.ext.Java7SupportImpl");
            impl = (Java7Support) ClassUtil.createInstance(cls, false);
        } catch (Throwable t) {
            // 09-Sep-2019, tatu: Used to log earlier, but with 2.10.0 let's not log
//            java.util.logging.Logger.getLogger(Java7Support.class.getName())
//                .warning("Unable to load JDK7 annotations (@ConstructorProperties, @Transient): no Java7 annotation support added");
        }
        IMPL = impl;
    }

    public static Java7Support instance() {
        return IMPL;
    }
    
    public abstract Boolean findTransient(Annotated a);

    public abstract Boolean hasCreatorAnnotation(Annotated a);

    public abstract PropertyName findConstructorName(AnnotatedParameter p);
}
