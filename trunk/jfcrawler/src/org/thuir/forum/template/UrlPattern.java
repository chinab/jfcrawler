package org.thuir.forum.template;

import java.util.regex.Pattern;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class UrlPattern {
	public static enum Type {
		REGEX,
		TOKEN,
		UNKNOWN,
	}
	
	private Type type = Type.UNKNOWN;
	
	private Pattern regex = null;
	
	/**
	 * 0:unknown;<br/>
	 * 1:${token}-123-1.html;<br/>
	 * 2:forum/${token}/page.php;<br/>
	 * 3:${token}.php?bid=1;<br/>
	 * 4:bbs.php?${token}=1;<br/>
	 */
	private int tokenType = 0;
	private String token = null;
	
	public UrlPattern(Element e) {
		try {
			type = Type.valueOf(e.getAttribute("type"));
		} catch(Exception e1) {
			type = Type.UNKNOWN;
		}
	}
	
	public boolean match(String uri) {
		switch(type) {
		case REGEX:
			return matchRegex(uri);
		case TOKEN:
			return matchSubstring(uri);
		default:
			return false;
		}
	}
	
	private boolean matchRegex(String uri) {
		return regex.matcher(uri).matches();
	}
	
	private boolean matchSubstring(String uri) {
		switch(tokenType) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			return uri.toLowerCase().contains(token);
		default:
			return false;
		}
	}
}
