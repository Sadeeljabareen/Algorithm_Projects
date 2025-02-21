package application;

import java.io.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;

public class HuffmanDecompression extends Scene {

    // Class member variables
    private VBox vBox; 
    private File path; 
    private Tree[] bytes; 
    private long lengthFileBefore; 
    private long lengthFileAfter; 
    private StringBuilder headerToShow = new StringBuilder(""); 
    private StringBuilder nameOfUncompressedFile; 

    // Constructor for HuffmanDecompression
    public HuffmanDecompression(File path) {
        super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(),
                Screen.getPrimary().getVisualBounds().getHeight());
        StackPane layout = (StackPane) getRoot();
        layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        layout.getStyleClass().add("root");
        layout.setStyle("-fx-background-color: #fbcbc9;");
        this.path = path;

        // Creating and configuring a label to indicate the uncompression process
        Label waitLabel = new Label("Please wait, the file will be uncompressed soon");
        waitLabel.setFont(Font.font("Segoe Print", 20));

        // Initialize vBox for UI elements
        vBox = new VBox(40);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(waitLabel);

        // Button labels
        String[] strings = { "Back to main page" };

        // Adding VBox to the layout
        layout.getChildren().addAll(vBox);
        layout.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        layout.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

        // Start uncompression in a new thread
        new Thread(() -> {
            uncompress();

            Platform.runLater(() -> {
                if (!vBox.getChildren().isEmpty()) {
                    vBox.getChildren().remove(vBox.getChildren().size() - 1);
                }

                if (lengthFileBefore > 0) {
                    displayUncompressionResults(strings);
                } else {
                    handleEmptyFile();
                }
            });
        }).start();
    }

    // Method to display uncompression results
    private void displayUncompressionResults(String[] strings) {
        Label welcomeLabel = new Label("Successful decompression of the  " + path.getName() + " file");
        welcomeLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 30));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        Label nameOfUncompressedFileLabel = new Label("The name of uncompressed file is: " + nameOfUncompressedFile);
        nameOfUncompressedFileLabel.setFont(Font.font("Segoe Print", 20));
        nameOfUncompressedFileLabel.setStyle("-fx-text-fill: #000000;");

        Button[] buttons = new Button[strings.length];
        setupButtons(strings, buttons);
        HBox arrangementButtons = new HBox(20);
        arrangementButtons.setAlignment(Pos.CENTER);
        arrangementButtons.getChildren().addAll(buttons);

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(welcomeLabel, nameOfUncompressedFileLabel, arrangementButtons);
    }

    // Method to handle empty file case
    private void handleEmptyFile() {
        Label failLabel = new Label("The file is empty, can't be uncompressed");
        failLabel.setFont(Font.font("Segoe Print", FontWeight.BOLD, 22));
        failLabel.setStyle("-fx-text-fill: #000000;");

        Button button = new Button("Back to main page");
        button.setOnAction(e -> ButtonAction.setMainScene());
        button.getStyleClass().add("custom-button");

        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().add(button);

        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(failLabel, buttonLayout);
    }
    
