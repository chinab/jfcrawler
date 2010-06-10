package org.thuir.forum.template;

import org.thuir.forum.data.ForumUrl;
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

	@Override
	public Tag checkOutlink(ForumUrl u) {
		return null;
	}
	
	
}
