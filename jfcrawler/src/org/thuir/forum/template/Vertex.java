package org.thuir.forum.template;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.forum.template.UrlPattern.UrlItem;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public abstract class Vertex {
	private static final Logger logger = Logger.getLogger(Vertex.class);
	
	public static enum Tag {
		CATALOG,
		BOARD,
		ARTICLE,
		OTHER
	}
	
	public static Vertex load(Tag tag, Element e) {
		switch(tag) {
		case CATALOG:
			return new Catalog(e);
		case BOARD:
			return new Board(e);
		case ARTICLE:
			return new Article(e);
		default:
			return new Other();
		}
	}
	
	protected Tag tag = null;
	
//	protected final static String DefaultScriptSrcExpr = "//SCRIPT[@src]/attribute::src";
	protected final static String DefaultScriptExpr = "//SCRIPT/text()";
	protected XPathExpression scriptExpr = null;
	
//	protected List<UrlPattern> patterns = null;
	protected UrlPattern pattern = null;
	protected MetaIdentity metaId = null;
	
	public Vertex(Element e) {
		if(e == null)
			return;

//		patterns = new ArrayList<UrlPattern>();

		//script expression
		Element script = (Element)e.getElementsByTagName("script").item(0);
		String xpathExpr = script.getAttribute("xpath");
		try {
			if(xpathExpr == null || xpathExpr.length() == 0) {
				scriptExpr = Factory.getXPathExpression(DefaultScriptExpr);
			} else {
				scriptExpr = Factory.getXPathExpression(xpathExpr);
			}
		} catch (XPathExpressionException e1) {
			logger.error("error while compiling script xpath.", e1);
		}
		
		//pattern
//		NodeList list = e.getElementsByTagName("pattern");
//		for(int i = 0; i < list.getLength(); i++) {
//			patterns.add(UrlPattern.getInstance((Element)list.item(i)));
//		}
		pattern = UrlPattern.getInstance(
				(Element)e.getElementsByTagName("pattern").item(0));
	}

	public boolean match(Url url) {
//		for(UrlPattern p : patterns) {
//			if(!p.match(url.getUri())) {
//				return false;
//			}
//		}
//		return true;
		return pattern.match(url.getUri());
	}

	public Tag getTag() {
		return tag;
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
	
	public XPathExpression getScriptExpr() {
		return scriptExpr;
	}
	
	public Identity identify(Url url) {
//		Identity ret = null;
//		for(UrlPattern p : patterns) {
//			if(p.match(url.getUri())) {
//				ret = p.getIdentity(url);
//				ret.setMeta(this.metaId);
//			}
//		}
//		return ret;
		Identity ret = pattern.getIdentity(url);
		if(ret != null)
			ret.setMeta(metaId);
		return ret;
	}
	
	public abstract Tag checkOutlink(ForumUrl u);
	
	public static abstract class MetaIdentity {
		protected List<UrlItem> keys = null;
		
		public MetaIdentity() {
			keys = new ArrayList<UrlItem>();
		}
		
		public void fill(List<UrlItem> keys) {
			this.keys.addAll(keys);
		}
	}
}
