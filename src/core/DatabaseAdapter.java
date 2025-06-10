package core;

import java.util.*;

public abstract class DatabaseAdapter<T, M extends DatabaseConnection<?>> {
    protected final Class<T> modelClass;
    protected final String tableName;
    protected final String primaryKey;
    protected final M connection;

    public DatabaseAdapter(Class<T> modelClass, M connection) {
        this.modelClass = modelClass;
        this.tableName = ModelMapper.getTableName(modelClass);
        this.primaryKey = ModelMapper.getPrimaryKeyField(modelClass);
        this.connection = connection;
    }

    public final void insert(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        List<String> fields = new ArrayList<>(columns.keySet());
        List<Object> values = new ArrayList<>(columns.values());
        insertImpl(fields, values, columns, entity, connection);
    }
    public final void update(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        List<String> fields = new ArrayList<>(columns.keySet());
        List<Object> values = new ArrayList<>(columns.values());
        updateImpl(fields, values, columns, entity, connection);
    }
    public final void delete(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        deleteImpl(columns, entity, connection);
    }
    public final T findById(Object id) {
        return findByIdImpl(id, connection);
    }
    public final List<T> findAll() {
        return findAllImpl(connection);
    }

    protected abstract void insertImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, M connection);
    protected abstract void updateImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, M connection);
    protected abstract void deleteImpl(Map<String, Object> columns, T entity, M connection);
    protected abstract T findByIdImpl(Object id, M connection);
    protected abstract List<T> findAllImpl(M connection);
}

