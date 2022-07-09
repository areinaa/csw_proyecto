package servers;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

public class TestJUnit {
	
	private int id = 0;
	private String[] timeoutMessage = {"Error1: Desc", "Error2: Desc","Error3: Desc"};
	
	
	
	static KeepAlive keepAlive = new KeepAlive(null, null, 2);
	
	@AfterClass
	public static void finalizar () {
		keepAlive.timeout();
	}
	
	@BeforeClass
	public static void setUp () {
		keepAlive.start();
	}
	
	
	@Ignore
	@Test
	public void testSocketNull  () {
		assertNotNull(keepAlive.getSocket());
	}
	
	@Test
	public void testRootThread  () {
		assertNull(keepAlive.getRootSocket());
	}
	
	@Test
	public void testSocketId  () {
		assertEquals(this.id, keepAlive.getSocketId());
	}

	@Test
	public void testTimeoutFunction  () {
		assertFalse(keepAlive.timeouted());
	}
	
	@Test
	public void testStarted() {
		assertTrue(keepAlive.getStatus());
	}
	
	@Test
	public void testThreadisRoot() {	
		assertSame(keepAlive.getClass() , keepAlive.getRootSocket());
	}
	
	@Test
	public void testThreadsNotSame() {
		assertNotSame(keepAlive.getKAResponses(),keepAlive.getClass());
	}
	
	@Test
	public void testArrayEquals() {
		assertArrayEquals(this.timeoutMessage, keepAlive.getTimeoutMessage());
	}

}
