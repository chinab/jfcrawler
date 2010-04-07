package org.thuir.jfcrawler.framework.handler;

import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;

/**
 * @author ruKyzhc
 *
 */
public class HTMLPageHandler extends PageHandler {

	@Override
	public PageUrl[] extractUrls(Page page) {
		ArrayList<PageUrl> urls = new ArrayList<PageUrl>();
		
		Source source = new Source(page.getHtmlContent());
		List<Element> list = source.getAllElements(HTMLElementName.A);
		
		for(Element e : list) {
			try {
				String link = e.getAttributeValue("href");
				if(link == null)
					continue;
				PageUrl url = new PageUrl(page.getPageUrl(), link);
				urls.add(url);
			} catch (BadUrlFormatException e1) {
				continue;
			}
		}
		
		PageUrl[] temp = new PageUrl[urls.size()];
		urls.toArray(temp);
		return temp;
	}
}
