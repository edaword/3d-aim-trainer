package mygame;
import java.util.ArrayList;

/*
Asad Jiwani & Edward Wang
April 6th, 2021
This class controls a player stat for the person playing the game. A player stat has a name and an
array list of stat entries
 */

public class PlayerStats {
    //create attributes for a player stat
    private String name;
    private ArrayList<StatEntry> entries;
    
    /**
     * Primary constructor - accept new values for the name in the player stat
     * @param name - the name in the player stat
     */
    public PlayerStats(String name){
        this.name = name;
    }
    
    /**
     * Secondary constructor - accept new values for the name and the list of stat entries in the player stat
     * @param name - the name in the player stat
     * @param entries - the ArrayList containing the new stat entries
     */
    public PlayerStats(String name, ArrayList<StatEntry> entries){
        this(name); //chain primary constructor
        this.name = name;
        this.entries = entries;
    }
    
    public void addEntry(StatEntry entry){
        entries.add(entry);
        sort(entries, 0, entries.size() - 1);
    }
    
    /**
     * Get the array list containing the player stat entries
     * @return the array list containing the player stat entries
     */
    public ArrayList<StatEntry> getEntries(){
        return entries;
    }
    
    /**
     * Create a String representation of all attributes
     * @return a String representation of all attributes
     */
    public String toString(){
        return "Name: " + name + "\nEntries: " + entries;
    }
    
    /**
     * Clone method - create a new player stat with the same attributes as the one in this class
     * @return a new player stat with the same attributes as the one in this class
     */
    public PlayerStats clone(){
        //create a clone
        PlayerStats clone = new PlayerStats(name, entries);
        return clone;
    }
    
    /**
     * Equals method - compare another player stat to the one in this class
     * @param other - the other player stat to compare
     * @return true or false depending on if the other player stat is equal
     */
    public boolean equals(PlayerStats other){
        return this.name.equals(other.name) && this.entries == other.entries;
    }
    
    /**
     * Sort an array of integers using quik sort
     * @param a - the array containing the integers
     * @param left - the left most side of the array
     * @param right - the right most side of the array
     * @return the sorted array
     */
    private static ArrayList<StatEntry> sort(ArrayList<StatEntry> a, int left, int right) {
        //create a variable that holds a temporary value
        StatEntry temp;
        //base case, the left side of the array is larger than or equal to the right
        if (left >= right) {
            return null; //return nothing
        }
        //create variables for the left and right side of the array
        int i = left;
        int j = right;
        //create a variable for the middle point of the array
        StatEntry pivot = a.get((left+right)/2);
        //while the left side is less than the right
        while (i < j) {
            //while the number at the left side is less than the pivot
            while ((a.get(i).compareTo(pivot) < 0)) {
                i++; //increase left side
            }
            while(a.get(j).compareTo(pivot) > 0) { //while the pivot is less than the number at the j value
                j--; //decrease right side
            }
            if (i <= j) { //if the left side is less than or equal to the j value
                temp = a.get(i); //move the current i value number to the temporary variable
                StatEntry ai = a.get(i);
                StatEntry aj = a.get(j);
                ai = aj; //change the new i number to the current j number
                aj = temp; //change the j number to the old i number
                i++; //increase left side
                j--; //increase right side
            }
        }
        //recursive call
        //keep invoking quikSort method
        sort(a, left, j);
        sort(a, i, right);
        //return the sorted array
        return a;
    }
    
}
