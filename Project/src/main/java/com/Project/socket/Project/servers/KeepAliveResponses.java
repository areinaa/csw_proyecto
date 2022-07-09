package com.Project.socket.Project.servers;

public class KeepAliveResponses extends Thread {
	
	private KeepAlive raiz = null;
	
	public KeepAliveResponses(KeepAlive raiz) {
		this.raiz = raiz;
	}
	
	
	
	
	
	
	public void run() {
		
		try {

			Thread.sleep(30000);
			raiz.timeout();
			this.interrupt();
			
		} catch (InterruptedException e) {
			//e.printStackTrace();
			this.interrupt();
			}
		
	}

}
