package org.thuir.jfcrawler.framework.crawler;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class CrawlerManager {
	private static final Logger logger =
		Logger.getLogger(CrawlerManager.class);
	
	private static final int maxSize = 
		CrawlerConfiguration.getMaxCrawlingUnit();
	private int curSize = 0;
	
	private Queue<ICrawler> pendingCrawlerQueue;
	
	private Queue<ICrawler> blockingCrawlerQueue;
	
	public CrawlerManager() {
		curSize = 0;

		pendingCrawlerQueue  = new ArrayBlockingQueue<ICrawler>(maxSize);
		blockingCrawlerQueue = new ArrayBlockingQueue<ICrawler>(maxSize);
	}
	
	public boolean addCrawler(ICrawler crawler) {
		if( curSize++ < maxSize ) {
			logger.error("crawler queue is full");
			return false;
		}
		if(!pendingCrawlerQueue.offer(crawler)) {
			logger.error("error when add crawler to pending queue");
			return false;
		}
		return true;
	}
	
	public synchronized ICrawler getPendingCrawler() {
		ICrawler crawler = pendingCrawlerQueue.poll();
		if(crawler != null) {
			if(!blockingCrawlerQueue.offer(crawler)) {
				logger.error("error when add crawler to blocking queue");
			}
		}
		ICrawler nextCrawler = blockingCrawlerQueue.poll();
		if(!nextCrawler.isCrawling()) {
			if(!pendingCrawlerQueue.offer(nextCrawler)) {
				logger.error("error when add next crawler to pending queue");
			}
		}
		return crawler;
	}

}
