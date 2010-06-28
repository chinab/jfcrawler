package org.thuir.forum.template;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RegexUrlPattern extends UrlPattern {
	private static Logger logger = Logger.getLogger(RegexUrlPattern.class);
	
	protected Pattern pattern = null;
	
	protected RegexUrlPattern(Element e) {
		super(e);

		StringBuffer buf = new StringBuffer(e.getAttribute("regex"));
		try {
			NodeList list = (NodeList)Factory.evaluateXPath(
					"./regex-item[@key]", e, XPathConstants.NODESET);
			for(int i = 0; i < list.getLength(); i ++) {
				RegexItem item = new RegexItem((Element)list.item(i));
				item.replaceRegex(buf);
				items.add(item);
			}
			
//			for(UrlItem i : items) {
//				if(i.refStr != null)
//					i.ref = UrlItem.itemLib.get(i.refStr);
//			}
			
			pattern = Pattern.compile(buf.toString());
		} catch (XPathExpressionException e1) {
			logger.error("error while compiling xpath", e1);
		}
		
	}

//	@Override
//	public Identity getIdentity(Url url) {
//		Matcher matcher = pattern.matcher(url.getUri());
//		int k = 0;
//		
//		Identity ret = new Identity();
//		if(matcher.find()) {
//			for(UrlItem i : items) {
//				RegexItem r = (RegexItem)i;
//				k += r.ignore + 1;
//
//				ret.put(r, matcher.group(k));
//			}
//		} else {
//			return null;
//		}
//		return ret;
//	}
	
	@Override
	public Map<UrlItem, String> extractItem(Url url) {
		Map<UrlItem, String> ret = new HashMap<UrlItem, String>();
		Matcher matcher = pattern.matcher(url.getUri());
		int k = 0;
		
		if(matcher.find()) {
			for(UrlItem i : items) {
				RegexItem r = (RegexItem)i;
				k += r.skip + 1;

				ret.put(r, matcher.group(k));
			}
		}
		return ret;
	}

	@Override
	public boolean match(String uri) {
		return pattern.matcher(uri).matches();
	}
	
	public static class RegexItem extends UrlItem {
		private String regexStr = null;
		private int skip = 0;
		
		public RegexItem(Element e) {
			super(e);
//			if(refStr != null) 
//				return;

			regexStr = e.getAttribute("regex");
			try {
				skip = Integer.parseInt(e.getAttribute("skip"));
			} catch(Exception e1) {
				skip = 0;
			}
		}
		
		public void replaceRegex(StringBuffer buf) {
			String token = "${" + key + "}";
			String regex = "(" + regexStr + ")";
			
			int p = buf.indexOf(token);
			buf.replace(p, p + token.length(), regex);
		}
		
//		public int getIgnore() {
//			return ignore;
//		}
		
	}

}
