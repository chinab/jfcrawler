package org.thuir.jfcrawler.test;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.io.nio.FetchingListener;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;

import junit.framework.TestCase;

public class TestJFCrawler extends TestCase implements FetchingListener{

	public void testWriter() throws Exception {
		NonBlockingFetcher fetcher = new NonBlockingFetcher();

		fetcher.addFetchingListener(this);
		fetcher.setUserAgent("THUIR-bot");

		Page page = new Page(Url.parse("http://www.discuz.net"));
		fetcher.fetch(page);
		while(!page.isReady())
			Thread.sleep(1000);

	}

	@Override
	public void onFetchingFinish(Page page) {
		page.ready();		
	}
}