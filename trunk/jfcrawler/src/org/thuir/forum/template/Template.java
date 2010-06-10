package org.thuir.forum.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Template {
	private static Logger logger = Logger.getLogger(Template.class);
	private static XPath xpath = XPathFactory.newInstance().newXPath();
	
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
			
			tmpl.identify = xpath.evaluate("//forum[@id]/attribute::id", document);
			//Node

			tmpl.catalog = Vertex.load(Tag.CATALOG, 
					(Element)xpath.evaluate("//vertex[@id='catalog']", 
							document, XPathConstants.NODE));
			tmpl.board   = Vertex.load(Tag.BOARD, 
					(Element)xpath.evaluate("//vertex[@id='board']", 
							document, XPathConstants.NODE));
			tmpl.article = Vertex.load(Tag.BOARD, 
					(Element)xpath.evaluate("//vertex[@id='article']", 
							document, XPathConstants.NODE));
			tmpl.other   = Vertex.load(Tag.OTHER, null);
			
			//sites
			NodeList list = (NodeList)xpath.evaluate(
					"//sites/site[@url]/attribute::url", document, XPathConstants.NODESET);
			for(int i = 0; i < list.getLength(); i++) {
				Url r = Url.parse(list.item(i).getNodeValue());
				
				lib.put(r.getHost(), tmpl);
				root.put(r.getHost(), r.getUrl());
			}
		} catch (SAXException e) {
			logger.error("error when loading template '" + tmpl.identify + "'.", e);
		} catch (IOException e) {
			logger.error("error when loading template '" + tmpl.identify + "'.", e);
		} catch (ParserConfigurationException e) {
			logger.error("error when loading template '" + tmpl.identify + "'.", e);
		} catch (BadUrlFormatException e) {
			logger.error("error when loading template '" + tmpl.identify + "'.", e);
		} catch (XPathExpressionException e) {
			logger.error("error when loading template '" + tmpl.identify + "'.", e);
		}
	}
	
	private String identify;
	
	private Vertex catalog = null;
	private Vertex board   = null;
	private Vertex article = null;
	private Vertex other   = null;
	
	private Vertex[] vertice = {catalog, board, article, other};
	
	public Vertex predict(Url url) {
		for(Vertex v : vertice) {
			if(v.match(url))
				return v;
		}
		return other;
	}
	
	public Vertex getVertexByTag(Tag tag) {
		switch(tag) {
		case CATALOG:
			return catalog;
		case BOARD:
			return board;
		case ARTICLE:
			return article;
		default:
			return other;
		}
	}
	
	public String getIdentify() {
		return identify;
	}

}
