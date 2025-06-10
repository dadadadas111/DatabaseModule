package core;

public interface DatabaseProvider {
    DatabaseConnection connect(String uri);
    <T> DatabaseAdapter<T> getAdapter(Class<T> modelClass);
}

