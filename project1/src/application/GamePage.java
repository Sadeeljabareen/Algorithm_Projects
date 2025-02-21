package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

public class GamePage extends VBox {

	// Array to hold coin values
	public static int[] coinsArray;
	private static Stage stage; // Stage for the game
	private Coins[][] tableC; // Table to hold optimal moves
	private StackPane[] circleArray; // Array to hold coin circles
	private VBox playerOnePanel, playerTwoPanel; // Panels for player information
	private VBox gameBoard; // Game board to display coins
	private Label titleLabel; // Title label for the game
	private Label resultLabel; // Label to show results
	public int playerOneCoins; // Total coins for player one
	public int playerTwoCoins; // Total coins for player two

//..........................................................................................................................................................................................................................

	// Constructor initializes the game stage
	public GamePage(Stage primaryStage, int[] coins) {
		coinsArray = coins; // Initialize coin values
		initializeStage(primaryStage); // Set up the stage
	}

//..........................................................................................................................................................................................................................

	// Method to initialize the main stage
	private void initializeStage(Stage primaryStage) {
		BorderPane root = createMainLayout(); // Create the main layout
		Scene scene = new Scene(root); // Create the scene
		primaryStage.setTitle("Optimal Strategy Game"); // Set the title
		primaryStage.setScene(scene); // Set the scene
		primaryStage.setMaximized(true); // Maximize the window
		primaryStage.show(); // Show the stage
	}

//..........................................................................................................................................................................................................................

	// Method to create the main layout of the game
	private BorderPane createMainLayout() {
		BorderPane root = new BorderPane(); // Create a BorderPane
		root.setStyle("-fx-background-color: lavender;"); // Set background color

		// Create images for the title
		ImageView smallImageLeft = new ImageView(new Image("coin.png"));
		smallImageLeft.setFitWidth(50);
		smallImageLeft.setPreserveRatio(true);

		ImageView smallImageRight = new ImageView(new Image("coin.png"));
		smallImageRight.setFitWidth(50);
		smallImageRight.setPreserveRatio(true);

		// Initialize UI components
		titleLabel = createTitleLabel();
		playerOnePanel = createPlayerPanel("Player One", "player1.gif");
		playerTwoPanel = createPlayerPanel("Player Two", "player2.gif");
		gameBoard = createGameBoard();

		HBox buttonBar = createButtonBar(); // Create button bar
		resultLabel = new Label(); // Label for results
		resultLabel.setFont(new Font("Cooper Black", 20));
		resultLabel.setTextFill(Color.DARKSLATEGRAY);

		// Create title box with images and title
		HBox titleBox = new HBox(10, smallImageLeft, titleLabel, smallImageRight);
		titleBox.setAlignment(Pos.CENTER);

		// Set layout components
		root.setTop(titleBox);
		root.setLeft(playerOnePanel);
		root.setRight(playerTwoPanel);
		root.setCenter(gameBoard);
		root.setBottom(new VBox(10, buttonBar, resultLabel));

		return root; // Return the main layout
	}

//..........................................................................................................................................................................................................................

	// Method to create the title label
	private Label createTitleLabel() {
		Label label = new Label("Optimal Strategy for a Game using Dynamic Programming");
		label.setFont(new Font("Cooper Black", 30)); // Set font size
		label.setStyle("-fx-font-weight: bold;"); // Set bold font
		label.setTextFill(Color.DARKSLATEBLUE); // Set text color
		return label; // Return the label
	}

//..........................................................................................................................................................................................................................

	// Method to create the game board layout
	private VBox createGameBoard() {
		VBox board = new VBox(10); // Create a vertical box for the board
		board.setAlignment(Pos.CENTER); // Center alignment
		circleArray = new StackPane[coinsArray.length]; // Initialize circle array

		// Calculate optimal moves for the game
		tableC = calculateOptimalMoves(coinsArray);

		// Create rows for the board
		HBox[] rows = new HBox[3];
		for (int i = 0; i < 3; i++) {
			rows[i] = new HBox(15);
			rows[i].setAlignment(Pos.CENTER);
		}

		// Create circles for each coin
		for (int i = 0; i < coinsArray.length; i++) {
			Circle circle = new Circle(35, Color.GOLD); // Create a gold circle
			circle.setStroke(Color.BLACK); // Add a black border
			Text text = new Text(String.valueOf(coinsArray[i])); // Coin value text
			text.setFont(new Font(15));
			text.setBoundsType(TextBoundsType.VISUAL);

			StackPane stack = new StackPane(circle, text); // Stack circle and text
			circleArray[i] = stack; // Store in array

			rows[i / 10].getChildren().add(stack); // Add to the appropriate row
		}

		// Add non-empty rows to the board
		for (HBox row : rows) {
			if (!row.getChildren().isEmpty())
				board.getChildren().add(row);
		}

		return board; // Return the game board
	}

//..........................................................................................................................................................................................................................

