package org.thuir.jfcrawler.framework.processor;


/**
 * @author ruKyzhc
 *
 */
public class DefaultCrawler extends Crawler {
//	private static final Logger logger =
//		Logger.getLogger(DefaultPreprocessor.class);
	
	private static int count = 0;
	
	private int id = 0;
	
	public DefaultCrawler() {
		this.id = count++;
		this.setName("crawler " + id);
	}
}
