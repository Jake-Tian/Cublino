package comp1140.ass2;

import comp1140.ass2.gui.Player;

import java.util.*;

public class Cublino {

    /**
     * Given a string of current state and get all white dices
     *
     * @param state a string representing a game state
     * @return a list of White Dices
     */
    public static List<String> getWhiteDices(String state){
        List<String> white = new ArrayList<>();

        for (int i = 1;i<state.length();i+=3) {
            String dice = state.substring(i, i + 3);
            if (Character.isUpperCase(dice.charAt(0))) white.add(dice);
        }
        return white;
    }


    /**
     * Given a string of current state and get all black dices
     *
     * @param state a string representing a game state
     * @return a list of black Dices
     */
    public static List<String> getBlackDices(String state){
        List<String> black = new ArrayList<>();

        for (int i = 1;i<state.length();i+=3) {
            String dice = state.substring(i, i + 3);
            if (Character.isLowerCase(dice.charAt(0))) black.add(dice);
        }

        return black;
    }



    /**
     * Determine whether the input state is well formed or not.
     * Note: you don't have to consider whether the state is valid for this task.
     * A state is well formed if it has the following format:
     * [variant][dice]*
     * where [variant] and [dice] are replaced by the corresponding substrings below. Note that [dice]* means zero or
     * more repetitions of the [dice] substring.
     *
     * 1. [variant] The variant substring is a single character which is either an upper or lower case 'p' or 'c' - The
     * letter encodes which variant the of the rules the game is using and the case represents the turn of the current
     * player.
     *
     * 2. [dice] The dice substring contains three characters. The first character can be either an upper or lower case
     * letter in the range 'a' to 'x'. The letter encodes the orientation of the dice and the case encodes which player
     * the dice belongs two. The next two characters encode the position of the dice in the format [column][row] where
     * the column is one character in the range 'a' to 'g' and the row is one character in the range '1' to '7'.
     *
     * See the "Encoding Game State" section in the README for more details.
     *
     * @param state a string representing a game state
     * @return true if the input state is well formed, otherwise false
     */
    public static Boolean isStateWellFormed(String state) {

        // Determine whether the length of the state is valid
        if (state.length() % 3 != 1) return false;

        // Check the first character of the state
        if (state.toUpperCase().charAt(0) != 'P' && state.toUpperCase().charAt(0) != 'C') return false;
        state = state.substring(1);     // Remove the first character

        // Every three characters is a dice. Check whether each of them is valid
        for (int i = 0; i*3 < state.length(); i++) {
            char firstLetter = state.charAt(3*i);
            char secondLetter = state.charAt(3*i+1);
            // Convert the the third character into number
            int thirdNum = Character.getNumericValue(state.charAt(3*i+2));

            // The first character must be uppercase of lowercase 'a' to 'x'
            if (firstLetter < 65 || (firstLetter > 88 && firstLetter < 97) || firstLetter > 120) return false;
            // The second character must be in range 'a' to 'g'
            if (secondLetter < 97 || secondLetter > 103) return false;
            // The third character must be in range '1' to '7'
            if (thirdNum < 1 || thirdNum > 7) return false;
        }

        return true;
    }

