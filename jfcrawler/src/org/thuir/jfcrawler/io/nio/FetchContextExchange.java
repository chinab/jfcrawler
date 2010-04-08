package org.thuir.jfcrawler.io.nio;

import java.io.IOException;

import org.eclipse.jetty.client.ContentExchange;
import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public class FetchContextExchange extends ContentExchange {
	private Page page = null;
	private FetchingListener listener = null;
	
	public FetchContextExchange(Page page) {
		super();
		this.page = page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void setFetchingListener(FetchingListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected void onResponseComplete() throws IOException {
		super.onResponseComplete();
		page.load(this.getResponseContentBytes());
		listener.onFetchingFinish(page);
	}
	
}
