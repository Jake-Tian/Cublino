package comp1140.ass2;

import comp1140.ass2.gui.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Timeout(value = 1000, unit = MILLISECONDS)
public class createPieceTest {
    private static final String[] pieceEncodings = {
            "Ac2",
            "ae4",
            "Ba7",
            "bg4",
            "Cd2",
            "da5",
            "Ef3",
            "gg1",
            "Ib3",
            "kc4",
            "Md3",
            "oc6",
            "Re2",
            "tf7",
            "Wc1"
    };

    @Test
    public void testPieceColour() {
        Player[] expectedColours = {
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE,
                Player.BLACK,
                Player.WHITE
        };

        for (int i = 0; i < pieceEncodings.length; i++) {
            Piece piece = new Piece(pieceEncodings[i]);
            assertEquals(expectedColours[i], piece.getColour());
        }

    }

    @Test
    public void testPieceLocation() {
        // First digit x, second digit y
        int[] expectedLocations = {
                32,
                54,
                17,
                74,
                42,
                15,
                63,
                71,
                23,
                34,
                43,
                36,
                52,
                67,
                31
        };

        for (int i = 0; i < expectedLocations.length; i++) {
            // Create piece
            Piece piece = new Piece(pieceEncodings[i]);
            // Test x
            assertEquals(expectedLocations[i]%10, piece.getLoc().getX());
            // Test y
            assertEquals(expectedLocations[i]/10, piece.getLoc().getY());
        }
    }

    @Test
    public void testPieceOrientation() {
        int[] expectedNumUp = { 1, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6};
        int[] expectedNumNorth = { 2, 2, 3, 3, 4, 5, 1, 4, 1, 5, 1, 5, 3, 6, 4};
        int[] expectedNumEast = { 4, 4, 2, 2, 5, 3, 3, 1, 5, 6, 2, 1, 1, 3, 2};

        for (int i = 0; i < pieceEncodings.length; i++) {
            Piece piece = new Piece(pieceEncodings[i]);
            // Test if the number facing up matches
            assertEquals(expectedNumUp[i], piece.getNumUp());
            // Test if the number facing forward matches
            assertEquals(expectedNumNorth[i], piece.getNumNorth());
            // Test if the number facing right matches
            assertEquals(expectedNumEast[i], piece.getNumEast());
        }
    }

    @Test
    public void testPieceBadInput() {
        // Check that basic badly formatted pieces raise an error
        Assertions.assertThrows(AssertionError.class, () -> {
           Piece p = new Piece("");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("   ");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("a");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("a3");
        });

        // Check that characters which are incorrect values raise an error
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("3bd");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("yb3");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("xB3");
        });
        Assertions.assertThrows(AssertionError.class, () -> {
            Piece p = new Piece("Ec8");
        });
    }



    @Test
    public void getNumTest(){
        String initial = "PWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7";
        String move = "d1d2";
        String state2 ="pWa1Wb1Wc1We1Wf1Wg1Ld2va7vb7vc7vd7ve7vf7vg7";
        String move2 = "f7f6";
        String state3 = "PWa1Wb1Wc1We1Wf1Wg1Ld2if6va7vb7vc7vd7ve7vg7";
        String move3 = "e1e2c2";
        int num = 3;
        int num2 = 3;
        int num3 = 3;
        assertEquals(num,moveScore.getNum(initial,move),"1");
        assertEquals(num2,moveScore.getNum(state2,move2),"2");
        assertEquals(num3,moveScore.getNum(state3,move3),"3");



    }




    @Test
    public void convertStringTest(){
        Piece[] piece = new Piece[15];
        for (int i =0;i<15;i++){
            piece[i] = new Piece(pieceEncodings[i]);
        }
        for (int i = 0;i<15;i++){
            assertEquals(pieceEncodings[i],piece[i].convertString());
        }

    }
}
