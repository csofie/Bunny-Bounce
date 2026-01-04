
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

public class LowBarrier extends Obstacle {
    
    private ModelInstance instance;
    //private String filePath = "barrel.g3db";
    private String filePath = "ball.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    
    //public static final float GROUND_DISPLACEMENT = 25f;
    private static final float SCALE_FACTOR = 50f;
    
    // Note: COMMON object
    static final int MaxNumOfObject = 6; // maximum number of object, as objects are reused
    
    public LowBarrier(AssetManager manager) {
        super();
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model); 
        this.instance.transform.scl(SCALE_FACTOR);
        //this.instance.transform.rotate(Vector3.Y, 90); //UNDO FOR OLYMPUS ONE
        //this.instance.transform.setTranslation(0, GROUND_DISPLACEMENT, 1000f);
        super.setInstance(instance);
        
        manager.finishLoading();
        setUp();
        
        boundingBox = new BoundingBox();
        instance.calculateBoundingBox(boundingBox);
        super.setBoundingBox(boundingBox);
        
    }
    
    private void setUp() {
        // <editor-fold defaultstate="collapsed" desc="Textures">
        /*
        Texture barrelcolor = manager.get("rough_wood_diff_4k.jpg", Texture.class);
        Texture rimcolor = manager.get("Metal046A_1K-JPG_Color.jpg", Texture.class);
        */
        
        //exture text = manager.get("main.png", Texture.class);
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Materials">
        /*
        OlympusAscension.appTextureToMaterial(instance, "Material.001", barrelcolor, null);
        OlympusAscension.appTextureToMaterial(instance, "Material.002", rimcolor, null);
        */
        
        //OlympusAscension.appTextureToMaterial(instance, "Material.001", text, null);
        // </editor-fold>
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
