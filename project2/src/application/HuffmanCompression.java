package application;

import java.io.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class HuffmanCompression extends Scene {

	// Class member variables
	private VBox vBox;
	File path;
	Tree[] bytes;
	long lengthFileBefore;
	long lengthFileAfter;
	StringBuilder headerToShow = new StringBuilder("");
	StringBuilder nameOfCompressedFile;

//..................................................................................................................................................................................................................................................

	// Constructor for CompressScene
	public HuffmanCompression(File path) {
		super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(),
				Screen.getPrimary().getVisualBounds().getHeight());
		StackPane layout = (StackPane) getRoot();
		layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		layout.getStyleClass().add("root");
		layout.setStyle("-fx-background-color: #fbcbc9;");
		this.path = path;

		// Creating and configuring a label to indicate the compression process
		Label waitLabel = new Label("Please wait, the file will be compressed soon");
		waitLabel.setFont(Font.font("Segoe Print", 20));

		// Initialize vBox for UI elements
		vBox = new VBox(40);
		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(waitLabel);

		// Button labels (added "Show File Info")
		String[] strings = { "Show header", "Show huffman table", "Show file info", "Back to main page" };

		// Adding VBox to the layout
		layout.getChildren().addAll(vBox);
		layout.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
		layout.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

		// Start compression in a new thread
		new Thread(() -> {
			compress();

			Platform.runLater(() -> {
				if (!vBox.getChildren().isEmpty()) {
					vBox.getChildren().remove(vBox.getChildren().size() - 1);
				}

				if (lengthFileBefore > 0) {
					displayCompressionResults(strings);
				} else {
					handleEmptyFile();
				}
			});
		}).start();
	}

//..................................................................................................................................................................................................................................................

	// Method to display compression results
	private void displayCompressionResults(String[] strings) {
		Label welcomeLabel = new Label("Compressing " + path.getName() + " file");
		welcomeLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 30));
		welcomeLabel.setStyle("-fx-text-fill: #000000;");

		Label nameOfCompressedFileLabel = new Label("The name of compressed file is: " + nameOfCompressedFile);
		nameOfCompressedFileLabel.setFont(Font.font("Segoe Print", 20));
		nameOfCompressedFileLabel.setStyle("-fx-text-fill: #000000;");

		Button[] buttons = new Button[strings.length];
		setupButtons(strings, buttons);
		HBox ArrangementButtons = new HBox(20);
		ArrangementButtons.setAlignment(Pos.CENTER);
		ArrangementButtons.getChildren().addAll(buttons);

		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(welcomeLabel, nameOfCompressedFileLabel, ArrangementButtons);
	}

//..................................................................................................................................................................................................................................................

	// Method to handle empty file case
	private void handleEmptyFile() {
		Label failLabel = new Label("The file is empty, can't be compressed");
		failLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 22));
		failLabel.setStyle("-fx-text-fill: #000000;");

		Button button = new Button("Back to main page");
		button.setOnAction(e -> ButtonAction.setMainScene());
		button.getStyleClass().add("custom-button");

		vBox.setAlignment(Pos.CENTER);
		vBox.getChildren().addAll(failLabel, button);
	}

//..................................................................................................................................................................................................................................................

	public void setupButtons(String[] strings, Button[] buttons) {
		HBox buttonBox = new HBox(20);
		buttonBox.setAlignment(Pos.CENTER);

		String[] gradients = { "-fx-background-color: linear-gradient(to right, #e75874, #be1558);",
				"-fx-background-color: linear-gradient(to right, #fbcbc9, #322514);",
				"-fx-background-color: linear-gradient(to right, #e75874, #be1558, #fbcbc9, #322514);" };

		for (int i = 0; i < strings.length; i++) {
			final int index = i;
			Button button = new Button(strings[i]);
			buttons[i] = button;
			button.getStyleClass().add("custom-button");

			button.setStyle(
					gradients[i % gradients.length] + "-fx-text-fill: white;" + "-fx-font-family: 'Segoe Print';"
							+ "-fx-font-size: 15px;" + "-fx-padding: 15px;" + "-fx-background-radius: 12px;"
							+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
							+ "-fx-font-weight: bold;");

			// Hover effect
			button.setOnMouseEntered(e -> button.setStyle(gradients[(index + 1) % gradients.length]
					+ "-fx-text-fill: white;" + "-fx-font-family: 'Segoe Print';" + "-fx-font-size: 15px;"
					+ "-fx-padding: 15px;" + "-fx-background-radius: 12px;"
					+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);"
					+ "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"));
			button.setOnMouseExited(e -> button.setStyle(
					gradients[index % gradients.length] + "-fx-text-fill: white;" + "-fx-font-family: 'Segoe Print';"
							+ "-fx-font-size: 15px;" + "-fx-padding: 15px;" + "-fx-background-radius: 12px;"
							+ "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"));

			// Add action handlers for buttons
			if (i == 0) {
				button.setOnAction(e -> ButtonAction.handleShowHeader(headerToShow));
			} else if (i == 1) {
				button.setOnAction(e -> handleShowHuffmanTable());
			} else if (i == 2) {
				button.setOnAction(e -> showFileInfo());
			} else if (i == 3) {
				button.setOnAction(e -> ButtonAction.setMainScene());
			}

			buttonBox.getChildren().add(button);
		}

		vBox.getChildren().add(buttonBox);
	}

