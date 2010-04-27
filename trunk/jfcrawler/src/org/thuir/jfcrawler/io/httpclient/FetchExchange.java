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
	
	public FetchExchange(Page page, FetchingListener listener) {
		this.listener = listener;
		this.page = page;
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

}
