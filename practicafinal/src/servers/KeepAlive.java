package servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class KeepAlive extends Thread {

	private ServerThread padre = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private int id = 0;
	private boolean started = false;
	private boolean timeouted = false;
	private KeepAliveResponses wait = new KeepAliveResponses(this);
	private String[] message = {"Error1", "Error2","Error3"};
	
	
	public KeepAlive(ServerThread padre, Socket socket, int id) {
		this.padre = padre;
		this.socket = socket;
		this.id = id;
		this.started = true;
	}
	
	
	
	
	public void run() {
		try {
			String linea = "";
			System.out.println("Abriendo keepalive");
			
			do {
				linea = "0";

				pw.println("KEEPALIVE");
				pw.flush();
				System.out.println("[" + this.id + "] KEEPALIVE");
				this.wait.start();
				linea = br.readLine();
				this.wait.interrupt();
				
			}while("OK".equals(linea));
			
			this.socket.close();
			this.timeout();
			
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	
	public void timeout() {
		
		System.out.println("TIMEOUT");
		
		this.interrupt();
	}
	
	public ServerThread getRootSocket() {
		return this.padre;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public int getSocketId() {
		return this.id;
	}
	
	public boolean timeouted() {
		return this.timeouted;
	}
	
	public boolean getStatus() {
		return this.started;
	}
	
	public KeepAliveResponses getKAResponses() {
		return this.wait;
	}
	
	public String[] getTimeoutMessage() {
		return this.message;
	}
	
}