//..................................................................................................................................................................................................................................................

	private void showFileInfo() {
		Label fileInfo = new Label("File Name: " + path.getName() + "\n" + "File Path Before Compress: "
				+ path.getAbsolutePath() + "\n" + "File Size Before Compress: " + lengthFileBefore + " bytes\n"
				+ "File Size After Compress: " + lengthFileAfter + " bytes\n" + "Compression Ratio: "
				+ String.format("%.2f", (double) lengthFileAfter / lengthFileBefore ) + "%\n");

		fileInfo.setStyle(
				"-fx-font-size: 16px; -fx-text-fill: #322514; -fx-font-weight: bold; -fx-font-family: 'Segoe Print';");

		Stage customStage = new Stage();
		customStage.setTitle("File Info");

		VBox layout = new VBox(20);
		layout.setStyle("-fx-background-color: #fbcbc9; -fx-padding: 20px; -fx-font-family: 'Segoe Print';");
		layout.getChildren().addAll(fileInfo);

		Button copyOriginalPathButton = new Button("Copy Original File Path");
		copyOriginalPathButton.setStyle(
				"-fx-background-color: #be1558; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Segoe Print';");
		copyOriginalPathButton.setOnAction(e -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(path.getAbsolutePath());
			clipboard.setContent(content);
		});

		Button copyCompressedPathButton = new Button("Copy Compressed Folder Path");
		copyCompressedPathButton.setStyle(
				"-fx-background-color: #be1558; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Segoe Print';");
		copyCompressedPathButton.setOnAction(e -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(new File(path.getParent(), nameOfCompressedFile.toString()).getAbsolutePath());
			clipboard.setContent(content);
		});

		Button closeButton = new Button("Close");
		closeButton.setStyle(
				"-fx-background-color: #e75874; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: 'Segoe Print';");
		closeButton.setOnAction(e -> customStage.close());

		layout.getChildren().addAll(copyOriginalPathButton, copyCompressedPathButton, closeButton);

		Scene scene = new Scene(layout, 800, 500);
		customStage.setScene(scene);
		customStage.show();
	}

