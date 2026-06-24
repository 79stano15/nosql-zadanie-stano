package nosql.crawl.entity;

import java.time.LocalDateTime;
import java.util.Collection;

public class Download {
	private Long id;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private boolean finished;
	private String url;
	private String country;
	private String language;
	private Page seedPage;
	private Collection<Page> pages;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public boolean isFinished() {
		return finished;
	}
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Page getSeedPage() {
		return seedPage;
	}
	public void setSeedPage(Page seedPage) {
		this.seedPage = seedPage;
	}
	public Collection<Page> getPages() {
		return pages;
	}
	public void setPages(Collection<Page> pages) {
		this.pages = pages;
	}
	@Override
	public String toString() {
		return "Download [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", finished=" + finished
				+ ", url=" + url + ", country=" + country + ", language=" + language + ", seedPage=" + seedPage
				+ ", #pages=" + pages.size() + "]";
	}
}
