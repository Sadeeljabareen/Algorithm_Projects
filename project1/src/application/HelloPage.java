package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class HelloPage extends Application {

	public static int[] coinsArray; // Array to hold coin values
	public static Stage stage; // Main application stage
	public Scene previousScene; // To keep track of the previous scene
	private String gameMode; // New variable to track game mode
	private static final int MAX_COINS = 50;

//..........................................................................................................................................................................................................................

	// Entry point for the JavaFX application
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;

		// Create UI elements for player selection
		Label titleLabel = new Label("Welcome to Dynamic Programming Game");
		Button singlePlayerButton = new Button("Single Player");
		Button twoPlayerButton = new Button("Two Player");

		// Style UI elements
		titleLabel.setFont(new Font("Cooper Black", 30));
		titleLabel.setTextFill(javafx.scene.paint.Color.DARKSLATEBLUE);
		singlePlayerButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		twoPlayerButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");

		// Set up action for buttons
		singlePlayerButton.setOnAction(e -> {
			gameMode = "single"; // Set game mode to single player
			showCoinInputScene(); // Show coin input scene
		});
		twoPlayerButton.setOnAction(e -> {
			gameMode = "two"; // Set game mode to two player
			showCoinInputScene(); // Show coin input scene
		});

		// Create HBox for buttons
		HBox buttonBox = new HBox(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(singlePlayerButton, twoPlayerButton);

		// Layout UI elements in VBox for the center area
		VBox centerContent = new VBox(10);
		centerContent.setAlignment(Pos.CENTER);
		centerContent.setPadding(new Insets(20));
		centerContent.getChildren().addAll(titleLabel, buttonBox);

		// Create the main layout as BorderPane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: lavender;");
		root.setCenter(centerContent);

		// Create scene
		previousScene = new Scene(root, 800, 600);

		// Set stage
		primaryStage.setTitle("Coins Game");
		primaryStage.setScene(previousScene);
		primaryStage.show();
	}

//..........................................................................................................................................................................................................................

	// Method to show the coin input scene
	private void showCoinInputScene() {
		// Create UI elements for initial input
		Label numCoinsLabel = new Label("Enter an Even Number of Coins:");
		TextField numCoinsField = new TextField();
		Button proceedButton = new Button("Proceed");

		// Style UI elements
		numCoinsLabel.setFont(new Font("Cooper Black", 16));
		proceedButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");

		// Create HBox for input field and button
		HBox inputBox = new HBox(10);
		inputBox.setAlignment(Pos.CENTER);
		inputBox.getChildren().addAll(numCoinsField, proceedButton);

		// Layout UI elements in VBox for the center area
		VBox centerContent = new VBox(10);
		centerContent.setAlignment(Pos.CENTER);
		centerContent.setPadding(new Insets(20));
		centerContent.getChildren().addAll(numCoinsLabel, inputBox);

		// Create the main layout as BorderPane
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: lavender;");
		root.setCenter(centerContent);

		// Create scene
		Scene scene = new Scene(root, 800, 600);
		previousScene = scene;

		// Set up button action
		proceedButton.setOnAction(e -> handleProceedButton(numCoinsField));

		// Set stage
		stage.setScene(scene);
	}

//..........................................................................................................................................................................................................................

	private void handleProceedButton(TextField numCoinsField) {
		try {
			int numCoins = Integer.parseInt(numCoinsField.getText().trim()); // Parse input number of coins

			if (numCoins <= 0) {
				showError("Please enter a positive number."); // Error for non-positive input
				return;
			}
			if (numCoins % 2 != 0) {
				showError("Please enter an even number of coins."); // Error for odd input
				return;
			}
			if (numCoins > MAX_COINS) {
				showError("Please enter a number of coins less than or equal to " + MAX_COINS + "."); // Error for
																										// exceeding
																										// limit
				return;
			}

			askUserInputOption(numCoins); // Proceed to ask for input option
		} catch (NumberFormatException exception) {
			showError("Please enter a valid integer for the number of coins."); // Error for invalid input
		}
	}

//..........................................................................................................................................................................................................................

	// Method to prompt the user for input options for coin values
	private void askUserInputOption(int numCoins) {
		Label optionLabel = new Label("Choose how to enter the coin values:");
		Button randomButton = new Button("Generate Randomly");
		Button manualButton = new Button("Enter Manually");
		Button fileButton = new Button("Load from File");
		Button backButton = new Button("Back");

		// Style UI elements
		optionLabel.setFont(new Font("Cooper Black", 16));
		optionLabel.setTextFill(javafx.scene.paint.Color.DARKSLATEBLUE);
		randomButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		manualButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		fileButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		backButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: lightcoral; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");

		HBox optionBox = new HBox(10);
		optionBox.setAlignment(Pos.CENTER);
		optionBox.getChildren().addAll(randomButton, manualButton, fileButton);

		VBox optionContent = new VBox(10);
		optionContent.setAlignment(Pos.CENTER);
		optionContent.setPadding(new Insets(20));
		optionContent.getChildren().addAll(optionLabel, optionBox, backButton);

		BorderPane optionRoot = new BorderPane(optionContent);
		optionRoot.setStyle("-fx-background-color: lavender;");

		Scene optionScene = new Scene(optionRoot, 800, 600);
		stage.setScene(optionScene);

		// Set actions for buttons
		randomButton.setOnAction(e -> showRandomRangeInputScene(numCoins)); // Call method for range input
		manualButton.setOnAction(e -> askManualEntry(numCoins)); // Call method for manual entry
		fileButton.setOnAction(e -> loadCoinsFromFile("C:\\Users\\Dell\\Desktop\\Algorothm 2024\\coins.txt", numCoins)); // Load
																															// from
																															// file
		backButton.setOnAction(e -> stage.setScene(previousScene)); // Go back to previous scene
	}

//..........................................................................................................................................................................................................................

	// Method to show the random range input scene
	private void showRandomRangeInputScene(int numCoins) {
		// Create UI elements for random range input
		Label minLabel = new Label("Enter the minimum value:");
		TextField minField = new TextField();
		Label maxLabel = new Label("Enter the maximum value:");
		TextField maxField = new TextField();
		Button proceedButton = new Button("Generate Coins");
		Button backButton = new Button("Back");

		// Style UI elements
		minLabel.setFont(new Font("Cooper Black", 16));
		maxLabel.setFont(new Font("Cooper Black", 16));
		proceedButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		backButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: lightcoral; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");

		VBox rangeContent = new VBox(10);
		rangeContent.setAlignment(Pos.CENTER);
		rangeContent.setPadding(new Insets(20));
		rangeContent.getChildren().addAll(minLabel, minField, maxLabel, maxField, proceedButton, backButton);

		BorderPane rangeRoot = new BorderPane(rangeContent);
		rangeRoot.setStyle("-fx-background-color: lavender;");

		Scene rangeScene = new Scene(rangeRoot, 800, 600);
		stage.setScene(rangeScene);

		// Set up button action for generating coins
		proceedButton.setOnAction(e -> {
			try {
				int min = Integer.parseInt(minField.getText().trim()); // Parse minimum value
				int max = Integer.parseInt(maxField.getText().trim()); // Parse maximum value

				// Validate the input values
				if (min <= 0 || max <= 0 || min >= max) {
					showError("Please enter valid positive integers where the maximum is greater than the minimum."); // Error
																														// for
																														// invalid
																														// range
					return;
				}

				generateRandomCoins(numCoins, min, max); // Generate random coins
			} catch (NumberFormatException exception) {
				showError("Please enter valid integers for the range."); // Error for non-integer input
			}
		});

		// Back button action
		backButton.setOnAction(e -> askUserInputOption(numCoins)); // Go back to input options
	}

//..........................................................................................................................................................................................................................

	// Method to generate random coin values
	private void generateRandomCoins(int numCoins, int min, int max) {
		Random random = new Random();
		coinsArray = new int[numCoins]; // Initialize coins array
		for (int i = 0; i < numCoins; i++) {
			coinsArray[i] = random.nextInt((max - min) + 1) + min; // Generate random value within range
		}
		showGamePage(); // Proceed to game page
	}

//..........................................................................................................................................................................................................................

	// Method to prompt user for manual entry of coin values
	private void askManualEntry(int numCoins) {
		Label instructionLabel = new Label("Enter the coin values separated by commas (,):");
		TextField inputField = new TextField();
		Button startButton = new Button("Start Game");
		Button backButton = new Button("Back");

		instructionLabel.setFont(new Font("Cooper Black", 16));
		instructionLabel.setTextFill(javafx.scene.paint.Color.DARKSLATEBLUE);
		startButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: dodgerblue; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");
		backButton.setStyle(
				"-fx-font-family: 'Cooper Black'; -fx-background-color: lightcoral; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 10;");

		HBox inputBox = new HBox(10);
		inputBox.setAlignment(Pos.CENTER);
		inputBox.getChildren().addAll(inputField, startButton);

		VBox manualContent = new VBox(10);
		manualContent.setAlignment(Pos.CENTER);
		manualContent.setPadding(new Insets(20));
		manualContent.getChildren().addAll(instructionLabel, inputBox, backButton);

		BorderPane manualRoot = new BorderPane(manualContent);
		manualRoot.setStyle("-fx-background-color: lavender;");

		Scene manualScene = new Scene(manualRoot, 800, 600);
		stage.setScene(manualScene);

		// Set actions for buttons
		startButton.setOnAction(e -> handleManualEntry(inputField, numCoins)); // Handle manual entry
		backButton.setOnAction(e -> askUserInputOption(numCoins)); // Go back to input options
	}

//..........................................................................................................................................................................................................................

	// Method to load coin values from a file
	private void loadCoinsFromFile(String filePath, int expectedNumCoins) {
		File file = new File(filePath);
		if (file.exists()) {
			try (Scanner scanner = new Scanner(file)) {
				boolean foundMatchingCoins = false; // Flag to track if matching coins found
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine().trim(); // Read each line
					String[] values = line.split("\\s+"); // Split into values
					if (values.length < 2) {
						showError("Each line should contain the number of coins followed by their values."); // Error
																												// for
																												// insufficient
																												// values
						return;
					}

					int numCoinsInLine; // Variable to hold number of coins in line
					try {
						numCoinsInLine = Integer.parseInt(values[0]); // Parse number of coins
					} catch (NumberFormatException e) {
						showError("The first value must be an integer representing the number of coins."); // Error for
																											// non-integer
																											// input
						return;
					}

					// Check if the number of coins in file matches expected number
					if (numCoinsInLine == expectedNumCoins) {
						if (values.length - 1 < numCoinsInLine) {
							showError("Not enough coin values provided for the specified number of coins."); // Error
																												// for
																												// insufficient
																												// values
							return;
						}
						coinsArray = new int[numCoinsInLine]; // Initialize coins array
						for (int i = 1; i <= numCoinsInLine; i++) {
							try {
								int value = Integer.parseInt(values[i]); // Parse coin value
								if (value <= 0) {
									showError("Coin values must be positive integers."); // Error for non-positive
																							// values
									return;
								}
								coinsArray[i - 1] = value; // Store coin value
							} catch (NumberFormatException ex) {
								showError("Invalid coin value: " + values[i]); // Error for invalid coin value
								return;
							}
						}
						foundMatchingCoins = true; // Set flag to true if matching coins found
						break; // Exit loop once matching coins found
					}
				}
				if (foundMatchingCoins) {
					showGamePage(); // Show game page if matching coins found
				} else {
					showError("No matching number of coins found in the file."); // Error if no matches found
				}
			} catch (FileNotFoundException e) {
				showError("File not found. Please check the file path."); // Error for file not found
			}
		} else {
			showError("File does not exist. Please provide a valid file path."); // Error for invalid file path
		}
	}

