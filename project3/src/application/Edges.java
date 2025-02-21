package application;

public class Edges {
	Vertex destination;
	Vertex source;
	double cost; // Represents cost
	double time; // Duration of the flight
	double distance;

	public Edges(Vertex source, Vertex destination, double cost, double time) {
		this.source = source;
		this.destination = destination;
		this.cost = cost;
		this.time = time;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public Vertex getSource() {
		return source;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}
}