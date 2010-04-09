package org.thuir.jfcrawler.framework.processor;

import java.io.IOException;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.writer.Writer;

/**
 * @author ruKyzhc
 *
 */
public abstract class Crawler extends Thread {

	private static final long INTERVAL = 1000l;

	protected Extractor pageHandler = null;

	protected Filter  urlHandler  = null;

	protected Writer  writer      = null;

	protected Cache   cache   = null;
	
	protected Frontier frontier = null;

	public void setPageHandler(Extractor pageHandler) {
		this.pageHandler = pageHandler;
	}

	public void setUrlHandler(Filter urlHandler) {
		this.urlHandler = urlHandler;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	
	public void setFrontier(Frontier frontier) {
		this.frontier = frontier;
	}

	@Override
	public void run() {
		while(true) {
			try {
				Page page = cache.poll();
				if(page == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				
				writer.write(page);
				
				PageUrl[] urls = pageHandler.extractUrls(page);
				if(urls == null)
					continue;
				
				for(PageUrl url : urls) {
					if(!urlHandler.shouldVisit(url))
						continue;
					frontier.schedule(url);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

}
