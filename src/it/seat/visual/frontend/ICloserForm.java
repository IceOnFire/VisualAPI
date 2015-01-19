package it.seat.visual.frontend;

import it.seat.visual.model.CloserListener;
import it.seat.visual.model.coords.LL;
import it.seat.visual.model.coords.XY;

public interface ICloserForm extends CloserListener {
	public void setViewMode(int mode);
	
	public void reset();

	public void zoomIn();

	public void zoomOut();

	public int getZoomLevel();

//	public void place(int x, int y);

	public void centerMap(LL navtech);

	public LL getCenter();

	public XY getMapCenter();

	public void addCloserListener(CloserListener listener);
}
