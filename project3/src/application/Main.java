package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {
	public static File file;
	static ComboBox<String> scourseText = new ComboBox<String>();
	static ComboBox<String> filterText = new ComboBox<String>();
	static ComboBox<String> targetText = new ComboBox<String>();
	static ToggleButton click = new ToggleButton("Click in map");
	static ToggleButton combo = new ToggleButton("Combo Box");
	static int numOfPointChoice = 0;
	static Pane pane2 = new Pane();
	private Alert error = new Alert(AlertType.ERROR);
	ObservableList<PathTable> data = FXCollections.observableArrayList();
	static ArrayList<Vertex> Capital = new ArrayList<>();

//...............................................................................................................................................................................................................

	@Override
	public void start(Stage primaryStage) {
		Image m = new Image("World-Map.jpg");
		ImageView image = new ImageView(m);
		image.setFitHeight(519);
		image.setFitWidth(1002);
		pane2.getChildren().add(image);

		Label title = new Label("World Map Dijkstra");
		title.setFont(Font.font("Segoe Print", FontWeight.BOLD, FontPosture.REGULAR, 30));
		title.setPadding(new Insets(15));

		TranslateTransition transition = new TranslateTransition(Duration.seconds(1), title);
		transition.setFromY(-10);
		transition.setToY(10);
		transition.setCycleCount(TranslateTransition.INDEFINITE);
		transition.setAutoReverse(true);
		transition.play();

		file = new File("C:\\Users\\Dell\\Desktop\\Capital.txt");
		readFile(file);

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		pane.setTop(title);
		BorderPane.setAlignment(title, Pos.CENTER);

		Label choose = new Label("Choose city by :");
		choose.setPadding(new Insets(15));
		ToggleGroup tg = new ToggleGroup();

		click.setToggleGroup(tg);
		combo.setToggleGroup(tg);
		icons(click);
		icons(combo);

		click.setOnAction(e -> {
			click.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n"
					+ "-fx-font-family: Segoe Print;\n" + "-fx-font-weight: Bold;\n" + " -fx-text-fill: #ff6800;\n"
					+ "-fx-background-color: #d8d9e0;\n" + "-fx-border-color: #d8d9e0;\n" + "-fx-border-width:  3.5;"
					+ "-fx-background-radius: 25 25 25 25");
			combo.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n"
					+ "-fx-font-family: Segoe Print;\n" + "-fx-font-weight: Bold;\n"
					+ "-fx-background-color: #f6f6f6;\n" + "-fx-border-color: #d8d9e0;\n" + "-fx-border-width:  3.5;"
					+ "-fx-background-radius: 25 25 25 25");

		});

		combo.setOnAction(e -> {
			combo.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n"
					+ "-fx-font-family: Segoe Print;\n" + "-fx-font-weight: Bold;\n" + " -fx-text-fill: #ff6800;\n"
					+ "-fx-background-color: #d8d9e0;\n" + "-fx-border-color: #d8d9e0;\n" + "-fx-border-width:  3.5;"
					+ "-fx-background-radius: 25 25 25 25");
			click.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n"
					+ "-fx-font-family: Segoe Print;\n" + "-fx-font-weight: Bold;\n"
					+ "-fx-background-color: #f6f6f6;\n" + "-fx-border-color: #d8d9e0;\n" + "-fx-border-width:  3.5;"
					+ "-fx-background-radius: 25 25 25 25");

		});

		scourseText.setOnAction(e -> {
			if (combo.isSelected()) {
				for (int i = 0; i < Capital.size(); i++) {
					if (Capital.get(i).getCapital().getName()
							.equals(scourseText.getSelectionModel().getSelectedItem())) {
						ImageView vi0 = new ImageView(new Image("location-pin.png"));
						vi0.setFitHeight(16);
						vi0.setFitWidth(16);
						Capital.get(i).getCapital().getRadioButton().setGraphic(vi0);
						Capital.get(i).getCapital().getRadioButton().setSelected(true);
						numOfPointChoice += 1;
						if (numOfPointChoice == 2) {
							lock();
						}
						break;
					}
				}
			}
		});

		targetText.setOnAction(e -> {
			if (combo.isSelected()) {
				for (int i = 0; i < Capital.size(); i++) {
					if (Capital.get(i).getCapital().getName()
							.equals(targetText.getSelectionModel().getSelectedItem())) {
						ImageView vi0 = new ImageView(new Image("location-pin (2).png"));
						vi0.setFitHeight(16);
						vi0.setFitWidth(16);
						Capital.get(i).getCapital().getRadioButton().setGraphic(vi0);
						Capital.get(i).getCapital().getRadioButton().setSelected(true);
						numOfPointChoice += 1;
						if (numOfPointChoice == 2) {
							lock();
						}
						break;
					}
				}
			}
		});

		HBox hx = new HBox(10, click, combo);
		hx.setAlignment(Pos.CENTER);
		hx.setPadding(new Insets(3));

		IconedTextFieled(choose, hx);
		HBox h = new HBox(choose, hx);
		h.setAlignment(Pos.CENTER);

		Label scourse = new Label("Sourse :");
		scourse.setPadding(new Insets(7));
		scourseText.setMinWidth(150);
		for (int i = 0; i < Capital.size(); i++) {
			scourseText.getItems().add(Capital.get(i).getCapital().getName());
		}

		IconedTextFieled(scourse, scourseText);
		HBox h1 = new HBox(scourse, scourseText);
		h1.setAlignment(Pos.CENTER);

		Label target = new Label("Target :");
		target.setPadding(new Insets(7));
		for (int i = 0; i < Capital.size(); i++) {
			targetText.getItems().add(Capital.get(i).getCapital().getName());
		}
		targetText.setMinWidth(150);
		IconedTextFieled(target, targetText);

		HBox h2 = new HBox(target, targetText);
		h2.setAlignment(Pos.CENTER);

		// Filter ComboBox
		Label filterLabel = new Label("Filter :");
		filterLabel.setPadding(new Insets(7));
		filterText.setMinWidth(150);

		filterText.setMinWidth(150);
		filterText.getItems().addAll("Distance", "Time", "Cost");
		IconedTextFieled(filterLabel, filterText);

		HBox h3WithFilter = new HBox(filterLabel, filterText);
		h3WithFilter.setAlignment(Pos.CENTER);

		ImageView imageView = new ImageView(new Image("run.png"));
		imageView.setFitWidth(20); // تحديد عرض الصورة
		imageView.setFitHeight(20); // تحديد ارتفاع الصورة
		imageView.setPreserveRatio(true); // الحفاظ على نسبة العرض إلى الارتفاع
		Button run = new Button("Run", imageView);
		run.setOnMouseEntered(e -> run.setStyle("-fx-background-color: #ff8c00;"));
		run.setOnMouseExited(e -> run.setStyle("-fx-background-color: #ff6800;"));
		run.setTooltip(new Tooltip("Run the algorithm"));

		ImageView imageView2 = new ImageView(new Image("reset.png"));
		imageView2.setFitWidth(20); // تحديد عرض الصورة
		imageView2.setFitHeight(20); // تحديد ارتفاع الصورة
		imageView2.setPreserveRatio(true); // الحفاظ على نسبة العرض إلى الارتفاع
		Button reset = new Button("Reset", imageView2);
		reset.setOnMouseEntered(e -> reset.setStyle("-fx-background-color: #ff8c00;"));
		reset.setOnMouseExited(e -> reset.setStyle("-fx-background-color: #ff6800;"));
		reset.setTooltip(new Tooltip("Reset the selection"));

		HBox butBox = new HBox(20, run, reset);
		butBox.setAlignment(Pos.CENTER);
		icons(reset);
		icons(run);
		butoonEffect(reset);
		butoonEffect(run);

		// Label for path
		Label pathLabel = new Label("Shortest Path :");
		pathLabel.setPadding(new Insets(7));

		// Create a TextArea for displaying paths
		TextArea pathText = new TextArea();
		pathText.setEditable(false);
		pathText.setWrapText(true);
		pathText.setPrefWidth(300); // Set your desired width
		pathText.setPrefHeight(100); // Set your desired height

		// Create a layout for the path display
		HBox hPath = new HBox(pathLabel, pathText);
		hPath.setAlignment(Pos.CENTER);

		// Distance label and field
		Label distance = new Label("Distance :");
		distance.setPadding(new Insets(7));
		TextField distanceText = new TextField();
		IconedTextFieled(distance, distanceText);
		HBox h5 = new HBox(distance, distanceText);
		h5.setAlignment(Pos.CENTER);

		// Cost and Time fields
		Label costLabel = new Label("Cost:");
		costLabel.setPadding(new Insets(7));
		TextField costText = new TextField();
		costText.setEditable(false);
		costText.setMinWidth(100);

		Label timeLabel = new Label("Time:");
		timeLabel.setPadding(new Insets(7));
		TextField timeText = new TextField();
		timeText.setEditable(false);
		timeText.setMinWidth(100);

		IconedTextFieled(costLabel, costText);
		IconedTextFieled(timeLabel, timeText);

		HBox hCost = new HBox(costLabel, costText);
		hCost.setAlignment(Pos.CENTER);
		HBox hTime = new HBox(timeLabel, timeText);
		hTime.setAlignment(Pos.CENTER);

		VBox v = new VBox(30, h, h1, h2, h3WithFilter, butBox, hPath, h5, hCost, hTime);
		v.setAlignment(Pos.TOP_CENTER);
		v.setPadding(new Insets(1));
		icons(v);

