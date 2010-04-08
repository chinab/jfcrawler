package org.thuir.jfcrawler.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AccessController {
	private Map<String, Long> accessLog
		= Collections.synchronizedMap(new HashMap<String, Long>());
	
	public void access(String host, long timeStamp) {
		accessLog.put(host, timeStamp);
	}
	
	public long lastAccess(String host) {
		Long temp = accessLog.get(host);
		return temp == null?0l:temp;
	}
}
