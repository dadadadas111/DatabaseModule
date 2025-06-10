package core;

import annotations.Column;
import annotations.PrimaryKey;
import annotations.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ModelMapper {
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        return table != null ? table.name() : clazz.getSimpleName();
    }

    public static Map<String, Object> toColumnMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) || field.isAnnotationPresent(PrimaryKey.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    Column col = field.getAnnotation(Column.class);
                    String name = col != null ? col.name() : field.getName();
                    map.put(name, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return map;
    }

    public static String getPrimaryKeyField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                Column col = field.getAnnotation(Column.class);
                return col != null ? col.name() : field.getName();
            }
        }
        throw new RuntimeException("No @PrimaryKey found");
    }
}

