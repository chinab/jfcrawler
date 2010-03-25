package org.thuir.jfcrawler.framework.crawler;

/**
 * @author ruKyzhc
 *
 */
public class CrawlerEvent {
	private AbstractCrawler crawler;
	
	public CrawlerEvent(AbstractCrawler crawler) {
		this.crawler = crawler;
	}
	
	public ICrawler getCrawler() {
		return crawler;		
	}
	
}
