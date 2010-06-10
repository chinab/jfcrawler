package org.thuir.jfcrawler.io.httpclient;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class FetchExchange {
	
	private FetchingListener listener = null;
	
	private Page page = null;
	
	private String userAgent = null;
	
	private Exception exception = null;
	
	public FetchExchange(Page page, FetchingListener listener) {
		this.listener = listener;
		this.page = page;
	}
	
	public FetchExchange(Page page, FetchingListener listener, String userAgent) {
		this.listener = listener;
		this.page = page;
		this.userAgent = userAgent;
	}
	
	public void setUserAgent(String agent) {
		this.userAgent = agent;
	}
	
	public String getUserAgent() {
		return this.userAgent;
	}
	
	public void onComplete() {
		listener.onComplete(this);
	}
	
	public void onExpired() {
		listener.onExpired(this);
	}
	
	public void onExcepted() {
		listener.onExcepted(this);
	}
	
	public void onFailed() {
		listener.onFailed(this);
	}
	
	public Page getPage() {
		return page;
	}
	
	public Url getUrl() {
		return page.getUrl();
	}
	
	public void excepted(Exception cause) {
		this.exception = cause;
	}
	
	public Exception getException() {
		return exception;
	}

}
