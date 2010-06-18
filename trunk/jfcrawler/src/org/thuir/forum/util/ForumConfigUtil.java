package org.thuir.forum.util;

import java.util.HashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * @author ruKyzhc
 *
 */
public class ForumConfigUtil {
	private static final Logger logger = 
		Logger.getLogger(ForumConfigUtil.class);

	private static final String forumConfigLoc = "./conf/forum.xml";
	private static Configuration forumConfig;
	

	static {
		try {
			forumConfig = new XMLConfiguration(forumConfigLoc);
		}catch(ConfigurationException cex) {
			logger.fatal("Fail to load configuration file.", cex);
		}
	}

	public static Configuration getForumConfig() {
		return forumConfig;
	}
}
