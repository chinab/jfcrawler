package org.thuir.jfcrawler.framework.extractor;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractExtractor implements IExtractor {

	@Override
	public abstract PageUrl[] extractUrls(Page page);

}
