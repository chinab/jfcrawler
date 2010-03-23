package org.thuir.jfcrawler.framework.scheduler;

import org.thuir.jfcrawler.framework.fetcher.IFetcher;

/**
 * @author ruKyzhc
 *
 */
public interface IGlobalScheduler {
	
	public void schedule(IFetcher fetcher);

}
