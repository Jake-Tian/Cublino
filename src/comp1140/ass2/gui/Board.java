package comp1140.ass2.gui;

import comp1140.ass2.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Board extends Application {

    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;

    private final Group root = new Group();
    private final Group content = new Group();
    private final Group controls = new Group();

    private String stateEncoding = "";
    private String temporaryState = null;

    private String temporaryMove = null;
    private String selectedPiece = null;
    private ArrayList<String> stateHistory = new ArrayList<>();

    private int gameResult = 0;

    private final Controls controlState = new Controls();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cublino");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);
        try {
            scene.getStylesheets().add(getClass().getResource("board.css").toExternalForm());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Add content layout to board
        content.setLayoutX(166.5);
        content.setLayoutY(25);
        content.prefHeight(600);
        content.prefWidth(600);

        root.getChildren().add(content);
        root.getChildren().add(controls);

        refresh();

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Apply move depending on game mode
    private String applyMove(String state, String move) {
        if (controlState.isContra) return Cublino.applyMoveContra(state, move);
        return Cublino.applyMovePur(state, move);
    }

    // Check if game is over depending on game mode
    private int isGameOver(String state) {
        if (controlState.isContra) return Cublino.isGameOverContra(state);
        return Cublino.isGameOverPur(state);
    }

    // Generate move over depending on game mode
    private String generateMove(String state) {
        if (controlState.isContra) return Cublino.generateMoveContra(state);
        return Cublino.generateMovePur(state);
    }

    /**
     * Draw controls to the screen
     */
    void drawControls() {
        EventHandler<MouseEvent> finishMove = e -> {
            // A move has to have been started
            if (temporaryMove != null) {
                // Make move
                this.stateEncoding = applyMove(this.stateEncoding, temporaryMove);
                this.stateHistory.add(stateEncoding);
                this.gameResult = isGameOver(this.stateEncoding);
                // Reset data
                temporaryState = null;
                temporaryMove = null;
                selectedPiece = null;
                // If other player's move, and AI is enabled, make AI move
                if (this.stateEncoding.charAt(0) > 96 && controlState.isAI) {
                    String aiMove = generateMove(this.stateEncoding);
                    this.stateEncoding = applyMove(this.stateEncoding, aiMove);
                    this.stateHistory.add(stateEncoding);
                }
            }
            refresh();
        };

        EventHandler<MouseEvent> cancelMove = e -> {
            temporaryState = null;
            temporaryMove = null;
            selectedPiece = null;
            refresh();
        };

        // Start a Player vs Player Game
        EventHandler<MouseEvent> startPlayerGame = e -> {
            // If a game has ended, reset
            if (gameResult != 0) resetGame();
            controlState.gameHasStarted = true;
            controlState.isAI = false;
            refresh();
        };

        // Start a Player vs Computer Game
        EventHandler<MouseEvent> startAIGame = e -> {
            // If a game has ended, reset
            if (gameResult != 0) resetGame();
            controlState.gameHasStarted = true;
            controlState.isAI = true;
            refresh();
        };

        EventHandler<MouseEvent> exitGame = e -> {
            // Reset game data and go back to cover screen
            resetGame();
            controlState.onCoverScreen = true;
            refresh();
        };

        EventHandler<MouseEvent> undoMove = e -> {
            // Reset current data
            this.temporaryState = null;
            this.temporaryMove = null;
            this.selectedPiece = null;
            // Check if there are moves to revert
            if (stateHistory.size() > 1) {
                if (controlState.isAI) {
                    // Check if current turn is player
                    if (Character.isUpperCase(this.stateEncoding.charAt(0))) {
                        // If playing AI, remove two moves
                        stateHistory.remove(stateHistory.size() - 1);
                        if (stateHistory.size() > 1) stateHistory.remove(stateHistory.size() - 1);
                    }
                } else {
                    // Else remove the last move
                    stateHistory.remove(stateHistory.size() - 1);
                }
                // Revert the state to new history
                this.stateEncoding = stateHistory.get(stateHistory.size() - 1);
            }
            refresh();
        };

        controlState.makeControls(controls, finishMove, cancelMove, startPlayerGame, startAIGame, exitGame, undoMove);
        if (gameResult != 0) {
            String overlayText = gameResult == 1 ? "Player 1 Wins!" : gameResult == 2 ? "Player 2 Wins!" : "Draw!";
            controlState.showCompleteOverlay(controls, overlayText);
        }
    }



    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        assert(Cublino.isStateValid(placement));

        // Split up encoding
        String[] pieces = BoardGraphics.splitIntoPieces(placement);
        // Clear screen
        content.getChildren().clear();

        // Draw the board background
        String boardText = controlState.isContra ? "Cublino Contra" : "Cublino Pur";
        BoardGraphics.drawBoard(content, boardText);

        // Draw the highlights of pieces and piece moves
        highlightForSelected(content);

        // Draw pieces
        for (String piece : pieces) {
            drawPiece(piece);
        }
    }

    /**
     * Highlights the relevant squares for the selected die
     * @param group The JavaFX Group to add the highlights to
     */
    private void highlightForSelected(Group group) {
        if (selectedPiece != null) {
            // Highlight under selected piece
            highlightSquare(group, new Piece(selectedPiece).getLoc(), true);
            // Highlight all possible moves of die
            String state = temporaryState == null ? stateEncoding : temporaryState;
            boolean allowTip = temporaryMove == null || temporaryMove.length() <= 2;
            Step[] stepOptions = new Piece(selectedPiece).getValidSteps(state, allowTip, controlState.isContra);
            for (Step stepOption : stepOptions) {
                highlightSquare(group, stepOption.getNewLocation(), false);
            }

        }

    }

    /**
     * Highlights one square on the board
     * @param group The JavaFX Group to add the highlight to
     * @param loc The location of the highlight
     * @param isPiece Is the highlighted square where the selected piece is
     */
    private void highlightSquare(Group group, Location loc, boolean isPiece) {
        int size = isPiece ? 72 : 64;
        // Create a blue rectangle for highlight
        Rectangle selectedHighlight = new Rectangle();
        selectedHighlight.setFill(Color.rgb(155, 170, 209));
        // Alter size of highlight
        selectedHighlight.setHeight(size);
        selectedHighlight.setWidth(size);
        // Add border radius to highlight
        selectedHighlight.setArcHeight(20.0);
        selectedHighlight.setArcWidth(20.0);

        // Position highlight so that it is on top of the correct location
        selectedHighlight.setX(BoardGraphics.getPos(loc.getX() - 1) - ((size - 64) >> 1));
        selectedHighlight.setY(BoardGraphics.getPos(7 - loc.getY()) - ((size - 64) >> 1));

        selectedHighlight.setOnMouseClicked(e -> {
            if (!isPiece) {
                String checkMove;
                if (temporaryMove == null) {
                     checkMove = new Piece(selectedPiece).getLoc().toString();
                } else {
                    checkMove = temporaryMove;
                    int locIndex = temporaryMove.indexOf(loc.toString());
                    if (locIndex != -1) {
                        checkMove = checkMove.substring(0, locIndex);
                    }
                }
                checkMove += loc;
                temporaryMove = checkMove;
            }
            refresh();
        });

        // Add highlight to screen
        group.getChildren().add(selectedHighlight);
    }

    /**
     * Draws a piece on the board
     * @param pieceEncoding The string encoding of the piece
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
            diceView.setOnMouseClicked(e -> {
                // Ensure player can only click on their own pieces
                boolean whiteClickingBlack = stateEncoding.charAt(0) < 97 && piece.getColour() == Player.BLACK;
                boolean blackClickingWhite = stateEncoding.charAt(0) >= 97 && piece.getColour() == Player.WHITE;
                if (!whiteClickingBlack && !blackClickingWhite) {
                    if (controlState.gameHasStarted) {
                        selectedPiece = piece.convertString();
                        temporaryMove = null;
                        temporaryState = null;
                    }
                    refresh();
                }
            });
            // Add dice to board
            content.getChildren().add(diceView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the game data to original states
     */
    private void resetGame() {
        stateEncoding = (controlState.isContra ? "C" : "P") + "Wa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7";
        temporaryState = null;
        temporaryMove = null;
        selectedPiece = null;
        gameResult = 0;
        controlState.gameHasStarted = false;
        stateHistory = new ArrayList<>();
        stateHistory.add(this.stateEncoding);
    }

    /**
     * Refresh the UI
     */
    private void refresh() {
        // Draw Cover Screen
        if (controlState.onCoverScreen) {
            BoardGraphics.createCoverScreen(controls, e -> { // Start Code for Pur
                controlState.onCoverScreen = false;
                controlState.isContra = false;
                this.stateEncoding = "PWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7";
                stateHistory.add(this.stateEncoding);
                refresh();
            }, e -> { // Start Code for Contra
                controlState.onCoverScreen = false;
                controlState.isContra = true;
                this.stateEncoding = "CWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7";
                stateHistory.add(this.stateEncoding);
                refresh();
            });
        } else {
        // Show Main Game
            // Start the displayed state with the turn character, since
            // the turn will not be changed until the player clicks finish
            String updatedState = this.stateEncoding.substring(0, 1);
            // If move is long enough to be valid
            if (this.temporaryMove != null && this.temporaryMove.length() >= 2) {
                // Apply the move to display on screen
                updatedState += applyMove(this.stateEncoding, this.temporaryMove).substring(1);
            } else {
                // Else display the current state
                updatedState = this.stateEncoding;
            }
            this.temporaryState = updatedState;
            // If a piece is selected, and a move is in progress
            if (this.selectedPiece != null && this.temporaryMove != null) {
                // Move selected piece to new location after move
                this.selectedPiece = this.selectedPiece.charAt(0) + this.temporaryMove.substring(this.temporaryMove.length() - 2);
            }
            // Draw dice to screen
            makePlacement(updatedState);
            if (gameResult != 0) controlState.gameHasStarted = false;
            // Draw controls to screen
            drawControls();
        }
    }
}
