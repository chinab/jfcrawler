package org.thuir.forum.template;

import org.thuir.forum.data.Identity;
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

	@Override
	public Identity getIdentity(Url url) {
		return null;
	}

	@Override
	public boolean match(String uri) {
		return false;
	}

}
