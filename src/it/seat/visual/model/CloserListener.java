package it.seat.visual.model;

import it.seat.visual.model.coords.IJ;
import it.seat.visual.model.coords.XY;

public interface CloserListener {
	public void onMapCentered(XY center);

	public void onMapDragged(XY center);

	public void onZoomChanged(int zoomLevel);

	public void onViewModeChanged(int viewMode);

	public void onTileLoaded(AbstractTile tile, IJ coords);
}
