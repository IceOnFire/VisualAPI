package it.seat.visual.model;

import it.seat.visual.model.coords.IJ;
import it.seat.visual.model.coords.XY;

public class TileMatrix {
	private AbstractTile[][] tiles;
	private IJ center;// tile containing current center point

	public TileMatrix(int rows, int cols) {
		tiles = new AbstractTile[rows][cols];
	}

	public AbstractTile[][] getTiles() {
		return tiles;
	}

	public void setTiles(AbstractTile[][] tiles) {
		this.tiles = tiles;
	}

	public AbstractTile getTileAt(int row, int col) {
		return tiles[row][col];
	}

	private IJ getTileCoords(XY position) {
		IJ result = new IJ();
		result.setI(position.getY() / AbstractTile.DEFAULT_SIZE);
		result.setJ(position.getX() / AbstractTile.DEFAULT_SIZE);
		return result;
	}

	public void setTileAt(IJ coords, AbstractTile tile) {
		tiles[coords.getI()][coords.getJ()] = tile;
	}

	/**
	 * Searches for the tile that contains the coordinate information specified
	 * by the parameters given.
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public AbstractTile searchTile(int row, int col) {
		for (int i = 0; i < getRows(); i++) {
			for (int j = 0; j < getCols(); j++) {
				AbstractTile tile = getTileAt(i, j);
				if (tile != null && tile.getIJ().getI() == row
						&& tile.getIJ().getJ() == col)
					return tile;
			}
		}
		return null;
	}

	public int getRows() {
		return tiles.length;
	}

	public int getCols() {
		return tiles[0].length;
	}

	public IJ getCenter() {
		return center;
	}

	public void setCenter(XY newCenter) {
		center = getTileCoords(newCenter);
	}
}
