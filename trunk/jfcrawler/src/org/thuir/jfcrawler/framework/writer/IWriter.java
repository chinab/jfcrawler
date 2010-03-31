package org.thuir.jfcrawler.framework.writer;

import java.io.IOException;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public interface IWriter {
	
	public void write(Page page) throws IOException;

}
