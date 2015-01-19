package it.seat.visual.model;

public interface ImageLoaderListener {
	public void onImageLoaded(int row, int col, int z, byte[] rawData);

	public void onLoadingCancelled(int row, int col, int z);

	public void onError(int row, int col, int z, Exception e);
}
