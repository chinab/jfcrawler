package org.thuir.jfcrawler.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * basic data structure of page urls
 * 
 * @author ruKyzhc
 *
 */
public class PageUrl implements Serializable {
	private static final Logger logger = 
		Logger.getLogger(PageUrl.class);

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -207345866948400297L;

	protected String url;

	protected String protocol;

	protected String host;

	protected String port;

	protected String page;

	protected String path;

	protected ArrayList<UrlParameter> params;
	
	//statistics
	
	protected long lastVisit = 0;

	public PageUrl() {
		params = new ArrayList<UrlParameter>();
	}

	public PageUrl(PageUrl parent, String url) throws BadUrlFormatException {
		this();

		this.url = null;
		this.protocol = null;
		this.host = null;
		this.port = null;
		this.page = null;
		this.path = null;
		this.params.clear();

		normalizeUrl(this, parent, url);
	}

	public PageUrl(String url) throws BadUrlFormatException {
		this();

		this.url = null;
		this.protocol = null;
		this.host = null;
		this.port = null;
		this.page = null;
		this.path = null;
		this.params.clear();

		normalizeUrl(this, url);
	}

	public static PageUrl parse(PageUrl parent, String url) 
	throws BadUrlFormatException {
		PageUrl pageUrl = new PageUrl(parent, url);
		return pageUrl;
	}
	
