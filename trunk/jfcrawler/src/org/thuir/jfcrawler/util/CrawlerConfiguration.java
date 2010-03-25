package org.thuir.jfcrawler.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @author ruKyzhc
 *
 */
public class CrawlerConfiguration {
	private static final Logger logger = 
		Logger.getLogger(CrawlerConfiguration.class);
	private static final String CONFIG_FILE = 
		"./conf/crawlerConfiguration.properties";

	private static Configuration config;

	static {
		try {
			config = new PropertiesConfiguration(CONFIG_FILE);
		}catch(ConfigurationException cex) {
			logger.fatal("Fail to load configuration file '" + CONFIG_FILE + "'");
		}
	}

	//fetcher
	private static int maxConnectionsPerAddress =
		config.getInt("maxConnectionsPerAddress", 10);
	private static int maxHttpFetcherThreadPoolSize = 
		config.getInt("maxHttpFetcherThreadPoolSize", 10);
	private static int maxTimeout =
		config.getInt("maxTimeout", 30000);

	public static int getMaxConnectionsPerAddress() {
		return maxConnectionsPerAddress;
	}
	public static int getMaxHttpFetcherThreadPoolSize() {
		return maxHttpFetcherThreadPoolSize;
	}
	public static int getMaxTimeout() {
		return maxTimeout;
	}
	
	//crawler unit
	private static int maxCrawlingUnit =
		config.getInt("maxCrawlingUnit", 100);
	public static int getMaxCrawlingUnit() {
		return maxCrawlingUnit;
	}

	//fetching thread
	private static int defaultInterval =
		config.getInt("defaultInterval", 10000);
	private static int maxFetcherThreadPoolSize = 
		config.getInt("maxFetcherThreadPoolSize", 10);
	
	public static int getDefaultInterval() {
		return defaultInterval;
	}
	public static int getMaxFetcherThreadPoolSize() {
		return maxFetcherThreadPoolSize;
	}
	
	

}
