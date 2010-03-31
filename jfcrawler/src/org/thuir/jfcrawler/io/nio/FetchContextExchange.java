package org.thuir.jfcrawler.io.nio;

import java.io.IOException;

import org.eclipse.jetty.client.ContentExchange;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.framework.crawler.ICrawler;

/**
 * @author ruKyzhc
 *
 */
public class FetchContextExchange extends ContentExchange {
	private Page page = null;
	private ICrawler crawler = null;
	
	public FetchContextExchange(Page page) {
		super();
		this.page = page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	public void setCrawler(ICrawler crawler) {
		this.crawler = crawler;
	}
	
	@Override
	protected void onResponseComplete() throws IOException {
		super.onResponseComplete();
		page.load(this.getResponseContent());
		crawler.postFetch(page);
	}
	
}
