package sk.upjs.nosql.neo4japi;

import nosql.aislike.DaoFactory;
import nosql.crawl.DownloadDao;
import nosql.crawl.entity.Download;
import nosql.crawl.entity.Page;
import org.neo4j.graphalgo.BasicEvaluationContext;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class DbService {
    private DownloadDao downloadDao = DaoFactory.INSTANCE.getDownloadDao();
    private GraphDatabaseService db;

    public static final Label DOWNLOAD = Label.label("Download");
    public static final Label PAGE = Label.label("Page");
    public static final RelationshipType CONTAINS = RelationshipType.withName("Contains");
    public static final RelationshipType SEED_PAGE = RelationshipType.withName("SeedPage");
    public static final RelationshipType LINKS_TO = RelationshipType.withName("Links to");

    public DbService(GraphDatabaseService db) {
        this.db = db;
    }

    public void fillDatabase() {
        List<Download> allDownloads = downloadDao.getAllDownloads();
        long start = System.nanoTime();
        System.out.println("Creating indexes...");
        try(Transaction tx = db.beginTx()) {
            tx.schema().indexFor(DOWNLOAD).on("id").create();
            tx.schema().indexFor(PAGE).on("id").create();
            tx.commit();
        }
        try(Transaction tx = db.beginTx()) {
            tx.schema().awaitIndexesOnline(1, TimeUnit.MINUTES);
        }
        System.out.println("Indexes created in " + (System.nanoTime() - start)/1_000_000 + " ms");
        try(Transaction tx = db.beginTx()) {
            for (Download download : allDownloads) {
                Node node = tx.createNode(DOWNLOAD);
                node.setProperty("id", download.getId());
                node.setProperty("url", download.getUrl());
                node.setProperty("startTime", download.getStartTime().toString());
            }
            tx.commit();
        }
        System.out.println("Download nodes created " + (System.nanoTime() - start)/1_000_000 + " ms");
        for (Download download : allDownloads) {
            try(Transaction tx = db.beginTx()) {
                Node dNode = tx.findNode(DOWNLOAD, "id", download.getId());
                for(Page page : download.getPages()) {
                    Node pNode = tx.createNode(PAGE);
                    pNode.setProperty("id", page.getId());
                    pNode.setProperty("url", page.getUrl());
                    pNode.setProperty("isDetailPage", page.isDetailPage());
                    dNode.createRelationshipTo(pNode, CONTAINS);
                    if (download.getSeedPage().getId() == page.getId()) {
                        dNode.createRelationshipTo(pNode, SEED_PAGE);
                    }
                }
                tx.commit();
            }
        }
        System.out.println("Page nodes created " + (System.nanoTime() - start)/1_000_000 + " ms");

        for(Download download : allDownloads) {
            for(Page page : download.getPages()) {
                try(Transaction tx = db.beginTx()) {
                    Node n1 = tx.findNode(PAGE, "id", page.getId());
                    for (Map.Entry<String, Page> pair: page.getxPathToChildrenPages().entrySet()) {
                        Node n2 = tx.findNode(PAGE, "id", pair.getValue().getId());
                        Relationship r = n1.createRelationshipTo(n2, LINKS_TO);
                        r.setProperty("xPath", pair.getKey());
                    }
                    tx.commit();
                }
            }
        }
        System.out.println("Page links created " + (System.nanoTime() - start)/1_000_000 + " ms");
    }
    public void printOneDownload() {
        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> dNnodes = tx.findNodes(DOWNLOAD);
            Node seedNode = dNnodes.next().getSingleRelationship(SEED_PAGE, Direction.OUTGOING)
                    .getEndNode();
            TraversalDescription td = tx.traversalDescription()
                    .depthFirst()
                    .relationships(LINKS_TO, Direction.OUTGOING);
            Traverser traverser = td.traverse(seedNode);
            Iterator<Path> paths = traverser.iterator();
            int count = 0;
            while (paths.hasNext()) {
                Path path = paths.next();
                count++;
                System.out.print(count + ": ");
                for(int i = 0; i < path.length(); i++) {
                    System.out.print("  ");
                }
                System.out.println(path.endNode().getProperty("url"));
            }
        }
    }

    private Node getPageNode(Transaction tx, String url, long downloadId) {
        ResourceIterator<Node> nodes = tx.findNodes(PAGE, "url", url);
        while (nodes.hasNext()) {
            Node node = nodes.next();
            if (node.getSingleRelationship(CONTAINS, Direction.INCOMING)
                    .getStartNode().getProperty("id").equals(downloadId)) {
                return node;
            }
        }
        return null;
    }

    public void fillDatabaseWithoutDuplicities() {
        List<Download> allDownloads = downloadDao.getAllDownloads();
        long start = System.nanoTime();
        System.out.println("Creating indexes...");
        try(Transaction tx = db.beginTx()) {
            tx.schema().indexFor(DOWNLOAD).on("id").create();
            tx.schema().indexFor(PAGE).on("url").create();
            tx.commit();
        }
        try(Transaction tx = db.beginTx()) {
            tx.schema().awaitIndexesOnline(1, TimeUnit.MINUTES);
        }
        System.out.println("Indexes created in " + (System.nanoTime() - start)/1_000_000 + " ms");
        try(Transaction tx = db.beginTx()) {
            for (Download download : allDownloads) {
                Node node = tx.createNode(DOWNLOAD);
                node.setProperty("id", download.getId());
                node.setProperty("url", download.getUrl());
                node.setProperty("startTime", download.getStartTime().toString());
            }
            tx.commit();
        }
        System.out.println("Download nodes created " + (System.nanoTime() - start)/1_000_000 + " ms");
        for (Download download : allDownloads) {
            Long seedPageId = download.getSeedPage().getId();
            try(Transaction tx = db.beginTx()) {
                Node dNode = tx.findNode(DOWNLOAD, "id", download.getId());
                for(Page page : download.getPages()) {
                    Node pNode = getPageNode(tx, page.getUrl(), download.getId());
                    if (pNode == null) {
                        pNode = tx.createNode(PAGE);
                        pNode.setProperty("id", page.getId());
                        pNode.setProperty("url", page.getUrl());
                        pNode.setProperty("isDetailPage", page.isDetailPage());
                        dNode.createRelationshipTo(pNode, CONTAINS);
                    }
                    if (seedPageId == page.getId()) {
                        dNode.createRelationshipTo(pNode, SEED_PAGE);
                    }
                }
                tx.commit();
            }
        }
        System.out.println("Page nodes created " + (System.nanoTime() - start)/1_000_000 + " ms");

        for(Download download : allDownloads) {
            for(Page page : download.getPages()) {
                try(Transaction tx = db.beginTx()) {
                    //Node n1 = tx.findNode(PAGE, "id", page.getId());
                    Node n1 = getPageNode(tx, page.getUrl(), download.getId());
                    for (Map.Entry<String, Page> pair: page.getxPathToChildrenPages().entrySet()) {
                        //Node n2 = tx.findNode(PAGE, "id", pair.getValue().getId());
                        Node n2 = getPageNode(tx, pair.getValue().getUrl(), download.getId());
                        Relationship r = n1.createRelationshipTo(n2, LINKS_TO);
                        r.setProperty("xPath", pair.getKey());
                    }
                    tx.commit();
                }
            }
        }
        System.out.println("Page links created " + (System.nanoTime() - start)/1_000_000 + " ms");
    }

    public void printShortestPathsToDetailPages() {
        try(Transaction tx = db.beginTx()) {
            ResourceIterator<Node> dNnodes = tx.findNodes(DOWNLOAD);
            Node seedNode = dNnodes.next().getSingleRelationship(SEED_PAGE, Direction.OUTGOING)
                    .getEndNode();
            TraversalDescription td = tx.traversalDescription()
                    .depthFirst()
                    .relationships(LINKS_TO, Direction.OUTGOING)
                    .evaluator(new Evaluator() {
                        @Override
                        public Evaluation evaluate(Path path) {
                            if (path.endNode().getProperty("isDetailPage").equals(true)) {
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            } else {
                                return Evaluation.EXCLUDE_AND_CONTINUE;
                            }
                        }
                    });
            Traverser traverser = td.traverse(seedNode);
            Iterator<Path> paths = traverser.iterator();
            PathFinder<Path> pathFinder = GraphAlgoFactory.shortestPath(new BasicEvaluationContext(tx, db),
                    PathExpanders.forTypeAndDirection(LINKS_TO, Direction.OUTGOING), 50);
            int count = 0;
            while (paths.hasNext()) {
                Path path = paths.next();
                Path shortestPath = pathFinder.findSinglePath(seedNode, path.endNode());
                count++;
                System.out.print(count + ": ");
                for(int i = 0; i < shortestPath.length(); i++) {
                    System.out.print("  ");
                }
                System.out.println(shortestPath.endNode().getProperty("url"));
            }
        }

    }
}