    /**
     * Determine whether the input state is valid.
     * A game state is valid if it satisfies the conditions below. Note that there are some shared conditions and
     * some conditions which are specific to each variant of the game. For this task you are expected to check states
     * from both variants.
     *
     * [Both Variants]
     * 1. The game state is well formed.
     * 2. No two dice occupy the same position on the board.
     *
     * [Pur]
     * 1. Each player has exactly seven dice.
     * 2. Both players do not have all seven of their dice on the opponent's end of the board (as the game would have
     * already finished before this)
     *
     * [Contra]
     * 1. Each player has no more than seven dice.
     * 2. No more than one player has a dice on the opponent's end of the board.
     *
     * @param state a string representing a game state
     * @return true if the input state is valid, otherwise false
     */
    public static Boolean isStateValid(String state) {
        if(state != null && isStateWellFormed(state) ){
            // Store strings of dices separately
            List<String> white = getWhiteDices(state);  // an array to store strings of white dices
            List<String> black = getBlackDices(state);  // an array to store strings of black dices

            int wDice = 0;       // number of white dice which is on black's end of the board
            int bDice = 0;       // number of black dice which is on white's end of the board

            // Store locations of all dices on the board
            String[] location = new String[white.size()+black.size()];
            int num = 2;      // the beginning index of the location of the first die

            for (int i = 0;i< location.length;i++){
                location[i] = state.substring(num,num+2);
                num +=3;
            }

            // sorted locations to determine whether two dices occupy the san position
            Arrays.sort(location);

            // check if there exists overlapping dices
            for (int i = 0;i<location.length-1;i++){
                if (location[i].equals(location[i+1]))
                    return false;
            }

            if (state.charAt(0) == 'p' || state.charAt(0)=='P'){
                //  conditions of Cublino Pur
                if (white.size() == 7 && black.size() == 7) {

                    // count the number of white dices on black's end of the board
                    for (String w : white){
                        if (w.charAt(2) == '7')
                            wDice ++;
                    }

                    // count the number of black dices on white's end of the board
                    for (String b : black){
                        if (b.charAt(2) == '1')
                            bDice ++;
                    }

                    // Both players do not have all seven of their dice on the opponent's end of the board
                    return !(wDice == 7 && bDice == 7 );
                }
            }else {
                //  conditions of Cublino Contra
                if (white.size() <= 7 && black.size() <= 7){

                    // no dies on the board
                    if (state.length() == 1) return false;

                    // count the number of white dices on black's end of the board
                    for (String w : white){
                        if (w.charAt(2) == '7')
                            wDice ++;
                    }
                    // count the number of black dices on white's end of the board
                    for (String b : black){
                        if (b.charAt(2) == '1')
                            bDice ++;
                    }

                    // No more than one player has a dice on the opponent's end of the board.
                    return (bDice >= 0 && wDice == 0) || (bDice == 0 && wDice >= 0);
                }
            }
        }
        return false;
    }

    /**
     * Determine whether a state represents a finished Pur game, and if so who the winner is.
     *
     * A game of Cublino Pur is finished once one player has reached the opponent's end of the board with all seven of
     * their dice. Each player then adds the numbers facing upwards on their dice which have reached the opponent's end
     * of the board. The player with the highest total wins.
     *
     * You may assume that all states input into this method will be of the Pur variant and that the state will be
     * valid.
     *
     * @param state a Pur game state
     * @return 1 if player one has won, 2 if player two has won, 3 if the result is a draw, otherwise 0.
     */
    public static int isGameOverPur(String state) {
        if(isStateValid(state) && (state.charAt(0)=='p' || state.charAt(0) == 'P')){

            List<String> white = new ArrayList<>(); // a list to store strings of white dices on black side
            List<String> black = new ArrayList<>(); // a list to store strings of black dices on white side
            int wScore = 0;   // score of white dice
            int bScore = 0;   // score of black dice

            // store dices on the opponent's end of board separately
            for (int i = 1;i < state.length();i+=3){
                String dice = state.substring(i,i+3);
                if (Character.isUpperCase(dice.charAt(0)) && dice.charAt(2) == '7')
                    white.add(dice);

                if (Character.isLowerCase(dice.charAt(0)) && dice.charAt(2) == '1')
                    black.add(dice);
            }

            // determine whether the game is finished

            // the game is finished
            if (white.size()== 7 || black.size() == 7){

                // count the facing upward number of white dices
                for (String dice : white){
                    char or = dice.charAt(0); // the orientation of a dice
                    wScore += (or -65)/4 +1;  // add the number facing up to the score
                }

                // count the facing upward number of black dices
                for (String dice : black) {
                    char or = dice.charAt(0); // the orientation of a dice
                    bScore += (or - 97)/4+1;  // add the number facing up to the score
                }

                if (bScore == wScore)  // it is a draw
                    return 3;
                if (bScore > wScore)  // black/player2 wins
                    return 2;
                else                  // white/player1 wins
                    return 1;
            }
        }

        return 0;
    }

