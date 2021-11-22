package comp1140.ass2;

import java.util.*;

public class Move {
    // The sequence of steps that makes up this move
    private Step[] steps;

    public Move(Step[] steps) {
        this.steps = steps;
    }

    public Move (List<Step> steps){
        this.steps = steps.toArray(new Step[0]);
    }


    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }

    /**
     * Construct a move from a string
     * For example,
     * if a move string is "b1d1d3b3"
     * then the steps it contains are "b1d1","d1d3" and "d3b3"
     *
     * @param move a string of a move
     */
    public Move(String move){
        List<Step> step = new ArrayList<>();
        for (int i = 0; i < move.length()-2; i+= 2){
            String s = move.substring(i,i+4);
            step.add(new Step(s));
        }
        this.steps = step.toArray(new Step[0]);
    }


    /** Given a exist move and a next step
     * construct a new move
     *
     * @param next next step
     * @return a new move
     */
    public Move addStep(Step next){
        List<Step> exist = new ArrayList<>(Arrays.asList(this.steps));
        exist.add(next);
        this.steps = exist.toArray(new Step[0]);
        return new Move(steps);
    }


    /**
     * Checks the validity of the move
     * @return Whether the move is valid or not
     */
    public boolean isMoveValid() { return false; }


    @Override
    public String toString() {
        if(steps.length == 1)
            return steps[0].toString();
        else{
            StringBuilder move = new StringBuilder();
            for (int i = 1;i<steps.length;i++){
                move.append(steps[i].getNewLocation().toString()) ;
            }
            return steps[0].toString() + move;
        }
    }
}
