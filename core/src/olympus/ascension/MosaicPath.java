
package olympus.ascension;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.collision.BoundingBox;
import java.util.ArrayList;

public class MosaicPath {
    
    private ModelInstance instance;
    //private String filePath = "mosaic_path_section.g3db";
    private String filePath = "room.g3db";
    private AssetManager manager;
    private BoundingBox boundingBox;
    
    private float x;
    private float y;
    private float z;
    
    private float betweenPathways = 240f;
    
    private ArrayList<Obstacle> associatedObstacles;
    
    public MosaicPath(AssetManager manager) {
        this.manager = manager;
        Model model = manager.get(filePath, Model.class);
        this.instance = new ModelInstance(model);
                
        manager.finishLoading();
        setUp();
                
        boundingBox = new BoundingBox();
        instance.calculateBoundingBox(boundingBox);
        associatedObstacles = new ArrayList<>();

        /*
        System.out.println("path width: " + getWidth());
        System.out.println("path height: " + getHeight());
        System.out.println("path depth: " + getDepth());
        */
    }
    
    private void setUp() {
        // <editor-fold defaultstate="collapsed" desc="Textures">
        /*
        Texture tile1color = manager.get("stone tile color.png", Texture.class);
        Texture tile1normal = manager.get("stone tile normals.png", Texture.class);

        Texture tile2color = manager.get("tile 2 color.png", Texture.class);
        Texture tile2normal = manager.get("tile 2 normal.png", Texture.class);

        Texture cubeBorderColor = manager.get("cube border color.png", Texture.class);
        Texture cubeBorderNormal = manager.get("cube border normal.png", Texture.class);

        Texture cellFractureColor = manager.get("cell fracture color.png", Texture.class);

        Texture cubeCircleColor = manager.get("cube circle color.png", Texture.class);
        Texture cubeCircleNormal = manager.get("cube circle normal.png", Texture.class);

        Texture rectanglesColor = manager.get("rectangles color.png", Texture.class);
        Texture rectanglesNormal = manager.get("rectangles normal.png", Texture.class);

        Texture middleCubeCircleColor = manager.get("middle cube color.png", Texture.class);
        Texture middleCubeCircleNormal = manager.get("middle cube circle normal.png", Texture.class);

        Texture outerTrianglesColor = manager.get("outer triangles color.png", Texture.class);
        Texture outerTrianglesNormal = manager.get("outer triangles normal.png", Texture.class);

        Texture innerTrianglesColor = manager.get("rocky triangles color.png", Texture.class);

        Texture miniCubeCircle1color = manager.get("mini cube circle color 1.png", Texture.class);
        Texture miniCubeCircle1normal = manager.get("mini cube circle1 normal.png", Texture.class);

        Texture miniCubeCircle2color = manager.get("mini cube cicle 2 color.png", Texture.class);
        Texture miniCubeCircle2normal = manager.get("mini cube cicle 2 normal.png", Texture.class);

        Texture miniTrianglesColor = manager.get("mini triangles color.png", Texture.class);
        Texture miniTrianglesNormal = manager.get("mini triangles normal.png", Texture.class);

        Texture starColor = manager.get("star color.png", Texture.class);
        Texture starNormal = manager.get("star normal.png", Texture.class);
        
        Texture flour = manager.get("Marble_Carrara_002_COLOR.jpg", Texture.class);
        */
        //Texture text = manager.get("main.png", Texture.class);
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Materials">
        /*
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis tile1", tile1color, tile1normal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis tile2", tile2color, tile2normal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis cube border", cubeBorderColor, cubeBorderNormal);
        OlympusAscension.appTextureToMaterial(instance, "rocky", cellFractureColor, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis cube circle", cubeCircleColor,cubeCircleNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis.001", rectanglesColor, rectanglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis middle cube circle", middleCubeCircleColor, middleCubeCircleNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis outer triangles", outerTrianglesColor, outerTrianglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "rocky triangles", innerTrianglesColor, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis mini cube circle", miniCubeCircle1color, miniCubeCircle1normal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis mini cube circle 2", miniCubeCircle2color, miniCubeCircle2normal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis.minitriangles", miniTrianglesColor, miniTrianglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis star", starColor, starNormal);
        */
        /*  //this one idk whats the above one
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis tile1", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis tile2", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis cube border", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "rocky", cellFractureColor, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis cube circle", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis.001", rectanglesColor, rectanglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis middle cube circle", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis outer triangles", outerTrianglesColor, outerTrianglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "rocky triangles", innerTrianglesColor, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis mini cube circle", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis mini cube circle 2", flour, null);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis.minitriangles", miniTrianglesColor, miniTrianglesNormal);
        OlympusAscension.appTextureToMaterial(instance, "idkwhatthisis star", flour, null);
        */
        //OlympusAscension.appTextureToMaterial(instance, "Material.002", text, null);
        //OlympusAscension.appTextureToMaterial(instance, "Material.004", text, null);
        // </editor-fold>
    }
    
    public ModelInstance getInstance() {
        return instance;
    }
    
    public float getWidth() {
        return boundingBox.getWidth();
    }
    
    public float getHeight() {
        return boundingBox.getHeight();
    }
    
    public float getDepth() {
        return boundingBox.getDepth() - betweenPathways;
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
    
    public ArrayList<Obstacle> getAssociatedObstacles() {
        return associatedObstacles;
    }

    public ArrayList<Obstacle> findInstanceOfAssociates(Class<? extends Obstacle> instance_class) {
        
        ArrayList<Obstacle> specific_instances = new ArrayList<>();
        
        for(Obstacle obstacle : associatedObstacles) {
            if(obstacle.getClass().equals(instance_class)) {
                specific_instances.add(obstacle);
            }
        }
        return specific_instances;
        
    }
}
