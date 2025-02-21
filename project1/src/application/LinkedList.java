package application;

public class LinkedList {
	private Node front;
	private Node back;
	private int size;

	public LinkedList() {
		front = back = null;
		size = 0;
	}

	public void clear() {
		front = back = null; // Reset front and back
		size = 0; // Reset size
	}

	public Node getFront() {
		return front;
	}

	public Node getBack() {
		return back;
	}

	public int getSize() {
		return size;
	}

	public void addFirst(Integer element) {
		Node newNode = new Node(element);
		if (size == 0) {
			front = back = newNode;
		} else {
			newNode.setNext(front);
			front.setPrev(newNode);
			front = newNode;
		}
		size++;
	}

	public void addLast(Integer element) {
		Node newNode = new Node(element);
		if (size == 0) {
			front = back = newNode;
		} else {
			back.setNext(newNode);
			newNode.setPrev(back);
			back = newNode;
		}
		size++;
	}

	public void addAll(LinkedList otherList) {
		Node current = otherList.getFront();
		while (current != null) {
			this.addLast(current.getElement()); // No need to cast
			current = current.getNext();
		}
	}

	public boolean contains(Integer value) {
		Node current = front;
		while (current != null) {
			if (current.getElement().equals(value)) {
				return true;
			}
			current = current.getNext();
		}
		return false;
	}

	public void add(int index, Integer element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index out of bounds");
		}
		if (index == 0) {
			addFirst(element);
		} else if (index == size) {
			addLast(element);
		} else {
			Node newNode = new Node(element);
			Node current = front;
			for (int i = 0; i < index - 1; i++) {
				current = current.getNext();
			}
			newNode.setNext(current.getNext());
			newNode.setPrev(current);
			if (current.getNext() != null) {
				current.getNext().setPrev(newNode);
			}
			current.setNext(newNode);
			size++;
		}
	}

	public boolean removeFirst() {
		if (size == 0) {
			return false;
		} else if (size == 1) {
			front = back = null;
		} else {
			front = front.getNext();
			front.setPrev(null);
		}
		size--;
		return true;
	}

	public boolean removeLast() {
		if (size == 0) {
			return false;
		} else if (size == 1) {
			front = back = null;
		} else {
			back = back.getPrev();
			back.setNext(null);
		}
		size--;
		return true;
	}

	public void print() {
		Node current = front;
		while (current != null) {
			System.out.print(current.getElement());
			if (current.getNext() != null) {
				System.out.print(" -> ");
			}
			current = current.getNext();
		}
		System.out.println();
	}

}