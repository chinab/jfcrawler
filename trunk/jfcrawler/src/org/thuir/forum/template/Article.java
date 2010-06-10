package org.thuir.forum.template;

import org.thuir.forum.data.ArticleIdentity;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Article extends Vertex {

	public Article(Element e) {
		super(e);
		tag = Tag.ARTICLE;		
	}

	@Override
	public Identity identify(Url url) {
		return new ArticleIdentity();
	}

	@Override
	public Tag checkOutlink(ForumUrl u) {
		return null;
	}
	
}
