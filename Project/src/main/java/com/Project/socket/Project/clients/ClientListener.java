package com.Project.socket.Project.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;

import com.Project.socket.Project.elementos.*;



public class ClientListener extends Thread {

	private Socket socket = null;
	private ClientMain clientMain = null;
	
	public ClientListener(Socket socket, ClientMain clientMain) {
		this.socket = socket;
		this.clientMain = clientMain;
	}
	
	
	public void run() {
	
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String linea = "";
			String[] partes;
			
			
			do {
				linea = br.readLine();
				partes = linea.split(" ");
				
				if(clientMain.getNumber() != Integer.parseInt(partes[1]))
					System.out.println("WARNING: Numero de respuesta no coincidente. C:" + clientMain.getNumber() + "|S:" + partes[1]);
				
				 switch(partes[0]) {
				case "OK":
					
					break;
				case "PREOK":
					if("203".equals(partes[2])) { //MODO ADDCLIENTE UPDATECLIENTE
						
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						Cliente cliente = new Cliente(partes[1], partes[4]);
						
						ObjectOutputStream oos = new ObjectOutputStream(dataChannel.getOutputStream());
						oos.writeObject(cliente);
						oos.flush();
						oos.close();
						dataChannel.close();
					}else if("205".equals(partes[2])) { //MODO GETCLIENTE
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						
						ObjectInputStream ios = new ObjectInputStream(dataChannel.getInputStream());
						
						Cliente cliente = (Cliente)ios.readObject();
						System.out.println("id del cliente: " + cliente.getId() + ", Info del cliente: " + cliente.getInfo());
						dataChannel.close();
						
					}else if("208".equals(partes[2])) { //MODO LISTCLIENTES
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						
						ObjectInputStream ios = new ObjectInputStream(dataChannel.getInputStream());
						
						@SuppressWarnings("unchecked")
						Hashtable<String, Cliente> clientes = (Hashtable<String, Cliente>)ios.readObject();
						System.out.println(clientes.toString());
						dataChannel.close();
					
					}else if("210".equals(partes[2])) { //MODO ADDPRODUCTO UPDATEPRODUCTO
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						Producto producto = new Producto(partes[1], partes[4]);
						
						ObjectOutputStream oos = new ObjectOutputStream(dataChannel.getOutputStream());
						oos.writeObject(producto);
						oos.flush();
						oos.close();
						dataChannel.close();
						
					}else if("212".equals(partes[2])) { //MODO GETPRODUCTO
						
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						
						ObjectInputStream ios = new ObjectInputStream(dataChannel.getInputStream());
						
						Producto producto = (Producto)ios.readObject();
						System.out.println("id del producto: " + producto.getId() + ", Info del producto: " + producto.getInfo());
						dataChannel.close();
						
					}else if("215".equals(partes[2])) { //MODO LISTPRODUCTOS
						Socket dataChannel = new Socket(partes[3], Integer.parseInt(partes[4]));
						
						ObjectInputStream ios = new ObjectInputStream(dataChannel.getInputStream());
						
						@SuppressWarnings("unchecked")
						Hashtable<String, Producto> productos = (Hashtable<String, Producto>)ios.readObject();
						System.out.println(productos.toString());
						dataChannel.close();
					}
					break;
				case "FAILED":
					
					break;
				default:
					System.out.println("ERROR: Formato de respuesta no compatible.");
					break;
				} 
				
				
				System.out.println(linea);
				
			}while(!"299".equals(partes[2]) || !"300".equals(partes[2]));
			socket.close();
			this.interrupt();
			
		} catch (IOException | ClassNotFoundException e) {e.printStackTrace();}
		
	
	}
	
	
	
	
}
