package org.thuir.jfcrawler.test;

import java.io.IOException;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.framework.writer.DefaultFileWriter;

import junit.framework.TestCase;

public class TestJFCrawler extends TestCase {

	public void testWriter() throws BadUrlFormatException, IOException {
		DefaultFileWriter writer = new DefaultFileWriter();
		writer.setRoot("test");
		
		PageUrl url = 
			PageUrl.parse(null, "http://127.0.0.1/test/test.php?action=save");
		Page page = new Page(url);
		page.load(new byte[10]);
		writer.write(page);
	}
}
