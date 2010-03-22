package org.thuir.jfcrawler.framework.frontier;

import java.util.ArrayList;

import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public interface IFrontier {
	
	public PageUrl getNextUrl();
	
	public void scheduleNewUrls(ArrayList<PageUrl> urls);

}
