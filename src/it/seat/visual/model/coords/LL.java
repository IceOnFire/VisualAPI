package it.seat.visual.model.coords;

import it.seat.visual.model.GeoUtils;

public class LL {
	private float lon;
	private float lat;

	public LL() {
	}

	public LL(float lon, float lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	private UTM toUTM(int z) {
		// Make sure the longitude is between -180.00 .. 179.9
		double lonTemp = (lon + 180) - Math.floor((lon + 180) / 360) * 360
				- 180;

		double lonRad = lonTemp * GeoUtils.deg2rad;
		double latRad = lat * GeoUtils.deg2rad;

		// if( LongTemp >= 3.0 && LongTemp < 12.0 ) ZoneNumber = 32;
		// else ZoneNumber = 33;
		int zoneNumber = 32;

		double lonOrigin = (zoneNumber - 1) * 6 - 180 + 3; // +3 puts origin in
		// middle of zone
		double lonOriginRad = lonOrigin * GeoUtils.deg2rad;

		double eccPrimeSquared = (GeoUtils.eccSquared)
				/ (1 - GeoUtils.eccSquared);

		double N = GeoUtils.a
				/ Math.sqrt(1 - GeoUtils.eccSquared * Math.sin(latRad)
						* Math.sin(latRad));
		double T = Math.tan(latRad) * Math.tan(latRad);
		double C = eccPrimeSquared * Math.cos(latRad) * Math.cos(latRad);
		double A = Math.cos(latRad) * (lonRad - lonOriginRad);
		double M = GeoUtils.a
				* ((1 - GeoUtils.eccSquared / 4 - 3 * GeoUtils.eccSquared
						* GeoUtils.eccSquared / 64 - 5 * GeoUtils.eccSquared
						* GeoUtils.eccSquared * GeoUtils.eccSquared / 256)
						* latRad
						- (3 * GeoUtils.eccSquared / 8 + 3
								* GeoUtils.eccSquared * GeoUtils.eccSquared
								/ 32 + 45 * GeoUtils.eccSquared
								* GeoUtils.eccSquared * GeoUtils.eccSquared
								/ 1024)
						* Math.sin(2 * latRad)
						+ (15 * GeoUtils.eccSquared * GeoUtils.eccSquared / 256 + 45
								* GeoUtils.eccSquared
								* GeoUtils.eccSquared
								* GeoUtils.eccSquared / 1024)
						* Math.sin(4 * latRad) - (35 * GeoUtils.eccSquared
						* GeoUtils.eccSquared * GeoUtils.eccSquared / 3072)
						* Math.sin(6 * latRad));

		double easting = GeoUtils.k0
				* N
				* (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72
						* C - 58 * eccPrimeSquared)
						* A * A * A * A * A / 120) + 500000.0;
		double northing = GeoUtils.k0
				* (M + N
						* Math.tan(latRad)
						* (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A
								* A / 24 + (61 - 58 * T + T * T + 600 * C - 330 * eccPrimeSquared)
								* A * A * A * A * A * A / 720));
		if (lat < 0) {
			northing += 10000000.0; // 10000000 meter offset for southern
			// hemisphere
		}
		UTM utm = new UTM((float) easting, (float) northing, z);

		return utm;
	}

	public CM toCM(int zoomLevel) {
		UTM utm = toUTM(zoomLevel);
		CM cm = utm.toCM();

		return cm;
	}

	public XY toXY(int zoomLevel) {
		return toXY(zoomLevel, 1);
	}

	public XY toXY(int zoomLevel, double scalingFactor) {
		CM cm = toCM(zoomLevel);
		XY xy = cm.toXY(zoomLevel, scalingFactor);

		return xy;
	}

	public XY toDelta(LL centerNavtech, int zoomLevel, double scalingFactor) {
		XY centerXY = centerNavtech.toXY(zoomLevel, 1.86 * scalingFactor);
		XY xy = toXY(zoomLevel, 1.86 * scalingFactor);
		XY delta = new XY(xy.getX() - centerXY.getX(), centerXY.getY() - xy.getY());

		return delta;
	}

	public String toString() {
		return "(lon, lat) = [" + lon + ", " + lat + "]";
	}
}
