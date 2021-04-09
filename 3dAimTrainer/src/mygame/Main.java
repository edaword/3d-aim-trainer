package mygame;

/*
Asad Jiwani & Edward Wang
April 5th, 2021
This class contains the main class for the program. 
It also contains all the code for the game
*/


import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.io.FileNotFoundException;

 
public class Main extends SimpleApplication implements ActionListener {
    //create variable that holds te value of 90 degrees in radian form
    static final float degToRad90 = 1.570796f;
    //the mesh of the scene
    private Spatial sceneModel;
    //bullet physics engine
    private BulletAppState bulletAppState;
    //physics controller for the scene
    private RigidBodyControl landscape;
    //physics controller for the player
    private CharacterControl player;
    //the direction the player is walking in
    //a vector of 3 numbers, the x,y, and z direction of movement
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //declare counter variables
    private static int targetsHit, shotsFired;
    //create AudioNodes for sound effects
    private AudioNode gunshot;
    private AudioNode ding;
    //declare variable for the game stats
    private StatEntry gameStats;
    //Temporary vectors used on each frame that hold the direction the player is moving in
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    //text box to hold stats
    private BitmapText hudStats;
    //decimal format for the in game accuracy
    private DecimalFormat twoPoints = new DecimalFormat("0.0%");
    //create an array list of stat entries for the user's stats
    private static ArrayList<StatEntry> userStats = new ArrayList<StatEntry>();
    //an array that holds all the positions of the targets
    private static SpaceDef[][] targetPositions;
    //whether the user is playing the 50 shot challenge or not
    private boolean challengeMode;
    //whether the user is in endless or challenge mode
    private BitmapText state;
    //displays the top 5 scores
    private BitmapText leaderboard;
    //the string that BitmapText leaderboard contains;
    String output = "Leaderboard:\n";
    
    public static void main(String[] args) {
       //create a 2D array of 5 target positions
       SpaceDef[][] tempTargetPositions5 = {
                                            {new SpaceDef(20f,3f,-2),new SpaceDef(20f,3f,-1),new SpaceDef(20f,3f,0),new SpaceDef(20f,3f,1),new SpaceDef(20f,3f,2)},
                                            {new SpaceDef(20f,4f,-2),new SpaceDef(20f,4f,-1),new SpaceDef(20f,4f,0),new SpaceDef(20f,4f,1),new SpaceDef(20f,4f,2)},
                                            {new SpaceDef(20f,5f,-2),new SpaceDef(20f,5f,-1),new SpaceDef(20f,5f,0),new SpaceDef(20f,5f,1),new SpaceDef(20f,5f,2)},
                                            {new SpaceDef(20f,6f,-2),new SpaceDef(20f,6f,-1),new SpaceDef(20f,6f,0),new SpaceDef(20f,6f,1),new SpaceDef(20f,6f,2)},
                                            {new SpaceDef(20f,7f,-2),new SpaceDef(20f,7f,-1),new SpaceDef(20f,7f,0),new SpaceDef(20f,7f,1),new SpaceDef(20f,7f,2)},    
                                           };
           //assign the 5 target position 2D array to the static 2D array                 
           targetPositions = tempTargetPositions5;
        //start game
        Main app = new Main();
        app.start();
    }
    
    //node to hold spatials that can be shot
    private Node shootables;

    @Override
    /**
     * Initialize the main game code
     */
    public void simpleInitApp() {
        //the user has not started the 50 shot challenge yet
        challengeMode = false;
        //set counter variables to 0
        shotsFired = 0;
        targetsHit = 0;
        
        //set rotation speed for camera (mouse sensitivity)
        flyCam.setRotationSpeed(1);
        
        /** Set up Physics */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
//        flyCam.setMoveSpeed(100);
        initCrossHairs(); // a "+" in the middle of the screen to help aiming
        setUpKeys();
        //invoke method to initialize the audio soundeffects
        initAudio();
    //light is already done
//        setUpLight();

        //load scene from asset folder and add code to control/make scene
        sceneModel = assetManager.loadModel("Scenes/MyScene.j3o");
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        
        //add code to cuztomize the player in the game
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        player.setGravity(new Vector3f(0,-30f,0));
        player.setPhysicsLocation(new Vector3f(0,10,0));
        
        //create a new node for the users shots
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables); //attach node to main node
        //start with a single target in the middle
        shootables.attachChild(makeCube("target", targetPositions[2][2], ColorRGBA.Orange));
       
