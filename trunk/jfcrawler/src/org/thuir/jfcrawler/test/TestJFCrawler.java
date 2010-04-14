package org.thuir.jfcrawler.test;

import java.sql.Time;

import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.io.database.UrlDB;

import junit.framework.TestCase;

public class TestJFCrawler extends TestCase {

	public void testWriter() throws Exception {
		UrlDB db = new UrlDB();
		db.clear();
		
		Url url = Url.parse("www.google.com");
		url.setFetched();
		url.setHttpCode(400);
		url.setLastVisit(System.currentTimeMillis());
		
		Url temp = Url.parse("www.google.com");
		
		System.out.println(db.check(url));
		db.save(url);
		db.load(temp);
		
		System.out.println(temp.getHttpCode());
		System.out.println(new Time(temp.getLastVisit()));
	}
}
