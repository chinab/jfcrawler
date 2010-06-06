package org.thuir.forum.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.js.JavaScriptRepository;
import org.thuir.forum.js.JavaScriptRepository.JsHandler;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.util.Statistic;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class ForumExtractor extends HTMLExtractor {
	private TemplateRepository lib = TemplateRepository.getInstance();
	private JavaScriptRepository jsRepository = JavaScriptRepository.getRepository();

	private List<JsHandler> js = new ArrayList<JsHandler>();

	@Override
	public List<Url> extractUrls(Page page) {
		//		List<Url> urls = super.extractUrls(page);

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
		for(int i = 0; i < scriptNodes.getLength(); i++) {
			String token = ((Element)scriptNodes.item(i)).getAttribute("src");
			js.add(jsRepository.getJsHandler(page.getUrl(), token));
		}
		String script = "";
		String content = "";
		if((scriptExpr = vertex.getXPathExpression()) != null) {
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
				
				content = "";
			} catch (XPathExpressionException e1) {

			}
		} else {
			scriptNodes = (NodeList)doc.getElementsByTagName("script");

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
			
			content = "";
		}

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

			}
		} else {
			nodes = (NodeList)doc.getElementsByTagName("a"); 
			extractUrlsFromNodes(vertex, url, nodes, ret);
		}

		js.clear();
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


