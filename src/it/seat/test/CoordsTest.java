package it.seat.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CoordsTest {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for it.seat.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(CMTest.class);
		suite.addTestSuite(LLTest.class);
		suite.addTestSuite(XYTest.class);
		// $JUnit-END$
		return suite;
	}
}
