package org.thuir.jfcrawler.io.httpclient;

/**
 * @author ruKyzhc
 *
 */
public interface FetchingListener {
	
	public void onExpired(FetchExchange exchange);
	
	public void onComplete(FetchExchange exchange);
	
	public void onExcepted(FetchExchange exchange);
	
	public void onFailed(FetchExchange exchange);
	
}
