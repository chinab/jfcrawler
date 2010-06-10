package org.thuir.jfcrawler.io.httpclient;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class FetchUnit extends BasicThread {
	private static Logger logger = Logger.getLogger(FetchUnit.class);

	private final long INTERVAL =
		ConfigUtil.getConfig().getInt("basic.thread-interval");		

	//timer
	private final long TIMEOUT = 
		ConfigUtil.getConfig().getInt("fetcher.max-timeout");
	private long expired = -1l;

	//httpclient
	private HttpClient httpClient = null;

	private HttpGet httpget = null;
	private HttpContext context = null;

	private FetchExchange exchange = null;

	public FetchUnit(String threadName, HttpClient client) {
		this.setName(threadName);
		this.httpClient = client;
	}

	@Override
	public void run() {
		super.run();
		while(true) {
			try{
				while(alive() && idle())
					Thread.sleep(INTERVAL);
				if(!alive())
					break;

				if(exchange == null) {
					setIdle(true);
					continue;
				}

				if(httpget == null 
						|| httpClient == null 
						|| httpget == null 
						|| context == null) {
					logger.error("http fetcher isn't ready");
					exchange.excepted(new NullPointerException());
					exchange.onExcepted();
					setIdle(true);
					continue;
				}

				timer();
				HttpResponse response = httpClient.execute(httpget, context);
				reset();

				HttpEntity entity = response.getEntity();
				if(entity != null) {
					Object obj = context.getAttribute("http.protocol.redirect-locations");
					if(obj != null) {
						RedirectLocations locs = (RedirectLocations)obj;
						List<URI> redirectUris = locs.getAll();
						for(URI uri : redirectUris) {
							System.out.println(exchange.getPage().getUrl() + " -->> " + uri.toString());
							exchange.getPage().redirect(uri.toString());
						}
					}
					exchange.getPage().load(EntityUtils.toByteArray(entity));

					Url url = exchange.getUrl();
					url.setCode(response.getStatusLine().getStatusCode());
					url.setStatus(Url.STATUS_COMPLETE);
					url.setVisit(System.currentTimeMillis());

					exchange.onComplete();
				} else {
					Url url = exchange.getUrl();
					url.setCode(response.getStatusLine().getStatusCode());
					url.setStatus(Url.STATUS_FAILED);
					url.setVisit(System.currentTimeMillis());

					exchange.onFailed();
				}
			} catch (InterruptedException e) {
			} catch (IOException e) {
				logger.error("error while fetching '" + 
						exchange.getPage().getUrl().toString() + "'", e);
				
				Url url = exchange.getUrl();
				url.setCode(0);
				url.setStatus(Url.STATUS_EXCEPTED);
				url.setVisit(System.currentTimeMillis());
				
				exchange.excepted(e);
				exchange.onExcepted();
			} finally {
				setIdle(true);
			}
		}

	}

	public synchronized void fetch(FetchExchange exchange) {
		this.exchange = exchange;

		context = new BasicHttpContext();
		httpget = new HttpGet(exchange.getUrl().getUrl());
		String userAgent = null;
		if((userAgent = exchange.getUserAgent()) != null)
			httpget.addHeader("User-Agent", userAgent);
		setIdle(false);
	}

	//	protected synchronized void setAlive(boolean alive) {
	//		this.alive = alive;
	//	}
	//	
	//	protected synchronized boolean alive() {
	//		return alive;
	//	}
	//
	//	protected synchronized void setIdle(boolean idle) {
	//		this.idle = idle;
	//	}
	//
	//	public synchronized boolean idle() {
	//		return idle;
	//	}

	public synchronized void timer() {
		expired = System.currentTimeMillis() + TIMEOUT;
	}

	public synchronized void reset() {
		expired = -1l;
	}

	public synchronized boolean timeout() {
		return expired > 0l && System.currentTimeMillis() > expired;
	}

	public void expired() {
		Url url = exchange.getUrl();
		url.setCode(0);
		url.setStatus(Url.STATUS_EXPIRED);
		url.setVisit(System.currentTimeMillis());
		
		this.exchange.onExpired();
		setAlive(false);
	}

	public void close() {
		setAlive(false);
	}

}
