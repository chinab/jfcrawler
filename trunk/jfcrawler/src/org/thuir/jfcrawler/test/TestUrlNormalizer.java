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
				"http://www.google.com:8080",
				"www.renren.com/home.do?userid=&pass=2&home=3",
				"https://www.googlecode.com:443/",
				"www.baidu.com"
		};
		
		PageUrl page = new PageUrl();
		
		page.generateNormalizedUrl(urls[0]);		
		assertEquals(page.getHost(), "www.google.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "8080");
		assertNull(page.getPage());
		assertEquals(page.getUrl(), "http://www.google.com:8080/");
		
		page.generateNormalizedUrl(urls[1]);		
		assertEquals(page.getHost(), "www.renren.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "80");
		assertEquals(page.getPage(), "home.do");
		assertEquals(page.getUrl(), "http://www.renren.com/home.do?home=3&pass=2");
		
		page.generateNormalizedUrl(urls[2]);		
		assertEquals(page.getHost(), "www.googlecode.com");
		assertEquals(page.getProtocol(), "https");
		assertEquals(page.getPort(), "443");
		assertNull(page.getPage());
		assertEquals(page.getUrl(), "https://www.googlecode.com:443/");
		
		page.generateNormalizedUrl(urls[3]);		
		assertEquals(page.getHost(), "www.baidu.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "80");
		assertNull(page.getPage());
		assertEquals(page.getUrl(), "http://www.baidu.com/");
	}
}
