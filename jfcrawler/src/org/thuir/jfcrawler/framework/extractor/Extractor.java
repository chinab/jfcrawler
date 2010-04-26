package org.thuir.jfcrawler.framework.extractor;

import java.util.List;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class Extractor {

	public abstract List<Url> extractUrls(Page page);

}
