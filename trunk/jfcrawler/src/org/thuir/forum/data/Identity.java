package org.thuir.forum.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.thuir.forum.template.UrlPattern.UrlItem;
import org.thuir.forum.template.Vertex.MetaIdentity;

/**
 * @author ruKyzhc
 *
 */
public final class Identity {
	protected Map<UrlItem, String> map = null;
	
	protected MetaIdentity meta = null;
	
	public Identity() {
		map = new HashMap<UrlItem, String>();
	}
	
	public void setMeta(MetaIdentity meta) {
		this.meta = meta;
	}
	
	public MetaIdentity getMeta() {
		return this.meta;
	}
	
//	public void synchronize(Identity identity) {
//		UrlItem ref = null;
//		String value = null;
//		
//		for(UrlItem item : map.keySet()) {
//			if(((ref = item.getRef()) != null) && 
//					((value = identity.get(ref)) != null))
//				map.put(item, value);
//		}
//		
//		for(UrlItem item : identity.map.keySet()) {
//			if(((ref = item.getRef()) != null) && 
//					((value = this.get(ref)) != null))
//				identity.map.put(item, value);
//		}
//	}
	
	public static void synchronize(Identity id1, Identity id2) {
		UrlItem ref = null;
		String  value = null;
		if(id1 == null || id2 == null)
			return;
		
		for(UrlItem item : id1.map.keySet()) {
			if(((ref = item.getRef()) != null) && 
					((value = id2.get(ref)) != null))
				id1.map.put(item, value);
		}
		
		for(UrlItem item : id2.map.keySet()) {
			if(((ref = item.getRef()) != null) && 
					((value = id1.get(ref)) != null))
				id2.map.put(item, value);
		}
	}
	
	public void put(UrlItem ref, String value) {
		this.map.put(ref, value);
	}
	
	public String get(UrlItem ref) {
		return map.get(ref);
	}
	
	@Override
	public String toString() {
		String ret = "{";
		for(Entry<UrlItem, String> entry : map.entrySet()) {
			ret += "[" + entry.getKey() + "][" + entry.getValue() + "],";
		}
		ret += "}";
		return ret;
	}
}
