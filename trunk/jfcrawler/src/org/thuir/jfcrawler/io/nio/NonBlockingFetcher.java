package org.thuir.jfcrawler.io.nio;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.framework.crawler.ICrawler;
import org.thuir.jfcrawler.io.IHttpFetcher;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class NonBlockingFetcher implements IHttpFetcher {
	private static final Logger logger =
		Logger.getLogger(NonBlockingFetcher.class);
	
	private static HttpClient client = null;

	//initialize the fetcher
	static {
		client = new HttpClient();
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.setMaxConnectionsPerAddress(
				CrawlerConfiguration.getMaxConnectionsPerAddress());
		client.setThreadPool(new QueuedThreadPool(
				CrawlerConfiguration.getMaxHttpFetcherThreadPoolSize()));
		client.setTimeout(
				CrawlerConfiguration.getMaxTimeout());

		try {
			client.start();
		} catch (Exception e) {
			logger.fatal("HttpClient initialization fails!");
		}
	}
	
	public static void closeFetcher() {			
		try {
			client.stop();
		} catch (Exception e) {
			logger.fatal("Error occurs when closing HttpClient");
		}
	}

	private NonBlockingFetcherStatus status = NonBlockingFetcherStatus.UNKNOWN;

	@Override
	public void fetchPage(ICrawler crawler, Page page) 
	throws FetchingException {
		if(client == null) {
			logger.fatal("HttpClient has not been deployed.");
			throw new FetchingException(status);
		}
		
		FetchContextExchange exchange = new FetchContextExchange(page);
		exchange.setURL(page.getPageUrl().getUrl());

		try {
			status = NonBlockingFetcherStatus.SENDING_EXCHANGE;
			client.send(exchange);
		} catch (IOException e) {
			logger.error("Error [" + 
					e.getMessage() + "] occurs while fetching " +
					page.getPageUrl().getUrl());
			
			throw new FetchingException(status);
		}

	}

	@Override
	public void stopFetcher() {
		try {
			client.stop();
		} catch (Exception e) {
			logger.fatal("error when closing http client");
		}		
	}

}
