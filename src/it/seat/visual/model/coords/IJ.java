package it.seat.visual.model.coords;

public class IJ {
	private int i;
	private int j;

	public IJ() {		
	}

	public IJ(int i, int j) {
		this.i = i;
		this.j = j;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public String toString() {
		return "(i, j) = [" + i + ", " + j + "]";
	}
}
