package it.seat.visual.model.coords;

import it.seat.visual.model.GeoUtils;

public class UTM {
	private float easting;
	private float northing;
	private int z;

	public UTM() {
	}

	public UTM(float easting, float northing, int z) {
		this.easting = easting;
		this.northing = northing;
		this.z = z;
	}

	public float getEasting() {
		return easting;
	}

	public void setEasting(float easting) {
		this.easting = easting;
	}

	public float getNorthing() {
		return northing;
	}

	public void setNorthing(float northing) {
		this.northing = northing;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	/** Converte le coordinate UTM in LL. */
	public LL toLL() {
		double e1 = (1 - Math.sqrt(1 - GeoUtils.eccSquared))
				/ (1 + Math.sqrt(1 - GeoUtils.eccSquared));
		// int NorthernHemisphere = 1; // 1 for northern hemispher, 0 for
		// southern

		double x = easting - 500000.0; // remove 500,000 meter offset for
		// longitude
		double y = northing;

		int zoneNumber = 31;

		double lonOrigin = (zoneNumber - 1) * 6 - 180 + 3; // +3 puts origin in
		// middle of zone

		double eccPrimeSquared = (GeoUtils.eccSquared)
				/ (1 - GeoUtils.eccSquared);

		double M = y / GeoUtils.k0;
		double mu = M
				/ (GeoUtils.a * (1 - GeoUtils.eccSquared / 4 - 3
						* GeoUtils.eccSquared * GeoUtils.eccSquared / 64 - 5
						* GeoUtils.eccSquared * GeoUtils.eccSquared
						* GeoUtils.eccSquared / 256));
		double phi1Rad = mu + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32)
				* Math.sin(2 * mu)
				+ (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32)
				* Math.sin(4 * mu) + (151 * e1 * e1 * e1 / 96)
				* Math.sin(6 * mu);
		// double phi1 = phi1Rad * rad2deg;
		double N1 = GeoUtils.a
				/ Math.sqrt(1 - GeoUtils.eccSquared * Math.sin(phi1Rad)
						* Math.sin(phi1Rad));
		double T1 = Math.tan(phi1Rad) * Math.tan(phi1Rad);
		double C1 = eccPrimeSquared * Math.cos(phi1Rad) * Math.cos(phi1Rad);
		double R1 = GeoUtils.a
				* (1 - GeoUtils.eccSquared)
				/ GeoUtils.pow(1 - GeoUtils.eccSquared * Math.sin(phi1Rad)
						* Math.sin(phi1Rad), 1.5);
		double D = x / (N1 * GeoUtils.k0);

		double lon = (D - (1 + 2 * T1 + C1) * D * D * D / 6 + (5 - 2 * C1 + 28
				* T1 - 3 * C1 * C1 + 8 * eccPrimeSquared + 24 * T1 * T1)
				* D * D * D * D * D / 120)
				/ Math.cos(phi1Rad);
		lon = 6 + lonOrigin + lon * GeoUtils.rad2deg;
		double lat = phi1Rad
				- (N1 * Math.tan(phi1Rad) / R1)
				* (D
						* D
						/ 2
						- (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * eccPrimeSquared)
						* D * D * D * D / 24 + (61 + 90 * T1 + 298 * C1 + 45
						* T1 * T1 - 252 * eccPrimeSquared - 3 * C1 * C1)
						* D * D * D * D * D * D / 720);
		lat *= GeoUtils.rad2deg;
		LL LL = new LL((float) lon, (float) lat);

		return LL;
	}

	/** Converte le coordinate UTM in CM. */
	public CM toCM() {
		double x = -(easting - 637847.3) / 4709238.7;
		double y = (northing - 5671365.6) / 4709238.7;
		CM cm = new CM((float) x, (float) y);

		return cm;
	}

	public String toString() {
		return "(easting, northing) = [" + easting + ", " + northing + "]";
	}
}
