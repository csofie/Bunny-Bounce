
package olympus.ascension;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.TimeUtils;

public class LampPost extends Obstacle {
    
    private ModelInstance instance;
    //private String filePath = "streetlamp.g3db";
    private String filePath = "toylamp.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    
    private static final float SCALE_FACTOR = 30f; //50F FOR STREETLAMP
    
    // Note: COMMON object
    static final int MaxNumOfObject = 2; // maximum number of object, as objects are reused
    
    public LampPost(AssetManager manager) {
        super();
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model); 
        this.instance.transform.scl(SCALE_FACTOR);
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
        //Texture lampcolor = manager.get("Metal046A_1K-JPG_Color.jpg", Texture.class);
        //Texture text = manager.get("main.png", Texture.class);
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Materials">
        //OlympusAscension.appTextureToMaterial(instance, "Material.001", lampcolor, null);
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
