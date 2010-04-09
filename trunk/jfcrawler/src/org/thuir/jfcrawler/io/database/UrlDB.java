package org.thuir.jfcrawler.io.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.thuir.jfcrawler.data.PageUrl;

public class UrlDB {
	private Connection connection = null; 
	private Statement statement = null; 

	public UrlDB() {
		String url = "jdbc:mysql://localhost:3306/urldb";
		String username = "root"; 
		String password = "root"; 

		try { 
			Class.forName( "org.gjt.mm.mysql.Driver" ); 
			connection = DriverManager.getConnection( 
					url, username, password );
		} catch ( ClassNotFoundException cnfex ) {
		} catch ( SQLException sqlex ) {
		} 
	}
	
	public void load(PageUrl url) {

	}
	
	public void save(PageUrl url) {
		
	}
}
