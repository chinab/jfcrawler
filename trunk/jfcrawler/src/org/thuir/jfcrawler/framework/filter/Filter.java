package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class Filter {

	public abstract boolean shouldVisit(PageUrl url);

}