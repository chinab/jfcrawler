package org.thuir.forum.template;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.thuir.forum.data.ArticleInfo;
import org.thuir.forum.data.Info;
import org.thuir.forum.template.UrlPattern.UrlItem;
import org.w3c.dom.Element;

/**
 * @author ruKyzhc
 *
 */
public final class Article extends Vertex {

	public Article(Element e) {
		super(e);
		tag = Tag.ARTICLE;

		infoFactory = new ArticleInfoFactory();
		infoFactory.setReference(pattern.getUrlItems());
	}
	
	public static class ArticleInfoFactory extends InfoFactory {
		private static Logger logger = Logger.getLogger(ArticleInfoFactory.class);
	
		private UrlItem boardKeyRef = null;
		private UrlItem boardIdRef  = null;
		private UrlItem positionRef = null;

		@Override
		public Info extractInfo(Map<UrlItem, String> values) {
			ArticleInfo info = new ArticleInfo();
			if(!this.setParameters(info, values))
				return null;
			String value = null;
			String token = info.getToken();
			try {				
				if(boardIdRef != null && (value = values.get(boardIdRef)) != null) {
					info.setBoardId(Long.parseLong(value));
					token += "[" + value + "]";
				}
				if(boardKeyRef != null && (value = values.get(boardKeyRef)) != null) {
					info.setBoardKey(value);
					token += "[" + value + "]";
				}
				if(positionRef != null && (value = values.get(positionRef)) != null) {
					info.setPosition(Integer.parseInt(value));
					token += "[" + value + "]";
				}
				info.setToken(token);
			} catch(Exception e) {
				logger.error(e);
				return null;
			}
			return info;
		}

		public void setBoardKeyRef(UrlItem boardKeyRef) {
			this.boardKeyRef = boardKeyRef;
		}

		public void setBoardIdRef(UrlItem boardIdRef) {
			this.boardIdRef = boardIdRef;
		}

		public void setPositionRef(UrlItem positionRef) {
			this.positionRef = positionRef;
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
				
				if(i.key.equalsIgnoreCase("boardId")) {
					boardIdRef = i;
					continue;
				}
				if(i.key.equalsIgnoreCase("boardKey")) {
					boardKeyRef = i;
					continue;
				}
				if(i.key.equalsIgnoreCase("position")) {
					positionRef = i;
					continue;
				}
			}
		}
		
//		@Override
//		public String getCheckStmt() {
//			return null;
//		}
//
//		@Override
//		public void setCheckStmt(PreparedStatement stmt, Info info) {
//			
//		}
//		
	}
}
