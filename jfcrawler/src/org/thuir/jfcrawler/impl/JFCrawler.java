package org.thuir.jfcrawler.impl;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.framework.AbstractJFCrawler;
import org.thuir.jfcrawler.framework.cache.BlockingQueueCache;
import org.thuir.jfcrawler.framework.frontier.BlockingQueueFrontier;
import org.thuir.jfcrawler.framework.handler.DefaultUrlHandler;
import org.thuir.jfcrawler.framework.handler.HTMLPageHandler;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.framework.processor.DefaultPreprocessor;

/**
 * @author ruKyzhc
 *
 */
public class JFCrawler extends AbstractJFCrawler {
	
	public JFCrawler(String jobName) {
		super(jobName);
	}
	
	public static void main(String[] args) throws BadUrlFormatException {
		JFCrawler crawler = new JFCrawler("job");
		
		crawler.initializeFrontier(BlockingQueueFrontier.class);
		crawler.initializeCache(BlockingQueueCache.class);
		
		crawler.initalizeFetcher(DefaultFetcher.class);
		crawler.initializeUrlHandlerClass(DefaultUrlHandler.class);
		crawler.initializePageHandlerClass(HTMLPageHandler.class);

		crawler.initializeModules();
		crawler.initializeProcessor(DefaultPreprocessor.class, 10);
		
		crawler.addSeed("http://www.discuz.net/");
		
		crawler.start();
	}

}