    /**
     * Determine whether a single step of a move is valid for a given Pur game.
     * A step is encoded as follows: [position][position]. The [position] substring is a two character string encoding a
     * position on the board. The first position represents the starting position of the dice making the step and the
     * second position represents the ending position of the dice making the step. You may assume that the step strings
     * input into this method are well formed according to the above specification.
     *
     * A step is valid if it satisfies the following conditions:
     * 1. It represents either a tilt or a jump of a dice.
     * 2. The ending position of the step is not occupied.
     * 3. The step moves towards the opponent's end of the board or horizontally (along its current row).
     * 3. If it is a jump step, there is a dice in the position which is jumped over.
     *
     * You may assume that all states input into this method will be of the Pur variant and that the state will be
     * valid.
     * @param state a Pur game state
     * @param step a string representing a single step of a move
     * @return true if the step is valid for the given state, otherwise false
     */
    public static Boolean isValidStepPur(String state, String step) {
        // Condition 1: It represents either a tilt or a jump of a dice

        // Get column and row positions as ints
        int startCol = (step.charAt(0) - 97);
        int startRow = Integer.parseInt(step.substring(1,2));
        int endCol = (step.charAt(2) - 97);
        int endRow = Integer.parseInt(step.substring(3,4));

        // Step must be in straight line
        if (!(startCol == endCol) && !(startRow == endRow)) return false;

        // Step must either be of either a 1 or 2 distance
        int distance = Math.abs(endCol - startCol) + Math.abs(endRow - startRow);
        if (distance != 1 && distance != 2) return false;

        // Condition 2: The ending position of the step is not occupied
        String endPosition = step.substring(2);
        for (int i = 2; i < state.length(); i += 3) { // Check each die in state
            if (state.substring(i, i+2).equals(endPosition)) {
                // If there is a dice already at the end point, return false
                return false;
            }
        }

        // Condition 3a: The step moves towards the opponent's end of the board or horizontally (along its current row)
        if (state.charAt(0) == 80 && startRow > endRow) return false;   // Player 1 should move dices to row 7
        if (state.charAt(0) == 112 && startRow < endRow) return false;  // Player 2 should move dices to row 1

        // Condition 3b: If it is a jump step, there is a dice in the position which is jumped over
        if (distance == 2) {    // It only need to consider the step with the distance of 2 (jump)

            int diceNum = 0;      // The index number of tested dice. The first one is 0 and the last one is 13
            String dicePosition;  // The position of the tested dice
            String middleDice;    // The position of the dice in the position which is jumped over

            // The position of the dice jumped over is the mid point of the starting position and the ending position
            if (startRow == endRow) {   // The dice jumps horizontally
                char midCol = (char) ((step.charAt(0) + step.charAt(2))/2);
                middleDice = Character.toString(midCol) + startRow;
            } else {                    // The dice jumps vertically
                middleDice = step.substring(0,1) + (startRow+endRow)/2;
            }

            // Check whether the mid point is on the board
            while (diceNum < 14) {
                dicePosition = state.substring(diceNum*3+2,diceNum*3+4);
                if (middleDice.equals(dicePosition)) return true;   // If the mid point is on the board, return true
                diceNum += 1;
            }
            return false;   // Otherwise, return false
        }

        return true;
    }

    /**
     * Determine whether a single step of a move is valid for a given Contra game. Based off the isValidStepPur method.
     * @param state a Contra game state
     * @param step a string representing a single step of a move
     * @return true if the step is valid for the given state, otherwise false
     */
    public static boolean isValidStepContra(String state, String step) {
        // Condition 1: It represents a tilt of a dice

        // Get column and row positions as ints
        int startCol = (step.charAt(0) - 97);
        int startRow = Integer.parseInt(step.substring(1, 2));
        int endCol = (step.charAt(2) - 97);
        int endRow = Integer.parseInt(step.substring(3, 4));

        // Step must be in straight line
        if (!(startCol == endCol) && !(startRow == endRow)) return false;

        // Step must be 1 unit apart
        int distance = Math.abs(endCol - startCol) + Math.abs(endRow - startRow);
        if (distance != 1) return false;

        // Condition 2: The ending position of the step is not occupied
        String endPosition = step.substring(2);
        for (int i = 2; i < state.length(); i += 3) { // Check each die in state
            if (state.substring(i, i + 2).equals(endPosition)) {
                // If there is a dice already at the end point, return false
                return false;
            }
        }

        // Condition 3: The step moves towards the opponent's end of the board or horizontally (along its current row)
        if (state.charAt(0) == 'C' && startRow > endRow) return false;   // Player 1 should move dices to row 7
        if (state.charAt(0) == 'c' && startRow < endRow) return false;  // Player 2 should move dices to row 1

        return true; // otherwise valid step
    }



