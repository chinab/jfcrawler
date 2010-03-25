package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractFilter implements IFilter {

	@Override
	public abstract boolean shouldVisit(PageUrl url);

}
