package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;

import static comp1140.ass2.ExampleGames.FULL_GAME_WITH_MOVES_MOVES;
import static comp1140.ass2.ExampleGames.FULL_PUR_GAME_WITH_MOVES_STATES;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Timeout(value = 1000, unit = MILLISECONDS)
public class stepClassTest {

    public static final String[] state = {
            "PWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7"
            , "pWb1Wc1Wd1We1Wf1Wg1La2va7vb7vc7vd7ve7vf7vg7"
            , "PWb1Wc1Wd1We1Wf1Wg1La2ia6vb7vc7vd7ve7vf7vg7"
            , "pWb1Wc1We1Wf1Wg1La2Ld2ia6vb7vc7vd7ve7vf7vg7"
            , "PWb1Wc1We1Wf1Wg1La2Ld2ia6ra7vc7vd7ve7vf7vg7"
            , "pWb1Wc1We1Wf1Wg1Tb2Ld2ia6ra7vc7vd7ve7vf7vg7"
            , "PWb1Wc1We1Wf1Wg1Tb2Ld2ra5ia6vc7vd7ve7vf7vg7"
            , "pWb1Wc1We1Wg1Tb2Ld2Wd3ra5ia6vc7vd7ve7vf7vg7"
            , "PWb1Wc1We1Wg1Tb2Ld2Wd3ja4ia6vc7vd7ve7vf7vg7"
            , "pWb1Wc1Wg1La2Tb2Ld2Wd3ja4ia6vc7vd7ve7vf7vg7"
            , "PWb1Wc1Wg1La2Tb2Ld2Wd3ja4ia6ie6vc7vd7vf7vg7"
            , "pWb1Wc1Wg1La2Tb2Ld2Gc3ja4ia6ie6vc7vd7vf7vg7"
            , "PWb1Wc1Wg1La2Tb2Ld2ga3Gc3ia6ie6vc7vd7vf7vg7"
            , "pWb1Wc1Wg1La2Pc2Ld2ga3Gc3ia6ie6vc7vd7vf7vg7"
            , "PWb1Wc1Wg1La2Pc2Ld2ga3Gc3ia6ic6ie6vd7vf7vg7"
            , "pWb1Wc1La2Pc2Ld2Lg2ga3Gc3ia6ic6ie6vd7vf7vg7"
            , "PWb1Wc1La2Pc2Ld2Lg2ga3Gc3ia6ed6ie6vd7vf7vg7"
            , "pWc1Wd1La2Pc2Ld2Lg2ga3Gc3ia6ed6ie6vd7vf7vg7"
            , "PWc1Wd1La2Pc2Ld2Lg2ga3Gc3ia6ed6ie6fe7vf7vg7"
            , "pWc1Se1La2Pc2Ld2Lg2ga3Gc3ia6ed6ie6fe7vf7vg7"
    };

