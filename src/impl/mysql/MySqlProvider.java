package impl.mysql;

import core.DatabaseConnection;
import core.DatabaseProvider;
import core.DatabaseAdapter;
import java.sql.Connection;
import impl.mysql.converters.StringIntegerHashMapConverter;
import java.util.HashMap;
import core.TypeConverterRegistry;

public class MySqlProvider extends DatabaseProvider<MySqlConnection, MySqlAdapter<?>> {

    static {
        // register type converters
        core.TypeConverterRegistry.register((Class) HashMap.class, new StringIntegerHashMapConverter());
    }

    @Override
    public MySqlConnection connect(String uri) {
        connection = new MySqlConnection();
        connection.init(uri);
        return connection;
    }

    @Override
    public <T> MySqlAdapter<T> getAdapter(Class<T> modelClass) {
        if (connection == null || !connection.isConnected()) {
            throw new IllegalStateException("Not connected to MySQL");
        }
        return new MySqlAdapter<>(modelClass, connection);
    }
}
