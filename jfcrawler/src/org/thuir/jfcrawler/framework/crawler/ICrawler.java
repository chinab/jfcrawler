package org.thuir.jfcrawler.framework.crawler;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.io.IHttpFetcher;

/**
 * @author ruKyzhc
 *
 */
public interface ICrawler {
	
	public void init();

	//fetch process
	public void preFetch();
	
	public void fetch(IHttpFetcher fetcher);

	public void postFetch(Page page);
	
	//status
	public boolean isCrawling();
	
	public void setIsCrawling(boolean b);

}
