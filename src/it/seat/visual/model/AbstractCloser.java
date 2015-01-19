package it.seat.visual.model;

import it.seat.visual.model.coords.IJ;
import it.seat.visual.model.coords.LL;
import it.seat.visual.model.coords.XY;

import java.util.Vector;

/**
 * @author Ice
 */
public abstract class AbstractCloser implements ImageLoaderListener {
	private static final long serialVersionUID = 1L;

	public static final int MODE_MAP = 0;
	public static final int MODE_ORTO = 1;
	public static final int MODE_MIXED = 2;

	public static final int ZOOM_HIGHEST_LEVEL = 17;
	public static final int ZOOM_LOWEST_LEVEL = 2;

	public static final float DEFAULT_LON = 12.49353f;
	public static final float DEFAULT_LAT = 41.89504f;
	public static final int DEFAULT_ZOOM_LEVEL = 15;

	private TileMatrix tileMatrix;

	private ViewMode viewMode;// map, orto or mixed
	private int zoomLevel;
	private LL center;
	private XY mapCenter;
	private int width, height;
	private int mapSize;

	private Vector loaders;
	private Vector listeners;

	public abstract AbstractImageLoader getImageLoader(int row, int col,
			int zoomLevel, int viewMode, int size, String format);

	public abstract AbstractTile getTile(IJ coords, byte[] rawData);

	public AbstractCloser(int width, int height) {
		this(MODE_MAP, width, height);
	}

	public AbstractCloser(int mode, int width, int height) {
		viewMode = ViewMode.createViewMode(mode);
		this.width = width;
		this.height = height;

		loaders = new Vector();
		listeners = new Vector();

		center = new LL(DEFAULT_LON, DEFAULT_LAT);
		reset();
		viewMode.setZoomLevel(DEFAULT_ZOOM_LEVEL, this);
	}

