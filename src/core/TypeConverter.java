package core;

public interface TypeConverter<JavaType, DbType> {
    DbType toDb(JavaType javaValue);
    JavaType fromDb(DbType dbValue);
}

