
package olympus.ascension;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.TimeUtils;

public class Player {
    
    private ModelInstance instance;
    private String filePath = "bunny.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    private float x;
    private float y;
    private float z;
    
    private boolean isOnTopOfObstacle = false;
    private boolean previousTopOfObsCase = false;
    
    //public static final float GROUND_DISPLACEMENT = 25f;
    private static final float SCALE_FACTOR = 75f;
    
    public Player(AssetManager manager) {
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model); 
        this.instance.transform.scl(SCALE_FACTOR);
        this.instance.transform.setTranslation(0, OlympusAscension.GROUND_DISPLACEMENT, 0);
        this.instance.transform.rotate(Vector3.Y, 270);
        manager.finishLoading();
        setUp();
        
        boundingBox = new BoundingBox();
        instance.calculateBoundingBox(boundingBox);
        
        System.out.println("player width: " + getWidth());
        System.out.println("player height: " + getHeight());
        System.out.println("player depth: " + getDepth());
        
        System.out.println("x: " + getX());
        System.out.println("y: " + getY());
        System.out.println("z: " + getZ());
        
    }
    
    private void setUp() {
        //Texture text = manager.get("jakey.png", Texture.class);
        //OlympusAscension.appTextureToMaterial(instance, "Material.001", text, null);
    }
    
    public ModelInstance getInstance() {
        return instance;
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
    public float getWidth() {
        return boundingBox.getWidth() * SCALE_FACTOR; //* SCALE_FACTOR TEMPORARY FOR SCALE
    }
    
    public float getHeight() {
        return boundingBox.getHeight() * SCALE_FACTOR;//* SCALE_FACTOR TEMPORARY FOR SCALE
    }
    
    public float getDepth() {
        return boundingBox.getDepth() * SCALE_FACTOR;//* SCALE_FACTOR TEMPORARY FOR SCALE
    }
    
    public float getX() {
        x = OlympusAscension.getModelPosition(instance).x;
        return x;
    }
    
    public float getY() {
        y = OlympusAscension.getModelPosition(instance).y;
        return y;
    }
    
    public float getZ() {
        z = OlympusAscension.getModelPosition(instance).z;
        return z;
    }
    
    public void setOnTopOfObstacle(boolean onTop) {
        this.isOnTopOfObstacle = onTop;
    }            
    
    public boolean isOnTopOfObstacle() {
        return isOnTopOfObstacle;
    }
    
    public void setPrevOnTopOfObs(boolean onTop) {
        this.previousTopOfObsCase = onTop;
    }            
    
    public boolean wasPreviousTopOfObsCase() {
        return previousTopOfObsCase;
    }
}
