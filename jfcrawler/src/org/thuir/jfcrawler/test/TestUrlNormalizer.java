package org.thuir.jfcrawler.test;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.PageUrl;

import junit.framework.TestCase;

/**
 * @author ruKyzhc
 *
 */
public class TestUrlNormalizer extends TestCase {
	public void testNormalizeUrl() throws BadUrlFormatException {
		String[] urls = {
				"www.google.com:8080",
				"www.renren.com/home.do?userid=&pass=2&home=3",
				"https://www.googlecode.com:443/",
				"plugin/index.html",
				"http://www.discuz.net/#"
		};
		
		PageUrl page = null;
		
		page = PageUrl.parse(urls[0]);		
		assertEquals(page.getHost(), "www.google.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "8080");
		assertEquals(page.getPage(), "#");
		assertEquals(page.getUrl(), "http://www.google.com:8080/");
		
		page = PageUrl.parse(urls[1]);			
		assertEquals(page.getHost(), "www.renren.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "80");
		assertEquals(page.getPage(), "home.do");
		assertEquals(page.getUrl(), "http://www.renren.com/home.do?home=3&pass=2");
		
		page = PageUrl.parse(null, urls[2]);			
		assertEquals(page.getHost(), "www.googlecode.com");
		assertEquals(page.getProtocol(), "https");
		assertEquals(page.getPort(), "443");
		assertEquals(page.getPage(), "#");
		assertEquals(page.getUrl(), "https://www.googlecode.com:443/");
		
		PageUrl temp = new PageUrl(null, "http://www.baidu.com/more");
		System.out.println(temp);
		page = PageUrl.parse(temp, urls[3]);		
		System.out.println(page);
		
		page = PageUrl.parse(urls[4]);
		System.out.println(page);
	}
}
