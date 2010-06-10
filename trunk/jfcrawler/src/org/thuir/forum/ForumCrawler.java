package org.thuir.forum;

import java.io.File;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.extractor.ForumExtractor;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.framework.AbstractJFCrawler;
import org.thuir.jfcrawler.framework.Factory;
import org.thuir.jfcrawler.framework.frontier.BlockingQueueFrontier;
import org.thuir.jfcrawler.framework.processor.DefaultCrawler;
import org.thuir.jfcrawler.framework.processor.DefaultFetcher;
import org.thuir.jfcrawler.util.Statistic;

/**
 * @author ruKyzhc
 *
 */
public class ForumCrawler extends AbstractJFCrawler {
	private static Logger logger =
		Logger.getLogger(ForumCrawler.class);

	public ForumCrawler(String jobName) {
		super(jobName);
	}

	public static void main(String[] args) 
	throws BadUrlFormatException {
		File testFile = new File("");
		System.out.println(testFile.getAbsolutePath());
		
		ForumUrl.registerToUrlFactory();
		TemplateRepository.load("./template");
		String job = "newsmth";
		ForumCrawler crawler = new ForumCrawler(job);
		
		Factory.registerFrontierClass(BlockingQueueFrontier.class);

		crawler.initialize();
		crawler.initializeFetcher(DefaultFetcher.class);
		crawler.initializeCrawler(DefaultCrawler.class);

		crawler.addExtractor(new ForumExtractor());

		crawler.addSeed("http://www.newsmth.net/bbsfav.php?x");

		Statistic.create("catalog-counter");
		Statistic.create("board-counter");
		Statistic.create("thread-counter");
		
		logger.info("crawler " + job + " starts");
		crawler.start();
	}

}
