package org.thuir.jfcrawler.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import net.htmlparser.jericho.Source;

/**
 * @author ruKyzhc
 *
 */
public class Page {
	private Url url = null;

	private byte[] html = null;
	private String charset = "gbk";

	private boolean isReady = false;

	public Page() {
		isReady = false;
	}

	public Page(Url url) {
		this();
		this.url = url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public Url getUrl() {
		return this.url;
	}

	public byte[] getHtmlContent() {
		return html;
	}

	public String getCharset() {
		return charset;
	}

	public void load(byte[] response) {
		int len = response.length;
		this.html = new byte[len];
		System.arraycopy(response, 0, html, 0, len);

		charset = parseCharset(html);
	}

	private String parseCharset(byte[] src) {
		try {
			return new Source(new ByteArrayInputStream(html)).getEncoding();
		} catch (IOException e) {
			return "utf-8";
		}
	}

	public synchronized void ready() {
		isReady = true;
	}

	public synchronized boolean isReady() {
		return isReady;
	}
}
