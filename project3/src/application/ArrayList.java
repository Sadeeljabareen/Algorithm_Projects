package application;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;

public class ArrayList<T> extends AbstractList<T> {
	private Object[] elements;
	private int size;
	private static final int INITIAL_CAPACITY = 10;

	// Constructor
	public ArrayList() {
		elements = new Object[INITIAL_CAPACITY];
		size = 0;
	}

	// Adds an element to the end of the list
	@Override
	public boolean add(T element) {
		ensureCapacity();
		elements[size++] = element;
		return true; // This method returns a boolean
	}

	// Adds an element at a specific index
	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		ensureCapacity();
		System.arraycopy(elements, index, elements, index + 1, size - index);
		elements[index] = element;
		size++;
	}

	// Retrieves an element at a specific index
	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		return (T) elements[index];
	}

	// Removes an element at a specific index
	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		T removedElement = (T) elements[index];
		int numMoved = size - index - 1;
		if (numMoved > 0) {
			System.arraycopy(elements, index + 1, elements, index, numMoved);
		}
		elements[--size] = null; // Prevent memory leak
		return removedElement;
	}

	// Replaces an element at a specific index and returns the old element
	public T set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		}
		T oldElement = (T) elements[index];
		elements[index] = element;
		return oldElement;
	}

	// Returns the current size of the list
	@Override
	public int size() {
		return size;
	}

	// Checks if the list is empty
	public boolean isEmpty() {
		return size == 0;
	}

	// Clears the list
	public void clear() {
		Arrays.fill(elements, 0, size, null);
		size = 0;
	}

	// Ensures the internal array has enough capacity
	private void ensureCapacity() {
		if (size == elements.length) {
			int newCapacity = elements.length * 2;
			elements = Arrays.copyOf(elements, newCapacity);
		}
	}

	// Converts the list to a string representation
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < size; i++) {
			sb.append(elements[i]);
			if (i < size - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	// Returns an iterator for the list
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < size;
			}

			@Override
			public T next() {
				return get(currentIndex++);
			}
		};
	}
}