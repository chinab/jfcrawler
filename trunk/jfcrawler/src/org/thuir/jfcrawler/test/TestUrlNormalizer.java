package org.thuir.jfcrawler.test;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

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
				"http://www.discuz.net/#",
				"http://www.newsmth.net/bbsavv.php?select=1?x"
		};
		
		Url page = null;
		
		page = Url.parse(urls[0]);		
		assertEquals(page.getHost(), "www.google.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "8080");
		assertEquals(page.getPage(), "");
		assertEquals(page.getUrl(), "http://www.google.com:8080/");
		
		page = Url.parse(urls[1]);			
		assertEquals(page.getHost(), "www.renren.com");
		assertEquals(page.getProtocol(), "http");
		assertEquals(page.getPort(), "80");
		assertEquals(page.getPage(), "home.do");
		assertEquals(page.getUrl(), "http://www.renren.com/home.do?home=3&pass=2&userid");
		
		page = Url.parseWithParent(null, urls[2]);			
		assertEquals(page.getHost(), "www.googlecode.com");
		assertEquals(page.getProtocol(), "https");
		assertEquals(page.getPort(), "443");
		assertEquals(page.getPage(), "");
		assertEquals(page.getUrl(), "https://www.googlecode.com:443/");
		
		Url temp = Url.parseWithParent(null, "http://www.baidu.com/more");
		System.out.println(temp);
		page = Url.parseWithParent(temp, urls[3]);		
		System.out.println(page);
		
		page = Url.parse(urls[4]);
		System.out.println(page);
		
		page = Url.parse(urls[5]);
		System.out.println(page);
		System.out.println(page.getUri());
	}
}
