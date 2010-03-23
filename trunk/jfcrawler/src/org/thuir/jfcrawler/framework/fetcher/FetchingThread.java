package org.thuir.jfcrawler.framework.fetcher;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.framework.crawler.ICrawler;
import org.thuir.jfcrawler.io.IHttpFetcher;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class FetchingThread extends Thread implements IFetcher {
	private static final Logger logger =
		Logger.getLogger(FetchingThread.class.getName());

	private final static int DEFAULT_INTERVAL = 
		CrawlerConfiguration.getDefaultInterval();;

	private static int threadCount = 0;
	private final int threadId = threadCount++;

	private Boolean isFetching = false;

	private ICrawler crawler = null;
	
	private IHttpFetcher httpFetcher = null;

	@Override
	public void startCrawler(ICrawler crawler) {
		synchronized(isFetching) {
			this.crawler = crawler;
			isFetching.notify();
		}
	}
	
	public void setHttpFetcher(IHttpFetcher httpFetcher) {
		this.httpFetcher = httpFetcher;
	}
	
	public void setCrawler(ICrawler crawler) {
		this.crawler = crawler;
	}

	@Override
	public void run() {
		logger.info("Fetching Thread [" + threadId + "] starts to working.");
		while(true) {
			synchronized(isFetching) {
				try {
					if(crawler != null) {
						isFetching = true;
						crawler.preFetch();
						crawler.fetch(httpFetcher);
						
						crawler = null;
						isFetching.wait(DEFAULT_INTERVAL);
						isFetching = false;
					} else {
						isFetching = false;
						Thread.sleep(DEFAULT_INTERVAL);

					}
				} catch (InterruptedException e) {
				}
			}
		}
	}

	@Override
	public boolean isFetching() {
		synchronized(isFetching) {
			return isFetching;
		}
	}

}
