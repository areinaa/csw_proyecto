package clients;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {

	private int number = 0;
	
	public void execute() throws InterruptedException {
		System.out.println("-----CLIENTE------");
		
		try {
			Socket socket = new Socket("127.0.0.1",2022);
			ClientListener listener = new ClientListener(socket, this);
			System.out.println("Estableciendo conexión de A a B");
			listener.start();
			
			Socket KAsocket = new Socket("127.0.0.1",2120);
			ClientIsAlive isAlive = new ClientIsAlive(KAsocket);
			isAlive.start();
			
			Scanner teclado = new Scanner(System.in);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String msg= "";
			
		
			while(!socket.isClosed()) {
				msg = teclado.nextLine();
				this.number++;
				
				pw.println(this.number + " " + msg);
				
/*				
				pw.println(this.number + " " + "USER admin");
				pw.flush();
				pw.println(this.number + " " + "PASS admin");
				pw.flush();
				pw.println(this.number + " " + "ADDCLIENTE");
				pw.flush();
				pw.println(this.number + " " + "GETCLIENTE 1");
				pw.flush();
				
				*/
				
			}
			teclado.close();
			System.out.println("-------------SALIDAS-------------");
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		ClientMain clientMain = new ClientMain();
		clientMain.execute();
	}


	public int getNumber() {
		return number;
	}
	
	
}
