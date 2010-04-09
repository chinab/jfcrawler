package org.thuir.jfcrawler.framework.processor;

import java.io.IOException;
import java.util.ArrayList;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
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

	protected ArrayList<Extractor> extractors = null;

	protected ArrayList<Filter>  filters  = null;

	protected ArrayList<Classifier> classifiers = null;

	protected Writer writer = null;

	protected Cache cache = null;

	protected Frontier frontier = null;

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

	@Override
	public void run() {
		ArrayList<PageUrl> urls = new ArrayList<PageUrl>();
		while(true) {
			try {
				Page page = cache.poll();
				if(page == null) {
					Thread.sleep(INTERVAL);
					continue;
				}

				writer.write(page);

				for(Extractor e : extractors) {
					ArrayList<PageUrl> ret = e.extractUrls(page);
					if(ret == null)
						continue;
					urls.addAll(ret);
				}



				boolean forbidden = false;
				for(PageUrl url : urls) {
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
					if(!forbidden)
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
