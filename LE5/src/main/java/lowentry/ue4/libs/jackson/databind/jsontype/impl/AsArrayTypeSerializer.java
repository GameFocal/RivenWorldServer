package lowentry.ue4.libs.jackson.databind.jsontype.impl;

import lowentry.ue4.libs.jackson.annotation.JsonTypeInfo.As;

import lowentry.ue4.libs.jackson.databind.BeanProperty;
import lowentry.ue4.libs.jackson.databind.jsontype.TypeIdResolver;

/**
 * Type serializer that will embed type information in an array,
 * as the first element, and actual value as the second element.
 */
@SuppressWarnings("all")
public class AsArrayTypeSerializer extends TypeSerializerBase
{
    public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property) {
        super(idRes, property);
    }

    @Override
    public AsArrayTypeSerializer forProperty(BeanProperty prop) {
        return (_property == prop) ? this : new AsArrayTypeSerializer(_idResolver, prop);
    }
    
    @Override
    public As getTypeInclusion() { return As.WRAPPER_ARRAY; }
}
