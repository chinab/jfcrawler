package org.thuir.jfcrawler.framework;

import java.util.ArrayList;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.crawler.AbstractCrawler;
import org.thuir.jfcrawler.framework.extractor.AbstractExtractor;
import org.thuir.jfcrawler.framework.filter.AbstractFilter;
import org.thuir.jfcrawler.framework.frontier.AbstractFrontier;
import org.thuir.jfcrawler.framework.scheduler.AbstractScheduler;
import org.thuir.jfcrawler.framework.writer.AbstractWriter;
import org.thuir.jfcrawler.impl.DefaultExtractor;
import org.thuir.jfcrawler.impl.DefaultFileWriter;
import org.thuir.jfcrawler.impl.DefaultFilter;
import org.thuir.jfcrawler.impl.DefaultFrontier;
import org.thuir.jfcrawler.impl.DefaultScheduler;
import org.thuir.jfcrawler.io.IHttpFetcher;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractJFCrawler extends Thread {

	protected AbstractCrawler[] crawlerPool = null;
	protected int crawlerPoolSize = 0;
	
	protected IHttpFetcher fetcher = null;
	
	private ArrayList<PageUrl> seeds = new ArrayList<PageUrl>();

	protected Class<? extends AbstractFrontier>  frontierClass = 
		DefaultFrontier.class;
	
	protected Class<? extends AbstractFilter>    filterClass   = 
		DefaultFilter.class;
	
	protected Class<? extends AbstractExtractor> extractorClass = 
		DefaultExtractor.class;
	
	protected Class<? extends AbstractScheduler> schedulerClass = 
		DefaultScheduler.class;
	
	protected Class<? extends AbstractWriter> writerClass =
		DefaultFileWriter.class;

	public void init() {
		fetcher = new NonBlockingFetcher();
	}
	
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
	public void initializeScheduler(
			Class<? extends AbstractScheduler> T) {
		this.schedulerClass = T;
	}
	public void initializeWriter(
			Class<? extends AbstractWriter> T) {
		this.writerClass = T;
	}

	public void initializeCrawlerPool(
			Class<? extends AbstractCrawler> T, int nThread) {
		crawlerPoolSize = nThread;
		try {
			crawlerPool = new AbstractCrawler[nThread];
			for(int i = 0; i < nThread; i++) {
				crawlerPool[i] = T.newInstance();
				
				crawlerPool[i].setHttpFetcher(fetcher);
				
				crawlerPool[i].setExtractor(
						extractorClass.newInstance());
				crawlerPool[i].setFilter(
						filterClass.newInstance());
				crawlerPool[i].setScheduler(
						schedulerClass.newInstance());
				crawlerPool[i].setFrontierToScheduler(
						frontierClass.newInstance());
				crawlerPool[i].setWriter(
						writerClass.newInstance());
				
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public void addSeed(String url) {
		try {
			seeds.add(new PageUrl(url));
		} catch (BadUrlFormatException e) {
		}
	}

	@Override
	public void start() {
		for(int j = 0; j < seeds.size(); j++) {
			crawlerPool[j%crawlerPoolSize].addSeed(seeds.get(j));
		}
		for(int i = 0; i < crawlerPoolSize; i++) {
			crawlerPool[i].start();
		}
	}

}
