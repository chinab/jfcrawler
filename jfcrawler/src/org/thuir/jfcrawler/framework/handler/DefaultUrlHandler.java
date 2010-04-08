package org.thuir.jfcrawler.framework.handler;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public class DefaultUrlHandler extends UrlHandler {

	@Override
	public boolean shouldVisit(PageUrl url) {
		if(url.getHost().equals("www.discuz.net")) 
			return true;
		else
			return false;
	}

	@Override
	public void process(PageUrl url) {		
	}

}
