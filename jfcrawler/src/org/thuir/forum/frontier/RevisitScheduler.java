package org.thuir.forum.frontier;

import java.util.List;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.database.ForumInfoDatabase;
import org.thuir.forum.util.ForumConfigUtil;
import org.thuir.jfcrawler.framework.Factory;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.util.BasicThread;

public class RevisitScheduler extends BasicThread {
	private static Logger logger = Logger.getLogger(RevisitScheduler.class);
	
	private long boardRevisitInterval = 
		ForumConfigUtil.getForumConfig().getLong("revisit-interval");
	private ForumInfoDatabase infoDb = 
		(ForumInfoDatabase)Factory.getModule(ForumInfoDatabase.NAME);
	private Frontier frontier = Factory.getFrontierInstance();
	
	public RevisitScheduler() {
		this.setName("revisit-scheduler");
	}
	
	@Override
	public void run() {
		while(alive()) {
			try {
				Thread.sleep(boardRevisitInterval);
			} catch (InterruptedException e) {
				continue;
			}
			
			List<ForumUrl> urls = infoDb.revisit();
			for(ForumUrl url : urls) {
				frontier.schedule(url);
			}

			System.err.println("reschedule " + urls.size() + " urls");
			logger.info("reschedule " + urls.size() + " urls");
		}
	}

}
