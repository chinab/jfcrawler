package org.thuir.jfcrawler.framework.frontier;

import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public class BlockingQueueFrontier extends Frontier {
	private ArrayBlockingQueue<PageUrl> queue = null;
	
	private final static int size = 1024;
	
	public BlockingQueueFrontier() {
		queue = new ArrayBlockingQueue<PageUrl>(size);
	}

	@Override
	public synchronized PageUrl next() {
		return queue.poll();
	}

	@Override
	public synchronized void schedule(PageUrl url) {
		queue.offer(url);
	}

}