//		VBox v1 = new VBox(30, new HBox();
//		v1.setAlignment(Pos.CENTER);
//		v1.setPadding(new Insets(10));
//		icons(v1);

		VBox mix = new VBox(5, v);
		mix.setAlignment(Pos.TOP_RIGHT);

		VBox Vmap = new VBox(pane2);
		Vmap.setAlignment(Pos.CENTER);

		HBox mainBox = new HBox(20, Vmap, mix);
		mainBox.setAlignment(Pos.TOP_CENTER);

		pane.setCenter(mainBox);
		// Set the background color of the pane
		pane.setStyle("-fx-background-color: #D3D3D3;");

		run.setOnAction(e -> {
			Vertex vertx1 = null;
			Vertex vertx2 = null;
			String s1 = scourseText.getValue();
			String s2 = targetText.getValue();
			String selectedCriterion = filterText.getValue(); // Get the selected criterion

			for (int i = 0; i < Capital.size(); i++) {
				if (Capital.get(i).getCapital().getName().equals(s1)) {
					vertx1 = Capital.get(i);
				}
				if (Capital.get(i).getCapital().getName().equals(s2)) {
					vertx2 = Capital.get(i);
				}
			}

			if (vertx1 != null && vertx2 != null) {
				Vertex result = null;

				// Determine the Dijkstra method based on the selected criterion
				if ("Cost".equals(selectedCriterion)) {
					result = DijkstraCost(vertx1, vertx2);
				} else if ("Distance".equals(selectedCriterion)) {
					result = DijkstraDistance(vertx1, vertx2);
				} else if ("Time".equals(selectedCriterion)) {
					result = DijkstraTime(vertx1, vertx2);
				}

				// Check if a path was found
				if (result == null) {
//					error.setContentText("No path found between " + s1 + " and " + s2);
//					error.show();

					// Set distance, cost, and time to 0 when no path is found
					distanceText.setText("0 km");
					costText.setText("0 $");
					timeText.setText("0 min");

					// Clear the path text
					pathText.setText("No path found between " + s1 + " and " + s2);

					return; // Exit early if no path
				}

				// Proceed with drawing the line and updating the text fields
				double[] results = drowLine(result);

				// Update the text fields with results for all criteria
				distanceText.setText(String.valueOf(results[0]) + " km");
				costText.setText(String.valueOf(results[1]) + " $");
				timeText.setText(String.valueOf(results[2]) + " min");

				// Prepare the output for the TextArea
				String pathString = printPath(result, selectedCriterion);
				StringBuilder pathOutput = new StringBuilder();
				pathOutput.append("Shortest Path: ").append(pathString).append("\n");
				pathText.setText(pathOutput.toString());
			} else {
				error.setContentText("Please select valid source and target cities.");
				error.show();
			}
		});

		reset.setOnAction(l -> {
			// Clear the map pane and selected values
			pane2.getChildren().clear();
			targetText.getSelectionModel().select(null);
			scourseText.getSelectionModel().select(null);
			distanceText.setText("");
			costText.setText(""); // Clear the cost field
			timeText.setText(""); // Clear the time field
			filterText.getSelectionModel().select(null); // Reset the filter selection

			// Clear data lists
			data.clear();
			data.clear();
			numOfPointChoice = 0;

			// Re-add the map image
			pane2.getChildren().add(image);
			Image pinImage = new Image("location-pin (1).png"); // Changed variable name for clarity
			for (Vertex country : Capital) {
				// Reset the country radio buttons
				ImageView pinView = new ImageView(pinImage);
				pinView.setFitHeight(17);
				pinView.setFitWidth(16);
				country.getCapital().getRadioButton().setGraphic(pinView);
				country.getCapital().getRadioButton().setSelected(false);
				free(); // Enable all country radio buttons
			}

			// Reset visited and previous states for all countries
			for (Vertex country : Capital) {
				country.visited = false;
				country.previous = null;
			}

			// Re-add points to the map
			addPoint();
		});

		addPoint();

		Scene scene = new Scene(pane, 1535, 800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

//...............................................................................................................................................................................................................

	private double[] drowLine(Vertex Destination) {
		if (Destination == null) {
			error.setContentText("No path");
			error.show();
			return new double[] { 0, 0, 0 }; // Return zero cost, distance, and time
		} else {
			ArrayList<Vertex> p = new ArrayList<>();
			double totalCost = 0;
			double totalDistance = 0;
			int totalTime = 0;

			for (Vertex v = Destination; v != null; v = v.previous) {
				p.add(v);
			}
			Collections.reverse((List<?>) p);

			if (p.size() < 2) {
				error.setContentText("No path");
				error.show();
				return new double[] { 0, 0, 0 }; // No valid path
			}

			for (int i = 1; i < p.size(); i++) {
				Vertex u = p.get(i - 1);
				Vertex v = p.get(i);

				// Assuming you have access to the edges between u and v
				for (Edges edge : u.getEdges()) {
					if (edge.destination.equals(v)) {
						totalCost += edge.getCost(); // Add cost
						totalTime += edge.getTime(); // Add time
						totalDistance += Distance(u, v); // Add distance
						break;
					}
				}

				Line line = new Line(u.capital.getX(), u.capital.getY(), v.capital.getX(), v.capital.getY());
				pane2.getChildren().add(line);
				line.setStrokeWidth(2);
				line.setStroke(Color.RED);

				Polygon arrowHead = new Polygon();
				double arrowHeadSize = 10;
				double angle = Math.atan2(v.capital.getY() - u.capital.getY(), v.capital.getX() - u.capital.getX());

				arrowHead.getPoints().addAll(v.capital.getX(), v.capital.getY(),
						v.capital.getX() - arrowHeadSize * Math.cos(angle - Math.toRadians(30)),
						v.capital.getY() - arrowHeadSize * Math.sin(angle - Math.toRadians(30)),
						v.capital.getX() - arrowHeadSize * Math.cos(angle + Math.toRadians(30)),
						v.capital.getY() - arrowHeadSize * Math.sin(angle + Math.toRadians(30)));

				arrowHead.setFill(Color.RED);
				pane2.getChildren().add(arrowHead);
			}
			return new double[] { totalDistance, totalCost, totalTime }; // Return total distance, cost, and time
		}
	}

//...............................................................................................................................................................................................................

	private void addPoint() {
		for (int i = 0; i < Capital.size(); i++) {
			RadioButton r = Capital.get(i).getCapital().getRadioButton();
			r.setLayoutX(Capital.get(i).getCapital().getX());
			r.setLayoutY(Capital.get(i).getCapital().getY());
			pane2.getChildren().add(r);
		}

	}

//...............................................................................................................................................................................................................

	private void IconedTextFieled(javafx.scene.Node l, javafx.scene.Node t) {
		l.setStyle("-fx-border-color: #d8d9e0;" + "-fx-font-size: 14;\n" + "-fx-border-width: 1;"
				+ "-fx-border-radius: 50;" + "-fx-font-weight: Bold;\n" + "-fx-background-color:#d8d9e0;"
				+ "-fx-background-radius: 50 0 0 50");

		t.setStyle("-fx-border-radius: 0 50 50 0;\n" + "-fx-font-size: 14;\n" + "-fx-font-family: Segoe Print;\n"
				+ "-fx-font-weight: Bold;\n" + "-fx-background-color: #f6f6f6;\n" + "-fx-border-color: #d8d9e0;\n"
				+ "-fx-border-width:  3.5;" + "-fx-text-fill: #ff6800;" + "-fx-background-radius: 0 50 50 0");
	}

	private void icons(Node l) {
		l.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n" + "-fx-font-family: Segoe Print;\n"
				+ "-fx-font-weight: Bold;\n" + "-fx-background-color: transparent;\n" + "-fx-border-color: #d8d9e0;\n"
				+ "-fx-border-width:  3.5;" + "-fx-background-color: #f6f6f6;\n"
				+ "-fx-background-radius: 25 25 25 25");
	}

	private void butoonEffect(Node b) {
		b.setOnMouseMoved(e -> {
			b.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n" + "-fx-font-family: Segoe Print;\n"
					+ "-fx-font-weight: Bold;\n" + " -fx-text-fill: #ff6800;\n" + "-fx-background-color: #d8d9e0;\n"
					+ "-fx-border-color: #d8d9e0;\n" + "-fx-border-width:  3.5;"
					+ "-fx-background-radius: 25 25 25 25");
		});

		b.setOnMouseExited(e -> {
			b.setStyle("-fx-border-radius: 25 25 25 25;\n" + "-fx-font-size: 14;\n" + "-fx-font-family: Segoe Print;\n"
					+ "-fx-font-weight: Bold;\n" + "-fx-background-color: #f6f6f6;\n" + "-fx-border-color: #d8d9e0;\n"
					+ "-fx-border-width:  3.5;" + "-fx-background-radius: 25 25 25 25");
		});
	}

