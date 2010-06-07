package org.thuir.forum.data;

import org.apache.log4j.Logger;
import org.thuir.forum.template.Tag;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

public class ForumUrl extends Url {
	private static Logger logger = Logger.getLogger(ForumUrl.class);
	private static final long serialVersionUID = -8806556896198541103L;
	
	public static void registerToUrlFactory() {
		Url.setUrlClass(ForumUrl.class);
	}
	
	public static ForumUrl parseToTag(String url) throws BadUrlFormatException {
		ForumUrl u = (ForumUrl) Url.parse(url);
		
		Template tmpl = 
			TemplateRepository.getInstance().getTemplate(u.getHost());
		Vertex v = null;
		if((v = tmpl.predict(u))==null)
			u.setTag(Tag.UNKNOWN);
		else
			u.setTag(v.getTag());
		return u;
	}
	
	public ForumUrl() throws BadUrlFormatException {
		super();
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