	// Method to create a player panel
	private VBox createPlayerPanel(String playerName, String imagePath) {
		VBox panel = new VBox(10); // Create a vertical box for the player panel
		panel.setPadding(new Insets(20)); // Set padding
		panel.setStyle(
				"-fx-background-color: rgba(173, 216, 230, 0.9); -fx-background-radius: 15; -fx-border-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 5, 0.5, 1, 1);");
		panel.setAlignment(Pos.TOP_CENTER); // Center alignment

		// Load player image
		ImageView playerImage = new ImageView(new Image(imagePath));
		playerImage.setFitWidth(150);
		playerImage.setFitHeight(150);
		playerImage.setPreserveRatio(true);
		playerImage.setStyle("-fx-background-color: transparent; -fx-background-radius: 15;");

		Label nameLabel = new Label(playerName); // Create label for player name
		nameLabel.setFont(new Font("Cooper Black", 24));
		nameLabel.setStyle("-fx-font-weight: bold;");

		panel.getChildren().addAll(playerImage, nameLabel); // Add image and name to panel
		return panel; // Return the player panel
	}

//..........................................................................................................................................................................................................................

	// Method to create the button bar
	private HBox createButtonBar() {
		Button startButton = new Button("Start"); // Create start button
		startButton.setOnAction(e -> startSimulation()); // Set action for button
		styleButton(startButton); // Style the button

		Button showTableButton = new Button("Show Table"); // Show table button
		showTableButton.setOnAction(e -> showOptimalMovesTable(tableC)); // Set action
		styleButton(showTableButton); // Style button

		Button backButton = new Button("Back"); // Back button
		backButton.setOnAction(e -> returnToInitialPage(stage)); // Set action
		styleButton(backButton); // Style button

		HBox buttonBar = new HBox(20, startButton, showTableButton, backButton); // Create button bar
		buttonBar.setAlignment(Pos.CENTER); // Center alignment
		buttonBar.setPadding(new Insets(20)); // Set padding
		return buttonBar; // Return the button bar
	}

//..........................................................................................................................................................................................................................

	// Method to return to the initial page
	private void returnToInitialPage(Stage primaryStage) {
		if (primaryStage != null) {
			primaryStage.close(); // Close the current stage
		}
		HelloPage helloPage = new HelloPage(); // Create a new HelloPage
		Stage initialStage = new Stage();
		helloPage.start(initialStage); // Start the HelloPage
		initialStage.show(); // Show the initial stage
	}

//..........................................................................................................................................................................................................................

	// Method to style buttons
	private void styleButton(Button button) {
		button.setFont(new Font("Cooper Black", 18)); // Set font size
		button.setTextFill(Color.WHITE); // Set text color
		button.setStyle("-fx-background-color: dodgerblue; -fx-padding: 15px 30px; -fx-background-radius: 10;"); // Set
																													// button
																													// style
	}

//..........................................................................................................................................................................................................................