//..........................................................................................................................................................................................................................

	// Method to handle manual entry of coin values
	private void handleManualEntry(TextField inputField, int numCoins) {
		String[] arr = inputField.getText().trim().split(","); // Split input by commas
		if (arr.length != numCoins) {
			showError("Please enter exactly " + numCoins + " coin values."); // Error for incorrect number of values
			return;
		}
		coinsArray = new int[numCoins]; // Initialize coins array
		for (int i = 0; i < arr.length; i++) {
			try {
				int x = Integer.parseInt(arr[i].trim()); // Parse each coin value
				if (x > 0) {
					coinsArray[i] = x; // Store positive coin value
				} else {
					showError("Please enter positive integers only."); // Error for non-positive values
					return;
				}
			} catch (NumberFormatException exception) {
				showError("Please enter valid numbers separated by commas."); // Error for non-integer input
				return;
			}
		}
		showGamePage(); // Show game page after successful entry
	}

//..........................................................................................................................................................................................................................

	// Method to show an error message in an alert dialog
	public void showError(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR); // Create error alert
		Image gifImage = new Image("no.gif"); // Provide the correct path to your GIF
		ImageView imageView = new ImageView(gifImage);
		imageView.setFitWidth(80); // Set image width
		imageView.setFitHeight(80); // Set image height
		alert.setGraphic(imageView);
		alert.setContentText(message); // Set error message
		alert.show(); // Show alert
	}

//..........................................................................................................................................................................................................................

	// Method to show the game page based on the selected game mode
	private void showGamePage() {
		if (gameMode.equals("single")) {
			new GamePage(stage, coinsArray); // Handle single player mode
		} else {
			new TwoPlayer(stage, coinsArray, this); // Pass reference to HelloPage for two player mode
		}
	}

}