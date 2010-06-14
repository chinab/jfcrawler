package org.thuir.forum.template;

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
	
	public static class ArticleMetaIdentity extends MetaIdentity {
		
	}
}
