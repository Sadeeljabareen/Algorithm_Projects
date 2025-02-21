package application;

import java.io.File;
import java.util.UUID;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ButtonAction {

	private static Stage primaryStage;

	// Constants for styling
	private static final String FONT_FAMILY = "Segoe Print";
	private static final String CONTENT_STYLE = "-fx-text-fill: #322514; -fx-font-family: '" + FONT_FAMILY
			+ "'; -fx-font-weight: bold;";
	private static final String BIT_LABEL_STYLE = "-fx-text-fill: #322514; -fx-font-family: '" + FONT_FAMILY
			+ "'; -fx-font-weight: bold; -fx-background-color: #e75874; -fx-padding: 5px; -fx-background-radius: 8px;";

	public static void setPrimaryStage(Stage stage) {
		primaryStage = stage;
	}

	public static void setMainScene() {
		primaryStage.setScene(new FirstPage(primaryStage));
	}

	public static void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

	public static void showAlert(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static void getUniqueName(StringBuilder fileName) {
		String originalPath = fileName.toString();
		File file = new File(originalPath);

		String fileNameOnly = file.getName();

		String directoryPath = file.getParent();

		while (file.exists()) {
			String newName = UUID.randomUUID().toString();

			fileName.setLength(0); // Clear existing content
			fileName.append(directoryPath).append(File.separator).append(newName);
			file = new File(fileName.toString());
		}
	}

	public static String replaceExtension(String fileName, String newExtension) {
		int lastDotIndex = fileName.lastIndexOf(".");
		return (lastDotIndex == -1) ? fileName + "." + newExtension
				: fileName.substring(0, lastDotIndex) + "." + newExtension;
	}

//......................................................................................................................................................................................................................................................

	public static void handleShowHeader(StringBuilder headerToShow) {
		StringBuilder header = new StringBuilder(headerToShow);
		ScrollPane subScenePane = new ScrollPane();

		VBox background = new VBox(20);
		background.setPadding(new Insets(40));
		background.setStyle(
				"-fx-background-color: linear-gradient(to bottom, #ffdde1, #ee9ca7); -fx-border-radius: 20px; -fx-padding: 20px;");
		background.setEffect(new DropShadow(20, Color.GREY));

		Label headerLabel = new Label("🌟 *** The Header *** 🌟");
		headerLabel.setFont(Font.font(FONT_FAMILY, FontWeight.BOLD, 36));
		headerLabel.setStyle("-fx-text-fill: #322514; -fx-underline: true;");

		String extensionInBitString = "", extensionInCharString = "";
		for (int i = 0; i < 8; i++) {
			extensionInCharString += (char) Integer.parseInt(header.substring(i * 8, (i + 1) * 8), 2);
			extensionInBitString += header.substring(i * 8, (i + 1) * 8) + " ";
		}
		header.delete(0, 64);

		TextFlow extensionInBitTextFlow = createTextFlow("🧩 Extension in bits: ", extensionInBitString);
		Label extensionInCharLabel = new Label("🧩 Extension in char: " + extensionInCharString);
		styleContentLabel(extensionInCharLabel);

		TextFlow sizeInBitTextFlow = createTextFlow("🧩 Size of header in bits: ", header.substring(0, 32));
		int sizeOfHeader = Integer.parseInt(header.substring(0, 32), 2);
		header.delete(0, 32);

		int sizeOfHeaderInBytes = sizeOfHeader / 8;
		Label sizeInBytesLabel = new Label("🧩 Size of header in bytes: " + sizeOfHeaderInBytes);
		styleContentLabel(sizeInBytesLabel);

		Label postOrderLabel = new Label("🧩 Post Order for binary tree:");
		styleContentLabel(postOrderLabel);

		background.getChildren().addAll(headerLabel, extensionInBitTextFlow, extensionInCharLabel, sizeInBitTextFlow,
				sizeInBytesLabel, postOrderLabel);

		int counter = 0, numberOfExtraBit = 8 - sizeOfHeader % 8;
		while (counter < sizeOfHeader) {
			Label bitLabel;
			if (header.charAt(counter) == '0') {
				counter++;
				bitLabel = new Label("0 " + header.substring(counter, counter + 8));
				counter += 8;
			} else {
				counter++;
				bitLabel = new Label("1");
			}
			styleBitLabel(bitLabel);
			background.getChildren().add(bitLabel);
		}

		if (numberOfExtraBit == 8) {
			Label label = new Label("✨ The size of the header is divisible by 8; no extra bit added.");
			styleContentLabel(label);
			background.getChildren().add(label);
		} else {
			TextFlow textFlow = createTextFlow("✨ Extra bit: ", header.substring(counter, counter + numberOfExtraBit));
			background.getChildren().add(textFlow);
		}

		subScenePane.setContent(background);
		Scene scene = new Scene(subScenePane, 800, 500);
		Stage newStage = new Stage();
		newStage.setTitle("Header Information");
		newStage.setScene(scene);
		newStage.show();
	}

	private static TextFlow createTextFlow(String labelText, String contentText) {
		Label label = new Label(labelText);
		styleContentLabel(label);

		Label contentLabel = new Label(contentText);
		contentLabel.setStyle(CONTENT_STYLE);

		TextFlow textFlow = new TextFlow(label, contentLabel);
		textFlow.setStyle(
				"-fx-padding: 10px; -fx-background-color: rgba(255, 255, 255, 0.2); -fx-background-radius: 10px;");

		return textFlow;
	}

	private static void styleContentLabel(Label label) {
		label.setStyle(CONTENT_STYLE + " -fx-font-size: 18px;");
	}

	private static void styleBitLabel(Label label) {
		label.setStyle(BIT_LABEL_STYLE);
		label.setEffect(new DropShadow(5, Color.BLACK));
	}

	public static String byteToBinaryString(byte b) {
		StringBuilder binaryString = new StringBuilder();
		for (int i = 7; i >= 0; i--) {
			int bit = (b >> i) & 1;
			binaryString.append(bit);
		}
		return binaryString.toString();
	}
}