package org.thuir.jfcrawler.framework.extractor;

import java.util.ArrayList;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public abstract class Extractor {

	public abstract ArrayList<PageUrl> extractUrls(Page page);

}
