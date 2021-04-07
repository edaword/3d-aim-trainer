package mygame;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class extend the abstract class for a game target and creates a ball target. 
A target has a position, dimensions, a boolean that controls whether it is hit, and a color
 */

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;

public class BallTarget extends AbstractTarget{
    private Sphere ball;
    private Geometry ball1;
    private float radius;
    private int zSamples;
    private int radialSamples;
    private SpaceDef pos;
    
    /**
     * Primary constructor - accept new values for the position, radius, and color of the target
     * @param pos - the position of the target
     * @param radius - the radiius of the target
     * @param color - the color of the target
     */
    public BallTarget(SpaceDef pos, float radius, ColorRGBA color) {
        this.pos = pos;
        this.radius = radius;
        this.color = color;
        zSamples = 30;
        radialSamples = 30;
        ball = new Sphere(zSamples, radialSamples, radius);
    }
    
    /**
     * Secondary constructor - accept new values for the position, radius, z position, radial, 
     * and color of the target
     * @param pos - the position of the target
     * @param radius - the radiius of the target
     * @param zSamples - the z position of the target
     * @param radialSamples - the radial of the target
     * @param color - the color of the target
     */
    public BallTarget(SpaceDef pos, float radius, int zSamples, int radialSamples, ColorRGBA color) {
        this(pos, radius, color);
	this.zSamples = zSamples;
	this.radialSamples = radialSamples;
	ball = new Sphere(zSamples,radialSamples, radius);
        ball1 = new Geometry("Ball", ball);
	ball1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
        
    /**
     * Set the radius of the target
     * @param radius - the radius of the target
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    /**
     * Set the z position of the target
     * @param zSamples - the z position of the target
     */
    public void setZSamples(int zSamples) {
        this.zSamples = zSamples;
    }
    
    /**
     * Set the radial of the target
     * @param radialSamples - the radial of the target
     */
    public void setRadialSamples(int radialSamples) {
        this.radialSamples = radialSamples;
    }
    
    /**
     * Get the radius of the target
     * @return the radius of the target
     */
    public float getRadius() {
        return radius;
    }
    
    /**
     * Get the z position of the target
     * @return the z position of the target
     */
    public int getZsamples() {
        return zSamples;
    }
    
    /**
     * Get the radial of the target
     * @return the radial of the target
     */
    public int getRadialSamples() {
        return radialSamples;
    }
    
    /**
     * Create a String representation of all attributes
     * @return a String representation of all attributes
     */
    public String toString() {
        String s = "Radius: " + radius + " z-samples: " + zSamples + " Radial Samples: " + radialSamples;
        return s;
    }
    
    /**
     * Clone method - create a new ball target with same attributes as this one
     * @return a new ball target with same attributes as this one
     */
    public BallTarget clone() {
        BallTarget b1 = new BallTarget(pos, radius, color);
        return b1;
    }
}
