package mygame;

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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.renderer.RenderManager;

 
public class Main extends SimpleApplication implements ActionListener {
    //test s
    //testtttttttttttttttttt
    //test again
    private Spatial sceneModel;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    //declare counter variables
    int targetsHit, numShots;
    //create AudioNodes for sound effects
    private AudioNode gunshot;
    private AudioNode ding;
    //declare variable for the crosshair shape
    int crosshairIndex;
    //Temporary vectors used on each frame.
    //They here to avoid instanciating new vectors on each frame
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    static int[][] test = {{1,2},{3,4}};
    
    static SpaceDef[][] targetPositions;
    
    public static void main(String[] args) {
       SpaceDef[][] tempTargetPositions8 = {{new SpaceDef(4f,3f,-3.5f),new SpaceDef(4f,3f,-2.5f),new SpaceDef(4f,3f,-1.5f),new SpaceDef(4f,3f,-0.5f),new SpaceDef(4f,3f,0.5f),new SpaceDef(4f,3f,1.5f),new SpaceDef(4f,3f,2.5f),new SpaceDef(4f,3f,3.5f)},
                                           {new SpaceDef(4f,4f,-3.5f),new SpaceDef(4f,4f,-2.5f),new SpaceDef(4f,4f,-1.5f),new SpaceDef(4f,4f,-0.5f),new SpaceDef(4f,4f,0.5f),new SpaceDef(4f,4f,1.5f),new SpaceDef(4f,4f,2.5f),new SpaceDef(4f,4f,3.5f)}, 
                                           {new SpaceDef(4f,5f,-3.5f),new SpaceDef(4f,5f,-2.5f),new SpaceDef(4f,5f,-1.5f),new SpaceDef(4f,5f,-0.5f),new SpaceDef(4f,5f,0.5f),new SpaceDef(4f,5f,1.5f),new SpaceDef(4f,5f,2.5f),new SpaceDef(4f,5f,3.5f)},
                                           {new SpaceDef(4f,6f,-3.5f),new SpaceDef(4f,6f,-2.5f),new SpaceDef(4f,6f,-1.5f),new SpaceDef(4f,6f,-0.5f),new SpaceDef(4f,6f,0.5f),new SpaceDef(4f,6f,1.5f),new SpaceDef(4f,6f,2.5f),new SpaceDef(4f,6f,3.5f)},
                                           {new SpaceDef(4f,7f,-3.5f),new SpaceDef(4f,7f,-2.5f),new SpaceDef(4f,7f,-1.5f),new SpaceDef(4f,7f,-0.5f),new SpaceDef(4f,7f,0.5f),new SpaceDef(4f,7f,1.5f),new SpaceDef(4f,7f,2.5f),new SpaceDef(4f,7f,3.5f)},
                                           {new SpaceDef(4f,8f,-3.5f),new SpaceDef(4f,8f,-2.5f),new SpaceDef(4f,8f,-1.5f),new SpaceDef(4f,8f,-0.5f),new SpaceDef(4f,8f,0.5f),new SpaceDef(4f,8f,1.5f),new SpaceDef(4f,8f,2.5f),new SpaceDef(4f,8f,3.5f)},
                                           {new SpaceDef(4f,9f,-3.5f),new SpaceDef(4f,9f,-2.5f),new SpaceDef(4f,9f,-1.5f),new SpaceDef(4f,9f,-0.5f),new SpaceDef(4f,9f,0.5f),new SpaceDef(4f,9f,1.5f),new SpaceDef(4f,9f,2.5f),new SpaceDef(4f,9f,3.5f)},
                                           {new SpaceDef(4f,10f,-3.5f),new SpaceDef(4f,10f,-2.5f),new SpaceDef(4f,10f,-1.5f),new SpaceDef(4f,10f,-0.5f),new SpaceDef(4f,10f,0.5f),new SpaceDef(4f,10f,1.5f),new SpaceDef(4f,10f,2.5f),new SpaceDef(4f,10f,3.5f)}};
       SpaceDef[][] tempTargetPositions5 = {{new SpaceDef(20f,3f,-2),new SpaceDef(20f,3f,-1),new SpaceDef(20f,3f,0),new SpaceDef(20f,3f,1)},{new SpaceDef(20f,3f,2)},
                                           {new SpaceDef(20f,4f,-2),new SpaceDef(20f,4f,-1),new SpaceDef(20f,4f,0),new SpaceDef(20f,4f,1)},{new SpaceDef(20f,4f,2)},
                                           {new SpaceDef(20f,5f,-2),new SpaceDef(20f,5f,-1),new SpaceDef(20f,5f,0),new SpaceDef(20f,5f,1)},{new SpaceDef(20f,5f,2)},
                                           {new SpaceDef(20f,6f,-2),new SpaceDef(20f,6f,-1),new SpaceDef(20f,6f,0),new SpaceDef(20f,6f,1)},{new SpaceDef(20f,6f,2)},
                                           {new SpaceDef(20f,7f,-2),new SpaceDef(20f,7f,-1),new SpaceDef(20f,7f,0),new SpaceDef(20f,7f,1)},{new SpaceDef(20f,7f,2)},    };
       targetPositions = tempTargetPositions5;
        
       Main app = new Main();
        app.start();
    }
    
