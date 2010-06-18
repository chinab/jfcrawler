package org.thuir.forum.template;

import java.util.List;
import java.util.Map;

import org.thuir.forum.data.BoardInfo;
import org.thuir.forum.data.Info;
import org.thuir.forum.template.UrlPattern.UrlItem;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Board extends Vertex {

	public Board(Element e) {
		super(e);
		tag = Tag.BOARD;
		
		infoFactory = new BoardInfoFactory();
		infoFactory.setReference(pattern.getUrlItems());
	}
	
	public static class BoardInfoFactory extends InfoFactory {		
		@Override
		public Info extractInfo(Map<UrlItem, String> values) {
			BoardInfo info = new BoardInfo();
			if(!this.setParameters(info, values))
				return null;
			return info;
		}
		
		@Override
		public void setReference(List<UrlItem> list) {
			for(UrlItem i : list) {
				if(i.key.equalsIgnoreCase("id")) {
					idRef = i;
					continue;
				}
				if(i.key.equalsIgnoreCase("key")) {
					keyRef = i;
					continue;
				}
				if(i.key.equalsIgnoreCase("page")) {
					pageRef = i;
					continue;
				}
			}
		}
	}
}
