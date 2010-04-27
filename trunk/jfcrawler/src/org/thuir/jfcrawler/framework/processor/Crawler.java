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
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;
import org.thuir.jfcrawler.util.stat.Statistic;

/**
 * @author ruKyzhc
 *
 */
public abstract class Crawler extends BasicThread {

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
		super.run();
		List<Url> urls = new ArrayList<Url>();
		long revisit = 
			ConfigUtil.getConfig().getLong("crawler.revisit-interval");
		while(alive()) {
			try {
				Page page = cache.poll();
				if(page == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				setIdle(false);
				long cur_time = System.currentTimeMillis();

				writer.write(page);
				page.getUrl().setLastVisit(cur_time);

				Statistic.get("download-size-counter")
				.inc(page.getHtmlContent().length);
				Statistic.get("url-counter").inc();
				System.out.println("[url]" + page.getUrl());
				System.out.println("[url-counter]" + 
						Statistic.get("url-counter").count());
				System.out.println("[download-size]" + 
						(Statistic.get("download-size-counter").count()
								/ 1048576.0));

				if(urldb != null)
					urldb.save(page.getUrl());

				for(Extractor e : extractors) {
					List<Url> ret = e.extractUrls(page);
					if(ret == null)
						continue;
					urls.addAll(ret);
				}
				long lastvisit = 0l;
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

					if(urldb != null)
						lastvisit = urldb.check(url);
					else
						lastvisit = -1l;

					if(!forbidden) {
						if((lastvisit < 0) ||
								(lastvisit - cur_time > revisit))
							frontier.schedule(url);
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			} finally {
				setIdle(true);
			}
		}
	}
	
}
