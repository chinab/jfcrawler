package org.thuir.jfcrawler.framework.scheduler;

import org.thuir.jfcrawler.framework.frontier.AbstractFrontier;

/**
 * @author ruKyzhc
 *
 */
public abstract class AbstractScheduler implements IScheduler {
	
	protected AbstractFrontier frontier = null;
	
	public void setFrontier(AbstractFrontier frontier) {
		this.frontier = frontier;
	}

}
