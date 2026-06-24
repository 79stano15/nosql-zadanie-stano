package sk.upjs.nosql.neo4jrepository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface NeoPageRepository extends Neo4jRepository<NeoPage, Long> {
    @Query("MATCH (det:page {isDetailPage: true}) RETURN det")
    List<NeoPage> findAllDetailPages();

    List<NeoPage> findAllByIsDetailPage(boolean isDetailPage);
}
