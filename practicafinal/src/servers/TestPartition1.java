package servers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;


public class TestPartition1 {
	
	
	
	static ServerMain serverMain = new ServerMain();
	

	@BeforeClass
	public static void setUp () {
		serverMain.users.put("admin", "admin");
	}
	

	@Test
	public void testCheckUser  () {
		assertTrue(serverMain.checkUser("admin"));
		assertFalse(serverMain.checkUser("A"));
		
	}
	
	@Test
	public void testLoginUser  () {
		assertTrue(serverMain.loginUser("admin", "admin"));
		assertFalse(serverMain.loginUser("A" , "a"));
		
	}

	
}