    /**
     * Determine whether a move (sequence of steps) is valid for a given game.
     * A move is encoded as follows [position]*. The [position] substring encodes a position on the board. This encoding
     * lists a sequence of positions that a dice will have as it makes a move. Note that [position]* means zero or more
     * repetitions of the [position] substring. You may assume that the move strings input into this method are well
     * formed according to the above specification.
     *
     * A move is valid if it satisfies the following conditions:
     * 1. The starting position of the move contains a dice belonging to the player who's turn it is.
     * 2. All steps in the move are valid.
     * 3. The move contains at least one step.
     * 4. Only the first step may be a tipping step.
     * 5. The starting and ending positions of the moved dice are different.
     *
     * You may assume that all states input into this method will be of the Pur variant and that the state will be
     * valid.
     *
     * @param state a Pur game state
     * @param move a string representing a move
     * @return true if the move is valid for the given state, otherwise false
     */
    public static Boolean isValidMovePur(String state, String move) {
        // The move contains at least one step.
        if (move.length() >= 4) {

            // a list of strings to store steps
            List<String> steps = new ArrayList<>();
            for (int i = 0; i < move.length()-2; i+= 2){
                String step = move.substring(i,i+4);
                steps.add(step);
            }

            //All steps in the move are valid
            for (String step : steps){
                if (!isValidStepPur(state,step)) return false;
            }

            //Only the first step may be a tipping step.
            for (int n = 1;n < steps.size();n++){
                if (isTipStep(steps.get(n))) return false;
            }

            // The starting and ending positions of the moved dice are different.
            if (move.substring(0,2).equals(move.substring(move.length()-2))) return false;

            List<String> white = getWhiteDices(state); // a list to store white dices
            String turn = steps.get(0).substring(0,2); // the turn of current state
            boolean bw = false; // an identifier to determine whose turn of the move,false for black and true for white

            for (String w : white){
                String location = w.substring(1,3); // the location of a white dice
                if (location.equals(turn)) {
                    bw = true;  // if the first position has a white dice
                    break;
                }
            }

            //The starting position of the move contains a dice belonging to the player who's turn it is.
            return (Character.isLowerCase(state.charAt(0)) && !bw ) || (Character.isUpperCase(state.charAt(0)) && bw);
        }
        return false;
    }

    /**
     * Given a step string, determine whether the step is a tip step
     *
     * @param step a Pur game state
     * @return true if the step is a tip step
     */

