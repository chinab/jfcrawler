package org.thuir.jfcrawler.framework.fetcher;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.framework.crawler.CrawlerEvent;
import org.thuir.jfcrawler.framework.crawler.CrawlerEventListener;
import org.thuir.jfcrawler.framework.crawler.CrawlerManager;
import org.thuir.jfcrawler.io.IHttpFetcher;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class FetchingThreadManager extends Thread 
		implements CrawlerEventListener{
	private static final Logger logger = 
		Logger.getLogger(FetchingThreadManager.class);
	
	private static final int DEFAULT_INTERVAL = 
		CrawlerConfiguration.getDefaultInterval();
	
	private ArrayList<FetchingThread> threadList = null;

	private CrawlerManager manager = null;

	private static IHttpFetcher httpFetcher = null;
	static {
		httpFetcher = new NonBlockingFetcher();
	}

	public FetchingThreadManager(int nThreads) {
		threadList = new ArrayList<FetchingThread>(nThreads);
	}

	public void setCrawlerManager(CrawlerManager manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		logger.info("Fetching threads start");
		for(FetchingThread fetcher : threadList) {
			fetcher.setHttpFetcher(httpFetcher);
			fetcher.setCrawler(manager.getPendingCrawler());
			fetcher.start();
		}
		while(true) {
			try {
				Thread.sleep(DEFAULT_INTERVAL);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onCrawlingFinish(CrawlerEvent event) {
		event.getFetcher().startCrawler(manager.getPendingCrawler());
	}
}
