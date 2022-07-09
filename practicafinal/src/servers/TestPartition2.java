package servers;

import org.junit.Test;
import elementos.Producto;
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class TestPartition2 {
	
	
	
	static ServerMain serverMain = new ServerMain();
	

	@BeforeClass
	public static void setUp () {
		serverMain.putProducto("0", new Producto("0", "AAAAAAA"));
	}
	


	@Test
	public void testPutProducto  () {
		assertTrue(serverMain.putProducto("1", new Producto("1", "aaaaa")));
		assertFalse(serverMain.putProducto("1" , new Producto("1","BBBBB")));
		
	}
	
	@Test
	public void testUpdateProducto  () {
		assertTrue(serverMain.updateProducto("0", new Producto("0", "aBBBaaaa")));
		assertFalse(serverMain.updateProducto("423123", new Producto("423123", "aBBBaaaa")));
		
	}
	
	@Test
	public void testGetProducto  () {
		assertNull(serverMain.getProducto("123"));
		assertNotNull(serverMain.getProducto("0"));
		
	}
	
	@Test
	public void testRemoveProducto  () {
		assertTrue(serverMain.removeProducto("0"));
		assertFalse(serverMain.removeProducto("123"));
		
	}
	
	public void testCountAndList () {
		assertNotNull(serverMain.getListaProductos());
		assertNotNull(serverMain.countProductos());
	}
	
	
	
	
	
}
