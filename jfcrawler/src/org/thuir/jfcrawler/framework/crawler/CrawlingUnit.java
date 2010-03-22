package org.thuir.jfcrawler.framework.crawler;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.extractor.IExtractor;
import org.thuir.jfcrawler.framework.fetcher.FetchingException;
import org.thuir.jfcrawler.framework.fetcher.IFetcher;
import org.thuir.jfcrawler.framework.filter.IFilter;
import org.thuir.jfcrawler.framework.frontier.IFrontier;

/**
 * @author ruKyzhc
 *
 */
public class CrawlingUnit implements ICrawler {
	private static final Logger logger = Logger.getLogger(CrawlingUnit.class);

	private IFilter    filter = null;
	private IFrontier  frontier = null;
	private IExtractor extractor = null;

	//	private int crawlingId;
	private Boolean isCrawling = false;

	private Page curPage = null;

	public void setFilter(IFilter filter) {
		this.filter = filter;
	}

	public void setFrontier(IFrontier frontier) {
		this.frontier = frontier;
	}

	public void setExtractor(IExtractor extractor) {
		this.extractor = extractor;
	}


	@Override
	public void init() {

	}

	@Override
	public void fetch(IFetcher fetcher) {
		if(curPage == null)
			return;
		
		try {
			fetcher.fetchPage(curPage);
		} catch (FetchingException e) {
			//TODO
		}		
	}

	@Override
	public void preFetch() {
		PageUrl url; 
		if(filter.shouldVisit(url = frontier.getNextUrl())) {
			curPage = new Page(url);
			curPage.setCrawler(this);
		} else {
			curPage = null;
		}
	}

	@Override
	public void postFetch(Page page) {
		frontier.scheduleNewUrls(
				extractor.extractUrls(page));
	}

	@Override
	public boolean isCrawling() {
		synchronized(isCrawling) {
			return isCrawling;
		}
	}

	@Override
	public void setIsCrawling(boolean b) {
		synchronized(isCrawling) {
			isCrawling = b;
		}
	}

}
