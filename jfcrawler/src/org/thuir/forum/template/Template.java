package org.thuir.forum.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.util.LogUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Template {
	private static Logger logger = Logger.getLogger(Template.class);
	
	public static void load(
			Map<String, Template> lib, 
			Map<String, String> root,
			InputStream input) {
		Template tmpl = new Template();
		
		try {
			DocumentBuilderFactory factory = 
				DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = 
				factory.newDocumentBuilder();
			
			Document document = builder.parse(input);

			NodeList nodeList = null;
			
			tmpl.identify = 
				((Element)document.getElementsByTagName("forum").item(0))
				.getAttribute("id");
			//Node
			nodeList = document.getElementsByTagName("vertex");
			for(int i = 0; i < nodeList.getLength(); i++) {
				Vertex.create(tmpl.vertice, (Element)nodeList.item(i));
			}
			for(int i = 0; i < nodeList.getLength(); i++) {
				Vertex.load(tmpl.vertice, (Element)nodeList.item(i));
			}
			
			//sites
			nodeList = document.getElementsByTagName("site");
			for(int i = 0; i < nodeList.getLength(); i++) {
				Element s = (Element)nodeList.item(i);
				Url e = Url.parse(s.getAttribute("entrance"));
				Url r = Url.parse(s.getAttribute("root"));

				lib.put(r.getHost(), tmpl);
				root.put(r.getHost(), e.getUrl());
			}
		} catch (SAXException e) {
			logger.error(
					LogUtil.message("error when loading template '" + tmpl.identify + "'.", e));
		} catch (IOException e) {
			logger.error(
					LogUtil.message("error when loading template '" + tmpl.identify + "'.", e));
		} catch (ParserConfigurationException e) {
			logger.error(
					LogUtil.message("error when loading template '" + tmpl.identify + "'.", e));
		} catch (BadUrlFormatException e) {
			logger.error(
					LogUtil.message("error when loading template '" + tmpl.identify + "'.", e));
		}
	}
	
	private String identify;
	
	private Map<String, Vertex> vertice = null;

	public Template() {
		vertice = new HashMap<String, Vertex>();
	}
	
	public Vertex predict(Url url) {
		for(Vertex v : vertice.values()) {
			if(v.match(url))
				return v;
		}
		return null;
	}
	
	public Vertex predictByTag(Tag tag, Url url) {
		for(Vertex v : vertice.values()) {
			if(v.getTag() == tag && v.match(url))
				return v;
		}
		return null;
	}
	
	public Object[] getVertexByTag(Tag tag) {
		ArrayList<Vertex> ret = new ArrayList<Vertex>();
		for(Vertex v : vertice.values()) {
			if(v.getTag() == tag)
				ret.add(v);
		}
		
		return ret.toArray();
	}
	
	public String getIdentify() {
		return identify;
	}

}
