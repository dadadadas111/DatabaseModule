package core;

public interface DatabaseConnection<Con> extends AutoCloseable {
    void init(String uri);
    Con getConnection();
    boolean isConnected();
    void close();
}