    public static boolean isTipStep(String step) {
        // Get column and row positions as ints
        int startCol = step.charAt(0);
        int startRow = Integer.parseInt(step.substring(1, 2));
        int endCol = step.charAt(2);
        int endRow = Integer.parseInt(step.substring(3, 4));

        // if the two position is next to each other, then it is a tipping step
        if (startCol == endCol)
            return Math.abs(endRow-startRow) == 1;
        if (startRow == endRow)
            return Math.abs(endCol-startCol) == 1;

        return false;
    }
    /**
     * Given a Pur game state and a move to play, determine the state that results from that move being played.
     * If the move ends the game, the turn should be the player who would have played next had the game not ended. If
     * the move is invalid the game state should remain unchanged.
     *
     * You may assume that all states input into this method will be of the Pur variant and that the state will be
     * valid.
     *
     * @param state a Pur game state
     * @param move a move being played
     * @return the resulting state after the move has been applied
     */
    public static String applyMovePur(String state, String move) {

        // If the move is not valid, return the original state
        if (!isValidMovePur(state,move)) return state;

        // Convert the state into a new string builder so it can be edited
        StringBuilder newState = new StringBuilder(state);
        // The new encoding of the dice after moving
        String newDice = "";

        // Using a for loop to find out which dice on the board moved to the new location
        for (int i = 2; i < state.length(); i+=3) {
            // If the location of the dice on the board is same as the starting location on the board
            // then this piece is going to move
            if (state.substring(i, i + 2).equals(move.substring(0,2))) {

                char orientation = state.charAt(i-1);   // Get the orientation of the dice
                newState.delete(i-1, i+2);              // Remove the old dice from the state

                // If the move begins with a tip, the orientation changes
                // If the move begins with the jump, the orientation remains the same
                if (isTipStep(move.substring(0,4))) {

                    // If the orientation is a capital letter, then is a white dice. Otherwise, it's black
                    boolean white = orientation < 89;
                    // Get the number on the top face
                    int numUp = white ? (orientation-65)/4+1 : (orientation-97)/4+1;

                    /* Get the ordinal on the forward face by ranking from the lowest to the highest.
                    For example, A represents the lowest number on the forward face (2) and
                    J represents the second lowest number on the forward face (2) */
                    int ordinalForward = white ? (orientation-65)%4+1 : (orientation-97)%4+1;

                    /* Get the ordinal on the right face depending on the top number and forward ordinal.
                    (I) If the top number is odd: the forward ordinal and the right ordinal will have the relation:
                    1 -> 2; 2 -> 4; 4 -> 3; 3 -> 1
                    (II) If the top number is even: the forward ordinal and the right ordinal will have the relation:
                    1 -> 3; 2 -> 1; 3 -> 4; 4 -> 2
                    */
                    int oddRightOrdinal = ordinalForward <= 2 ? 2*ordinalForward : 2*ordinalForward-5;
                    int ordinalRight = numUp%2 == 0 ? 5 - oddRightOrdinal : oddRightOrdinal;

                    int numForward = ordinalToNum(ordinalForward, numUp);   // Get the number on the forward face
                    int numRight = ordinalToNum(ordinalRight, numUp);       // Get the number on the right face

                    // Update the number on the top and forward face
                    int temporaryNum = numUp;
                    if (move.charAt(3) - move.charAt(1) == 1) {         // Move forward
                        numUp = 7 - numForward;
                        numForward = temporaryNum;
                    } else if (move.charAt(1) - move.charAt(3) == 1) {  // Move backward
                        numUp = numForward;
                        numForward = 7 - temporaryNum;
                    } else if (move.charAt(2) - move.charAt(0) == 1) {  // Move left
                        numUp = numRight;
                    } else {                                            // Move right
                        numUp = 7 - numRight;
                    }

                    // Convert the forward number back to the ordinal by the same way
                    ordinalForward = switch (numUp) {
                        case 1,6 -> numForward - 1;
                        case 2,5 -> numForward - numForward/3;
                        default -> numForward > 3 ? numForward-2 : numForward;
                    };

                    // Find out the new orientation
                    orientation = white ? (char) (64 + 4*numUp-4 + ordinalForward) :
                            (char) (96 + 4*numUp-4 + ordinalForward);
                }

                // Identify the encoding of the new dice by the (new) orientation and the new location
                newDice = orientation + move.substring(move.length()-2);

            }
        }

        // Adding the new dice back to the state in the correct position
        // The encoding of the state is sorted from lower row to higher low and from lower column to higher column
        for (int i = 1; i < newState.length(); i+=3) {
            char xCor = newDice.charAt(1);
            char yCor = newDice.charAt(2);
            if (newState.charAt(i+2) > yCor || (newState.charAt(i+2) == yCor && newState.charAt(i+1) > xCor)) {
                newState.insert(i,newDice);
                break;
            }
        }
        if (newState.length() < 43) newState.append(newDice);

        // Change the player
        if (newState.charAt(0) < 89) {
            newState.replace(0, 1, Character.toString(newState.charAt(0) + 32));
        } else {
            newState.replace(0, 1, Character.toString(newState.charAt(0) - 32));
        }

        // Convert the edited string builder back to the string and return it
        return newState.toString();
    }


    /**
     * Convert the ordinal into number depending on the top number. It has the following rules:
     * 1. If the top number is 1 or 6: 1 -> 2; 2 -> 3; 3 -> 4; 4 -> 5
     * 2. If the top number is 2 or 5: 1 -> 1; 2 -> 3; 3 -> 4; 4 -> 6
     * 3. If the top number is 3 or 4: 1 -> 1; 2 -> 2; 3 -> 5; 4 -> 6
     *
     * @param ordinal the ordinal needs to be converted. The range is 1 to 4
     * @param numUp the number on the top face. The range is 1 to 6
     * @return the number corresponding to the given ordinal. The range is 1 to 6
     */
    public static int ordinalToNum (int ordinal, int numUp) {
        return switch (numUp) {
            case 1, 6 -> ordinal + 1;
            case 2, 5 -> ordinal + ordinal / 2;
            default -> ordinal > 2 ? ordinal + 2 : ordinal;
        };
    }

