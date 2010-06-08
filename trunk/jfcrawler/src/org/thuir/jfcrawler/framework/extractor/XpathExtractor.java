package org.thuir.jfcrawler.framework.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 * @deprecated
 */
public class XpathExtractor extends HTMLExtractor {

	private XPathExpression expr = null;

	public void setXpathExpr(XPathExpression expr) {
		this.expr = expr;
	}

	@Override
	public List<Url> extractUrls(Page page) {
		List<Url> urls = new ArrayList<Url>();

		Document doc = parse(page);

		if(doc == null)
			return urls;

		NodeList nodes = null;
		if(expr == null) {
			nodes = doc.getElementsByTagName("a");
		} else {
			try {
				nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				nodes = doc.getElementsByTagName("a");
			}
		}
		String href = null;
		for(int i = 0; i < nodes.getLength(); i++) {
			Element n = (Element)nodes.item(i);
			if((href = n.getAttribute("href")) == null)
				continue;

			try {
				urls.add(Url.parseWithParent(page.getUrl(), href));
			} catch(Exception e) {
				continue;
			}
		}
		return urls;
	}

}