//..................................................................................................................................................................................................................................................

	public void compress() {
		try {
			// Initialize an array of Tree objects for each byte value
			bytes = new Tree[256];
			for (int i = 0; i < 256; i++) {
				bytes[i] = new Tree((byte) i);
			}

			// Get the length of the file before compression
			lengthFileBefore = path.length();
			if (lengthFileBefore == 0) // If the file is empty, return
				return;

			byte[] bufferIn = new byte[8]; // Buffer for reading bytes
			try (FileInputStream scan = new FileInputStream(path)) {
				int countOfBytesInBuffer;
				// Read bytes from the file
				while ((countOfBytesInBuffer = scan.read(bufferIn)) != -1) {
					for (int i = 0; i < countOfBytesInBuffer; i++) {
						// Increment frequency count for each byte
						if (bufferIn[i] < 0)
							bytes[bufferIn[i] + 256].increment();
						else
							bytes[bufferIn[i]].increment();
					}
				}
			} catch (Exception e) {
				// Show alert if there's an error during file reading
				ButtonAction.showAlert("Error", e.getMessage());
			}

			// Create a heap for Huffman tree construction
			Heap heap = new Heap(256);
			for (Tree byteNode : bytes) {
				if (byteNode.getFrequency() != 0)
					heap.insert(byteNode); // Insert only non-zero frequency nodes
			}

			// Build the Huffman tree
			while (heap.getSize() > 1) {
				Tree x = heap.remove(); // Remove two nodes with the lowest frequency
				Tree y = heap.remove();
				Tree z = new Tree(x.getFrequency() + y.getFrequency()); // Create a new internal node
				z.setLeft(x);
				z.setRight(y);
				heap.insert(z); // Insert the new node back into the heap
			}
			Tree rootTreeNode = heap.remove(); // The remaining node is the root of the Huffman tree

			// Assign codes to each byte
			if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null)
				rootTreeNode.setCode("1"); // Handle the case of a single-node tree
			else
				Tree.gaveCodeForEachByte(rootTreeNode); // Generate codes for each byte

			// Prepare header for the compressed file
			StringBuilder header = new StringBuilder(rootTreeNode.traverse());
			String fileExtension = path.getName().substring(path.getName().lastIndexOf(".") + 1);
			for (int i = 0; i < bufferIn.length; i++) {
			    if (i < fileExtension.length()) {
			        bufferIn[i] = (byte) fileExtension.charAt(i); // Write file extension to header
			        header.append(ButtonAction.byteToBinaryString(bufferIn[i]));
			    } else {
			        bufferIn[i] = (byte) 0; // Fill remaining bytes with zeros
			        header.append("00000000");
			    }
			}

			// Include header size in the header
			byte[] bufferForHeaderSize = { 
			    (byte) (header.length() >> 24), 
			    (byte) (header.length() >> 16),
			    (byte) (header.length() >> 8), 
			    (byte) header.length() 
			};
			for (byte b : bufferForHeaderSize) {
			    header.append(ButtonAction.byteToBinaryString(b));
			}

			// Pad the header to a multiple of 8 bits
			if (header.length() % 8 != 0) {
			    int paddingLength = 8 - header.length() % 8;
			    for (int i = 0; i < paddingLength; i++) {
			        header.append("0");
			    }
			}

			// Store the header as a single line
			headerToShow.append(header);


			// Prepare the name for the compressed file
			nameOfCompressedFile = new StringBuilder(ButtonAction.replaceExtension(path.getName(), "huf"));
			ButtonAction.getUniqueName(nameOfCompressedFile);
			FileOutputStream out = new FileOutputStream(new File(path.getParent(), nameOfCompressedFile.toString()));

			// Write the file extension and header size to the output file
			out.write(bufferIn);
			out.write(bufferForHeaderSize);

			// Write the encoded data to the output file
			int numOfBytes = header.length() / 8;
			int sizeForLastBuffer = numOfBytes % 8;
			for (int i = 0; i < numOfBytes; i++) {
				String byteString = header.substring(i * 8, (i + 1) * 8);
				bufferIn[i % 8] = (byte) Integer.parseInt(byteString, 2);
				if (i % 8 == 7)
					out.write(bufferIn); // Write every 8 bytes
			}
			if (sizeForLastBuffer > 0)
				out.write(bufferIn, 0, sizeForLastBuffer); // Write remaining bytes if any

			// Read original file data and encode it
			StringBuilder data = new StringBuilder();
			byte[] bufferOut = new byte[8];
			int bufferLength;
			try (FileInputStream scan = new FileInputStream(path)) {
				while ((bufferLength = scan.read(bufferIn)) != -1) {
					for (int k = 0; k < bufferLength; k++) {
						// Append the Huffman code for each byte
						if (bufferIn[k] < 0)
							data.append(bytes[bufferIn[k] + 256].getCode());
						else
							data.append(bytes[bufferIn[k]].getCode());

						// Write to the output file every 64 bits
						if (data.length() >= 64) {
							for (int i = 0; i < 8; i++) {
								bufferOut[i] = (byte) Integer.parseInt(data.substring(0, 8), 2);
								data.delete(0, 8);
							}
							out.write(bufferOut);
						}
					}
				}
			} catch (Exception e) {
				// Show alert if there's an error during encoding
				ButtonAction.showAlert("Error", e.getMessage());
			}

			// Handle any remaining bits and write final byte indicating effective bits
			int numberOfEffectiveBits = data.length() % 8;
			if (numberOfEffectiveBits % 8 != 0) {
				int paddingLength = 8 - numberOfEffectiveBits;
				for (int i = 0; i < paddingLength; i++)
					data.append("0"); // Pad with zeros
			} else {
				numberOfEffectiveBits = 8;
			}

			if (data.length() > 0) {
				int remainsBytes = data.length() / 8;
				byte[] bufferOut1 = new byte[remainsBytes];
				for (int i = 0; i < remainsBytes; i++) {
					bufferOut1[i] = (byte) Integer.parseInt(data.substring(0, 8), 2);
					data.delete(0, 8);
				}
				out.write(bufferOut1); // Write remaining bytes
			}

			out.write((byte) numberOfEffectiveBits); // Write the number of effective bits
			out.close();

			// Check the length of the compressed file
			File toCheck = new File(path.getParent(), nameOfCompressedFile.toString());
			lengthFileAfter = toCheck.length();

		} catch (Exception e) {
			// Show alert if there's an error during compression
			ButtonAction.showAlert("Error", e.getMessage());
		}
	}

