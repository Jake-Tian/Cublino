package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Timeout(value = 1000, unit = MILLISECONDS)
public class moveClassTest {

    private String errorPrefix(String move,String step) {
        return "Step " + step + " Move " + move
                + System.lineSeparator()
                + "returned \"" + move + "\";"
                + System.lineSeparator();
    }


    @Test
    public void toStringTest(){

        String[][] steps = {
                new String[] {"b1d1","d1d3","d3b3"}
                ,new String[] {"a3b3","b3d3","d3d1","d1b1"}
                ,new String[] {"a3b3","b3d3"}
                ,new String[] {"a3b3","b3d3","d3d1"}
                ,new String[] {"a2a3","a3a5"}
        };

        String[] moves = {
                "b1d1d3b3",
                "a3b3d3d1b1",
                "a3b3d3",
                "a3b3d3d1",
                "a2a3a5",
        };

        for (int i = 0; i< steps.length;i++){
            List<Step> step = new ArrayList<>();
            for (String s : steps[i]){
                Step ss = new Step(s);
                step.add(ss);
            }

            Move move = new Move(step);
            String m = move.toString();

            assertEquals(moves[i],m,"except Move "+moves[i]+ " but got Move "+m);
        }


    }




    @Test
    public void convertMoveTest(){

        String[] moves = {
                "b1d1d3b3",
                "a3b3d3d1b1",
                "a3b3d3",
                "a3b3d3d1",
                "a2a3a5",
        };


        String[][] steps = {
                 new String[] {"b1d1","d1d3","d3b3"}
                ,new String[] {"a3b3","b3d3","d3d1","d1b1"}
                ,new String[] {"a3b3","b3d3"}
                ,new String[] {"a3b3","b3d3","d3d1"}
                ,new String[] {"a2a3","a3a5"}
        };

        for (int i = 0; i< moves.length;i++){
            Move m = new Move(moves[i]);
            Step[] s = m.getSteps();
            Set<String> legalSteps =  new HashSet<>(Arrays.asList(steps[i]));
            for (Step step : s){
                String s1 = step.toString();
                assertTrue(legalSteps.contains(s1), errorPrefix(moves[i],s1) + "expected a move from \n" + Arrays.toString(steps[i]));
            }

        }

    }




    @Test
    public void countDiceTest(){
        String[] testState = {
                  "pgb1vc1cd1rf1pg1ea2gg2Ka6Oa7Wb7Vc7Ud7Ce7Rg7"
                , "Pgb1vc1cd1pe1rf1ea2gg2Ka6Oa7Wb7Vc7Ud7Ce7Rg7"
                , "pgb1vc1cd1pe1rf1ea2gg2Ka6Oa7Wb7Vc7Ud7Ce7Bf7"
                , "Pda1gb1vc1cd1pe1rf1gg2Ka6Oa7Wb7Vc7Ud7Ce7Bf7"
                , "pda1gb1vc1cd1pe1rf1gg2Ka6Oa7Wb7Vc7Ud7Ce7Rg7"
        };

        int[] testNum = {
                7,
                7,
                7,
                7,
                7
        };
        for (int i = 0;i<5;i++){
            assertEquals(testNum[i],moveScore.countDice(testState[i]),"not same" + i);
        }

    }
}
