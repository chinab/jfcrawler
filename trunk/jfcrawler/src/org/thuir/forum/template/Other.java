package org.thuir.forum.template;

import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public final class Other extends Vertex {

	public Other() {
		super(null);
		tag = Tag.OTHER;
	}

	@Override
	public Identity identify(Url url) {
		return null;
	}	
	
}
