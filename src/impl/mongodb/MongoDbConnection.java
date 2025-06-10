package impl.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import core.DatabaseConnection;

public class MongoDbConnection extends DatabaseConnection<MongoDatabase> {
    private MongoClient client;

    @Override
    public void init(String uri) {
        client = MongoClients.create(uri);
        String dbName = uri.substring(uri.lastIndexOf("/") + 1);
        this.connection = client.getDatabase(dbName);
    }

    @Override
    public boolean isConnected() {
        return client != null && connection != null;
    }

    @Override
    public void close() {
        if (client != null) client.close();
    }
}