	private void startSimulation() {
		resultLabel.setText(""); // Clear previous results
		playerOneCoins = 0; // Reset player one coins
		playerTwoCoins = 0; // Reset player two coins

		// StringBuilder to track selected coins for each player
		StringBuilder playerOneSelectedCoins = new StringBuilder("Coins: ");
		StringBuilder playerTwoSelectedCoins = new StringBuilder("Coins: ");

		// Calculate optimal result and track the coins selected by each player
		int start = 0, end = coinsArray.length - 1; // Start and end indices
		boolean isPlayerOneTurn = true; // Track whose turn it is

		while (start <= end) {
			if (isPlayerOneTurn) {
				// Logic for Player One's turn
				if (tableC[start][end].currentCoin == coinsArray[start]) {
					playerOneSelectedCoins.append(coinsArray[start]).append(", "); // Append selected coin
					start++; // Move start index
				} else {
					playerOneSelectedCoins.append(coinsArray[end]).append(", "); // Append selected coin
					end--; // Move end index
				}
			} else {
				// Logic for Player Two's turn
				if (tableC[start][end].currentCoin == coinsArray[start]) {
					playerTwoSelectedCoins.append(coinsArray[start]).append(", "); // Append selected coin
					start++; // Move start index
				} else {
					playerTwoSelectedCoins.append(coinsArray[end]).append(", "); // Append selected coin
					end--; // Move end index
				}
			}
			isPlayerOneTurn = !isPlayerOneTurn; // Switch turns
		}

		// Remove the trailing comma and space
		if (playerOneSelectedCoins.length() > 7) {
			playerOneSelectedCoins.setLength(playerOneSelectedCoins.length() - 2); // Remove last two characters
		}
		if (playerTwoSelectedCoins.length() > 7) {
			playerTwoSelectedCoins.setLength(playerTwoSelectedCoins.length() - 2); // Remove last two characters
		}

		// Display results in player panels
		Label playerOneResultLabel = new Label("Expected Result: " + tableC[0][coinsArray.length - 1].max);
		playerOneResultLabel.setFont(new Font("Cooper Black", 16));
		playerOneResultLabel.setTextFill(Color.RED);

		Label playerTwoResultLabel = new Label(
				"Expected Result: " + (coinsArrayTotal() - tableC[0][coinsArray.length - 1].max));
		playerTwoResultLabel.setFont(new Font("Cooper Black", 16));
		playerTwoResultLabel.setTextFill(Color.BLUE);

		// Use TextArea for coin labels to allow wrapping
		TextArea playerOneCoinsLabel = new TextArea(playerOneSelectedCoins.toString());
		playerOneCoinsLabel.setFont(new Font("Cooper Black", 14));
		playerOneCoinsLabel.setWrapText(true);
		playerOneCoinsLabel.setEditable(false);
		playerOneCoinsLabel.setPrefRowCount(1); // Adjust row count as needed
		playerOneCoinsLabel.setPrefWidth(200); // Set a preferred width
		playerOneCoinsLabel.setStyle("-fx-control-inner-background: white; -fx-text-fill: red;"); // Set text color to
																									// red

		TextArea playerTwoCoinsLabel = new TextArea(playerTwoSelectedCoins.toString());
		playerTwoCoinsLabel.setFont(new Font("Cooper Black", 14));
		playerTwoCoinsLabel.setWrapText(true);
		playerTwoCoinsLabel.setEditable(false);
		playerTwoCoinsLabel.setPrefRowCount(1); // Adjust row count as needed
		playerTwoCoinsLabel.setPrefWidth(200); // Set a preferred width
		playerTwoCoinsLabel.setStyle("-fx-control-inner-background: white; -fx-text-fill: blue;"); // Set text color to
																									// blue

		// Add result labels to player panels
		playerOnePanel.getChildren().addAll(playerOneResultLabel, playerOneCoinsLabel);
		playerTwoPanel.getChildren().addAll(playerTwoResultLabel, playerTwoCoinsLabel);

		// Simulate coin selection visually
		new Thread(() -> {
			populateSelectedCoins(playerOneSelectedCoins.toString(), playerTwoSelectedCoins.toString());
			Platform.runLater(() -> {
				int playerOneScore = tableC[0][coinsArray.length - 1].max;
				int playerTwoScore = coinsArrayTotal() - playerOneScore;

				String winner;
				if (playerOneScore > playerTwoScore) {
					winner = "Player One";
				} else if (playerOneScore < playerTwoScore) {
					winner = "Player Two";
				} else {
					winner = "It's a Tie!";
				}
				showCongratulationsStage(winner, Math.max(playerOneScore, playerTwoScore)); // Show congratulations
																							// stage
			});
		}).start();
	}

//..................................................................................................

	// Helper method to calculate total coin value
	private int coinsArrayTotal() {
		int total = 0; // Initialize total
		for (int coin : coinsArray) {
			total += coin; // Sum all coin values
		}
		return total; // Return total
	}

//..........................................................................................................................................................................................................................

