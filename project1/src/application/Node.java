package application;

public class Node {
	private Integer element;
	private Node prev;
	private Node next;

	public Node(Integer element) {
		this.element = element;
		this.prev = null;
		this.next = null;
	}

	public Integer getElement() {
		return element; // No need to cast now
	}

	public void setElement(Integer element) {
		this.element = element;
	}

	public Node getPrev() {
		return prev;
	}

	public void setPrev(Node prev) {
		this.prev = prev;
	}

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public String getData() {
		return element.toString();
	}

	public void displayNode() {
		System.out.print(element + " ");
	}
}