package org.thuir.jfcrawler.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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
/*
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
	private static int threadInterval =
		config.getInt("threadInterval", 1000);
	private static int maxFetcherThreadPoolSize = 
		config.getInt("maxFetcherThreadPoolSize", 10);
	private static int accessInterval =
		config.getInt("accessInterval", 10000);
		
	public static int getThreadInterval() {
		return threadInterval;
	}
	public static int getMaxFetcherThreadPoolSize() {
		return maxFetcherThreadPoolSize;
	}
	public static int getAccessInterval() {
		return accessInterval;
	}

	//writer
	private static String writerRoot =
		config.getString("writerRoot", "jobs/");
	public static String getWriterRoot() {
		return writerRoot;
	}
*/
	public static Configuration getConfig() {
		return config;
	}
}
