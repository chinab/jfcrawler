package org.thuir.forum.data;

import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public final class ArticleIdentity extends Identity {
	private int boardId = 0;
	private int position = 0;

	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public int getBoardId() {
		return this.boardId;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	public int getPosition() {
		return this.position;
	}

	@Override
	public ForumUrl generateUrl() {
		return null;
	}

	@Override
	public Identity toChild(Url url) {
		return null;
	}
}