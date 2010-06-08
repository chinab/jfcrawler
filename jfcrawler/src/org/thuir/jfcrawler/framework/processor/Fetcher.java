package org.thuir.jfcrawler.framework.processor;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.io.database.UrlDB;
import org.thuir.jfcrawler.io.httpclient.FetchExchange;
import org.thuir.jfcrawler.io.httpclient.MultiThreadHttpFetcher;
import org.thuir.jfcrawler.io.httpclient.FetchingListener;
import org.thuir.jfcrawler.util.AccessController;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public abstract class Fetcher extends BasicThread implements FetchingListener{
	private static Logger logger = Logger.getLogger(Fetcher.class);

	private static final long INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.thread-interval");
	
	private static final long ACCESS_INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.accessing-interval");

	protected MultiThreadHttpFetcher fetcher = null;

	protected Frontier frontier = null;

	protected Cache cache = null;
	
	protected AccessController accessCtrl = null;
	
	protected UrlDB urldb = null;
	
	public Fetcher() {
		frontier = 
			Factory.getFrontierInstance();
		cache =
			Factory.getCacheInstance();
		accessCtrl = 
			(AccessController)Factory.getModule(Factory.MODULE_ACCESSCONTROLLER);
		fetcher =
			(MultiThreadHttpFetcher)Factory.getModule(Factory.MODULE_HTTPFETCHER);
		urldb = 
			(UrlDB)Factory.getModule(Factory.MODULE_URLDB);
	}

	@Override
	public void run() {		
		super.run();
		while(alive()) {
			try {
				long cur_time = System.currentTimeMillis();
				Url url = frontier.next();
				if(url == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				setIdle(false);
				long temp = 
					cur_time -
					accessCtrl.lastAccess(url.getHost());
				
				if(temp < ACCESS_INTERVAL ) {
					frontier.schedule(url);
					continue;
				}
				accessCtrl.access(url.getHost(), cur_time);
				

				if(urldb != null) {
					url.setLastVisit(cur_time);
					if(urldb.save(url))
						System.err.println("[urldb:exists]" + url);
					else
						fetcher.fetch(new FetchExchange(new Page(url), this));
				}
			} catch (InterruptedException e) {
				continue;
			} catch (SQLException e) {
				logger.error("error when check with database.", e);
			} finally {
				setIdle(true);
			}
		}
	}

	@Override
	public void onComplete(FetchExchange exchange) {
		System.err.println("finish:" + exchange.getUrl());
		logger.info("finish:" + exchange.getUrl());
		while(!cache.offer(exchange.getPage())) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void onExcepted(FetchExchange exchange) {
		System.err.println("excepted:" + exchange.getUrl());
		logger.info("excepted:" + exchange.getUrl());
	}

	@Override
	public void onExpired(FetchExchange exchange) {
		System.err.println("expired:" + exchange.getUrl());
		logger.info("expired:" + exchange.getUrl());
	}
	
	@Override
	public void onFailed(FetchExchange exchange) {
		System.err.println("failed:" + exchange.getUrl());
		logger.info("failed:" + exchange.getUrl());
	}
	
	@Override
	public void close() {
		super.close();
		fetcher.close();
	}

}
