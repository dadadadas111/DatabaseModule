package impl.mongodb;

import core.DatabaseAdapter;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.*;

public class MongoDbAdapter<T> extends DatabaseAdapter<T, MongoDbConnection> {

    public MongoDbAdapter(Class<T> modelClass, MongoDbConnection connection) {
        super(modelClass, connection);
    }

    @Override
    protected void insertImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, MongoDbConnection connection) {
        MongoCollection<Document> collection = connection.getTrueConnection().getCollection(tableName);
        Document doc = new Document();
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i);
            Object value = values.get(i);
            // Nếu là primary key, map sang _id
            if (fieldName.equals(primaryKey)) {
                doc.append("_id", value);
            } else {
                doc.append(fieldName, value);
            }
        }
        collection.insertOne(doc);
    }

    @Override
    protected void updateImpl(List<String> fields, List<Object> values, Map<String, Object> columns, T entity, MongoDbConnection connection) {
        MongoCollection<Document> collection = connection.getTrueConnection().getCollection(tableName);
        Object pkValue = columns.get(primaryKey);
        Document filter = new Document("_id", pkValue);
        Document update = new Document();
        for (int i = 0; i < fields.size(); i++) {
            String fieldName = fields.get(i);
            Object value = values.get(i);
            if (fieldName.equals(primaryKey)) {
                update.append("_id", value);
            } else {
                update.append(fieldName, value);
            }
        }
        collection.replaceOne(filter, update);
    }

    @Override
    protected void deleteImpl(Map<String, Object> columns, T entity, MongoDbConnection connection) {
        MongoCollection<Document> collection = connection.getTrueConnection().getCollection(tableName);
        Object pkValue = columns.get(primaryKey);
        Document filter = new Document("_id", pkValue);
        collection.deleteOne(filter);
    }

    @Override
    protected T findByIdImpl(Object id, MongoDbConnection connection) {
        MongoCollection<Document> collection = connection.getTrueConnection().getCollection(tableName);
        Document filter = new Document("_id", id);
        Document doc = collection.find(filter).first();
        if (doc == null) return null;
        try {
            T obj = modelClass.getDeclaredConstructor().newInstance();
            for (String key : doc.keySet()) {
                String javaField = key.equals("_id") ? primaryKey : key;
                try {
                    java.lang.reflect.Field field = modelClass.getDeclaredField(javaField);
                    field.setAccessible(true);
                    field.set(obj, doc.get(key));
                } catch (NoSuchFieldException ignore) {
                    // Bỏ qua nếu field không tồn tại trong model
                }
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected List<T> findAllImpl(MongoDbConnection connection) {
        MongoCollection<Document> collection = connection.getTrueConnection().getCollection(tableName);
        List<T> result = new ArrayList<>();
        for (Document doc : collection.find()) {
            try {
                T obj = modelClass.getDeclaredConstructor().newInstance();
                for (String key : doc.keySet()) {
                    String javaField = key.equals("_id") ? primaryKey : key;
                    try {
                        java.lang.reflect.Field field = modelClass.getDeclaredField(javaField);
                        field.setAccessible(true);
                        field.set(obj, doc.get(key));
                    } catch (NoSuchFieldException ignore) {
                        // Bỏ qua nếu field không tồn tại trong model
                    }
                }
                result.add(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
