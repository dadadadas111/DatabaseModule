package core;

public abstract class DatabaseConnection<Con> implements AutoCloseable {
    protected Con connection;

    public abstract void init(String uri);
    public Con getConnection() {
        return connection;
    }
    public abstract boolean isConnected();
    public abstract void close();
}

