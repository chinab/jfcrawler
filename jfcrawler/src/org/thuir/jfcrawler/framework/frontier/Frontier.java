package org.thuir.jfcrawler.framework.frontier;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class Frontier {
	
	public abstract void schedule(Url url);
	
	public abstract Url next();
	
	public abstract void dump();
	
}
