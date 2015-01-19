package it.seat.test;

import it.seat.visual.model.coords.CM;
import it.seat.visual.model.coords.LL;
import it.seat.visual.model.coords.XY;
import junit.framework.TestCase;

public class XYTest extends TestCase {
	private XY romaXY = new XY(272, 367);
	private XY milanoXY = new XY(242, 324);
	private XY rossanoXY = new XY(312, 393);
	private LL romaNavtech = new LL(41.89f, 12.49f);
	private LL milanoNavtech = new LL(45.4986f, 9.1223f);
	private LL rossanoNavtech = new LL(16.63473f, 39.57589f);
	private XY romaDelta = new XY();
	private XY milanoDelta = new XY(-113, 157);
	private XY rossanoDelta = new XY(148, -94);
	private int zoomLevel = 16;

	public void testToCM(XY xy) {
		CM cm = xy.toCM(zoomLevel);
		XY result = cm.toXY(zoomLevel);
		assertEquals(xy.getX(), result.getX(), 0.000001f);
		assertEquals(xy.getY(), result.getY(), 0.0001f);
	}

	public void testToNavtech(XY xy) {
		LL navtech = xy.toLL(zoomLevel);
		XY result = navtech.toXY(zoomLevel);
		assertEquals(xy.getX(), result.getX(), 1f);
		assertEquals(xy.getY(), result.getY(), 1f);
	}

	public void testToCM() {
		testToCM(romaXY);
		testToCM(milanoXY);
		testToCM(rossanoXY);
	}

	public void testToNavtech() {
		testToNavtech(romaXY);
		testToNavtech(milanoXY);
		testToNavtech(rossanoXY);
	}

	public void testDeltaToNavtech() {
		LL result = romaDelta.toLL(romaNavtech, zoomLevel, 2);
		System.out.println(romaNavtech);
		System.out.println(romaDelta);
		System.out.println(result);
		assertEquals(romaNavtech.getLon(), result.getLon(), 0.1);
		assertEquals(romaNavtech.getLat(), result.getLat(), 0.1);
		
		result = milanoDelta.toLL(romaNavtech, zoomLevel, 2);
		System.out.println(milanoNavtech);
		System.out.println(milanoDelta);
		System.out.println(result);
		assertEquals(milanoNavtech.getLon(), result.getLon(), 10);
		assertEquals(milanoNavtech.getLat(), result.getLat(), 10);

		result = rossanoDelta.toLL(romaNavtech, zoomLevel, 2);
		System.out.println(rossanoDelta);
		System.out.println(result);
		assertEquals(rossanoNavtech.getLon(), result.getLon(), 30);
		assertEquals(rossanoNavtech.getLat(), result.getLat(), 30);
	}
}
