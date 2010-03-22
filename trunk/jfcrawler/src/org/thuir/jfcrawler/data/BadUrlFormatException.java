package org.thuir.jfcrawler.data;

import org.thuir.jfcrawler.util.AbstractJFCrawlerException;

/**
 * @author ruKyzhc
 *
 */
public class BadUrlFormatException extends AbstractJFCrawlerException {
	private static final long serialVersionUID = 6694466667833392808L;
	
	private String message;
	
	public BadUrlFormatException(String className, String cause) {
		this.message = className + " : " + cause;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
