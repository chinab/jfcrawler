package org.thuir.jfcrawler.test;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.Url;
import org.thuir.jfcrawler.io.nio.FetchingListener;
import org.thuir.jfcrawler.io.nio.NonBlockingFetcher;

import junit.framework.TestCase;

public class TestJFCrawler extends TestCase implements FetchingListener{

	public void testWriter() throws Exception {
		NonBlockingFetcher fetcher = new NonBlockingFetcher();

		fetcher.addFetchingListener(this);
		fetcher.setUserAgent("THUIR-bot");
		Page[] pages = {
				new Page(Url.parse("http://www.icefirer.com/index-htm-m-bbs.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-1458-page-3.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-5933.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-7092.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-12552-page-2.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-21634.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-21723.html")),
				new Page(Url.parse("http://www.icefirer.com/thread-htm-fid-67.html")),
				new Page(Url.parse("http://www.icefirer.com/thread-htm-fid-84.html")),
				new Page(Url.parse("http://www.icefirer.com/thread-htm-fid-21.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-21597.html")),
				new Page(Url.parse("http://www.icefirer.com/read-htm-tid-21724.html")),
				new Page(Url.parse("http://www.icefirer.com/thread-htm-fid-65.html")),
//				new Page(Url.parse("http://www.google.com/")),
//				new Page(Url.parse("http://www.baidu.com/")),
//				new Page(Url.parse("http://www.sogou.com/")),
//				new Page(Url.parse("http://www.renren.com/")),
//				new Page(Url.parse("http://www.sina.com.cn/")),
//				new Page(Url.parse("http://www.163.com/")),
//				new Page(Url.parse("http://www.cctv.com/")),
//				new Page(Url.parse("http://www.hao123.com/")),
//				new Page(Url.parse("http://www.qq.com/")),
//				new Page(Url.parse("http://www.wanmei.com/")),
//				new Page(Url.parse("http://www.icefirer.com/")),
//				new Page(Url.parse("http://www.discuz.net/")),
//				new Page(Url.parse("http://www.newsmth.net/")),
//				new Page(Url.parse("http://www.phpwind.com/")),
//				new Page(Url.parse("http://www.tianya.cn/")),
		};

		for(int i = 0; i < pages.length * 1; i++)
			fetcher.fetch(pages[i % pages.length]);
		
		while(true) {
			boolean flag = true;
			for(Page p : pages) {
				if(!p.isReady()) {
					flag = false;
					break;
				}
			}
			if(flag)
				break;
		}
	}

	@Override
	public void onFetchingFinish(Page page) {
		page.ready();
		System.out.println(page.getUrl() + ":" + page.getHtmlContent().length);
	}
}