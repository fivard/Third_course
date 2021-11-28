package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.util.SkyFactory;

public class Main extends SimpleApplication {

  public static void main(String args[]) {
    Main app = new Main();
    app.start();
  }

  /** Prepare the Physics Application State (jBullet) */
  private BulletAppState bulletAppState;
  private static int gravity = 10;

  Material stone_mat;
  Material floor_mat;

  /** Prepare geometries and physical nodes for bricks and cannon balls. */
  private RigidBodyControl      ballPhy;
  private static final Sphere   sphere;
  private RigidBodyControl      floorPhy;
  private static final Box      floor;
  
  private static final Cylinder cannon;
  
  private BitmapText txt; 

  static {
    
    sphere = new Sphere(32, 32, 0.4f, true, false);
    sphere.setTextureMode(TextureMode.Projected);
    
    floor = new Box(20, 0.1f, 100f);
    floor.scaleTextureCoordinates(new Vector2f(50, 10));
    
    cannon = new Cylinder(20, 50, 1, 2, true);
    cannon.scaleTextureCoordinates(new Vector2f(10, 20));
  }

  @Override
  public void simpleInitApp() {
    
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);

    cam.setLocation(new Vector3f(0, 4f, 6f));
    cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
    rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/BrightSky.dds", SkyFactory.EnvMapType.CubeMap));
    inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping("IncreaseGravity", new KeyTrigger(KeyInput.KEY_R));
    inputManager.addMapping("DecreaseGravity", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addListener(actionListener, "Shoot", "IncreaseGravity", "DecreaseGravity");
    
    initMaterials();
    initFloor();
    initCannon();
    initTextGravity();
  }

  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (!keyPressed)
            return;
        switch (name) {
            case "Shoot":
                makeCannonBall();
                break;
            case "IncreaseGravity":
                gravity += 1;
                break;
            case "DecreaseGravity":
                gravity -= 1;
                break;
        }
        txt.setText("Gravity: " + gravity + "m/s^2");
    }
  };

  /** Initialize the materials used in this scene. */
  public void initMaterials() {

    stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
    key2.setGenerateMips(true);
    Texture tex2 = assetManager.loadTexture(key2);
    stone_mat.setTexture("ColorMap", tex2);

    floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
    key3.setGenerateMips(true);
    Texture tex3 = assetManager.loadTexture(key3);
    tex3.setWrap(WrapMode.Repeat);
    floor_mat.setTexture("ColorMap", tex3);
  }

  public void initFloor() {
    Geometry floor_geo = new Geometry("Floor", floor);
    floor_geo.setMaterial(floor_mat);
    floor_geo.setLocalTranslation(0, -0.1f, 0);
    rootNode.attachChild(floor_geo);
    /* Make the floor physical with mass 0.0f! */
    floorPhy = new RigidBodyControl(0.0f);
    floor_geo.addControl(floorPhy);
    bulletAppState.getPhysicsSpace().add(floorPhy);
  }
  
  public void initCannon(){
    Geometry cannonGeo = new Geometry("Cannon", cannon);
    cannonGeo.setMaterial(floor_mat);
    cannonGeo.setLocalTranslation(-10, 0.5f, 0);
    cannonGeo.rotate(0.4f, 0f, 0);
    rootNode.attachChild(cannonGeo);
  }
  
   public void initTextGravity(){
       BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        txt = new BitmapText(fnt, false);
        txt.setBox(new Rectangle(0, 0, 600, 300));
        txt.setQueueBucket(RenderQueue.Bucket.Transparent);
        txt.setSize( 11f );
        txt.setLocalTranslation(0, 20, -100);
        rootNode.attachChild(txt);
        txt.setText("Gravity: " + gravity + "m/s^2");
   }

   public void makeCannonBall() {
    
    Geometry ball_geo = new Geometry("cannon ball", sphere);
    ball_geo.setMaterial(stone_mat);
    rootNode.attachChild(ball_geo);
    
    ball_geo.setLocalTranslation(new Vector3f(-10, 0, 0));
    
    ballPhy = new RigidBodyControl(1f);
    
    ball_geo.addControl(ballPhy);
    bulletAppState.getPhysicsSpace().add(ballPhy);
    
    ballPhy.setLinearVelocity(new Vector3f(0, 0.4f, -1).mult(20));
    ballPhy.setGravity(new Vector3f(0, -gravity, 0));
  }
}