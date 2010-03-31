package org.thuir.jfcrawler.io.nio;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public interface FetchingListener {

	public void onFetchingFinish(Page page);

}
