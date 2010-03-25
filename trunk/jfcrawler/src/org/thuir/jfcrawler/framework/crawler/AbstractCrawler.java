package org.thuir.jfcrawler.framework.crawler;

import org.thuir.jfcrawler.framework.extractor.AbstractExtractor;
import org.thuir.jfcrawler.framework.filter.AbstractFilter;
import org.thuir.jfcrawler.framework.frontier.AbstractFrontier;
//import org.thuir.jfcrawler.framework.scheduler.AbstractScheduler;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractCrawler implements ICrawler {
	protected CrawlerEventListener listener = null;
	
	protected AbstractExtractor extractor = null;
	
	protected AbstractFrontier  frontier  = null;
	
//	protected AbstractScheduler scheduler = null;
	
	protected AbstractFilter    filter    = null;
	
	protected Boolean isCrawling  = false;

	public void setExtractor(AbstractExtractor extractor) {
		this.extractor = extractor;
	}
	
	public void setFilter(AbstractFilter filter) {
		this.filter = filter;
	}
	
	public void setFrontier(AbstractFrontier frontier) {
		this.frontier = frontier;
	}
	
	public void addCrawlerEventListener(CrawlerEventListener listener) {
		this.listener = listener;
	}

	public boolean isCrawling() {
		return isCrawling;
	}

}