//.................................................................................................................................................................................................................................
    
    public void uncompress() {
        try {
            lengthFileBefore = path.length();

            FileInputStream scan = new FileInputStream(path);
            StringBuilder fileExtension = new StringBuilder();

            byte[] buffer = new byte[8];
            byte[] sizeOfHeaderbBuffer = new byte[4];
            int sizeOfHeader = 0;

            if (scan.read(buffer) != -1) {
                for (int i = 0; i < 8; i++) {
                    if (buffer[i] != 0) {
                        fileExtension.append((char) buffer[i]);
                    }
                    headerToShow.append(ButtonAction.byteToBinaryString(buffer[i]));
                }

                scan.read(sizeOfHeaderbBuffer);
                sizeOfHeader = byteArrayToInt(sizeOfHeaderbBuffer);

                for (int i = 0; i < sizeOfHeaderbBuffer.length; i++) {
                    headerToShow.append(ButtonAction.byteToBinaryString(sizeOfHeaderbBuffer[i]));
                }
            } else {
                scan.close();
                throw new IllegalArgumentException("The input file cannot be read");
            }

            int numberOfBytesForHeader = (sizeOfHeader + 7) / 8;
            StringBuilder header = new StringBuilder();
            StringBuilder serialData = new StringBuilder();
            int numberOfBytesRead = 0, counterHowManyByteReadFromHeader = 0;

            while ((numberOfBytesRead = scan.read(buffer)) != -1) {
                for (int i = 0; i < numberOfBytesRead; i++) {
                    if (counterHowManyByteReadFromHeader < numberOfBytesForHeader) {
                        header.append(ButtonAction.byteToBinaryString(buffer[i]));
                        counterHowManyByteReadFromHeader++;
                    } else {
                        serialData.append(ButtonAction.byteToBinaryString(buffer[i]));
                    }
                }
            }
            scan.close();
            headerToShow.append(header);

            // Huffman tree reconstruction
            Stack stack = new Stack(256);
            int counter = 0, numberOfLeafNode = 0;
            while (counter < sizeOfHeader) {
                if (header.charAt(counter) == '0') {
                    counter++;
                    stack.push(new Tree((byte) Integer.parseInt(header.substring(counter, counter + 8), 2)));
                    numberOfLeafNode++;
                    counter += 8;
                } else {
                    counter++;
                    Tree node = new Tree(0);
                    node.setRight(stack.pop());
                    node.setLeft(stack.pop());
                    stack.push(node);
                }
            }
            Tree rootTreeNode = stack.peek();

            if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null)
                rootTreeNode.setCode("1");
            else
                Tree.gaveCodeForEachByte(rootTreeNode);

            bytes = new Tree[numberOfLeafNode];
            rootTreeNode.getLeafNodes(bytes);

            // Prepare the name for the uncompressed file
            nameOfUncompressedFile = new StringBuilder(
                    ButtonAction.replaceExtension(path.getName(), fileExtension.toString()));
            ButtonAction.getUniqueName(nameOfUncompressedFile);

            // Write the uncompressed data to a file in the same directory
            FileOutputStream out = new FileOutputStream(new File(path.getParent(), nameOfUncompressedFile.toString()));
            int startIndex = serialData.length() - 8;
            int numberOfEffectiveBits = Integer.parseInt(serialData.substring(startIndex), 2);
            serialData.delete(startIndex + numberOfEffectiveBits - 8, serialData.length());

            byte[] bufferOut = new byte[8];
            int counterForBufferSerialData = 0, counterForBufferOut = 0;

            while (counterForBufferSerialData < serialData.length()) {
                Tree curr = rootTreeNode;

                while (curr != null && counterForBufferSerialData < serialData.length()) {
                    if (serialData.charAt(counterForBufferSerialData) == '0' && curr.hasLeft()) {
                        curr = curr.getLeft();
                    } else if (curr.hasRight()) {
                        curr = curr.getRight();
                    } else if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null) {
                        counterForBufferSerialData++;
                        break;
                    } else {
                        break;
                    }

                    counterForBufferSerialData++;
                }

                bufferOut[counterForBufferOut++] = curr.getByteContent();
                if (counterForBufferOut == 8) {
                    out.write(bufferOut);
                    counterForBufferOut = 0;
                }
            }

            if (counterForBufferOut > 0)
                out.write(bufferOut, 0, counterForBufferOut);

            out.close();

            File toCheck = new File(path.getParent(), nameOfUncompressedFile.toString());
            lengthFileAfter = toCheck.length();

        } catch (Exception e) {
            ButtonAction.showAlert("Error", e.getMessage());
        }
    }
    
//.............................................................................................................................................................................................................................................
    
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

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

            button.setStyle(gradients[i % gradients.length]
                    + "-fx-text-fill: white; -fx-font-family: 'Segoe Print'; -fx-font-size: 15px;"
                    + "-fx-padding: 15px; -fx-background-radius: 12px;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"
                    + "-fx-font-weight: bold;");

            button.setOnMouseEntered(e -> button.setStyle(gradients[(index + 1) % gradients.length]
                    + "-fx-text-fill: white; -fx-font-family: 'Segoe Print'; -fx-font-size: 15px;"
                    + "-fx-padding: 15px; -fx-background-radius: 12px;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 0, 5);"
                    + "-fx-scale-x: 1.1; -fx-scale-y: 1.1;"));

            button.setOnMouseExited(e -> button.setStyle(gradients[index % gradients.length]
                    + "-fx-text-fill: white; -fx-font-family: 'Segoe Print'; -fx-font-size: 15px;"
                    + "-fx-padding: 15px; -fx-background-radius: 12px;"
                    + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);"));

            if (i == 0) {
                button.setOnAction(e -> ButtonAction.setMainScene());
            }

            buttonBox.getChildren().add(button);
        }

		vBox.getChildren().add(buttonBox);
	}
}