package org.thuir.jfcrawler.framework.extractor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cyberneko.html.parsers.DOMParser;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author ruKyzhc
 *
 */
public class HTMLExtractor extends Extractor {

	@Override
	public List<Url> extractUrls(Page page) {
		List<Url> urls = new ArrayList<Url>();
		
		Document doc = parse(page);
		String href = null;
		NodeList nodes = doc.getElementsByTagName("a");
		for(int i = 0; i < nodes.getLength(); i++) {
			if((href = ((Element)nodes.item(i)).getAttribute("href")) != null) {
				try {
					urls.add(Url.parseWithParent(page.getUrl(), href));
				} catch(BadUrlFormatException e) {
					//TODO
				}
			}
		}
		return urls;
	}
	
	protected Document parse(Page page) {
		try {
			DOMParser parser = new DOMParser();
			parser.setProperty(
					"http://cyberneko.org/html/properties/default-encoding",
					page.getCharset()
			);
			parser.setFeature("http://xml.org/sax/features/namespaces", false);
			//input file
			parser.parse(
					new InputSource(
							new ByteArrayInputStream(page.getHtmlContent())
					)
			);
			Document doc = parser.getDocument();

			return doc;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
}
