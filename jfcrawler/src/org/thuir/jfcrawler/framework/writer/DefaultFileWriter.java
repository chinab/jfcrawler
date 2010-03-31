package org.thuir.jfcrawler.framework.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.data.PageUrl;
import org.thuir.jfcrawler.util.CrawlerConfiguration;

/**
 * @author ruKyzhc
 *
 */
public class DefaultFileWriter extends Writer {

	private File root = null;
	
	public DefaultFileWriter() {
		this.root = new File(CrawlerConfiguration.getWriterRoot());
	}
	
	@Override
	public void write(Page page) throws IOException {
		File file = generatePath(page.getPageUrl());
		
		FileWriter writer = new FileWriter(file);
		writer.append(page.getHtmlContent());
	}
	
	private File generatePath(PageUrl url) {
		return null;
	}

}
