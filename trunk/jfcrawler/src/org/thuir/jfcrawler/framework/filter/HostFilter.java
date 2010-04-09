package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public class HostFilter extends Filter {

	@Override
	public boolean shouldVisit(PageUrl url) {
		if(url.getHost().equals("www.discuz.net")) 
			return true;
		else
			return false;
	}
	
}
