package org.thuir.jfcrawler.data;

/**
 * @author ruKyzhc
 *
 */
public class Page {

	private PageUrl url = null;
	
	private byte[] html = null;
	
	public Page() {
		
	}
	
	public Page(PageUrl url) {
		this();
		this.url = url;
	}
	
	public void setPageUrl(PageUrl url) {
		this.url = url;
	}
	
	public PageUrl getPageUrl() {
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
