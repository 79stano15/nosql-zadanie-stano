package sk.upjs.nosql.neo4jrepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@RelationshipProperties()
public class LinksTo {
    @RelationshipId
    private Long id;
    @TargetNode
    private NeoPage targetNode;
    @ToString.Include
    private String xPath;

    public LinksTo(NeoPage targetNode, String xPath) {
        this.targetNode = targetNode;
        this.xPath = xPath;
    }
}
