
package olympus.ascension;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.collision.BoundingBox;


class Obstacle {
    private boolean centerLaneOnly;
    private boolean sideLanesOnly;
    
    private float x;
    private float y;
    private float z;
    
    private ModelInstance instance;
    private MosaicPath path;
    
    private BoundingBox boundingBox;
    
    public Obstacle() {
        centerLaneOnly = false;
        sideLanesOnly = false;
    }
    
    public Obstacle(boolean centerLaneOnly, boolean sideLanesOnly) {
        this.centerLaneOnly = centerLaneOnly;
        this.sideLanesOnly = sideLanesOnly;
    }
    
    
    public void setInstance(ModelInstance instance) {
        this.instance = instance;
    }
    
    public ModelInstance getInstance() {
        System.out.println("This should never be ran");
        return instance;
    }
    
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }
    
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
    public boolean centerLaneOnly() {
        return centerLaneOnly;
    }
    
    public boolean sideLanesOnly() {
        return sideLanesOnly;
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
