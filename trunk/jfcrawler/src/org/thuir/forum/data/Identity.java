package org.thuir.forum.data;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public abstract class Identity {
	protected int id = 0;
	protected int page = 0;

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	public int getPage() {
		return this.page;
	}
	
	public abstract ForumUrl generateUrl();
	
	public abstract Identity toChild(Url url);
}
