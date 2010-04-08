package org.thuir.jfcrawler.framework.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.util.CrawlerConfiguration;
import org.thuir.jfcrawler.util.DynamicUrlUtil;

/**
 * @author ruKyzhc
 *
 */
public class DefaultFileWriter extends Writer {

	private String root = null;
	
	@Override
	public void setRoot(String job) {
		this.root = CrawlerConfiguration.getWriterRoot() + "\\" + job;		
	}
	
	@Override
	public void write(Page page) throws IOException {
		File file = new File(root + "\\" +
				DynamicUrlUtil.generatePath(page.getPageUrl()));
		File path = new File(file.getParent());
		
		if(!path.exists()) {
			path.mkdirs();
		}
		FileOutputStream writer = new FileOutputStream(file);
		writer.write(page.getHtmlContent());
		writer.close();
		System.out.println("[url]" + page.getPageUrl());
	}
}
