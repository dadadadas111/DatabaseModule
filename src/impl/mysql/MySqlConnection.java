package impl.mysql;

import core.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection extends DatabaseConnection<Connection> {
    @Override
    public void init(String uri) {
        try {
            this.connection = DriverManager.getConnection(uri);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to MySQL: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            // do something
        }
    }
}