    public static final String[][] fstStep = {
            new String[]{"a1a2", "b1b2", "c1c2", "d1d2", "e1e2", "f1f2", "g1g2"}
            , new String[]{"a7a6", "b7b6", "c7c6", "d7d6", "e7e6", "f7f6", "g7g6"}
            , new String[]{"a2a3", "a2b2", "b1b2", "b1a1a3", "b1a1", "c1c2", "c1a1a3", "c1a1", "d1d2", "e1e2", "f1f2", "g1g2"}
            , new String[]{"a6a5", "a6b6", "b7b6", "b7a7a5", "b7a7", "c7c6", "c7a7a5", "c7a7", "d7d6", "e7e6", "f7f6", "g7g6"}
            , new String[]{"a2a3", "a2b2", "b1b2", "b1a1a3", "b1a1", "b1d1d3", "b1d1", "c1c2e2", "c1c2", "c1a1a3", "c1a1", "c1d1d3", "c1d1", "d2d3", "d2c2", "d2e2", "e1e2c2", "e1e2", "e1d1d3", "e1d1", "f1f2", "f1d1d3", "f1d1", "g1g2"}
            , new String[]{"a6a5", "a6b6", "a7a5", "a7b7", "c7c6", "c7b7", "d7d6", "d7b7", "e7e6", "f7f6", "g7g6"}
            , new String[]{"b1b3", "b1a1", "b1d1d3", "b1d1", "b2b3", "b2a2", "b2c2e2", "b2c2", "c1c2a2", "c1c2e2", "c1c2", "c1a1", "c1d1d3", "c1d1", "d2d3", "d2c2a2", "d2c2", "d2e2", "e1e2c2a2", "e1e2c2", "e1e2", "e1d1d3", "e1d1", "f1f2", "f1d1d3", "f1d1", "g1g2"}
            , new String[]{"a5a4", "a5b5", "a6a4", "a6b6", "c7c6", "c7b7", "d7d6", "d7b7", "e7e6", "f7f6", "g7g6"}
            , new String[]{"b1b3", "b1a1", "b1d1f1", "b1d1", "b2b3", "b2a2", "b2c2e2", "b2c2", "c1c2a2", "c1c2e2", "c1c2", "c1a1", "c1d1f1", "c1d1", "d2d4", "d2c2a2", "d2c2", "d2e2", "d3d4", "d3c3", "d3e3", "e1e2c2a2", "e1e2c2", "e1e2", "e1d1", "e1f1", "g1g2", "g1f1d1", "g1f1"}
            , new String[]{"a4a3a1", "a4a3", "a4b4", "a6a5a3a1", "a6a5a3", "a6a5", "a6b6", "c7c6", "c7b7", "d7d6", "d7b7", "e7e6", "f7f6", "g7g6"}
            , new String[]{"a2a3a5a7", "a2a3a5", "a2a3", "a2c2e2", "a2c2", "b1b3", "b1a1a3a5a7", "b1a1a3a5", "b1a1a3", "b1a1", "b1d1", "b2b3", "b2c2e2", "b2c2", "c1c2e2", "c1c2", "c1a1a3a5a7", "c1a1a3a5", "c1a1a3", "c1a1", "c1d1", "d2d4", "d2c2", "d2e2", "d3d4", "d3c3", "d3e3", "g1g2", "g1f1"}
            , new String[]{"a4a3a1", "a4a3", "a4b4", "a6a5a3a1", "a6a5a3", "a6a5", "a6b6", "c7c6", "c7b7", "c7e7e5", "c7e7", "d7d6f6", "d7d6", "d7b7", "d7e7e5", "d7e7", "e6e5", "e6d6", "e6f6", "f7f6d6", "f7f6", "f7e7e5", "f7e7", "g7g6", "g7e7e5", "g7e7"}
            , new String[]{"a2a4", "a2c2c4", "a2c2e2", "a2c2", "b1b3d3", "b1b3", "b1a1", "b1d1d3b3", "b1d1d3", "b1d1", "b2b3d3", "b2b3", "b2c2c4", "b2c2e2", "b2c2", "c1c2c4", "c1c2e2", "c1c2", "c1a1", "c1d1d3b3", "c1d1d3", "c1d1", "c3c4", "c3b3", "c3d3", "d2d3b3", "d2d3", "d2c2c4", "d2c2", "d2e2", "g1g2", "g1f1"}
            , new String[]{"a3a1", "a3b3d3d1", "a3b3d3", "a3b3", "a6a5", "a6b6", "c7c6", "c7b7", "c7e7e5", "c7e7", "d7d6f6", "d7d6", "d7b7", "d7e7e5", "d7e7", "e6e5", "e6d6", "e6f6", "f7f6d6", "f7f6", "f7e7e5", "f7e7", "g7g6", "g7e7e5", "g7e7"}
            , new String[]{"a2a4", "a2b2", "b1b2", "b1a1", "b1d1d3b3", "b1d1d3", "b1d1", "c1a1", "c1d1d3b3", "c1d1d3", "c1d1", "c2c4", "c2b2", "c2e2", "c3c4", "c3b3", "c3d3", "d2d3b3", "d2d3", "d2b2", "d2e2", "g1g2", "g1f1"}
            , new String[]{"a3a1", "a3b3d3d1", "a3b3d3", "a3b3", "a6a5", "a6b6d6f6", "a6b6d6", "a6b6", "c6c5", "c6b6", "c6d6f6", "c6d6", "d7d6b6", "d7d6f6", "d7d6", "d7c7c5", "d7c7", "d7e7e5", "d7e7", "e6e5", "e6d6b6", "e6d6", "e6f6", "f7f6d6b6", "f7f6d6", "f7f6", "f7e7e5", "f7e7c7c5", "f7e7c7", "f7e7", "g7g6", "g7e7e5", "g7e7c7c5", "g7e7c7", "g7e7"}
            , new String[]{"a2a4", "a2b2", "b1b2", "b1a1", "b1d1d3b3", "b1d1d3", "b1d1", "c1a1", "c1d1d3b3", "c1d1d3", "c1d1", "c2c4", "c2b2", "c2e2", "c3c4", "c3b3", "c3d3", "d2d3b3", "d2d3", "d2b2", "d2e2", "g2g3", "g2f2"}
            , new String[]{"a3a1", "a3b3d3", "a3b3", "a6a5", "a6b6", "d6d5", "d6c6", "d6f6", "d7d5", "d7c7", "d7e7e5", "d7e7", "e6e5", "e6c6", "e6f6", "f7f6", "f7e7e5", "f7e7c7", "f7e7", "g7g6", "g7e7e5", "g7e7c7", "g7e7"}
            , new String[]{"a2a4", "a2b2", "c1b1", "c1e1", "c2c4", "c2b2", "c2e2", "c3c4", "c3b3", "c3d3", "d1d3b3", "d1d3", "d1b1", "d1e1", "d2d3b3", "d2d3", "d2b2", "d2e2", "g2g3", "g2f2"}
            , new String[]{"a3a1", "a3b3d3d1b1", "a3b3d3d1f1", "a3b3d3d1", "a3b3d3", "a3b3", "a6a5", "a6b6", "d6d5", "d6c6", "d6f6", "e6e5", "e6c6", "e6f6", "e7e5", "e7d7d5", "e7d7", "f7f6", "f7d7d5", "f7d7", "g7g6"}

    };



