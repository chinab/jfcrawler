package org.thuir.jfcrawler.util;

import java.util.TreeMap;
import java.util.Map.Entry;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public final class UrlNormalizer {
	private static final String PROTOCOL_HTTP  = "http";
	private static final String PROTOCOL_HTTPS = "https";
	private static final String PROTOCOL_FTP   = "ftp";

	private static final String PORT_HTTP  = "80";
	private static final String PORT_HTTPS = "443";
	private static final String PORT_FTP   = "21";
	private static final String[] PORT = {
		PORT_HTTP, 
		PORT_HTTPS, 
		PORT_FTP};

	private static final int INDEX_HTTP  = 0;
	private static final int INDEX_HTTPS = 1;
	private static final int INDEX_FTP   = 2;

	private static final String PREFIX     = "://";
	private static final String PORT_SEP   = ":";
	private static final String SEPERATOR  = "/";
	private static final String QUERY      = "?";
	private static final String PARAMETERS = "&";
	private static final String EMAIL      = "@";
	private static final String EQUAL      = "=";

	public static void normalizeUrl(PageUrl pageUrl, String url) 
	throws BadUrlFormatException{
		String protocol = null;
		String host = null;
		String port = null;
		String page = null;

		String[] temp_array = new String[2];
		String key = null;
		String value = null;

		int pointer = 0;
		StringBuffer urlbuf = 
			new StringBuffer(url.trim().replaceAll("\\\\", "/"));

		//Email
		pointer = urlbuf.indexOf(EMAIL);
		if(pointer >= 0) {
			throw new BadUrlFormatException(
					UrlNormalizer.class.getName(), "url : " + url);
		}

		//protocol
		pointer = urlbuf.indexOf(PREFIX);
		if(pointer >= 0) {
			protocol = urlbuf.substring(0, pointer);
			urlbuf.delete(0, pointer + 3);
		}

		int pro = 0;
		if(protocol == null) {
			protocol = PROTOCOL_HTTP;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_HTTP)) {
			pro = INDEX_HTTP;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_HTTPS)) {
			pro = INDEX_HTTPS;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_FTP)) {
			pro = INDEX_FTP;
		} else {
			throw new BadUrlFormatException(
					UrlNormalizer.class.getName(), "url : " + url);
		}

		//host&port
		pointer = urlbuf.indexOf(SEPERATOR);
		if(pointer >= 0) {
			temp_array = urlbuf.substring(0, pointer).split(PORT_SEP);
			host = temp_array[0];
			if(temp_array.length > 1)
				port = temp_array[1];
			else
				port = PORT[pro];

			urlbuf.delete(0, pointer + 1);
		} else if(urlbuf.toString().matches("^([\\w]+\\.)+[\\w]+(:[\\d]+)?$")){
			temp_array = urlbuf.toString().split(PORT_SEP);
			host = temp_array[0];
			if(temp_array.length > 1)
				port = temp_array[1];
			else
				port = PORT[pro];

		} else {
			throw new BadUrlFormatException(
					UrlNormalizer.class.getName(), "url : " + url);
		}

		//page
		pointer = urlbuf.indexOf(QUERY);
		if(pointer >= 0) {
			page = urlbuf.substring(0, pointer);
			urlbuf.delete(0, pointer + 1);
		}

		//query
		TreeMap<String, String> temp_map = new TreeMap<String, String>();
		String[] params = urlbuf.toString().split(PARAMETERS);
		for(String param : params) {
			temp_array = param.split(EQUAL);
			if(temp_array.length >= 2) {
				key = temp_array[0].trim();
				value = temp_array[1].trim();
				temp_map.put(key, value);
			} else {
				continue;
			}
		}

		StringBuffer buf = new StringBuffer();

		pageUrl.setProtocol(protocol);
		buf.append(protocol);
		buf.append(PREFIX);

		pageUrl.setHost(host);
		buf.append(host);

		pageUrl.setPort(port);
		if(pro != INDEX_HTTP || !(port.equals(PORT_HTTP))) {
			buf.append(PORT_SEP);
			buf.append(port);
		}

		buf.append(SEPERATOR);

		if(page == null) {
			pageUrl.setUrl(buf.toString());
			return;
		}

		pageUrl.setPage(page);
		buf.append(page);

		Entry<String, String> iter = null;
		int count = 0;
		while((iter = temp_map.pollFirstEntry()) != null) {
			if(count == 0)
				buf.append(QUERY);
			else
				buf.append(PARAMETERS);

			key = iter.getKey();
			value = iter.getValue();
			pageUrl.addParameter(key, value);
			buf.append(key);
			buf.append(EQUAL);
			buf.append(value);

			count++;
		}
		
		pageUrl.setUrl(buf.toString());
	}

} 