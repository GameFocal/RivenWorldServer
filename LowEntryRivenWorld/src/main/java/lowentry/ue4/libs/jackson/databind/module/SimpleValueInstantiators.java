package lowentry.ue4.libs.jackson.databind.module;

import java.util.HashMap;

import lowentry.ue4.libs.jackson.databind.BeanDescription;
import lowentry.ue4.libs.jackson.databind.DeserializationConfig;
import lowentry.ue4.libs.jackson.databind.deser.ValueInstantiator;
import lowentry.ue4.libs.jackson.databind.deser.ValueInstantiators;
import lowentry.ue4.libs.jackson.databind.type.ClassKey;

@SuppressWarnings("all")
public class SimpleValueInstantiators
    extends ValueInstantiators.Base
    implements java.io.Serializable
{
    private static final long serialVersionUID = -8929386427526115130L;

    /**
     * Mappings from raw (type-erased, i.e. non-generic) types
     * to matching {@link ValueInstantiator} instances.
     */
    protected HashMap<ClassKey,ValueInstantiator> _classMappings;

    /*
    /**********************************************************
    /* Life-cycle, construction and configuring
    /**********************************************************
     */

    public SimpleValueInstantiators()
    {
        _classMappings = new HashMap<ClassKey,ValueInstantiator>();        
    }
    
    public SimpleValueInstantiators addValueInstantiator(Class<?> forType,
            ValueInstantiator inst)
    {
        _classMappings.put(new ClassKey(forType), inst);
        return this;
    }
    
    @Override
    public ValueInstantiator findValueInstantiator(DeserializationConfig config,
            BeanDescription beanDesc, ValueInstantiator defaultInstantiator)
    {
        ValueInstantiator inst = _classMappings.get(new ClassKey(beanDesc.getBeanClass()));
        return (inst == null) ? defaultInstantiator : inst;
    }
}