    //node to hold spatials that can be shot
    private Node shootables;

    @Override
    public void simpleInitApp() {

        
        /** Set up Physics */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.setDebugEnabled(true);

        
        // We re-use the flyby camera for rotation, while positioning is handled by physics
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(100);
        initCrossHairs(); // a "+" in the middle of the screen to help aiming
        setUpKeys();
        //invoke method to initialize the audio soundeffects
        initAudio();
    //light is already done
//        setUpLight();

        
        sceneModel = assetManager.loadModel("Scenes/MyScene.j3o");
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        player.setGravity(new Vector3f(0,-30f,0));
        player.setPhysicsLocation(new Vector3f(0,10,0));
        
        shootables = new Node("Shootables");
        rootNode.attachChild(shootables);
        drawTargets();
    }
    
    private void drawTargets() {
        for (SpaceDef[] outer : targetPositions) {
            for (SpaceDef pos : outer) {
                shootables.attachChild(makeCube("e", pos));
            }
        }
    }

    /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
    inputManager.addListener(this, "Shoot");
  }

  /**
   * Initialize all the aduio sounds for the game
   */
  private void initAudio(){
      gunshot = new AudioNode(assetManager, "Sound/gunshot.wav", DataType.Buffer);
      gunshot.setPositional(false);
      gunshot.setLooping(false);
      gunshot.setVolume(2);
      rootNode.attachChild(gunshot);
      
      ding = new AudioNode(assetManager, "Sound/ding.wav", DataType.Buffer);
      ding.setPositional(false);
      ding.setLooping(false);
      ding.setVolume(2);
      rootNode.attachChild(ding);
  }
  
  /** These are our custom actions triggered by key presses.
   * We do not walk yet, we just keep track of the direction the user pressed. */
  public void onAction(String binding, boolean isPressed, float tpf) {
    if (binding.equals("Left")) {
      left = isPressed;
    } else if (binding.equals("Right")) {
      right= isPressed;
    } else if (binding.equals("Up")) {
      up = isPressed;
    } else if (binding.equals("Down")) {
      down = isPressed;
    } else if (binding.equals("Jump")) {
      if (isPressed) { player.jump(new Vector3f(0,20f,0));}
    } else if (binding.equals("Shoot") && !isPressed) {
        //play the gunshot sound
        gunshot.playInstance();
        //add one to the total shots counter
        numShots++;
        // 1. Reset results list. CollisionResults is an arrayList of objects 
        //    of type CollisionResult
        CollisionResults results = new CollisionResults();
        // 2. Draw a ray from the camera location in the camera direction
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        shootables.collideWith(ray, results);
        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
          float dist = results.getCollision(i).getDistance();
          Vector3f pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
          System.out.println("* Collision #" + i);
          System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        }
        // 5. If something was hit
        if (results.size() > 0) {
          ding.playInstance(); //play ding sound effect
          //add one to the num targets hit counter
          targetsHit++;
          // The closest collision point is what was truly hit:
          CollisionResult closest = results.getClosestCollision();
          //remove it from shootables
          shootables.detachChild(closest.getGeometry());
          
        }
    }
  }
  
  public void newTarget() {
      
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
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        player.setWalkDirection(walkDirection);
        cam.setLocation(player.getPhysicsLocation());
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    /** A centered plus sign to help the player aim. */
    protected void initCrossHairs() {
        crosshairIndex = SettingsWindow.getCrosshairIndex();
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        if(crosshairIndex == 0){ //if the user selected a + shape
            ch.setText("+"); // create + shape crosshair
        }else if (crosshairIndex == 1){ //if the user selected the o shape
            ch.setText("o"); //create o shape crosshair
        }
        ch.setLocalTranslation( // center
          settings.getWidth() / 2 - ch.getLineWidth()/2,
          settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
        guiNode.attachChild(ch);
    }

    /** A cube object for target practice */
    protected Geometry makeCube(String name, SpaceDef pos) {
        Box box = new Box(0.5f, 0.5f, 0.5f);
        Geometry cube = new Geometry(name, box);
        cube.setLocalTranslation(pos.getX(), pos.getY(), pos.getZ());
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        return cube;
    }

}