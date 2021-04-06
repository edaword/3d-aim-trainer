package mygame;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class extend the abstract class for a game target and creates a ball target. 
A target has a position, dimensions, a boolean that controls whether it is hit, and a color
 */

public class BallTarget extends AbstractTarget{
    BallTarget() {
        
    }
    
    public BallTarget clone() {
        BallTarget b1 = new BallTarget();
        return b1;
    }
}
