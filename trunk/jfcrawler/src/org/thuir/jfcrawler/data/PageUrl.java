package org.thuir.jfcrawler.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.thuir.jfcrawler.util.UrlNormalizer;

/**
 * basic data structure of page urls
 * 
 * @author ruKyzhc
 *
 */
public class PageUrl implements Serializable {

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -207345866948400297L;

	private String url;

	private String protocol;

	private String host;

	private String port;

	private String page;

	private ArrayList<UrlParameter> params;

	private int    docId;
	
	public PageUrl() {
		params = new ArrayList<UrlParameter>();
	}
	
	public PageUrl(String url) throws BadUrlFormatException {
		this();
		this.generateNormalizedUrl(url);
	}
	
	public static PageUrl parse(String url) throws BadUrlFormatException {
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
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the docId
	 */
	public int getDocId() {
		return docId;
	}

	/**
	 * @param docId the docId to set
	 */
	public void setDocId(int docId) {
		this.docId = docId;
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
	public void setHost(String host) {
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
	public void setProtocol(String protocol) {
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
	public void setPage(String page) {
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
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return the params
	 */
	public ArrayList<UrlParameter> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(ArrayList<UrlParameter> params) {
		this.params = params;
	}

	/**
	 * @return get all paremeters
	 */
	public Object[] getParameters() {
		return params.toArray();
	}

	/**
	 * @param add <key, value> to parameters list.
	 */
	public void addParameter(String key, String value) {
		this.params.add(new UrlParameter(key, value));
	}

	/**
	 * 
	 * @param url
	 * @throws BadUrlFormatException
	 */
	public void generateNormalizedUrl(String url) throws BadUrlFormatException {
		this.url = null;
		this.protocol = null;
		this.host = null;
		this.port = null;
		this.page = null;
		this.params.clear();
		UrlNormalizer.normalizeUrl(this, url);
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
}