	// Method to populate selected coins visually
	private void populateSelectedCoins(String string, String string2) {
		int turn = 1; // 1 for Player One, 2 for Player Two

		for (int i = 0; i < coinsArray.length; i++) {
			int coin = coinsArray[i]; // Current coin

			// Skip the visual representation as we removed coloring

			turn = 3 - turn; // Alternate between players

			try {
				Thread.sleep(10); // Simulate the step-by-step process
			} catch (InterruptedException e) {
				e.printStackTrace(); // Handle interruption
			}
		}
	}

//..........................................................................................................................................................................................................................

	// Method to show the congratulations stage
	private void showCongratulationsStage(String winner, int score) {
		Platform.runLater(() -> {
			Stage congratulationsStage = new Stage(); // Create a new stage for congratulations
			Image backgroundImage = new Image("cong.gif"); // Load background image
			ImageView backgroundImageView = new ImageView(backgroundImage);
			backgroundImageView.setFitWidth(800);
			backgroundImageView.setFitHeight(600);

			StackPane root = new StackPane(); // Create a root pane
			root.getChildren().add(backgroundImageView); // Add background image

			VBox vbox = new VBox(20); // Create a vertical box for content
			vbox.setAlignment(Pos.CENTER); // Center alignment
			vbox.setPadding(new Insets(20)); // Set padding
			vbox.setStyle("-fx-background-color: rgba(230, 230, 250, 0.9);"); // Set background color

			// Create label for congratulations message
			Label congratulationsLabel = new Label("Congratulations " + winner + "!");
			congratulationsLabel.setFont(Font.font("Cooper Black", 50));
			congratulationsLabel.setTextFill(Color.DARKSLATEBLUE); // Set text color
			congratulationsLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 10, 0.5, 1, 1);");

			Label scoreLabel = new Label("Your Optimal Score: " + score); // Score label
			scoreLabel.setFont(Font.font("Cooper Black", 30));
			scoreLabel.setTextFill(Color.DARKSLATEBLUE); // Set text color
			scoreLabel.setStyle("-fx-effect: dropshadow(gaussian, black, 5, 0.3, 0, 0);");

			Button closeButton = new Button("Close"); // Close button
			closeButton.setFont(Font.font("Cooper Black", 20));
			closeButton.setStyle(
					"-fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
			closeButton.setOnAction(e -> congratulationsStage.close()); // Set action to close stage
			closeButton.setOnMouseEntered(e -> closeButton
					.setStyle("-fx-background-color: lightblue; -fx-text-fill: black; -fx-border-color: lightblue;")); // Mouse
																														// over
																														// effect
			closeButton.setOnMouseExited(e -> closeButton
					.setStyle("-fx-background-color: dodgerblue; -fx-text-fill: white; -fx-border-color: lightblue;")); // Mouse
																														// out
																														// effect

			Image congImage = new Image("clap.gif"); // Load congratulations image
			ImageView congImageView = new ImageView(congImage);
			congImageView.setFitWidth(150);
			congImageView.setFitHeight(150);

			// Add components to the vertical box
			vbox.getChildren().addAll(congImageView, congratulationsLabel, scoreLabel, closeButton);
			root.getChildren().add(vbox); // Add vertical box to root

			Scene scene = new Scene(root, 800, 600); // Create scene
			congratulationsStage.setTitle("Game Over"); // Set title
			congratulationsStage.setScene(scene); // Set scene
			congratulationsStage.show(); // Show the stage
		});
	}

//..........................................................................................................................................................................................................................

