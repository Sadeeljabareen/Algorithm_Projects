package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Capital {
	static final double WIDTH = 1000;
	static final double HEIGHT = 500;
	String name;
	double longitude;
	double latitude;
	RadioButton radioButton;
	ToggleGroup group;

	public Capital() {
		// Default constructor
	}

	public Capital(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		setupRadioButton();
	}

	public Capital(String line) {
		String[] arr = line.split(",");
		this.name = arr[0];
		this.latitude = Double.parseDouble(arr[1]);
		this.longitude = Double.parseDouble(arr[2]);
		setupRadioButton();
	}

	private void setupRadioButton() {
		// Initialize the RadioButton
		radioButton = new RadioButton();
		radioButton.setToggleGroup(group); // Add the RadioButton to a toggle group
		radioButton.setPadding(new Insets(-5)); // Adjust padding to reduce size

		// Create an ImageView for the pin image
		ImageView imageView = new ImageView(new Image("location-pin (1).png"));
		imageView.setFitHeight(8); // Set image height to 8px (smaller size)
		imageView.setFitWidth(8); // Set image width to 8px (smaller size)

		// Create a Label for the capital name
		Label nameLabel = new Label(this.getName());
		nameLabel.setStyle("-fx-font-size: 6px; -fx-text-fill: white;"); // Smaller font size (6px) and white text color
		nameLabel.setMinWidth(Region.USE_PREF_SIZE); // Ensure the label is wide enough for the text
		nameLabel.setAlignment(Pos.CENTER); // Center the text

		// Create a VBox to stack the image and label vertically
		VBox graphicBox = new VBox(0.5); // Set smaller spacing between image and label
		graphicBox.setAlignment(Pos.CENTER); // Center the elements
		graphicBox.getChildren().addAll(imageView, nameLabel); // Add image and label to the VBox

		// Set the VBox as the graphic of the RadioButton
		radioButton.setGraphic(graphicBox);

		// Add a Tooltip to the RadioButton
		Tooltip tooltip = new Tooltip(this.getName());
		tooltip.setFont(new Font(10)); // Set tooltip font size to 10px
		tooltip.setStyle("-fx-background-color: grey;"); // Set tooltip background color to grey
		radioButton.setTooltip(tooltip);

		// Set the action for the RadioButton
		radioButton.setOnAction(event -> handleRadioButtonAction()); // Handle button click event
	}

	private void handleRadioButtonAction() {
		// Change icon based on number of selections
		String iconName = switch (Main.numOfPointChoice) {
		case 0 -> "location-pin.png";
		case 1 -> "location-pin (2).png";
		default -> "location-pin (1).png"; // Default icon
		};
		updateRadioButtonIcon(iconName);
		Main.numOfPointChoice++;

		if (Main.click.isSelected()) {
			if (Main.numOfPointChoice == 1) {
				Main.scourseText.getSelectionModel().select(getName());
			} else if (Main.numOfPointChoice == 2) {
				Main.targetText.getSelectionModel().select(getName());
			}
		}

		if (Main.numOfPointChoice == 2) {
			Main.lock();
		}
	}

	private void updateRadioButtonIcon(String iconName) {
		// Update the image in the RadioButton's graphic
		VBox graphicBox = (VBox) radioButton.getGraphic();
		ImageView imageView = (ImageView) graphicBox.getChildren().get(0);
		imageView.setImage(new Image(iconName));
	}

	public String getName() {
		return name;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public RadioButton getRadioButton() {
		return radioButton;
	}

	public double getX() {
		return ((longitude + 180) / 360 * WIDTH);
	}

	public double getY() {
		return (HEIGHT - (latitude + 90) / 180 * HEIGHT);
	}
}