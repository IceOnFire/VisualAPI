package it.seat.visual.model;

public class GeoUtils {
	public final static int ZOOM_HIGHEST_LEVEL = 17;
	public final static int ZOOM_LOWEST_LEVEL = 2;
	public final static int ZOOM_START_LEVEL = 15;

	// private final static double FOURTHPI = Math.PI / 4;
	public final static double deg2rad = Math.PI / 180;
	public final static double rad2deg = 180.0 / Math.PI;

	/* converte le coorindate lon/lat in UTM */
	public final static double a = 6378137;
	public final static double eccSquared = 0.00669438;
	public final static double k0 = 0.9996;

	/* ottimizzazione delle formule con costanti */
	// private final static double c_180pi = 57.2957795130;
	// private final static double c_pi180 = 0.0174532925;
	// private final static double c_asqrt = (a * Math.sqrt(1 - eccSquared
	// * eccSquared));

	public static double pow(double a, double b) {
		double pow = 1;
		for (int i = 0; i < b; i++) {
			pow *= a;
		}
		return pow;
	}
}
