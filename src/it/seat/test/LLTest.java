package it.seat.test;

import it.seat.visual.model.coords.CM;
import it.seat.visual.model.coords.LL;
import it.seat.visual.model.coords.XY;
import junit.framework.TestCase;

public class LLTest extends TestCase {
	private LL romaNavtech = new LL(12.5f, 41.95f);
	private LL milanoNavtech = new LL(9.1223f, 45.4986f);
	private LL rossanoNavtech = new LL(16.63473f, 39.57589f);
	private XY romaDelta = new XY();
	private XY milanoDelta = new XY(-113, 157);
	private XY rossanoDelta = new XY(148, -94);
	private int zoomLevel = 16;

	public void testToCM(LL navtech) {
		CM cm = navtech.toCM(zoomLevel);
		LL result = cm.toLL(zoomLevel);
		assertEquals(navtech.getLon(), result.getLon(), 0.001f);
		assertEquals(navtech.getLat(), result.getLat(), 0.001f);
	}

	public void testToXY(LL navtech) {
		XY xy = navtech.toXY(zoomLevel);
		LL result = xy.toLL(zoomLevel);
		assertEquals(navtech.getLon(), result.getLon(), 0.1f);
		assertEquals(navtech.getLat(), result.getLat(), 0.1f);
	}

	public void testToCM() {
		testToCM(romaNavtech);
		testToCM(milanoNavtech);
		testToCM(rossanoNavtech);
	}

	public void testToXY() {
		testToXY(romaNavtech);
		testToXY(milanoNavtech);
		testToXY(rossanoNavtech);
	}

	public void testToDelta() {
		XY result = romaNavtech.toDelta(romaNavtech, zoomLevel, 2);
		assertEquals(romaDelta.getX(), result.getX(), 5);
		assertEquals(romaDelta.getY(), result.getY(), 5);
		
		result = milanoNavtech.toDelta(romaNavtech, zoomLevel, 2);
		assertEquals(milanoDelta.getX(), result.getX(), 5);
		assertEquals(milanoDelta.getY(), result.getY(), 5);
		
		result = rossanoNavtech.toDelta(romaNavtech, zoomLevel, 2);
		assertEquals(rossanoDelta.getX(), result.getX(), 5);
		assertEquals(rossanoDelta.getY(), result.getY(), 5);
	}
}
