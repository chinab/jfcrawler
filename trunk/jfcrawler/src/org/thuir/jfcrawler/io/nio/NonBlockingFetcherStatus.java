package org.thuir.jfcrawler.io.nio;

/**
 * @author ruKyzhc
 *
 */
public enum NonBlockingFetcherStatus {
	UNKNOWN,
	
	INITIALIZING,
	CONNECTING,
	
	SENDING_EXCHANGE,
}
