package org.thuir.jfcrawler.framework.cache;

import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public class BlockingQueueCache extends Cache {

	private ArrayBlockingQueue<Page> queue = null;
	private final static int size = 512;
	
	public BlockingQueueCache() {
		queue = new ArrayBlockingQueue<Page>(size);
	}
	
	@Override
	public synchronized boolean offer(Page page) {
		return queue.offer(page);
	}

	@Override
	public synchronized Page poll() {
		return queue.poll();
	}

}
