package org.thuir.jfcrawler.framework.frontier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class BlockingQueueFrontier extends Frontier {
	private ArrayBlockingQueue<Url> queue = null;
	private Set<String> set = null;
	
	private final static int size = 65536;
	
	public BlockingQueueFrontier() {
		set = Collections.synchronizedSet(new HashSet<String>());
		queue = new ArrayBlockingQueue<Url>(size);
	}

	@Override
	public synchronized Url next() {
		Url url = queue.poll();
		if(url != null)
			set.remove(url.getUrl());
		return url;
	}

	@Override
	public synchronized void schedule(Url url) {
		if(url==null || set.contains(url.getUrl()))
			return;
		queue.offer(url);
		set.add(url.getUrl());
	}

}
