package comp1140.ass2.gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Controls {
    public boolean gameHasStarted = false;
    public boolean onCoverScreen = true;
    public boolean isAI = false;
    public boolean isContra = false;

    public Controls() {
    }

    public void makeControls(Group group,
                             EventHandler<MouseEvent> finishMove,
                             EventHandler<MouseEvent> cancelMove,
                             EventHandler<MouseEvent> startPlayerGame,
                             EventHandler<MouseEvent> startAIGame,
                             EventHandler<MouseEvent> exitGame,
                             EventHandler<MouseEvent> undoMove) {
        group.getChildren().clear();
        overallControls(group, exitGame);
        if (gameHasStarted) {
            manageMoveControls(group, finishMove, cancelMove, undoMove);
        } else {
            manageGameControls(group, startPlayerGame, startAIGame);
        }
    }

    public void overallControls(Group group, EventHandler<MouseEvent> exitGame) {
        // Exit Button
        Group exitButton = new Group();
        exitButton.getStyleClass().add("button");
        exitButton.setOnMouseClicked(exitGame);

        Circle exitButtonCircle = new Circle(850, 70, 40);
        exitButtonCircle.getStyleClass().add("red-button");
        exitButton.getChildren().add(exitButtonCircle);

        Text exitText = new Text("Exit Game");
        exitText.setX(815);
        exitText.setY(25);
        exitText.setFont(Font.font(16.0));
        exitText.setFill(Color.rgb(185, 86, 86));
        exitButton.getChildren().add(exitText);

        try {
            FileInputStream crossInputStream = new FileInputStream("images/cross-red.png");
            Image crossImage = new Image(crossInputStream);
            ImageView cancelTick = new ImageView(crossImage);
            cancelTick.setX(829);
            cancelTick.setY(51);
            exitButton.getChildren().add(cancelTick);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        group.getChildren().add(exitButton);
    }

    /**
     * Draw Player vs Player and Player vs AI buttons
     * @param group The group to add the buttons to
     * @param startPlayerGame Code to run when Player vs Player is clicked
     * @param startAIGame Code to run when Player vs AI is clicked
     */
    public void manageGameControls(Group group, EventHandler<MouseEvent> startPlayerGame, EventHandler<MouseEvent> startAIGame) {
        Group startButton = new Group();
        startButton.getStyleClass().add("button");
        Circle startMoveCircle = new Circle(82, 330, 65);
        startMoveCircle.getStyleClass().add("green-button");
        startButton.getChildren().add(startMoveCircle);

        Text startText = new Text("Player vs Player");
        startText.setX(35);
        startText.setY(255);
        startText.setFill(Color.rgb(30, 149, 63));
        startText.setFont(Font.font(15));
        group.getChildren().add(startText);

        startButton.setOnMouseClicked(startPlayerGame);

        Group aiButton = new Group();
        aiButton.getStyleClass().add("button");
        Circle aiMoveCircle = new Circle(82, 480, 50);
        aiMoveCircle.getStyleClass().add("green-button");
        aiButton.getChildren().add(aiMoveCircle);

        Text aiText = new Text("Player vs AI");
        aiText.setX(40);
        aiText.setY(425);
        aiText.setFill(Color.rgb(30, 149, 63));
        aiText.setFont(Font.font(15));
        group.getChildren().add(aiText);

        aiButton.setOnMouseClicked(startAIGame);

        // Add button images
        try {
            FileInputStream playerInputStream = new FileInputStream("images/player.png");
            Image playerImage = new Image(playerInputStream);
            ImageView player = new ImageView(playerImage);
            player.setX(47);
            player.setY(285);
            startButton.getChildren().add(player);

            FileInputStream aiInputStream = new FileInputStream("images/computer.png");
            Image aiImage = new Image(aiInputStream);
            ImageView aiLogo = new ImageView(aiImage);
            aiLogo.setX(55);
            aiLogo.setY(460);
            aiButton.getChildren().add(aiLogo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        group.getChildren().add(startButton);
        group.getChildren().add(aiButton);
    }

    /**
     * Draw finish and cancel move buttons
     * @param group Group to add buttons to
     * @param finishMove Code to run when finish button is clicked
     * @param cancelMove Code to run when cancel button is clicked
     */
    public void manageMoveControls(Group group, EventHandler<MouseEvent> finishMove, EventHandler<MouseEvent> cancelMove, EventHandler<MouseEvent> undoMove) {
        Group finishButton = new Group();
        Group cancelButton = new Group();

        finishButton.setOnMouseClicked(finishMove);
        cancelButton.setOnMouseClicked(cancelMove);

        finishButton.getStyleClass().add("button");
        cancelButton.getStyleClass().add("button");

        Circle finishMoveCircle = new Circle(82, 330, 65);
        finishMoveCircle.getStyleClass().add("green-button");
        finishButton.getChildren().add(finishMoveCircle);

        Circle cancelMoveCircle = new Circle(115, 380, 35);
        cancelMoveCircle.getStyleClass().add("grey-button");
        cancelButton.getChildren().add(cancelMoveCircle);

        Text moveText = new Text("Current Move");
        moveText.setX(25);
        moveText.setY(230);
        moveText.setFill(Color.rgb(50, 50, 50));
        moveText.setFont(Font.font(20));
        group.getChildren().add(moveText);

        Text finishText = new Text("Finish");
        finishText.setX(67);
        finishText.setY(260);
        finishText.setFill(Color.rgb(25, 121, 52));
        finishText.setFont(Font.font(15));
        group.getChildren().add(finishText);

        Text cancelText = new Text("Cancel");
        cancelText.setX(95);
        cancelText.setY(430);
        cancelText.setFill(Color.rgb(150, 150, 150));
        cancelText.setFont(Font.font(15));
        group.getChildren().add(cancelText);

        try {
            FileInputStream tickInputStream = new FileInputStream("images/tick.png");
            Image tickImage = new Image(tickInputStream);
            ImageView finishTick = new ImageView(tickImage);
            finishTick.setX(45);
            finishTick.setY(305);
            finishButton.getChildren().add(finishTick);

            FileInputStream crossInputStream = new FileInputStream("images/cross.png");
            Image crossImage = new Image(crossInputStream);
            ImageView cancelCross = new ImageView(crossImage);
            cancelCross.setX(100);
            cancelCross.setY(365);
            cancelButton.getChildren().add(cancelCross);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Undo Button
        Group undoButton = new Group();
        undoButton.getStyleClass().add("button");
        undoButton.setOnMouseClicked(undoMove);

        Circle undoButtonCircle = new Circle(850, 185, 40);
        undoButtonCircle.getStyleClass().add("grey-button");
        undoButton.getChildren().add(undoButtonCircle);

        Text undoText = new Text("Undo Move");
        undoText.setX(810);
        undoText.setY(137);
        undoText.setFont(Font.font(16.0));
        undoText.setFill(Color.rgb(150, 150, 150));
        undoButton.getChildren().add(undoText);

        try {
            FileInputStream undoInputStream = new FileInputStream("images/undo.png");
            Image undoImage = new Image(undoInputStream);
            ImageView undoArrow = new ImageView(undoImage);
            undoArrow.setX(810);
            undoArrow.setY(150);
            undoButton.getChildren().add(undoArrow);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        group.getChildren().add(finishButton);
        group.getChildren().add(cancelButton);
        group.getChildren().add(undoButton);
    }

    /**
     * Show the game result in an overlay
     * @param group The group to add the overlay in
     * @param text The text to display in the overlay
     */
    public void showCompleteOverlay(Group group, String text) {
        Rectangle banner = new Rectangle();
        banner.setFill(Color.rgb(255, 255, 255));
        banner.setX(167);
        banner.setY(280);
        banner.setWidth(600);
        banner.setHeight(100);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setWidth(600);
        dropShadow.setHeight(100);

        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        dropShadow.setRadius(50);
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        banner.setEffect(dropShadow);

        Text bannerText = new Text(text);
        bannerText.setFont(new Font(50.0));
        bannerText.setY(350);
        double textWidth = bannerText.getLayoutBounds().getWidth();
        bannerText.setX(467 - (textWidth / 2));

        group.getChildren().add(banner);
        group.getChildren().add(bannerText);
    }
}
