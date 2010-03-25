package org.thuir.jfcrawler.framework.extractor;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractExtractor implements IExtractor {

	@Override
	public abstract Object[] extractUrls(Page page);

}
