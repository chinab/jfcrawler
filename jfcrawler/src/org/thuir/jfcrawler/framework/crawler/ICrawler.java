package org.thuir.jfcrawler.framework.crawler;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.io.IHttpFetcher;

/**
 * @author ruKyzhc
 *
 */
public interface ICrawler {
	
	public void init();
	
//	public void setFetcher(IFetcher fetcher);
//	
//	public IFetcher getFetcher();
//	
//	public void addCrawlerEventListener(CrawlerEventListener listener);

	//fetch process
	public void preFetch();
	
	public void doFetch(IHttpFetcher fetcher);

	public void postFetch(Page page);
//	
//	//status
//	public boolean isCrawling();
	
//	public void setIsCrawling(boolean b);

}
