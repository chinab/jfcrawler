package org.thuir.jfcrawler.framework.fetcher;

import org.thuir.jfcrawler.util.AbstractJFCrawlerException;

/**
 * @author ruKyzhc
 *
 */
public class FetchingException extends AbstractJFCrawlerException {
	private static final long serialVersionUID = -9024367626545224043L;
	
	private FetcherStatus fetcherStatus = null;
	
	public FetchingException(FetcherStatus status) {
		this.fetcherStatus = status;
	}
	
	/**
	 * @return the fetcherStatus
	 */
	public FetcherStatus getFetcherStatus() {
		return fetcherStatus;
	}
	/**
	 * @param fetcherStatus the fetcherStatus to set
	 */
	public void setFetcherStatus(FetcherStatus fetcherStatus) {
		this.fetcherStatus = fetcherStatus;
	}

}
