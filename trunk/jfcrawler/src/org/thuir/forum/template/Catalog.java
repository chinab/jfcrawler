package org.thuir.forum.template;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Catalog extends Vertex {

	public Catalog(Element e) {
		super(e);
		tag = Tag.CATALOG;
		
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
