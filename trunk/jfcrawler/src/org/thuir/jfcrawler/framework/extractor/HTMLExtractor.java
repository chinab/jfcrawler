package org.thuir.jfcrawler.framework.extractor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class HTMLExtractor extends Extractor {

	@Override
	public List<Url> extractUrls(Page page) {
		List<Url> urls = new ArrayList<Url>();
		
		Source source = null;
		try {
			source = new Source(
					new ByteArrayInputStream(
							page.getHtmlContent()));
		} catch (IOException e) {
			//TODO
			return null;
		}
		
		List<Element> list = source.getAllElements(HTMLElementName.A);
		
		for(Element e : list) {
			try {
				String link = e.getAttributeValue("href");
				if(link == null)
					continue;
				Url url = Url.parseWithParent(page.getUrl(), link);
				urls.add(url);
			} catch (BadUrlFormatException e1) {
				continue;
			}
		}
		
		return urls;
	}
}