        //displays stats in real time
        hudStats = new BitmapText(guiFont, false);
        hudStats.setSize(guiFont.getCharSet().getRenderedSize() * 4);  
        hudStats.setColor(ColorRGBA.Blue);                             // font color
        hudStats.setText("Accuracy: " + twoPoints.format((float) targetsHit / shotsFired) + "\nTargets Hit: " + targetsHit + "\nShots Taken: " + shotsFired);
        hudStats.setLocalTranslation(300, hudStats.getLineHeight() * 3, 0); // position
        guiNode.attachChild(hudStats);
        
        //display the intro wall
        //write code to create/customize intro wall text
        BitmapText title = new BitmapText(guiFont,false);
        title.setSize(2); //set font size
        title.setText("3D AIM TRAINER");
        title.setLocalTranslation(-4,16,-24);
        rootNode.attachChild(title);

        //create nodes for the buttons on intro wall
        Node buttons = new Node();
        rootNode.attachChild(buttons);

        //create buttons for the users options on intro wall
        buttons.attachChild(makeButton("Endless", new SpaceDef(0,4,-24), "-z"));
        buttons.attachChild(makeButton("50 Shot Challenge", new SpaceDef(0,6,-24), "-z"));
        
        //text box for credits wall
        BitmapText credits = new BitmapText(guiFont,false);
        credits.setText("Credits!!\nThis game was created by: Edward Wang & Asad Jiwani\nApril 8th 2021"
                + "\nProject Manager, Programmer, Technical Writer, Sound Effects: Asad Jiwani\nSystems Analyst, "
                + "Lead Programmer, Graphics Artist: Edward Wang");
        //cuztomize credits wall text
        credits.rotate(0,degToRad90 * 2,0);
        credits.setSize(0.5f);
        credits.setLocalTranslation(0,8,24);
        //attach credits text to node
        rootNode.attachChild(credits);
        
        //create text to show if the user chose endless mode
        state = new BitmapText(guiFont, false);
        state.setText("Endless");
        //cuztomize text
        state.setSize(2f);
        //move to new location and rotate
        state.setLocalTranslation(24,16,0);
        state.rotate(0,-degToRad90, 0);
        //attach to node
        rootNode.attachChild(state);
        
