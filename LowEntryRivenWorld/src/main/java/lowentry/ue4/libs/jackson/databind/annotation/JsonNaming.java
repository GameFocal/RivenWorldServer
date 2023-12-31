package lowentry.ue4.libs.jackson.databind.annotation;

import java.lang.annotation.*;

import lowentry.ue4.libs.jackson.databind.PropertyNamingStrategy;

/**
 * Annotation that can be used to indicate a {@link PropertyNamingStrategy}
 * to use for annotated class. Overrides the global (default) strategy.
 * Note that if the {@link #value} property is omitted, its default value
 * means "use default naming" (that is, no alternate naming method is used).
 * This can be used as an override with mix-ins.
 * 
 * @since 2.1
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@lowentry.ue4.libs.jackson.annotation.JacksonAnnotation
public @interface JsonNaming
{
    /**
     * @return Type of {@link PropertyNamingStrategy} to use, if any; default value of
     *    <code>PropertyNamingStrategy.class</code> means "no strategy specified"
     *    (and may also be used for overriding to remove otherwise applicable
     *    naming strategy)
     */
    public Class<? extends PropertyNamingStrategy> value() default PropertyNamingStrategy.class;
}
