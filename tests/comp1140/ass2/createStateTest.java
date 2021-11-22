package comp1140.ass2;


import comp1140.ass2.gui.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static comp1140.ass2.State.getTurn;
import static comp1140.ass2.gui.Player.BLACK;
import static comp1140.ass2.gui.Player.WHITE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@Timeout(value = 1000, unit = MILLISECONDS)
public class createStateTest {
    private static final String[] p2wins = {
            // finished game with winner Player2 and Player1's turn
            "Pna1jb1lc1re1rf1ng1fa2Ga7Vb7Fc7Ad7Oe7Gf7Bg7",
            "Poa1rb1rc1bd1ve1dg1wb2Ga7Fb7Gc7Gd7Be7Uf7Mg7",
            "Pwa1eb1lc1nd1be1if1cg1Tg5Ic6Fa7Db7Gd7Wf7Ug7",
            "Pva1ob1bc1dd1ee1bf1rg1Td6Db7Bc7Hd7Ae7Df7Ug7",
            "Pfa1cb1vc1ad1we1sf1ug1Pa6Hc6Wa7Fb7Fe7Ff7Rg7",

    };

    private static final String[] p1wins = {
            // finished game with winner Player1 and Player2's turn
            "poa1cb1fc1gd1we1vg1dd2Ua7Bb7Tc7Dd7Ke7Gf7Sg7",
            "pca1cb1dc1we1lf1sg1eb2Ra7Kb7Vc7Ed7Ce7Ff7Kg7",
            "pgb1rc1ud1le1sf1og1ec2Na7Xb7Gc7Ud7Fe7Uf7Gg7",
            "pga1xb1gc1dd1sf1bg1ib2Ea7Gb7Kc7Md7Pe7Wf7Ag7",
            "psa1tb1cc1fd1pf1rg1hf2Ra7Ub7Nc7Cd7Ve7Nf7Jg7",
            "pxa1rb1nc1wd1jf1bg1xe2Aa7Xb7Oc7Wd7Qe7Af7Sg7"
    };

    // finished games with a darw
    private static final String[] drawGames = {
            "Pia1eb1cc1fd1ce1hf1kg1Rf6Ba7Pb7Uc7Ce7Af7Dg7",
            "pga1cb1dc1gd1if1vg1jb2Da7Vb7Dc7Fd7He7Df7Eg7",
            "Pca1xb1ac1hd1ke1vf1vg1Df6Ma7Dc7Gd7We7Xf7Wg7",
            "pnc1ud1ge1wf1xg1ed2xe2Ea7Sb7Jc7Kd7Ke7Vf7Gg7",
            "Pga1gb1pc1fd1ue1bg1jb2Aa7Hb7Jc7Dd7Be7Kf7Vg7"
    };


    // unfinished game states with Player1's turn
    private static final String[] unfinishedStates1 = {
            "PWa1Wb1Wc1Wd1We1Lf2Lg2ia6ig6vb7vc7vd7ve7vf7",
            "PWa1Wb1Wc1Wd1We1Le2Lf2ia6ib6ig6vc7vd7ve7vf7",
            "PWa1Wb1Wc1Wd1We1Ld2Le2ia6ib6ig6vc7vd7ve7vf7",
            "PWb1Gc1We1La2Ld2Le2Wa3ia6ib6ie6ig6vc7vd7vf7",
            "PWb1Gc1We1La2Ld2Le2Wa3ia6ib6ie6if6ig6vc7vf7",
    };


    // unfinished game states with Player2's turn
    private static final String[] unfinishedStates2 = {
            "pGc1Wd1We1La2Ld2Le2Wa3ia6ib6ie6if6ig6vc7vf7",
            "pGc1Wd1We1La2Ld2Le2Wa3ia6ib6id6ie6ig6vc7vf7",
            "pGc1We1La2Le2Lf2Wa3Wd3re5ia6ib6id6ef6ig6vc7",
            "pGc1Sf1La2Le2Lf2Wa3Wd3re5ia6ib6id6ef6ig6vc7",
            "pGc1We1La2Lf2Wa3Be3ee4Lf4re5ia6ib6qd6ig6vc7"
    };


    @Test
    public void getTurnTest(){
        for (String p2 : p1wins){
            assertEquals(getTurn(p2), Player.BLACK,"current turn is Player1");
        }

        for (String p1 : p2wins){
            assertEquals(getTurn(p1), WHITE,"current turn is Player2");
        }

        for (String p1 : unfinishedStates1){
            assertEquals(getTurn(p1), WHITE,"current turn is Player2");
        }

        for (String p2 : unfinishedStates2){
            assertEquals(getTurn(p2), Player.BLACK,"current turn is Player1");
        }

    }


    @Test
    public void getPiecesTest(){
        String st = "pGc1Wd1We1La2Ld2Le2Wa3ia6ib6ie6if6ig6vc7vf7";
        String piece = "ia6ib6ie6if6ig6vc7vf7";
        int index = 0;
        State s = new State(st);
        Piece [] p = s.getPieces();
        Piece [] ps = new Piece[p.length];
        for (int i = 0;i< piece.length();i+=3){
            String str = piece.substring(i,i+3);
            ps[index] = new Piece(str);
            index++;
        }

        for (int i =0;i<p.length;i++){
            String s1 = p[i].convertString();
            String s2 = ps[i].convertString();
            assertEquals(s1,s2,"except "+s1+" but got "+s2);
        }

    }



    @Test
    public void isGameOverTest(){
        for (String p1 : p1wins){
            State board = new State (p1);
            assertTrue(State.isGameOver(board),"The game is over");
        }
        for (String p2 : p2wins){
            State board = new State (p2);
            assertTrue(State.isGameOver(board),"The game is over");
        }
        for (String p : unfinishedStates1){
            State board = new State (p);
            assertFalse(State.isGameOver(board),"The game is unfinished");
        }
        for (String p : unfinishedStates2){
            State board = new State (p);
            assertFalse(State.isGameOver(board),"The game is unfinished");
        }

    }

    @Test
    public void getResultsTest(){
        for (String p1 : p1wins){
            Piece[] board = new State(p1).getCurrentBoard();
            assertTrue(State.getResults(WHITE,board) > State.getResults(BLACK,board),"Player1 wins");
        }
        for (String p2 : p2wins){
            Piece[] board = new State(p2).getCurrentBoard();
            assertTrue(State.getResults(WHITE,board) < State.getResults(BLACK,board),"Player2 wins");
        }
        for (String p : drawGames){
            Piece[] board = new State(p).getCurrentBoard();
            assertEquals(State.getResults(WHITE,board),State.getResults(BLACK,board),"It is draw");
        }

    }


}
