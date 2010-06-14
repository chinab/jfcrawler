package org.thuir.forum;

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
		ForumUrl.registerToUrlFactory();
		TemplateRepository.load("./template");
		String job = "newsmth";
		ForumCrawler crawler = new ForumCrawler(job);
		
		Factory.registerFrontierClass(BlockingQueueFrontier.class);

		crawler.initialize();
		crawler.initializeFetcher(DefaultFetcher.class);
		crawler.initializeCrawler(DefaultCrawler.class);

		crawler.addExtractor(new ForumExtractor());

		crawler.addSeed("http://www.newsmth.net/bbssec.php");
		
		logger.info("crawler " + job + " starts");
		crawler.start();
	}

}
