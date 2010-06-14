package org.thuir.forum.test;

import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.forum.template.Template;
import org.thuir.forum.template.TemplateRepository;
import org.thuir.forum.template.Vertex;
import org.thuir.jfcrawler.data.Url;

import junit.framework.TestCase;

/**
 * @author ruKyzhc
 *
 */
public class TestTemplate extends TestCase {

	public void testTemplate() throws Exception {
		ForumUrl.registerToUrlFactory();
		TemplateRepository.load("./template");
		
		TemplateRepository rep = TemplateRepository.getInstance();
		Template tmpl = rep.getTemplate("www.newsmth.net");

		Url url1 = Url.parse("http://www.newsmth.net/bbsdoc.php?board=Heroes&page=703");
		Url url2 = Url.parse("http://www.newsmth.net/bbscon.php?bid=1066&id=41169");
		Vertex v1 = tmpl.predict(url1);
//		Vertex v2 = tmpl.predict(url2);
//		
//		System.out.println(v1.getTag());
//		Identity id1 = tmpl.identify(v1.getTag(), url1);
//		System.out.println(v2.getTag());
//		Identity id2 = tmpl.identify(v2.getTag(), url2);
//		
//		System.out.println(id1);
//		System.out.println(id2);
//		
//		Identity.synchronize(id1, id2);
//		
//		System.out.println(id1);
//		System.out.println(id2);
//		
//		System.out.println(id1.getMeta());
//		System.out.println(id2.getMeta());
		System.out.println(v1.checkOutlink(url2));
	}

}
