package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public class HostFilter extends Filter {
	private String host = null;
	
	public void setHost(String host) throws BadUrlFormatException {
		this.host = PageUrl.parse(null, host).getHost();;
	}

	@Override
	public boolean shouldVisit(PageUrl url) {
		if(host == null)
			return false;
		else if(url.getHost().equals(host)) 
			return true;
		else
			return false;
	}
	
}
