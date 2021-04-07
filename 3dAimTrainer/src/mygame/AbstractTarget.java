package mygame;
import com.jme3.math.ColorRGBA;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class is an abstract class for a game target. A target has a position, dimensions, 
a boolean that controls whether it is hit, and a color
 */

public abstract class AbstractTarget {
    protected SpaceDef pos;
    protected boolean hit;
    protected ColorRGBA color;
    protected SpaceDef dimensions;
    
    /**
     * Set the color of the targets
     * @param color - the color of the target
     */
    public void setColor(ColorRGBA color){
        this.color = color;
    }
    
    /**
     * Set the target to be hit
     * Change the target color to white
     */
    public void setHit(){
        this.hit = true;
        color = color.set(ColorRGBA.White);
    }
    
    /**
     * Set the target to not be hit
     * Change the target color to black
     */
    public void setUnHit(){
        this.hit = false;
        color = color.set(ColorRGBA.Black);
    }
    
    public void update(){
        if(hit){
            color = color.set(ColorRGBA.White);
        }else{
            color = color.set(ColorRGBA.Black);
        }
    }
    /**
     * Get the current color of the target
     * @return the current color of the target
     */
    public ColorRGBA getColor(){
        return color;
    }
    
    /**
     * Get the status of whether or not the target was hit
     * @return the status of whether or not the target was hit
     */
    public boolean getHit(){
        return hit;
    }
    
    /**
     * Get the positions of the target
     * @return the positions of the target
     */
    public SpaceDef getPos(){
        return pos;
    }
    
    /**
     * Create a String representation of all attributes
     * @return a String representation of all attributes
     */
    public String toString(){
        return "Positions: " + pos + "Dimensions: " + dimensions + "Color: " + color + "Hit: " + hit;
    }
    
    /**
     * Create a new Target with same attributes as this one
     * @return a new Target with same attributes as this one
     */
    public abstract AbstractTarget clone();
}
