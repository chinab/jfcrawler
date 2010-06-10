package org.thuir.jfcrawler.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.thuir.jfcrawler.util.ConfigUtil;

/**
 * @author ruKyzhc
 *
 */
public class Page {
	private static Logger logger = Logger.getLogger(Page.class);
	
	private static Pattern pattern = 
		Pattern.compile("charset=([\\w|\\d|-]+)", Pattern.CASE_INSENSITIVE);
	
	private Url url = null;

	private byte[] html = null;
	private String charset = ConfigUtil.getConfig().getString("basic.default-encode");

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

	public String getCharset() {
		return charset;
	}

	public void load(byte[] response) {
		int len = response.length;
		this.html = new byte[len];
		System.arraycopy(response, 0, html, 0, len);

		charset = parseCharset(html);
	}
	
	public Url redirect(String uri) {
		try {
			this.url = Url.parseWithParent(url, uri);
		} catch (BadUrlFormatException e) {
			return null;
		}
		return this.url;
	}

	private static String parseCharset(byte[] src) {
		BufferedReader reader = 
			new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(src)));
		String line = "";
		String charset = ConfigUtil.getConfig().getString("basic.default-encode");
		String temp = "";
		try {
			while((line = reader.readLine()) != null) {
				Matcher m = pattern.matcher(line); 
				if(m.find()) {
					temp = m.group(1);
					if(Charset.isSupported(temp))
						return temp;
					else
						return charset;
				}
			}
		} catch (IOException e) {
			logger.error("cannot get charset.", e);
			return charset;
		}
		return charset;
	}
}
