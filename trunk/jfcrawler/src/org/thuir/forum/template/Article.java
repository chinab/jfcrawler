package org.thuir.forum.template;

import org.thuir.forum.data.ForumUrl;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Article extends Vertex {

	public Article(Element e) {
		super(e);
		tag = Tag.ARTICLE;
		
		metaId = new ArticleMetaIdentity();
		metaId.fill(pattern.items);
	}

	@Override
	public Tag checkOutlink(ForumUrl u) {
		return null;
	}
	
	public static class ArticleMetaIdentity extends MetaIdentity {
		
	}
}
