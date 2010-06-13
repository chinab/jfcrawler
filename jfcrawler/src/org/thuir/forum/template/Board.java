package org.thuir.forum.template;

import org.thuir.forum.data.ForumUrl;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Board extends Vertex {

	public Board(Element e) {
		super(e);
		tag = Tag.BOARD;

		metaId = new BoardMetaIdentity();
		metaId.fill(pattern.items);
	}

	@Override
	public Tag checkOutlink(ForumUrl u) {
		return null;
	}

	public static class BoardMetaIdentity extends MetaIdentity {

	}
}
