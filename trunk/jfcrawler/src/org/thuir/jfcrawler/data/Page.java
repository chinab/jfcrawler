package org.thuir.jfcrawler.data;

/**
 * @author ruKyzhc
 *
 */
public class Page {

	private Url url = null;
	
	private byte[] html = null;
	
	public Page() {
		
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
	
	public void load(byte[] response) {
		int len = response.length;
		this.html = new byte[len];
		System.arraycopy(response, 0, html, 0, len);
	}
}
