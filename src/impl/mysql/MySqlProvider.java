package impl.mysql;

import core.DatabaseConnection;
import core.DatabaseProvider;
import core.DatabaseAdapter;
import impl.mysql.converters.DoubleConverter;
import impl.mysql.converters.IntergerConverter;
import impl.mysql.converters.StringConverter;
import impl.mysql.converters.HashMapConverter;
import java.util.HashMap;
import core.TypeConverterRegistry;

public class MySqlProvider implements DatabaseProvider {
    private MySqlConnection connection;

    static {
        // register type converters
        core.TypeConverterRegistry.register(HashMap.class, new HashMapConverter());
    }

    @Override
    public DatabaseConnection connect(String uri) {
        connection = new MySqlConnection();
        connection.init(uri);
        return connection;
    }

    @Override
    public <T> DatabaseAdapter<T> getAdapter(Class<T> modelClass) {
        if (connection == null || !connection.isConnected()) {
            throw new IllegalStateException("Not connected to MySQL");
        }
        return new MySqlAdapter<>(modelClass, connection.getConnection());
    }
}
