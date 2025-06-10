package core;

import java.util.*;

public abstract class DatabaseAdapter<T> {
    protected final Class<T> modelClass;
    protected final String tableName;
    protected final String primaryKey;

    public DatabaseAdapter(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.tableName = ModelMapper.getTableName(modelClass);
        this.primaryKey = ModelMapper.getPrimaryKeyField(modelClass);
    }

    public final void insert(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        List<String> fields = new ArrayList<>(columns.keySet());
        List<Object> values = new ArrayList<>(columns.values());
        insertImpl(fields, values, columns, entity);
    }
    public final void update(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        List<String> fields = new ArrayList<>(columns.keySet());
        List<Object> values = new ArrayList<>(columns.values());
        updateImpl(fields, values, columns, entity);
    }
    public final void delete(T entity) {
        Map<String, Object> columns = ModelMapper.toColumnMap(entity);
        deleteImpl(columns, entity);
    }
    public final T findById(Object id) {
        return findByIdImpl(id);
    }
    public final List<T> findAll() {
        return findAllImpl();
    }

    protected abstract void insertImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity);
    protected abstract void updateImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity);
    protected abstract void deleteImpl(Map<String, Object> columns, T entity);
    protected abstract T findByIdImpl(Object id);
    protected abstract List<T> findAllImpl();
}

