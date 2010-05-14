package org.thuir.jfcrawler.io.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.ConfigUtil;

public class UrlRepository {
	private static UrlRepository instance = null;
	public static UrlRepository getInstance() {
		if(instance == null) {
			try {
				instance = new UrlRepository();
			} catch (Exception e) {
				return null;
			}
		}
		return instance;
	}
	/////////////////////
	//database
	private Connection conn = null;

	private static final String table = "urlstatus";

	private static final String SQL_LOAD = 
		"SELECT * FROM " + table + " WHERE url = ?;";
	private static final String SQL_SAVE = 
		"UPDATE " + table + 
		" SET status = ?, last_visit = ?, last_modify = ? " +
		"WHERE url = ?;";
	private static final String SQL_CREATE = 
		"INSERT INTO " + table +
		" (url, status, last_visit, last_modify)" +
		" VALUES (?, ?, ?, ?)";
	private static final String SQL_CHECK = 
		"SELECT last_visit FROM " + table + " WHERE url = ?;";

	private PreparedStatement stmt_load = null;
	private PreparedStatement stmt_save = null;
	private PreparedStatement stmt_create = null;
	private PreparedStatement stmt_check = null;
	
	private void connect() throws Exception {
		String url = 
			ConfigUtil.getConfig().getString("url-database.host");
		String username = 
			ConfigUtil.getConfig().getString("url-database.user");
		String password = 
			ConfigUtil.getConfig().getString("url-database.pass");

		Class.forName( "org.gjt.mm.mysql.Driver" ); 
		conn = DriverManager.getConnection( 
				url, username, password );

		stmt_load = conn.prepareStatement(SQL_LOAD);
		stmt_save = conn.prepareStatement(SQL_SAVE);
		stmt_check = conn.prepareStatement(SQL_CHECK);
		stmt_create = conn.prepareStatement(SQL_CREATE);
	}
	
	public synchronized boolean load(Url url) throws SQLException {
		stmt_load.setString(1, url.getUrl());
		ResultSet res = stmt_load.executeQuery();
		if(res.next()) {
			url.setStatus(res.getInt("status"));
			url.setLastVisit(res.getLong("last_visit"));
			url.setLastModify(res.getLong("last_modify"));
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean save(Url url) throws SQLException {
		stmt_check.setString(1, url.getUrl());
		ResultSet res = stmt_check.executeQuery();
		if(res.next()) {
			stmt_save.setInt(1, url.getStatus());
			stmt_save.setLong(2, url.getLastVisit());
			stmt_save.setLong(3, url.getLastModify());

			stmt_save.setString(4, url.getUrl());

			stmt_save.executeUpdate();
			return true;
		} else {			
			stmt_create.setString(1, url.getUrl());

			stmt_create.setInt(2, url.getStatus());
			stmt_create.setLong(3, url.getLastVisit());
			stmt_create.setLong(4, url.getLastModify());

			stmt_create.executeUpdate();
			return false;
		}
	}

	public synchronized long check(Url url) throws SQLException {
			stmt_check.setString(1, url.getUrl());
			ResultSet res = stmt_check.executeQuery();
			if(res.next())
				return res.getLong("last_visit");
			else
				return -1l;
	}
	
	private void submit() {
		
	}
	
	/////////////////////
	//memory
	private final int SIZE = 1024;
	private final int MAX_COUNT = 512;
	private int counter = 0;
	private Set<Url> repository = 
		Collections.synchronizedSet(new HashSet<Url>(SIZE));
	
	private synchronized long hitUrl(Url url) {
		if(repository.contains(url))
			return url.getLastVisit();
		return -1l;
	}
	
	private synchronized boolean cacheUrl(Url url) {
		if(counter++ > MAX_COUNT) {
			submit();
			repository.clear();
			counter = 0;
		}
		if(repository.contains(url)) {
			repository.add(url);
			return false;
		} else {
			repository.add(url);
			return true;
		}
	}

	/////////////////////
	protected UrlRepository() throws Exception {
		connect();
	}
	public void close() throws SQLException {
		stmt_load.close();
		stmt_save.close();
		stmt_check.close();
		stmt_create.close();
		conn.close();
	}
}
