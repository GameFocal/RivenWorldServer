package lowentry.ue4.libs.jackson.databind.util;

import java.util.*;

import lowentry.ue4.libs.jackson.core.SerializableString;
import lowentry.ue4.libs.jackson.databind.*;
import lowentry.ue4.libs.jackson.databind.cfg.MapperConfig;

/**
 * Helper class used for storing String serializations of
 * enumerations.
 */
@SuppressWarnings("all")
public final class EnumValues
    implements java.io.Serializable
{
    private static final long serialVersionUID = 1;

    private final Class<Enum<?>> _enumClass;

    private final Enum<?>[] _values;
    private final SerializableString[] _textual;

    private transient EnumMap<?,SerializableString> _asMap;

    private EnumValues(Class<Enum<?>> enumClass, SerializableString[] textual)
    {
        _enumClass = enumClass;
        _values = enumClass.getEnumConstants();
        _textual = textual;
    }

    /**
     * NOTE: do NOT call this if configuration may change, and choice between toString()
     *   and name() might change dynamically.
     */
    public static EnumValues construct(SerializationConfig config, Class<Enum<?>> enumClass) {
        if (config.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
            return constructFromToString(config, enumClass);
        }
        return constructFromName(config, enumClass);
    }

    public static EnumValues constructFromName(MapperConfig<?> config, Class<Enum<?>> enumClass)
    {
        // Enum types with per-instance sub-classes need special handling
        Class<? extends Enum<?>> enumCls = ClassUtil.findEnumType(enumClass);
        Enum<?>[] enumValues = enumCls.getEnumConstants();
        if (enumValues == null) {
            throw new IllegalArgumentException("Cannot determine enum constants for Class "+enumClass.getName());
        }
        String[] names = config.getAnnotationIntrospector().findEnumValues(enumCls, enumValues, new String[enumValues.length]);
        SerializableString[] textual = new SerializableString[enumValues.length];
        for (int i = 0, len = enumValues.length; i < len; ++i) {
            Enum<?> en = enumValues[i];
            String name = names[i];
            if (name == null) {
                name = en.name();
            }
            textual[en.ordinal()] = config.compileString(name);
        }
        return new EnumValues(enumClass, textual);
    }

    public static EnumValues constructFromToString(MapperConfig<?> config, Class<Enum<?>> enumClass)
    {
        Class<? extends Enum<?>> cls = ClassUtil.findEnumType(enumClass);
        Enum<?>[] values = cls.getEnumConstants();
        if (values != null) {
            SerializableString[] textual = new SerializableString[values.length];
            for (Enum<?> en : values) {
                textual[en.ordinal()] = config.compileString(en.toString());
            }
            return new EnumValues(enumClass, textual);
        }
        throw new IllegalArgumentException("Cannot determine enum constants for Class "+enumClass.getName());
    }

    public SerializableString serializedValueFor(Enum<?> key) {
        return _textual[key.ordinal()];
    }

    public Collection<SerializableString> values() {
        return Arrays.asList(_textual);
    }

    /**
     * Convenience accessor for getting raw Enum instances.
     * 
     * @since 2.6
     */
    public List<Enum<?>> enums() {
        return Arrays.asList(_values);
    }

    /**
     * Method used for serialization and introspection by core Jackson code.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public EnumMap<?,SerializableString> internalMap() {
        EnumMap<?,SerializableString> result = _asMap;
        if (result == null) {
            // Alas, need to create it in a round-about way, due to typing constraints...
            Map<Enum<?>,SerializableString> map = new LinkedHashMap<Enum<?>,SerializableString>();
            for (Enum<?> en : _values) {
                map.put(en, _textual[en.ordinal()]);
            }
            result = new EnumMap(map);
        }
        return result;
    }

    /**
     * @since 2.2
     */
    public Class<Enum<?>> getEnumClass() { return _enumClass; }
}
