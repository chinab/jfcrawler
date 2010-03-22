package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public interface IFilter {
	
	public boolean shouldVisit(PageUrl url);

}
