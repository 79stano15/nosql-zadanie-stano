package sk.upjs.nosql.neo4jrepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.crawl.entity.Download;
import nosql.crawl.entity.Page;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Node(primaryLabel = "page")
public class NeoPage {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String url;
    @Property
    private boolean isDetailPage;
    @Relationship(type = "LINKS_TO", direction = Relationship.Direction.OUTGOING)
    private Set<LinksTo> xPaths = new HashSet<>();

    public NeoPage(Page p) {
        this.url = p.getUrl();
        this.isDetailPage = p.isDetailPage();
    }
}
