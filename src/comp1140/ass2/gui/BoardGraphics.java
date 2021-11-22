package comp1140.ass2.gui;

import comp1140.ass2.Piece;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BoardGraphics {
    /**
     * Draw the background to the board
     * @param group The group to add the board to
     * @param boardText The text to display above the board
     */
    public static void drawBoard(Group group, String boardText) {
        // Create group to contain board
        Group boardGroup = new Group();
        boardGroup.prefHeight(600);
        boardGroup.prefWidth(600);
        // Add board background
        Rectangle board = new Rectangle();
        board.setHeight(600);
        board.setWidth(600);
        board.setFill(Color.rgb(202, 195, 159));
        board.setArcHeight(20.0);
        board.setArcWidth(20.0);
        boardGroup.getChildren().add(board);
        // Add spots to board
        for (int i = 0; i < 49; i++) {
            Rectangle spot = new Rectangle();
            spot.setHeight(64);
            spot.setWidth(64);
            spot.setY(getPos(i % 7));
            spot.setX(getPos(i / 7));
            spot.setFill(Color.rgb(186, 176, 124));
            spot.setArcHeight(17.0);
            spot.setArcWidth(17.0);
            boardGroup.getChildren().add(spot);
        }
        group.getChildren().add(boardGroup);
        // Display instructions
        Text instruction = new Text(boardText);
        instruction.setY(-10);
        instruction.setX(20);
        instruction.setFont(Font.font(15));
        group.getChildren().add(instruction);
    }

    public static void createCoverScreen(Group group, EventHandler<MouseEvent> startPur, EventHandler<MouseEvent> startContra) {
        Group coverScreen = new Group();

        Rectangle background = new Rectangle();
        background.setWidth(933);
        background.setHeight(700);
        background.setFill(Color.rgb(202, 195, 159));
        coverScreen.getChildren().add(background);

        Rectangle foreground = new Rectangle();
        foreground.setWidth(721);
        foreground.setHeight(505);
        foreground.setX(106);
        foreground.setY(97);
        foreground.setFill(Color.rgb(186, 176, 124));
        foreground.setArcHeight(60.0);
        foreground.setArcWidth(60.0);
        coverScreen.getChildren().add(foreground);

        try {
            FileInputStream logoInputStream = new FileInputStream("images/logo.png");
            Image logoImage = new Image(logoInputStream);
            ImageView logo = new ImageView(logoImage);
            logo.setX(361);
            logo.setY(137);
            coverScreen.getChildren().add(logo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Text title = new Text("Cublino");
        title.setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 55.0));
        title.setFill(Color.rgb(255,255,255));
        title.setX(467 - title.getLayoutBounds().getWidth() / 2);
        title.setY(340);
        coverScreen.getChildren().add(title);

        Text subTitle = new Text("The classic duo dice game for all ages!");
        subTitle.setFont(Font.font(25.0));
        subTitle.setFill(Color.rgb(255,255,255));
        subTitle.setX(467 - subTitle.getLayoutBounds().getWidth() / 2);
        subTitle.setY(370);
        coverScreen.getChildren().add(subTitle);

        Text chooseText = new Text("Choose Game Mode:");
        chooseText.setFont(Font.font(30.0));
        chooseText.setFill(Color.rgb(255,255,255));
        chooseText.setX(200);
        chooseText.setY(480);
        coverScreen.getChildren().add(chooseText);

        Group purButton = createCoverScreenButton("Pur");
        purButton.setLayoutX(250);
        purButton.setLayoutY(500);
        purButton.setOnMouseClicked(startPur);
        coverScreen.getChildren().add(purButton);
        Group contraButton = createCoverScreenButton("Contra");
        contraButton.setLayoutX(550);
        contraButton.setLayoutY(500);
        contraButton.setOnMouseClicked(startContra);
        coverScreen.getChildren().add(contraButton);

        group.getChildren().add(coverScreen);
    }

    private static Group createCoverScreenButton(String text) {
        Group button = new Group();
        button.getStyleClass().add("button");
        Rectangle buttonBackground = new Rectangle();
        buttonBackground.getStyleClass().add("brown-button");
        buttonBackground.setWidth(200);
        buttonBackground.setHeight(50);
        buttonBackground.setArcHeight(50.0);
        buttonBackground.setArcWidth(50.0);

        button.getChildren().add(buttonBackground);

        Text buttonText = new Text(text);
        buttonText.setFont(Font.font(22.0));
        buttonText.setFill(Color.rgb(255,255,255));
        buttonText.setX(100 - buttonText.getLayoutBounds().getWidth() / 2);
        buttonText.setY(32);
        button.getChildren().add(buttonText);

        return button;
    }

    /**
     *
     * @param piece The piece to find
     * @param offsetX The face to be found for a particular x direction, either -1,0 or 1
     * @param offsetY The face to be found for a particular y direction, either -1,0 or 1
     * @return A string in the form of "white6" with the colour and number facing up
     */
    public static String getPieceFileName(Piece piece, int offsetX, int offsetY) {
        // Choose colour based on upper or lower case
        String pieceType = piece.getColour() == Player.WHITE ? "white" : "black";

        // This assumes that the offset is not diagonal
        if (offsetY == 1) return pieceType + piece.getNumNorth();
        if (offsetY == -1) return pieceType + (7 - piece.getNumNorth());
        if (offsetX == 1) return pieceType + (7 - piece.getNumEast());
        if (offsetX == -1) return pieceType + piece.getNumEast();
        // If nothing else matches, show the number on the top of the dice
        return pieceType + piece.getNumUp();
    }

    /**
     *
     * @param ordering The position in a particular row or column
     * @return The pixel value from the origin
     */
    public static double getPos(int ordering) {
        return 28 + ordering * 80;
    }


    /**
     * Split a placement into its pieces
     * @param placement The game state
     * @return An array of the piece placements
     */
    public static String[] splitIntoPieces(String placement) {
        int numberOfPieces = (placement.length() - 1)/3;
        String[] result = new String[numberOfPieces];
        for (int i = 0; i < numberOfPieces; i++) {
            result[i] = placement.substring(i*3+1,i*3+4);
        }
        return result;
    }
}
