package nosql.crawl;

import nosql.aislike.DaoFactory;
import nosql.crawl.entity.Download;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MysqlDownloadDaoTest {

//    @Test
    void getAllDownloads() {
        var downloadDao = DaoFactory.INSTANCE.getDownloadDao();
        List<Download> allDownloads = downloadDao.getAllDownloads();
        System.out.println(allDownloads);
        assertNotNull(allDownloads);
        assertFalse(allDownloads.isEmpty());
    }

    @Test
    void getDownloadById() {
        var downloadDao = DaoFactory.INSTANCE.getDownloadDao();
        Download download = downloadDao.getDownloadById(489);
        System.out.println(download);
        assertNotNull(download);
    }
}