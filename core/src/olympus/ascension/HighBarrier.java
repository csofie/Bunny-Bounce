
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

public class HighBarrier extends Obstacle {
    
    private ModelInstance instance;
    //private String filePath = "trellis.g3db";
    private String filePath = "hightower.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    
    private static final float SCALE_FACTOR = 30f; //80F FOR TRELLIS.
    
    // Note: COMMON object
    static final int MaxNumOfObject = 4; // maximum number of object, as objects are reused
    
    public HighBarrier(AssetManager manager) {
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
        /*
        System.out.println("high barrier width: " + getWidth());
        System.out.println("high barrier height: " + getHeight());
        System.out.println("high barrier depth: " + getDepth());
        
        System.out.println("x: " + getX());
        System.out.println("y: " + getY());
        System.out.println("z: " + getZ());
        */
        
    }
    
    private void setUp() {
        // <editor-fold defaultstate="collapsed" desc="Textures">
        /*
        Texture leaves = manager.get("bpng.jpg", Texture.class);
        Texture rope = manager.get("Carpet016_1K-JPG_Color.jpg", Texture.class);
        Texture marble = manager.get("Marble001_2K-JPG_Color.jpg", Texture.class);
        Texture pot = manager.get("Pot textures_col.jpg", Texture.class);
        */
        //Texture text = manager.get("main.png", Texture.class);

        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Materials">
        /*
        OlympusAscension.appTextureToMaterial(instance, "Blatt", leaves, null);
        OlympusAscension.appTextureToMaterial(instance, "rope", rope, null);
        OlympusAscension.appTextureToMaterial(instance, "Material", marble, null);
        OlympusAscension.appTextureToMaterial(instance, "Material.002", pot, null);
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
