package comp1140.ass2;

import comp1140.ass2.gui.Player;

import java.util.ArrayList;
import java.util.List;


import static comp1140.ass2.GameType.CONTRA;
import static comp1140.ass2.GameType.PUR;
import static comp1140.ass2.gui.Player.WHITE;
import static comp1140.ass2.gui.Player.BLACK;

public class State {
    //
    private GameType gameType;
    // The current pieces on the board
    private Piece[] currentBoard;
    // The current player's turn
    private Player turn;


    /**
     * Create a State from state string
     *
     * @param state The current game state
     */
    public State (String state){
        // change a String of state to a State data type
        List<Piece> board = new ArrayList<>();

        // convert all substring to pieces
        for(int i = 1; i<state.length();i+=3){
            String piece = state.substring(i,i+3);
            board.add(new Piece(piece));
        }

        this.currentBoard = board.toArray(new Piece[0]);
        // determine the current player,give two player an identifier
        this.turn = Character.isUpperCase(state.charAt(0)) ? WHITE : BLACK;

        this.gameType = (state.charAt(0) == 'P' || state.charAt(0)== 'p') ? PUR : CONTRA;
    }


    /**
     * Create a State from a given board and turn
     *
     * @param turn The player to get the score for
     * @param board The current game state
     */
    public State (Piece[] board,Player turn,GameType type){
        this.currentBoard = board;
        this.turn = turn;
        this.gameType = type;
    }


    /**
     * Create a list of dices of given player from a State
     *
     * @return pieces of dices of given player
     */
    public Piece[] getPieces() {
        Player turn = this.turn;
        List<Piece> dices = new ArrayList<>(); // a list to store the dices of the current player
        Piece[] allDices = this.getCurrentBoard(); // all dices on the board

        // add all pieces of current player to the list
        for(Piece p : allDices){
            if(p.getColour() == turn)
                dices.add(p);
        }
        return dices.toArray(new Piece[0]);
    }


    public Player getTurn() { return turn; }

    public GameType getGameType() { return gameType; }

    public Piece[] getCurrentBoard() { return currentBoard; }


    /**
     * Convert a State to a state string
     *
     * @return a game state string
     */
    public String convertState(){
        StringBuilder board = new StringBuilder();

        // convert current board to a string
        for (Piece piece : this.currentBoard) {
            board.append(piece.convertString()) ;
        }

        // Add an identifier to the current player
        if (gameType.equals(PUR)) {
            if (this.turn == WHITE)
                return "P" + board;
            if (this.turn == BLACK)
                return "p" + board;
        } else {
            if (this.turn == WHITE)
                return "C" + board;
            if (this.turn == BLACK)
                return "c" + board;
        }

        return board.toString();
    }


    /**
     * Get the current player of game from a game state
     *
     * @param state The current game state
     * @return current player
     */
    public static Player getTurn(String state){
        char player = state.charAt(0);
        // according to the identifier, get the current turn
        return Character.isUpperCase(player) ? WHITE : BLACK;
    }


    /**
     * Calculates the score for a player
     *
     * @param player The player to get the score for
     * @param state The current game state
     * @return The number of dots on their dice that reached the end
     */
    public static int getResults(Player player, Piece[] state) {
        int score = 0;
        for (Piece p : state){
            // only count the score of required player
                if (p.getColour() == player)
                    if (player == WHITE && p.getLoc().getY() == 7)
                        // count the score of white dices on row 7
                        score += p.getNumUp();

                    if (player == BLACK && p.getLoc().getY() == 1)
                        // count the score of black dices on row 1
                        score += p.getNumUp();
        }
        return score;
    }

    /**
     * Checks if the game has ended
     *
     * @param state the current state
     * @return A boolean of whether the game has ended or not
     */
    public static boolean isGameOver(State state) {
        int whiteDices = 0; // the number of white dices
        int blackDices = 0; // the number of black dices

        Piece[] pieces = state.getCurrentBoard();
        for (Piece p : pieces){
            // count how many dices achieve their opponent size
            if ( p != null) {
                if (p.getColour() == WHITE && p.getLoc().getY() == 7)
                    whiteDices++;
                if (p.getColour() == BLACK && p.getLoc().getY() == 1)
                    blackDices++;
            }
        }

        if (state.gameType.equals(PUR))
            return (whiteDices == 7 || blackDices == 7) && !(whiteDices == 7 && blackDices == 7);
        // if only one of the player has 7 dices on his opponent size, the game is over
        else
            return  (blackDices >= 0 && whiteDices == 0) || (blackDices == 0 && whiteDices >= 0);
    }


    /**
     * Switches the turn to the other player
     */
    public void changeTurn() {
        if(turn == WHITE)
            this.turn = BLACK;
        else
            this.turn = WHITE;
    }

    /**
     * switch to other player
     *
     * @param state The current game state
     * @return other player
     */
    public static Player otherTurn(State state){
        return state.turn == WHITE ? BLACK : WHITE;
    }

}
