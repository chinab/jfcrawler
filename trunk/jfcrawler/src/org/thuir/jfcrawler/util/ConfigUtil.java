package org.thuir.jfcrawler.util;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * @author ruKyzhc
 *
 */
public class ConfigUtil {
	private static final Logger logger = 
		Logger.getLogger(ConfigUtil.class);
	private static final String CONFIG_FILE = 
		"./conf/crawlerConfiguration.properties";

	private static Configuration config;
	
	private static HashMap<String, Object> properties = 
		new HashMap<String, Object>();

	static {
		try {
			config = new XMLConfiguration("./conf/crawler.xml");
		}catch(ConfigurationException cex) {
			logger.fatal("Fail to load configuration file '" + CONFIG_FILE + "'");
		}
	}

	public static Configuration getConfig() {
		return config;
	}
	
	public static void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public static Object getProperty(String key) {
		return properties.get(key);
	}
}
