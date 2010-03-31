package org.thuir.jfcrawler.impl;

import org.thuir.jfcrawler.framework.AbstractJFCrawler;

/**
 * @author ruKyzhc
 *
 */
public class JFCrawler extends AbstractJFCrawler {
	
	public JFCrawler() {
	}
	
	public static void main(String[] args) {
		JFCrawler crawler = new JFCrawler();
		
		crawler.init();
		
		crawler.initializeFrontier(DefaultFrontier.class);
		crawler.initializeExtractor(DefaultExtractor.class);
		crawler.initializeFilter(DefaultFilter.class);
		crawler.initializeScheduler(DefaultScheduler.class);
		
		crawler.initializeCrawlerPool(DefaultCrawler.class, 2);
		
		crawler.addSeed("www.sina.com.cn");
		crawler.addSeed("www.baidu.com");
		
		crawler.start();
	}

}