    /**
     * Given a valid Pur game state, return a valid move.
     * This task imposes an additional constraint that moves returned must not revisit positions previously occupied as
     * part of the move (ie. a move may not contain a jumping move followed by another jumping move back to the previous
     * position).
     *
     * You may assume that all states input into this method will be of the Pur variant and that the state will be
     * valid.
     *
     * @param state a Pur game state
     * @return a valid move for the current game state.
     */
    public static String generateMovePur(String state) {

        int count = moveScore.countDice(state); // count the dices which are closer to opponent's side

        if (count < 7) {

            Random rand = new Random();

            Step[] firstSteps = Step.findFirstSteps(state); // find all valid first steps for current player's dices

            List<String> moves = Step.getAllSteps(firstSteps, new State(state)); // get all continued steps

            List<moveScore> moveScores = new ArrayList<>();

            for (String move : moves) {
                moveScores.add(new moveScore(state, move));
                // for all moves, construct a moveScore to store its final numUp number
            }

            boolean allSameScore = false;
            int score = moveScores.get(0).getScore();    // use the score of the first move as an initialisation
            String target = moveScores.get(0).getMove(); // use the first move as an initialisation

            for (int i = 0; i < moveScores.size() - 1; i++) {
                if (moveScores.get(i).getScore() == moveScores.get(i + 1).getScore())
                    allSameScore = true;
                else {
                    allSameScore = false;
                    break;
                }
            }

            if (allSameScore) return moves.get(rand.nextInt(moves.size()));

            for (moveScore move : moveScores) {
                // if find a move with greater score, replace the score and target move with the move we find
                if (move.getScore() >= score) {
                    score = move.getScore();
                    target = move.getMove();
                }
            }
            return target;

        }else {

            GameTree tree = stateToGameTree(state);

            int highestScore = -5;
            String bestMove = "";
            List<Integer> scores = new ArrayList<>();

            for (GameTree subTree : tree.getNextStates()) {
                findScore(tree,scores);
                for (int score : scores) {
                    if (score > highestScore) {
                        bestMove = subTree.getMove();
                        highestScore = score;
                    }
                }
            }
            return bestMove;
        }
    }


    /**
     * Calculate the all scores a tree can possibly generate at the farthest step it can predict
     *
     * @param tree the tree to be calculated
     * @param list the list of scores from different states, set to be empty at the beginning
     */
    public static void findScore (GameTree tree, List<Integer> list) {

        if (tree.getNextStates() == null) {     // If we reach the end of the tree, add the score to the list
            list.add(tree.getScore());
        } else {
            for (GameTree subTree : tree.getNextStates()) {
                findScore(subTree,list);
            }
        }
    }


    /**
     * Convert a single state to a game tree looking up into 3 steps
     *
     * @param state the game state
     * @return the game tree
     */
    public static GameTree stateToGameTree (String state) {

        GameTree tree = new GameTree(state, null, stateToColumnsScore(state));

        // Expand the tree three times to get a bigger tree
        for (int i = 0; i < 3; i++) {
            expandTree(tree);
        }

        return tree;
    }


    /**
     * Expand the tree to the next step
     *
     * @param tree the game tree
     */
    public static void expandTree (GameTree tree) {

        // If we reach the end of the tree, expand it for one level
        if (tree.getNextStates() == null) {

            Step[] firstSteps = Step.findFirstSteps(tree.getState());
            List<String> moves = Step.getAllSteps(firstSteps,new State(tree.getState())); // get all continued steps

            List<GameTree> trees = new ArrayList<>();

            // For each possible move, generate a new state from the current state and calculate the corresponding score
            for (String move : moves) {
                String newState = applyMovePur(tree.getState(), move);
                GameTree subTree = new GameTree(newState, move, stateToColumnsScore(newState));
                trees.add(subTree);
            }

            tree.setNextStates(trees);
        }

        // If we haven't reached the end, check the next stage by recursion
        else {
            for (GameTree subTree : tree.getNextStates()) {
                expandTree(subTree);
            }
        }
    }


