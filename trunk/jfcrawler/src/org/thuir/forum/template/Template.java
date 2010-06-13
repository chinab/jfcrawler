package org.thuir.forum.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ForumUrl;
import org.thuir.forum.data.Identity;
import org.thuir.forum.template.Vertex.Tag;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.Factory;
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
			
			tmpl.identify = (String)
			Factory.evaluateXPath("//forum[@id]/attribute::id", document, XPathConstants.STRING);
			//Node

			tmpl.catalog = Vertex.load(Tag.CATALOG, 
					(Element)Factory.evaluateXPath("//vertex[@id='catalog']", 
							document, XPathConstants.NODE));
			tmpl.board   = Vertex.load(Tag.BOARD, 
					(Element)Factory.evaluateXPath("//vertex[@id='board']", 
							document, XPathConstants.NODE));
			tmpl.article = Vertex.load(Tag.BOARD, 
					(Element)Factory.evaluateXPath("//vertex[@id='article']", 
							document, XPathConstants.NODE));
			tmpl.other   = Vertex.load(Tag.OTHER, null);
			
			//sites
			NodeList list = (NodeList)Factory.evaluateXPath(
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
	
	public Identity identify(Tag tag, Url url) {
		switch(tag) {
		case CATALOG:
			return catalog.identify(url);
		case BOARD:
			return board.identify(url);
		case ARTICLE:
			return article.identify(url);
		default:
			return other.identify(url);
		}
	}
	
	public ForumUrl generateUrl(Tag tag, Identity id) {
		return null;
	}
}
