
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

public class Sky {
    
    private ModelInstance instance;
    private String filePath = "sunset.g3db";
    private AssetManager manager;
    
    private float x;
    private float y;
    private float z;
    
    private static final float SCALE_FACTOR = 1.5f;
    
    public Sky(AssetManager manager) {
        super();
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model); 
        this.instance.transform.scl(SCALE_FACTOR);
        this.instance.transform.rotate(Vector3.Y, -93);
        
        manager.finishLoading();
        setUp();

    }
    
    private void setUp() {
        Texture sunset = manager.get("sunset.jpg", Texture.class);
        OlympusAscension.appTextureToMaterial(instance, "Material.001", sunset, null);
        Material material = instance.materials.get(0);
        material.set(TextureAttribute.createEmissive(sunset));
    }
    
    public ModelInstance getInstance() {
        return instance;
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
}
