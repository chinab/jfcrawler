package org.thuir.forum.js;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.thuir.jfcrawler.data.BadUrlFormatException;
import org.thuir.jfcrawler.data.Url;

/**
 * @author ruKyzhc
 *
 */
public class JavaScriptRepository {
	private static Logger logger = Logger.getLogger(JavaScriptRepository.class);

	private static JavaScriptRepository instance = new JavaScriptRepository();
	private static ScriptEngineManager manager = new ScriptEngineManager();

	public static JavaScriptRepository getRepository() {
		return instance;
	}

	private Map<String, JsHandler> jsCache = null;
	private HttpClient httpClient = null;

	protected JavaScriptRepository() {
		jsCache = new HashMap<String, JsHandler>();
		loadBrowserJs();

		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 5);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ClientConnectionManager cm = 
			new ThreadSafeClientConnManager(params, schemeRegistry);

		httpClient = new DefaultHttpClient(cm, params);
	}

	public JsHandler getJsHandler(Url url, String uri) {
		String token = JsHandler.getToken(url, uri);
		if(token == null) {
			return null;
		}

		JsHandler handler = jsCache.get(token);
		if(handler == null) {
			ScriptEngine engine = manager.getEngineByName("javascript");
			try {
				engine.eval(browserJs);

				HttpContext contextPage = new BasicHttpContext();
				HttpGet httpget = new HttpGet(token);

				HttpResponse response = 
					httpClient.execute(httpget, contextPage);
				HttpEntity jsPage = response.getEntity();

				BufferedReader pageReader = 
					new BufferedReader(
							new InputStreamReader(jsPage.getContent()));

				engine.eval(pageReader);

				handler = new JsHandler(engine);
			} catch (ScriptException e) {
				logger.error("script errors when generating JsHandler '" + token + "'.", e);
				return null;
			} catch (ClientProtocolException e) {
				logger.error("error when downloading js file '" + token + "'.", e);
				return null;
			} catch (IOException e) {
				logger.error("error when downloading js file '" + token + "'.", e);
				return null;
			}

			jsCache.put(token, new JsHandler(engine));
		}

		return handler;
	}

	public static class JsHandler { 
		public static String getToken(Url url, String uri) {
			try {
				return Url.parseWithParent(url, uri).getUrl();
			} catch (BadUrlFormatException e) {
				return null;
			}
		}

		private ScriptEngine engine = null;

		JsHandler(ScriptEngine engine) {
			this.engine = engine;
		}

		public String eval(String script) throws ScriptException {
			engine.eval("reset()");
			engine.eval(script);
			return engine.get("printer").toString();
		}
	}

	private final static String BROWSER_JS_LOC = "./src/org/thuir/forum/js/browser.js";
	private static String browserJs = "";
	
	private static void loadBrowserJs() {
		try {
			FileReader reader = new FileReader(BROWSER_JS_LOC);
			browserJs = "";

			char[] buf = new char[65536];
			while(reader.read(buf) > 0) {
				browserJs += new String(buf);
			}
			
		} catch (FileNotFoundException e) {
			logger.error("cannot find browser js file", e);
			browserJs = "";
		} catch (IOException e) {
			browserJs = "";
		}
	}
}
