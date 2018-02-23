package CouchDB;

public class CouchDBClientSingleton {
    private static CouchDBClient client;

    public static CouchDBClient getInstance() {
        if (client == null) {
            client = new CouchDBClient();
        }
        return client;
    }
}
