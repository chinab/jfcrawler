package org.thuir.jfcrawler.data;

import org.thuir.jfcrawler.framework.crawler.ICrawler;

/**
 * @author ruKyzhc
 *
 */
public class Page {

	private PageUrl url = null;
	
	private String html = null;
	
	private ICrawler crawler = null;
	
	public Page() {
		
	}
	
	public Page(PageUrl url) {
		this();
		this.url = url;
	}
	
	public void setCrawler(ICrawler crawler) {
		this.crawler = crawler;
	}
	
	public void setPageUrl(PageUrl url) {
		this.url = url;
	}
	
	public PageUrl getPageUrl() {
		return this.url;
	}
	
	public String getHtmlContent() {
		return html;
	}
	
	public void load(String html) {
		this.html = html;
		this.crawler.postFetch(this);
	}
}
