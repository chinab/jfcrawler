package org.thuir.jfcrawler.framework.fetcher;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.framework.crawler.ICrawler;

/**
 * @author ruKyzhc
 *
 */
public class FetchingThread extends Thread {
	private static final Logger logger =
		Logger.getLogger(FetchingThread.class.getName());
	
	private final static int CHECKING_INTERVAL = 10000;
	
	private static int threadCount = 0;
	private final int threadId = threadCount++;

	private static IFetcher fetcher = null;
	static {
		fetcher = new NonBlockingFetcher();
	}
	
	private ICrawler crawler = null;
	
	public void setCrawler(ICrawler crawler) {
		this.crawler = crawler;
	}

	@Override
	public void run() {
		logger.info("Fetching Thread [" + threadId + "] starts to working.");
		while(true) {
			if(crawler == null) {
				try {
					Thread.sleep(CHECKING_INTERVAL);
					continue;
				} catch (InterruptedException e) {
				}
			}
			crawler.preFetch();
			crawler.fetch(fetcher);
		}
	}
	
}
