package org.thuir.jfcrawler.util;

public abstract class BasicThread extends Thread {
	private boolean alive = true;
	private boolean idle = true;
	
	@Override
	public void run() {
		setAlive(true);
	}
	
	protected synchronized void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	protected synchronized boolean alive() {
		return alive;
	}
	
	protected synchronized void setIdle(boolean idle) {
		this.idle = idle;
	}
	
	public synchronized boolean idle() {
		return idle;
	}
	
	public void close() {
		setAlive(false);
	}

}
