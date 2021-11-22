package comp1140.ass2;

import comp1140.ass2.gui.Player;


import java.util.ArrayList;

public class Piece {
    // Colour of the piece
    private Player colour;
    // Location of the piece
    private Location loc;

    // The number pointing up
    private int numUp;
    // The number pointing forwards (NORTH)
    private int numNorth;
    // The number pointing right (EAST)
    private int numEast;

    /**
     * Create piece from attributes
     * @param colour The colour of the piece
     * @param loc The location of the piece
     * @param numUp The number facing up
     * @param numNorth The number facing forwards
     * @param numEast The number facing right
     */
    public Piece (Player colour, Location loc, int numUp, int numNorth, int numEast) {
        this.colour = colour;
        this.loc = loc;
        this.numUp = numUp;
        this.numNorth = numNorth;
        this.numEast = numEast;
    }

    /**
     * Create piece from encoding string
     * @author James Noonan
     * @param piece The encoding string
     */
    public Piece (String piece) {
        // Assert that encoding is of right length
        assert(piece.length() == 3);

        char pieceEncoding = piece.charAt(0);
        char columnEncoding = piece.charAt(1);
        String rowEncoding = piece.substring(2,3);

        // Assert that each character is within the correct range
        assert(pieceEncoding >= 65 && pieceEncoding <= 88 || pieceEncoding >= 97 && pieceEncoding <= 120);
        assert(columnEncoding >= 97 && columnEncoding <= 103);
        assert(piece.charAt(2) >= 49 && piece.charAt(2) <= 55);

        // Set location of the piece
        int x = columnEncoding - 96;
        int y = Integer.parseInt(rowEncoding);

        this.loc = new Location(x,y);

        // Choose colour based on upper or lower case
        this.colour = (pieceEncoding >= 97) ? Player.BLACK : Player.WHITE;
        // Get the number of the piece in the alphabet
        int orientation = (pieceEncoding >= 97) ? pieceEncoding - 97 : pieceEncoding - 65;
        // Each number on top has four associated letters
        int numOnTop = (orientation / 4) + 1;
        // Find the number facing forward
        int numForwardIndex = (orientation % 4) + 1;
        int numForward = 0;
        for (int i = 0; i < numForwardIndex; i++) {
            numForward++;
            while (numForward == numOnTop || numForward == (7 - numOnTop)) {
                numForward++;
            }
        }

        // Set sides of dice
        this.numUp = numOnTop;
        this.numNorth = numForward;
        this.numEast = getRightSide(numOnTop, numForward);
    }

    /**
     * For current game state, find it corresponding string
     *
     * @author Yinsi Zhou
     * @return string of current state
     */
    public String convertString(){
            String row = "" + this.getLoc().getY();
            String column = "" + (char) (this.getLoc().getX() + 96);
            if (this.colour == Player.WHITE)
                return this.convertOrientation().toUpperCase() + column + row;
            else
                return this.convertOrientation() + column + row;

    }

    /**
     * Find the corresponding string of the orientation
     *
     * @author Yinsi Zhou
     * @return string of orientation
     */
    public  String convertOrientation(){
        switch (this.numUp) {
            case 1:
                return ""+(char)(97-2+this.numNorth);
            case 2:
                switch (this.numNorth) {
                    case 1:
                        return "e";
                    case 3:
                        return "f";
                    case 4:
                        return "g";
                    case 6:
                        return "h";

                }
            case 3:
                switch (this.numNorth) {
                    case 1:
                        return "i";
                    case 2:
                        return "j";
                    case 5:
                        return "k";
                    case 6:
                        return "l";
                }
            case 4:
                switch (this.numNorth) {
                    case 1:
                        return "m";
                    case 2:
                        return "n";
                    case 5:
                        return "o";
                    case 6:
                        return "p";
                }
            case 5:
                switch (this.numNorth) {
                    case 1:
                        return "q";
                    case 3:
                        return "r";
                    case 4:
                        return "s";
                    case 6:
                        return "t";
                }
            case 6:
                return ""+(char)(117-2+this.numNorth);
        }
        return null;
    }


