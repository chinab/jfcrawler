package org.thuir.forum.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public abstract class UrlPattern {
	public static enum Type {
		REGEX,
		TOKEN,
		NONE,
		UNKNOWN,
	}
	
	public static UrlPattern getInstance(Element e) {
		Type type = Type.UNKNOWN;
		try {
			type = Type.valueOf(e.getAttribute("type").toUpperCase());
		} catch(Exception e1) {
		}
		
		switch(type) {
		case REGEX:
			return new RegexUrlPattern(e);
		case TOKEN:
			return new TokenUrlPattern(e);
		default:
			return new DefaultUrlPattern(e);
		}
	}
	
	protected Type type = Type.UNKNOWN;
	
	protected List<UrlItem> items = null;
	
	protected UrlPattern(Element e) {	
		items = new ArrayList<UrlItem>();
	}
	
	public List<UrlItem> getUrlItems() {
		return items;
	}

	public abstract Identity getIdentity(Url url);
	
	public abstract boolean match(String uri);
	
	public static abstract class UrlItem {
//		private static Logger logger = Logger.getLogger(UrlItem.class);
		
		protected static Map<String, UrlItem> itemLib = new HashMap<String, UrlItem>();
		public static void resolveRef() {
			for(UrlItem u : itemLib.values()) {
				if(u.refStr != null)
					u.ref = itemLib.get(u.refStr);
			}
		}
		
		protected String id = null;
		protected String defaultValue = "";
		
		protected String refStr = null;
		protected UrlItem ref = null;
		
		protected UrlItem(Element e) {
			id = e.getAttribute("id");
			itemLib.put(id, this);
			
			defaultValue = e.getAttribute("default");
			
			refStr = e.getAttribute("ref");
			if(refStr.length() == 0) {
				refStr = null;
			}
		}
		
		public String getId() {
			return id;
		}
		
		public UrlItem getRef() {
			return ref;
		}
		
		@Override
		public String toString() {
			return id;
		}
	}
}
