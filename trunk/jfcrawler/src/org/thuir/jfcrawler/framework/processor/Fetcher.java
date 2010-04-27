package org.thuir.jfcrawler.framework.processor;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.frontier.Frontier;
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

	private static final long INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.thread-interval");
	
	private static final long ACCESS_INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.accessing-interval");

	protected MultiThreadHttpFetcher fetcher = null;

	protected Frontier frontier = null;

	protected Cache cache = null;
	
	protected AccessController accessCtrl = null;
	
	public void setAccessController(AccessController accessCtrl) {
		this.accessCtrl = accessCtrl;
	}

	public void setHttpFetcher(MultiThreadHttpFetcher fetcher) {
		this.fetcher = fetcher;
	}

	public void setFrontier(Frontier frontier) {
		this.frontier = frontier;
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	@Override
	public void run() {
		super.run();
		while(alive()) {
			try {
				Url url = frontier.next();
				if(url == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				setIdle(false);
				long temp = 
					System.currentTimeMillis() -
					accessCtrl.lastAccess(url.getHost());
				
				if(temp < ACCESS_INTERVAL ) {
					frontier.schedule(url);
					continue;
				}
				accessCtrl.access(url.getHost(), System.currentTimeMillis());
				
				fetcher.fetch(new FetchExchange(new Page(url), this));
//			} catch (FetchingException e) {
//				// TODO Auto-generated catch block
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			} finally {
				setIdle(true);
			}
		}
	}

	@Override
	public void onComplete(FetchExchange exchange) {
		System.err.println("finish:" + exchange.getUrl());
		cache.offer(exchange.getPage());
	}

	@Override
	public void onExcepted(FetchExchange exchange) {
		System.err.println("excepted:" + exchange.getUrl());
	}

	@Override
	public void onExpired(FetchExchange exchange) {
		System.err.println("expired:" + exchange.getUrl());
	}
	
	@Override
	public void onFailed(FetchExchange exchange) {
		System.err.println("failed:" + exchange.getUrl());
	}

}