    /**
     * Calculate the current score of the state by counting how far the dice goes
     * Then use the white score minus black score
     *
     * @param state the game state
     * @return the columns score
     */
    public static int stateToColumnsScore(String state) {

        int score = 0;

        for (int i = 1; i < state.length(); i += 3) {
            int rowNum = Character.getNumericValue(state.charAt(i+2));
            if (state.charAt(i) <= 88) score += rowNum;
            else score += 7 - rowNum;
        }
        if (isGameOverPur(state) == 2 && Character.isLowerCase(state.charAt(0)))
            score += 50;

        return score;
    }

    /**
     * Determine whether a state represents a finished Contra game, and if so who the winner is.
     *
     * A player wins a game of Cublino Contra by reaching the opponent's end of the board with one of their dice.
     *
     * You may assume that all states input into this method will be of the Contra variant and that the state will be
     * valid.
     *
     * @param state a Contra game state
     * @return 1 if player one has won, 2 if player two has won, otherwise 0.
     */
    public static int isGameOverContra(String state) {
        // if it is a contra game
        if (isStateValid(state) && (state.charAt(0)=='c' || state.charAt(0) == 'C')){

            List<String> white = getWhiteDices(state); // a list to store all white dices
            List<String> black = getBlackDices(state); // a list to store all black dices

            for (String w : white){
                if (w.charAt(2) == '7') // if there is a white dice on black side, player 1 wins
                    return 1;
            }

            for (String b : black){
                if (b.charAt(2) == '1') // if there is a black dice on white side, player 2 wins
                    return 2;
            }

        }
        return 0;
    }

