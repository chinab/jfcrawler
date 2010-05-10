package org.thuir.forum.template;

import java.util.regex.Pattern;

import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public class UrlPattern {
	public static enum Type {
		REGEX,
		INCLUDE,
		EXCLUDE,
		UNKNOWN,
	}
	
	private Type type = Type.UNKNOWN;
	private String source = null;
	
	private Pattern regex = null;
	
	public UrlPattern(Element e) {
		this.type = Type.valueOf(e.getAttribute("type"));
		source = e.getTextContent();
		
		if(type == Type.REGEX)
			regex = Pattern.compile(source);
	}
	
	public boolean match(String url) {
		switch(type) {
		case REGEX:
			return regex.matcher(url).matches();
		case INCLUDE:
			return url.contains(source);
		case EXCLUDE:
			return !url.contains(source);
		default:
			return true;
		}
	}

}
