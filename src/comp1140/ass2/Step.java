package comp1140.ass2;

import comp1140.ass2.gui.Player;

import java.util.*;

import static comp1140.ass2.GameType.CONTRA;
import static comp1140.ass2.GameType.PUR;

public class Step {
    // The old location of the piece (before moving)
    private Location oldLocation;
    // The new location of the piece (after moving)
    private Location newLocation;
    // The direction the piece is being moved in
    private Direction direction;
    // The type of step (tip or jump)
    private StepType stepType;

    public Step(Location oldLocation, Location newLocation) {
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public Step(String step){
        String oldLoc = step.substring(0,2);
        String newLoc = step.substring(2);
        this.oldLocation = new Location(oldLoc);
        this.newLocation = new Location(newLoc);
        if (Cublino.isTipStep(step))
            this.stepType = StepType.TIP;
        else
            this.stepType = StepType.JUMP;
    }


    public Step(Location oldLocation, Direction direction, StepType stepType) {
        this.oldLocation = oldLocation;
        this.direction = direction;
        this.stepType = stepType;
    }


    public Location getOldLocation() {
        return oldLocation;
    }

    public Location getNewLocation() {
        return newLocation;
    }



    /**
     * Given a location that a dice would move to
     * and find all possible jumping that the dice can reach for this location
     *
     * @param location the location to find all corresponding jumping steps
     * @param state The current game state
     */
    public static Step[] getJumpStep (String location, State state){
        Piece[] allDice = state.getCurrentBoard(); // all Dices of the board
        Location loc = new Location(location); // a location to find next valid jumping step
        List<Location> neighbors = new ArrayList<>(); // all neighbour locations valid for finding a jump step
        List<Step> steps = new ArrayList<>(); // a list to store all valid jumping step

        // get all neighbour valid location for jumping
        if (state.getTurn() == Player.WHITE){
            for (int dX = -2; dX <= 2; dX+=2) {
                for (int dY = 0; dY <= 2; dY+=2) { // white dices cannot move along the direction which y get smaller
                    int neighX = loc.getX() + dX;
                    int neighY = loc.getY() + dY;
                    if (neighX >= 1 && neighX <= 7 && neighY >= 1 && neighY <= 7) // the coordinate cannot be out of the board
                        neighbors.add(new Location(neighX,neighY));
                }
            }
        }else {
            for (int dX = -2; dX <= 2; dX+=2) {
                for (int dY = -2; dY <= 0; dY+=2) { // black dices cannot move along the direction which y get bigger
                    int neighX = loc.getX() + dX;
                    int neighY = loc.getY() + dY;
                    if (neighX >= 1 && neighX <= 7 && neighY >= 1 && neighY <= 7) // the coordinate cannot be out of the board
                        neighbors.add(new Location(neighX,neighY));
                }
            }
        }

        // find all valid jumping step for given location
        for (Location neigh : neighbors){
            String step = loc +neigh.toString();
            if(loc.isValidJump(neigh,allDice))
                steps.add(new Step(step));
        }
        return steps.toArray(new Step[0]);
    }


    /**
     * Given a location that a dice would move to
     * and find all possible jumping that the dice can reach for this location
     *
     * @param location the location to find all corresponding tipping steps
     * @param state The current game state
     * @return a array of all tipping steps for a dice with given location
     */
    public static Step[] getTipStep (String location, State state){

        Location loc = new Location(location); // a location to find next valid tipping step
        List<Location> neighbors = new ArrayList<>(); // all neighbour locations valid for finding a tip step
        List<Step> steps = new ArrayList<>(); // a list to store all valid tipping step
        String stateString = state.convertState(); // a string of current state

        // get all neighbour valid location for tipping
        if (state.getTurn() == Player.WHITE){
            for (int dX = -1; dX <= 1; dX++) {
                for (int dY = 0; dY <= 1; dY++) { // white dices cannot move along the direction which y get smaller
                    int neighX = loc.getX() + dX;
                    int neighY = loc.getY() + dY;
                    if (neighX >= 1 && neighX <= 7 && neighY >= 1 && neighY <= 7) // the coordinate cannot be out of the board
                        neighbors.add(new Location(neighX,neighY));
                }
            }
        }else {
            for (int dX = -1; dX <= 1; dX++) {
                for (int dY = -1; dY <= 0; dY++) { // black dices cannot move along the direction which y get bigger
                    int neighX = loc.getX() + dX;
                    int neighY = loc.getY() + dY;
                    if (neighX >= 1 && neighX <= 7 && neighY >= 1 && neighY <= 7) // the coordinate cannot be out of the board
                        neighbors.add(new Location(neighX, neighY));
                }
            }
        }

        // find all valid tipping step for given location
        for (Location neigh : neighbors){
            String step = loc + neigh.toString();
            if(Cublino.isValidStepPur(stateString,step))
                steps.add(new Step(step));
        }
        return steps.toArray(new Step[0]);

    }


    /**
     * Given a location that a dice would move to and current state
     * determine whether it has possible next jumping step
     *
     * @param location the location to find all corresponding jumping steps
     * @param state The current game state
     * @return true if there exists next jumping step, false if not
     */
    public static boolean hasJumpStep(String location,State state){
        return getJumpStep(location,state).length != 0;
        // if there are no jumping step, return false
    }


    /**
     * Given the current state and find all possible steps
     *
     * @param state The current game state
     * @return the list of all possible first moves of all dives of current player
     */
    public static Step[] findFirstSteps(String state){
        State gameState = new State(state);
        Piece[] movedDices = gameState.getPieces(); // find all dices of current player
        List<Step> fstSteps = new ArrayList<>(); // a list to store all valid first step for all pieces of current player

        if (gameState.getGameType() == PUR) {
            for (Piece movedDice : movedDices) {
                // get tipping steps and jumping steps
                Step[] fstStep = movedDice.getValidSteps(state, true, false);
                // add all first steps of this piece to the conclusion list
                fstSteps.addAll(Arrays.asList(fstStep));
            }
        }else {
            for (Piece movedDice : movedDices) {
                // get tipping steps
                Step[] fstStep = getTipStep(movedDice.getLoc().toString(), new State(state));
                fstSteps.addAll(Arrays.asList(fstStep));
            }
        }

        return fstSteps.toArray(new Step[0]);

    }


    /**
     * Recursive method to find all next steps given a starting step.
     *
     * @param step a step to find next steps
     * @param state current game state
     * @param move a move found so far
     * @param moves moves found so far
     */
    public static void findContinueSteps(Step step, State state, String move, List<String> moves){
        if (hasJumpStep(step.newLocation.toString(),state)) {
            // for the given step, find its all valid jump step
            Step[] jumpStep = getJumpStep(step.newLocation.toString(), state);

            // Only the first step may be a tipping step. Steps after the first step can only be jump step
            for (Step jump : jumpStep) {
                Move exist = new Move(move); // a move found so far
                // a move may not contain a jumping move followed by another jumping move back to the previous position
                if (!move.contains(jump.newLocation.toString())) {
                    // add the valid jump step to the move
                    Move found = exist.addStep(jump);

                    // every time find a move, add it to moves since we can choose move for a jump step or not
                    moves.add(found.toString());

                    // define the search depth
                    if (found.toString().length() > 10)
                        break;

                    String st = state.convertState();
                    // replace the location of the old dice by the new dice to avoid get a jump step by its own old occupation
                    String newSt = st.replaceAll(jump.oldLocation.toString(),jump.newLocation.toString());

                    // construct the new state
                    State newState = new State(newSt);

                    // a recursive action to find next valid step for this jump step
                    findContinueSteps(jump,newState,found.toString(),moves);
                }
            }
        }
    }


    /**
     * Given the current state and find all possible steps
     *
     * @param state The current game state
     * @param fstStep first steps each current player's dice can take
     * @return the list of all possible first moves of all dives of current player
     */
    public static List<String> getAllSteps (Step[] fstStep,State state){

        List<String> moves = new ArrayList<>(); // a list to store all valid moves we found
        String exist; // a string to store all valid steps that can construct a move we found for a staring step

        if (state.getGameType() == PUR) {
            for (Step step : fstStep) {
                // construct the simplest move of a starting step
                exist = step.toString();
                moves.add(step.toString());

                // change the game state, replace the old location to the new location getting from the step
                String st = state.convertState();
                String newSt = st.replaceAll(step.oldLocation.toString(), step.newLocation.toString());
                State newState = new State(newSt);

                // find all possible valid move for the first step
                findContinueSteps(step, newState, exist, moves);
            }
        }else{
            for (Step step : fstStep){
                exist = step.toString();
                moves.add(exist);
            }
        }

        return moves;
    }



    @Override
    public String toString() {
        return oldLocation.toString()+ newLocation.toString();
    }
}
