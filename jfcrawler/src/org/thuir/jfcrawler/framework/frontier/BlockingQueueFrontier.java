package org.thuir.jfcrawler.framework.frontier;

import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class BlockingQueueFrontier extends Frontier {
	private ArrayBlockingQueue<Url> queue = null;
	
	private final static int size = 65536;
	
	public BlockingQueueFrontier() {
		queue = new ArrayBlockingQueue<Url>(size);
	}

	@Override
	public synchronized Url next() {
		return queue.poll();
	}

	@Override
	public synchronized void schedule(Url url) {
		queue.offer(url);
	}

}
