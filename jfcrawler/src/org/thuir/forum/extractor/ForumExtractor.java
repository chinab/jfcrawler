package org.thuir.forum.extractor;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.HTMLExtractor;
import org.thuir.jfcrawler.util.Statistic;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ruKyzhc
 *
 */
public class ForumExtractor extends HTMLExtractor {
	TemplateRepository lib = TemplateRepository.getInstance();

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
		XPathExpression xpathExpr = null;
		if((xpathExpr = vertex.getXPathExpression()) == null) {
			nodes = doc.getElementsByTagName("a");
		} else {
			try {
				nodes = (NodeList)xpathExpr.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e1) {
				nodes = doc.getElementsByTagName("a");
			} 
		}
		
		int len = nodes.getLength();
		for(int i = 0; i < len; i++) {
			String href = ((Element)nodes.item(i)).getAttribute("href");

			ForumUrl f;
			try {
				f = (ForumUrl) Url.parseWithParent(page.getUrl(), href);
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

		return ret;
	}

}


