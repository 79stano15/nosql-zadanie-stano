package nosql.crawl.entity;

import java.util.HashMap;
import java.util.Map;

public class Page {
	private Long id;
	private String url;
	private Download download;
	private Map<String, Page> xPathToChildrenPages = new HashMap<>();
	private boolean isDetailPage;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Download getDownload() {
		return download;
	}
	public void setDownload(Download download) {
		this.download = download;
	}
	public Map<String, Page> getxPathToChildrenPages() {
		return xPathToChildrenPages;
	}
	public void setxPathToChildrenPages(Map<String, Page> xPathToChildrenPages) {
		this.xPathToChildrenPages = xPathToChildrenPages;
	}
	public boolean isDetailPage() {
		return isDetailPage;
	}
	public void setDetailPage(boolean isDetailPage) {
		this.isDetailPage = isDetailPage;
	}
	@Override
	public String toString() {
		return "Page [id=" + id + ", url=" + url + ", download.id=" + download.getId() + ", #xPathToChildrenPages="
				+ xPathToChildrenPages.size() + ", isDetailPage=" + isDetailPage + "]";
	}
}
