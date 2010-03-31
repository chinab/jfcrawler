package org.thuir.jfcrawler.impl;

import java.util.ArrayList;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.extractor.AbstractExtractor;

/**
 * @author ruKyzhc
 *
 */
public class DefaultHTMLExtractor extends AbstractExtractor {
	private ArrayList<PageUrl> urls = null;
	
	public DefaultHTMLExtractor() {
		urls = new ArrayList<PageUrl>();
	}

	@Override
	public PageUrl[] extractUrls(Page page) {
		PageUrl[] pageUrls = new PageUrl[urls.size()];
		urls.toArray(pageUrls);
		return pageUrls;
	}

}
