package impl.mongodb;

import core.DatabaseProvider;
import core.DatabaseAdapter;

public class MongoDbProvider extends DatabaseProvider<MongoDbConnection, MongoDbAdapter<?>> {
    @Override
    public MongoDbConnection connect(String uri) {
        connection = new MongoDbConnection();
        connection.init(uri);
        return connection;
    }

    @Override
    public <T> MongoDbAdapter<T> getAdapter(Class<T> modelClass) {
        if (connection == null || !connection.isConnected()) {
            throw new IllegalStateException("Not connected to MongoDB");
        }
        return new MongoDbAdapter<>(modelClass, connection);
    }
}
