package application;

// Class to represent coin-related data in the game
public class Coins {
	public int min; // Minimum value of the coin
	public int max; // Maximum value of the coin
	public int currentCoin; // Value of the currently selected coin
	public int currentX; // X-coordinate of the current coin in a table 
	public int currentY; // Y-coordinate of the current coin in a table 

	// Constructor to initialize all attributes
	public Coins(int max, int min, int currentCoin, int x, int y) {
		this.min = min; // Set minimum value
		this.max = max; // Set maximum value
		this.currentCoin = currentCoin; // Set current coin value
		this.currentX = x; // Set current X coordinate
		this.currentY = y; // Set current Y coordinate
	}

	// Constructor to initialize only current X and Y coordinates
	public Coins(int x, int y) {
		this.currentX = x; // Set current X coordinate
		this.currentY = y; // Set current Y coordinate
	}

	// Override toString method to provide a formatted string representation of the
	// object
	@Override
	public String toString() {
		return String.format("(%d,%d , %d , %d , %d)", max, min, currentCoin, currentX, currentY);
	}

	// Default constructor
	public Coins() {
	}
}