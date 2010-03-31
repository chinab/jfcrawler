package org.thuir.jfcrawler.framework.handler;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class PageHandler {

	public abstract PageUrl[] extractUrls(Page page);

}
