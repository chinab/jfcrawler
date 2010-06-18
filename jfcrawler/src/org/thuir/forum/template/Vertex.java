package org.thuir.forum.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.forum.data.Info;
import org.thuir.forum.template.UrlPattern.UrlItem;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
	
	protected final static String DefaultScriptExpr = "//SCRIPT/text()";
	protected XPathExpression scriptExpr = null;
	
	protected UrlPattern pattern = null;
	protected InfoFactory infoFactory = null;
	protected boolean     hasOutlink  = true;
	
	public Vertex(Element e) {
		outlinks = new ArrayList<Vertex>();
		paging   = Paging.NONE;

		if(e == null) {
			return;
		}
		
		try {
			String outlinkStr = e.getAttribute("outlink");
			if(outlinkStr.length() == 0) {
				hasOutlink = true;
			} else {
				hasOutlink = Boolean.parseBoolean(outlinkStr);
			}
		} catch(Exception e1) {
			logger.error("invalid value", e1);
		}
		
		//script expression
		NodeList scriptNodes = e.getElementsByTagName("script");
		if(scriptNodes.getLength() != 0) {
			Element script = (Element)scriptNodes.item(0);
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
		} else {
			scriptExpr = null;
		}

		//pattern
		pattern = UrlPattern.getInstance(
				(Element)e.getElementsByTagName("pattern").item(0));
		try {
			paging = Paging.valueOf(e.getAttribute("paging").toUpperCase());
		} catch(Exception e1) {
			paging = Paging.NONE;
		}
	}

	public boolean hasOutlink() {
		return hasOutlink;
	}
	public boolean match(Url url) {
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
	
	public Info getUrlInfo(Url url) {
		if(infoFactory == null) {
			return null;
		} else {
			Info ret = infoFactory.extractInfo(pattern.extractItem(url));
			infoFactory.evaluate(paging, ret);
			return ret;
		}
	}
	
	public Tag checkOutlink(Url url) {
		for(Vertex outlink : outlinks) {
			if(outlink.match(url))
				return outlink.tag;
		}
		
		switch(paging) {
		case CATALOG:
		case PAGING:
		case NEXT: {
			if(this.match(url))
				return this.tag;
		};break;
		case NONE:
		default:
			return null;
		}
		
		return null;
	}
	
	public static enum Paging {
		NONE,
		
		CATALOG,
		PAGING,
		
		NEXT,
		PREV,
	}
	
	protected List<Vertex> outlinks = null;
	protected Paging paging = Paging.NONE;
	
	public void addOutlinks(Vertex v) {
		outlinks.add(v);
	}
	
	public abstract static class InfoFactory {		
		protected static final int MAX_PAGING = 1000;
		
		protected UrlItem keyRef  = null;
		protected UrlItem idRef   = null;
		protected UrlItem pageRef = null;
		
		public abstract Info extractInfo(Map<UrlItem, String> values);
		
		public abstract void setReference(List<UrlItem> list);
		
		public void evaluate(Paging paging, Info info) {
			int page = info.getPage();
			if(page == -1) {
				info.evaluate(0.0);
				return;
			} else if(page <= 0) {
				info.evaluate(1.0);
				return;
			}
			
			switch(paging) {
			case PREV:
				info.evaluate(1.0 / page);
			case NEXT:
			case PAGING:
				info.evaluate(page / (double)MAX_PAGING);
			default:
				info.evaluate(0.0);
			}
		}
		
		protected boolean setParameters(Info info, Map<UrlItem, String> values) {
			String token = "";
			String value = null;
			try {
				if(idRef != null && (value = values.get(idRef)) != null) {
					info.setId(Long.parseLong(value));
					token += "[" + value + "]";
				}
				if(keyRef != null && (value = values.get(keyRef)) != null) {
					info.setKey(value);
					token += "[" + value + "]";
				}
				if(pageRef != null && (value = values.get(pageRef)) != null) {
					info.setPage(Integer.parseInt(value));
					token += "[" + value + "]";
				}
				if(token.length() == 0)
					return false;

				info.setToken(token);
			} catch(Exception e) {
				logger.error(e);
				return false;
			}
			return true;
		}
	}
}
