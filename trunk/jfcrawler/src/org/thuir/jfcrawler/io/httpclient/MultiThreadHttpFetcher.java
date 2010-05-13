package org.thuir.jfcrawler.io.httpclient;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
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
import org.thuir.jfcrawler.util.BasicThread;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class MultiThreadHttpFetcher extends BasicThread{
	private final static long INTERVAL = 
		ConfigUtil.getConfig().getLong("basic.thread-interval");
	private HttpClient client = null;

	private FetchUnit[] threadPool = null;
	private int nThread = 0;

	private int threadCounter = 0;

//	private boolean working = false;
	
	protected String userAgent = 
		ConfigUtil.getConfig().getString("fetcher.user-agent");

	public MultiThreadHttpFetcher() {
		nThread = ConfigUtil.getConfig().getInt("fetcher.max-pool-size");
		this.setName("MultiThreadHttpFetcher");

		// initialize httpclient
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ClientConnectionManager cm = 
			new ThreadSafeClientConnManager(params, schemeRegistry);

		client = new DefaultHttpClient(cm, params);

		// initialize thread pool
		threadPool = new FetchUnit[nThread];

		for(int i = 0; i < nThread; i++) {
			threadPool[i] = new FetchUnit("fetch-unit " + i, client);
		}

		threadCounter = nThread;
		
		this.doStart();
	}

	protected void doStart() {
		setAlive(true);
		for(FetchUnit u : threadPool) {
			u.start();
		}
		this.start();
	}

	@Override
	public void run() {
		super.run();
		setIdle(false);
		while(alive()) {
			try {
				Thread.sleep(INTERVAL);
				
				boolean isIdle = true;
				for(int i = 0; i < nThread; i++) {
					if(threadPool[i].timeout()) {
						threadPool[i].expired();
						threadPool[i] = 
							new FetchUnit(
									"fetch-unit " + threadCounter++, 
									client);
						threadPool[i].start();
					}
					if(!threadPool[i].idle()) {
						isIdle = false;
					}
				}
				setIdle(isIdle);
			} catch (InterruptedException e) {
			} finally {
			}
		}
	}

	@Override
	public void close() {
		super.close();
		setAlive(false);

		try {
			Thread.sleep(INTERVAL * 2);
		} catch (InterruptedException e) {
		}
		for(FetchUnit u : threadPool) {
			u.close();
		}
	}

	public boolean access(FetchExchange exchange) {
		if(alive() == false)
			return false;

		FetchUnit u = getIdleUnit();
		if(u == null)
			return false;

		exchange.setUserAgent(userAgent);
		u.fetch(exchange);
		return true;
	}
	
	public void fetch(FetchExchange exchange) {
		if(alive() == false)
			return;

		FetchUnit u = null;
		while((u = getIdleUnit()) == null) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
			}
		}

		exchange.setUserAgent(userAgent);
		u.fetch(exchange);
	}

	protected FetchUnit getIdleUnit() {
		for(FetchUnit u : threadPool) {
			if(u.idle())
				return u;
		}
		return null;
	}
}
