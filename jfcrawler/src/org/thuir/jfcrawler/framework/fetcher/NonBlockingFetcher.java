package org.thuir.jfcrawler.framework.fetcher;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.io.nio.FetchContextExchange;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class NonBlockingFetcher implements IFetcher {
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
				CrawlerConfiguration.getMaxThreadPoolSize()));
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

	private FetcherStatus status = FetcherStatus.UNKNOWN;

	@Override
	public void fetchPage(Page page) throws FetchingException {
		if(client == null) {
			logger.fatal("HttpClient has not been deployed.");
			throw new FetchingException(status);
		}
		
		FetchContextExchange exchange = new FetchContextExchange(page);
		exchange.setURL(page.getPageUrl().getUrl());

		try {
			status = FetcherStatus.SENDING_EXCHANGE;
			client.send(exchange);
		} catch (IOException e) {
			logger.error("Error [" + 
					e.getMessage() + "] occurs while fetching " +
					page.getPageUrl().getUrl());
			
			throw new FetchingException(status);
		}

	}

}
