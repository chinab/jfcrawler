package org.thuir.forum.template;

import org.thuir.forum.data.BoardIdentity;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Board extends Vertex {

	public Board(Element e) {
		super(e);
		tag = Tag.BOARD;
	}

	@Override
	public Identity identify(Url url) {
		return new BoardIdentity();
	}

	@Override
	public Tag checkOutlink(ForumUrl u) {
		return null;
	}
	
}
