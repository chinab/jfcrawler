package org.thuir.jfcrawler.impl;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.scheduler.AbstractScheduler;

/**
 * @author ruKyzhc
 *
 */
public class DefaultScheduler extends AbstractScheduler {

	protected Queue<PageUrl> queue = new ArrayBlockingQueue<PageUrl>(100);

	@Override
	public synchronized PageUrl getNextUrl() {
		return queue.poll();
	}

	@Override
	public synchronized void scheduleNewUrls(PageUrl[] urls) {
		for(PageUrl url : urls) {
			queue.offer(url);
		}

	}

	@Override
	public synchronized void scheduleNewUrl(PageUrl url) {
		queue.offer(url);
	}

}
