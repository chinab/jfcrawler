package org.thuir.jfcrawler.io.nio;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class NonBlockingFetcher {
	private static final Logger logger =
		Logger.getLogger(NonBlockingFetcher.class);
	
	protected FetchingListener listener = null;
	
	public void addFetchingListener(FetchingListener listener) {
		this.listener = listener;
	}

	private HttpClient client = null;

	//initialize the fetcher
	public NonBlockingFetcher() {
		client = new HttpClient();
		client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
		client.setMaxConnectionsPerAddress(
				ConfigUtil.
				getConfig().getInt("fetcher.max-connections"));
		
		QueuedThreadPool pool = new QueuedThreadPool(
				ConfigUtil.
				getConfig().getInt("fetcher.max-pool-size"));
		client.setThreadPool(pool);
		client.setTimeout(
				ConfigUtil.
				getConfig().getInt("fetcher.max-timeout"));

		try {
			client.start();
		} catch (Exception e) {
			logger.fatal("HttpClient initialization fails!");
		}
	}

	public void close() {			
		try {
			client.stop();
		} catch (Exception e) {
			logger.fatal("Error occurs when closing HttpClient");
		}
	}


//	private NonBlockingFetcherStatus status = NonBlockingFetcherStatus.UNKNOWN;

	public void fetch(Page page) {
		if(client == null) {
			logger.fatal("HttpClient has not been deployed.");
//			throw new FetchingException(status);
		}

		FetchContextExchange exchange = new FetchContextExchange(page);
		exchange.setURL(page.getUrl().getUrl());
		exchange.setFetchingListener(listener);

		try {
//			status = NonBlockingFetcherStatus.SENDING_EXCHANGE;
			client.send(exchange);
		} catch (IOException e) {
			logger.error("Error [" + 
					e.getMessage() + "] occurs while fetching " +
					page.getUrl().getUrl());

//			throw new FetchingException(status);
		}

	}

	public void stop() {
		try {
			client.stop();
		} catch (Exception e) {
			logger.fatal("error when closing http client");
		}		
	}

}
