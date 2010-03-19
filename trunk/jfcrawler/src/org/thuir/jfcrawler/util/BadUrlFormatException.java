package org.thuir.jfcrawler.util;

/**
 * @author ruKyzhc
 *
 */
public class BadUrlFormatException extends Exception {
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
