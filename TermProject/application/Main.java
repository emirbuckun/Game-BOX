package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Declare panes
			BorderPane pane = new BorderPane();
			BorderPane topPane = new BorderPane();
			GridPane centerPane = new GridPane();
			BorderPane bottomPane = new BorderPane();

			// Set styles
			topPane.setPadding(new Insets(1, 5, 1, 5));
			centerPane.setStyle("-fx-background-color: gray;");
			centerPane.setAlignment(Pos.CENTER);
			bottomPane.setPadding(new Insets(1, 5, 1, 5));

			// Create top labels
			Label level = new Label("Level #1");
			Label score = new Label("0");
			Label highScore = new Label("High Score: 0");

			// Create bottom labels
			Label action = new Label("---Text---");
			Label nextLevel = new Label("Next Level >>");
			nextLevel.setVisible(false);

			// Set top labels to the topPane
			topPane.setLeft(level);
			topPane.setCenter(score);
			topPane.setRight(highScore);

			// Declare the box images
			Image imageWall = new Image("boxes/Wall Type Box.png");
			Image imageEmpty = new Image("boxes/Empty Type Box.png");
			Image imageMirror = new Image("boxes/Mirror Type Box.png");
			Image imageWood = new Image("boxes/Wood Type Box.png");

			// Create a File instance
			File file = new File("levels/level1.txt");

			// Create a Scanner for the file
			Scanner input = new Scanner(file);

			// An ArrayList for already added cells (row, column)
			ArrayList<String> addedCells = new ArrayList<String>();

			while (input.hasNext()) {
				// Get the next line -> Get row and column indexes from the line -> Add the
				// indexes to the arraylist
				String boxType = input.next();
				int rowIndex = (int) boxType.charAt(boxType.length() - 3) - '0';
				int columnIndex = (int) boxType.charAt(boxType.length() - 1) - '0';
				addedCells.add(rowIndex + "," + columnIndex);

				// Add boxes to centerPane according to the box type
				if (boxType.substring(0, 4).equals("Wood")) // if it's wood box type
					centerPane.add(setupImage(imageWood), columnIndex, rowIndex);
				else if (boxType.substring(0, 5).equals("Empty")) // if it's empty box type
					centerPane.add(setupImage(imageEmpty), columnIndex, rowIndex);
				else if (boxType.substring(0, 6).equals("Mirror")) // if it's mirror box type
					centerPane.add(setupImage(imageMirror), columnIndex, rowIndex);
			}

			// Close the Scanner
			input.close();

			// Fill the remaining boxes with the wall type box
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (!addedCells.contains(i + "," + j))
						centerPane.add(setupImage(imageWall), j, i);
				}
			}

			// Add bottom labels to the bottomPane
			bottomPane.setLeft(action);
			bottomPane.setRight(nextLevel);

			// Set the parts of the main pane
			pane.setTop(topPane);
			pane.setCenter(centerPane);
			pane.setBottom(bottomPane);

			// Set the scene
			Scene scene = new Scene(pane, 300, 350);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the stage
			primaryStage.setTitle("Game BOX");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This method takes an image and convert it to ImageView with 30x30 sizes
	public ImageView setupImage(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		return imageView;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
