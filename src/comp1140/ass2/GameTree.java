package comp1140.ass2;

import java.util.List;

public class GameTree {

    private String state;                   // The current state of the game
    private String move;                    // The move applied
    private int score;                      // The score of the state, calculated by white minus black
    private List<GameTree> nextStates;      // The possible states after moves are applied

    public GameTree(String state, String move, int score, List<GameTree> nextStates) {
        this.state = state;
        this.move = move;
        this.score = score;
        this.nextStates = nextStates;
    }

    public GameTree (String state, String move, int score) {
        this.state = state;
        this.move = move;
        this.score = score;
    }

    // Getters and Setters
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<GameTree> getNextStates() {
        return nextStates;
    }

    public void setNextStates(List<GameTree> nextStates) {
        this.nextStates = nextStates;
    }




}
