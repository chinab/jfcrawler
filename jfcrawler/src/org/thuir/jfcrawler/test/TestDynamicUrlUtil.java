package org.thuir.jfcrawler.test;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.DynamicUrlUtil;

import junit.framework.TestCase;

public class TestDynamicUrlUtil extends TestCase {
	
	public void testConvertor() throws BadUrlFormatException {
		Url url = Url.parseWithParent(null, "http://www.google.cn/search?hl=zh-CN&q=googlecode.com+403");
		System.out.println(url);
		System.out.println(url.getPath());
		System.out.println(DynamicUrlUtil.danymicUrlToString(url));
	}
}
