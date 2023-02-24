package lowentry.ue4.libs.jackson.databind.ser.impl;

import lowentry.ue4.libs.jackson.annotation.JsonInclude;
import lowentry.ue4.libs.jackson.core.JsonGenerator;
import lowentry.ue4.libs.jackson.databind.*;
import lowentry.ue4.libs.jackson.databind.cfg.MapperConfig;
import lowentry.ue4.libs.jackson.databind.introspect.AnnotatedClass;
import lowentry.ue4.libs.jackson.databind.introspect.BeanPropertyDefinition;
import lowentry.ue4.libs.jackson.databind.ser.VirtualBeanPropertyWriter;
import lowentry.ue4.libs.jackson.databind.util.Annotations;

/**
 * {@link VirtualBeanPropertyWriter} implementation used for
 * {@link lowentry.ue4.libs.jackson.databind.annotation.JsonAppend},
 * to serialize properties backed-by dynamically assignable attribute
 * values.
 * 
 * @since 2.5
 */
@SuppressWarnings("all")
public class AttributePropertyWriter
    extends VirtualBeanPropertyWriter
{
    private static final long serialVersionUID = 1;

    protected final String _attrName;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef,
            Annotations contextAnnotations, JavaType declaredType) {
        this(attrName, propDef, contextAnnotations, declaredType, propDef.findInclusion());
    }

    protected AttributePropertyWriter(String attrName, BeanPropertyDefinition propDef,
            Annotations contextAnnotations, JavaType declaredType,
            JsonInclude.Value inclusion)
    {
        super(propDef, contextAnnotations, declaredType,
                /* value serializer */ null, /* type serializer */ null, /* ser type */ null,
                inclusion,
                // 10-Oct-2016, tatu: Could enable per-view settings too in future
                null);
        _attrName = attrName;
    }

    public static AttributePropertyWriter construct(String attrName,
            BeanPropertyDefinition propDef,
            Annotations contextAnnotations,
            JavaType declaredType)
    {
        return new AttributePropertyWriter(attrName, propDef,
                contextAnnotations, declaredType);
    }
    
    protected AttributePropertyWriter(AttributePropertyWriter base) {
        super(base);
        _attrName = base._attrName;
    }

    /**
     * Since this method should typically not be called on this sub-type,
     * default implementation simply throws an {@link IllegalStateException}.
     */
    @Override
    public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config,
            AnnotatedClass declaringClass, BeanPropertyDefinition propDef, JavaType type) {
        throw new IllegalStateException("Should not be called on this type");
    }
    
    /*
    /**********************************************************
    /* Overrides for actual serialization, value access
    /**********************************************************
     */
    
    @Override
    protected Object value(Object bean, JsonGenerator jgen, SerializerProvider prov) throws Exception {
        return prov.getAttribute(_attrName);
    }
}
