package org.thuir.jfcrawler.framework.processor;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.io.nio.FetchingListener;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;
import org.thuir.jfcrawler.util.AccessController;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public abstract class Fetcher extends Thread implements FetchingListener{

	private static final long INTERVAL = 1000l;
	
	private static final long ACCESS_INTERVAL = 
		CrawlerConfiguration.getAccessInterval();

	protected NonBlockingFetcher fetcher = null;

	protected Frontier frontier = null;

	protected Cache cache = null;
	
	protected AccessController accessCtrl = null;
	
	public void setAccessController(AccessController accessCtrl) {
		this.accessCtrl = accessCtrl;
	}

	public void setNonBlockingFetcher(NonBlockingFetcher fetcher) {
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
		while(true) {
			try {
				PageUrl url = frontier.next();
				if(url == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				
				long temp = 
					System.currentTimeMillis() -
					accessCtrl.lastAccess(url.getHost());
				
				if(temp < ACCESS_INTERVAL ) {
					frontier.schedule(url);
					continue;
				}
				accessCtrl.access(url.getHost(), System.currentTimeMillis());
				fetcher.fetch(new Page(url));
//			} catch (FetchingException e) {
//				// TODO Auto-generated catch block
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	@Override
	public void onFetchingFinish(Page page) {
		try {
			while(!cache.offer(page)) {
				Thread.sleep(INTERVAL);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}

	}

}
