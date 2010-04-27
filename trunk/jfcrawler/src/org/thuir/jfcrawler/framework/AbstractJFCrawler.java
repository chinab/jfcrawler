package org.thuir.jfcrawler.framework;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.BlockingQueueCache;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.BlockingQueueFrontier;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.framework.processor.Fetcher;
import org.thuir.jfcrawler.framework.processor.Crawler;
import org.thuir.jfcrawler.framework.writer.DefaultFileWriter;
import org.thuir.jfcrawler.framework.writer.Writer;
import org.thuir.jfcrawler.io.database.UrlDB;
import org.thuir.jfcrawler.io.httpclient.MultiThreadHttpFetcher;
import org.thuir.jfcrawler.util.AccessController;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.stat.Statistic;

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

	protected MultiThreadHttpFetcher httpFetcher = null;

	//cache
	protected Cache cache = null;

	protected Frontier frontier = null;

	protected UrlDB urldb = null;

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

	public void initalizeUrlDB() {
		try {
			urldb = new UrlDB();
			urldb.clear();
		} catch (Exception e) {
			this.urldb = null;
		}
	}

	public void initializeCrawler(
			Class<? extends Crawler> T, int nThread) {
		crawlerPoolSize = nThread;

		assert cache != null;
		assert frontier != null;
		assert urldb != null;

		try {			
			crawlerPool = new Crawler[nThread];
			for(int i = 0; i < nThread; i++) {
				crawlerPool[i] = T.newInstance();

				crawlerPool[i].setCache(cache);
				crawlerPool[i].setFrontier(frontier);
				crawlerPool[i].setUrlDB(urldb);

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
			httpFetcher = new MultiThreadHttpFetcher();

			frontier = frontierClass.newInstance();
			cache = cacheClass.newInstance();

			fetcher = fetcherClass.newInstance();
			fetcher.setHttpFetcher(httpFetcher);
			fetcher.setCache(cache);
			fetcher.setFrontier(frontier);
			fetcher.setAccessController(new AccessController());

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
		frontier.schedule(Url.parseWithParent(null, url));
	}

	@Override
	public void run() {
		Statistic.create("url-counter");
		Statistic.create("download-size-counter");

		for(Crawler p : crawlerPool) {
			p.start();
		}
		fetcher.start();
		
		while(true) {
			try {
				Thread.sleep(
						BasicThread.INTERVAL * 10);
			} catch (InterruptedException e) {
			}
			boolean allIdle = true;
			for(Crawler p : crawlerPool) {
				if(!p.idle()) {
					allIdle = false;
					break;
				}
			}
			if(!fetcher.idle())
				allIdle = false;
			if(cache.size() == 0 && frontier.size() == 0 && allIdle) {
				for(Crawler p : crawlerPool) {
					p.close();
				}
				fetcher.close();
				httpFetcher.close();
				break;
			}				
		}
	}

	public void addExtractor(Extractor e) {
		for(Crawler c : crawlerPool) {
			c.addExtractor(e);
		}
	}

	public void addFilter(Filter f) {
		for(Crawler c : crawlerPool) {
			c.addFilter(f);
		}
	}

	public void addClassifier(Classifier l) {
		for(Crawler c : crawlerPool) {
			c.addClassifier(l);
		}
	}

}
