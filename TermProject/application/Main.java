/* This program runs the game called 'game box' 
 * and allows it to be played with the mouse.
 *
 * Student Name - ID:
 * Emir BÜÇKÜN - 150119024
 * Haydar Taha TUNÇ - 150119745 */

package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
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

			// Declare the urls and box images
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

			// After creating the game board, set the mouse click event
			centerPane.setOnMouseClicked(e -> {
				for (Node box : centerPane.getChildren()) {
					// Control the box for specific properties
					String boxType = getBoxType(box);
					boolean instanceCheck = box instanceof ImageView;
					boolean areaCheck = box.getBoundsInParent().contains(e.getX(), e.getY());
					boolean typeCheck = boxType == "Mirror" || boxType == "Wood";

					// If the control is successful, then take the coordinates of the clicked box
					if (instanceCheck && areaCheck && typeCheck) {
						int i = GridPane.getRowIndex(box);
						int j = GridPane.getColumnIndex(box);

						// Destroy the clicked box
						if (boxType == "Mirror")
							((ImageView) box).setImage(imageEmpty);
						else if (boxType == "Wood")
							((ImageView) box).setImage(imageMirror);
						int destroyedBoxCount = 1;

						// Set the action text for clicked box
						String actionText = "Box: " + i + "-" + j;

						// Destroy the neighboring boxes
						for (Node aroundBox : centerPane.getChildren()) {
							// Get coordinates and type of the aroundBox
							int rowIndex = GridPane.getRowIndex(aroundBox);
							int colIndex = GridPane.getColumnIndex(aroundBox);
							String aroundBoxType = getBoxType(aroundBox);

							// Validate the location of the aroundBox
							boolean topBoxCheck = rowIndex == i - 1 && colIndex == j;
							boolean rightBoxCheck = rowIndex == i && colIndex == j + 1;
							boolean bottomBoxCheck = rowIndex == i + 1 && colIndex == j;
							boolean leftBoxCheck = rowIndex == i && colIndex == j - 1;

							boolean locationControl = topBoxCheck || rightBoxCheck || bottomBoxCheck || leftBoxCheck;
							boolean typeControl = aroundBoxType == "Mirror" || aroundBoxType == "Wood";

							// If the box is in one of these places and its type is suitable for destroy
							if (locationControl && typeControl) {
								if (aroundBoxType == "Mirror")
									((ImageView) aroundBox).setImage(imageEmpty);
								else if (aroundBoxType == "Wood")
									((ImageView) aroundBox).setImage(imageMirror);
								destroyedBoxCount++;

								// Set the action text for destroyed neighboring box
								actionText += " - " + "Hit: " + rowIndex + "," + colIndex;
							}
						}

						// Get the earned point amount and add it to the score label
						int earnedPoint = getEarnedPoint(destroyedBoxCount);
						score.setText("" + (Integer.valueOf(score.getText()) + earnedPoint));

						// If the current score is bigger than the high score, set the high score
						int scoreValue = Integer.valueOf(score.getText());
						int highScoreValue = Integer.valueOf(highScore.getText().substring(12));

						if (scoreValue > highScoreValue)
							highScore.setText("High Score: " + score.getText());

						// Set the action text according to the amount of points earned
						if (earnedPoint <= 0)
							actionText += " (" + earnedPoint + " points)";
						else if (earnedPoint > 0)
							actionText += " (+" + earnedPoint + " points)";

						// Show the action text after click event
						action.setText("" + actionText);

						break;
					}
				}
			});

			// Add bottom labels to the bottomPane
			bottomPane.setLeft(action);
			bottomPane.setRight(nextLevel);

			// Set the parts of the main pane
			pane.setTop(topPane);
			pane.setCenter(centerPane);
			pane.setBottom(bottomPane);

			// Set the scene
			Scene scene = new Scene(pane, 400, 450);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the stage
			primaryStage.setTitle("Game BOX");
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Takes an image and converts it to ImageView with 39x39 sizes
	public ImageView setupImage(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(39);
		imageView.setFitWidth(39);
		return imageView;
	}

	// Gives the type of the box
	public String getBoxType(Node box) {
		String url = ((ImageView) box).getImage().getUrl();
		if (url.contains("Wall"))
			return "Wall";
		else if (url.contains("Empty"))
			return "Empty";
		else if (url.contains("Mirror"))
			return "Mirror";
		else if (url.contains("Wood"))
			return "Wood";
		else
			return "";
	}

	// Returns the amount of points earned based on the destroyed box count
	public int getEarnedPoint(int destroyedBoxCount) {
		int earnedPoint;

		switch (destroyedBoxCount) {
		case 1:
			earnedPoint = -3;
			break;
		case 2:
			earnedPoint = -1;
			break;
		case 3:
			earnedPoint = 1;
			break;
		case 4:
			earnedPoint = 2;
			break;
		case 5:
			earnedPoint = 4;
			break;
		default:
			earnedPoint = 0;
		}

		return earnedPoint;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
