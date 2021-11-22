package comp1140.ass2;

import comp1140.ass2.gui.Player;

import java.util.List;

public class moveScore {
        String initialState;
        String move;
        int num;

        public moveScore(String initialState,String move){
            this.initialState = initialState;
            this.move = move;
            this.num = getNum(initialState,move);
        }

        public int getScore() {
            return num;
        }

        public String getMove() {
            return move;
        }

    /** Given a game state and a move, return the upward number of the move dice
     *
     *
     * @param initial the initial state
     * @param move the move that would be applied
     * @return the upward number of the move die
     */
        public static int getNum(String initial,String move){
            String newState = Cublino.applyMovePur(initial,move); // get the new state after applying a move
            int colDifference = Math.abs(move.charAt(1)-move.charAt(move.length()-1));
            List<String> dice;
            if (Character.isUpperCase(initial.charAt(0)))
                dice = Cublino.getWhiteDices(newState);
            else
                dice = Cublino.getBlackDices(newState);


            int count = 0; // a variable to count the number of the dices which is closer to the opponent side

            for (int i = 1;i<initial.length();i+=3){
                String location = newState.substring(i+1,i+3); // get the location for a dice from the new game state
                /* if the location of the die from new state is equal to the last position of the move
                   then the die is the moving dice
                   return the upward number of the dice to get its score
                 */
                if (location.equals(move.substring(move.length()-2))){
                    if (Character.isUpperCase(initial.charAt(0))){
                        for (String b : dice){
                            if (b.charAt(2)>4)
                                count ++;
                        }
                        return count == 7 ? 0: new Piece(newState.substring(i,i+3)).getNumUp() + 6 * colDifference;
                    }else {
                        for (String b : dice){
                            if (b.charAt(2)<4)
                                count ++;
                        }
                        return count == 7 ? 0: new Piece(newState.substring(i,i+3)).getNumUp() + 6 * colDifference;
                    }
                }
            }
            return 0;
        }


    /** Given a game state and count the number of dices of the current player which are closer to
     * the opponent's side
     *
     * @param state the current game state
     * @return the number of dices which are closer to the opponent's side
     */

    public static int countDice(String state){
            State game = new State(state);
            Piece[] dice = game.getPieces();
            int count = 0;
            if (game.getTurn() == Player.WHITE){
                for (Piece white : dice){
                    if (white.getLoc().getY() > 5)
                        count ++;
                }
            }else {
                for (Piece black : dice){
                    if (black.getLoc().getY() < 3)
                        count++;
                }
            }
            return count;
        }

}
