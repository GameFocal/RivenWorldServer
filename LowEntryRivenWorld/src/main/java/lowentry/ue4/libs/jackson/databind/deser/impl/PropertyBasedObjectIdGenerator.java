package lowentry.ue4.libs.jackson.databind.deser.impl;

import lowentry.ue4.libs.jackson.annotation.ObjectIdGenerator;
import lowentry.ue4.libs.jackson.annotation.ObjectIdGenerators;

// Simple placeholder
@SuppressWarnings("all")
public class PropertyBasedObjectIdGenerator
	extends ObjectIdGenerators.PropertyGenerator
{
    private static final long serialVersionUID = 1L;

    public PropertyBasedObjectIdGenerator(Class<?> scope) {
        super(scope);
    }
    
    @Override
    public Object generateId(Object forPojo) {
    	throw new UnsupportedOperationException();
    }

    @Override
    public ObjectIdGenerator<Object> forScope(Class<?> scope) {
        return (scope == _scope) ? this : new PropertyBasedObjectIdGenerator(scope);
    }

    @Override
    public ObjectIdGenerator<Object> newForSerialization(Object context) {
        return this;
    }

    @Override
    public IdKey key(Object key) {
        if (key == null) {
            return null;
        }
        // should we use general type for all; or type of property itself?
        return new IdKey(getClass(), _scope, key);
    }

}
