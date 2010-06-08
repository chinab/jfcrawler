package org.thuir.forum.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.js.JavaScriptRepository;
import org.thuir.forum.js.JavaScriptRepository.JsHandler;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.util.LogUtil;
import org.thuir.jfcrawler.util.Statistic;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class ForumExtractor extends HTMLExtractor {
	private static Logger logger = 
		Logger.getLogger(ForumExtractor.class);
	
	private TemplateRepository lib = TemplateRepository.getInstance();
	private JavaScriptRepository jsRepository = JavaScriptRepository.getRepository();

	@Override
	public List<Url> extractUrls(Page page) {
		if(!(page.getUrl() instanceof ForumUrl)) {
			return super.extractUrls(page);
		}

		ForumUrl url = (ForumUrl)page.getUrl();

		if(url.getTag() != null) {
			switch(url.getTag()) {
			case CATALOG: {
				Statistic.get("catalog-counter").inc();
			};break;
			case BOARD: {
				Statistic.get("board-counter").inc();
			};break;
			case THREAD: {
				Statistic.get("thread-counter").inc();
			};break;
			default:
				break;
			}
		}

		Template tmpl = lib.getTemplate(url.getHost());

		Vertex vertex = null;
		if(url.getTag()==null)
			vertex = tmpl.predict(url);
		else
			vertex = tmpl.predictByTag(url.getTag(), url);

		if(vertex == null) {
			return super.extractUrls(page);
		}

		List<Url> ret = new ArrayList<Url>();
		Document doc = parse(page);

		NodeList nodes = null;

		//javascript
		NodeList scriptNodes = null;
		XPathExpression scriptExpr = null;
		scriptNodes = doc.getElementsByTagName("script");
		List<JsHandler> js = new ArrayList<JsHandler>();
		JsHandler jsHandler = null;
		
		for(int i = 0; i < scriptNodes.getLength(); i++) {
			String token = ((Element)scriptNodes.item(i)).getAttribute("src");
			if(token.trim().isEmpty()) {
				continue;
			}
			jsHandler = jsRepository.getJsHandler(page.getUrl(), token);
			if(jsHandler != null)
				js.add(jsHandler);
		}
		String script = "";
		String content = "";
		if((scriptExpr = vertex.getScriptExpression()) != null) {
			try {
				scriptNodes = (NodeList)scriptExpr.evaluate(doc, XPathConstants.NODESET);

				int len = scriptNodes.getLength();
				for(int i = 0; i < len; i++) {
					script = scriptNodes.item(i).getTextContent();
					for(JsHandler handler : js) {
						try {
							content += handler.eval(script);
						} catch (ScriptException e) {
							continue;
						}
					}
				}
			} catch (XPathExpressionException e1) {
				logger.error(
						LogUtil.message("script xpath expression '" 
								+ scriptExpr.toString() + "' error.", e1));
			}
		} else {
			scriptNodes = (NodeList)doc.getElementsByTagName("script");

			int len = scriptNodes.getLength();
			for(int i = 0; i < len; i++) {
				script = scriptNodes.item(i).getTextContent();
				if(script.trim().length() == 0)
					continue;
				for(JsHandler handler : js) {
					try {
						content += handler.eval(script);
					} catch (ScriptException e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		}
		js.clear();
		Document jsDoc = parse(content, page.getCharset());
		NodeList jsNodes = jsDoc.getElementsByTagName("a");
		extractUrlsFromNodes(vertex, url, jsNodes, ret);
		
		//xpath
		NodeList xpathNodes = null;
		XPathExpression xpathExpr = null;
		if((xpathExpr = vertex.getXPathExpression()) != null) {
			try {
				xpathNodes = (NodeList)xpathExpr.evaluate(doc, XPathConstants.NODESET);

				int len = xpathNodes.getLength();
				for(int i = 0; i < len; i++) {
					extractUrlsFromNodes(vertex, url, xpathNodes, ret);
				}
			} catch (XPathExpressionException e1) {
				logger.error(LogUtil.message("xpath expression '"+ scriptExpr.toString() 
						+ "' error.", e1));
			}
		} else {
			nodes = (NodeList)doc.getElementsByTagName("a"); 
			extractUrlsFromNodes(vertex, url, nodes, ret);
		}

		return ret;
	}

	private void extractUrlsFromNodes(
			Vertex vertex, ForumUrl url, NodeList nodes, List<Url> ret) {
		int len = nodes.getLength();
		for(int i = 0; i < len; i++) {
			String href = ((Element)nodes.item(i)).getAttribute("href");

			ForumUrl f;
			try {
				f = (ForumUrl) Url.parseWithParent(url, href);
			} catch (Exception e) {
				continue;
			}
			f.setInlinkTag(url.getTag());

			if(vertex.match(f)) {
				f.setTag(vertex.getTag());
				ret.add(f);
			}

			for(Vertex v : vertex.getChildren()) {
				if(v.match(f)) {
					f.setTag(v.getTag());
					ret.add(f);
					break;
				}
			}
		}
	}

}


