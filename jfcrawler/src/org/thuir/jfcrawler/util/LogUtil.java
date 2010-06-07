package org.thuir.jfcrawler.util;

/**
 * @author ruKyzhc
 *
 */
public class LogUtil {
	
	public static String message(String msg, Exception e) {
		return msg + "[Message][" + e.getMessage() + "]";
	}
}
