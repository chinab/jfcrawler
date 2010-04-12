package org.thuir.jfcrawler.test;

import java.io.IOException;

import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.writer.DefaultFileWriter;
import org.thuir.jfcrawler.io.database.UrlDB;

import junit.framework.TestCase;

public class TestJFCrawler extends TestCase {

	public void testWriter() throws BadUrlFormatException, IOException {
		UrlDB db = new UrlDB();
	}
}
