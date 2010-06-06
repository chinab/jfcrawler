package org.thuir.forum.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class Vertex {
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
}
