package CouchDB;

import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static Utilities.Main.generateUUID;
import static Utilities.Main.readPropertiesFile;

public class CouchDBClient {
    private String databaseHostname;
    private String databasePort;
    private String databaseName;
    private CouchDbConnector database;

    public void connect() throws IOException {
        Properties properties = readPropertiesFile("src/main/resources/config.properties");
        this.databaseHostname = properties.getProperty("DEVELOPMENT_DATABASE_HOSTNAME");
        this.databasePort = properties.getProperty("DEVELOPMENT_DATABASE_PORT");
        this.databaseName = properties.getProperty("DEVELOPMENT_DATABASE_NAME");

        String connectionUrl = this.constructConnectionUrl(databaseHostname, databasePort);

        this.database = new StdCouchDbConnector(
                this.databaseName,
                new StdCouchDbInstance(
                        new StdHttpClient.Builder().url(connectionUrl).build()
                )
        );

        this.database.createDatabaseIfNotExists();
    }

    public String createAttachment(String contentType, InputStream inputStream) {
        String documentId = generateUUID();
        String attachmentId = "attachment";

        this.database.createAttachment(
                documentId,
                new AttachmentInputStream(
                        attachmentId,
                        inputStream,
                        contentType
                )
        );

        return this.constructAttachmentUrl(
                this.databaseHostname,
                this.databasePort,
                this.databaseName,
                documentId,
                attachmentId
        );
    }

    private String constructAttachmentUrl(
            String databaseHostname,
            String databasePort,
            String databaseName,
            String documentId,
            String attachmentId
    ) {
        return this.constructConnectionUrl(databaseHostname, databasePort)
                + "/" + databaseName + "/" + documentId + "/" + attachmentId;
    }

    private String constructConnectionUrl(String databaseHostname, String databasePort) {
        return "http://" + databaseHostname + ":" + databasePort;
    }
}
