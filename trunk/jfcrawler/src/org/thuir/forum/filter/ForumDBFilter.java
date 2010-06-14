package org.thuir.forum.filter;

import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.filter.Filter;

/**
 * @author ruKyzhc
 *
 */
public class ForumDBFilter extends Filter {

	@Override
	public boolean shouldVisit(Url url) {
		return false;
	}

}
