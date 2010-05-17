package org.thuir.jfcrawler.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class BufferedUrlRepository extends UrlRepository {
	private static final int MAX_SIZE = 1024;
	private int bufferSize = 0;
	private Map<String, Url> buffer = new HashMap<String, Url>(2 * MAX_SIZE);
	private Set<Integer> tables = new HashSet<Integer>();

	private static final String SQL_CHECK = 
		"SELECT last_visit FROM ? WHERE url = ?;";
	private static final String SQL_INSERT = 
		"INSERT INTO ? (url, status, last_visit, last_modify)" +
		" VALUES (?, ?, ?, ?)" +
		" ON DUPLICATE KEY UPDATE url= ?";

	private Connection conn = null;
	private PreparedStatement checkStmt = null;
	private PreparedStatement insertStmt = null;

	protected void synchronize() throws SQLException {
		conn.setAutoCommit(false);

		for(Url url : buffer.values()) {
			insertStmt.setString(1, table(url.getHost()));
			insertStmt.setString(2, url.getUrl());
			insertStmt.setInt(3, url.getStatus());
			insertStmt.setLong(4, url.getLastVisit());
			insertStmt.setLong(5, url.getLastModify());
			insertStmt.setString(6, url.getUrl());

			insertStmt.addBatch();
		}
		insertStmt.executeBatch();
		
		conn.setAutoCommit(true);
	}

	protected String table(String host) {
		int temp = host.hashCode();
		if(!tables.contains(temp)) {
			try {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(
						"DROP TABLE IF EXISTS urldb." + temp + ";"
				);
				stmt.executeUpdate(
						"CREATE TABLE urldb." + temp + " (" +
						"url varchar(1023) NOT NULL," +
						"status int(10) unsigned NOT NULL," +
						"last_visit bigint NOT NULL," +
						"last_modify bigint NOT NULL," +
						"PRIMARY KEY (url)" +
						") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
				);
				stmt.close();
			} catch(Exception e) {
				//TODO
			}
		}
		return String.valueOf(temp);
	}

	protected boolean checkInDB(Url url) {
		try {
			checkStmt.setString(1, table(url.getHost()));
			checkStmt.setString(2, url.getUrl());

			ResultSet res = checkStmt.executeQuery();
			if(res.next()) {
				return System.currentTimeMillis() - res.getLong("last_visit") 
				> url.getRevisitInterval();
			} else {
				return true;
			}
		} catch (SQLException e) {
			return true;
		}
	}

	@Override
	public boolean check(Url url) {
		Url temp = buffer.get(url);
		if(temp == null)
			return checkInDB(url);

		return (System.currentTimeMillis() - temp.getLastVisit()) 
		> url.getRevisitInterval();
	}

	@Override
	public void initialize() {
		try {
			String url = 
				ConfigUtil.getConfig().getString("url-database.host");
			String username = 
				ConfigUtil.getConfig().getString("url-database.user");
			String password = 
				ConfigUtil.getConfig().getString("url-database.pass");

			Class.forName( "org.gjt.mm.mysql.Driver" ); 
			conn = DriverManager.getConnection( 
					url, username, password );
			
			checkStmt = conn.prepareStatement(SQL_CHECK);
			insertStmt  = conn.prepareStatement(SQL_INSERT);
		} catch (SQLException e) {
			//TODO
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public synchronized void submit(Url url) {
		buffer.put(url.getUrl(), url);
		try {
			if(++bufferSize > MAX_SIZE) {
				synchronize();

				buffer.clear();
				bufferSize = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public synchronized void close() {
		try {
			synchronize();

			buffer.clear();
			bufferSize = 0;
			
			checkStmt.close();
			insertStmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
	}

}
