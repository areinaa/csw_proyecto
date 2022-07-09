package servers;

import org.junit.Test;

import elementos.Cliente;
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class TestPartition3 {
	
	
	
	static ServerMain serverMain = new ServerMain();
	

	@BeforeClass
	public static void setUp () {
		serverMain.putCliente("0", new Cliente("0", "AAAAAA"));
	}
	
	
	@Test
	public void testPutCliente  () {
		assertTrue(serverMain.putCliente("1", new Cliente("1", "aaaaa")));
		assertFalse(serverMain.putCliente("1" , new Cliente("1","BBBBB")));
		
	}
	
	@Test
	public void testUpdateCliente  () {
		serverMain.putCliente("0", new Cliente("0", "AAAAAA"));
		
		assertTrue(serverMain.updateCliente("0", new Cliente("0", "aBBBaaaa")));
		assertFalse(serverMain.updateCliente("423123", new Cliente("423123", "aBBBaaaa")));
		
	}
	
	@Test
	public void testGetCliente  () {
		serverMain.putCliente("0", new Cliente("0", "AAAAAA"));
		
		assertNull(serverMain.getCliente("123"));
		assertNotNull(serverMain.getCliente("0"));
		
	}
	
	@Test
	public void testRemoveCliente  () {
		assertTrue(serverMain.removeCliente("0"));
		assertFalse(serverMain.removeCliente("123"));
		
	}
	
	public void testCountAndList () {
		assertNotNull(serverMain.getListaClientes());
		assertNotNull(serverMain.countClientes());
	}
	
	
	
	
	
}
