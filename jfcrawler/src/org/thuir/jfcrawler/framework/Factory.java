package org.thuir.jfcrawler.framework;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.framework.cache.BlockingQueueCache;
import org.thuir.jfcrawler.framework.cache.Cache;
import org.thuir.jfcrawler.framework.frontier.BlockingQueueFrontier;
import org.thuir.jfcrawler.framework.frontier.Frontier;
import org.thuir.jfcrawler.framework.writer.DefaultFileWriter;
import org.thuir.jfcrawler.framework.writer.Writer;
import org.thuir.jfcrawler.io.database.UrlDB;
import org.thuir.jfcrawler.io.httpclient.MultiThreadHttpFetcher;
import org.thuir.jfcrawler.util.AccessController;

/**
 * @author ruKyzhc
 *
 */
public class Factory {
	private static Logger logger = Logger.getLogger(Factory.class);
	
	public static void registerUrlClass(
			Class<? extends Url> urlClazz) {
		Url.setUrlClass(urlClazz);
	}	

	private static Class<? extends Page> pageClass = Page.class;
	public static void registerPageClass(
			Class<? extends Page> pageClazz) {
		pageClass = pageClazz;
	}	
	public static Page getPageInstance() {
		try {
			return pageClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	
	private static Cache cache = new BlockingQueueCache();
	public static boolean registerCacheClass(
			Class<? extends Cache> cacheClazz) {
		try {
			cache = cacheClazz.newInstance();
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		return true;
	}
	public static Cache getCacheInstance() {
		return cache;
	}
	
	private static Frontier frontier = new BlockingQueueFrontier();
	public static boolean registerFrontierClass(
			Class<? extends Frontier> frontierClazz) {
		try {
			frontier = frontierClazz.newInstance();
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		return true;
	}
	public static Frontier getFrontierInstance() {
		return frontier;
	}
	
	private static Writer writer = new DefaultFileWriter();
	public static boolean registerWriterClass(
			Class<? extends Writer> writerClazz) {
		try {
			writer = writerClazz.newInstance();
		} catch (InstantiationException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		}
		return true;
	}
	public static Writer getWriterInstance(String jobName) {
		writer.setRoot(jobName);
		return writer;
	}

	private static Map<String, Object> modules =
		new HashMap<String, Object>();
	public final static String MODULE_URLDB = 
		"module.urldb";
	public final static String MODULE_STATISTICS = 
		"module.statistics";
	public final static String MODULE_HTTPFETCHER = 
		"module.httpfetcher";
	public final static String MODULE_ACCESSCONTROLLER = 
		"module.accesscontroller";
	
	public static void initAllModuleWithDefault() {
		modules.put(MODULE_HTTPFETCHER,      new MultiThreadHttpFetcher());
		modules.put(MODULE_ACCESSCONTROLLER, new AccessController());
		//TODO
		modules.put(MODULE_STATISTICS, null);
		try {
			UrlDB urldb = new UrlDB();
			urldb.clear();
			modules.put(MODULE_URLDB, urldb);
		} catch (Exception e) {
			logger.fatal("database initialization failed", e);
			modules.put(MODULE_URLDB, null);
		}
	}
	public static void registerModule(String name, Object module) {
		modules.put(name, module);
	}
	public static Object getModule(String name) {
		return modules.get(name);
	}
}