        //create text for the leaderboard wall
        leaderboard = new BitmapText(guiFont,false);
        leaderboard.setText(output);
        //cuztomize leaderboard wall text
        leaderboard.rotate(0,degToRad90 * 2,0);
        leaderboard.setSize(0.5f);
        leaderboard.setLocalTranslation(-5,12,-24);
        //attach leaderboard text to node
        rootNode.attachChild(leaderboard);
        readData();
    }
    
    private void readData() {
        try{
            //set up connection to data file containing the top scores
            //create Scanner to read data from file
            InputStream in = Main.class.getResourceAsStream("scores.txt");
            Scanner s = new Scanner(in); 
            //while there is data in the file
            while(s.hasNextLine()){
                //read in the shots fired in the data file
                shotsFired = Integer.parseInt(s.nextLine());
                //create a new StatEntry using the data in the data fie
                //since the user played challenge mode, targets hit is always 50
                StatEntry stat = new StatEntry(50, shotsFired, 50/shotsFired);
                userStats.add(stat); //add the stat entry to the array list
            }
            
        }catch (Exception e){ //if file not found
            System.out.println("Error: " + e); //print error
        }
    }
    
    private void writeData() {
        try {
            FileOutputStream fOut = new FileOutputStream(System.getProperty("user.dir") + "/save.txt");
            PrintWriter pw = new PrintWriter(fOut);
            for (StatEntry stat : userStats) {
                pw.println(stat.getShotsFired());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    /**
     * Make a button on the screen
     * @param text - the text to display on the button
     * @param pos - the position of the button
     * @param direction - the direction of the button
     * @return a node that has the button set up
     */
    private Node makeButton(String text, SpaceDef pos, String direction) {
        //create node for the button
        Node container = new Node();
        //move button to new location
        container.setLocalTranslation(new Vector3f(pos.getX(),pos.getY(),pos.getZ()));
        //set and cuztomize text on button
        BitmapText bText = new BitmapText(guiFont, false);
        bText.setText(text);
        bText.setColor(ColorRGBA.Red);
        bText.setSize(2);
        //make cubes so that the user has something to shoot
        Geometry cube = makeCube(text, new SpaceDef(0,0,0), ColorRGBA.Red);
        //add an identical (but transparent) cube at the same position in the 
        //  shootables node so the user can click it.
        shootables.attachChild(makeCube(text, pos, new ColorRGBA(0f,0f,0f,0f)));
        container.attachChild(cube);
        //attach text to cube
        container.attachChild(bText);
        //set the location of the text based on the direction parameter
        if (direction.equals("-z")) {
            bText.setLocalTranslation(2,1.5f,0);
        } else if (direction.equals("+z")) {
            bText.setLocalTranslation(-2,0,0);
            bText.rotate(0f,degToRad90 * 2,0f); //rotate the text to face the origin
        } else if (direction.equals("-x")) {
            bText.setLocalTranslation(2,0,4);
            bText.rotate(0f,degToRad90,0f); //rotate the text to face the origin
        } else if (direction.equals("+x")) {
            bText.setLocalTranslation(-2,0,-4);
            bText.rotate(0f,-degToRad90,0f); //rotate the text to face the origin
        }
        return container; //return node with button
    }

    /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    //set up keys that control the user's movement and shooting
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
    //add listener to keys so that we can tell when the player clicked a button
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(this, "Shoot");
  }

  /**
   * Initialize all the audio sounds for the game
   */
  private void initAudio(){
      //create node for the audio
      gunshot = new AudioNode(assetManager, "Sound/gunshot.wav", DataType.Buffer);
      //cuztomize the sound controls so that it does not loop/keep on playing
      gunshot.setPositional(false);
      gunshot.setLooping(false);
      //set volume for audio
      gunshot.setVolume(2);
      //attach to node
      rootNode.attachChild(gunshot);
      
      //create node for the audio
      ding = new AudioNode(assetManager, "Sound/ding.wav", DataType.Buffer);
      //cuztomize the sound controls so that it does not loop/keep on playing
      ding.setPositional(false);
      ding.setLooping(false);
      //set volume for audio
      ding.setVolume(2);
      //attach to node
      rootNode.attachChild(ding);
  }
  
  /** These are our custom actions triggered by key presses.
   * We do not walk yet, we just keep track of the direction the user pressed. */
  public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Left")) { //if the user wants to go left
      left = isPressed;
    } else if (binding.equals("Right")) { //if the user wants to go right
      right= isPressed;
    } else if (binding.equals("Up")) { //if the user wants to go up
      up = isPressed;
    } else if (binding.equals("Down")) { //if the user wants to go down
      down = isPressed;
    } else if (binding.equals("Jump")) { //if the user wants to jump
      if (isPressed) { player.jump(new Vector3f(0,20f,0));} //write code to allow user to jump
    } else if (binding.equals("Shoot") && !isPressed) {
        //if the user has hit 50 targets, the game ends
        if (targetsHit >=50 && challengeMode) {
            challengeMode = false;

            //run this code everytime a 50 round game ends
            //store their stats from this round
            StatEntry currentGameStats = new StatEntry(targetsHit,shotsFired,(targetsHit/shotsFired)*100);
            //add it to the arrayList userStats
            userStats.add(currentGameStats);
            //sort the user stats
           quikSort(userStats, 0,userStats.size()-1);
            
            writeData();

            //get the top 5 scores from user stats
            for (int i = 0; i < 5; i++) {
                output += i + ":" + userStats.get(i).getAccuracy() + "\n";
            }
            //set that text to the leaderboard
            leaderboard.setText(output);
            
            //write the new records to the data file
            
            
            //reset counts
            targetsHit = 0;
            shotsFired = 0;
        }
        //if the user wants to shoot
        //waiting until the click ends avoids multiple inputs
        //play the gunshot sound
        gunshot.playInstance();
        //add one to the total shots counter
        shotsFired++;
        // 1. Reset the results. CollisionResults is an arrayList of objects of type CollisionResult
        CollisionResults results = new CollisionResults();
        // 2. Draw a ray from the camera location in the camera direction
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between the Ray and objects in the shootables node in results list.
        shootables.collideWith(ray, results);
        // 5. If something was hit
        if (results.size() > 0) {
            ding.playInstance(); //play ding sound effect
            // The closest collision point is what was truly hit:
            CollisionResult closest = results.getClosestCollision();
            String closestID = closest.getGeometry().getName();
            if (closestID.equals("target")) {
                //add one to the num targets hit counter
                targetsHit++;
                //remove it from shootables (removes it from the scene)
                shootables.detachChild(closest.getGeometry());
                //make a new target in a random position
                newTarget();
            } else if (closestID.equals("Endless")) {
                //if the user clicked the endless button
                ///quit challenge mode
                challengeMode = false;
                //the shot doesn't count
                shotsFired--;
                state.setText("Endless");
                
            } else if (closestID.equals("50 Shot Challenge")) {
                //enter or stay in challenge mode
                challengeMode = true;
                //the shot doesn't count
                shotsFired = 0;
                targetsHit = 0;
            }
            
        }
        //update stats in real time
        hudStats.setText("Accuracy: " + twoPoints.format((float) targetsHit / shotsFired) + "\nTargets hit: " + targetsHit + "\nShots taken: " + shotsFired);
        if (challengeMode) {
            state.setText("Challenge mode:\n" + (50 - targetsHit) + " targets left!"); 
        }
    }
  }
  /**
   * Create a new target for the user to shoot
   */
  public void newTarget() {
      //position variables for target
      int xPos, yPos;
      //randomize position for target
      xPos = (int) (Math.random() * (targetPositions.length));
      yPos = (int) (Math.random() * (targetPositions.length));
      //attach to node
      shootables.attachChild(makeCube("target", targetPositions[xPos][yPos], ColorRGBA.Orange));
  }

  /**
   * This is the main event loop--walking happens here.
   * We check in which direction the player is walking by interpreting
   * the camera direction forward (camDir) and to the side (camLeft).
   * The setWalkDirection() command is what lets a physics-controlled player walk.
   * We also make sure here that the camera moves with player.
   */
  @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        //prevents user from flying (walking in the vertical direction)
        camDir.setY(0);
        camLeft.setY(0);
        walkDirection.set(0, 0, 0);
        //create a new walking direction for the player based on where they wanna go
        if (left) { //if we want to go left
            walkDirection.addLocal(camLeft);
        }
        if (right) { //if we want to go right
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) { //if we want to go up
            walkDirection.addLocal(camDir);
        }
        if (down) { //if we want to go down
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);//set the walking direction of the player
        cam.setLocation(player.getPhysicsLocation()); //set location of the camera
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    /** A centered plus sign to help the player aim. */
    protected void initCrossHairs() {
        setDisplayStatView(false);
        //load font from assets folder
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        //create and cuztomize crosshair font
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // create + shape crosshair
        ch.setLocalTranslation( // center
          settings.getWidth() / 2 - ch.getLineWidth()/2,
          settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
        guiNode.attachChild(ch); //attach to node
    }

    /**
     * Make a cube for target practice
     * @param name - name of the cube
     * @param pos - position of the cube
     * @param color - the color of the cube
     * @return the cube made for target practice
     */
    protected Geometry makeCube(String name, SpaceDef pos, ColorRGBA color) {
        Box box = new Box(0.5f, 0.5f, 0.5f); //create box using built in methods
        //create new geomtery using the box above
        Geometry cube = new Geometry(name, box);
        //move the geometry to new location
        cube.setLocalTranslation(pos.getX(), pos.getY(), pos.getZ());
        //load data from asset folder
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", color); //randomize color of cube
        cube.setMaterial(mat1);
        return cube; //return the cube
    }
    
    /**
     * Sort an array of stat entries using quik sort
     * @param a - the array containing the stat entries
     * @param left - the left most side of the array
     * @param right - the right most side of the array
     * @return the sorted array
     */
    private static ArrayList<StatEntry> quikSort(ArrayList<StatEntry> a, int left, int right) {
        //create a variable that holds a temporary value
        StatEntry temp;
        //base case, the left side of the arraylist is larger than or equal to the right
        if (left >= right) {
            return a; //return the array list
        }
        //create variables for the left and right side of the arraylist
        int i = left;
        int j = right;
        //create a variable for the middle point of the arraylist
        StatEntry pivot = a.get((left+right)/2);
        //while the left side is less than the right
        while (i < j) {
            //while the number at the left side is less than the pivot
            while (a.get(i).getShotsFired() < pivot.getShotsFired()) {
                i++; //increase left side
            }
            while(pivot.getShotsFired() < a.get(j).getShotsFired()){ //while the pivot is less than the number at the j value
                j--; //decrease right side
            }
            if (i <= j) { //if the left side is less than or equal to the j value
                Collections.swap(a,left,right); //swap the two entries
                i++; //increase left side
                j--; //decrease right side
            }
        }
        //recursive call
        //keep invoking quikSort method
        quikSort(a, left, j);
        quikSort(a, i, right);
        //return the sorted arraylist
        return a;
    }
}