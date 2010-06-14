package org.thuir.forum.template;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class TokenUrlPattern extends UrlPattern {
	private static Logger logger = Logger.getLogger(TokenUrlPattern.class);
	
	private String token = null;
	
	protected TokenUrlPattern(Element e) {
		super(e);
		
		token = e.getAttribute("token");
		
		try {
			NodeList list = (NodeList)Factory.evaluateXPath(
					"./query-item[@key]", e, XPathConstants.NODESET);
			for(int i = 0; i < list.getLength(); i ++) {
				TokenItem item = new TokenItem((Element)list.item(i));
				items.add(item);
			}
		} catch (XPathExpressionException e1) {
			logger.error("error while compiling xpath", e1);
		}
	}

//	@Override
//	public Identity getIdentity(Url url) {
//		Identity ret = new Identity();
//		for(UrlItem i : items) {
////			String value = url.getParameter(((TokenItem)i).getQuery());
////			value = (value == null)?i.defaultValue:value;
////			ret.put(i, value);
//			ret.put(i, url.getParameter(((TokenItem)i).getQuery()));
//		}
//		return ret;
//	}
	
	@Override
	public boolean match(String uri) {
		return uri.contains(token);
	}
	
	public static class TokenItem extends UrlItem {
		private String query = null;
		
		public TokenItem(Element e) {
			super(e);
//			if(refStr != null) 
//				return;
			
			query = e.getAttribute("query");
		}
		
		public String getQuery() {
			return query;
		}
	}

	@Override
	public Map<UrlItem, String> extractItem(Url url) {
		Map<UrlItem, String> ret = new HashMap<UrlItem, String>();
		for(UrlItem i : items) {
			ret.put(i, url.getParameter(((TokenItem)i).getQuery()));
		}
		return ret;
	}

}