    /**
     * Given a Contra game state and a move to play, determine the state that results from that move being played.
     * If the move ends the game, the turn should be the player who would have played next had the game not ended. If
     * the move is invalid the game state should remain unchanged. See the README for what constitutes a valid Contra
     * move and the rules for updating the game state.
     *
     * You may assume that all states input into this method will be of the Contra variant and that the state will be
     * valid.
     *
     * @param state a Contra game state
     * @param move a move being played
     * @return the resulting state after the move has been applied
     */
    public static String applyMoveContra(String state, String move) {

        if (!isTipStep(move)) return state;     // The Contra game only consists of tip step

        int stateLength = state.length();       // The original length of the state string
        // Convert the state into a new string builder so it can be edited
        StringBuilder newState = new StringBuilder(state);
        String newDice = "";                    // The new encoding of the dice after moving

        // Using a for loop to find out which dice on the board moved to the new location
        for (int i = 2; i < state.length(); i+=3) {
            // If the location of the dice on the board is same as the starting location on the board
            // then this piece is going to move
            if (state.substring(i, i + 2).equals(move.substring(0,2))) {

                char orientation = state.charAt(i-1);   // Get the orientation of the dice
                newState.delete(i-1, i+2);              // Remove the old dice from the state

                boolean white = orientation < 89;
                // Get the number on the top face
                int numUp = white ? (orientation-65)/4+1 : (orientation-97)/4+1;

                // Get the ordinal on the forward face by ranking from the lowest to the highest.
                int ordinalForward = white ? (orientation-65)%4+1 : (orientation-97)%4+1;

                // Get the ordinal on the right face depending on the top number and forward ordinal.
                int oddRightOrdinal = ordinalForward <= 2 ? 2*ordinalForward : 2*ordinalForward-5;
                int ordinalRight = numUp%2 == 0 ? 5 - oddRightOrdinal : oddRightOrdinal;

                int numForward = ordinalToNum(ordinalForward, numUp);   // Get the number on the forward face
                int numRight = ordinalToNum(ordinalRight, numUp);       // Get the number on the right face

                // Update the number on the top and forward face
                int temporaryNum = numUp;
                if (move.charAt(3) - move.charAt(1) == 1) {         // Move forward
                    numUp = 7 - numForward;
                    numForward = temporaryNum;
                } else if (move.charAt(1) - move.charAt(3) == 1) {  // Move backward
                    numUp = numForward;
                    numForward = 7 - temporaryNum;
                } else if (move.charAt(2) - move.charAt(0) == 1) {  // Move left
                    numUp = numRight;
                } else {                                            // Move right
                    numUp = 7 - numRight;
                }

                // Convert the forward number back to the ordinal by the same way
                ordinalForward = switch (numUp) {
                    case 1,6 -> numForward - 1;
                    case 2,5 -> numForward - numForward/3;
                    default -> numForward > 3 ? numForward-2 : numForward;
                };

                // Find out the new orientation
                orientation = white ? (char) (64 + 4*numUp-4 + ordinalForward) :
                        (char) (96 + 4*numUp-4 + ordinalForward);

                // Identify the encoding of the new dice by the (new) orientation and the new location
                newDice = orientation + move.substring(move.length()-2);

            }
        }

        for (int i = 1; i < newState.length(); i += 3) {

            if (Math.abs(newDice.charAt(1)-newState.charAt(i+1)) + Math.abs(newDice.charAt(2)-newState.charAt(i+2)) == 1) {

                char orientation = newDice.charAt(0);   // Get the orientation of the dice
                boolean white = orientation < 89;
                int numUp = white ? (orientation-65)/4+1 : (orientation-97)/4+1;

                if ((white && newState.charAt(i)>=97) || (!white && newState.charAt(i)<89)) {
                    int friendTotal = opponentAroundTotal(newState.toString(), newState.substring(i,i+3)) + numUp;
                    int enemyTotal = opponentAroundTotal(newState.toString(), newDice);

                    if (friendTotal < enemyTotal) {
                        // Change the player
                        if (newState.charAt(0) < 89) {
                            newState.replace(0, 1, Character.toString(newState.charAt(0) + 32));
                        } else {
                            newState.replace(0, 1, Character.toString(newState.charAt(0) - 32));
                        }

                        return newState.toString();
                    }
                    else if (friendTotal > enemyTotal) {
                        newState.delete(i, i + 3);
                        stateLength -= 3;
                    }
                }

            }
        }

        // Adding the new dice back to the state in the correct position
        // The encoding of the state is sorted from lower row to higher low and from lower column to higher column
        for (int i = 1; i < newState.length(); i+=3) {
            char xCor = newDice.charAt(1);
            char yCor = newDice.charAt(2);
            if (newState.charAt(i+2) > yCor || (newState.charAt(i+2) == yCor && newState.charAt(i+1) > xCor)) {
                newState.insert(i,newDice);
                break;
            }
        }
        if (newState.length() < stateLength) newState.append(newDice);

        // Change the player
        if (newState.charAt(0) < 89) {
            newState.replace(0, 1, Character.toString(newState.charAt(0) + 32));
        } else {
            newState.replace(0, 1, Character.toString(newState.charAt(0) - 32));
        }

        return newState.toString();
    }

    /**
     * When the "battle" occurs in the Contra game, calculate the sum of upward numbers of all adjacent dice
     * belonging to the opponent
     *
     * @param state a Contra game state
     * @param dice the coding of the dice
     * @return the total number of upward face of the opponent dice
     */
    public static int opponentAroundTotal (String state, String dice) {

        int total = 0;

        for (int i = 1; i < state.length(); i += 3) {

            if (Math.abs(dice.charAt(1)-state.charAt(i+1)) + Math.abs(dice.charAt(2)-state.charAt(i+2)) == 1) {

                char orientation = state.charAt(i);   // Get the orientation of the dice
                boolean white = orientation < 89;
                int numUp = white ? (orientation-65)/4+1 : (orientation-97)/4+1;

                if ((white && dice.charAt(0)>=97) || (!white && dice.charAt(0)<89)) total += numUp;
            }
        }

        return total;
    }

    /**
     * Given a valid Contra game state, return a valid move.
     *
     * You may assume that all states input into this method will be of the Contra variant and that the state will be
     * valid.
     *
     * @param state the Pur game state
     * @return a move for the current game state.
     */
    public static String generateMoveContra(String state) {

        Random rand = new Random();

        Step[] firstSteps = Step.findFirstSteps(state); // find all valid tipping steps for current player's dices

        List<String> moves = Step.getAllSteps(firstSteps,new State(state));

        int index = rand.nextInt(moves.size()); // a random number to get a random move from the list

        return moves.get(index);

    }

}
