package org.thuir.jfcrawler.framework.processor;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.io.nio.FetchingException;
import org.thuir.jfcrawler.io.nio.FetchingListener;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;

/**
 * @author ruKyzhc
 *
 */
public abstract class Fetcher extends Thread implements FetchingListener{

	private static final long INTERVAL = 1000l;

	protected NonBlockingFetcher fetcher = null;

	protected Frontier frontier = null;

	protected Cache cache = null;

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
				System.out.println("url : [" + url +"]");
				fetcher.fetch(new Page(url));
			} catch (FetchingException e) {
				// TODO Auto-generated catch block
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
