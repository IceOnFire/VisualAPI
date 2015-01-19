package it.seat.test;

import it.seat.visual.model.coords.CM;
import it.seat.visual.model.coords.LL;
import it.seat.visual.model.coords.XY;
import junit.framework.TestCase;

public class CMTest extends TestCase {
	private CM cmRoma = new CM(-0.032332323f, -0.21685323f);
	private CM cmMilano = new CM(0.027242664f, -0.13442037f);

	public void testToNavtech(CM cm) {
		LL navtech = cm.toLL(15);
		CM result = navtech.toCM(15);
		assertEquals(cm.getX(), result.getX(), 0.000001f);
		assertEquals(cm.getY(), result.getY(), 0.0001f);
	}

	public void testToXY(CM cm) {
		XY xy = cm.toXY(15);
		CM result = xy.toCM(15);
		assertEquals(cm.getX(), result.getX(), 0.001f);
		assertEquals(cm.getY(), result.getY(), 0.001f);
	}

	public void testToNavtech() {
		testToNavtech(cmRoma);
		testToNavtech(cmMilano);
	}

	public void testToXY() {
		testToXY(cmRoma);
		testToXY(cmMilano);
	}
}
