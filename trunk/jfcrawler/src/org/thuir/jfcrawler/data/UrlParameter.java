package org.thuir.jfcrawler.data;

import java.io.Serializable;

/**
 * @author ruKyzhc
 *
 */
public final class UrlParameter implements Serializable {
	
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -664061118486665562L;

	private String key;
	
	private String value;
	
	public UrlParameter() {
		this.key = "";
		this.value = "";
	}
	
	public UrlParameter(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return key + '=' + value;
	}
	
	public static UrlParameter parse(String str) {
		int pointer = str.indexOf('=');
		if(pointer < 0)
			return null;
		UrlParameter p = new UrlParameter();
		p.setKey(str.substring(0, pointer));
		p.setValue(str.substring(pointer + 1));
		return p;
	}
	
}
