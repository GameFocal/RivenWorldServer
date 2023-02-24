package lowentry.ue4.libs.jackson.databind.util;

import lowentry.ue4.libs.jackson.databind.JavaType;
import lowentry.ue4.libs.jackson.databind.type.TypeFactory;

/**
 * Helper interface for things that convert Objects of
 * one type to another.
 *<p>
 * NOTE: implementors are strongly encouraged to extend {@link StdConverter}
 * instead of directly implementing {@link Converter}, since that can
 * help with default implementation of typically boiler-plate code.
 *
 * @param <IN> Type of values converter takes
 * @param <OUT> Result type from conversion
 *
 * @see lowentry.ue4.libs.jackson.databind.ser.std.StdDelegatingSerializer
 * @see lowentry.ue4.libs.jackson.databind.deser.std.StdDelegatingDeserializer
 *
 * @since 2.1
 */
@SuppressWarnings("all")
public interface Converter<IN,OUT>
{
    /**
     * Main conversion method.
     */
    public OUT convert(IN value);

    /**
     * Method that can be used to find out actual input (source) type; this
     * usually can be determined from type parameters, but may need
     * to be implemented differently from programmatically defined
     * converters (which cannot change static type parameter bindings).
     * 
     * @since 2.2
     */
    public JavaType getInputType(TypeFactory typeFactory);

    /**
     * Method that can be used to find out actual output (target) type; this
     * usually can be determined from type parameters, but may need
     * to be implemented differently from programmatically defined
     * converters (which cannot change static type parameter bindings).
     * 
     * @since 2.2
     */
    public JavaType getOutputType(TypeFactory typeFactory);
    
    /*
    /**********************************************************
    /* Helper class(es)
    /**********************************************************
     */

    /**
     * This marker class is only to be used with annotations, to
     * indicate that <b>no converter is to be used</b>.
     *<p>
     * Specifically, this class is to be used as the marker for
     * annotation {@link lowentry.ue4.libs.jackson.databind.annotation.JsonSerialize},
     * property <code>converter</code> (and related)
     * 
     * @since 2.2
     */
@SuppressWarnings("all")
    public abstract static class None
        implements Converter<Object,Object> { }
}
