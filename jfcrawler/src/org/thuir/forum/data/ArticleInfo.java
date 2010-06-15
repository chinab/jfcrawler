package org.thuir.forum.data;

/**
 * @author ruKyzhc
 *
 */
public class ArticleInfo extends Info {
//	public ArticleInfo(InfoFactory factory) {
//		super(factory);
//	}

	//basic
	private String boardKey = "";
	private long   boardId  = -1;
	private int    position = -1;
	
	public String getBoardKey() {
		return boardKey;
	}
	public void setBoardKey(String boardKey) {
		this.boardKey = boardKey;
	}
	public long getBoardId() {
		return boardId;
	}
	public void setBoardId(long boardId) {
		this.boardId = boardId;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void synchronize(Info inlink) {
		if(inlink instanceof BoardInfo) {
			BoardInfo board = (BoardInfo)inlink;
			
			long tempL = (board.id == -1l) ? boardId : board.id;
			board.id   = boardId  = tempL;
			
			String tempS = (board.key == null) ? boardKey : board.key;
			board.key  = boardKey = tempS;
			
			int tempI = (board.page == -1) ? position : board.page;
			board.page = position = tempI;
		}
	}
	
	@Override
	public String toString() {
		return "[article]" + super.toString() + 
		"[board.key:" + boardKey + "][board.id:" + boardId + "][position:" + position + "]";
	}
}