    /**
     * Get the third side of the dice based on the other two
     * @author James Noonan
     * @param numOnTop The number on top of the dice
     * @param numForward The number at the front of the dice
     * @return The number on the right side of the dice
     */
    private int getRightSide(int numOnTop, int numForward) {
        // The smaller number of left and right
        int smallNumSide = 6 - (numOnTop >= 4 ? 7 - numOnTop : numOnTop) - (numForward >= 4 ? 7 - numForward : numForward);
        // The four numbers surrounding the top face
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            if (i != numOnTop && i != (7 - numOnTop)) {
                nums.add(i);
            }
        }
        // From experimentation with physical die, ordering follows this pattern:
        // (top, forward) -> rightIsSmallSide
        // 1,2 -> false | 1,3 -> true | 1,4 -> false | 1,5 -> true
        // 2,1 -> true | 2,3 -> false | 2,4 -> true | 2,6 -> false
        //...
        boolean rightIsSmallSide = (numOnTop % 2 == 0) == (nums.indexOf(numForward) % 2 == 0);
        return rightIsSmallSide ? smallNumSide : 7 - smallNumSide;
    }

    /**
     *
     * Calculates the bottom on the opposite of the die
     * @param num The number on the given side
     * @return The number on the opposite of the die
     */
    public static int getNumOpposite(int num) {
        return 7-num;
    }

    /**
     * Finds the valid steps on the board for this piece
     * @author James Noonan
     * @param boardState The current state of the board
     * @param allowTip Are tipping steps allowed?
     * @param isContra Are only tipping steps allowed?
     * @return An array of valid steps for this piece
     */
    public Step[] getValidSteps(String boardState, boolean allowTip, boolean isContra) {
        ArrayList<Step> steps = new ArrayList<>();
        // Starting point of step
        String oldLocation = this.loc.toString();
        int minX = Math.max(this.loc.getX() - 2, 1);
        int maxX = Math.min(this.loc.getX() + 2, 7);
        int minY = Math.max(this.loc.getY() - 2, 1);
        int maxY = Math.min(this.loc.getY() + 2, 7);
        // Loop through each position on board
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                // Construct the step encoding
                String newLocation = "" + (char) (i+96) + "" + j;
                String stepEncoding = oldLocation + newLocation;
                boolean isValid = isContra ? Cublino.isValidStepContra(boardState, stepEncoding): Cublino.isValidStepPur(boardState, stepEncoding);
                // If it is a valid step, then add to arraylist
                if (isValid) {
                    boolean allowed = allowTip || this.loc.getDistance(new Location(newLocation)) == 2;
                    if (allowed) {
                        Step step = new Step(this.loc, new Location(i,j));
                        steps.add(step);
                    }
                }
            }
        }
        // Convert and return array of steps
        return steps.toArray(new Step[0]);
    }

    /**
     * Tip the piece in the specified direction
     * @param dir The direction to tip the piece
     */
    public void tipPiece(Direction dir) {
        if (dir == Direction.NORTH) {
            int tempNorth = this.numNorth;
            this.numNorth = this.numUp;
            this.numUp = 7 - tempNorth;
        } else if (dir == Direction.EAST) {
            int tempEast = this.numEast;
            this.numEast = this.numUp;
            this.numUp = 7 - tempEast;
        } else if (dir == Direction.SOUTH) {
            int tempNorth = this.numNorth;
            this.numNorth = 7 - this.numUp;
            this.numUp = tempNorth;
        } else if (dir == Direction.WEST) {
            int tempEast = this.numEast;
            this.numEast = 7 - this.numUp;
            this.numUp = tempEast;
        }
    }


    // Getters and Setters

    public Location getLoc() {
        return loc;
    }

    public Player getColour() {
        return colour;
    }

    public int getNumUp() {
        return numUp;
    }

    public int getNumNorth() {
        return numNorth;
    }

    public int getNumEast() {
        return numEast;
    }

    public void setColour(Player colour) {
        this.colour = colour;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setNumUp(int numUp) {
        this.numUp = numUp;
    }

    public void setNumNorth(int numNorth) { this.numNorth = numNorth; }

    public void setNumEast(int numEast) {
        this.numEast = numEast;
    }
}