	public static PageUrl parse(String url) 
	throws BadUrlFormatException {
		PageUrl pageUrl = new PageUrl(url);
		return pageUrl;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	protected void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	protected void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	protected void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	protected void setPage(String page) {
		this.page = page;
	}


	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	protected void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the params
	 */
	public ArrayList<UrlParameter> getParameters() {
		return params;
	}

	/**
	 * @param add <key, value> to parameters list.
	 */
	protected void addParameter(String key, String value) {
		this.params.add(new UrlParameter(key, value));
	}

	/**
	 * @param path the path to set
	 */
	protected void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PageUrl) {
			return this.url.equals(((PageUrl)obj).url);
		} else {
			return false;
		}
	}

	//normalization
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
	private static final String BOOKMARK   = "#";

	public static void normalizeUrl(PageUrl pageUrl, PageUrl parent, String url) 
	throws BadUrlFormatException{	
		boolean isPrefix = true;

		String protocol = null;
		String host = null;
		String port = null;
		String page = null;
		String path = null;

		String[] temp_array = new String[2];
		String temp_host = null;
		String key = null;
		String value = null;

		int pointer = 0;
		
		//Email
		pointer = url.indexOf(EMAIL);
		if(pointer >= 0) {
			logger.error("bad url:" + url);
			throw new BadUrlFormatException(
					PageUrl.class.getName(), "url : " + url);
		}

		//bookmark
		pointer = url.indexOf(BOOKMARK);
		if(pointer >= 0)  {
			url = url.substring(0, pointer);
		}
		
		StringBuffer urlbuf = 
			new StringBuffer(url.trim().replaceAll("\\\\", "/"));
		
		//protocol
		pointer = urlbuf.indexOf(PREFIX);
		if(pointer >= 0) {
			protocol = urlbuf.substring(0, pointer);
			urlbuf.delete(0, pointer + 3);
		}

		int pro = 0;
		if(protocol == null) {
			isPrefix = false;
			protocol = PROTOCOL_HTTP;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_HTTP)) {
			pro = INDEX_HTTP;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_HTTPS)) {
			pro = INDEX_HTTPS;
		} else if(protocol.equalsIgnoreCase(PROTOCOL_FTP)) {
			pro = INDEX_FTP;
		} else {
			logger.error("bad url:" + url);
			throw new BadUrlFormatException(
					PageUrl.class.getName(), "url : " + url);
		}

		//host&port
		pointer = urlbuf.indexOf(SEPERATOR);
		if(pointer == 0) {
			if(parent == null) {
				logger.error("bad url:" + url);
				throw new BadUrlFormatException(
						PageUrl.class.getName(), "url : " + url);
			} else {
				host = parent.getHost();
				port = parent.getPort();

				urlbuf.delete(0, 1);
			}
		} else if(pointer > 0) {
			temp_host = urlbuf.substring(0, pointer);
			if(temp_host.indexOf('.') == -1) {
				host = parent.getHost();
				port = parent.getPort();
			} else {
				temp_array = temp_host.split(PORT_SEP);
				host = temp_array[0];
				if(temp_array.length > 1)
					port = temp_array[1];
				else
					port = PORT[pro];

				urlbuf.delete(0, pointer + 1);
			}
		} else {
			if(isPrefix) {
				temp_array = urlbuf.toString().split(PORT_SEP);
				host = temp_array[0];
				if(temp_array.length > 1)
					port = temp_array[1];
				else
					port = PORT[pro];

				urlbuf.delete(0, urlbuf.length());
			} else {
				if(parent == null) {
					logger.error("bad url:" + url);
					throw new BadUrlFormatException(
							PageUrl.class.getName(), "url : " + url);
				} else {
					host = parent.getHost();
					port = parent.getPort();
					urlbuf.insert(0, parent.getPath());
				}
			}			
		}

		//path
		pointer = urlbuf.lastIndexOf(SEPERATOR);
		if(pointer >= 0) {
			path = urlbuf.substring(0, pointer + 1);
			urlbuf.delete(0, pointer + 1);
		} else {
			path = "";
		}

		//page
		pointer = urlbuf.indexOf(QUERY);
		if(pointer >= 0) {
			page = urlbuf.substring(0, pointer);
			urlbuf.delete(0, pointer + 1);
		} else if(urlbuf.length() > 0){
			if(urlbuf.indexOf(".") > 0) {
				page = urlbuf.toString();
			} else if (urlbuf.indexOf("#") >= 0){
				page = urlbuf.toString();
			} else {
				path = path + urlbuf.toString() + "/";
			}
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

		pageUrl.setPath(path);
		buf.append(path);

		if(page == null) {
			pageUrl.setPage("#");
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
	
	public static void normalizeUrl(PageUrl pageUrl, String url) 
	throws BadUrlFormatException{

		String protocol = null;
		String host = null;
		String port = null;
		String page = null;
		String path = null;

		String[] temp_array = new String[2];
		String temp_host = null;
		String key = null;
		String value = null;

		int pointer = 0;
		
		//Email
		pointer = url.indexOf(EMAIL);
		if(pointer >= 0) {
			logger.error("bad url:" + url);
			throw new BadUrlFormatException(
					PageUrl.class.getName(), "url : " + url);
		}

		//bookmark
		pointer = url.indexOf(BOOKMARK);
		if(pointer >= 0)  {
			url = url.substring(0, pointer);
		}
		
		StringBuffer urlbuf = 
			new StringBuffer(url.trim().replaceAll("\\\\", "/"));

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
			logger.error("bad url:" + url);
			throw new BadUrlFormatException(
					PageUrl.class.getName(), "url : " + url);
		}

		//host&port
		pointer = urlbuf.indexOf(SEPERATOR);
		if(pointer == 0) {
			logger.error("bad url:" + url);
			throw new BadUrlFormatException(
					PageUrl.class.getName(), "url : " + url);
		} else if(pointer > 0) {
			temp_host = urlbuf.substring(0, pointer);

			temp_array = temp_host.split(PORT_SEP);
			host = temp_array[0];
			if(temp_array.length > 1)
				port = temp_array[1];
			else
				port = PORT[pro];

			urlbuf.delete(0, pointer + 1);

		} else {
			temp_array = urlbuf.toString().split(PORT_SEP);
			host = temp_array[0];
			if(temp_array.length > 1)
				port = temp_array[1];
			else
				port = PORT[pro];

			urlbuf.delete(0, urlbuf.length());			
		}

		//path
		pointer = urlbuf.lastIndexOf(SEPERATOR);
		if(pointer >= 0) {
			path = urlbuf.substring(0, pointer + 1);
			urlbuf.delete(0, pointer + 1);
		} else {
			path = "";
		}

		//page
		pointer = urlbuf.indexOf(QUERY);
		if(pointer >= 0) {
			page = urlbuf.substring(0, pointer);
			urlbuf.delete(0, pointer + 1);
		} else if(urlbuf.length() > 0){
			if(urlbuf.indexOf(".") > 0) {
				page = urlbuf.toString();
			} else if (urlbuf.indexOf("#") >= 0){
				page = urlbuf.toString();
			} else {
				path = path + urlbuf.toString() + "/";
			}
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

		pageUrl.setPath(path);
		buf.append(path);

		if(page == null) {
			pageUrl.setPage("#");
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
	
	/**
	 * @author ruKyzhc
	 *
	 */
	public static class UrlParameter implements Serializable {
		
		/**
		 * generated serialVersionUID
		 */
		private static final long serialVersionUID = -664061118486665562L;

		private String key;
		
		private String value;
		
		public UrlParameter() {
			this.key = "";
			this.value = "";
		}
		
		public UrlParameter(String key, String value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return key + '=' + value;
		}
		
		public static UrlParameter parse(String str) {
			int pointer = str.indexOf('=');
			if(pointer < 0)
				return null;
			UrlParameter p = new UrlParameter();
			p.setKey(str.substring(0, pointer));
			p.setValue(str.substring(pointer + 1));
			return p;
		}
		
	}
}