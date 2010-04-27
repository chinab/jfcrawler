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
//		// Create and initialize HTTP parameters
//		HttpParams params = new BasicHttpParams();
//		ConnManagerParams.setMaxTotalConnections(params, 100);
//		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//
//		// Create and initialize scheme registry 
//		SchemeRegistry schemeRegistry = new SchemeRegistry();
//		schemeRegistry.register(
//				new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//
//		// Create an HttpClient with the ThreadSafeClientConnManager.
//		// This connection manager must be used if more than one thread will
//		// be using the HttpClient.
//		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
//		HttpClient httpClient = new DefaultHttpClient(cm, params);
//
//		HttpGet httpget = new HttpGet(Url.parse("http://www.icefirer.com").getUrl());
//		HttpContext context = new BasicHttpContext();
//		HttpResponse response = httpClient.execute(httpget, context);
//		// get the response body as an array of bytes
//		HttpEntity entity = response.getEntity();
//		
//		if (entity != null) {
//			byte[] bytes = EntityUtils.toByteArray(entity);
//			System.out.println(bytes.length + " bytes read");
//			System.out.println(new String(bytes));
//		}
		MultiThreadHttpFetcher fetcher = new MultiThreadHttpFetcher(5);
		fetcher.doStart();

		System.out.println("start");
		TestHttpClient test = new TestHttpClient();

		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.icefirer.com")), test));
		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.baidu.com")), test));
		fetcher.fetch(new FetchExchange(new Page(Url.parse("http://www.renren.com")), test));
		
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
