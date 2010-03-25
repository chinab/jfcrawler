package org.thuir.jfcrawler.framework.extractor;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public interface IExtractor {

	public Object[] extractUrls(Page page);
	
}

