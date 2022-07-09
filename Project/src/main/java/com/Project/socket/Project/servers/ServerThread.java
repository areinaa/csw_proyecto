package com.Project.socket.Project.servers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.Project.socket.Project.elementos.*;

public class ServerThread extends Thread {
	
	private int id = 99;
	private ServerMain root = null;
	private Socket socket = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	private int number = 0;
	private String autentication = "0";
	private String user = "null";
	
	public ServerThread(int id, ServerMain root, Socket socket) {
		this.id = id;
		this.root = root;
		this.socket = socket;
			
		try {
			this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (IOException e) {e.printStackTrace();}
		
		
	}
	
	public void run() {
		String line = "";
		String request[];
		int port = 0;
		String idc = "null";
		try {
			ServerSocket ss = new ServerSocket(2120);
			Socket skeepAlive = ss.accept();
			KeepAlive keepAlive = new KeepAlive(this, skeepAlive, this.id);
			keepAlive.start();
			ss.close();
			
			//CANAL DE DATOS
			ServerSocket dataChannel;
			Socket datos;
			ObjectInputStream ios;
			ObjectOutputStream oos;
			//--------------
			
			System.out.println("C se ha lanzado correctamente");
			
			
			do {
				line = br.readLine();
				
				request = line.split(" ");
				this.number++;
				
				if(this.badFormat(request)) {
					pw.flush();
					continue;
				}
				
				
				if(this.number != Integer.parseInt(request[0])) {
					System.out.println("WARNING: Peticion perdida en hilo " + this.id + ": " + this.number + "|" + request[0]);
					this.number = Integer.parseInt(request[0]);
				}
				
				
				switch(request[1]) {
				case "USER":
					if(root.checkUser(request[2])) {
						//pw.println("OK " + this.number + " 201 Envie_contrase�a");
						System.out.println("Funci�n USER ha procedido correctamente");
						this.autentication = request[2];
						
						
					}else
						pw.println("FAILED " + this.number + " 401 Not_user");
					
					break;
				case "PASS":
					if(!"0".equals(autentication)) 
						if(root.loginUser(this.autentication,request[2])) {
							this.user = this.autentication;
							this.autentication = "2";
							pw.println("OK " + this.number + " 202 Welcome " + this.user);
							
						}else {
							this.autentication = "0";
							pw.println("FAILED " + this.number + " 403 Contrase�a_incorrecta");
						}
					else
						pw.println("FAILED " + this.number + " 402 Usuario_no_especificado");
					
					break;
				case "EXIT":
					pw.println("OK " + this.number + " 299 Bye");
					System.out.println("Conexi�n con cliente cerrada correctamente");
					
					break;
				case "ADDCLIENTE":
					port = root.reservedataPort();
					
					pw.println("PREOK " + this.number + " 203 127.0.0.1 " + port);
					pw.flush();
					
					dataChannel = new ServerSocket(port);
					datos = dataChannel.accept();
					ios = new ObjectInputStream(datos.getInputStream());
					
					Cliente cliente = (Cliente)ios.readObject();
					
					
					if(root.putCliente(cliente.getId(), cliente))
						pw.println("OK " + this.number + " 204 Transferencia_finalizada");
					else
						pw.println("FAILED " + this.number + " 405 Transferencia_fallida:El_elemento_ya_existe.");
					
					ios.close();
					datos.close();
					dataChannel.close();
					
					break;
				case "UPDATECLIENTE":
					port = root.reservedataPort();
					idc = request[2];
					
					pw.println("PREOK " + this.number + " 203 127.0.0.1 " + port);
					pw.flush();
					
					dataChannel = new ServerSocket(port);
					datos = dataChannel.accept();
					ios = new ObjectInputStream(datos.getInputStream());
					
					Cliente cliente2 = (Cliente)ios.readObject();
					
					if(root.updateCliente(idc, cliente2))
						pw.println("OK " + this.number + " 204 Transferencia_finalizada");
					else
						pw.println("FAILED " + this.number + " 405 Transferencia_fallida:El_elemento_no_existe.");
					
					ios.close();
					datos.close();
					dataChannel.close();
					
					break;
				case "GETCLIENTE":
					port = root.reservedataPort();
					if(root.getCliente(request[2]) != null) {
						pw.println("PREOK " + this.number + " 205 127.0.0.1 " + port);
						pw.flush();
						
						dataChannel = new ServerSocket(port);
						datos = dataChannel.accept();
						
						oos = new ObjectOutputStream(datos.getOutputStream());
						oos.writeObject(root.getCliente(request[2]));
						oos.flush();
						
						oos.close();
						datos.close();
						dataChannel.close();
						pw.println("OK " + this.number + " 206 Transferencia_finalizada");
					}else {
						pw.println("FAILED " + this.number + " 406 Transferencia_fallida:El_elemento_NO_existe ");
					}
					
					break;
				case "REMOVECLIENTE":
					if (root.removeCliente(request[2])) {
						pw.println("OK " + this.number + " 207 Cliente_Eliminado.");
					}else {
						pw.println("FAILED " + this.number + " 407 No_Autorizado.");
					}
					break;
				case "LISTCLIENTES":
					port = root.reservedataPort();
					if(root.countClientes() > 0) {
						pw.println("PREOK " + this.number + " 208 127.0.0.1 " + port);
						pw.flush();
						
						dataChannel = new ServerSocket(port);
						datos = dataChannel.accept();
						
						oos = new ObjectOutputStream(datos.getOutputStream());
						oos.writeObject(root.getListaClientes());
						oos.flush();
						
						oos.close();
						datos.close();
						dataChannel.close();
						
						pw.println("OK " + this.number + " 209 Transferencia_finalizada");
						
					}else {
						pw.println("FAILED " + this.number + " 408 Transferencia_fallida:No_hay_clientes_registrados ");
					}
					
					
					break;
				case "COUNTCLIENTES":
					if(!"null".equals(root.countClientes())) {
						pw.println("OK " + this.number + " 209 " + root.countClientes() + " ");
					}else {
						pw.println("FAILED " + this.number + " 409 Error_de_recuento");
					}
				
					break;
				case "ADDPRODUCTO":
					port = root.reservedataPort();
					
					pw.println("PREOK " + this.number + " 210 127.0.0.1 " + port);
					pw.flush();
					
					dataChannel = new ServerSocket(port);
					datos = dataChannel.accept();
					ios = new ObjectInputStream(datos.getInputStream());
					
					Producto producto = (Producto)ios.readObject();
					
					
					if(root.putProducto(producto.getId(), producto))
						pw.println("OK " + this.number + " 211 Transferencia_finalizada");
					else
						pw.println("FAILED " + this.number + " 410 Transferencia_fallida:El_elemento_ya_existe.");
					
					ios.close();
					datos.close();
					dataChannel.close();
					
					break;
				case "UPDATEPRODUCTO":
					port = root.reservedataPort();
					idc = request[2];
					
					pw.println("PREOK " + this.number + " 210 127.0.0.1 " + port);
					pw.flush();
					
					dataChannel = new ServerSocket(port);
					datos = dataChannel.accept();
					ios = new ObjectInputStream(datos.getInputStream());
					
					Producto producto2 = (Producto)ios.readObject();
					
					if(root.updateProducto(idc, producto2))
						pw.println("OK " + this.number + " 211 Transferencia_finalizada");
					else
						pw.println("FAILED " + this.number + " 411 Transferencia_fallida:El_elemento_no_existe.");
					
					ios.close();
					datos.close();
					dataChannel.close();
					
					break;
				case "GETPRODUCTO":
					port = root.reservedataPort();
					if(root.getProducto(request[2]) != null) {
						pw.println("PREOK " + this.number + " 212 127.0.0.1 " + port);
						pw.flush();
						
						dataChannel = new ServerSocket(port);
						datos = dataChannel.accept();
						
						oos = new ObjectOutputStream(datos.getOutputStream());
						oos.writeObject(root.getProducto(request[2]));
						oos.flush();
						
						oos.close();
						datos.close();
						dataChannel.close();
						pw.println("OK " + this.number + " 213 Transferencia_finalizada");
					}else {
						pw.println("FAILED " + this.number + " 412 Transferencia_fallida:El_elemento_NO_existe ");
					}
					
					break;
				case "REMOVEPRODUCTO":
					if (root.removeProducto(request[2])) {
						pw.println("OK " + this.number + " 214 Producto_Eliminado.");
					}else {
						pw.println("FAILED " + this.number + " 413 No_Autorizado.");
					}
					break;
				case "LISTPRODUCTOS":
					port = root.reservedataPort();
					if(root.countProductos() > 0) {
						pw.println("PREOK " + this.number + " 215 127.0.0.1 " + port);
						pw.flush();
						
						dataChannel = new ServerSocket(port);
						datos = dataChannel.accept();
						
						oos = new ObjectOutputStream(datos.getOutputStream());
						oos.writeObject(root.getListaProductos());
						oos.flush();
						
						oos.close();
						datos.close();
						dataChannel.close();
						
						pw.println("OK " + this.number + " 216 Transferencia_finalizada");
						
					}else {
						pw.println("FAILED " + this.number + " 414 Transferencia_fallida:No_hay_productos_registrados ");
					}
					
					break;
				case "COUNTPRODUCTOS":
					if(!"null".equals(root.countProductos())) {
						pw.println("OK " + this.number + " 217 " + root.countProductos() + " ");
					}else {
						pw.println("FAILED " + this.number + " 415 Error_de_recuento");
					}
					break;
				default:
					pw.println("FAILED " + this.number + " 400 FORMATO_DE_PETICION_NO_IMPLEMENTADO");
					break;
				}
				
				pw.flush();
				
				
			}while(!"EXIT".equals(request[1]));
			socket.close();
			this.interrupt();
			
			
		} catch (IOException | ClassNotFoundException e) {	e.printStackTrace();}
		
		
		
	}

	private boolean badFormat(String[] request) {
		if(request.length<2 && !"EXIT".equals(request[1])) {
			pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA");
			return true;
		}
			
		
		try {
			Integer.parseInt(request[0]);
		}catch(NumberFormatException nfe){
			pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:FALTA_NUMBER");
			return true;
		}
		
		switch(request[1]) {
		default:
			return false;
		case "USER":
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_USUARIO");
				return true;
			}
			break;
		case "PASS":
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_PASSWORD");
				return true;
			}
			break;
		case "ADDCLIENTE":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
				
			break;
		case "UPDATECLIENTE":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDCLIENTE");
				return true;
			}
			break;
		case "GETCLIENTE":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDCLIENTE");
				return true;
			}
			break;
		case "REMOVECLIENTE":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDCLIENTE");
				return true;
			}
			break;
		case "LISTCLIENTES":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			break;
		case "COUNTCLIENTES":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			break;
		case "ADDPRODUCTO":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			break;
		case "UPDATEPRODUCTO":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDPRODUCTO");
				return true;
			}
			
			break;
		case "GETPRODUCTO":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDPRODUCTO");
				return true;
			}
			
			break;
		case "REMOVEPRODUCTO":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			if(request.length<3) {
				pw.println("FAILED " + this.number + " 499 FORMATO_INCORRECTO:PETICION_INAPROPIADA_FALTA_IDPRODUCTO");
				return true;
			}
			
			break;
		case "LISTPRODUCTOS":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			break;
		case "COUNTPRODUCTOS":
			if(this.user.equals("null")) {
				pw.println("FAILED " + this.number + " 498 USUARIO NO LOGEADO");
				return true;
			}
			
			
			break;	
			
		}
		return false;
	}
	
	
	public void kill() {
		pw.println("FAILED " + this.number + " 300 INACTIVITY_TIMEOUT");
		pw.flush();
		try {
			socket.close();
			this.interrupt();
			
		} catch (IOException e) {e.printStackTrace();}
		
	}
	
	
}
