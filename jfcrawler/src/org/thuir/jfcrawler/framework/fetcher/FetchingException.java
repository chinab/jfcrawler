package org.thuir.jfcrawler.framework.fetcher;

import org.thuir.jfcrawler.io.nio.NonBlockingFetcherStatus;
import org.thuir.jfcrawler.util.AbstractJFCrawlerException;

/**
 * @author ruKyzhc
 *
 */
public class FetchingException extends AbstractJFCrawlerException {
	private static final long serialVersionUID = -9024367626545224043L;
	
	private NonBlockingFetcherStatus fetcherStatus = null;
	
	public FetchingException(NonBlockingFetcherStatus status) {
		this.fetcherStatus = status;
	}
	
	/**
	 * @return the fetcherStatus
	 */
	public NonBlockingFetcherStatus getFetcherStatus() {
		return fetcherStatus;
	}
	/**
	 * @param fetcherStatus the fetcherStatus to set
	 */
	public void setFetcherStatus(NonBlockingFetcherStatus fetcherStatus) {
		this.fetcherStatus = fetcherStatus;
	}

}
