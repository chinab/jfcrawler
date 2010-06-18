package org.thuir.forum.frontier;

import java.util.Queue;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.frontier.Frontier;

/**
 * @author ruKyzhc
 *
 */
public class ForumFrontier extends Frontier {
	private static Logger logger = Logger.getLogger(ForumFrontier.class);
	
	private static final double bWeight = 0.9;
	private static final double aWeight = 0.1;
	private static final double cWeight = 0.2;
	private static final double oWeight = 0.01;
	
	private TreeSet<ForumUrl> boardSet = null;
	private TreeSet<ForumUrl> articleSet = null;
	private TreeSet<ForumUrl> catalogSet = null;
	
	private Queue<Url> otherQueue = null;
	private int maxSize = 0;	
	private Random rand = null;
	
	public ForumFrontier() {
		boardSet   = new TreeSet<ForumUrl>();
		articleSet = new TreeSet<ForumUrl>();
		catalogSet = new TreeSet<ForumUrl>();
		otherQueue = new ArrayBlockingQueue<Url>(maxSize);
		
		rand = new Random(System.currentTimeMillis());
	}

	@Override
	public synchronized Url next() {
		
		double bW = bWeight * boardSet.size();
		double aW = aWeight * articleSet.size();
		double cW = cWeight * catalogSet.size();		
		double oW = oWeight * otherQueue.size();
		
		double total = bW + aW + cW + oW;
		
		aW = aW / total;
		bW = bW / total;
		cW = cW / total;
		oW = oW / total;
		
		double aT = aW;
		double bT = bW + aW;
		double cT = bW + aW + cW;
		
		double random = rand.nextDouble();
		Url ret = null;
		if(random < aT) {
			ret = articleSet.pollFirst();
		} else if(random < bT) {
			ret = boardSet.pollFirst();
		} else if(random < cT) {
			catalogSet.pollFirst();
		} else {
			ret = otherQueue.poll();
		}
		return ret;
	}

	@Override
	public synchronized void schedule(Url url) {
		if(url instanceof ForumUrl) {
			ForumUrl u = (ForumUrl)url;
			if(u.getTag() == Tag.BOARD) {
				boardSet.add(u);
				return;
			} else if(u.getTag() == Tag.ARTICLE) {
				articleSet.add(u);
				return;
			}
		}
		otherQueue.add(url);
	}

	@Override
	public int size() {
		return 
		boardSet.size() + 
		articleSet.size() + 
		catalogSet.size() + 
		otherQueue.size();
	}

}
