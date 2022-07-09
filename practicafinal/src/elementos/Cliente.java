package elementos;

import java.io.Serializable;

public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id = "";
	private String info = "";
	
	public Cliente(String id, String info) {
		
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
		return "ID:" + this.id +" INFO:" + this.info;
	}
	
}
