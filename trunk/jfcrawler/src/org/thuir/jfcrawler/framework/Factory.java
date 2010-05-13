package org.thuir.jfcrawler.framework;

import java.util.HashMap;
import java.util.Map;

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
	private static Class<? extends Url> urlClass = Url.class;	
	public static void registerUrlClass(
			Class<? extends Url> urlClazz) {
		urlClass = urlClazz;
	}	
	public static Url getUrlInstance() {
		try {
			return urlClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
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
	
	private static Class<? extends Cache> cacheClass = 
		BlockingQueueCache.class;
	public static void registerCacheClass(
			Class<? extends Cache> cacheClazz) {
		cacheClass = cacheClazz;
	}
	public static Cache getCacheInstance() {
		try {
			return cacheClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	
	private static Class<? extends Frontier> frontierClass = 
		BlockingQueueFrontier.class;
	public static void registerFrontierClass(
			Class<? extends Frontier> frontierClazz) {
		frontierClass = frontierClazz;
	}
	public static Frontier getFrontierInstance() {
		try {
			return frontierClass.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	
	private static Writer writer = null;
	public static void registerWriterClass(
			Class<? extends Writer> writerClazz) {
		try {
			writer = writerClazz.newInstance();
		} catch (InstantiationException e) {
			writer = new DefaultFileWriter();
		} catch (IllegalAccessException e) {
			writer = new DefaultFileWriter();
		}
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
			modules.put(MODULE_URLDB, new UrlDB());
		} catch (Exception e) {
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
