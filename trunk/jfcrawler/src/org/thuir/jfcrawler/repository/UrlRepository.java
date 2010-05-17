package org.thuir.jfcrawler.repository;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class UrlRepository {
	
	public abstract void initialize();
	
	public abstract boolean check(Url url);
	
	public abstract void submit(Url url);

	public abstract void close();
	
}
