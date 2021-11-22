package comp1140.ass2.gui;

import comp1140.ass2.Cublino;
import comp1140.ass2.Piece;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A very simple viewer for piece placements in the Cublino game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int VIEWER_WIDTH = 933;
    private static final int VIEWER_HEIGHT = 700;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group content = new Group();
    private final Group orientationIndicators = new Group();
    private TextField textField;

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        if (!Cublino.isStateValid(placement)) return;
        String[] pieces = BoardGraphics.splitIntoPieces(placement);
        content.getChildren().clear();
        BoardGraphics.drawBoard(content, "Hovering over dice shows their orientation (which side will show when tipped)");

        for (String piece : pieces) {
            drawPiece(piece);
        }

        content.getChildren().add(orientationIndicators);
    }

    /**
     * Draws a piece on the board
     * @param pieceEncoding A three character string representing the piece
     */
    private void drawPiece(String pieceEncoding) {
        Piece piece = new Piece(pieceEncoding);
        // Column index from piece
        int column = piece.getLoc().getX() - 1;
        // Row index is inverted since board starts from top to bottom
        int row = 7 - piece.getLoc().getY();
        String fileName = BoardGraphics.getPieceFileName(piece, 0, 0);
        try{
            // Get image from file
            FileInputStream imageInputStream = new FileInputStream("images/dice/" + fileName + ".png");
            // Create JavaFx image objects
            Image image = new Image(imageInputStream);
            ImageView diceView = new ImageView(image);
            // Move to specified position
            diceView.setX(BoardGraphics.getPos(column));
            diceView.setY(BoardGraphics.getPos(row));
            diceView.setOnMouseEntered(event -> {
                showOrientation(piece);
            });
            diceView.setOnMouseExited(event -> {
                orientationIndicators.getChildren().clear();
            });
            // Add dice to board
            content.getChildren().add(diceView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows images of die if the hovered one is tipped
     * @param piece The piece being hovered above
     */
    private void showOrientation(Piece piece) {
        // Column index from piece
        int column = piece.getLoc().getX() - 1;
        // Row index is inverted since board starts from top to bottom
        int row = 7 - piece.getLoc().getY();
        // Loop through each four directions
        for (int i = 0; i < 4; i++) {
            int offset = i % 2 == 0 ? -1 : 1;
            int indicatorRow = i / 2 == 0 ? row : row + offset;
            int indicatorCol = i / 2 == 0 ? column + offset : column;

            // If the dice is outside the bounds, skip it
            if (indicatorCol < 0 || indicatorRow < 0) continue;
            if (indicatorCol > 6 || indicatorRow > 6) continue;

            try {
                // Get image for the indicator
                String fileName = BoardGraphics.getPieceFileName(piece, i / 2 == 0 ? offset : 0, i / 2 == 0 ? 0 : offset);
                FileInputStream imageInputStream = new FileInputStream("images/dice/" + fileName + ".png");
                Image diceImage = new Image(imageInputStream);
                ImageView indicator = new ImageView(diceImage);

                // Indicators are slightly transparent to show they aren't real pieces
                indicator.setOpacity(0.9);
                indicator.setX(BoardGraphics.getPos(indicatorCol));
                indicator.setY(BoardGraphics.getPos(indicatorRow));

                orientationIndicators.getChildren().add(indicator);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button refresh = new Button("Refresh");
        refresh.setOnAction(actionEvent -> {
                makePlacement(textField.getText());
                textField.clear();
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, refresh);
        hb.setSpacing(10);
        hb.setLayoutX(230);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cublino Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        // Add content layout to board
        content.setLayoutX(166.5);
        content.setLayoutY(25);
        content.prefHeight(600);
        content.prefWidth(600);
        root.getChildren().add(content);
        BoardGraphics.drawBoard(content, "Hovering over dice shows their orientation (which side will show when tipped)");
        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
