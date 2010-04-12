package org.thuir.jfcrawler.framework.extractor;

import java.util.ArrayList;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class Extractor {

	public abstract ArrayList<Url> extractUrls(Page page);

}
