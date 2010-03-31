package org.thuir.jfcrawler.framework.cache;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public abstract class Cache {

	public abstract boolean offer(Page page);
	
	public abstract Page poll();

}
