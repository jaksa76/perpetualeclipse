package perpetualeclipse.report;

import java.io.File;

import junit.framework.TestCase;

public class DummyTest extends TestCase {
	public void testSuccess() throws Exception {
		new File("C:\\temp\\testSuccess").createNewFile();
	}
	
	public void testFailure() throws Exception {
		new File("C:\\temp\\testFailure").createNewFile();
		fail();
	}
}
