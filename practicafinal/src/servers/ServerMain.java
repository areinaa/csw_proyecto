package servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import elementos.*;

public class ServerMain {
	
	private static ServerMain servermain = null;
	private ServerSocket serverSocket = null;
	private int contador = 0;
	private int dataPort = 2022;
	
	private ArrayList<ServerThread> lista = new ArrayList<ServerThread>();
	public Hashtable<String, String> users = new Hashtable<String,String>();
	private Hashtable<String, Cliente> clientes = new Hashtable<String, Cliente>();
	private Hashtable<String, Producto> productos = new Hashtable<String, Producto>();
	
	
	public void execute() {
		System.out.println("-----AREINA DATABASE TM-----");
		
		users.put("admin", "admin");
		
		
		
		
		try {
			serverSocket = new ServerSocket(2022);
			
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Conexión con A establecida.");
				ServerThread hilo = new ServerThread(contador,this,socket);
				
				hilo.start();
				System.out.println("B ha lanzado hilo C");
				
				this.contador++;
				lista.add(hilo);
			}
			System.out.println("FIN");
			
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void terminate() {
		try {
			this.serverSocket.close();
			
		} catch (IOException e) {e.printStackTrace();}
	}

	public Boolean checkUser(String user) {
		if(users.containsKey(user))
			return true;
		else
			return false;
	}
	
	public Boolean loginUser(String user, String pass) {
		if(pass.equals(users.get(user)))
			return true;
		else
			return false;
	}
	
	
	public static void main (String[] args) {
		ServerMain server = new ServerMain();
		servermain = server;
		servermain.execute();
	}

	public int reservedataPort() {
		this.dataPort++;
		return this.dataPort;
	}
	
	public boolean putProducto(String id, Producto producto) {
		if(!productos.containsKey(id)) {
			productos.put(id, producto);
			return true;
		}else {
			return false;
		}

	}
	
	public boolean updateProducto(String id, Producto producto) {
		if(productos.containsKey(id)) {
			productos.put(id, producto);
			return true;
		}else {
			return false;
		}
	}

	public Object getProducto(String key) {
		if(productos.containsKey(key))
			return productos.get(key);
		else
			return null;
	}
	
	public Boolean removeProducto(String key) {
		if(productos.containsKey(key)) {
			productos.remove(key);
			return true;
		}else {
			return false;
		}
	}
	
	public int countProductos() {
		return productos.size();
	}
	
	public Hashtable<String, Producto> getListaProductos() {
		return productos;
	}
	
	public boolean putCliente(String id, Cliente cliente) {
		if(!clientes.containsKey(id)) {
			clientes.put(id, cliente);
			return true;
		}else {
			return false;
		}

	}
	
	public boolean updateCliente(String id, Cliente cliente) {
		if(clientes.containsKey(id)) {
			clientes.put(id, cliente);
			return true;
		}else {
			return false;
		}
	}

	public Object getCliente(String key) {
		if(clientes.containsKey(key))
			return clientes.get(key);
		else
			return null;
	}
	
	public Boolean removeCliente(String key) {
		if(clientes.containsKey(key)) {
			clientes.remove(key);
			return true;
		}else {
			return false;
		}
	}
	
	public int countClientes() {
		return clientes.size();
	}
	
	public Hashtable<String, Cliente> getListaClientes() {
		return clientes;
	}
	
	
	
}
