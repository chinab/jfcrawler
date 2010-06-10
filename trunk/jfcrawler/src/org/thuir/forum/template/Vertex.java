package org.thuir.forum.template;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public abstract class Vertex {
	protected static XPath xpath = XPathFactory.newInstance().newXPath();
	
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
	
	protected final static String DefaultScriptSrcExpr = "//SCRIPT[@src]/attribute::src";
	protected final static String DefaultScriptExpr = "//SCRIPT/text()";
	protected XPathExpression scriptExpr = null;
//	protected XPathExpression scriptSrcExpr = null;
	
	protected List<UrlPattern> patterns = null;
	protected Paging paging  = null;
	
	public Vertex(Element e) {
		if(e == null)
			return;
		int len = 0;
		NodeList list = null;
		
//		try {
//			scriptSrcExpr = xpath.compile(DefaultScriptSrcExpr);
//		} catch (XPathExpressionException e1) {
//			scriptSrcExpr = null;
//		}
		
		patterns = new ArrayList<UrlPattern>(4);
		list = e.getElementsByTagName("pattern");
		len = list.getLength();
		for(int i = 0; i < len; i++) {
			patterns.add(new UrlPattern((Element)list.item(i)));
		}
		
		list = e.getElementsByTagName("paging");
		len = list.getLength();
		if(len >= 1)
			paging = new Paging((Element)list.item(0));
	}

	public boolean match(Url url) {
		if(patterns != null) {
			for(UrlPattern p : patterns) {
				if(p.match(url.getUri()))
					return true;
			}
		}
		return false;
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
	
	public abstract Identity identify(Url url);
	
	public abstract Tag checkOutlink(ForumUrl u);

	/*
	private static Logger logger = Logger.getLogger(Vertex.class);
	private static XPath xpath = XPathFactory.newInstance().newXPath();
	
	public static void create(Map<String, Vertex> index, Element e) {
		Vertex v = new Vertex(e.getAttribute("id"));
		index.put(v.getId(), v);
	}
	public static void load(Map<String, Vertex> index, Element e) {
		String id = e.getAttribute("id");
		Vertex v = index.get(id);
		
		try {
			v.tag = Tag.valueOf(e.getAttribute("classification"));
		} catch(IllegalArgumentException exception) {
			v.tag = Tag.UNKNOWN;
		}
		
		String exprStr = e.getAttribute("xpath");
		if(exprStr.trim().length() == 0) {
			v.xpathExpr = null;
		} else {
			try {
				v.xpathExpr = xpath.compile(exprStr);
			} catch (XPathExpressionException e1) {
				logger.error("error xpath expression '" + exprStr + "'.", e1);
				v.xpathExpr = null;
			}
		}
		
		String scriptExpr = e.getAttribute("script");
		if(exprStr.trim().length() == 0) {
			v.scriptExpr = null;
		} else {
			try {
				v.scriptExpr = xpath.compile(scriptExpr);
			} catch (XPathExpressionException e1) {
				logger.error("error script xpath expression '" + scriptExpr + "'.", e1);
				v.scriptExpr = null;
			}
		}
		
		NodeList list = null;
		list = e.getElementsByTagName("pattern");
		for(int i = 0; i < list.getLength(); i++)
			v.patterns.add(new UrlPattern((Element)list.item(i)));
		
		list = e.getElementsByTagName("child");
		for(int i = 0; i < list.getLength(); i++) {
			Vertex child = 
				index.get(((Element)list.item(i)).getAttribute("ref"));
			v.children.add(child);
		}
		
		v.paging = 
			new Paging((Element)e.getElementsByTagName("paging").item(0));
	}
	
	private Tag tag = null;

	private List<Vertex> children = null;
	private List<UrlPattern> patterns = null;
	
	private XPathExpression xpathExpr = null;
	private XPathExpression scriptExpr = null;
	
	private Paging paging = null;
	private String id = null;

	public Vertex(String id) {
		children = new ArrayList<Vertex>();
		patterns = new ArrayList<UrlPattern>();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public List<Vertex> getChildren() {
		return children;
	}
	
	public List<UrlPattern> getPatterns() {
		return patterns;
	}
	
	public XPathExpression getXPathExpression() {
		return xpathExpr;
	}
	
	public XPathExpression getScriptExpression() {
		return scriptExpr;
	}
	
	public Paging getPaging() {
		return paging;
	}
	
	public Tag getTag() {
		return tag;
	}
	
	public boolean match(Url url) {
		for(UrlPattern p : patterns) {
			if(!p.match(url.getUri()))
				return false;
		}
		return true;
	}
	
	public Object[] getChildrenByTag(Tag tag) {
		List<Vertex> ret = new ArrayList<Vertex>();
		for(Vertex v : children) {
			if(v.tag == tag)
				ret.add(v);
		}
		return ret.toArray();
	}
	*/
}
