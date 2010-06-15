package org.thuir.forum.filter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ArticleInfo;
import org.thuir.forum.data.BoardInfo;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Info;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class ForumDBFilter extends Filter {
	private static Logger logger = Logger.getLogger(ForumDBFilter.class);
	
	private Connection conn = null;

	private static final String forum_schema = 
		ConfigUtil.getConfig().getString("foruminfo");
	private static final String article_table = "articleinfo";
	private static final String board_table   = "boardinfo";
	
	private static final String SQL_INSERT_A = 
		"INSERT INTO ? " +
		" (token, id, key, page, boardId, boardKey, position)" +
		" VALUES (?, ?, ?, ?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE " +
		" id = ?, key = ?, page = ?," +
		" boardId = ?, boardKey = ?, position = ?;";
	private static final String SQL_INSERT_B = 
		"INSERT INTO ? " + 
		" (token, id, key, page)" +
		" VALUES (?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE " +
		" id = ?, key = ?, page = ?;";
	private static final String SQL_CHECK =
		"SELECT * FROM ? WHERE token = ?;";
	private static final String SQL_INSERT_URL =
		"INSERT INTO urlref (url, token) VALUES (?, ?)" +
		" ON DUPLICATE KEY UPDATE token = ?;";
	
	private PreparedStatement stmt_insert_a = null;
	private PreparedStatement stmt_insert_b = null;
	private PreparedStatement stmt_check = null;
	private PreparedStatement stmt_insert_url = null;
	
	
	public void initial() throws Exception {
		String host = 
			ConfigUtil.getConfig().getString("url-database.host") + "/" + forum_schema;
		String username = 
			ConfigUtil.getConfig().getString("url-database.user");
		String password = 
			ConfigUtil.getConfig().getString("url-database.pass");

		Class.forName( "org.gjt.mm.mysql.Driver" ); 
		conn = DriverManager.getConnection( 
				host, username, password );

		stmt_insert_a = conn.prepareStatement(SQL_INSERT_A);
		stmt_insert_b = conn.prepareStatement(SQL_INSERT_B);
		stmt_check = conn.prepareStatement(SQL_CHECK);
		stmt_insert_url = conn.prepareStatement(SQL_INSERT_URL);
		
		String create_url_sql =
			"CREATE TABLE  urlref (" +
			"url      varchar(127) NOT NULL, " +
			"token    varchar(127) NOT NULL, " +
			"PRIMARY KEY (url)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS urlref;");
		stmt.executeUpdate(create_url_sql);
		stmt.close();
	}

	public void initialArticle(String checkA) throws Exception {
		String create_article_sql = 
			"CREATE TABLE  " + article_table + " (" +
			"aId      int unsigned NOT NULL AUTO_INCREMENT" +
			"id       bigint unsigned NOT NULL DFEAULT -1," +
			"key      varchar(63)," +
			"page     int unsigned NOT NULL DEFAULT -1," +
			
			"boardId  bigint unsigned NOT NULL DEFAULT -1," +
			"boardKey varchar(63)," +
			"position int unsigned NOT NULL DEFAULT -1," +
			"PRIMARY KEY (aId)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS " + article_table + ";");
		stmt.executeUpdate(create_article_sql);
		stmt.close();
	}
	
	public void initialBoard(String checkB) throws Exception {
		String create_board_sql = 
			"CREATE TABLE  " + board_table + " (" +
			"bId      int unsigned NOT NULL AUTO_INCREMENT" +
			"id       bigint unsigned NOT NULL DFEAULT -1," +
			"key      varchar(63)," +
			"page     int unsigned NOT NULL DEFAULT -1," +
			"PRIMARY KEY (bId)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS " + board_table + ";");
		stmt.executeUpdate(create_board_sql);
		stmt.close();
	}
	
	public void insert(Info info, String url) {
		try {
			if(info instanceof ArticleInfo) {
				ArticleInfo aInfo = (ArticleInfo)info;

				stmt_insert_a.setString(1, article_table);
				stmt_insert_a.setLong(2, aInfo.getId());
				stmt_insert_a.setString(3, aInfo.getKey());
				stmt_insert_a.setInt(4, aInfo.getPage());
				
				stmt_insert_a.setLong(5, aInfo.getBoardId());
				stmt_insert_a.setString(6, aInfo.getBoardKey());
				stmt_insert_a.setInt(7, aInfo.getPosition());
				
				stmt_insert_a.setLong(8, aInfo.getId());
				stmt_insert_a.setString(9, aInfo.getKey());
				stmt_insert_a.setInt(10, aInfo.getPage());
				
				stmt_insert_a.setLong(11, aInfo.getBoardId());
				stmt_insert_a.setString(12, aInfo.getBoardKey());
				stmt_insert_a.setInt(13, aInfo.getPosition());
				
				stmt_insert_a.executeUpdate();
			}
			if(info instanceof BoardInfo) {
				BoardInfo bInfo = (BoardInfo)info;
				
				stmt_insert_b.setString(1, board_table);
				stmt_insert_b.setLong(2, bInfo.getId());
				stmt_insert_b.setString(3, bInfo.getKey());
				stmt_insert_b.setInt(4, bInfo.getPage());
				
				stmt_insert_b.setLong(5, bInfo.getId());
				stmt_insert_b.setString(6, bInfo.getKey());
				stmt_insert_b.setInt(7, bInfo.getPage());
				
				stmt_insert_b.executeUpdate();
			}
			stmt_insert_url.setString(1, url);
			stmt_insert_url.setString(2, info.getToken());
			stmt_insert_url.setString(3, info.getToken());
			
			stmt_insert_url.executeUpdate();
		} catch(Exception e) {
			logger.error(e);
		}
	}
	
	public boolean check(Info info) {
		try {
			String table = null;
			if(info instanceof ArticleInfo) {
				table = article_table;
			}
			if(info instanceof BoardInfo) {
				table = board_table;
			}
			stmt_check.setString(1, table);
			stmt_check.setString(2, info.getToken());
			
			ResultSet rs = stmt_check.executeQuery();
			if(rs.next())
				return false;
		} catch(Exception e) {
			logger.error(e);
		}
		return false;
	}

	@Override
	public boolean shouldVisit(Url url) {
		if(url instanceof ForumUrl) {
			Info i = ((ForumUrl)url).getForumInfo();
			boolean isExist = check(i);
			insert(i, url.getUrl());
			return isExist;
		}
		return false;
	}

}