//.........................................................................................................................................................................................................................................

	private void handleShowHuffmanTable() {
		// Create a ScrollPane
		ScrollPane subScenePane = new ScrollPane();
		subScenePane.setFitToWidth(true); // Allow the content to fit the width of the ScrollPane

		// Create a VBox for the background
		VBox background = new VBox(20);
		background.setPadding(new Insets(40));
		background.setStyle(
				"-fx-background-color: linear-gradient(to bottom, #ffdde1, #ee9ca7); -fx-border-radius: 20px; -fx-padding: 20px;");
		background.setEffect(new DropShadow(20, Color.GREY));
		background.setAlignment(Pos.CENTER); // Center the content in VBox

		Label huffmanLabel = new Label("🌟 *** Huffman Table *** 🌟");
		huffmanLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 36));
		huffmanLabel.setStyle("-fx-text-fill: #322514; -fx-underline: true;");

		// Create a GridPane for the table
		GridPane gridPane = new GridPane();
		gridPane.setVgap(15);
		gridPane.setHgap(35);
		gridPane.setPadding(new Insets(20));
		gridPane.setAlignment(Pos.CENTER); // Center the table in GridPane

		// Define table headers
		String[] headersText = { "Byte", "Huffman", "Frequency", "Size" };
		for (int i = 0; i < headersText.length; i++) {
			Label header = new Label(headersText[i]);
			styleHeaderLabel(header);
			GridPane.setConstraints(header, i, 0);
			GridPane.setHalignment(header, HPos.CENTER);
			gridPane.getChildren().add(header);
		}

		// Populate the table with data
		int rowIndex = 1;
		for (Tree node : bytes) {
			if (node.getCode() != null) {
				Label byteLabel = new Label(node.getByteContent() + "");
				Label codeLabel = new Label(node.getCode());
				Label frequencyLabel = new Label(node.getFrequency() + "");
				Label lengthLabel = new Label(node.getCode().length() + "");

				styleContentLabel(byteLabel);
				styleContentLabel(codeLabel);
				styleContentLabel(frequencyLabel);
				styleContentLabel(lengthLabel);

				gridPane.add(byteLabel, 0, rowIndex);
				gridPane.add(codeLabel, 1, rowIndex);
				gridPane.add(frequencyLabel, 2, rowIndex);
				gridPane.add(lengthLabel, 3, rowIndex);

				rowIndex++;
			}
		}

		// Add the header and gridPane to the background
		background.getChildren().addAll(huffmanLabel, gridPane);

		// Set the content of the ScrollPane
		subScenePane.setContent(background);

		// Create a scene and stage
		Scene scene = new Scene(subScenePane, 800, 500);
		Stage newStage = new Stage();
		newStage.setTitle("Huffman Table");
		newStage.setScene(scene);
		newStage.setResizable(false); // Prevent resizing of the window
		newStage.show();

		// Center the stage on the screen
		newStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - newStage.getWidth()) / 2);
		newStage.setY((Screen.getPrimary().getVisualBounds().getHeight() - newStage.getHeight()) / 2);
	}

//...........................................................
	private static void styleHeaderLabel(Label label) {
		label.setStyle(
				"-fx-text-fill: #ffffff; -fx-font-family: 'Segoe Print'; -fx-font-weight: bold; -fx-font-size: 20px;");
	}

//...........................................................
	private static void styleContentLabel(Label label) {
		label.setStyle(
				"-fx-text-fill: #322514; -fx-font-family: 'Segoe Print'; -fx-font-weight: bold; -fx-font-size: 18px;");
	}

}