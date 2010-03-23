package org.thuir.jfcrawler.framework.fetcher;

import org.thuir.jfcrawler.framework.crawler.ICrawler;

/**
 * @author ruKyzhc
 *
 */
public interface IFetcher {
	
	public boolean isFetching();

	public void startCrawler(ICrawler crawler);

}
