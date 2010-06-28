package org.thuir.forum.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ArticleInfo;
import org.thuir.forum.data.BoardInfo;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Info;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.ConfigUtil;

public class ForumInfoDatabase {
	private static Logger logger = Logger.getLogger(ForumInfoDatabase.class);
	public static final String NAME = "ForumInfoDatabase";
	
	private Connection conn = null;

	private static final String forum_schema = 
		ConfigUtil.getDatabaseConfig().getString("foruminfo.schema");
	private static final String article_table = 
		ConfigUtil.getDatabaseConfig().getString("foruminfo.article-table");
	private static final String board_table   = 
		ConfigUtil.getDatabaseConfig().getString("foruminfo.board-table");
	private static final String urlref_table  = 
		ConfigUtil.getDatabaseConfig().getString("foruminfo.urlref-table");
	
	private static final String SQL_INSERT_A = 
		"INSERT INTO " + article_table + 
		" (token, infoId, infoKey, infoPage, boardId, boardKey, position)" +
		" VALUES (?, ?, ?, ?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE " +
		" infoId = ?, infoKey = ?, infoPage = ?," +
		" boardId = ?, boardKey = ?, position = ?, trial = trial + 1;";
	private static final String SQL_INSERT_B = 
		"INSERT INTO " + board_table + 
		" (token, infoId, infoKey, infoPage)" +
		" VALUES (?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE " +
		" infoId = ?, infoKey = ?, infoPage = ?, trial = trial + 1;";
	private static final String SQL_CHECK_A =
		"SELECT * FROM " + article_table + " WHERE token = ?;";
	private static final String SQL_CHECK_B =
		"SELECT * FROM " + board_table + " WHERE token = ?;";
	private static final String SQL_INSERT_URL =
		"INSERT INTO " + urlref_table + " (url, token) VALUES (?, ?)" +
		" ON DUPLICATE KEY UPDATE token = ?;";
	private static final String SQL_REVISIT = 
		"SELECT infoId, infoKey, infoPage, url FROM (" +
		board_table + " INNER JOIN " + urlref_table +
		" ON " + urlref_table +	".token = " + board_table +	".token" +
		") WHERE infopage = -1;"; 
	
	private PreparedStatement stmt_insert_a = null;
	private PreparedStatement stmt_insert_b = null;
	private PreparedStatement stmt_check_a = null;
	private PreparedStatement stmt_check_b = null;
	private PreparedStatement stmt_insert_url = null;
	private PreparedStatement stmt_revisit = null;
	
	
	public void initial() throws Exception {
		String host = 
			ConfigUtil.getDatabaseConfig().getString("basic.host") + "/" + forum_schema;
		String username = 
			ConfigUtil.getDatabaseConfig().getString("basic.user");
		String password = 
			ConfigUtil.getDatabaseConfig().getString("basic.pass");

		Class.forName( "org.gjt.mm.mysql.Driver" ); 
		conn = DriverManager.getConnection( 
				host, username, password );

		stmt_insert_a = conn.prepareStatement(SQL_INSERT_A);
		stmt_insert_b = conn.prepareStatement(SQL_INSERT_B);
		stmt_check_a = conn.prepareStatement(SQL_CHECK_A);
		stmt_check_b = conn.prepareStatement(SQL_CHECK_B);
		stmt_insert_url = conn.prepareStatement(SQL_INSERT_URL);
		
		String create_url_sql =
			"CREATE TABLE  " + urlref_table + " (" +
			"url      varchar(127) NOT NULL, " +
			"token    varchar(127) NOT NULL, " +
			"PRIMARY KEY (url)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS urlref;");
		stmt.executeUpdate(create_url_sql);
		stmt.close();
		
		stmt_revisit = conn.prepareStatement(SQL_REVISIT);
	}

	public void initialArticle() throws Exception {
		String create_article_sql = 
			"CREATE TABLE  " + article_table + " (" +
			"token    varchar(255) NOT NULL, " +
			"infoId   bigint NOT NULL DEFAULT '-1'," +
			"infoKey  varchar(63) NOT NULL DEFAULT 'null'," +
			"infoPage int NOT NULL DEFAULT '-1'," +
			
			"boardId  bigint NOT NULL DEFAULT '-1'," +
			"boardKey varchar(63) NOT NULL DEFAULT 'null'," +
			"position int NOT NULL DEFAULT '-1'," +
			
			"trial    int NOT NULL DEFAULT '0', " +
			"PRIMARY KEY (token)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS " + article_table + ";");
		stmt.executeUpdate(create_article_sql);
		stmt.close();
	}
	
	public void initialBoard() throws Exception {
		String create_board_sql = 
			"CREATE TABLE  " + board_table + " (" +
			"token    varchar(255) NOT NULL, " +
			"infoId   bigint NOT NULL DEFAULT '-1'," +
			"infoKey  varchar(63) NOT NULL DEFAULT 'null'," +
			"infoPage int NOT NULL DEFAULT '-1'," +
			
			"trial    int NOT NULL DEFAULT '0', " +
			"PRIMARY KEY (token)" +
			") ENGINE=InnoDB DEFAULT CHARSET=gb2312;";
		
		Statement stmt = conn.createStatement();
		stmt.executeUpdate("DROP TABLE IF EXISTS " + board_table + ";");
		stmt.executeUpdate(create_board_sql);
		stmt.close();
	}
	
	public ForumInfoDatabase() {
		try {
			this.initial();
			this.initialArticle();
			this.initialBoard();
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public synchronized void insert(Info info, String url) {
		try {
			if(info instanceof ArticleInfo) {
				ArticleInfo aInfo = (ArticleInfo)info;

				stmt_insert_a.setString(1, aInfo.getToken());
				
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
				
				stmt_insert_b.setString(1, bInfo.getToken());
				
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
	
	public synchronized boolean check(Info info) {
		try {
			if(info instanceof ArticleInfo) {
				stmt_check_a.setString(1, info.getToken());
				
				ResultSet rs = stmt_check_a.executeQuery();
				if(rs.next())
					return false;

				return true;
			}
			if(info instanceof BoardInfo) {
				stmt_check_b.setString(1, info.getToken());
				
				ResultSet rs = stmt_check_b.executeQuery();
				if(rs.next())
					return false;

				return true;
			}
		} catch(Exception e) {
			logger.error(e);
		}
		return false;
	}
	
	public List<ForumUrl> revisit() {
		ArrayList<ForumUrl> ret = new ArrayList<ForumUrl>();
		try {
			ResultSet res = stmt_revisit.executeQuery();
			while(res.next()) {
				ForumUrl url = (ForumUrl)Url.parse(res.getString("url"));
				url.setTag(Tag.BOARD);
				Info info = new BoardInfo();
				info.setId(res.getLong("infoId"));
				info.setKey(res.getString("infoKey"));
				info.setPage(res.getInt("infoPage"));
				url.setForumInfo(info);
				
				ret.add(url);
			}
		} catch(Exception e) {
			logger.error(e);
		} 
		return ret;
	}
}
