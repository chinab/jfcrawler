package org.thuir.forum.data;

import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

public final class ForumUrl extends Url {
//	private static Logger logger = Logger.getLogger(ForumUrl.class);
	private static final long serialVersionUID = -8806556896198541103L;
	
	public static void registerToUrlFactory() {
		Url.setUrlClass(ForumUrl.class);
	}

	private boolean instant = false;
	
	public void setInstant(boolean i) {
		this.instant = i;
	}
	
	public boolean isInstant() {
		return this.instant;
	}
	
	@Override
	public int compareTo(Url other) {
		if(other == null) {
			return 1;
		}
		
		if(!(other instanceof ForumUrl)) {
			return super.compareTo(other);
		}
		
		ForumUrl u = (ForumUrl)other;
		if(forumInfo == null || u.forumInfo == null)
			return super.compareTo(other);
		
		return forumInfo.compareTo(u.forumInfo);
	}
	
	private Info forumInfo = null;
	public void setForumInfo(Info info) {
		this.forumInfo = info;
	}
	public Info getForumInfo() {
		return forumInfo;
	}
	
	public ForumUrl() throws BadUrlFormatException {
		super();
	}
	
	public ForumUrl(Url url) {
		this.clone(url);
	}
	
	private Tag inlinkTag = null;
	private Tag tag = null;
	
	public void setInlinkTag(Tag tag) {
		this.inlinkTag = tag;
	}
	public Tag getInlinkTag() {
		return inlinkTag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}
	public Tag getTag() {
		return tag;
	}

}
