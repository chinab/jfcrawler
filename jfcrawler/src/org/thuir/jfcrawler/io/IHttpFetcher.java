package org.thuir.jfcrawler.io;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.framework.crawler.ICrawler;
import org.thuir.jfcrawler.io.nio.FetchingException;

/**
 * @author ruKyzhc
 *
 */
public interface IHttpFetcher {
	
	public void fetchPage(ICrawler crawler, Page page) throws FetchingException;
	
	public void stopFetcher();

}
