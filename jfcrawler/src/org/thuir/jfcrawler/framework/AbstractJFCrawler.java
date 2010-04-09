package org.thuir.jfcrawler.framework;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.cache.BlockingQueueCache;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.HostFilter;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.BlockingQueueFrontier;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.framework.processor.Fetcher;
import org.thuir.jfcrawler.framework.processor.Crawler;
import org.thuir.jfcrawler.framework.writer.DefaultFileWriter;
import org.thuir.jfcrawler.framework.writer.Writer;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;
import org.thuir.jfcrawler.util.AccessController;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractJFCrawler extends Thread {
	protected Crawler[] crawlerPool = null;
	protected int crawlerPoolSize = 0;
	
	protected String jobName = null;

	//processor
	protected Fetcher fetcher = null;

	protected NonBlockingFetcher httpFetcher = null;

	//cache
	protected Cache cache = null;

	protected Frontier frontier = null;

	protected Class<? extends Filter> urlHandlerClass   = 
		HostFilter.class;

	protected Class<? extends Extractor> pageHandlerClass = 
		HTMLExtractor.class;

	protected Class<? extends Writer> writerClass =
		DefaultFileWriter.class;

	protected Class<? extends Cache> cacheClass = 
		BlockingQueueCache.class;

	protected Class<? extends Frontier> frontierClass = 
		BlockingQueueFrontier.class;

	protected Class<? extends Fetcher> fetcherClass = 
		DefaultFetcher.class;
	
	public AbstractJFCrawler(String jobName) {
		this.jobName = jobName;
	}

	public void initalizeFetcher(
			Class<? extends Fetcher> T) {
		this.fetcherClass = T;
	}

	public void initializeUrlHandlerClass(
			Class<? extends Filter> T) {
		this.urlHandlerClass = T;
	}

	public void initializePageHandlerClass(
			Class<? extends Extractor> T) {
		this.pageHandlerClass = T;
	}
	public void initializeFrontier(
			Class<? extends Frontier> T) {
		this.frontierClass = T;
	}
	public void initializeCache(
			Class<? extends Cache> T) {
		this.cacheClass = T;
	}
	public void initializeWriter(
			Class<? extends Writer> T) {
		this.writerClass = T;
	}

	public void initializeCrawler(
			Class<? extends Crawler> T, int nThread) {
		crawlerPoolSize = nThread;
		
		assert cache != null;
		assert frontier != null;
		
		try {			
			crawlerPool = new Crawler[nThread];
			for(int i = 0; i < nThread; i++) {
				crawlerPool[i] = T.newInstance();

				crawlerPool[i].setCache(cache);
				crawlerPool[i].setFrontier(frontier);

				crawlerPool[i].setPageHandler(
						pageHandlerClass.newInstance());
				crawlerPool[i].setUrlHandler(
						urlHandlerClass.newInstance());
				Writer w = writerClass.newInstance();
				w.setRoot(jobName);
				crawlerPool[i].setWriter(w);

			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		}
	}

	public void initializeModules() {		
		try {
			httpFetcher = new NonBlockingFetcher();

			frontier = frontierClass.newInstance();
			cache = cacheClass.newInstance();
			
			fetcher = fetcherClass.newInstance();
			fetcher.setNonBlockingFetcher(httpFetcher);
			fetcher.setCache(cache);
			fetcher.setFrontier(frontier);
			fetcher.setAccessController(new AccessController());
			
			httpFetcher.addFetchingListener(fetcher);
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public void setJobName(String name) {
		this.setName(name);
	}

	public void addSeed(String url) throws BadUrlFormatException {
		frontier.schedule(PageUrl.parse(null, url));
	}

	@Override
	public void run() {
		for(Crawler p : crawlerPool) {
			p.start();
		}
		fetcher.start();
	}

}
