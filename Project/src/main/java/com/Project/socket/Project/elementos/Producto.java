package com.Project.socket.Project.elementos;

import java.io.Serializable;

public class Producto implements Serializable {


	private static final long serialVersionUID = 1L;
	private String id = "";
	private String info = "";
	
	public Producto(String id, String info) {
		
		this.id = id;
		this.info = info;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public String toString() {
		return "IDPROD:" + this.id +" INFOPROD:" + this.info;
	}
	
}
