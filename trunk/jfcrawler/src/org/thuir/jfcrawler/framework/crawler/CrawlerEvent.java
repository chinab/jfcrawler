package org.thuir.jfcrawler.framework.crawler;

import org.thuir.jfcrawler.framework.fetcher.IFetcher;

/**
 * @author ruKyzhc
 *
 */
public class CrawlerEvent {
	private IFetcher fetcher;
	
	public CrawlerEvent(IFetcher fetcher) {
		this.fetcher = fetcher;
	}
	
	public IFetcher getFetcher() {
		return fetcher;		
	}
	
}
