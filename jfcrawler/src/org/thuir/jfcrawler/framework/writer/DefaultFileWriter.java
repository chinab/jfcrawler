package org.thuir.jfcrawler.framework.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.thuir.jfcrawler.data.Page;
import org.thuir.jfcrawler.util.ConfigUtil;
import org.thuir.jfcrawler.util.DynamicUrlUtil;

/**
 * @author ruKyzhc
 *
 */
public class DefaultFileWriter extends Writer {

	private String root = null;
	
	@Override
	public void setRoot(String job) {
		this.root = 
			ConfigUtil.getCrawlerConfig().getString("writer.root") 
			+ File.separatorChar + job;		
	}
	
	@Override
	public void write(Page page) throws IOException {
		File file = new File(root + File.separatorChar +
				DynamicUrlUtil.generatePath(page.getUrl()));
		File path = new File(file.getParent());
		
		if(!path.exists()) {
			path.mkdirs();
		}
		if(file.exists()) {
			System.err.println("[writer:file exists]" + page.getUrl());
		}
		FileOutputStream writer = new FileOutputStream(file);
		writer.write(page.getHtmlContent());
		writer.close();
	}
}
