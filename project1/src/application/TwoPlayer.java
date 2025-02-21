package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;

// Class for handling a two-player coin game
public class TwoPlayer extends VBox {

	public static int[] coinsArray; // Array to hold coin values
	private StackPane[] circleArray; // Array to hold UI representations of coins
	private VBox playerOnePanel, playerTwoPanel; // Panels for each player
	private VBox gameBoard; // Game board layout
	private Label titleLabel; // Title label for the game
	private Label resultLabel; // Label to display results
	private Label playerOneScoreLabel, playerTwoScoreLabel; // Score labels for players
	private int playerOneCoins; // Score for player one
	private int playerTwoCoins; // Score for player two
	private boolean isPlayerOneTurn; // Flag to track whose turn it is
	private HelloPage helloPage; // Reference to the main menu page
	private Stage primaryStage; // Reference to the main application stage

	// Indices to track available coins
	private int firstCoinIndex; // Index of the first available coin
	private int lastCoinIndex; // Index of the last available coin

//..........................................................................................................................................................................................................................

	// Constructor to initialize the two-player game
	public TwoPlayer(Stage primaryStage, int[] coins, HelloPage helloPage) {
		this.primaryStage = primaryStage; // Store reference to primaryStage
		this.helloPage = helloPage; // Store reference to HelloPage
		coinsArray = coins.clone(); // Clone to avoid modifying original array
		isPlayerOneTurn = true; // Set initial turn to player one
		firstCoinIndex = 0; // Initialize first coin index
		lastCoinIndex = coinsArray.length - 1; // Initialize last coin index
		initializeStage(primaryStage); // Set up the game stage
	}

//..........................................................................................................................................................................................................................

	// Method to initialize the game stage
	private void initializeStage(Stage primaryStage) {
		BorderPane root = createMainLayout(); // Create the main layout
		Scene scene = new Scene(root); // Create the scene
		primaryStage.setTitle("Simple Coin Game"); // Set stage title
		primaryStage.setScene(scene); // Set the scene to the stage
		primaryStage.setMaximized(true); // Maximize the stage
		primaryStage.show(); // Show the stage
	}

//..........................................................................................................................................................................................................................

	// Method to create the main layout of the game
	private BorderPane createMainLayout() {
		BorderPane root = new BorderPane(); // Create a BorderPane for layout
		root.setStyle("-fx-background-color: lavender;"); // Set background color

		titleLabel = createTitleLabel(); // Create and set the title label
		playerOnePanel = createPlayerPanel("Player One", "player1.gif"); // Create player one panel
		playerTwoPanel = createPlayerPanel("Player Two", "player2.gif"); // Create player two panel
		gameBoard = createGameBoard(); // Create the game board

		HBox buttonBar = createButtonBar(); // Create the button bar
		resultLabel = new Label(); // Initialize the result label
		resultLabel.setFont(new Font("Cooper Black", 20)); // Set font for result label
		resultLabel.setTextFill(Color.DARKSLATEGRAY); // Set color for result label

		// Set layout positions
		root.setTop(titleLabel); // Set title at the top
		BorderPane.setAlignment(titleLabel, Pos.CENTER); // Center the title
		root.setLeft(playerOnePanel); // Set player one panel on the left
		root.setRight(playerTwoPanel); // Set player two panel on the right
		root.setCenter(gameBoard); // Set game board in the center
		root.setBottom(new VBox(10, buttonBar, resultLabel)); // Set button bar and result label at the bottom

		return root; // Return the main layout
	}

//..........................................................................................................................................................................................................................

	// Method to create the title label
	private Label createTitleLabel() {
		Label label = new Label("Optimal Strategy for a Game using Dynamic Programming"); // Title text
		label.setFont(new Font("Cooper Black", 30)); // Set font size
		label.setStyle("-fx-font-weight: bold;"); // Set font weight to bold
		label.setTextFill(Color.DARKSLATEBLUE); // Set text color
		return label; // Return the title label
	}

//..........................................................................................................................................................................................................................

	// Method to create the game board
	private VBox createGameBoard() {
		VBox board = new VBox(10); // Create a VBox for the game board
		board.setAlignment(Pos.CENTER); // Center the board
		circleArray = new StackPane[coinsArray.length]; // Initialize the circle array

		HBox[] rows = new HBox[(coinsArray.length + 11) / 12]; // Create rows for coins

		// Initialize rows for the game board layout
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new HBox(15); // Create a new HBox for each row
			rows[i].setAlignment(Pos.CENTER); // Center the content in the row
		}

		// Create circles for each coin
		for (int i = 0; i < coinsArray.length; i++) {
			Circle circle = new Circle(35, Color.GOLD); // Create a gold circle for the coin
			circle.setStroke(Color.BLACK); // Set stroke color for the circle
			Text text = new Text(String.valueOf(coinsArray[i])); // Create text for the coin value
			text.setFont(new Font(15)); // Set font for the text
			text.setBoundsType(TextBoundsType.VISUAL); // Set bounds type for text

			StackPane stack = new StackPane(circle, text); // Create a StackPane to hold the circle and text
			circleArray[i] = stack; // Store the StackPane in the array

			// Add click event for selecting coins
			int index = i; // Create a final variable for the click event
			circle.setOnMouseClicked(e -> handleCoinClick(index)); // Set event handler for coin click

			rows[i / 12].getChildren().add(stack); // Add the coin StackPane to the corresponding row
		}

