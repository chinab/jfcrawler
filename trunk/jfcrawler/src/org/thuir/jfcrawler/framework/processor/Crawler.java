package org.thuir.jfcrawler.framework.processor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.writer.Writer;
import org.thuir.jfcrawler.io.database.UrlDB;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public abstract class Crawler extends Thread {

	private static final long INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.thread-interval");;

	protected List<Extractor> extractors = null;

	protected List<Filter>  filters  = null;

	protected List<Classifier> classifiers = null;

	protected Writer writer = null;

	protected Cache cache = null;

	protected Frontier frontier = null;
	
	protected UrlDB urldb = null;

	public Crawler() {
		extractors = new ArrayList<Extractor>();
		filters  = new ArrayList<Filter>();
		classifiers = new ArrayList<Classifier>();
	}

	public void addExtractor(Extractor extractor) {
		this.extractors.add(extractor);
	}

	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}

	public void addClassifier(Classifier classifier) {
		this.classifiers.add(classifier);
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

	public void setUrlDB(UrlDB urldb) {
		this.urldb = urldb;
	}
	
	@Override
	public void run() {
		List<Url> urls = new ArrayList<Url>();
		while(true) {
			try {
				Page page = cache.poll();
				if(page == null) {
					Thread.sleep(INTERVAL);
					continue;
				}

				writer.write(page);
				
				if(urldb != null)
					urldb.save(page.getUrl());
				
				for(Extractor e : extractors) {
					List<Url> ret = e.extractUrls(page);
					if(ret == null)
						continue;
					urls.addAll(ret);
				}



				boolean forbidden = false;
				for(Url url : urls) {
					forbidden = false;
					for(Classifier c : classifiers) {
						c.process(url);
					}
					for(Filter f : filters) {
						if(!f.shouldVisit(url)) {
							forbidden = true;
							break;
						}
					}
					if(!forbidden) {
						frontier.schedule(url);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}
		}
	}

}
