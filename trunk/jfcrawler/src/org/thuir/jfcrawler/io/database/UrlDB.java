package org.thuir.jfcrawler.io.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.thuir.jfcrawler.data.Url;

public class UrlDB {
	private Connection conn = null;
	
	private static final String table = "urlstatus";

	private static final String SQL_LOAD = 
		"SELECT * FROM " + table + " WHERE url = ?";
	private static final String SQL_SAVE = "";
	private static final String SQL_CREATE = "";
	private static final String SQL_CHECK = "";

	private PreparedStatement stmt_load = null;
	private PreparedStatement stmt_save = null;
	private PreparedStatement stmt_create = null;
	private PreparedStatement stmt_check = null;

	public UrlDB() {
		String url = "jdbc:mysql://localhost:3306/urldb";
		String username = "root";
		String password = "root";

		try {
			Class.forName( "org.gjt.mm.mysql.Driver" ); 
			conn = DriverManager.getConnection( 
					url, username, password );

			stmt_load = conn.prepareStatement(SQL_LOAD);
			stmt_save = conn.prepareStatement(SQL_SAVE);
			stmt_check = conn.prepareStatement(SQL_CHECK);
			stmt_create = conn.prepareStatement(SQL_CREATE);

		} catch ( ClassNotFoundException cnfex ) {
		} catch ( SQLException sqlex ) {
		} 
	}


	public boolean load(Url url) throws SQLException {
		ResultSet res = stmt_load.executeQuery();
		if(res.next()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean save(Url url) throws SQLException {
		ResultSet res = stmt_check.executeQuery();
		if(res.next()) {
			stmt_save.executeUpdate();
			return true;
		} else {
			stmt_create.executeUpdate();
			return false;
		}
	}

	public long check(Url url) throws SQLException {
		ResultSet res = stmt_check.executeQuery();
		if(res.next())
			return res.getLong("last-visit");
		else
			return -1l;
	}

	public void close() throws SQLException {
		stmt_load.close();
		stmt_save.close();
		stmt_check.close();
		conn.close();
	}

}
