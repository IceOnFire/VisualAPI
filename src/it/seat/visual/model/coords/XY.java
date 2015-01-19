package it.seat.visual.model.coords;

import it.seat.visual.model.GeoUtils;

public class XY {
	private int x;
	private int y;

	public XY() {
	}

	public XY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public CM toCM(int zoomLevel) {
		return toCM(zoomLevel, 1);
	}

	public CM toCM(int zoomLevel, double scalingFactor) {
		int mapSize = (int) (256 * GeoUtils.pow(2, 17 - zoomLevel) * /*1.86 */ scalingFactor);
		double xn = 1.0 * (mapSize - x) / mapSize - 0.5;
		double yn = 1.0 * (mapSize - y) / mapSize - 0.5;
		CM cm = new CM(xn, yn);

		return cm;
	}

	public LL toLL(int zoomLevel) {
		return toLL(zoomLevel, 1);
	}

	public LL toLL(int zoomLevel, double scalingFactor) {
		CM cm = toCM(zoomLevel, scalingFactor);
		LL ll = cm.toLL(zoomLevel);

		return ll;
	}

	public LL toLL(LL centerNavtech, int zoomLevel, double scalingFactor) {
		XY centerXY = centerNavtech.toXY(zoomLevel, 1.86 * scalingFactor);
		XY xy = new XY(centerXY.getX() + x, centerXY.getY() - y);
		LL navtech = xy.toLL(zoomLevel, 1.86 * scalingFactor);

		return navtech;
	}

	public IJ toIJ(int tileWidth, int tileHeight) {
		IJ ij = new IJ(y / tileHeight, x / tileWidth);
		return ij;
	}

	public String toString() {
		return "(x, y) = [" + x + ", " + y + "]";
	}
}
