package org.thuir.forum.frontier;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.frontier.Frontier;

/**
 * @author ruKyzhc
 *
 */
public class ForumFrontier extends Frontier {
	private static Logger logger = Logger.getLogger(ForumFrontier.class);
	
	private ArrayBlockingQueue<Url> boardQueue = null;
	private ArrayBlockingQueue<Url> threadQueue = null;
	private ArrayBlockingQueue<Url> otherQueue = null;

	@Override
	public synchronized Url next() {
		return null;
	}

	@Override
	public synchronized void schedule(Url url) {
	}

	@Override
	public int size() {
		return boardQueue.size() + threadQueue.size() + otherQueue.size();
	}

}
