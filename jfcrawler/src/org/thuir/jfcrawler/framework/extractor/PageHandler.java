package org.thuir.jfcrawler.framework.extractor;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class PageHandler {

	public abstract PageUrl[] extractUrls(Page page);

}
