package sk.upjs.nosql.neo4jrepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories
@SpringBootApplication
public class Neo4jRepositoryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Neo4jRepositoryApplication.class, args);
        DbService service = context.getBean(DbService.class);
        //service.fillDb();
//        service.printDetailPages();
        service.printShortestPathsToDetailPagesWithXpaths();
    }

}
