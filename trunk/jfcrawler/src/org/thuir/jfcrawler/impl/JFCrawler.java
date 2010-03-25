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
		
		crawler.initializeFrontier(DefaultFrontier.class);
		crawler.initializeExtractor(DefaultExtractor.class);
		
		crawler.initializeCrawlerPool(DefaultCrawler.class, 5);
		crawler.start();
	}

}
