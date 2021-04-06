
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class extend the abstract class for a game target and creates a box target. 
A target has a position, dimensions, a boolean that controls whether it is hit, and a color
 */

public class BoxTarget extends AbstractTarget{
    private Box box;
    private Geometry box1;
    private SpaceDef dimensions;
    private SpaceDef pos;
    private ColorRGBA color;
    
    /**
     * Primary constructor - accept new values for all attributes
     * @param pos - the position of the box target
     * @param dimensions - the dimensions for the box target
     * @param color - the color of the box target
     */
    public BoxTarget(SpaceDef pos, SpaceDef dimensions, ColorRGBA color){
        this.pos = pos;
        this.dimensions = dimensions;
        this.color = color;
        box = new Box(dimensions.getX(),dimensions.getY(),dimensions.getZ());
        box1 = new Geometry("Box", box);
        box1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
    
    /**
     * Set the dimensions for the target
     * @param dimensions - the dimensions for the target
     */
    public void setDimensions(SpaceDef dimensions){
        this.dimensions = dimensions;
    }
    
    /**
     * Get the dimensions for the target
     * @return the dimensions for the target
     */
    public SpaceDef getDimensions(){
        return dimensions;
    }
    
    /**
     * Set the position for the target
     * @param pos - the position for the target
     */
    public void setPos(SpaceDef pos){
        this.pos = pos;
    }
    
    /**
     * Get the position for the target
     * @return the position for the target
     */
    @Override
    public SpaceDef getPos(){
        return pos;
    }
    
    /**
     * Create a new box target with same attributes as this one
     * @return a new box target with same attributes as this one
     */
    @Override
    public BoxTarget clone(){
        BoxTarget clone = new BoxTarget(pos, dimensions, color);
        return clone;
    }
    
    /**
     * Check if another box target is equal to the one in this class
     * @param other - the other box target to compare
     * @return true or false depending on if the target is equal
     */
    public boolean equals(BoxTarget other){
        return this.pos == other.pos && this.dimensions == other.dimensions && this.color == other.color;
    }
}
