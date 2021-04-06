
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class extend the abstract class for a game target and creates a ball target. 
A target has a position, dimensions, a boolean that controls whether it is hit, and a color
 */

public class BallTarget extends AbstractTarget{
    //create attributes for a ball target
    private Geometry ball1;
    private Sphere ball;
    private float radius;
    private int zSamples;
    private int radialSamples;
    private SpaceDef pos;
    
    /**
     * Primary constructor - accept new values for the position and radius of the target
     * @param pos - the position of the target
     * @param radius - the radius of the target
     */
    public BallTarget(SpaceDef pos, float radius){
        this.pos = pos;
        this.radius = radius;
        //set the zSamples and radialSamples to 30
        zSamples = 30; 
        radialSamples = 30;
        //create a new Sphere and a Geometry using that sphere
        ball = new Sphere(zSamples,radialSamples,radius);
        ball1 = new Geometry("Ball", ball);
        //Translate the ball
        ball1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
    
    /**
     * Second constructor - accept new values for the position, radius, and color of the target
     * @param pos - the position of the target
     * @param radius - the radius of the target
     * @param color - the color of the target
     */
    public BallTarget(SpaceDef pos, float radius, ColorRGBA color){
        this(pos,radius); //chain primary constructor
        this.pos = pos;
        this.radius = radius;
        this.color = color;
        //set the zSamples and radialSamples to 30
        zSamples = 30; 
        radialSamples = 30;
        //create a new Sphere and a Geometry using that sphere
        ball = new Sphere(zSamples,radialSamples,radius);
        ball1 = new Geometry("Ball", ball);
        //Translate the ball
        ball1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
    
    /**
     * Third constructor - accept new values for the position, radius, zSamples, and radialSamples of the target
     * @param pos - the position of the target
     * @param radius - the radius of the target
     * @param zSamples - the z position sample for the target
     * @param radialSamples - the radial sample for the target
     */
    public BallTarget(SpaceDef pos, float radius, int zSamples, int radialSamples){
        this(pos,radius); //chain primary constructor
        this.zSamples = zSamples;
        this.radialSamples = radialSamples;
        //create a new Sphere and a Geometry using that sphere
        ball = new Sphere(zSamples,radialSamples,radius);
        ball1 = new Geometry("Ball", ball);
        //Translate the ball
        ball1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
    
    /**
     * Set the radius of the target
     * @param radius - the radius of the target
     */
    public void setRadius(float radius){
        this.radius = radius;
    }
    
    /**
     * Set the z position of the target
     * @param zSamples - the z position of the target
     */
    public void setZSamples(int zSamples){
        this.zSamples = zSamples;
    }
    /**
     * Set the radial of the target
     * @param radialSamples - the radial of the target
     */
    public void setRadialSamples(int radialSamples){
        this.radialSamples = radialSamples;
    }
    
    /**
     * Get the radius of the target
     * @return the radius of the target
     */
    public float getRadius(){
        return radius;
    }
    
    /**
     * Get the z position of the target
     * @return the z position of the target
     */
    public float getZSamples(){
        return zSamples;
    }
    
    /**
     * Get the radial of the target
     * @return the radial of the target
     */
    public float getRadialSamples(){
        return radialSamples;
    }
    
    /**
     * Create a new ball target with same attributes as this one
     * @return a new ball target with same attributes as this one
     */
    @Override
    public BallTarget clone(){
        BallTarget clone = new BallTarget(pos, radius, zSamples, radialSamples);
        return clone;
    }
    
    /**
     * Check if another ball target is equal to the one in this class
     * @param other - the other ball target to compare
     * @return true or false depending on if the target is equal
     */
    public boolean equals(BallTarget other){
        return this.pos == other.pos && this.dimensions == other.dimensions && this.color == other.color;
    }
}
