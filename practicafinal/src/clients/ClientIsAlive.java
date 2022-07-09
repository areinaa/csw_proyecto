package clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientIsAlive extends Thread {
	
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;

	
	
	public ClientIsAlive(Socket socket) {
		this.socket = socket;

		try {
			this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	
	public void run() {
		try {
			
			String linea = "";
			System.out.println("Abriendo keepalive");
			
			
			do{

				linea = br.readLine();
				
				if("KEEPALIVE".equals(linea)) {
					Thread.sleep(10000);
					pw.println("OK"); //COMENTAR
					pw.flush();
					//System.out.println("KEEPALIVE CHECK");
				}else {
					pw.println("???");
				}
				pw.flush();
				
				
			}while(!this.socket.isClosed());
			
			
			
			
		} catch (IOException | InterruptedException e) {e.printStackTrace();}
		
	}
	

}
