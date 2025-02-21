package application;

public class PathTable {
	String source;
	String target;
	double distance;
	double time;
	double cost;

	public PathTable() {
		// Default constructor
	}

	public PathTable(double distance, String source, String target) {
		this.distance = distance;
		this.source = source;
		this.target = target;
	}

	public PathTable(double distance, double time, double cost, String source, String target) {
		this.distance = distance;
		this.time = time;
		this.cost = cost;
		this.source = source;
		this.target = target;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) { // Change parameter type to double
		this.distance = distance;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) { // Change parameter type to double
		this.cost = cost;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) { // Change parameter type to double
		this.time = time;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "PathTable [distance=" + distance + ", source=" + source + ", target=" + target + "]";
	}
}