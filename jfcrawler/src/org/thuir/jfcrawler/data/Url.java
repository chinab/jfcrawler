package org.thuir.jfcrawler.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * basic data structure of page urls
 * 
 * @author ruKyzhc
 *
 */
public class Url implements Serializable, Comparable<Url> {
	private static final Logger logger = 
		Logger.getLogger(Url.class);

	private static final long serialVersionUID = -207345866948400297L;
	private static final long DEFAULT_REVISIT = 
		ConfigUtil.getConfig().getLong("crawler.revisit-interval");

	protected long revisitInterval = DEFAULT_REVISIT;

	protected String url;
	
	protected String uri;

	protected String protocol;

	protected String host;

	protected String port;

	protected String page;

	protected String path;

	protected HashMap<String, String> params;

	private static Class<? extends Url> urlClass = Url.class;

	@Override
	public int compareTo(Url other) {
		return url.compareTo(other.url);
	}
	
	protected void clone(Url url) {
		this.host = url.host;
		this.url = url.url;
		this.uri = url.uri;
		this.protocol = url.protocol;
		this.host = url.host;
		this.port = url.port;
		this.page = url.page;
		this.path = url.path;

		this.status = url.status;
		this.code  = url.code;
		this.visit = url.visit;
		this.revisitInterval = url.revisitInterval;
		
		this.params.putAll(url.params);
	}

	protected Url() {
		params = new HashMap<String, String>();
	}

	public static void setUrlClass(Class<? extends Url> c) {
		urlClass= c;
	}

	public static Url parseWithParent(Url parent, String url) 
	throws BadUrlFormatException {
		if(urlClass == null) 
			return null;

		Url inst = null;
		try {
			inst = urlClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		normalizeUrl(inst, parent, url);
		return inst;
	}

	public static Url parse(String url) 
	throws BadUrlFormatException {
		if(urlClass == null) 
			return null;

		Url inst = null;
		try {
			inst = urlClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		normalizeUrl(inst, url);
		return inst;
	}

	public String getUrl() {
		return url;
	}
	protected void setUrl(String url) {
		this.url = url;
	}
	public String getUri() {
		return uri;
	}
	protected void setUri(String uri) {
		this.uri = uri;
	}
	public String getHost() {
		return host;
	}
	protected void setHost(String host) {
		this.host = host;
	}
	public String getProtocol() {
		return protocol;
	}
	protected void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getPage() {
		return page;
	}
	protected void setPage(String page) {
		this.page = page;
	}
	public String getPort() {
		return port;
	}
	protected void setPort(String port) {
		this.port = port;
	}
	public HashMap<String, String> getParameters() {
		return params;
	}
	public String getParameter(String key) {
		return params.get(key);
	}
	protected void addParameter(String key, String value) {
		this.params.put(key, value);
	}
	protected void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public void setRevisitInterval(long interval) {
		this.revisitInterval = interval;
	}	
	public long getRevisitInterval() {
		return revisitInterval;
	}
	
	@Override
	public String toString() {
		return url;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Url) {
			return this.url.equals(((Url)obj).url);
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

	public static void normalizeUrl(Url target, Url parent, String url) 
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
					Url.class.getName(), "url : " + url);
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
					Url.class.getName(), "url : " + url);
		}

		//host&port
		pointer = urlbuf.indexOf(SEPERATOR);
		if(pointer == 0) {
			if(parent == null) {
				logger.error("bad url:" + url);
				throw new BadUrlFormatException(
						Url.class.getName(), "url : " + url);
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
							Url.class.getName(), "url : " + url);
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
			urlbuf.delete(0, urlbuf.length());
		}

		//query
		TreeMap<String, String> temp_map = new TreeMap<String, String>();
		String[] params = urlbuf.toString().split(PARAMETERS);
		for(String param : params) {
			if(param.indexOf(EQUAL) >= 0) {
				temp_array = param.split(EQUAL);
				if(temp_array.length >= 2) {
					key = temp_array[0].trim();
					value = temp_array[1].trim();
					temp_map.put(key, value);
				} else if(temp_array.length == 1){
					key = temp_array[0];
					temp_map.put(key, null);
				}
			} else if(param.length() > 0){
				key = param;
				temp_map.put(key, null);
			} else {
				continue;
			}
		}

		StringBuffer buf = new StringBuffer();
		StringBuffer buf_uri = new StringBuffer();

		target.setProtocol(protocol);
		buf.append(protocol);
		buf.append(PREFIX);

		target.setHost(host);
		buf.append(host);

		target.setPort(port);
		if(pro != INDEX_HTTP || !(port.equals(PORT_HTTP))) {
			buf.append(PORT_SEP);
			buf.append(port);
		}

		buf.append(SEPERATOR);

		target.setPath(path);
		buf_uri.append(path);

		if(page == null) {
			target.setPage("");
			target.setUri(buf_uri.toString());
			
			buf.append(buf_uri);
			target.setUrl(buf.toString());
			return;
		}

		target.setPage(page);
		buf_uri.append(page);

		Entry<String, String> iter = null;
		int count = 0;
		while((iter = temp_map.pollFirstEntry()) != null) {
			if(count == 0)
				buf_uri.append(QUERY);
			else
				buf_uri.append(PARAMETERS);


			key = iter.getKey();
			value = iter.getValue();
			target.addParameter(key, value);
			if(value == null) {
				buf_uri.append(key);
			} else {
				buf_uri.append(key);
				buf_uri.append(EQUAL);
				buf_uri.append(value);
			}

			count++;
		}
		
		target.setUri(buf_uri.toString());
		
		buf.append(buf_uri);
		target.setUrl(buf.toString());
	}

	public static void normalizeUrl(Url target, String url) 
	throws BadUrlFormatException{
		if(url.startsWith("http://"))
			normalizeUrl(target, null, url);
		else
			normalizeUrl(target, null, "http://" + url);
	}
	
	
	/////////////////////////////
	public static final int STATUS_INITIAL  = 0;
	public static final int STATUS_COMPLETE = 1;
	public static final int STATUS_FAILED   = 2;
	public static final int STATUS_EXCEPTED = 3;
	public static final int STATUS_EXPIRED  = 4;
	
	private int status = 0;
	private int code   = 0;
	private long  visit  = 0l;

	public void setStatus(int status) {
		this.status = status;
	}
	public int getStatus() {
		return this.status;
	}

	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
	
	public void setVisit(long visit) {
		this.visit = visit;
	}
	public long getVisit() {
		return this.visit;
	}
}