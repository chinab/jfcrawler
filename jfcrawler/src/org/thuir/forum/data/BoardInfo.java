package org.thuir.forum.data;

/**
 * @author ruKyzhc
 *
 */
public class BoardInfo extends Info {
	//basic
	
	//statistic
	private int articleCount = 0;

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	@Override
	public void synchronize(Info inlink) {
		
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}