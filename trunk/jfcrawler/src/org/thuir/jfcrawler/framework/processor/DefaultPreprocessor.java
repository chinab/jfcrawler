package org.thuir.jfcrawler.framework.processor;


/**
 * @author ruKyzhc
 *
 */
public class DefaultPreprocessor extends Preprocessor {
//	private static final Logger logger =
//		Logger.getLogger(DefaultPreprocessor.class);
	
	private static int count = 0;
	
	private int id = 0;
	
	public DefaultPreprocessor() {
		this.id = count++;
		this.setName("preprocessor " + id);
	}
}
