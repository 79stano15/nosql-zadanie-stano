package sk.upjs.nosql.neo4jrepository;

import nosql.aislike.DaoFactory;
import nosql.crawl.DownloadDao;
import nosql.crawl.entity.Download;
import org.neo4j.driver.Driver;
import org.neo4j.driver.EagerResult;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.InternalRelationship;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {

    DownloadDao downloadDao = DaoFactory.INSTANCE.getDownloadDao();
    private final NeoDownloadRepository neoDownloadRepository;
    private final NeoPageRepository neoPageRepository;
    private final Neo4jTemplate neo4jTemplate;
    private final Driver driver;

    public DbService(NeoDownloadRepository neoDownloadRepository,
                     NeoPageRepository neoPageRepository,
                     Neo4jTemplate neo4jTemplate,
                     Driver driver) {
        this.neoDownloadRepository = neoDownloadRepository;
        this.neoPageRepository = neoPageRepository;
        this.neo4jTemplate = neo4jTemplate;
        this.driver = driver;
    }

    public void fillDb() {
        List<Download> allDownloads = downloadDao.getAllDownloads();
        Download download = allDownloads.get(0);
        NeoDownload neoDownload = new NeoDownload(download);
        neoDownloadRepository.save(neoDownload);
    }

    public void printDetailPages() {
//        List<NeoPage> detailPages = neoPageRepository.findAllDetailPages();
//        List<NeoPage> detailPages = neoPageRepository.findAllByIsDetailPage(true);
        List<NeoPage> detailPages = neo4jTemplate.findAll("MATCH (det:page {isDetailPage: true}) RETURN det", NeoPage.class);
        detailPages.forEach(p -> System.out.println(p));
    }

    public void printShortestPathsToDetailPages() {
        String query = "MATCH (seed: page)<-[:SEED_PAGE]-(d:download),(det:page {isDetailPage: true}), " +
                "p = shortestPath((seed)-[:LINKS_TO*]->(det)) RETURN nodes(p) as sPath";
        EagerResult result = driver.executableQuery(query).execute();
        List<Record> records = result.records();
        for (Record record : records) {
            List<Object> sPath = record.get("sPath").asList();
            for(Object neoNode : sPath) {
                InternalNode node = (InternalNode) neoNode;
                System.out.print(node.get("url") + " ");
            }
            System.out.println();
        }
    }

    public void printShortestPathsToDetailPagesWithXpaths() {
        String query = "MATCH (seed: page)<-[:SEED_PAGE]-(d:download),(det:page {isDetailPage: true}), " +
                "p = shortestPath((seed)-[:LINKS_TO*]->(det)) RETURN nodes(p) as sPath, relationships(p) as rels";
        EagerResult result = driver.executableQuery(query).execute();
        List<Record> records = result.records();
        for (Record record : records) {
            List<Object> sPath = record.get("sPath").asList();
            List<Object> rels = record.get("rels").asList();
            for(int i = 0; i < rels.size(); i++) {
                InternalRelationship rel = (InternalRelationship) rels.get(i);
                InternalNode node = (InternalNode) sPath.get(i+1);
                System.out.print(rel.get("xPath") + " -> ");
                System.out.print(node.get("url") + " - ");
            }
            System.out.println();
        }
    }
}
