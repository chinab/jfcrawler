package org.thuir.forum.data;

/**
 * @author ruKyzhc
 *
 */
public abstract class Info implements Comparable<Info>{
	//basic
//	protected InfoFactory factory = null;
	protected double evaluation = 0.0;
	
	protected String token = "";
	protected String key  = "";
	protected long   id   = -1;
	protected int    page = -1;
	
//	public Info(InfoFactory factory) {
//		this.factory = factory;
//	}
//	
//	public InfoFactory getFactory() {
//		return factory;
//	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	//statistic
	public abstract void synchronize(Info inlink);
	
	@Override
	public String toString() {
		return "[key:" + key + "][id:" + id + "][page:" + page + "]";
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String getToken() {
		return token;
	}
	
	public void evaluate(double eval) {
		eval = (eval > 1.0) ? 1.0:eval;
		eval = (eval < 0.0) ? 0.0:eval;
		this.evaluation = eval;
	}
	
	public int compareTo(Info info) {
		return (evaluation - info.evaluation) > 0 ? 1:-1;
	}
}
