package lowentry.ue4.libs.jackson.databind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import lowentry.ue4.libs.jackson.databind.deser.ValueInstantiator;

/**
 * Annotation that can be used to indicate a {@link ValueInstantiator} to use
 * for creating instances of specified type.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@lowentry.ue4.libs.jackson.annotation.JacksonAnnotation
public @interface JsonValueInstantiator
{
    /**
     * @return  {@link ValueInstantiator} to use for annotated type
     */
    public Class<? extends ValueInstantiator> value();
}
