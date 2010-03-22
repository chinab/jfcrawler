package org.thuir.jfcrawler.framework.fetcher;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public interface IFetcher {
	
	public void fetchPage(Page page) throws FetchingException;

}
