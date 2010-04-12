package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class Filter {

	public abstract boolean shouldVisit(Url url);

}
