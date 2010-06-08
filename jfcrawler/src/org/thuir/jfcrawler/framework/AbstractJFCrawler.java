package org.thuir.jfcrawler.framework;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.processor.Fetcher;
import org.thuir.jfcrawler.framework.processor.Crawler;
import org.thuir.jfcrawler.io.httpclient.MultiThreadHttpFetcher;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.Statistic;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractJFCrawler extends Thread {
	private static Logger logger = Logger.getLogger(AbstractJFCrawler.class);

	protected Crawler[] crawlerPool = null;
	protected int crawlerPoolSize = 0;

	protected String jobName = null;

	//processor
	protected Fetcher fetcher = null;

	protected MultiThreadHttpFetcher httpFetcher = null;

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
		httpFetcher =
			(MultiThreadHttpFetcher)Factory.getModule(Factory.MODULE_HTTPFETCHER);
	}

	public void initializeFetcher(
			Class<? extends Fetcher> T) {
		try {
			fetcher = T.newInstance();
		} catch (InstantiationException e) {
			logger.fatal("fetcher initialization failed", e);
		} catch (IllegalAccessException e) {
			logger.fatal("fetcher initialization failed", e);
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
			logger.fatal("crawler initialization failed", e);
		} catch (IllegalAccessException e) {
			logger.fatal("crawler initialization failed", e);
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
		logger.info("job " + this.jobName + " starts!!");

		Statistic.create("url-counter");
		Statistic.create("download-size-counter");
		long time = System.currentTimeMillis();

		for(Crawler p : crawlerPool) {
			p.start();
		}
		fetcher.start();
		int accumulate = 0;

		while(true) {
			try {
				Thread.sleep(
						BasicThread.INTERVAL * 20);
			} catch (InterruptedException e) {
			}
			System.err.println(
					"[cache:" + 
					cache.size() +
					"][frontier:" + 
					frontier.size() + 
			"]");
			boolean allIdle = true;
			for(Crawler p : crawlerPool) {
				if(!p.idle()) {
					allIdle = false;
					break;
				}
			}
			if(!httpFetcher.idle())
				allIdle = false;

			if(cache.size() == 0 && frontier.size() == 0 
					&& allIdle ) {
				if(accumulate >= 2) {
					System.err.println("finish");
					for(Crawler p : crawlerPool) {
						p.close();
					}
					fetcher.close();
					httpFetcher.close();
					break;
				} else {
					accumulate ++;
				}
			} else {
				accumulate = 0;
			}
		}
		double duration = (System.currentTimeMillis() - time) / 1000.0;

		System.err.println(
				"[catalog:" + Statistic.get("catalog-counter").count() + "]");
		System.err.println(
				"[board:" + Statistic.get("board-counter").count() + "]");
		System.err.println(
				"[thread:" + Statistic.get("thread-counter").count() + "]");

		System.err.println(
				"[time:" + duration+ "]");
		System.err.println(
				"[speed:" + 
				(Statistic.get("download-size-counter").count() / 
						(double)duration) + 
		"]");

		logger.info("job " + this.jobName + " finished!!");
		logger.info(
				"[catalog:" + Statistic.get("catalog-counter").count() + "]");
		logger.info(
				"[board:" + Statistic.get("board-counter").count() + "]");
		logger.info(
				"[thread:" + Statistic.get("thread-counter").count() + "]");

		logger.info(
				"[time:" + duration+ "]");
		logger.info(
				"[speed:" + 
				(Statistic.get("download-size-counter").count() / 
						(double)duration) + 
				"]"
		);
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