		// Add rows to the board
		for (HBox row : rows) {
			if (!row.getChildren().isEmpty())
				board.getChildren().add(row); // Add non-empty rows to the board
		}

		return board; // Return the game board
	}

//..........................................................................................................................................................................................................................

	// Method to create a panel for a player
	private VBox createPlayerPanel(String playerName, String imagePath) {
		VBox panel = new VBox(10); // Create a VBox for the player panel
		panel.setPadding(new Insets(20)); // Set padding for the panel
		panel.setStyle(
				"-fx-background-color: rgba(173, 216, 230, 0.9); -fx-background-radius: 15; -fx-border-radius: 15;"); // Style
																														// the
																														// panel
		panel.setAlignment(Pos.TOP_CENTER); // Align contents to the top center

		ImageView playerImage = new ImageView(new Image(imagePath)); // Load player image
		playerImage.setFitWidth(150); // Set image width
		playerImage.setFitHeight(150); // Set image height
		playerImage.setPreserveRatio(true); // Preserve aspect ratio

		Label nameLabel = new Label(playerName); // Create label for player name
		nameLabel.setFont(new Font("Cooper Black", 24)); // Set font size for name
		nameLabel.setTextFill(playerName.equals("Player One") ? Color.RED : Color.BLUE); // Set color based on player
		nameLabel.setStyle("-fx-font-weight: bold;"); // Set font weight to bold

		// Set up score label for each player
		if (playerName.equals("Player One")) {
			playerOneScoreLabel = new Label("Score: 0"); // Initialize player one score
			playerOneScoreLabel.setFont(new Font("Cooper Black", 20)); // Set font for score label
			playerOneScoreLabel.setTextFill(Color.RED); // Set color for player one score
			panel.getChildren().addAll(playerImage, nameLabel, playerOneScoreLabel); // Add elements to panel
		} else {
			playerTwoScoreLabel = new Label("Score: 0"); // Initialize player two score
			playerTwoScoreLabel.setFont(new Font("Cooper Black", 20)); // Set font for score label
			playerTwoScoreLabel.setTextFill(Color.BLUE); // Set color for player two score
			panel.getChildren().addAll(playerImage, nameLabel, playerTwoScoreLabel); // Add elements to panel
		}

		return panel; // Return the player panel
	}

//..........................................................................................................................................................................................................................

	// Method to create the button bar
	private HBox createButtonBar() {
		Button resetButton = new Button("Reset"); // Create reset button
		resetButton.setOnAction(e -> resetGame()); // Set action for reset button
		styleButton(resetButton); // Style the reset button

		HBox buttonBar = new HBox(20, resetButton); // Create HBox for button bar
		buttonBar.setAlignment(Pos.CENTER); // Center the button bar
		buttonBar.setPadding(new Insets(20)); // Set padding for the button bar
		return buttonBar; // Return the button bar
	}

//..........................................................................................................................................................................................................................

	// Method to style a button
	private void styleButton(Button button) {
		button.setFont(new Font("Cooper Black", 18)); // Set font for button
		button.setTextFill(Color.WHITE); // Set text color for button
		button.setStyle("-fx-background-color: dodgerblue; -fx-padding: 15px 30px; -fx-background-radius: 10;"); // Style
																													// button
	}

//..........................................................................................................................................................................................................................

	// Method to reset the game state
	private void resetGame() {
		playerOneCoins = 0; // Reset player one score
		playerTwoCoins = 0; // Reset player two score
		isPlayerOneTurn = true; // Reset turn to player one
		resultLabel.setText(""); // Clear result label

		// Reset game board colors and scores
		for (StackPane stack : circleArray) {
			Circle circle = (Circle) stack.getChildren().get(0); // Get the circle from the stack
			circle.setFill(Color.GOLD); // Reset color to gold
		}

		// Reset indices for available coins
		firstCoinIndex = 0; // Reset first coin index
		lastCoinIndex = coinsArray.length - 1; // Reset last coin index

		// Reset score labels
		playerOneScoreLabel.setText("Score: 0"); // Reset player one score label
		playerTwoScoreLabel.setText("Score: 0"); // Reset player two score label

		// Go back to the HelloPage
		primaryStage.setScene(helloPage.previousScene); // Return to the previous scene
	}

