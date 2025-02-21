package application;

import javafx.animation.TranslateTransition;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.util.Duration;
import java.io.*;
import java.util.*;

public class FirstPage extends Scene {

	// Constructor for the FirstPage class.
	public FirstPage(Stage primaryStage) {
		super(new StackPane());
		StackPane layout = (StackPane) getRoot();
		layout.setStyle("-fx-background-color: #fbcbc9;"); // Set background color

		// Set up the main layout using a VBox.
		VBox vBox = new VBox(20); // Use spacing for vertical arrangement
		vBox.setPadding(new Insets(60));
		vBox.setAlignment(Pos.CENTER); // Center the buttons vertically

		// Set up a welcome label.
		Label welcomeLabel = new Label("Welcome to the file compression and decompression program");
		welcomeLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 25));
		welcomeLabel.setStyle("-fx-text-fill: #000000;");

		// Array of button labels.
		String[] strings = { "Compress", "Uncompress" };

		// ArrayList to store buttons.
		ArrayList<Button> buttons = new ArrayList<>();

		// Method to set up buttons with labels and actions.
		setupButtons(strings, buttons, primaryStage);

		// Add components to the main layout.
		vBox.getChildren().addAll(welcomeLabel, buttons.get(0), buttons.get(1));
		layout.getChildren().add(vBox);
	}

	// This method sets up buttons based on an array of strings and adds them to a
	// stage in a JavaFX application.
	public static void setupButtons(String[] strings, ArrayList<Button> buttons, Stage primaryStage) {
		// Iterate over the array of strings to create buttons
		for (String label : strings) {
			Button button = new Button(label); // Create a new button with text from the strings array
			buttons.add(button); // Add the button to the buttons ArrayList
			button.setStyle("-fx-background-color: #322514; -fx-text-fill: white; -fx-font-family: 'Segoe Print';"
					+ " -fx-font-size: 20px; -fx-padding: 20px;"); // Set button style
			addButtonAnimation(button); // Add animation to the button
			button.setPrefHeight(50); // Set preferred height for the button
			button.setPrefWidth(250); // Set preferred width for the button
		}

		// Set actions for the buttons
		buttons.get(0).setOnAction(e -> {
			FileChooser fileChooser = new FileChooser(); // Create a new file chooser
			File selectedFile = fileChooser.showOpenDialog(primaryStage); // Show the file chooser

			// Check if the selected file is valid and ends with .huf, show an error if it
			// does
			if (selectedFile != null && selectedFile.getName().endsWith(".huf")) {
				ButtonAction.showAlert("Error", "Cannot compress a Huffman file.");
			} else if (selectedFile != null) { // If valid, change the scene
				ButtonAction.setScene(new HuffmanCompression(selectedFile));
			}
		});

		// Set an action for the second button
		buttons.get(1).setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(System.getProperty("user.dir"))); // Set initial directory
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Huffman file", "*.huf")); // Set
																												// extension
																												// filter
			File selectedFile = fileChooser.showOpenDialog(primaryStage); // Show the file chooser

			// Check if the selected file is valid
			if (selectedFile != null && !selectedFile.getName().endsWith(".huf")) {
				ButtonAction.showAlert("Error", "Please select a valid Huffman file.");
			} else if (selectedFile != null) { // If valid, change the scene
				ButtonAction.setScene(new HuffmanDecompression(selectedFile));
			}
		});
	}

	// Method to add animation to buttons
	private static void addButtonAnimation(Button button) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), button);
		translateTransition.setFromY(200); // Start off-screen vertically
		translateTransition.setToY(0); // Move to original position
		translateTransition.setCycleCount(1);
		translateTransition.play();
	}
}