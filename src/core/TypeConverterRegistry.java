package core;

import java.util.HashMap;
import java.util.Map;

public class TypeConverterRegistry {
    private static final Map<Class<?>, TypeConverter<?, ?>> converters = new HashMap<>();

    public static <J, D> void register(Class<J> javaType, TypeConverter<J, D> converter) {
        converters.put(javaType, converter);
    }

    @SuppressWarnings("unchecked")
    public static <J, D> TypeConverter<J, D> get(Class<J> javaType) {
        if (isPrimitiveOrWrapper(javaType) || javaType == String.class) {
            return null;
        }
        return (TypeConverter<J, D>) converters.get(javaType);
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Short.class || clazz == Byte.class ||
                clazz == Boolean.class || clazz == Character.class;
    }
}

