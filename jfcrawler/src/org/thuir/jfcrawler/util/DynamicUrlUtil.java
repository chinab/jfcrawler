package org.thuir.jfcrawler.util;

import org.thuir.jfcrawler.data.Url;

public class DynamicUrlUtil {
	private static final String defaultPage = "#";
	
	public static String danymicUrlToString(Url url) {
		if(url.getPage().equals(""))
			return defaultPage;
		String temp = url.getPage();
		int pointer = temp.indexOf('.');
		
		String uri = null;
		String suffix = null;
		if(pointer < 0) {
			uri = temp;
		} else {
			uri = temp.substring(0, pointer);
			suffix = temp.substring(pointer);
		}
		
		StringBuffer buf = new StringBuffer();
		buf.append(uri);
		for(Url.UrlParameter p : url.getParameters()) {
			buf.append('_');
			buf.append(p.toString());
		}
		if(suffix != null) {
			buf.append(suffix);
		}
		return buf.toString();
	}
	
	public static Url parseStringToDanymicUri(String str) {
		return null;
	}
	
	public static String generatePath(Url url) {
		return
			url.getHost() + "\\" 
			+ url.getPath().replaceAll("/", "\\\\") + "\\" 
			+ danymicUrlToString(url);
	}
}
