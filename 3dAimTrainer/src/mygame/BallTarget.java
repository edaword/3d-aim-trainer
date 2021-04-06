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
    
    public BallTarget(SpaceDef pos, float radius, ColorRGBA color) {
        this.pos = pos;
        this.radius = radius;
        this.color = color;
        zSamples = 30;
        radialSamples = 30;
        ball = new Sphere(zSamples, radialSamples, radius);
    }
    
    public BallTarget(SpaceDef pos, float radius, int zSamples, int radialSamples, ColorRGBA color) {
        this(pos, radius, color);
	this.zSamples = zSamples;
	this.radialSamples = radialSamples;
	ball = new Sphere(zSamples,radialSamples, radius);
        ball1 = new Geometry("Ball", ball);
	ball1.setLocalTranslation(pos.getX(),pos.getY(),pos.getZ());
    }
        
    public void setRadius(float radius) {
        this.radius = radius;
    }
    
    public void setZSamples(int zSamples) {
        this.zSamples = zSamples;
    }
    
    public void setRadialSamples(int radialSamples) {
        this.radialSamples = radialSamples;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public int getZsamples() {
        return zSamples;
    }
    
    public int getRadialSamples() {
        return radialSamples;
    }
    
    public String toString() {
        String s = "Radius: " + radius + " z-samples: " + zSamples + " Radial Samples: " + radialSamples;
        return s;
    }
    
    public BallTarget clone() {
        BallTarget b1 = new BallTarget(pos, radius, color);
        return b1;
    }
}
