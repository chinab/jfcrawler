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

	private static final String crawlerConfigLoc = "./conf/crawler.xml";
	private static Configuration crawlerConfig;
	private static final String databaseConfigLoc = "./conf/database.xml";
	private static Configuration databaseConfig;
	
	private static HashMap<String, Object> properties = 
		new HashMap<String, Object>();

	static {
		try {
			crawlerConfig = new XMLConfiguration(crawlerConfigLoc);
			databaseConfig = new XMLConfiguration(databaseConfigLoc);
		}catch(ConfigurationException cex) {
			logger.fatal("Fail to load configuration file.", cex);
		}
	}

	public static Configuration getCrawlerConfig() {
		return crawlerConfig;
	}
	public static Configuration getDatabaseConfig() {
		return databaseConfig;
	}
	
	public static void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	
	public static Object getProperty(String key) {
		return properties.get(key);
	}
}
