package application;

import java.util.List;

public class TableEntry {
	List<Vertex> header;
	boolean known;
	double dist; // distType can be double for distance, cost, or time
	Vertex path;

	public TableEntry() {
		this.known = false;
		this.dist = Double.MAX_VALUE; // Use Double.MAX_VALUE for infinity
		this.path = null;
	}

	public List<Vertex> getHeader() {
		return header;
	}

	public void setHeader(List<Vertex> header) {
		this.header = header;
	}

	public boolean isKnown() {
		return known;
	}

	public void setKnown(boolean known) {
		this.known = known;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	public Vertex getPath() {
		return path;
	}

	public void setPath(Vertex path) {
		this.path = path;
	}
}