package org.thuir.forum.template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

public class TemplateRepository {
	private static Logger logger = Logger.getLogger(TemplateRepository.class);
	private static TemplateRepository repository = null;
	
	public static TemplateRepository load(String folder) {
		File path = new File(folder);
		if(!path.exists() || !path.isDirectory())
			repository = null;
		repository = new TemplateRepository(path);
		return repository;
	}
	
	public static TemplateRepository getInstance() {
		return repository;
	}
	
	private Map<String, Template> library = null;
	private Map<String, String> rootIndex = null;
	
	public Template getTemplate(String site) {
		return library.get(site);
	}
	
	public void setTemplate(String site, Template tmpl) {
		library.put(site, tmpl);
	}
	
	public Url getEntrance(String site) {
		try {
			return Url.parse(rootIndex.get(site));
		} catch (BadUrlFormatException e) {
			return null;
		}
	}
	
	protected TemplateRepository(File location) {
		library = new HashMap<String, Template>();
		rootIndex = new HashMap<String, String>();
		
		File[] tmplFiles = location.listFiles();
		
		for(File f : tmplFiles) {
			InputStream in;
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				in = null;
				continue;
			}

			logger.info("loading template '" + f + "'");
			Template.load(library, rootIndex, in);
		}
	}
	
}
