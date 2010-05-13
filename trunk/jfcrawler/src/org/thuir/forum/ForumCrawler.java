package org.thuir.forum;

import org.thuir.forum.classifier.ForumUrlClassifier;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.extractor.ForumExtractor;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.framework.AbstractJFCrawler;
import org.thuir.jfcrawler.framework.filter.HostFilter;
import org.thuir.jfcrawler.framework.processor.DefaultCrawler;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.util.stat.Statistic;

/**
 * @author ruKyzhc
 *
 */
public class ForumCrawler extends AbstractJFCrawler {

	public ForumCrawler(String jobName) {
		super(jobName);
	}

	public static void main(String[] args) 
	throws BadUrlFormatException {
		ForumUrl.registerToUrlFactory();
		TemplateRepository.load("./template");
		ForumCrawler crawler = new ForumCrawler("forum");

		crawler.initialize();
		crawler.initializeFetcher(DefaultFetcher.class);
		crawler.initializeCrawler(DefaultCrawler.class, 15);

		HostFilter f = new HostFilter();
		f.setHost("www.icefirer.com");
		crawler.addFilter(f);

		crawler.addExtractor(new ForumExtractor());
		crawler.addClassifier(new ForumUrlClassifier());

		crawler.addSeed("http://www.icefirer.com/index-htm-m-bbs.html");

		Statistic.create("catalog-counter");
		Statistic.create("board-counter");
		Statistic.create("thread-counter");

		crawler.start();
	}

}
