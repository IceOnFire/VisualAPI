package it.seat.visual.model;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public abstract class AbstractImageLoader extends Thread {
	protected String proxyHost;
	protected String proxyPort;
	protected String url;
	protected boolean stop;

	private int x;
	private int y;
	private int z;
	private Vector listeners;

	public AbstractImageLoader(int row, int col, int zoomLevel, int mode,
			int tileSize, String format) {
		super();
		listeners = new Vector();
		this.x = col;
		this.y = row;
		this.z = (int) GeoUtils.pow(2, zoomLevel);

		int index = (int) (new Random().nextInt(4) + 1);
		String call = "europa";
		if (mode == AbstractCloser.MODE_ORTO)
			call += "-orto";
		else if (mode == AbstractCloser.MODE_MIXED)
			call += "-mixed";
		url =
			"http://" +
			"visualimages" + index + ".paginegialle.it/xml.php/"
				+ call + ".imgi?cmd=tile&format=" + format + "&x=" + x + "&y="
				+ y + "&z=" + z + "&extra=2&ts=" + tileSize
				+ "&q=60&rdr=0&sito=vz";

		 setProxy("172.17.231.169", "8080");
	}

	public void setProxy(String host, String port) {
		this.proxyHost = host;
		this.proxyPort = port;
	}

	public void addListener(ImageLoaderListener listener) {
		listeners.addElement(listener);
	}

	public void run() {
		stop = false;
		try {
			byte[] image = loadImage();
			if (!stop) {
				notifyImageLoaded(y, x, z, image);
			} else {
				notifyLoadingCancelled(y, x, z);
			}
		} catch (IOException e) {
			notifyError(y, x, z, e);
		}
	}

	public abstract byte[] loadImage() throws IOException;

	public void stopLoading() {
		stop = true;
	}

	private void notifyImageLoaded(int row, int col, int z, byte[] rawData) {
		for (int i = 0; i < listeners.size(); i++) {
			ImageLoaderListener listener = (ImageLoaderListener) listeners
					.elementAt(i);
			listener.onImageLoaded(row, col, z, rawData);
		}
	}

	private void notifyLoadingCancelled(int row, int col, int z) {
		for (int i = 0; i < listeners.size(); i++) {
			ImageLoaderListener listener = (ImageLoaderListener) listeners
					.elementAt(i);
			listener.onLoadingCancelled(row, col, z);
		}
	}

	private void notifyError(int row, int col, int z, Exception e) {
		for (int i = 0; i < listeners.size(); i++) {
			ImageLoaderListener listener = (ImageLoaderListener) listeners
					.elementAt(i);
			listener.onError(row, col, z, e);
		}
	}
}