    private String errorPrefix(String inputState, String move) {
        return "Step.findFirstStep(\"" + inputState + "\")"
                + System.lineSeparator()
                + "returned \"" + move + "\";"
                + System.lineSeparator();
    }




    @Test
    public void findFirstStepTest() {


        for (int i = 0; i < state.length; i++) {
            Step[] moves = Step.findFirstSteps(state[i]);
            Set<String> legalMoves = new HashSet<>(Arrays.asList(fstStep[i]));
            if (moves.length > 0) {
                for (Step m : moves) {
                    String move = m.toString();
                    assertTrue(legalMoves.contains(move), errorPrefix(state[i], move) + "expected a move from \n" + Arrays.toString(fstStep[i]));
                }
            }else
                System.out.println("no steps");
        }
    }



    @Test
    public void stepConvertStringTest(){

        String[] step = {
                "b1d1", "d1d3", "d3b3",
                "a3b3", "b3d3", "d3d1",
                "d1b1", "a3b3", "b3d3",
                "a3b3", "b3d3", "d3d1",
                "a2a3", "a3a5"
        };

        Step[] steps = {
                new Step("b1d1"),
                new Step("d1d3"),
                new Step("d3b3"),
                new Step("a3b3"),
                new Step("b3d3"),
                new Step("d3d1"),
                new Step("d1b1"),
                new Step("a3b3"),
                new Step("b3d3"),
                new Step("a3b3"),
                new Step("b3d3"),
                new Step("d3d1"),
                new Step("a2a3"),
                new Step("a3a5")

        };

        for (int i = 0;i<steps.length;i++){
            assertEquals(step[i],steps[i].toString(),"Except step " + step[i] + " but got step " + steps[i].toString() );
        }

    }


    @Test
    public void addStep(){
        for (int i = 0;i<state.length;i++) {
            List<String> s = new ArrayList<>();
            Step[] firstSteps = Step.findFirstSteps(state[i]);

            for (Step step : firstSteps) {
                String st = step.toString();
                s.add(st);
            }

            for (int j = 0;j<fstStep[i].length;j++){
                assertEquals(fstStep[i][j],s.get(j));
            }
        }
    }




    private String errorFix(String inputState, String move) {
        return "Step.getAllSteps(\"" + inputState + "\")"
                + System.lineSeparator()
                + "returned \"" + move + "\";"
                + System.lineSeparator();
    }
    @Test
    public void getAllStepTest(){

        for (int i = 0; i < FULL_PUR_GAME_WITH_MOVES_STATES.length ; i++ ) {
            Step[] steps = Step.findFirstSteps(FULL_PUR_GAME_WITH_MOVES_STATES[i]);
            List<String> found = Step.getAllSteps(steps, new State(FULL_PUR_GAME_WITH_MOVES_STATES[i]));
            Set<String> legalMoves = new HashSet<>(Arrays.asList(FULL_GAME_WITH_MOVES_MOVES[i]));
            if (found.size() > 0) {
                for (String step : found) {
                    assertTrue(legalMoves.contains(step),
                            errorFix(FULL_PUR_GAME_WITH_MOVES_STATES[i], step) +
                                    "expected a move from \n" + Arrays.toString(FULL_GAME_WITH_MOVES_MOVES[i]));
                }
            }else
                System.out.println("no moves");

        }

    }




}
