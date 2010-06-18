package org.thuir.jfcrawler.io.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.http.HttpStatus;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.ConfigUtil;

public class UrlDB {
	private Connection conn = null;

	private static final String schema = 
		ConfigUtil.getCrawlerConfig().getString("urldb.schema");
	private static final String table = 
		ConfigUtil.getCrawlerConfig().getString("urldb.table");

	private static final String SQL_LOAD = 
		"SELECT * FROM " + table + " WHERE url = ?;";
	private static final String SQL_INSERT = 
		"INSERT INTO " + table + " (url, status, code, visit)" +
		" VALUES (?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE " +
		" status = ?, " +
		" code   = ?, " +
		" visit  = ?;";
	private static final String SQL_CHECK = 
		"SELECT code, visit, status FROM " + table + " WHERE url = ?;";
//	private static final String SQL_SAVE = 
//		"UPDATE " + table + 
//		" SET status = ?, code = ?, visit = ? " +
//		"WHERE url = ?;";
//	private static final String SQL_CREATE = 
//		"INSERT INTO " + table +
//		" (url, status, code, visit)" +
//		" VALUES (?, ?, ?, ?)";


	private PreparedStatement stmt_load = null;
//	private PreparedStatement stmt_save = null;
//	private PreparedStatement stmt_create = null;
	private PreparedStatement stmt_check = null;
	private PreparedStatement stmt_insert = null;

	public UrlDB() throws Exception {
		String host = 
			ConfigUtil.getDatabaseConfig().getString("basic.host") + "/" + schema;
		String username = 
			ConfigUtil.getDatabaseConfig().getString("basic.user");
		String password = 
			ConfigUtil.getDatabaseConfig().getString("basic.pass");

		Class.forName( "org.gjt.mm.mysql.Driver" ); 
		conn = DriverManager.getConnection( 
				host, username, password );

		stmt_load = conn.prepareStatement(SQL_LOAD);
		stmt_check = conn.prepareStatement(SQL_CHECK);
		stmt_insert = conn.prepareStatement(SQL_INSERT);
	}

	public void clear() throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(
		"DROP TABLE IF EXISTS " + table + ";" );
		stmt.executeUpdate(
				"CREATE TABLE  " + table + " (" +
//				"id     int unsigned NOT NULL AUTO_INCREMENT," +
				"url    varchar(255) NOT NULL," +
				"status int unsigned NOT NULL DEFAULT 0," +
				"code   int unsigned NOT NULL DEFAULT 0," +
				"visit  bigint NOT NULL," +
				"PRIMARY KEY (url)" +
				") ENGINE=InnoDB DEFAULT CHARSET=gb2312;"
		);
		stmt.close();
	}

	public synchronized boolean load(Url url) throws SQLException {
		stmt_load.setString(1, url.getUrl());
		ResultSet res = stmt_load.executeQuery();
		if(res.next()) {
			url.setStatus(res.getInt("status"));
			url.setCode(res.getInt("code"));
			url.setVisit(res.getLong("visit"));
			return true;
		} else {
			return false;
		}
	}

//	public synchronized boolean save(Url url) throws SQLException {
//		stmt_check.setString(1, url.getUrl());
//		ResultSet res = stmt_check.executeQuery();
//		if(res.next()) {
//			stmt_save.setShort(1, url.getStatus());
//			stmt_save.setShort(2, url.getCode());
//			stmt_save.setLong(3, url.getVisit());
//			stmt_save.setString(4, url.getUrl());
//
//			stmt_save.executeUpdate();
//			return true;
//		} else {			
//			stmt_create.setString(1, url.getUrl());
//
//			stmt_create.setShort(2, url.getStatus());
//			stmt_create.setShort(3, url.getCode());
//			stmt_create.setLong(4, url.getVisit());
//
//			stmt_create.executeUpdate();
//			return false;
//		}
//	}
	public synchronized void insert(Url url) throws SQLException {
		stmt_insert.setString(1, url.getUrl());
		stmt_insert.setInt(2, url.getStatus());
		stmt_insert.setInt(3, url.getCode());
		stmt_insert.setLong(4, url.getVisit());
		stmt_insert.setInt(5, url.getStatus());
		stmt_insert.setInt(6, url.getCode());
		stmt_insert.setLong(7, url.getVisit());
		
		stmt_insert.execute();
	}

	public synchronized long check(Url url) throws SQLException {
			stmt_check.setString(1, url.getUrl());
			ResultSet res = stmt_check.executeQuery();
			if(res.next() && res.getInt("code") == HttpStatus.SC_OK)
				return res.getLong("visit");
			else
				return -1l;
	}

	public void close() throws SQLException {
		stmt_load.close();
		stmt_check.close();
		conn.close();
	}

}