//..........................................................................................................................................................................................................................

	// Method to handle coin selection
	private void handleCoinClick(int index) {
		// Check if the selected coin is available
		if (index != firstCoinIndex && index != lastCoinIndex) {
			return; // Coin is not selectable
		}

		// Update scores based on whose turn it is
		if (isPlayerOneTurn) {
			playerOneCoins += coinsArray[index]; // Update player one score
			createCircleForPlayerOne(index); // Visual representation for player one
		} else {
			playerTwoCoins += coinsArray[index]; // Update player two score
			createCircleForPlayerTwo(index); // Visual representation for player two
		}

		// Mark coin as taken
		coinsArray[index] = -1; // Set coin value to -1 to indicate it's taken

		// Update available indices
		if (index == firstCoinIndex) {
			firstCoinIndex++; // Move first index up
		} else if (index == lastCoinIndex) {
			lastCoinIndex--; // Move last index down
		}

		// Switch turns
		isPlayerOneTurn = !isPlayerOneTurn; // Toggle turn flag

		// Update result label and score labels
		resultLabel.setText("Player One: " + playerOneCoins + " | Player Two: " + playerTwoCoins); // Update result
																									// label
		playerOneScoreLabel.setText("Score: " + playerOneCoins); // Update player one score label
		playerTwoScoreLabel.setText("Score: " + playerTwoCoins); // Update player two score label

		// Check for end game condition
		if (isGameOver()) {
			showGameOver(); // Show game over if all coins are taken
		}
	}

//..........................................................................................................................................................................................................................

	// Method to check if the game is over
	private boolean isGameOver() {
		return firstCoinIndex > lastCoinIndex; // All coins taken if the indices have crossed
	}

//..........................................................................................................................................................................................................................

	// Method to show game over screen
	private void showGameOver() {
		String winner = playerOneCoins > playerTwoCoins ? "Player One" : "Player Two"; // Determine winner
		int score = Math.max(playerOneCoins, playerTwoCoins); // Get the highest score
		showCongratulationsStage(winner, score); // Show congratulations stage
	}

//..........................................................................................................................................................................................................................

	// Method to display the congratulations stage
	private void showCongratulationsStage(String winner, int score) {
		Platform.runLater(() -> { // Ensure UI updates are run on the JavaFX Application Thread
			Stage congratulationsStage = new Stage(); // Create a new stage for congratulations

			// Set up background image
			Image backgroundImage = new Image("cong.gif"); // Load background image
			ImageView backgroundImageView = new ImageView(backgroundImage); // Create ImageView for background
			backgroundImageView.setFitWidth(800); // Set width for background
			backgroundImageView.setFitHeight(600); // Set height for background

			StackPane root = new StackPane(); // Create root StackPane
			root.getChildren().add(backgroundImageView); // Add background image to root

			VBox vbox = new VBox(20); // Create VBox for content
			vbox.setAlignment(Pos.CENTER); // Center the content
			vbox.setPadding(new Insets(20)); // Set padding for VBox
			vbox.setStyle("-fx-background-color: rgba(230, 230, 250, 0.9);"); // Style the VBox

			// Congratulations label
			Label congratulationsLabel = new Label("Congratulations " + winner + "!"); // Create label for winner
			congratulationsLabel.setFont(Font.font("Cooper Black", 50)); // Set font for label
			congratulationsLabel.setTextFill(Color.DARKSLATEBLUE); // Set text color
			congratulationsLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.5, 1, 1);"); // Add shadow
																										// effect

			// Score label
			Label scoreLabel = new Label("Your Score: " + score); // Create label for score
			scoreLabel.setFont(Font.font("Cooper Black", 30)); // Set font for score label
			scoreLabel.setTextFill(Color.DARKSLATEBLUE); // Set text color

			// Close button
			Button closeButton = new Button("Close"); // Create close button
			closeButton.setFont(Font.font("Cooper Black", 20)); // Set font for close button
			closeButton.setStyle(
					"-fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;"); // Style
																																// close
																																// button
			closeButton.setOnAction(e -> congratulationsStage.close()); // Set action for close button

			// Assemble the VBox
			vbox.getChildren().addAll(congratulationsLabel, scoreLabel, closeButton); // Add elements to VBox
			root.getChildren().add(vbox); // Add VBox to root

			// Create and set the scene
			Scene scene = new Scene(root, 800, 600); // Create scene with specified dimensions
			congratulationsStage.setTitle("Game Over"); // Set title for the stage
			congratulationsStage.setScene(scene); // Set scene for the stage
			congratulationsStage.show(); // Show the stage
		});
	}

//..........................................................................................................................................................................................................................

	// Method to create a visual representation of the coin taken by player one
	private void createCircleForPlayerOne(int index) {
		Platform.runLater(() -> { // Ensure UI updates are run on the JavaFX Application Thread
			Circle circle = new Circle(35, Color.RED); // Create a red circle for player one
			circle.setStroke(Color.BLACK); // Set stroke color for the circle
			circleArray[index].getChildren().set(0, circle); // Replace the circle in the array with the new one
		});
	}

//..........................................................................................................................................................................................................................

	// Method to create a visual representation of the coin taken by player two
	private void createCircleForPlayerTwo(int index) {
		Platform.runLater(() -> { // Ensure UI updates are run on the JavaFX Application Thread
			Circle circle = new Circle(35, Color.BLUE); // Create a blue circle for player two
			circle.setStroke(Color.BLACK); // Set stroke color for the circle
			circleArray[index].getChildren().set(0, circle); // Replace the circle in the array with the new one
		});
	}
}