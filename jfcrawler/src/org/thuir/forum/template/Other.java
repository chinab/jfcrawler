package org.thuir.forum.template;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.forum.data.Identity;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;

/**
 * @author ruKyzhc
 *
 */
public final class Other extends Vertex {
	private static Logger logger = Logger.getLogger(Other.class);

	public Other() {
		super(null);
		tag = Tag.OTHER;
		
		try {
			scriptExpr = Factory.getXPathExpression(DefaultScriptExpr);
		} catch (XPathExpressionException e1) {
			logger.error("error while compiling script xpath.", e1);
		}
	}

	@Override
	public Identity identify(Url url) {
		return null;
	}
	
}
