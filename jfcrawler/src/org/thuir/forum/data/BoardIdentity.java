package org.thuir.forum.data;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public final class BoardIdentity extends Identity {
	private String name = "";
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	@Override
	public ForumUrl generateUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Identity toChild(Url url) {
		return null;
	}
}
