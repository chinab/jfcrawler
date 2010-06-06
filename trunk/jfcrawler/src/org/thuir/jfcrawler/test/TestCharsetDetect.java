package org.thuir.jfcrawler.test;

import java.io.FileInputStream;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;

import junit.framework.TestCase;

public class TestCharsetDetect extends TestCase {

	public void testCharset() throws Exception {
		FileInputStream reader = new FileInputStream("./src/org/thuir/jfcrawler/test/non-javascript.html");
		Page page = new Page(Url.parse("www.test.com"));
		
		byte[] buf = new byte[65536];
		reader.read(buf);
		page.load(buf);
	}
}
