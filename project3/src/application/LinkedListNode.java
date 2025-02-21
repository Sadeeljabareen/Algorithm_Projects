package application;

public class LinkedListNode {

	private Edges edge;
	private LinkedListNode next;

	public LinkedListNode(Edges edge) {
		this.edge = edge;
	}

	public Edges getEdge() {
		return edge;
	}

	public void setEdge(Edges vertix) {
		this.edge = vertix;
	}

	public LinkedListNode getNext() {
		return next;
	}

	public void setNext(LinkedListNode next) {
		this.next = next;
	}
}