	public TileMatrix getTileMatrix() {
		return tileMatrix;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public LL getCenter() {
		return center;
	}

	public XY getMapCenter() {
		return mapCenter;
	}

	public void loadImages() {
		int rows = tileMatrix.getRows();
		int cols = tileMatrix.getCols();

		int centerRow = tileMatrix.getCenter().getI();
		int centerCol = tileMatrix.getCenter().getJ();
		int startRow = centerRow - (rows >> 1);
		int endRow = centerRow + (rows >> 1);
		int startCol = centerCol - (cols >> 1);
		int endCol = centerCol + (cols >> 1);

		int tilesNumber = mapSize / AbstractTile.DEFAULT_SIZE;

		if (startRow < 0)
			startRow = 0;
		if (startCol < 0)
			startCol = 0;
		if (endRow > tilesNumber)
			endRow = tilesNumber;
		if (endCol > tilesNumber)
			endCol = tilesNumber;

		loaders.removeAllElements();
		AbstractTile[][] temp = new AbstractTile[rows][cols];
		for (int row = startRow; row < endRow; row++) {
			for (int col = startCol; col < endCol; col++) {
				AbstractTile tile = tileMatrix.searchTile(row, col);
				if (tile != null && tile.getData() != AbstractTile.DEFAULT_DATA && tile.getForm() != null) {
					temp[row - startRow][col - startCol] = tile;
				} else {
					AbstractImageLoader loader = getImageLoader(row, col,
							zoomLevel, viewMode.getMode(),
							AbstractTile.DEFAULT_SIZE,
							AbstractTile.DEFAULT_FORMAT);
					loader.addListener(this);
					loaders.addElement(loader);
				}
			}
		}

		/* avvia il caricamento parallelo delle immagini */
		tileMatrix.setTiles(temp);
		tileMatrix.setCenter(mapCenter);
		for (int i = 0; i < loaders.size(); i++) {
			AbstractImageLoader loader = (AbstractImageLoader) loaders
					.elementAt(i);
			loader.start();
		}
	}

	private void abortLoading() {
		for (int i = 0; i < loaders.size(); i++) {
			AbstractImageLoader loader = (AbstractImageLoader) loaders
					.elementAt(i);
			loader.stopLoading();
		}
	}

	public void centerMap(LL navtech) {
		center = navtech;
		mapCenter = navtech.toXY(zoomLevel);
		tileMatrix.setCenter(mapCenter);
		fireMapCentered(mapCenter);
	}

	public void move(int dx, int dy) {
		mapCenter.setX(mapCenter.getX() + dx);
		mapCenter.setY(mapCenter.getY() + dy);
		tileMatrix.setCenter(mapCenter);
		center = mapCenter.toLL(zoomLevel);
		fireMapDragged(mapCenter);
	}

	private void resetTileMatrix() {
		int rows = 2 * (Math.max(height, AbstractTile.DEFAULT_SIZE) / AbstractTile.DEFAULT_SIZE + 1);
		int cols = 2 * (Math.max(width, AbstractTile.DEFAULT_SIZE) / AbstractTile.DEFAULT_SIZE + 1);
		tileMatrix = new TileMatrix(rows, cols);
	}

	public void reset() {
		abortLoading();
		resetTileMatrix();
		centerMap(center);
		loadImages();
	}

	public void setViewMode(int mode) {
		viewMode = ViewMode.createViewMode(mode);
		reset();
		fireViewModeChanged(mode);
	}

	private void setZoomLevel(int z) {
		zoomLevel = z;
		if (z > ZOOM_HIGHEST_LEVEL - 1) {
			zoomLevel = ZOOM_HIGHEST_LEVEL - 1;
		} else if (z < ZOOM_LOWEST_LEVEL) {
			zoomLevel = ZOOM_LOWEST_LEVEL;
		}

		int oldMapSize = mapSize;
		mapSize = (int) GeoUtils.pow(2, ZOOM_HIGHEST_LEVEL - zoomLevel) * 256;
		float ratio = 1f * mapSize / oldMapSize;

		mapCenter.setX((int) (mapCenter.getX() * ratio));
		mapCenter.setY((int) (mapCenter.getY() * ratio));

		reset();
		fireZoomChanged(zoomLevel);
	}

	public void zoomIn() {
		viewMode.setZoomLevel(zoomLevel - 1, this);
	}

	public void zoomOut() {
		viewMode.setZoomLevel(zoomLevel + 1, this);
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void addCloserListener(CloserListener listener) {
		listeners.addElement(listener);
	}

	private void fireImageLoaded(AbstractTile tile, IJ coords) {
		for (int i = 0; i < listeners.size(); i++) {
			CloserListener listener = (CloserListener) listeners.elementAt(i);
			listener.onTileLoaded(tile, coords);
		}
	}

	private void fireMapCentered(XY newCenter) {
		for (int i = 0; i < listeners.size(); i++) {
			CloserListener listener = (CloserListener) listeners.elementAt(i);
			listener.onMapCentered(newCenter);
		}
	}

	private void fireMapDragged(XY newCenter) {
		for (int i = 0; i < listeners.size(); i++) {
			CloserListener listener = (CloserListener) listeners.elementAt(i);
			listener.onMapDragged(newCenter);
		}
	}

	private void fireViewModeChanged(int mode) {
		for (int i = 0; i < listeners.size(); i++) {
			CloserListener listener = (CloserListener) listeners.elementAt(i);
			listener.onViewModeChanged(mode);
		}
	}

	private void fireZoomChanged(int z) {
		for (int i = 0; i < listeners.size(); i++) {
			CloserListener listener = (CloserListener) listeners.elementAt(i);
			listener.onZoomChanged(z);
		}
	}

	/** +--- ImageLoaderListener ---+ */
	public void onImageLoaded(int row, int col, int z, byte[] rawData) {
		int rows = tileMatrix.getRows();
		int cols = tileMatrix.getCols();

		int centerRow = tileMatrix.getCenter().getI();
		int centerCol = tileMatrix.getCenter().getJ();
		int startRow = centerRow - (rows >> 1);
		int startCol = centerCol - (cols >> 1);
		// controllo che non arrivino immagini che non c'entrano dai thread
		int i = row - startRow;
		int j = col - startCol;
		if (i >= 0 && i < rows && j >= 0 && j < cols) {
			IJ mapCoords = new IJ(row, col);
			AbstractTile tile = getTile(mapCoords, rawData);
			IJ coords = new IJ(i, j);
			tileMatrix.setTileAt(coords, tile);
			fireImageLoaded(tile, mapCoords);
		}
	}

	public void onLoadingCancelled(int row, int col, int z) {
	}

	public void onError(int row, int col, int z, Exception e) {
		onImageLoaded(row, col, z, AbstractTile.DEFAULT_DATA);
	}

	private static abstract class ViewMode {
		protected abstract int getMode();

		protected static ViewMode createViewMode(int mode) {
			ViewMode viewMode = null;
			switch (mode) {
			case 0:
				viewMode = new MapMode();
				break;
			case 1:
				viewMode = new OrtoMode();
				break;
			case 2:
				viewMode = new MixedMode();
				break;
			}
			return viewMode;
		}

		protected void setZoomLevel(int zoomLevel, AbstractCloser c) {
			c.setZoomLevel(zoomLevel);
		}
	}

	private static class MapMode extends ViewMode {
		protected int getMode() {
			return 0;
		}

		protected void setZoomLevel(int zoomLevel, AbstractCloser c) {
			if (zoomLevel < 3) {
				zoomLevel = 3;
			}
			c.setZoomLevel(zoomLevel);
		}
	}

	private static class OrtoMode extends ViewMode {
		protected int getMode() {
			return 1;
		}
	}

	private static class MixedMode extends ViewMode {
		protected int getMode() {
			return 2;
		}
	}
}
