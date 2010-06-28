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
	
//	private static final double bWeight = 0.9;
//	private static final double aWeight = 0.1;
//	private static final double cWeight = 0.2;
//	private static final double oWeight = 0.01;
	private static final double w = 0.5;
	
	private TreeSet<ForumUrl> boardSet = null;
	private TreeSet<ForumUrl> articleSet = null;
//	private TreeSet<ForumUrl> catalogSet = null;
	
	private Queue<Url> instantQueue = null;
//	private Queue<Url> otherQueue = null;
	
	private int maxSize = 8192;	
	private Random rand = null;
	
	public ForumFrontier() {
		boardSet   = new TreeSet<ForumUrl>();
		articleSet = new TreeSet<ForumUrl>();
//		catalogSet = new TreeSet<ForumUrl>();
		instantQueue = new ArrayBlockingQueue<Url>(maxSize);
//		otherQueue = new ArrayBlockingQueue<Url>(maxSize);
		
		rand = new Random(System.currentTimeMillis());
	}

	@Override
	public synchronized Url next() {
		/*
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
			ret = catalogSet.pollFirst();
		} else {
			ret = otherQueue.poll();
		}
		return ret;
		*/
		int as = articleSet.size();
		int bs = boardSet.size();
		int is = instantQueue.size();
		
		Url ret = null;
		if(is != 0) {
			ret = instantQueue.poll();
		} else {
			double random = rand.nextDouble();
			double t = (bs * w) / (bs * w + as * (1 - w));
			if(random < t) {
				ret = boardSet.pollFirst();
			} else {
				ret = articleSet.pollFirst();
			}
		}
		return ret;
	}

	@Override
	public synchronized void schedule(Url url) {
		/*
		if(url instanceof ForumUrl) {
			ForumUrl u = (ForumUrl)url;
			if(u.getTag() == Tag.BOARD) {
				boardSet.add(u);
				return;
			} else if(u.getTag() == Tag.ARTICLE) {
				articleSet.add(u);
				return;
			} else if(u.getTag() == Tag.CATALOG) {
				catalogSet.add(u);
				return;
			}
		}
		otherQueue.add(url);
		*/
		if(url instanceof ForumUrl) {
			ForumUrl u = (ForumUrl)url;
			if(u.getTag() == Tag.BOARD) {
				if(u.getForumInfo() != null &&
						u.getForumInfo().getPage() == -1 &&
						u.getParameters().size() <= 1) {
					instantQueue.add(u);
				} else {
					boardSet.add(u);
				}
				return;
			} else if(u.getTag() == Tag.ARTICLE) {
				articleSet.add(u);
				return;
			} else {
				instantQueue.add(u);
				return;
			}
		}
		instantQueue.add(url);
	}

	@Override
	public int size() {
		return 
		boardSet.size() + 
		articleSet.size() + 
//		catalogSet.size() + 
//		otherQueue.size();
		instantQueue.size();
	}
	
	public String toString() {
//		return 
//		"[c:" + catalogSet.size() + 
//		"][b:" + boardSet.size() +
//		"][a:" + articleSet.size() +
//		"][o:" + otherQueue.size() + "]";
		return
		"[i:" + instantQueue.size() +
		"][b:" + boardSet.size() +
		"][a:" + articleSet.size() + "]";
	}

}
