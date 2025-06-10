package core;

public abstract class DatabaseProvider<C extends DatabaseConnection<?>, A extends DatabaseAdapter<?, C>> {
    protected C connection;

    public abstract C connect(String uri);
    public abstract <T> A getAdapter(Class<T> modelClass);
}

