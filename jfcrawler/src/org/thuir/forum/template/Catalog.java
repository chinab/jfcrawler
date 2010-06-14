package org.thuir.forum.template;

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

//	@Override
//	public Identity identify(Url url) {
//		return null;
//	}
	
}
