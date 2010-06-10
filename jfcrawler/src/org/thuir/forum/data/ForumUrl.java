package org.thuir.forum.data;

import org.apache.log4j.Logger;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

public final class ForumUrl extends Url {
	private static Logger logger = Logger.getLogger(ForumUrl.class);
	private static final long serialVersionUID = -8806556896198541103L;
	
	public static void registerToUrlFactory() {
		Url.setUrlClass(ForumUrl.class);
	}
	
	private Identity identity = null;
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
	public Identity getIdentity() {
		return this.identity;
	}
	public void setIdentityFromParent(Identity parent) {
		this.identity = parent.toChild(this);
	}
	
	public ForumUrl() throws BadUrlFormatException {
		super();
	}
	
	public ForumUrl(Url url) {
		
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
