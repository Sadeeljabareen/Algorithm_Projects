package application;

import java.util.LinkedList;

public class Vertex {
	Capital capital;
	Vertex previous;
	int num;
	boolean visited;
	double distance = Double.MAX_VALUE; // Use Double.MAX_VALUE for distance
	public double time = Double.MAX_VALUE; // Use Double.MAX_VALUE for time
	public double cost = Double.MAX_VALUE; // Use Double.MAX_VALUE for cost
	LinkedList<Edges> edges = new LinkedList<>();


	public Vertex(Capital capital, int number) {
		this.capital = capital;
		this.num = number;
	}

	public Capital getCapital() {
		return capital;
	}

	public void setCapital(Capital capital) {
		this.capital = capital;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) { // Change parameter type to double
		this.distance = distance;
	}

	public LinkedList<Edges> getEdges() {
		return edges;
	}

	public void setEdges(LinkedList<Edges> edges) {
		this.edges = edges;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean findEdge(String capitalName) {
		for (Edges edge : edges) {
			if (edge.getDestination().getCapital().getName().equalsIgnoreCase(capitalName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(capital.getName() + ":");
		for (Edges edge : edges) {
			result.append(edge.getDestination().getCapital().getName()).append(",");
		}
		return result.toString();
	}

	// Initialize the table for Dijkstra's algorithm
	public static void initializeTable(Vertex start, Graph g, TableEntry[] T) {
		for (int i = 0; i < T.length; i++) {
			T[i] = new TableEntry();
			T[i].setKnown(false);
			T[i].setDist(Double.MAX_VALUE);
			T[i].setPath(null);
		}
		T[start.getNum()].setDist(0);
	}

	// Helper method to find the vertex with the smallest unknown distance
	public static Vertex smallestUnknownDistanceVertex(TableEntry[] T) {
		Vertex smallestVertex = null;
		double smallestDist = Double.MAX_VALUE;

		for (int i = 0; i < T.length; i++) {
			if (!T[i].isKnown() && T[i].getDist() < smallestDist) {
				smallestDist = T[i].getDist();
				smallestVertex = getVertexByNum(i); // Assuming you have a method to get Vertex by num
			}
		}
		return smallestVertex;
	}

	// Helper method to get Vertex by num (you need to implement this based on your
	// graph structure)
	private static Vertex getVertexByNum(int num) {
		// Implement this method to return the Vertex with the given num
		return null;
	}
}