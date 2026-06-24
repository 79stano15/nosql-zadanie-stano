package nosql.crawl;

import java.util.List;

import nosql.crawl.entity.Download;

public interface DownloadDao {

	List<Download> getAllDownloads();

	Download getDownloadById(long downloadId);

}