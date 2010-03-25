package org.thuir.jfcrawler.framework;

import org.thuir.jfcrawler.framework.crawler.AbstractCrawler;
import org.thuir.jfcrawler.framework.extractor.AbstractExtractor;
import org.thuir.jfcrawler.framework.filter.AbstractFilter;
import org.thuir.jfcrawler.framework.frontier.AbstractFrontier;
import org.thuir.jfcrawler.impl.DefaultExtractor;
import org.thuir.jfcrawler.impl.DefaultFilter;
import org.thuir.jfcrawler.impl.DefaultFrontier;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractJFCrawler extends Thread {

	protected AbstractCrawler[] crawlerPool = null;

	protected Class<? extends AbstractFrontier>  frontierClass = 
		DefaultFrontier.class;
	
	protected Class<? extends AbstractFilter>    filterClass   = 
		DefaultFilter.class;
	
	protected Class<? extends AbstractExtractor> extractorClass = 
		DefaultExtractor.class;

	protected int crawlerPoolSize = 0;

	public void initializeFilter(
			Class<? extends AbstractFilter> T) {
		this.filterClass = T;
	}

	public void initializeExtractor(
			Class<? extends AbstractExtractor> T) {
		this.extractorClass = T;
	}
	public void initializeFrontier(
			Class<? extends AbstractFrontier> T) {
		this.frontierClass = T;
	}

	public void initializeCrawlerPool(
			Class<? extends AbstractCrawler> T, int nThread) {
		try {
			crawlerPool = new AbstractCrawler[nThread];
			for(int i = 0; i < nThread; i++) {
				crawlerPool[i] = T.newInstance();
				
				crawlerPool[i].setExtractor(
						extractorClass.newInstance());
				crawlerPool[i].setFilter(
						filterClass.newInstance());
				crawlerPool[i].setFrontier(
						frontierClass.newInstance());
				
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void start() {
	}

}