//...............................................................................................................................................................................................................

	public static void lock() {
		try {
			for (int i = 0; i < Capital.size(); i++) {
				Capital.get(i).getCapital().getRadioButton().setDisable(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//...............................................................................................................................................................................................................

	public static void free() {
		try {
			for (int i = 0; i < Capital.size(); i++) {
				Capital.get(i).getCapital().getRadioButton().setDisable(false);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//...............................................................................................................................................................................................................

	public Vertex DijkstraDistance(Vertex Source, Vertex Destination) {
		// Step 1: Initialize all vertices
		initializeVertices();

		// Step 2: Set the distance of the source vertex to 0
		Source.distance = 0;

		// Step 3: If the source and destination are the same, return immediately
		if (Source == Destination) {
			return Source;
		}

		// Step 4: Run Dijkstra's algorithm
		while (true) {
			// Step 4.1: Find the vertex with the smallest unknown distance
			Vertex v = findSmallestUnknownDistanceVertex();

			// Step 4.2: If no vertex is found, exit the loop
			if (v == null) {
				break;
			}

			// Step 4.3: Mark the vertex as visited
			v.visited = true;

			// Step 4.4: If the destination is reached, print the path and return the vertex
			if (v.equals(Destination)) {
				printPath(Destination, null);
				return v;
			}

			// Step 4.5: Relax edges for each adjacent vertex
			relaxEdges(v);
		}

		// Step 5: If no path is found, return null
		return null;
	}

//..................................................................................................................

	// Helper method to relax edges for a given vertex
	private void relaxEdges(Vertex v) {
		for (Edges edge : v.getEdges()) {
			Vertex w = edge.destination; // Adjacent vertex

			// If the adjacent vertex is not visited, relax the edge
			if (!w.visited) {
				double newDist = v.distance + Distance(v, w); // Calculate new distance
				if (newDist < w.distance) { // If the new distance is shorter
					w.distance = newDist; // Update the distance
					w.previous = v; // Update the previous vertex
				}
			}
		}
	}

//...............................................................................................................................................................................................................

	public Vertex DijkstraCost(Vertex source, Vertex destination) {
		// Step 1: Initialize all vertices
		initializeVertices();

		// Step 2: Set the distance of the source vertex to 0
		source.distance = 0;

		// Step 3: If the source and destination are the same, return immediately
		if (source == destination) {
			return source;
		}

		// Step 4: Run Dijkstra's algorithm
		while (true) {
			// Step 4.1: Find the vertex with the smallest unknown distance
			Vertex v = findSmallestUnknownDistanceVertex();

			// Step 4.2: If no vertex is found, exit the loop
			if (v == null) {
				break;
			}

			// Step 4.3: Mark the vertex as visited
			v.visited = true;

			// Step 4.4: If the destination is reached, print the path and return the vertex
			if (v.equals(destination)) {
				printPath(destination, "Cost");
				return v;
			}

			// Step 4.5: Relax edges for each adjacent vertex based on cost
			relaxEdgesWithCost(v);
		}

		// Step 5: If no path is found, return null
		return null;
	}

//..................................................................................................................

	// Helper method to relax edges based on cost
	private void relaxEdgesWithCost(Vertex v) {
		for (Edges edge : v.getEdges()) {
			Vertex w = edge.destination; // Adjacent vertex

			// If the adjacent vertex is not visited, relax the edge
			if (!w.visited) {
				double newCost = v.distance + edge.getCost(); // Calculate new cost
				if (newCost < w.distance) { // If the new cost is cheaper
					w.distance = newCost; // Update the distance (cost)
					w.previous = v; // Update the previous vertex
				}
			}
		}
	}

//...............................................................................................................................................................................................................

	public Vertex DijkstraTime(Vertex source, Vertex destination) {
		// Step 1: Initialize all vertices
		initializeVertices();

		// Step 2: Set the distance of the source vertex to 0
		source.distance = 0;

		// Step 3: If the source and destination are the same, return immediately
		if (source == destination) {
			return source;
		}

		// Step 4: Run Dijkstra's algorithm
		while (true) {
			// Step 4.1: Find the vertex with the smallest unknown distance
			Vertex v = findSmallestUnknownDistanceVertex();

			// Step 4.2: If no vertex is found, exit the loop
			if (v == null) {
				break;
			}

			// Step 4.3: Mark the vertex as visited
			v.visited = true;

			// Step 4.4: If the destination is reached, print the path and return the vertex
			if (v.equals(destination)) {
				printPath(destination, "Time");
				return v;
			}

			// Step 4.5: Relax edges for each adjacent vertex based on time
			relaxEdgesWithTime(v);
		}

		// Step 5: If no path is found, return null
		return null;
	}

//..................................................................................................................

	// Helper method to relax edges based on time
	private void relaxEdgesWithTime(Vertex v) {
		for (Edges edge : v.getEdges()) {
			Vertex w = edge.destination; // Adjacent vertex

			// If the adjacent vertex is not visited, relax the edge
			if (!w.visited) {
				double newTime = v.distance + edge.getTime(); // Calculate new time
				if (newTime < w.distance) { // If the new time is quicker
					w.distance = newTime; // Update the distance (time)
					w.previous = v; // Update the previous vertex
				}
			}
		}
	}

//......................................................................................................................................

	// Helper method to find the vertex with the smallest unknown distance
	private Vertex findSmallestUnknownDistanceVertex() {
		Vertex smallestVertex = null;
		double minDistance = Double.MAX_VALUE;

		for (Vertex vertex : Capital) {
			if (!vertex.visited && vertex.distance < minDistance) {
				minDistance = vertex.distance;
				smallestVertex = vertex;
			}
		}

		return smallestVertex;
	}

	// ...................................................................................................................................

	// Helper method to initialize all vertices
	private void initializeVertices() {
		for (Vertex v : Capital) {
			v.distance = Double.MAX_VALUE; // Set initial distance to infinity
			v.previous = null; // No previous vertex
			v.visited = false; // Mark as unvisited
		}
	}

//...............................................................................................................................................................................................................

	// Method to print the path from the source to the destination
	public String printPath(Vertex v, String selectedCriterion) {
		if (v == null) {
			return "No path found.";
		}

		StringBuilder pathBuilder = new StringBuilder();
		ArrayList<Vertex> path = new ArrayList<>();

		// Build the path by traversing from the destination to source
		for (Vertex curr = v; curr != null; curr = curr.previous) {
			path.add(curr);
		}
		Collections.reverse((List<?>) path); // Reverse to get the correct order

		// Iterate through the path to construct the output string
		for (int i = 0; i < path.size(); i++) {
			Vertex curr = path.get(i);
			if (i > 0) {
				Vertex prev = path.get(i - 1);
				double value = 0;

				// Determine the value to display based on the selected criterion
				if ("Distance".equals(selectedCriterion)) {
					value = Distance(prev, curr);
					pathBuilder.append("From ").append(prev.getCapital().getName()).append(" to ")
							.append(curr.getCapital().getName()).append(" with Distance ")
							.append(String.format("%.2f", value)).append(" km\n");
				} else if ("Cost".equals(selectedCriterion)) {
					for (Edges edge : prev.getEdges()) {
						if (edge.destination.equals(curr)) {
							value = edge.getCost();
							pathBuilder.append("From ").append(prev.getCapital().getName()).append(" to ")
									.append(curr.getCapital().getName()).append(" with Cost ")
									.append(String.format("%.2f", value)).append(" $\n");
							break; // Exit once we find the edge
						}
					}
				} else if ("Time".equals(selectedCriterion)) {
					for (Edges edge : prev.getEdges()) {
						if (edge.destination.equals(curr)) {
							value = edge.getTime();
							pathBuilder.append("From ").append(prev.getCapital().getName()).append(" to ")
									.append(curr.getCapital().getName()).append(" with Time ")
									.append(String.format("%d", (int) value)).append(" min\n");
							break; // Exit once we find the edge
						}
					}
				}
			}
		}

		return pathBuilder.toString();
	}

//...............................................................................................................................................................................................................

	public void readFile(File file) {
		try {
			Scanner sc = new Scanner(file);

			String[] firstLine = sc.nextLine().split(",");
			int numCapital = Integer.parseInt(firstLine[0].trim());
			int numEdges = Integer.parseInt(firstLine[1].trim());

			int count = 0;
			int num = 0;
			while (count < numCapital) {
				String line = sc.nextLine().trim();
				String[] parts = line.split(",\\s*");

				if (parts.length != 3) {
					System.out.println("Invalid country format: " + line);
					continue;
				}

				String capitalName = parts[0];
				double latitude = 0;
				double longitude = 0;

				try {
					latitude = Double.parseDouble(parts[1]);
					longitude = Double.parseDouble(parts[2]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid latitude or longitude: " + line);
					continue;
				}

				Vertex ver = new Vertex(new Capital(capitalName, latitude, longitude), num++);
				Capital.add(ver);
				count++;
			}

			count = 0;
			while (count < numEdges) {
				String line = sc.nextLine().trim();
				String[] tokens = line.split(",\\s*");

				if (tokens.length != 4) {
					System.out.println("Invalid edge format: " + line);
					continue;
				}

				String fromCapital = tokens[0];
				String toCapital = tokens[1];
				double cost = Double.parseDouble(tokens[2].trim());
				int time = Integer.parseInt(tokens[3].trim());

				Vertex fromVertex = null;
				Vertex toVertex = null;

				for (Vertex v : Capital) {
					if (v.getCapital().getName().equalsIgnoreCase(fromCapital)) {
						fromVertex = v;
					}
					if (v.getCapital().getName().equalsIgnoreCase(toCapital)) {
						toVertex = v;
					}
				}

				if (fromVertex != null && toVertex != null) {
					fromVertex.edges.add(new Edges(fromVertex, toVertex, cost, time));
				} else {
					System.out.println("Edge references unknown country: " + line);
				}

				count++;
			}

			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error while reading file: " + e.getMessage());
		}
	}

//...............................................................................................................................................................................................................

	public double Distance(Vertex a, Vertex b) {

		final int EARTH_RADIUS = 6378;
		double lat1Rad = Math.toRadians(a.getCapital().getLatitude());
		double lat2Rad = Math.toRadians(b.getCapital().getLatitude());
		double deltaLat = Math.toRadians(b.getCapital().getLatitude() - a.getCapital().getLatitude());
		double deltaLon = Math.toRadians(b.getCapital().getLongitude() - a.getCapital().getLongitude());

		double dis = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(dis), Math.sqrt(1 - dis));

		return EARTH_RADIUS * c;

	}

//...............................................................................................................................................................................................................

	public static void main(String[] args) {
		launch(args);
	}
}