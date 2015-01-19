package it.seat.visual.model.coords;

import it.seat.visual.model.GeoUtils;

public class CM {
	private double x;
	private double y;

	public CM() {
	}

	public CM(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public UTM toUTM(int zoomLevel) {
		double easting = 637847.3 - (x * 4709238.7);
		double northing = 5671365.6 + (y * 4709238.7);
		UTM utm = new UTM((float) easting, (float) northing, zoomLevel);

		return utm;
	}

	public LL toLL(int zoomLevel) {
		UTM utm = toUTM(zoomLevel);
		LL navtech = utm.toLL();

		return navtech;
	}

	public XY toXY(int zoomLevel) {
		return toXY(zoomLevel, 1);
	}

	public XY toXY(int zoomLevel, double scalingFactor) {
		int mapSize = (int) (256 * GeoUtils.pow(2, 17 - zoomLevel) * /*1.86 */ scalingFactor);
		double xn = mapSize - (x + 0.5) * mapSize;
		double yn = mapSize - (y + 0.5) * mapSize;
		XY xy = new XY((int) xn, (int) yn);

		return xy;
	}

	public String toString() {
		return "(X, Y) = [" + x + ", " + y + "]";
	}
}
