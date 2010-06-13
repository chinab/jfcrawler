package org.thuir.forum.data;

import java.util.HashMap;
import java.util.Map;

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
	
	public void synchronize(Identity identity) {
		UrlItem ref = null;
		String value = null;
		
		for(UrlItem item : map.keySet()) {
			if(((ref = item.getRef()) != null) && 
					((value = identity.get(ref)) != null))
				map.put(item, value);
		}
		
		for(UrlItem item : identity.map.keySet()) {
			if(((ref = item.getRef()) != null) && 
					((value = this.get(ref)) != null))
				identity.map.put(item, value);
		}
	}
	
	public void put(UrlItem ref, String value) {
		this.map.put(ref, value);
	}
	
	public String get(UrlItem ref) {
		return map.get(ref);
	}
}
