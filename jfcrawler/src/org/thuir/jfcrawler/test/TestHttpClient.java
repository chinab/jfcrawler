package org.thuir.jfcrawler.test;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.io.httpclient.FetchExchange;
import org.thuir.jfcrawler.io.httpclient.FetchingListener;
import org.thuir.jfcrawler.io.httpclient.MultiThreadHttpFetcher;

/**
 * @author ruKyzhc
 *
 */
public class TestHttpClient implements FetchingListener{

	public static void main(String[] args) 
	throws BadUrlFormatException, ClientProtocolException, IOException, InterruptedException {
		MultiThreadHttpFetcher fetcher = new MultiThreadHttpFetcher();

		System.out.println("start");
		TestHttpClient test = new TestHttpClient();

		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.icefirer.com")), test, "THUIR-bot"));
		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.baidu.com")), test, "THUIR-bot"));
		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.renren.com")), test, "THUIR-bot"));
		
		Thread.sleep(3000);

		fetcher.close();
	}
	
	@Override
	public void onComplete(FetchExchange exchange) {
		System.out.println("complete");
		System.out.println(exchange.getPage().getHtmlContent().length);
	}

	@Override
	public void onExcepted(FetchExchange exchange) {
		System.out.println("excepted");
		
	}

	@Override
	public void onExpired(FetchExchange exchange) {
		System.out.println("expired");
		
	}

	@Override
	public void onFailed(FetchExchange exchange) {
		System.out.println("failed");
		
	}

}
