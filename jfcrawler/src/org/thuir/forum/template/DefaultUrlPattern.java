package org.thuir.forum.template;

import java.util.HashMap;
import java.util.Map;

import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public class DefaultUrlPattern extends UrlPattern {

	/**
	 * @param e
	 */
	protected DefaultUrlPattern(Element e) {
		super(e);
	}

//	@Override
//	public Identity getIdentity(Url url) {
//		return null;
//	}

	@Override
	public boolean match(String uri) {
		return false;
	}
	
	@Override
	public Map<UrlItem, String> extractItem(Url url) {
		return new HashMap<UrlItem, String>();
	}

}
