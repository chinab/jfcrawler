package org.thuir.jfcrawler.framework.fetcher;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.framework.crawler.ICrawler;
import org.thuir.jfcrawler.io.IHttpFetcher;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;

/**
 * @author ruKyzhc
 *
 */
public class FetchingThread extends Thread implements IFetcher {
	private static final Logger logger =
		Logger.getLogger(FetchingThread.class.getName());

	private final static int DEFAULT_INTERVAL = 10000;

	private static int threadCount = 0;
	private final int threadId = threadCount++;

	private Boolean isFetching = false;

	private static IHttpFetcher fetcher = null;
	static {
		fetcher = new NonBlockingFetcher();
	}

	private ICrawler crawler = null;

	@Override
	public void startCrawler(ICrawler crawler) {
		this.crawler = crawler;
	}

	@Override
	public void run() {
		logger.info("Fetching Thread [" + threadId + "] starts to working.");
		while(true) {
			synchronized(isFetching) {
				if(crawler != null) {
					isFetching = true;
					crawler.preFetch();
					crawler.fetch(fetcher);
				} else {
					try {
						isFetching = false;
						Thread.sleep(DEFAULT_INTERVAL);
					} catch (InterruptedException e) {
					}
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
