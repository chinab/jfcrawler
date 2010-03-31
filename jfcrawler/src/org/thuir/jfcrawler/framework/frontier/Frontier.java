package org.thuir.jfcrawler.framework.frontier;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class Frontier {
	
	public abstract void schedule(PageUrl url);
	
	public abstract PageUrl next();
}
