package org.thuir.jfcrawler.util.stat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ruKyzhc
 *
 */
public class Statistic {
	protected static Map<String, Statistic> statRepository =
		new HashMap<String, Statistic>();

	public static Statistic create(String key) {
		if(statRepository.containsKey(key))
			return statRepository.get(key);

		Statistic stat = new Statistic(key);
		statRepository.put(key, stat);

		return stat;			
	}
	
	public static Statistic get(String key) {
		return statRepository.get(key); 
	}

	protected long counter = 0l;
	protected String key = null;

	protected Statistic(String key) {
		this.counter = 0l;
		this.key = key;
	}

	public synchronized void reset() {
		counter = 0l;
	}

	public synchronized void inc(long inc) {
		counter += inc;
	}
	
	public synchronized void inc() {
		counter ++;
	}

	public long count() {
		return counter;
	}

}
