package org.thuir.forum.template;

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

	public static class BoardMetaIdentity extends MetaIdentity {

	}
}
