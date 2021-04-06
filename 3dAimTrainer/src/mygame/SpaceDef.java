package mygame;
/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class holds 3 values that represent the x, y, and z values of either the dimensions of an object or a position in a 3d space.
 */

public class SpaceDef {
    private float x;
    private float y;
    private float z;
    
    /**
     * Primary constructor - accept new values for all attributes
     * @param x - x position of object
     * @param y - y position of object
     * @param z - Z position of object
     */
    public SpaceDef(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Set the x position of the object
     * @param x - the x position of the object
     */
    public void setX(float x){
        this.x = x;
    }
    
    /**
     * Set the y position of the object
     * @param y - the y position of the object
     */
    public void setY(float y){
        this.y = y;
    }
    
    /**
     * Set the z position of the object
     * @param z - the z position of the object
     */
    public void setZ(float z){
        this.z = z;
    }
    
    /**
     * Get the X position of the object
     * @return the X position of the object
     */
    public float getX(){
        return x;
    }
    
    /**
     * Get the Y position of the object
     * @return the Y position of the object
     */
    public float getY(){
        return y;
    }
    
    /**
     * Get the Z position of the object
     * @return the Z position of the object
     */
    public float getZ(){
        return z;
    }
    
    /**
     * Create a String representation of all positions
     * @return a String representation of all positions
     */
    public String toString(){
        return "X value: " + x + "\nY value: " + y + "\nZ value: " + z;
    }
    
    /**
     * Create a new SpaceDef with same attribute values as this one
     * @return a new SpaceDef with same attribute values as this one
     */
    public SpaceDef clone(){
        SpaceDef clone = new SpaceDef(x, y, z);
        return clone;
    }
    
    /**
     * Compare another SpaceDef with the one in this class
     * @param other - the other SpaceDef to compare
     * @return true or false depending on if the SpaceDef is the same
     */
    public boolean equals(SpaceDef other){
        return this.x == other.x &&
                this.y == other.y &&
                this.z == other.z;
    }
    
}
