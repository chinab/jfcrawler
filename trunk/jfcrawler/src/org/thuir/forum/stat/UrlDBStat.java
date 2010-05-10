package org.thuir.forum.stat;

import java.io.File;
import java.io.FileWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.HashMap;
import java.util.TreeSet;
//import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.thuir.jfcrawler.util.ConfigUtil;

public class UrlDBStat {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//		String url = 
		//			ConfigUtil.getConfig().getString("url-database.host");
		//		String username = 
		//			ConfigUtil.getConfig().getString("url-database.user");
		//		String password = 
		//			ConfigUtil.getConfig().getString("url-database.pass");
		//
		//		Class.forName( "org.gjt.mm.mysql.Driver" ); 
		//		Connection conn = DriverManager.getConnection( 
		//				url, username, password );
		//
		//		Statement stmt = conn.createStatement();
		//		
		TreeSet<String> tree = new TreeSet<String>();
		//		
		//		ResultSet rs = stmt.executeQuery("SELECT * FROM urlstatus");
		//		while(rs.next()){
		//			tree.add(rs.getString("url"));
		//		}
		Pattern catalog = Pattern.compile("^index-htm-m-bbs(-cateid-[\\d]+)?\\.html$");
		Pattern board = Pattern.compile("^thread-htm-fid-[\\d]+(-page-[\\d]+)?\\.html$");
		Pattern thread = Pattern.compile("^read-htm-tid-[\\d]+(-fpage-[\\d]+)?(-page-[\\d]+)?\\.html$");

		//		Pattern board_a = Pattern.compile("^thread-htm-fid-([\\d])+\\.html$");
		//		Pattern board_p = Pattern.compile("^thread-htm-fid-([\\d])+-page-([\\d])+?\\.html$");
		//		
		//
		//		Pattern thread_a = Pattern.compile("^read-htm-tid-([\\d])+(-fpage-[\\d]+)?\\.html$");
		//		Pattern thread_p = Pattern.compile("^read-htm-tid-([\\d])+(-fpage-[\\d]+)?-page-([\\d])+\\.html$");

		File file = new File("E:\\±œ“µ…Ëº∆\\www.icefirer.com\\");
		File[] files = file.listFiles();
		for(File f : files) {
			tree.add(f.getName());
		}

		int ncata = 0;
		int nboard = 0;
		int nthread = 0;

		//		HashMap<Integer, Integer> temp1 = new HashMap<Integer, Integer>();
		//		HashMap<Integer, Integer> temp2 = new HashMap<Integer, Integer>();

		int[] temp1 = new int[30000];
		int[] temp2 = new int[30000];

		for(int i = 0; i < 30000; i++) {
			temp1[i] = 0;
			temp2[i] = 0;
		}

		int p = 0, p1 = 0, p2 = 0;
		String temp = null;

		FileWriter writer = new FileWriter("./log/urls.txt");
		for(String str : tree) {
			if(catalog.matcher(str).matches())
				ncata++;
			if(board.matcher(str).matches()) {
				p = str.indexOf("-fid-");
				if(p > 0) {
					temp = str.substring(p + 5);
					p1 = temp.indexOf('.');
					p2 = temp.indexOf('-');
					if(p2 < 0)
						temp = temp.substring(0, p1);
					else
						temp = temp.substring(0, p2);
					temp1[Integer.valueOf(temp)]++;
				}
				nboard++;
			}
			if(thread.matcher(str).matches()) {
				p = str.indexOf("-tid-");
				if(p > 0) {
					temp = str.substring(p + 5);
					p1 = temp.indexOf('.');
					p2 = temp.indexOf('-');
					if(p2 < 0)
						temp = temp.substring(0, p1);
					else
						temp = temp.substring(0, p2);
					temp2[Integer.valueOf(temp)]++;
				}
				nthread++;
			}

			writer.append(str);
			writer.append('\n');
		}


		System.out.println(ncata);
		System.out.println(nboard);
		System.out.println(nthread);
		nboard = 0;
		nthread = 0;
		for(int i = 0; i < 30000; i++) {
			if(temp1[i] != 0)
				nboard++;
			if(temp2[i] != 0)
				nthread++;
		}
		System.out.println(nboard);
		System.out.println(nthread);
		//		stmt.close();
		//		conn.close();
		writer.close();
	}

}
