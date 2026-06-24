package sk.upjs.nosql.neo4jrepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import nosql.crawl.entity.Download;
import nosql.crawl.entity.Page;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Node(primaryLabel = "download")
public class NeoDownload {
    @Id
    private Long id;

    @Property private String startTime;
    @Property private String endTime;
    @Property private boolean finished;
    @Property private String url;
    @Property private String country;
    @Property private String language;

    @Relationship(type = "SEED_PAGE", direction = Relationship.Direction.OUTGOING)
    private NeoPage seedPage;
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private Collection<NeoPage> pages = new HashSet<>();

    public NeoDownload(Download d) {
        this.id = d.getId();
        this.startTime = d.getStartTime().toString();
        this.endTime = d.getEndTime().toString();
        this.finished = d.isFinished();
        this.url = d.getUrl();
        this.country = d.getCountry();
        this.language = d.getLanguage();
        Map<String, NeoPage> urlToPage = new HashMap<>();
        for (Page p : d.getPages()) {
            NeoPage neoPage = urlToPage.get(p.getUrl());
            if (neoPage == null) {
                neoPage = new NeoPage(p);
                urlToPage.put(p.getUrl(), neoPage);
                pages.add(neoPage);
            }
            if (d.getSeedPage().getId() == p.getId()) {
                seedPage = neoPage;
            }
        }
        for (Page p : d.getPages()) {
            NeoPage neoPage = urlToPage.get(p.getUrl());
            Set<LinksTo> xPaths = neoPage.getXPaths();
            for(Map.Entry<String,Page> entry : p.getxPathToChildrenPages().entrySet()) {
                NeoPage targetPage = urlToPage.get(entry.getValue().getUrl());
                xPaths.add(new LinksTo(targetPage, entry.getKey()));
            }
        }
    }
}
