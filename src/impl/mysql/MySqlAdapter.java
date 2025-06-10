package impl.mysql;

import core.DatabaseAdapter;
import core.TypeConverter;
import core.TypeConverterRegistry;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class MySqlAdapter<T> extends DatabaseAdapter<T, MySqlConnection> {
    public MySqlAdapter(Class<T> modelClass, MySqlConnection connection) {
        super(modelClass, connection);
    }

    @Override
    protected void insertImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, MySqlConnection connection) {
        Connection sqlConn = connection.getTrueConnection();
        StringJoiner fieldJoiner = new StringJoiner(", ");
        StringJoiner valueJoiner = new StringJoiner(", ");
        for (String col : fields) {
            fieldJoiner.add(col);
            valueJoiner.add("?");
        }
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, fieldJoiner, valueJoiner);
        try (PreparedStatement stmt = sqlConn.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, MySqlConnection connection) {
        Connection sqlConn = connection.getTrueConnection();
        StringBuilder setClause = new StringBuilder();
        Object pkValue = columns.get(primaryKey);
        int count = 0;
        for (String col : fields) {
            if (col.equals(primaryKey)) continue;
            if (count++ > 0) setClause.append(", ");
            setClause.append(col).append(" = ?");
        }
        String sql = String.format("UPDATE %s SET %s WHERE %s = ?", tableName, setClause, primaryKey);
        try (PreparedStatement stmt = sqlConn.prepareStatement(sql)) {
            int i = 1;
            for (String col : fields) {
                if (col.equals(primaryKey)) continue;
                stmt.setObject(i++, columns.get(col));
            }
            stmt.setObject(i, pkValue);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void deleteImpl(Map<String, Object> columns, T entity, MySqlConnection connection) {
        Connection sqlConn = connection.getTrueConnection();
        Object pkValue = columns.get(primaryKey);
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, primaryKey);
        try (PreparedStatement stmt = sqlConn.prepareStatement(sql)) {
            stmt.setObject(1, pkValue);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected T findByIdImpl(Object id, MySqlConnection connection) {
        Connection sqlConn = connection.getTrueConnection();
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, primaryKey);
        try (PreparedStatement stmt = sqlConn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<T> findAllImpl(MySqlConnection connection) {
        Connection sqlConn = connection.getTrueConnection();
        String sql = String.format("SELECT * FROM %s", tableName);
        List<T> result = new ArrayList<>();
        try (PreparedStatement stmt = sqlConn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSet(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private T mapResultSet(ResultSet rs) throws Exception {
        T obj = modelClass.getDeclaredConstructor().newInstance();
        for (Field field : modelClass.getDeclaredFields()) {
            field.setAccessible(true);
            String colName = field.getName();
            if (field.isAnnotationPresent(annotations.Column.class)) {
                colName = field.getAnnotation(annotations.Column.class).name();
            } else if (field.isAnnotationPresent(annotations.PrimaryKey.class)) {
                annotations.Column col = field.getAnnotation(annotations.Column.class);
                if (col != null) colName = col.name();
            }
            Object value = rs.getObject(colName);
            // Sử dụng TypeConverter nếu có, ngược lại gán trực tiếp
            TypeConverter converter = TypeConverterRegistry.get(field.getType());
            if (converter != null) {
                value = converter.fromDb(value);
            }
            field.set(obj, value);
        }
        return obj;
    }
}
