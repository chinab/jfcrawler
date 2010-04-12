package org.thuir.jfcrawler.data;

/**
 * @author ruKyzhc
 *
 */
public class UrlStatus {
	//fetching status
	public final static int FETCHED = 0x1;
	public final static int DISCARD = 0x2;
	public final static int MISSING = 0x3;
	
	public static int setFetchingStatus(int status, int fetching) {
		return status & (~0x7) + fetching;
	}
	
	public static int fetchingStatus(int status) {
		return status & 0x7;
	}
	
	//http status
	public static int httpCode(int status) {
		return ((status & 0x1ff8) >> 3) & 0x3ff;
	}
	
	public static int setHttpCode(int status, int code) {
		return status & (~0x1ff8) + (code & 0x3ff) << 3;
	}
}
