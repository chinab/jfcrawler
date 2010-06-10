package org.thuir.forum.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.js.JavaScriptRepository;
import org.thuir.forum.js.JavaScriptRepository.JsHandler;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class ForumExtractor extends HTMLExtractor {
	private static Logger logger = 
		Logger.getLogger(ForumExtractor.class);

	private static XPath xpath = XPathFactory.newInstance().newXPath();
	
	private TemplateRepository lib = TemplateRepository.getInstance();
	private JavaScriptRepository jsRepository = JavaScriptRepository.getRepository();
	
	private XPathExpression scriptSrcExpr = null;
	private XPathExpression hrefExpr = null;

	public ForumExtractor() {
		try {
			scriptSrcExpr = xpath.compile("//SCRIPT[@src]/attitude::src");
			hrefExpr = xpath.compile("//A[@href]/attitude::href");
		} catch(Exception e) {
			
		}
	}
	
	@Override
	public List<Url> extractUrls(Page page) {
		ForumUrl url = null;
		
		if(!(page.getUrl() instanceof ForumUrl)) {
			url = new ForumUrl(page.getUrl());
			page.setUrl(url);
		}
		url = (ForumUrl)page.getUrl();

		Template tmpl = lib.getTemplate(url.getHost());

		Vertex vertex = tmpl.getVertexByTag(Tag.OTHER);
		if(url.getTag()==null)
			vertex = tmpl.predict(url);
		else
			vertex = tmpl.getVertexByTag(url.getTag());

		List<Url> ret = new ArrayList<Url>();
		Document doc = parse(page);
		int len = 0;
		
		XPathExpression scriptExpr = vertex.getScriptExpr();
		if(scriptExpr != null && scriptSrcExpr != null) {
			String script = "";
			String content = "";
			
			JsHandler jsHandler = null;
			NodeList scriptNodes = null;

			List<JsHandler> js = new ArrayList<JsHandler>();
			
			try {
				scriptNodes = (NodeList)scriptSrcExpr.evaluate(doc, XPathConstants.NODESET);

				len = scriptNodes.getLength();
				for(int i = 0; i < len; i++) {
					String token = scriptNodes.item(i).getNodeValue();
					if(token.trim().isEmpty()) {
						continue;
					}
					jsHandler = jsRepository.getJsHandler(page.getUrl(), token);
					if(jsHandler != null)
						js.add(jsHandler);
				}

				scriptNodes = (NodeList)scriptExpr.evaluate(doc, XPathConstants.NODESET);
				len = scriptNodes.getLength();
				for(int i = 0; i < len; i++) {
					script = scriptNodes.item(i).getNodeValue();
					if(script.trim().length() == 0)
						continue;
					for(JsHandler handler : js) {
						try {
							content += handler.eval(script);
						} catch (ScriptException e) {
							continue;
						}
					}
				}

				doc = parse(content, page.getCharset());
			} catch (XPathExpressionException e1) {
				logger.error("xpath expression error", e1);
			}
		}

		try {
			NodeList nodes = (NodeList)hrefExpr.evaluate(doc, XPathConstants.NODESET);
			len = nodes.getLength();
			String href = null;
			for(int i = 0; i < len; i++) {
				href = nodes.item(i).getNodeValue();
				ForumUrl u = (ForumUrl)Url.parseWithParent(url, href);
				
				u.setInlinkTag(url.getTag());
				u.setTag(vertex.checkOutlink(u));
				
				u.setIdentityFromParent(url.getIdentity());
				
				if(u.getTag() != Tag.OTHER)
					ret.add(u);
			}
		} catch(BadUrlFormatException e) {
			logger.error(e);
		} catch (XPathExpressionException e) {
			logger.error("xpath expression error", e);
		}

		return ret;
	}
}