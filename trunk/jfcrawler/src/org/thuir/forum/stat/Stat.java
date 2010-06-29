package org.thuir.forum.stat;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author ruKyzhc
 *
 */
public class Stat {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File path = new File(args[0]);
		File[] list = path.listFiles();
		String name = null;
		String keys[] = null;

		String t = null;
		int p = 0;
		String key = null;
		String value = null;

		String id = null;
		String board = null;
		String page = null;
		String bid = null;
		HashMap<String, String> temp = new HashMap<String, String>();
		
		HashSet<String> aset = new HashSet<String>(300000);
		HashSet<String> pset = new HashSet<String>(2000);
		HashSet<String> bset = new HashSet<String>(50000);
		for(File f : list) {
			if(!f.isFile()) {
				continue;
			}
			keys = f.getName().split("_");

			name = keys[0];
			for(int i = 1; i < keys.length; i++) {
				t = keys[i];
				p = t.indexOf('=');
				key = keys[i].substring(0, p).toLowerCase();
				value = keys[i].substring(p + 1).toLowerCase();
				temp.put(key, value);
			}
			String token = null;
			if(name.contains("bbscon")) {
				id = temp.get("id");
				bid = temp.get("bid");
				token = bid + "_" + id;
				aset.add(token);
				continue;
			}
			
			if(name.contains("bbsdoc")) {
				board = temp.get("board");
				page = temp.get("page");
				if(page == null) {
					page = "-1";
				}
				token = board + "_" + page;
				pset.add(board);
				bset.add(token);
				continue;
			}
			temp.clear();
		}
		
		System.out.println("board count  " + pset.size());
		System.out.println("article count  " + aset.size());
		System.out.println("boart page count  " + bset.size());
		System.out.println(list.length);
	}
}

