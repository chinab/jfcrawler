package org.thuir.jfcrawler.test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.cyberneko.html.parsers.DOMParser;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.extractor.XpathExtractor;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author ruKyzhc
 *
 */
public class TestHTMLExtractor extends TestCase {
	
	public void testExtractor() throws Exception {
		Page page = new Page(Url.parse("www.discuz.net"));
		
		byte[] buf = new byte[65536];
		FileInputStream in = new FileInputStream("./src/org/thuir/jfcrawler/test/non-javascript.html");
		in.read(buf);
		page.load(buf);
		
		XpathExtractor ex2 = new XpathExtractor();
		
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		
		
		DOMParser parser = new DOMParser();
		parser.setProperty(
				"http://cyberneko.org/html/properties/default-encoding",
				page.getCharset()
		);
		parser.setFeature("http://xml.org/sax/features/namespaces", false); 
		//input file
		parser.parse(
				new InputSource(
						new ByteArrayInputStream(page.getHtmlContent())
				)
		);
		Document doc = parser.getDocument();

		XPathExpression urlExpr = xpath.compile("//SCRIPT");
		NodeList nodes = (NodeList)urlExpr.evaluate(doc, XPathConstants.NODESET);

		ex2.setXpathExpr(urlExpr);
		List<Url> urls2 = ex2.extractUrls(page);
		for(int i = 0; i < nodes.getLength(); i++){
			System.out.println(nodes.item(i).getTextContent());
		}
		
		for(Url url : urls2) {
			System.out.println(url.toString());
		}
	}

}
