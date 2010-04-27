package org.thuir.jfcrawler.io.httpclient;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class FetchUnit extends BasicThread {
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

	//flag
//	private boolean idle = true;
//	private boolean alive = true;

	public FetchUnit(String threadName, HttpClient client) {
		this.setName(threadName);
		this.httpClient = client;
		
		context = new BasicHttpContext();
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
					exchange.onExcepted();
					setIdle(true);
					continue;
				}
				
				timer();
				HttpResponse response = httpClient.execute(httpget, context);
				reset();
				
				HttpEntity entity = response.getEntity();
				if(entity != null) {
					exchange.getPage().load(EntityUtils.toByteArray(entity));
					exchange.onComplete();
				} else {
					exchange.onFailed();
				}
			} catch (InterruptedException e) {
			} catch (IOException e) {
				exchange.onExcepted();
			} finally {
				setIdle(true);
			}
		}

	}
	
	public synchronized void fetch(FetchExchange exchange) {
		this.exchange = exchange;
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
		this.exchange.onExpired();
		setAlive(false);
	}
	
	public void close() {
		setAlive(false);
	}

}
