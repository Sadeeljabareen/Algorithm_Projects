package application;

import java.util.Comparator;

public class PriorityQueue<T> {
	private ArrayList<T> elements;
	private Comparator<T> comparator;

	public PriorityQueue(Comparator<T> comparator) {
		this.elements = new ArrayList<>();
		this.comparator = comparator;
	}

	public void add(T element) {
		elements.add(element);
		siftUp(elements.size() - 1);
	}

	public T poll() {
		if (elements.isEmpty()) {
			return null;
		}
		T result = elements.get(0);
		T lastElement = elements.remove(elements.size() - 1);
		if (!elements.isEmpty()) {
			elements.set(0, lastElement);
			siftDown(0);
		}
		return result;
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public int size() {
		return elements.size();
	}

	private void siftUp(int index) {
		T element = elements.get(index);
		while (index > 0) {
			int parentIndex = (index - 1) / 2;
			T parent = elements.get(parentIndex);
			if (comparator.compare(element, parent) >= 0) {
				break;
			}
			elements.set(index, parent);
			index = parentIndex;
		}
		elements.set(index, element);
	}

	private void siftDown(int index) {
		T element = elements.get(index);
		int half = elements.size() / 2;
		while (index < half) {
			int childIndex = 2 * index + 1; // Left child
			T child = elements.get(childIndex);
			int rightIndex = childIndex + 1;
			if (rightIndex < elements.size() && comparator.compare(child, elements.get(rightIndex)) > 0) {
				childIndex = rightIndex;
				child = elements.get(childIndex);
			}
			if (comparator.compare(element, child) <= 0) {
				break;
			}
			elements.set(index, child);
			index = childIndex;
		}
		elements.set(index, element);
	}
}
