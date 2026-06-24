package sk.upjs.nosql.neo4japi;

import org.neo4j.io.fs.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Neo4jApiApplication {

    public static void main(String[] args) {
//        deleteDb();
        ConfigurableApplicationContext context = SpringApplication.run(Neo4jApiApplication.class, args);
        DbService service = context.getBean(DbService.class);
//        service.fillDatabase();
//        service.fillDatabaseWithoutDuplicities();
//        service.printOneDownload();
        service.printShortestPathsToDetailPages();
    }

    public static void deleteDb() {
        try {
            FileUtils.deleteDirectory(Neo4jConfig.DATABASE_DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
