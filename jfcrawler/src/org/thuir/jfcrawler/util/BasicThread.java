package org.thuir.jfcrawler.util;

public abstract class BasicThread extends Thread {
	private boolean alive = false;
	
	protected void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	@Override
	public void run() {
		setAlive(true);
	}
	
	protected boolean alive() {
		return alive;
	}
	
	public void close() {
		setAlive(false);
	}

}
