package org.thuir.forum.frontier;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.frontier.Frontier;

/**
 * @author ruKyzhc
 *
 */
public class ForumFrontier extends Frontier {
//	private static Logger logger = Logger.getLogger(ForumFrontier.class);
	
	private static final double U = 20.0; // a_size / b_size;
	
	private Queue<Url> instantQueue = null;
	private TriplePriorityQueue boardQueue = null;
	private TriplePriorityQueue articleQueue = null;
	
	private Random rand = null;
	
	public ForumFrontier() {
		instantQueue = new ArrayBlockingQueue<Url>(PriorityQueue.MAXSIZE);
		boardQueue   = new TriplePriorityQueue(0.3, 0.125);
		articleQueue = new TriplePriorityQueue(0.0001, 0.001);
		
		rand = new Random(System.currentTimeMillis());
	}

	private double pI = 0.9;
	private double pB = 0.5;
	private double pA = 0.5;
	
	private void normalized() {
		int bs = 0;
		int as = 0;
		if((bs = boardQueue.size()) == 0) {
			pA = 1;
			pB = 0;
			return;
		}
		if((as = articleQueue.size()) == 0) {
			pA = 0;
			pB = 1;
			return;
		}
		double u = as / bs;
		double delta = (u - U) / U;
		if(delta > 0) {
			pB = pB + delta * delta;
		} else {
			pA = pA + delta * delta;			
		}
		
		double total = pA + pB;
		pA = pA / total;
		pB = pB / total;
	}
	
	@Override
	public synchronized Url next() {
		double random = rand.nextDouble();
		
		double total = pA + pB;
		pA = pA / total;
		pB = pB / total;
		
		double _pA = pA * (1 - pI);
		double _pI = pI;
		
		double _tI = _pI;
		double _tA = _pI + _pA;
		
		Url ret = null;
		if(random < _tI && instantQueue.size() != 0) {
			ret = instantQueue.poll();
		} else if(random < _tA && articleQueue.size() != 0) {
			ret = articleQueue.poll();
		} else {
			ret = boardQueue.poll();
		}
		
		normalized();
		return ret;
	}

	@Override
	public synchronized void schedule(Url url) {
		if(url instanceof ForumUrl) {
			ForumUrl u = (ForumUrl)url;
//			if(u.getTag() == Tag.BOARD) {
//				if(u.getForumInfo() != null &&
//						u.getForumInfo().getPage() == -1 &&
//						u.getParameters().size() <= 1) {
//					instantQueue.add(u);
//				} else {
//					boardSet.add(u);
//				}
//				return;
//			} else if(u.getTag() == Tag.ARTICLE) {
//				articleSet.add(u);
//				return;
//			} else {
//				instantQueue.add(u);
//				return;
//			}
			if(u.isInstant()) {
				instantQueue.add(u);
				return;
			}
			double t = 0.0, temp = 1.0;
			if(u.getForumInfo() == null) {
				instantQueue.add(u);
			}
			if(u.getTag() == Tag.BOARD) {
				temp = u.getForumInfo().getPage();
				if(temp <= 0)
					t = 0.0;
				else
					t = 1.0 / temp;
				boardQueue.offer(url, t);
			} else if(u.getTag() == Tag.ARTICLE) {
				temp = u.getForumInfo().getId();
				if(temp <= 0)
					t = 0.0;
				else
					t = 1.0 / temp;
				articleQueue.offer(url, t);
			} else {
				instantQueue.add(u);
			}
			
		}
		instantQueue.add(url);
	}

	@Override
	public int size() {
		return 
		articleQueue.size() + 
		boardQueue.size() + 
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
		"][b:" + boardQueue.size() +
		"][a:" + articleQueue.size() + "]";
	}

	public static abstract class PriorityQueue {
		protected static final int MAXSIZE = 65536;
		
		public abstract void offer(Url url, double threshold);
		public abstract Url  poll();
		public abstract int  size();
	}
	
	public static class TriplePriorityQueue extends PriorityQueue {
		private static final double lP = 0.1;
		private static final double mP = 0.4;
//		private static final double hP = 1.0;
		
		private Queue<Url> lowQueue = new ArrayBlockingQueue<Url>(MAXSIZE);
		private Queue<Url> mediumQueue = new ArrayBlockingQueue<Url>(MAXSIZE);
		private Queue<Url> highQueue = new ArrayBlockingQueue<Url>(MAXSIZE);
		
		private Random rand = new Random(System.currentTimeMillis());
		
		private double lT = 0.1;
		private double hT = 0.01;
		
		public TriplePriorityQueue(double lT, double hT) {
			this.lT = lT;
			this.hT = hT;
		}

		@Override
		public void offer(Url url, double threshold) {
			if(threshold > lT) {
				lowQueue.offer(url);
			} else if(threshold < hT) {
				highQueue.offer(url);
			} else {
				mediumQueue.offer(url);
			}
		}

		@Override
		public Url poll() {
			Url ret = null;
			
			double random = rand.nextDouble();
			if(random > mP && highQueue.size() != 0) {
				ret = highQueue.poll();
			} else if(random > lP && mediumQueue.size() != 0) {
				ret = mediumQueue.poll();
			} else {
				ret = lowQueue.poll();
			}
						
			return ret;
		}
		
		@Override
		public int size() {
			return 
			lowQueue.size() +
			mediumQueue.size() +
			highQueue.size();
		}
		
		@Override
		public String toString() {
			return 
			"[low:" + lowQueue.size() + 
			"][medium:" + mediumQueue.size() +
			"][high:" + highQueue.size() + "]";
		}
	}
}
