package application;

public class Graph {
	// Assuming you have a list of vertices
	private Vertex[] vertices;

	public Graph(Vertex[] vertices) {
		this.vertices = vertices;
	}

	public Vertex[] getVertices() {
		return vertices;
	}

	public void setVertices(Vertex[] vertices) {
		this.vertices = vertices;
	}

	// Method to read the graph and initialize the table
	public void readGraph(TableEntry[] T) {
		// Implement this method to read the graph and initialize the table
	}
}