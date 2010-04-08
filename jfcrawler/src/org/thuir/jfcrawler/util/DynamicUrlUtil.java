package org.thuir.jfcrawler.util;

import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.data.UrlParameter;

public class DynamicUrlUtil {
	private static final String defaultPage = "#";
	
	public static String danymicUrlToString(PageUrl url) {
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
		for(UrlParameter p : url.getParams()) {
			buf.append('_');
			buf.append(p.toString());
		}
		if(suffix != null) {
			buf.append(suffix);
		}
		return buf.toString();
	}
	
	public static PageUrl parseStringToDanymicUri(String str) {
		return null;
	}
	
	public static String generatePath(PageUrl url) {
		return
			url.getHost() + "\\" 
			+ url.getPath().replaceAll("/", "\\\\") + "\\" 
			+ danymicUrlToString(url);
	}
}
