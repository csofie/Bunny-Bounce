
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

public class testMonkey extends Obstacle {
    
    private ModelInstance instance;
    private String filePath = "testmonkey.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    
    private static final float SCALE_FACTOR = 100f;
    
    // Note: COMMON object
    static final int MaxNumOfObject = 12; // maximum number of object, as objects are reused
    
    public testMonkey(AssetManager manager) {
        super();
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model); 
        this.instance.transform.scl(SCALE_FACTOR);
        this.instance.transform.rotate(Vector3.Y, 180);
        //this.instance.transform.setTranslation(0, GROUND_DISPLACEMENT, 1000f);
        super.setInstance(instance);
        
        manager.finishLoading();
        setUp();
        
        boundingBox = new BoundingBox();
        instance.calculateBoundingBox(boundingBox);
        super.setBoundingBox(boundingBox);
        
    }
    
    private void setUp() {
        //no textures or materials right now
    }
    
    public ModelInstance getInstance() {
        return instance;
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
}
