package lowentry.ue4.libs.jackson.databind.deser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import lowentry.ue4.libs.jackson.core.JsonLocation;
import lowentry.ue4.libs.jackson.databind.DeserializationConfig;
import lowentry.ue4.libs.jackson.databind.DeserializationContext;
import lowentry.ue4.libs.jackson.databind.deser.ValueInstantiator;
import lowentry.ue4.libs.jackson.databind.deser.std.JsonLocationInstantiator;

/**
 * Container for a set of {@link ValueInstantiator}s used for certain critical
 * JDK value types, either as performance optimization for initialization time observed
 * by profiling, or due to difficulty in otherwise finding constructors.
 *
 * @since 2.10
 */
@SuppressWarnings("all")
public abstract class JDKValueInstantiators
{
    public static ValueInstantiator findStdValueInstantiator(DeserializationConfig config,
            Class<?> raw)
    {
        if (raw == JsonLocation.class) {
            return new JsonLocationInstantiator();
        }
        // [databind#1868]: empty List/Set/Map
        // [databind#2416]: optimize commonly needed default creators
        if (Collection.class.isAssignableFrom(raw)) {
            if (raw == ArrayList.class) {
                return ArrayListInstantiator.INSTANCE;
            }
            if (Collections.EMPTY_SET.getClass() == raw) {
                return new ConstantValueInstantiator(Collections.EMPTY_SET);
            }
            if (Collections.EMPTY_LIST.getClass() == raw) {
                return new ConstantValueInstantiator(Collections.EMPTY_LIST);
            }
        } else if (Map.class.isAssignableFrom(raw)) {
            if (raw == LinkedHashMap.class) {
                return LinkedHashMapInstantiator.INSTANCE;
            }
            if (raw == HashMap.class) {
                return HashMapInstantiator.INSTANCE;
            }
            if (Collections.EMPTY_MAP.getClass() == raw) {
                return new ConstantValueInstantiator(Collections.EMPTY_MAP);
            }
        }
        return null;
    }

@SuppressWarnings("all")
    private static class ArrayListInstantiator
        extends ValueInstantiator.Base
        implements java.io.Serializable
    {
        private static final long serialVersionUID = 2L;

        public final static ArrayListInstantiator INSTANCE = new ArrayListInstantiator();
        public ArrayListInstantiator() {
            super(ArrayList.class);
        }

        @Override
        public boolean canInstantiate() { return true; }

        @Override
        public boolean canCreateUsingDefault() {  return true; }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new ArrayList<>();
        }
    }

@SuppressWarnings("all")
    private static class HashMapInstantiator
        extends ValueInstantiator.Base
        implements java.io.Serializable
    {
        private static final long serialVersionUID = 2L;

        public final static HashMapInstantiator INSTANCE = new HashMapInstantiator();

        public HashMapInstantiator() {
            super(HashMap.class);
        }

        @Override
        public boolean canInstantiate() { return true; }

        @Override
        public boolean canCreateUsingDefault() {  return true; }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new HashMap<>();
        }
    }

@SuppressWarnings("all")
    private static class LinkedHashMapInstantiator
        extends ValueInstantiator.Base
        implements java.io.Serializable
    {
        private static final long serialVersionUID = 2L;

        public final static LinkedHashMapInstantiator INSTANCE = new LinkedHashMapInstantiator();

        public LinkedHashMapInstantiator() {
            super(LinkedHashMap.class);
        }

        @Override
        public boolean canInstantiate() { return true; }

        @Override
        public boolean canCreateUsingDefault() {  return true; }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return new LinkedHashMap<>();
        }
    }

@SuppressWarnings("all")
    private static class ConstantValueInstantiator
        extends ValueInstantiator.Base
        implements java.io.Serializable
    {
        private static final long serialVersionUID = 2L;

        protected final Object _value;

        public ConstantValueInstantiator(Object value) {
            super(value.getClass());
            _value = value;
        }

        @Override // yes, since default ctor works
        public boolean canInstantiate() { return true; }

        @Override
        public boolean canCreateUsingDefault() {  return true; }

        @Override
        public Object createUsingDefault(DeserializationContext ctxt) throws IOException {
            return _value;
        }
    }

}
