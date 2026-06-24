package nosql.crawl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nosql.crawl.entity.Download;
import nosql.crawl.entity.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class MysqlDownloadDao implements DownloadDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public MysqlDownloadDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/* (non-Javadoc)
	 * @see sk.gursky.nosql.crawl.DownloadDao#getAllDownloads()
	 */
	@Override
	public List<Download> getAllDownloads() {
		String sql = "SELECT DISTINCT(id_download) as id FROM crawl.urltree";
		return jdbcTemplate.query(sql, new RowMapper<Download>() {
			@Override
			public Download mapRow(ResultSet rs, int line) throws SQLException {
				return getDownloadById(rs.getLong("id"));				
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see sk.gursky.nosql.crawl.DownloadDao#getDownloadById(long)
	 */
	@Override
	public Download getDownloadById(long downloadId) {
		String sql = "SELECT download.start_time, download.end_time, "
				+ "`source`.url, country.name as country_name, language.name as language_name " + 
				"FROM crawl.download " + 
				"JOIN crawl.`source` ON download.id_source = `source`.id " + 
				"JOIN crawl.country ON `source`.id_country = country.id " + 
				"JOIN crawl.language ON `source`.id_language = language.id " + 
				"WHERE download.id = " + downloadId;
		final Download download = jdbcTemplate.query(sql, new ResultSetExtractor<Download>() {
			@Override
			public Download extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (!rs.next()) return null;
				Download download = new Download();
				download.setId(downloadId);
				download.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
				download.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
				download.setUrl(rs.getString("url"));
				download.setCountry(rs.getString("country_name"));
				download.setLanguage(rs.getString("language_name"));
				return download;
			}
		});
		if (download == null) {
			return null;
		}
		String sql2 = "SELECT urltree.id, parentId, url, isDetailPage, xpath " +
				"FROM crawl.urltree " + 
				"JOIN crawl.xpathtable ON urltree.xPathIdUrl = xpathtable.idURL " + 
				"WHERE id_download = " + downloadId + 
				" ORDER BY urltree.id";
		jdbcTemplate.query(sql2, new ResultSetExtractor<Void>() {
			@Override
			public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
				Map<Long, Page> pages = new HashMap<>();
				Page page = null;
				while (rs.next()) {
					Long pageId = rs.getLong("id");
					Page parent = null;
					if (page == null || page.getId() != pageId) {
						page = new Page();
						if (pages.size() == 0)
							download.setSeedPage(page);
						pages.put(pageId, page);
						page.setId(pageId);
						page.setUrl(rs.getString("url"));
						page.setDetailPage(rs.getInt("isDetailPage") == 1);
						page.setDownload(download);
						long parentId = rs.getLong("parentId");
						if (parentId > 0) {
							parent = pages.get(parentId);							
						} else {
							parent = null;
						}
					}
					if (parent != null) {
						parent.getxPathToChildrenPages().put(rs.getString("xpath"), page);
					}
				}
				download.setPages(pages.values());
				return null;
			}
		});
		return download;
	}
}