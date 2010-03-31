package org.thuir.jfcrawler.framework.scheduler;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public interface IScheduler {
	
	public PageUrl getNextUrl();
	
	public void scheduleNewUrl(PageUrl url);
	
	public void scheduleNewUrls(PageUrl[] urls);
	
}
