package org.thuir.jfcrawler.util;

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
}
