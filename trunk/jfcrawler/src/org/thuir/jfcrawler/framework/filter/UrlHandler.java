package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class UrlHandler {

	public abstract boolean shouldVisit(PageUrl url);
	
	public abstract void process(PageUrl url);

}
