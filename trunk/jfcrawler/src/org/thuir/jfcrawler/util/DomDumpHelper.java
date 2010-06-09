package org.thuir.jfcrawler.util;

import java.io.File;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class DomDumpHelper {

	public static void dump(String file, Document doc) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer tf = tFactory.newTransformer();

			tf.setOutputProperty("encoding", "gb2312");

			Source source = new DOMSource(doc);
			Result result = new StreamResult(new File(file));

			tf.transform(source, result);
		} catch (TransformerException e) {
			return;
		}
	}
}
