package org.thuir.jfcrawler.framework.processor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.classifier.Classifier;
import org.thuir.jfcrawler.framework.extractor.Extractor;
import org.thuir.jfcrawler.framework.filter.Filter;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.writer.Writer;
import org.thuir.jfcrawler.io.database.UrlDB;
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;
import org.thuir.jfcrawler.util.Statistic;

/**
 * @author ruKyzhc
 *
 */
public abstract class Crawler extends BasicThread {
	private static Logger logger = Logger.getLogger(Crawler.class);

	protected List<Extractor> extractors = null;

	protected List<Filter>  filters  = null;

	protected List<Classifier> classifiers = null;

	protected Writer writer = null;

	protected Cache cache = null;

	protected Frontier frontier = null;

	protected UrlDB urldb = null;

	public Crawler() {
		extractors  = new ArrayList<Extractor>();
		filters     = new ArrayList<Filter>();
		classifiers = new ArrayList<Classifier>();

		cache = Factory.getCacheInstance();
		frontier = Factory.getFrontierInstance();
		urldb = (UrlDB)Factory.getModule(Factory.MODULE_URLDB);
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

	@Override
	public void run() {
		super.run();
		List<Url> urls = new ArrayList<Url>();
		long revisit = 
			ConfigUtil.getConfig().getLong("crawler.revisit-interval");
		
		int c_c = 0;
		int s_c = 0;
		long p_t = 0l;
		long p_a = 0l;

		long s_t = 0l;
		long s_a = 0l;

		long c_t = 0l;
		long c_a = 0l;
		while(alive()) {
			try {
				Page page = cache.poll();
				if(page == null) {
					Thread.sleep(INTERVAL);
					continue;
				}
				setIdle(false);

				writer.write(page);
				
				Url tempUrl = page.getUrl();
				urldb.insert(tempUrl);
				
				Statistic.get("download-size-counter")
				.inc(page.getHtmlContent().length);
				Statistic.get("url-counter").inc();
				System.out.println("[" + this.getName() + 
						"][url]" + page.getUrl());
				System.out.println("[" + this.getName() + 
						"][url-counter]" + 
						Statistic.get("url-counter").count());

//				logger.info("[" + this.getName() + 
//						"][url]" + page.getUrl() + 
//						"\t[counter]" + Statistic.get("url-counter").count());

				for(Extractor e : extractors) {
					List<Url> ret = e.extractUrls(page);
					if(ret == null)
						continue;
					urls.addAll(ret);
				}
				long lastvisit = 0l;
				boolean forbidden = false;

				long cur_time = System.currentTimeMillis();

				p_t = 0l;
				s_t = 0l;
				c_t = 0l;
				c_c = 0;
				s_c = 0;
				for(Url url : urls) {
					forbidden = false;
					p_a = System.currentTimeMillis();
					for(Classifier c : classifiers) {
						c.process(url);
					}
					for(Filter f : filters) {
						if(!f.shouldVisit(url)) {
							forbidden = true;
							break;
						}
					}

					c_a = System.currentTimeMillis();
					p_t += c_a - p_a;
					c_c++;

					if(urldb != null)
						lastvisit = urldb.check(url);
					else
						lastvisit = -1l;

					s_a = System.currentTimeMillis();
					c_t += s_a - c_a;

					if(!forbidden) {
						if((lastvisit < 0) ||
								(cur_time - lastvisit > revisit)) {
							url.setStatus(Url.STATUS_PENDING);
							url.setVisit(System.currentTimeMillis());
							urldb.insert(url);
							frontier.schedule(url);
							s_c++;
						}
					}

					s_t += System.currentTimeMillis() - s_a;
				}

				System.err.println("[" + this.getName() + "]" 
						+ "[" + c_c + ":" + s_c + "]"  
						+ "[p time]" + p_t + "[p avg]" + ((double)p_t/c_c)
						+ "[c time]" + c_t + "[c avg]" + ((double)c_t/c_c)
						+ "[s time]" + s_t + "[s avg]" + ((double)s_t/c_c)
				);
			} catch (InterruptedException e) {
				continue;
			} catch (IOException e) {
				logger.error("IO excetpion in [" + this.getName() + "]", e);
			} catch (SQLException e) {
				logger.error("SQL excetpion in [" + this.getName() + "]", e);
			} catch (Exception e) {
				logger.error("Unknown error in [" + this.getName() + "]", e);
			}
			finally {
				setIdle(true);
				urls.clear();
			}
		}
	}

}
