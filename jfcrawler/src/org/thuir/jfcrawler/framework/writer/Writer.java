package org.thuir.jfcrawler.framework.writer;

import java.io.IOException;

import org.thuir.jfcrawler.data.Page;

/**
 * @author ruKyzhc
 *
 */
public abstract class Writer {
	
	public abstract void write(Page page) throws IOException ;
	
}
