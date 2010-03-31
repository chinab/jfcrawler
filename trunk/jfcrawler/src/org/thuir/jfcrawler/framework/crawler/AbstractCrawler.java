package org.thuir.jfcrawler.framework.crawler;

import org.thuir.jfcrawler.framework.extractor.AbstractExtractor;
import org.thuir.jfcrawler.framework.filter.AbstractFilter;
import org.thuir.jfcrawler.framework.frontier.AbstractFrontier;
import org.thuir.jfcrawler.framework.scheduler.AbstractScheduler;
import org.thuir.jfcrawler.framework.writer.AbstractWriter;
import org.thuir.jfcrawler.io.IHttpFetcher;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractCrawler 
		extends Thread implements ICrawler {
	
	protected CrawlerEventListener listener = null;
	
	protected IHttpFetcher httpFetcher = null;
	
	protected AbstractExtractor extractor = null;
	
//	protected AbstractFrontier  frontier  = null;
	
	protected AbstractScheduler scheduler = null;
	
	protected AbstractFilter    filter    = null;
	
	protected AbstractWriter    writer    = null;
	
	protected Boolean isCrawling  = false;

	public void setHttpFetcher(IHttpFetcher httpFetcher) {
		this.httpFetcher = httpFetcher;
	}
	
	public void setExtractor(AbstractExtractor extractor) {
		this.extractor = extractor;
	}
	
	public void setFilter(AbstractFilter filter) {
		this.filter = filter;
	}
	
	public void setFrontierToScheduler(AbstractFrontier frontier) {
		this.scheduler.setFrontier(frontier);
	}
	
	public void setScheduler(AbstractScheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	public void setWriter(AbstractWriter writer) {
		this.writer = writer;
	}
	
	public void addCrawlerEventListener(CrawlerEventListener listener) {
		this.listener = listener;
	}

	public boolean isCrawling() {
		return isCrawling;
	}
	
	@Override
	public void run() {
		while(true) {
			preFetch();
			doFetch();
		}
	}

}
