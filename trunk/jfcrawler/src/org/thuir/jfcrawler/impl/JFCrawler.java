package org.thuir.jfcrawler.impl;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.framework.AbstractJFCrawler;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.framework.filter.HostFilter;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.framework.processor.DefaultCrawler;

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
		
		crawler.initialize();
		crawler.initializeFetcher(DefaultFetcher.class);
		crawler.initializeCrawler(DefaultCrawler.class, 2);
		
		HostFilter f = new HostFilter();
		f.setHost("www.hiapk.com");
		crawler.addFilter(f);
		
		crawler.addExtractor(new HTMLExtractor());
		
		crawler.addSeed("http://www.hiapk.com/bbs/");
		
		crawler.start();
	}

}
