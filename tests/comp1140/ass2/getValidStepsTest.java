package comp1140.ass2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Timeout(value = 1000, unit = MILLISECONDS)
public class getValidStepsTest {
    @Test
    public void testVariousValidSteps() {
        String[] stateData = {
                "PWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7",
                "pWa1Wb1Wc1Wd1We1Wf1Wg1va7vb7vc7vd7ve7vf7vg7",
                "PWa1Wb1Wc1We1Wf1Wg1Ld2if6va7vb7vc7vd7ve7vg7",
                "PCe1Kc2pd2fd3Ge3Rg3ia4Lc4Td4qe4Gb5rf5cg5if6"
        };
        String[] pieceData = {
                "Wa1",
                "Vc7",
                "Wb1",
                "Ge3"
        };
        String[][] expectedStepData = {
                {"a1a2"},
                {"c7c6"},
                {"b1b2", "b1d1"},
                {"e3e5", "e3c3", "e3f3"},
        };
        for (int i = 0; i < pieceData.length; i++) {
            String state = stateData[i];
            Piece piece = new Piece(pieceData[i]);
            Step[] validSteps = piece.getValidSteps(state, true, false);
            String[] expectedSteps = expectedStepData[i];

            // Make sure created same number
            assertEquals(expectedSteps.length, validSteps.length, "Produced a different number of steps");

            // Check method produces valid steps
            for (Step validStep : validSteps) {
                assertTrue(Arrays.asList(expectedSteps).contains(validStep.toString()), "Produced a step that was not valid");
            }

            // Check method produces all valid steps
            for (String expectedStep : expectedSteps) {
                boolean stepWasProduced = false;
                for (Step validStep : validSteps) {
                    if (expectedStep.equals(validStep.toString())) {
                        stepWasProduced = true;
                    }
                }
                assertTrue(stepWasProduced, "Did not produce all valid steps");
            }

        }


    }
}
