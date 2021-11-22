package comp1140.ass2;

import comp1140.ass2.gui.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 1000, unit = MILLISECONDS)
public class testLocation {

    private void equalTest(Location in, Location other, boolean expected) {
        boolean out = in.equals(other);
        assertEquals(expected, out, "Expected equals called on Location (" + in.getX() +
                ", " + in.getY() + ") and Location (" + other.getX() + ", " +
                + other.getY() + ") to return " + expected +
                ", but got " + out);
    }


    @Test
    public void testLocationEquals() {

        for (int x1 = 0; x1 < 7; x1++) {
            for (int x2 = 0; x2 < 7; x2++) {
                for (int y1 = 0; y1 < 7; y1++) {
                    for (int y2 = 0; y2 < 7; y2++) {
                        equalTest(new Location(x1, y1), new Location(x2, y2), x1==x2 && y1==y2);
                    }
                }
            }
        }
    }


    private static final Piece[] stateA = {
            new Piece(Player.WHITE, new Location(1, 0), 6, 5, 1),
            new Piece(Player.WHITE, new Location(2, 0), 6, 5, 1),
            new Piece(Player.WHITE, new Location(3, 0), 6, 5, 1),
            new Piece(Player.WHITE, new Location(6, 0), 6, 5, 1),
            new Piece(Player.WHITE, new Location(3, 1), 6, 5, 1),
            new Piece(Player.WHITE, new Location(3, 3), 6, 5, 1),
            new Piece(Player.WHITE, new Location(5, 4), 6, 5, 1),
            new Piece(Player.BLACK, new Location(1, 6), 6, 5, 1),
            new Piece(Player.BLACK, new Location(3, 6), 6, 5, 1),
            new Piece(Player.BLACK, new Location(5, 6), 6, 5, 1),
            new Piece(Player.BLACK, new Location(6, 6), 6, 5, 1),
            new Piece(Player.BLACK, new Location(2, 5), 6, 5, 1),
            new Piece(Player.BLACK, new Location(4, 5), 6, 5, 1),
            new Piece(Player.BLACK, new Location(5, 2), 6, 5, 1)};


    @Test
    public void testDistanceTwo() {
        Location[] locationsA = {new Location(2,6), new Location(5,3),
                                new Location(3,2), new Location(4,1),
                                new Location(0,5), new Location(2,1),
                                new Location(6,5), new Location(5,3)};
        Location[] locationsB = {new Location(2,3), new Location(5,0),
                                new Location(3,5),new Location(4,6),
                                new Location(3,5), new Location(5,1),
                                new Location(3,5), new Location(0,3)};

        for (int i=0; i<7; i++) {
            assertFalse(locationsA[i].isValidJump(locationsB[i], stateA), "Location " + locationsA[i] + " to "
            + locationsB[i] + " is an invalid jump");
        }
    }

    @Test
    public void testJumpingPoint() {
        Location[] locationsA = {new Location(1,6), new Location(6,0),
                                new Location(2,5), new Location(1,0),
                                new Location(2,5), new Location(3,0),
                                new Location(6,0), new Location(5,2)};
        Location[] locationsB = {new Location(1,4), new Location(6,2),
                                new Location(2,3),new Location(1,2),
                                new Location(0,5), new Location(5,0),
                                new Location(4,0), new Location(3,2)};

        for (int i=0; i<7; i++) {
            assertFalse(locationsA[i].isValidJump(locationsB[i], stateA), "Location " + locationsA[i] + " to "
                    + locationsB[i] + " is an invalid jump");
        }
    }

    @Test
    public void destinationNotTaken() {
        Location[] locationsA = {new Location(1,3), new Location(3,2),
                                new Location(4,6), new Location(2,3),
                                new Location(1,3), new Location(3,2),
                                new Location(3,1), new Location(1,2)};
        Location[] locationsB = {new Location(3,1), new Location(3,0),
                                new Location(6,6),new Location(2,5),
                                new Location(3,3), new Location(3,0),
                                new Location(1,3), new Location(1,0)};

        for (int i=0; i<7; i++) {
            assertFalse(locationsA[i].isValidJump(locationsB[i], stateA), "Location " + locationsA[i] + " to "
                    + locationsB[i] + " is an invalid jump");
        }
    }

    @Test
    public void locationToString() {
        Location[] locations = {new Location(1,3), new Location(3,2),
                new Location(4,6), new Location(2,3),
                new Location(7,3), new Location(3,7),
                new Location(2,5), new Location(6,7)};

        String[] expected = {
                "a3", "c2", "d6", "b3", "g3", "c7", "b5", "f7"
        };

        for (int i=0; i < locations.length; i++) {
            assertEquals(expected[i], locations[i].toString());
        }
    }
}
