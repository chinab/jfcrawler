package org.thuir.jfcrawler.framework.filter;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class HostFilter extends Filter {
	private String host = null;
	
	public void setHost(String host) throws BadUrlFormatException {
		this.host = Url.parse(host).getHost();;
	}

	@Override
	public boolean shouldVisit(Url url) {
		if(host == null)
			return false;
		else if(url.getHost().equals(host)) 
			return true;
		else
			return false;
	}
	
}
