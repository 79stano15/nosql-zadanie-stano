package sk.upjs.nosql.neo4japi;

import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.Neo4jTemplate;

import java.nio.file.Path;

@Configuration
public class Neo4jConfig {

    public static final Path DATABASE_DIRECTORY = Path.of("target/database");

    @Bean
    public DatabaseManagementService databaseManagementService() {
        DatabaseManagementService service = new DatabaseManagementServiceBuilder(DATABASE_DIRECTORY).build();
        registerShutdown(service);
        return service;
    }

    @Bean
    public GraphDatabaseService graphDatabaseService(DatabaseManagementService databaseManagementService) {
        return databaseManagementService.database("neo4j");
    }

    private void registerShutdown(DatabaseManagementService databaseManagementService) {
        Runtime.getRuntime().addShutdownHook(new Thread(databaseManagementService::shutdown));
    }
}
