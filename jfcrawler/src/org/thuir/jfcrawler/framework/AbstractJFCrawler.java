package org.thuir.jfcrawler.framework;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.processor.Fetcher;
import org.thuir.jfcrawler.framework.processor.Crawler;
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

	//cache
	protected Cache cache = null;

	protected Frontier frontier = null;

	public AbstractJFCrawler(String jobName) {
		this.jobName = jobName;
	}
	
	public void initialize() {
		Factory.initAllModuleWithDefault();
		cache = Factory.getCacheInstance();
		frontier = Factory.getFrontierInstance();
	}

	public void initializeFetcher(
			Class<? extends Fetcher> T) {
		try {
			fetcher = T.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		}
	}

	public void initializeCrawler(
			Class<? extends Crawler> T, int nThread) {
		crawlerPoolSize = nThread;

		try {			
			crawlerPool = new Crawler[nThread];
			for(int i = 0; i < nThread; i++) {
				crawlerPool[i] = T.newInstance();				
				crawlerPool[i].setWriter(
						Factory.getWriterInstance(jobName));
			}
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
		long time = System.currentTimeMillis();

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
				System.err.println("finish");
				for(Crawler p : crawlerPool) {
					p.close();
				}
				fetcher.close();
				break;
			}				
		}
		double duration = (System.currentTimeMillis() - time) / 1000.0;

		System.err.println(
				"[time:" + duration+ "]");
		System.err.println(
				"[speed:" + 
				(Statistic.get("download-size-counter").count() / 
						(double)duration) + 
		"]");
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
