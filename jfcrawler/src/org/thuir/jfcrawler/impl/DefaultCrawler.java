package org.thuir.jfcrawler.impl;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.crawler.AbstractCrawler;
import org.thuir.jfcrawler.io.nio.FetchingException;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class DefaultCrawler extends AbstractCrawler {
	private static final Logger logger =
		Logger.getLogger(DefaultCrawler.class);
	
	private Page page = null;
	
	protected Queue<Page> buffer   = new ArrayDeque<Page>();

	@Override
	public void addSeed(PageUrl url) {
		scheduler.scheduleNewUrl(url);
	}

	@Override
	public void doFetch() {
		try {
			if(page != null) {
				System.err.println(page.getPageUrl());
				httpFetcher.fetchPage(this, page);
			}
		} catch (FetchingException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void postFetch(Page page) {
		System.err.println(page.getPageUrl().getUrl());
		if(!buffer.offer(page)) {
			logger.error("error to write into page buffer");
		}
		try {
			writer.write(page);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void preFetch() {
		Page temp = buffer.poll();
		if(temp != null) {
			PageUrl[] urls = extractor.extractUrls(temp);
			for(PageUrl url : urls) {
				if(filter.shouldVisit(url))
					scheduler.scheduleNewUrl(url);
			}
		} else {
			try {
				Thread.sleep(CrawlerConfiguration.getDefaultInterval());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PageUrl newUrl = scheduler.getNextUrl();
		if(newUrl != null) {
			page = new Page(newUrl);
		} else {
			page = null;
		}

	}

}
