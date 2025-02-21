package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) {
		// Configure SceneManager with the primary stage.
		ButtonAction.setPrimaryStage(primaryStage);

		// Set the main scene using SceneManager.
		ButtonAction.setMainScene();

		// Configure the primary stage.
		primaryStage.setTitle("Compression and Decompression");
		primaryStage.setWidth(1200); // Set width to 800
		primaryStage.setHeight(700); // Set height to 600

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}