	private void showOptimalMovesTable(Coins[][] tableC) {
		Stage stage = new Stage(); // Create a new stage for the table
		TextArea textArea = new TextArea(); // Text area for displaying the table

		// Calculate max length for formatting
		int maxLength = 0;
		for (Coins[] row : tableC) {
			for (Coins cell : row) {
				if (cell != null) {
					maxLength = Math.max(maxLength, String.valueOf(cell.max).length());
				}
			}
		}
		maxLength += 2; // Add padding for clarity

		StringBuilder tableBuilder = new StringBuilder();

		// Add column headers
		tableBuilder.append(String.format("%-" + maxLength + "s", " ")); // Empty cell for row/column intersection
		for (int col = 0; col < tableC[0].length; col++) {
			tableBuilder.append(String.format("%-" + maxLength + "s", "j=" + col));
		}
		tableBuilder.append("\n");
		tableBuilder.append(
				"------------------------------------------------------------------------------------------------------------------------------------------------");
		tableBuilder.append("\n");

		// Add rows with row headers
		for (int row = 0; row < tableC.length; row++) {
			tableBuilder.append(String.format("%-" + maxLength + "s", "i=" + row)); // Row header
			for (int col = 0; col < tableC[row].length; col++) {
				Coins cell = tableC[row][col];
				if (cell != null) {
					tableBuilder.append(String.format("%-" + maxLength + "d", cell.max)); // Format cell value
				} else {
					tableBuilder.append(String.format("%-" + maxLength + "s", "-")); // Use "-" for null cells
				}
			}
			tableBuilder.append("\n");
		}

		textArea.setText(tableBuilder.toString()); // Set the formatted table text
		textArea.setEditable(false); // Make text area read-only

		// Enhanced styling for the text area
		textArea.setStyle(
				"-fx-font-family: 'Courier New'; " + "-fx-font-size: 14px; " + "-fx-text-fill: darkslategray; "
						+ "-fx-background-color: #f4f4f4; " + "-fx-padding: 10px; " + "-fx-border-color: #007acc; "
						+ "-fx-border-width: 2px; " + "-fx-border-radius: 5px; " + "-fx-background-radius: 5px; "
						+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 10, 0, 0, 0);");

		// Create a title label
		Label titleLabel = new Label("Optimal Moves Table");
		titleLabel.setFont(new Font("Cooper Black", 16));
		titleLabel.setStyle("-fx-font-weight: bold; " + "-fx-text-fill: #007acc; " + "-fx-padding: 10px; "
				+ "-fx-alignment: center;");

		// Use a VBox for layout
		VBox vbox = new VBox(titleLabel, textArea);
		vbox.setStyle("-fx-background-color: #e6f7ff; " + // Light blue background
				"-fx-padding: 20px; " + "-fx-alignment: center;");

		Scene scene = new Scene(vbox, 600, 400); // Adjust scene dimensions
		stage.setScene(scene);
		stage.setTitle("Optimal Moves Table"); // Set title
		stage.show(); // Show the stage
	}

//..........................................................................................................................................................................................................................

	// Method to calculate optimal moves for the game
	private Coins[][] calculateOptimalMoves(int[] coins) {
		int c = coins.length; // Get the number of coins
		Coins[][] table = new Coins[c][c]; // Create a 2D array to hold optimal values

		// Initialize the table
		for (int i = 0; i < c; i++) {
			for (int j = 0; j < c; j++) {
				table[i][j] = new Coins(); // Initialize each Coins object
			}
		}

		
		for (int i = 0; i < c; i++) {
			
			table[i][i].max = coins[i];
			table[i][i].currentCoin = coins[i];
			table[i][i].currentX = i;
			table[i][i].currentY = i;
			
		}

		// Loop through possible differences between indices
		for (int dif = 0; dif < c; dif++) {

			// Loop to fill in the table for each pair of indices (i, j)
			for (int i = 0, j = dif; j < c; i++, j++) {
				// Declare variables to hold the potential maximum values
				int x = 0, y = 0, z = 0;

				// Set values only if conditions are met
				if (i + 2 <= j) {
					x = table[i + 2][j].max; // Value if choosing the leftmost coin next
				}
				if (i + 1 <= j - 1) {
					y = table[i + 1][j - 1].max; // Value if choosing from both ends next
				}
				if (i <= j - 2) {
					z = table[i][j - 2].max; // Value if choosing the rightmost coin next
				}

				// Calculate potential scores for choosing the left or right coin
				int leftCoin = coins[i] + Math.min(x, y); // Score if choosing the left coin
				int rightCoin = coins[j] + Math.min(y, z); // Score if choosing the right coin

				// Store the maximum score achievable from this choice
				table[i][j].max = Math.max(leftCoin, rightCoin);
				// Track which coin was chosen for this optimal score
				table[i][j].currentCoin = (leftCoin > rightCoin) ? coins[i] : coins[j];
				// Save the current indices for the chosen coins
				table[i][j].currentX = i;
				table[i][j].currentY = j;
			}
		}

		return table; // Return the completed table of optimal moves
	}

}