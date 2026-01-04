package olympus.ascension;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.UBJsonReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;


public class OlympusAscension extends Game implements ApplicationListener {
    // <editor-fold defaultstate="collapsed" desc="Variables">
    public PerspectiveCamera camera;
    public PerspectiveCamera skyCamera;
    public ModelBatch modelBatch;
    public Model model;
    public Environment environment;
    public CameraInputController camController;
    public AssetManager manager;
    private Player player;
    private Sky sky;
    
    String pathToPythonCode = "C:\\Users\\m_che\\Sofie OneDrive\\OneDrive\\Documents\\PycharmProjects\\OlympusAscensionController\\main.py";
    String pythonPath = "C:\\\\Users\\\\m_che\\\\AppData\\\\Local\\\\Programs\\\\Python\\\\Python311\\\\python.exe";
    private BufferedReader bufferedReader;
    private String gameMovements;
    private String detectedPose;
    private Process p;
    private ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    private Thread outputListenerThread;
    private Thread outputListenerThread2;
    
    private boolean playerMoving = false;
    private boolean playerJumping = false;
    private boolean playerLeft = false;
    private boolean playerRight = false;
    private boolean playerRolling = false;
    private boolean playerFalling = false;
    private boolean playerRebounding = false;
    private boolean playerUpARamp = false;
    private long startOfAnActionTime = 0;
    private long startOfSideMove = 0;
    private float originalXPos = 0;
    
    public static int highscore = 0;
    public static int currentscore = 0;
    
    public static boolean endOfGame = false;
    
    private static final int PATHWAY_LENGTH = 9;
    private static final double MIN_SECONDS_BETWEEN_JUMPS = 3.0;
    private static final float JUMPING_INITIAL_VELOCITY = 200f; //cm per s, as libgdx uses cm for units
    private static final float GRAVITY = -(380f);
    
    private static boolean moveRollDown = false;
    private static final float ROLL_TIME = 2f; // seconds
    private static final float ROLL_DOWN_SPEED = 70f;
    
    private static float SIDE_MOVING_SPEED = 150f; //150f
    private static final float SIDE_POSITIONS_LOCATIONS = 634.1757f;
    public static final float GROUND_DISPLACEMENT = 25f;
    
    private static final float JUMP_DURATION = 1.0f; //total time for a jump start to finish
    private static float LOWEST_JUMP_HEIGHT = GROUND_DISPLACEMENT;
    private static float ORIG_JUMP_HEIGHT = GROUND_DISPLACEMENT;
    private static final float JUMP_PEAK_HEIGHT = 500f; //max height for a jump 
    
    
    private static final float EASY_DIFF_SPEED = 500f;
    private static final float MED_DIFF_SPEED = 700f;
    private static final float HARD_DIFF_SPEED = 1000f;
    private static float PLAYER_SPEED;
    
    private static final float leftLaneXPos = SIDE_POSITIONS_LOCATIONS;
    private static final float centerLaneXPos = 0f;
    private static final float rightLaneXPos = -SIDE_POSITIONS_LOCATIONS;
    
    private static String side = "center";
    
    //private float collisionStartTime1 = 0;
    //private float collisionEndTime1 = 0;
    //private float collisionStartTime2 = 0;
    //private float collisionLeewayTime = 1000000000f;
    private float collisionNum = 0;
    
    private static Obstacle leftLaneTaken;
    private static Obstacle rightLaneTaken;
    private static Obstacle centerLaneTaken;
    private static boolean lampPostExists;
    
    private static int direction;
    
    private static boolean isPaused = false;
    
    private ArrayList<MosaicPath> pathways;
    private ArrayList<LowBarrier> lowBarriers;
    private ArrayList<MediumBarrier> mediumBarriers;
    private ArrayList<HighBarrier> highBarriers;
    private ArrayList<LampPost> lampPosts;
    private ArrayList<Ramp> ramps;
    private ArrayList<Carriage> carriages;
    //private ArrayList<testMonkey> testMonkies;
    
    public ArrayList<ArrayList<? extends Obstacle>> common_obstacles = new ArrayList<>();
    public ArrayList<ArrayList<? extends Obstacle>> occasional_obstacles = new ArrayList<>();
    public ArrayList<ArrayList<? extends Obstacle>> rare_obstacles = new ArrayList<>();
    
    private ArrayList<Obstacle> activeObjects = new ArrayList<>();
    private MosaicPath secondFarthestPath;
    private MosaicPath closestCollisionPath;
    private MosaicPath nextCollisionPath;
    
    private static final String HIGHSCORE_FILE = "highscore.txt";

    private static final float LEEWAY = 75f; //wiggle room when it comes to intersections; the leeway 75f og
    private Set<Obstacle> currentlyColliding = new HashSet<>();
    private BoundingBox rampBox;
    
    private int pathwayCounter = 0;
    //private int monkeyBlocker = 0;
    // </editor-fold>       
    
    public OlympusAscension(String gameMovements, String difficulty) throws IOException {
        this.gameMovements = gameMovements;
        
        ProcessBuilder pb = new ProcessBuilder(pythonPath, "-u", pathToPythonCode, gameMovements);
        p = pb.start();
        
        bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        try {
            // Pause the current thread for 5000 milliseconds (5 seconds)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // This part is executed if the sleep is interrupted
            System.err.println("Sleep was interrupted");
        }
        
        outputListenerThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    messageQueue.offer(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputListenerThread.start();
        
        outputListenerThread2 = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    messageQueue.offer(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputListenerThread2.start();
        
        if(difficulty.equals("Easy")) PLAYER_SPEED = EASY_DIFF_SPEED;
        else if(difficulty.equals("Hard")) PLAYER_SPEED = HARD_DIFF_SPEED;
        else PLAYER_SPEED = MED_DIFF_SPEED;

    }
    
	@Override
	public void create () {            
            //modelBatch is responsible for rendering
            
            modelBatch = new ModelBatch();
            manager = new AssetManager(); //for managing assets
            loadAssets();
            loadHighscore();
            manager.finishLoading();
            
            createObjectPools();
            
            closestCollisionPath = pathways.get(2); 
            nextCollisionPath = pathways.get(3);
            
            player = new Player(manager);
            sky = new Sky(manager);
            sky.getInstance().transform.setTranslation(0, -2100f, 0);
            
            Gdx.input.setInputProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    switch (keycode) {
                        case Input.Keys.W:
                            WPressed();
                            break;
                        case Input.Keys.SPACE:
                            SpacePressed();
                            break;
                        case Input.Keys.A:
                            APressed();
                            break;
                        case Input.Keys.D:
                            DPressed();
                            break;
                        case Input.Keys.S:
                            SPressed();
                            break;
                        case Input.Keys.P:
                            if(isPaused) {
                                isPaused = false;
                                System.out.println("Unpaused");
                            }
                            else {
                                isPaused = true;
                                System.out.println("Paused");
                            }
                            break;
                        case Input.Keys.ESCAPE:
                            dispose();
                             System.exit(0);
                            break;
                        case Input.Keys.Q:
                            dispose();
                            System.exit(0);
                            break;
                            
                    }
                    return true; // return true to indicate the event was handled
                }
                
                public boolean keyUp(int keycode) {
                    switch (keycode) {
                        case Input.Keys.W:
                            WReleased();
                            break;
                    }
                    return true; // return true to indicate the event was handled
                }
            });
            
            /* Camera Set-up */
            //67 degrees is common to use. Other stuff is aspect ratio
            camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); 
            camera.position.set(0f, 1700f, player.getZ()-2500f); //measured in units
            camera.lookAt(0, 1500f, 0);
            //camera.rotate(Vector3.X, 20f);
            camera.near = 1f;
            camera.far = 7000f;
            camera.update();
            
            /* Sky Camera */
            skyCamera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            skyCamera.position.set(camera.position); 
            skyCamera.direction.set(camera.direction);
            skyCamera.near = 1f;
            skyCamera.far = 20000f;
            skyCamera.update();
            
            /* lighting */
            environment = new Environment();
            //environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 1f));
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
            Vector3 lightDirection = new Vector3(camera.direction); // Reverse the camera's direction
            environment.add(new DirectionalLight().set(1f, 1f, 1f, lightDirection.x, lightDirection.y - 50f, lightDirection.z));

            
            /* moving camera */
            //this messes with input processor as well, plus we don't need it
            //camController = new CameraInputController(camera);
            //Gdx.input.setInputProcessor(camController); //to make sure to update it in the render call
	}
        
        private void loadAssets() {
            // <editor-fold defaultstate="collapsed" desc="Mosiac Path">
            /*
            //Mosaic Path
            manager.load("mosaic_path_section.g3db", Model.class);
            //textures
            manager.load("stone tile color.png", Texture.class);
            manager.load("stone tile normals.png", Texture.class);
            
            manager.load("tile 2 color.png", Texture.class);
            manager.load("tile 2 normal.png", Texture.class);
            
            manager.load("cube border color.png", Texture.class);
            manager.load("cube border normal.png", Texture.class);
            
            manager.load("cell fracture color.png", Texture.class);
            
            manager.load("cube circle color.png", Texture.class);
            manager.load("cube circle normal.png", Texture.class);
            
            manager.load("rectangles color.png", Texture.class);
            manager.load("rectangles normal.png", Texture.class);
            
            manager.load("middle cube color.png", Texture.class);
            manager.load("middle cube circle normal.png", Texture.class);
            
            manager.load("outer triangles color.png", Texture.class);
            manager.load("outer triangles normal.png", Texture.class);
            
            manager.load("rocky triangles color.png", Texture.class);
            
            manager.load("mini cube circle color 1.png", Texture.class);
            manager.load("mini cube circle1 normal.png", Texture.class);
            
            manager.load("mini cube cicle 2 color.png", Texture.class);
            manager.load("mini cube cicle 2 normal.png", Texture.class);
            
            manager.load("mini triangles color.png", Texture.class);
            manager.load("mini triangles normal.png", Texture.class);
            
            manager.load("star color.png", Texture.class);
            manager.load("star normal.png", Texture.class);
            
            
            manager.load("Marble_Carrara_002_COLOR.jpg", Texture.class);

            //end
            */
            manager.load("room.g3db", Model.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Player">
            //Player (currently temp)
            manager.load("bunny.g3db", Model.class);
            //manager.load("jakey.png", Texture.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Low Barrier">
            //Low Barrier
            /*
            manager.load("barrel.g3db", Model.class);
            
            manager.load("rough_wood_diff_4k.jpg", Texture.class);
            manager.load("Metal046A_1K-JPG_Color.jpg", Texture.class);
            */
            
            manager.load("ball.g3db", Model.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Medium Barrier">
            //Medium Barrier
            //manager.load("table.g3db", Model.class);
            manager.load("toytable.g3db", Model.class);
            //manager.load("wood_table_worn_diff_4k.jpg", Texture.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="High Barrier">
            //High Barrier
            /*
            manager.load("trellis.g3db", Model.class);
            manager.load("Marble001_2K-JPG_Color.jpg", Texture.class);
            manager.load("Pot textures_col.jpg", Texture.class);
            manager.load("Carpet016_1K-JPG_Color.jpg", Texture.class);
            manager.load("bpng.jpg", Texture.class);
            */
            manager.load("hightower.g3db", Model.class);
            //manager.load("main.png", Texture.class);
            
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Lamp Post">
            //Lamp Post (currently temp)
            //manager.load("streetlamp.g3db", Model.class);
            manager.load("toylamp.g3db", Model.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ramp">
            //Ramp
            //manager.load("planks.g3db", Model.class);
            manager.load("toyramp.g3db", Model.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Carriage">
            //Carriage
            /*
            manager.load("carriage.g3db", Model.class);
            manager.load("cloth.jpg", Texture.class);
            */
            manager.load("toytrain.g3db", Model.class);
            //manager.load("trains.png", Texture.class);
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Sky">
            //Sky
            manager.load("sunset.g3db", Model.class);
            // </editor-fold>
            /*
            // <editor-fold defaultstate="collapsed" desc="Test Monkey">
            //Carriage (currently temp)
            manager.load("testmonkey.g3db", Model.class);
            // </editor-fold>
            */
        }
        
        public static void appTextureToMaterial(ModelInstance instance, String materialName, Texture diffuse, Texture normal) {
            Material material = findMaterial(instance, materialName);
            if(diffuse != null) {
                //material.set(TextureAttribute.createDiffuse(diffuse));
               
                // Flip the V coordinate for the diffuse texture
                TextureAttribute diffuseAttr = new TextureAttribute(TextureAttribute.Diffuse, diffuse);
                diffuseAttr.offsetU = 0; // No offset in U
                diffuseAttr.offsetV = 1; // Offset V to flip
                diffuseAttr.scaleU = 1;  // No scaling in U
                diffuseAttr.scaleV = -1; // flip
                material.set(diffuseAttr);
            }
            if(normal != null) {
                material.set(TextureAttribute.createNormal(normal));
            }
        }
        
        public static Material findMaterial(ModelInstance instance, String materialName) {
            for (Material material : instance.materials) {
                if (material.id.equals(materialName)) {
                    return material;
                }
            }
            return null; //if needed throw an error here
        }
        
        public static Vector3 getModelPosition(ModelInstance modelInstance) {
            Vector3 position = new Vector3();
            modelInstance.transform.getTranslation(position);
            return position;
        }
	@Override
	public void render () {
            //camController.update(); //moving camera
            
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            
            //render sky (separately so depth perception is different
            modelBatch.begin(skyCamera);
            modelBatch.render(sky.getInstance(), environment);
            modelBatch.end();
            
            //model rendering
            modelBatch.begin(camera);
            for(MosaicPath path : pathways) {
                modelBatch.render(path.getInstance(), environment);
            }
            modelBatch.render(player.getInstance(), environment);
            
            //render all obstacles
            for(Obstacle obstacle : activeObjects) {
                modelBatch.render(obstacle.getInstance(), environment);
            }
            
            modelBatch.end();
            
            if(!isPaused) {
            
            //check for messages in the python console
            checkForPythonUpdates();
            
            //player movement
            if(playerMoving) {
                float Zmovement = PLAYER_SPEED * Gdx.graphics.getDeltaTime();
                currentscore += 1;
                for (MosaicPath pathway : pathways) {
                    pathway.getInstance().transform.setTranslation(pathway.getX(), pathway.getY(), pathway.getZ() - Zmovement);
                    //System.out.println(pathway.getZ());
                }
                
                for (Obstacle obstacle : activeObjects) {
                    obstacle.getInstance().transform.setTranslation(obstacle.getX(), obstacle.getY(), obstacle.getZ() - Zmovement);
                }
                
            }
            
            //moving left or right
            if(playerLeft) {
                direction = 1;
                updateSidewaysMove(direction);
            } else if (playerRight) {
                direction = -1;
                updateSidewaysMove(direction);
            }
            
            //player falling
            if(playerFalling) playerFallToGround();
            
            //player going up a ramp
            if(playerUpARamp) playerUpARamp(rampBox);
            
            //playerRolling
            if(playerRolling) playerRolling();
            
            
            //endless path
            if(player.getZ() >= (pathways.get(0).getZ() + pathways.get(0).getDepth()) + 1640f) { //it does not matter what pathway we get the depth from, since they all have the same dimensions 
                MosaicPath temp = pathways.get(0);
                
                //check if active objects are on that pathway
                for(int i = 0; i < activeObjects.size(); i++) {
                    if(activeObjects.get(i).getZ() == temp.getZ()) {
                        temp.getAssociatedObstacles().remove(activeObjects.get(i));
                        activeObjects.remove(activeObjects.get(i));
                        i--;
                    }
                }
                System.out.println("Path removed. Active objects count: " + activeObjects.size());
                
                temp.getInstance().transform.setTranslation(temp.getX(), temp.getY(),
                        pathways.get(PATHWAY_LENGTH - 1).getZ() + temp.getDepth());
                pathways.remove(0);
                pathways.add(temp);
                
                lampPostExists = false;
                centerLaneTaken = null;
                leftLaneTaken = null;
                rightLaneTaken = null;
                
                secondFarthestPath = pathways.get(pathways.size() - 2);
                closestCollisionPath = pathways.get(2);
                nextCollisionPath = pathways.get(3);
                
                pathwayCounter++;
                System.out.println("Pathway #" + pathwayCounter);
                
                //monkeyBlocker = 0;
                
                int runAmount = (int)(Math.random() * 4); //[0, 3] (0 for chance to not run)
                if(runAmount == 0) System.out.println("Decide Obstacle did not run this round at all");
                for(int i = 0; i < runAmount; i++) {
                    decideObstacle();
                }
            }
            
            updateAllBoundingBoxes();
            checkForCollisions();
            
            //jumping
            if(playerJumping) updateJump();
            
            //System.out.println(player.getY());
            
            }
	}
        
        public void checkForPythonUpdates() {
            while (!messageQueue.isEmpty()) {
                String message = messageQueue.poll();
                // Process the message, update game state, etc.
                System.out.println(message);
                
                switch (message) {
                    case "Marches started!":
                        WPressed();
                        break;
                    case "Marches stopped.":
                        if(!(playerJumping || playerRolling || playerFalling || playerLeft || playerRight)) WReleased();
                        break;
                    case "Jumping Jack performed!":
                        SpacePressed();
                        break;
                    case "Right Step Touch performed!":
                        side = "right";
                        DPressed();
                        break;
                    case "Left Step Touch performed!":
                        side = "left";
                        APressed();
                        break;
                    case "Center Step Touch performed!":
                        if(side.equals("right")) APressed();
                        else if(side.equals("left")) DPressed();
                        else if(side.equals("center")) System.out.println("Already Center!");
                        side = "center";
                    case "Burpee performed!":
                        SPressed();
                        break;
                    default:
                        break;
                }
            }
        }
        
        private void updateJump() {
        
        if (player.isOnTopOfObstacle() && (player.isOnTopOfObstacle() != player.wasPreviousTopOfObsCase())) {
            player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT, player.getZ());
            playerJumping = false;
            player.setPrevOnTopOfObs(player.isOnTopOfObstacle());
            return;
        } else {
            player.setPrevOnTopOfObs(player.isOnTopOfObstacle());
        }

        float passedTime = (TimeUtils.nanoTime() - startOfAnActionTime) / 1000000000f; // conv to seconds
        float progress = passedTime / JUMP_DURATION; //  progress of the jump

        // calc current height based on the sine wave(only for the upward part of the jump)
        float currentHeight = (float) Math.sin(Math.PI * progress) * JUMP_PEAK_HEIGHT;
        float totalJumpHeight = ORIG_JUMP_HEIGHT + currentHeight;

        if (currentHeight <= 0) {
            // Start a constant descent
            float descentSpeed = 3000f * Gdx.graphics.getDeltaTime(); // cosnt descent speed
            float newHeight = player.getY() - descentSpeed;

            if (newHeight <= LOWEST_JUMP_HEIGHT) {
                if (newHeight <= GROUND_DISPLACEMENT) { // check if player should stop descending
                    playerJumping = false;
                    player.getInstance().transform.setTranslation(player.getX(), GROUND_DISPLACEMENT, player.getZ());
                } else {
                    // continue descending until we hit the ground or another object
                    player.getInstance().transform.setTranslation(player.getX(), newHeight, player.getZ());
                }
            } else {
                // Continue descending at const speed
                player.getInstance().transform.setTranslation(player.getX(), newHeight, player.getZ());
            }
        } else {
            // continue sine wave jump while ascending
            player.getInstance().transform.setTranslation(player.getX(), totalJumpHeight, player.getZ());
        }

        // updateAllBoundingBoxes();
    }
        
        private void updateSidewaysMove(int direction) {
            player.getInstance().transform.setTranslation(player.getX() + (direction * SIDE_MOVING_SPEED), player.getY(), player.getZ());
            //System.out.println(player.getX());
            if(playerRebounding) {
                if((player.getX() <= originalXPos && direction == -1) || (player.getX() >= originalXPos && direction == 1)) {
                    player.getInstance().transform.setTranslation((originalXPos), player.getY(), player.getZ());
                    playerLeft = false;
                    playerRight = false;
                    playerRebounding = false;
                    SIDE_MOVING_SPEED = 150f; //back to default (for reg moving)
                }
            } else {
            
                if(player.getX() <= (originalXPos - SIDE_POSITIONS_LOCATIONS) && direction == -1) {
                    player.getInstance().transform.setTranslation((originalXPos - SIDE_POSITIONS_LOCATIONS), player.getY(), player.getZ());
                    playerLeft = false;
                    playerRight = false;
                } else if (player.getX() >= (originalXPos + SIDE_POSITIONS_LOCATIONS) && direction == 1) {
                    player.getInstance().transform.setTranslation((originalXPos + SIDE_POSITIONS_LOCATIONS), player.getY(), player.getZ());
                    playerLeft = false;
                    playerRight = false;
                }
            }
        }
        
        private void checkForCollisions() { 
            ArrayList<Obstacle> upcomingObstacles = new ArrayList<>();
            upcomingObstacles.addAll(closestCollisionPath.getAssociatedObstacles()); //Next closest path to player
            upcomingObstacles.addAll(nextCollisionPath.getAssociatedObstacles()); //2nd closest path to player
           
            BoundingBox playerBox = player.getBoundingBox();
            //playerBox.min.sub(LEEWAY, LEEWAY, LEEWAY);  // Expand min bounds
            //playerBox.max.add(LEEWAY, LEEWAY, LEEWAY);  // Expand max bounds
            
            for(Obstacle nextObst : upcomingObstacles) {
               BoundingBox barrierBox = nextObst.getBoundingBox();
                //barrierBox.min.sub(LEEWAY, LEEWAY, LEEWAY);  // Expand min bounds
                //barrierBox.max.add(LEEWAY, LEEWAY, LEEWAY);  // Expand max bounds
               if(playerBox.intersects(barrierBox)) {
                   //Collision Start
                   if(!currentlyColliding.contains(nextObst)) {
                       System.out.println("Collision start");
                       /*
                       if(collisionNum == 0) {
                           collisionNum = 1;
                           collisionStartTime1 = TimeUtils.nanoTime();
                       } else if(collisionNum == 1) {
                           collisionNum = 0;
                           //collisionStartTime2 = TimeUtils.nanoTime();
                       }*/
                       currentlyColliding.add(nextObst);
                       
                       
                       
                        if(nextObst instanceof LowBarrier) {
                            if(playerBox.min.y < barrierBox.max.y && Math.abs(playerBox.min.y - barrierBox.max.y) <= LEEWAY) { 
                                LOWEST_JUMP_HEIGHT = barrierBox.max.y - 20f;
                                player.setOnTopOfObstacle(true);
                            } else {
                                gameEnds();
                                return;
                            }
                            
                        } else if(nextObst instanceof MediumBarrier) {
                            if(playerBox.min.y < barrierBox.max.y && Math.abs(playerBox.min.y - barrierBox.max.y) <= LEEWAY) { 
                                LOWEST_JUMP_HEIGHT = barrierBox.max.y - 20f;
                                player.setOnTopOfObstacle(true);
                            } else if (playerRolling && playerBox.max.z > barrierBox.min.z && Math.abs(playerBox.max.z - barrierBox.min.z) <= LEEWAY) {
                                return;
                            } else {
                                gameEnds();
                                return;
                            }
                        } else if(nextObst instanceof HighBarrier) {
                            if (playerRolling && playerBox.max.z > barrierBox.min.z && Math.abs(playerBox.max.z - barrierBox.min.z) <= LEEWAY) {
                                return;
                            } else {
                                gameEnds();
                                return;
                            }
                        } else if(nextObst instanceof LampPost) {
                            gameEnds();
                            return;
                        } else if(nextObst instanceof Ramp) {
                            if(playerBox.min.x < barrierBox.max.x && Math.abs(playerBox.min.x - barrierBox.max.x) <= LEEWAY) {
                                gameEnds();
                                return;
                            } else {
                                playerUpARamp = true;
                                rampBox = barrierBox;
                                player.setOnTopOfObstacle(true);
                            }
                            

                        } else if(nextObst instanceof Carriage) {
                            if(player.getY() <= barrierBox.max.y && Math.abs(playerBox.min.y - barrierBox.max.y) <= LEEWAY) { 
                                LOWEST_JUMP_HEIGHT = barrierBox.max.y - 20f;
                                player.setOnTopOfObstacle(true);
                            } else {
                                gameEnds();
                                return;
                            }
                        }
                   }
               } else {
                   
                   if (currentlyColliding.contains(nextObst)) { // && ((TimeUtils.nanoTime() - collisionStartTime1) > collisionLeewayTime)
                        //Collision End
                        System.out.println("Collision end");
                        
                        if(nextObst instanceof Carriage || nextObst instanceof Ramp) {
                            for(Obstacle colliding : currentlyColliding) {
                                if(!colliding.equals(nextObst) && colliding instanceof Carriage
                                        && (nextObst.getX() - colliding.getX() <= 0.001)) {
                                    currentlyColliding.remove(nextObst);
                                    return;
                                }
                            }
                        }
                        
                        if(player.isOnTopOfObstacle() && !playerJumping) {
                            if(nextObst instanceof Ramp) playerUpARamp = false;
                            
                            player.setOnTopOfObstacle(false);
                            LOWEST_JUMP_HEIGHT = GROUND_DISPLACEMENT;
                             
                            startOfAnActionTime = TimeUtils.nanoTime();
                            playerFalling = true;
                             
                        } else if (player.isOnTopOfObstacle()) { //so if player is jumping from obstacle
                            if(nextObst instanceof Ramp) playerUpARamp = false;
                            
                            player.setOnTopOfObstacle(false);
                            LOWEST_JUMP_HEIGHT = GROUND_DISPLACEMENT;
                        }

                        if(nextObst instanceof LowBarrier) {
                            
                        } else if(nextObst instanceof MediumBarrier) {

                        } else if(nextObst instanceof HighBarrier) {

                        } else if(nextObst instanceof LampPost) {

                        } else if(nextObst instanceof Ramp) {

                        } 

                         currentlyColliding.remove(nextObst);
                   }
               }
           }
        }
       
        private void playerRolling() {

            if(moveRollDown) {
                if(player.getY() > LOWEST_JUMP_HEIGHT) {
                    player.getInstance().transform.setTranslation(player.getX(), player.getY() - ROLL_DOWN_SPEED, player.getZ());
                } else {
                    player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT-300f, player.getZ());
                }
            } else {
                player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT-300f, player.getZ());
            }
            if((TimeUtils.nanoTime() - startOfAnActionTime) / 1000000000f >= ROLL_TIME) {
                player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT, player.getZ());
                System.out.println("Player has stopped rolling");
                playerRolling = false;
            }
        }
        
        private void playerUpARamp(BoundingBox barrierBox) {
            float rampStartY = barrierBox.min.y;
            float rampStartZ = barrierBox.min.z;
            float rampEndY = barrierBox.max.y;
            float rampEndZ = barrierBox.max.z;
            
            float slope = (rampEndY - rampStartY) / (rampEndZ - rampStartZ);
            
            //point slope form
            float rampYAtPlayerZ = rampStartY + slope * (player.getZ() - rampStartZ);
            LOWEST_JUMP_HEIGHT = rampYAtPlayerZ;
            
            if(rampYAtPlayerZ < rampEndY) {
                player.getInstance().transform.setTranslation(player.getX(), rampYAtPlayerZ, player.getZ());
            } else {
                player.getInstance().transform.setTranslation(player.getX(), rampEndY, player.getZ());
                playerUpARamp = false;
            }   
        }
        
        private void Rebounding() {
            if(playerRight || playerLeft) {
                playerRebounding = true;
                SIDE_MOVING_SPEED = 75f;
                
                if (playerLeft) {
                    playerLeft = false;
                    playerRight = true;
                    direction = -1;
                    updateSidewaysMove(direction);
                } else if (playerRight) {
                    playerLeft = true;
                    playerRight = false;
                    direction = 1;
                    updateSidewaysMove(direction);
                }
            }
        }
        
        private void playerFallToGround() {
            float passedTime = (TimeUtils.nanoTime() - startOfAnActionTime) / 1000000000f; //converts to seconds
            
            float displacement = 0.5f * GRAVITY * passedTime * passedTime; //delta d = (1/2)at^2 (vi = 0)
            
            float currentHeight = player.getY() + displacement;
            
            if(currentHeight <= LOWEST_JUMP_HEIGHT) {
                playerFalling = false;
                player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT, player.getZ());
            } else {
                player.getInstance().transform.setTranslation(player.getX(), currentHeight, player.getZ());
            }
        }
        
        private void updateAllBoundingBoxes() {   
            player.getInstance().calculateBoundingBox(player.getBoundingBox());
            player.getBoundingBox().mul(player.getInstance().transform); //update transformations
            
            for(Obstacle obstacle : activeObjects) {
                obstacle.getInstance().calculateBoundingBox(obstacle.getBoundingBox());
                obstacle.getBoundingBox().mul(obstacle.getInstance().transform);
            }
        }
        
        private void createObjectPools() {
            
            pathways = new ArrayList<>();
            for(int i = 0; i < PATHWAY_LENGTH; i++) {
                MosaicPath pathway = new MosaicPath(manager);
                pathway.getInstance().transform.setToTranslation(0, 0, i * pathway.getDepth() - 1640f);
                pathways.add(pathway);
            }
            
            lowBarriers = new ArrayList<>();
            for(int i = 0; i < LowBarrier.MaxNumOfObject; i++) {
                LowBarrier lowBarrier = new LowBarrier(manager);
                lowBarriers.add(lowBarrier);
            }
            
            mediumBarriers = new ArrayList<>();
            for(int i = 0; i < MediumBarrier.MaxNumOfObject; i++) {
                MediumBarrier mediumBarrier = new MediumBarrier(manager);
                mediumBarriers.add(mediumBarrier);
            }
            
            highBarriers = new ArrayList<>();
            for(int i = 0; i < HighBarrier.MaxNumOfObject; i++) {
                HighBarrier highBarrier = new HighBarrier(manager);
                highBarriers.add(highBarrier);
            }
            
            lampPosts = new ArrayList<>();
            for(int i = 0; i < LampPost.MaxNumOfObject; i++) {
                LampPost lampPost = new LampPost(manager);
                lampPosts.add(lampPost);
            }
            
            ramps = new ArrayList<>();
            for(int i = 0; i < Ramp.MaxNumOfObject; i++) {
                Ramp ramp = new Ramp(manager);
                ramps.add(ramp);
            }
            
            carriages = new ArrayList<>();
            for(int i = 0; i < Carriage.MaxNumOfObject; i++) {
                Carriage carriage = new Carriage(manager);
                carriages.add(carriage);
            }
            /*
            testMonkies = new ArrayList<>();
            for(int i = 0; i < testMonkey.MaxNumOfObject; i++) {
                testMonkey testmonkey = new testMonkey(manager);
                testMonkies.add(testmonkey);
            }*/
            
            common_obstacles.add(lowBarriers);
            common_obstacles.add(mediumBarriers);
            common_obstacles.add(carriages);
            occasional_obstacles.add(highBarriers);
            occasional_obstacles.add(lampPosts);
            occasional_obstacles.add(ramps);
        }
        
        private void decideObstacle() {
            double frequencyDecider = Math.random();
            
            //carriage case                                                                      System.out.println(".");
            if(!secondFarthestPath.findInstanceOfAssociates(Ramp.class).isEmpty() ||
               !secondFarthestPath.findInstanceOfAssociates(Carriage.class).isEmpty()) {
                if(frequencyDecider <= .45) {                                                /* System.out.println("monkies MUST spawn...");*/
                    checkSpecificObstacle(carriages);                                         //testmonkies
                    return;
                } else {                                                                      /*System.out.println("Nuh uh no monkies here");*/ //monkeyBlocker++;
                    frequencyDecider = Math.random();
                }
            }
            
            //Common - 55%      ~~ all add
            //Occasional - 30%  ~~ up to
            //Rare - 15%        ~~ 100%
            if(frequencyDecider < 0.55) { //Common [0 - 0.55), 55%
                checkObstacle(common_obstacles);
                
            } else if(frequencyDecider < 0.85) { //Occasional [0.55 - 0.85), 30%
                checkObstacle(occasional_obstacles);
                
            } else { //Rare [0.85, 1), 15%
                checkObstacle(rare_obstacles);
            }
        }
        
        private void checkObstacle(ArrayList<ArrayList<? extends Obstacle>> frequency_obstacles) { //[frequency]_obstacles
            ArrayList<ArrayList<? extends Obstacle>> occupiedLists = new ArrayList<>();
            
            while(true) {
                ArrayList<? extends Obstacle> obstacleType = new ArrayList<>();
                if(!occupiedLists.containsAll(frequency_obstacles)) { //prevents do-while from running infinitely if no available arraylists left
                    do {
                        obstacleType = frequency_obstacles.get((int) (Math.random() * frequency_obstacles.size()));
                    } while(occupiedLists.contains(obstacleType)); //checks to make sure that you are not hitting that same list that is completely active and not available
                } else {
                    System.out.println("There are absolutely NO available objects in this frequency type right now!"); 
                    return; //simply don't make new obj
                }
                
                for(Obstacle obstacle : obstacleType) {
                    if(!activeObjects.contains(obstacle)) {
                        addObstacle(obstacle);
                        return;
                    }
                }
                occupiedLists.add(obstacleType);
                System.out.println("All objects of " + obstacleType.get(0).getClass() + " type are currently active, limit is hit"); //doesn't matter what object for .get(0), just want the class
                System.out.println("Are all the lists occupied? " + occupiedLists.containsAll(frequency_obstacles));
                System.out.println("Active objects count: " + activeObjects.size());
            }
        }
        
        private void checkSpecificObstacle(ArrayList<? extends Obstacle> specific_obstacle) { 
            boolean isOccupied = false;
            
            if(!isOccupied) {
                for(Obstacle obstacle : specific_obstacle) {
                    if(!activeObjects.contains(obstacle)) {
                        if (obstacle.getClass().equals(Carriage.class)) { //testMonkey.class
                            addToTrainChain(obstacle);
                        } else {
                            addObstacle(obstacle);
                        }
                        return;
                    }
                }
            }
            isOccupied = true;
            System.out.println("All of " + specific_obstacle.getClass() + " is occupied.");
        }
        
        private void addObstacle(Obstacle obstacle) {
            MosaicPath farthestPath = pathways.get(pathways.size() - 1);
            float lastPathwayZ = farthestPath.getZ();
            
            
            //checks for interference with other objects
            if(leftLaneTaken != null && rightLaneTaken != null && centerLaneTaken != null) return; //simply no object then
            if (leftLaneTaken != null && rightLaneTaken != null) {
                if(obstacle.sideLanesOnly()) return; //simply no object then
            }
            if(centerLaneTaken != null) {
                if (obstacle.centerLaneOnly()) return; //simply no object then
            }
            
            //sets positions (randomly if there's options)
            if(obstacle.centerLaneOnly()) {
                obstacle.getInstance().transform.setTranslation(centerLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ);
                centerLaneTaken = obstacle;
                
            } else if(obstacle.sideLanesOnly()) {
                int leftOrRightLane = (int)(Math.random() * 2); //[0.0, 2.0) (2 not included, decimal rounds down to 1)
                switch(leftOrRightLane) {
                    case 0: 
                        obstacle.getInstance().transform.setTranslation(leftLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //0 - left
                        leftLaneTaken = obstacle;
                        break;
                    case 1:
                        obstacle.getInstance().transform.setTranslation(rightLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //1 - right
                        rightLaneTaken = obstacle;
                        break;
                }
                
            /** cases **/
            
            //Lamp Post case
            } else if (obstacle.getClass().equals(LampPost.class)) {
                if (!lampPostExists) {
                    int lampDirection = Math.random() < 0.5 ? -1 : 1; //50% for -1 or 1
                    obstacle.getInstance().transform.setTranslation((SIDE_POSITIONS_LOCATIONS / 2) * lampDirection, GROUND_DISPLACEMENT, lastPathwayZ);
                    lampPostExists = true;
                } else return;
            
            /** cases end **/
            } else { //any lane
                int lanePos = (int)(Math.random() * 3); //[0.0, 3.0)
                switch(lanePos) {
                    case 0: 
                        if(leftLaneTaken != null) return; //simply no object then
                        
                        obstacle.getInstance().transform.setTranslation(leftLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //0 - left
                        leftLaneTaken = obstacle;
                        break;
                    case 1: 
                        if(rightLaneTaken != null) return; //simply no object then
                        
                        obstacle.getInstance().transform.setTranslation(rightLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //1 - right
                        rightLaneTaken = obstacle;
                        break;
                    case 2: 
                        if(centerLaneTaken != null) return; //simply no object then
                        
                        obstacle.getInstance().transform.setTranslation(centerLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //2 - center
                        centerLaneTaken = obstacle;
                        break;
                }
            }
            
            activeObjects.add(obstacle);
            farthestPath.getAssociatedObstacles().add(obstacle);
            if(obstacle instanceof Carriage || obstacle instanceof Ramp) {
                System.out.println("Ramp list size: " + farthestPath.findInstanceOfAssociates(Ramp.class).size());
                System.out.println("Carriage list size: " + farthestPath.findInstanceOfAssociates(Carriage.class).size());
            }
            //System.out.println("Added Obstacle: " + obstacle.getClass() + " in lane (" + obstacle.getX() + ") pathway " + pathwayCounter);
        }
        
        private void addToTrainChain(Obstacle newObstacle) {
            MosaicPath farthestPath = pathways.get(pathways.size() - 1);
            float lastPathwayZ = farthestPath.getZ();
            
            secondFarthestPath = pathways.get(pathways.size() - 2);
            
            ArrayList<Obstacle> rampsAndCarriages = secondFarthestPath.findInstanceOfAssociates(Ramp.class);
            rampsAndCarriages.addAll(secondFarthestPath.findInstanceOfAssociates(Carriage.class));
            for(int i = 0; i < rampsAndCarriages.size(); i++) { // + monkeyBlocker
                Obstacle obstacle = rampsAndCarriages.get(i);
                
                if(Math.abs(obstacle.getX() - rightLaneXPos) < 0.0001) { // to prevent float pointer exceptions; basically just compares if they are equal (enough)
                    if(rightLaneTaken == null || (!(rightLaneTaken instanceof Carriage) && !(rightLaneTaken instanceof Ramp) && !(rightLaneTaken instanceof testMonkey))) {
                        newObstacle.getInstance().transform.setTranslation(rightLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //1 - right
                        
                        //checking if its the impossible train case +  assigning jumpable variable
                        if (newObstacle instanceof Carriage) {
                           if (!canPlaceThirdTrain(newObstacle, obstacle, rightLaneTaken)) return;
                        }
                        
                        activeObjects.remove(rightLaneTaken);
                        rightLaneTaken = newObstacle;
                        activeObjects.add(newObstacle);
                        farthestPath.getAssociatedObstacles().add(newObstacle);
                        System.out.println("Carriage list size: " + farthestPath.findInstanceOfAssociates(Carriage.class).size());
                        System.out.println("Monkey spawned right!");
                        return;
                    }
                } else if(Math.abs(obstacle.getX() - leftLaneXPos) < 0.0001) { //left
                    if(leftLaneTaken == null || (!(leftLaneTaken instanceof Carriage) && !(leftLaneTaken instanceof Ramp) && !(leftLaneTaken instanceof testMonkey))) {
                        newObstacle.getInstance().transform.setTranslation(leftLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //1 - right
                        
                        //checking if its the impossible train case +  assigning jumpable variable
                        if (newObstacle instanceof Carriage) {
                           if (!canPlaceThirdTrain(newObstacle, obstacle, leftLaneTaken)) return;
                        }
                        
                        activeObjects.remove(leftLaneTaken);
                        leftLaneTaken = newObstacle;
                        activeObjects.add(newObstacle);
                        farthestPath.getAssociatedObstacles().add(newObstacle);
                        System.out.println("Carriage list size: " + farthestPath.findInstanceOfAssociates(Carriage.class).size());
                        System.out.println("Monkey spawned left!");
                        return;
                    }
                } else if(Math.abs(obstacle.getX() - centerLaneXPos) < 0.0001){ //center
                    if(centerLaneTaken == null || (!(centerLaneTaken instanceof Carriage) && !(centerLaneTaken instanceof Ramp) && !(centerLaneTaken instanceof testMonkey))) {
                        newObstacle.getInstance().transform.setTranslation(centerLaneXPos, GROUND_DISPLACEMENT, lastPathwayZ); //1 - right
                        
                        //checking if its the impossible train case +  assigning jumpable variable
                        if (newObstacle instanceof Carriage) {
                           if (!canPlaceThirdTrain(newObstacle, obstacle, centerLaneTaken)) return;
                        }
                        
                        activeObjects.remove(centerLaneTaken);
                        centerLaneTaken = newObstacle;
                        activeObjects.add(newObstacle);
                        farthestPath.getAssociatedObstacles().add(newObstacle);
                        System.out.println("Carriage list size: " + farthestPath.findInstanceOfAssociates(Carriage.class).size());
                        System.out.println("Monkey spawned center!");
                        return;
                    }
                }
            }
            /*if(monkeyBlocker >= 3) {
                System.out.println("Monkey blocker case...");
                return;
            }*/
            /*
            System.out.println("No room for a consecutive train");
            System.out.println("CASES:");
            System.out.println("Are any of the lanes null? RightLane: " + (rightLaneTaken == null) + " LeftLane: " + (leftLaneTaken == null) + " CenterLane: " + (centerLaneTaken == null));
            System.out.println("RIGHT LANE... Not carrage? " + !(rightLaneTaken instanceof Carriage) + " Not ramp? " + !(rightLaneTaken instanceof Ramp) + " Not testMonkey? " + !(rightLaneTaken instanceof testMonkey));
            System.out.println("LEFT LANE... Not carrage? " + !(leftLaneTaken instanceof Carriage) + " Not ramp? " + !(leftLaneTaken instanceof Ramp) + " Not testMonkey? " + !(leftLaneTaken instanceof testMonkey));
            System.out.println("CENTER LANE... Not carrage? " + !(centerLaneTaken instanceof Carriage) + " Not ramp? " + !(centerLaneTaken instanceof Ramp) + " Not testMonkey? " + !(centerLaneTaken instanceof testMonkey));
            */
        }
        
        /*
        for the super super super rare case that there will be 3 trains in a row. This is to prevent impossible scenarios.
        3 trains are allowed in a row IF......
        SCENARIOS ALLOWED:
        [movement direction]
        [train] [train] [train]
        [empty] [empty] [jumpable obj] --> if theres a jumpable obj anywhere in this row, then the player can use it to jump over the 3 trains
        [player]
        
        [movement direction]
        [train] [train] [train]
        [empty] [empty] [train] --> this row has NO jumpable objects, but has a train behind it. meaning... if train has jumpable obj before it, 
                                    player can jump on that and then walk on this train to go over the 3-train row.
        [empty] [empty] [jumpable obj] --> jumpable obj somewhere in this row
        [player]
        
        SCENARIOS NOT ALLOWED:
        [movement direction]
        [train] [train] [train]
        [empty] [empty] [empty] --> or... theres just no jumpable objects in this row (like a high barrier)
        [player]
        
        [movement direction]
        [train] [train] [train]
        [empty] [empty] [train]
        [empty] [empty] [train] 
        [empty] [empty] [jumpable obj] --> jumpable obj somewhere in this row
        [player]
        
        ^ that case is too much train walking to get over those 3 trains. Since the train behind the 3 train row doesn';t have any jumpable objs, 
          then this is not allowed to happen even though the train behind that one does have a jumpable obj.
        
        */
        private boolean canPlaceThirdTrain(Obstacle newObstacle, Obstacle obstacleInPreviousRow, Obstacle laneTaken) {
            // count if this is the 3rd train
            int trainCount = 0;
            if (obstacleInPreviousRow instanceof Carriage) trainCount++;
            if (laneTaken instanceof Carriage) trainCount++;
            if (trainCount < 2) {
                boolean currentHasJumpable = (laneTaken instanceof LowBarrier ||laneTaken instanceof MediumBarrier ||laneTaken instanceof Ramp);
                ((Carriage) newObstacle).setHasJumpableObjectBehind(currentHasJumpable); //if its a train it will set if it has a jumpable obj behind it
                return true;
            }
            //otherwise... jumpable checks
            boolean hasJumpable = (laneTaken instanceof LowBarrier || laneTaken instanceof MediumBarrier || laneTaken instanceof Ramp);

            boolean trainBehindHasJumpable = false;
            if (obstacleInPreviousRow instanceof Carriage) {
                trainBehindHasJumpable = ((Carriage) obstacleInPreviousRow).hasJumpableObjectBehind();
            }

            if (!hasJumpable && !trainBehindHasJumpable) {
                System.out.println("Can't place 3rd train. Impossible scenario!!");
                return false;
            }

            ((Carriage) newObstacle).setHasJumpableObjectBehind(hasJumpable);
            return true;
        }    
        
        private void loadHighscore() {
        File file = new File(HIGHSCORE_FILE);
            if (file.exists()) {
                try (Scanner scanner = new Scanner(file)) {
                    if (scanner.hasNextInt()) {
                        highscore = scanner.nextInt();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void saveHighscore() {
            try (PrintWriter writer = new PrintWriter(new FileWriter(HIGHSCORE_FILE))) {
                writer.println(highscore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public void WPressed() {
            if(!endOfGame) playerMoving = true;
        }
        
        public void SpacePressed() {
            //if(((TimeUtils.nanoTime() - startOfAnActionTime) / 1000000000) >= MIN_SECONDS_BETWEEN_JUMPS) { //checks if enough time has passed since last jump. startOfJumpTime represents time at start of last jump.
            if (!endOfGame && !playerJumping && (!playerLeft && !playerRight) && !playerFalling && player.getY() <= LOWEST_JUMP_HEIGHT) {
                if (playerRolling) {
                    player.getInstance().transform.setTranslation(player.getX(), LOWEST_JUMP_HEIGHT, player.getZ());
                    System.out.println("Player has stopped rolling");
                    playerRolling = false;
                    return;
                }
                originalXPos = player.getX();
                startOfAnActionTime = TimeUtils.nanoTime();
                ORIG_JUMP_HEIGHT = player.getY();
                if (playerUpARamp) {
                    playerUpARamp = false;
                }
                playerJumping = true;

            } else {
                System.out.println("Jumping too soon! Its been " + ((TimeUtils.nanoTime() - startOfAnActionTime) / 1000000000f) + " seconds");
            }
        }
        
        public void APressed() {
            if (!endOfGame && (!playerLeft && !playerRight) && (player.getX() < SIDE_POSITIONS_LOCATIONS)) {
                originalXPos = player.getX();
                startOfSideMove = TimeUtils.nanoTime();
                playerLeft = true;
            }
        }

        public void DPressed() {
            if (!endOfGame && (!playerLeft && !playerRight) && (player.getX() > -SIDE_POSITIONS_LOCATIONS)) {
                originalXPos = player.getX();
                startOfSideMove = TimeUtils.nanoTime();
                playerRight = true;
            }
        }

        public void SPressed() {
            if(!endOfGame) {

                if (playerJumping || playerFalling) {
                    playerJumping = false;
                    playerFalling = false;
                    moveRollDown = true;
                }
                System.out.println("Player has started to roll");
                startOfAnActionTime = TimeUtils.nanoTime();
                playerRolling = true;
            }
        }

        public void WReleased() {
            playerMoving = false;
        }
        
        public void gameEnds() {
            endOfGame = true;
            WReleased();
            Rebounding();
            
            if (currentscore > highscore) {
                highscore = currentscore;
            }
            
            new Thread(() -> {
                try {
                    Thread.sleep(1000);  // Sleep for 1 second(s)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
                endGame.gameOver();
            }).start();
        }
        
	@Override
	public void dispose() {
            
            System.out.println("Disposing..");
            System.out.println("THE HIGHSCORE = " + highscore);
            saveHighscore();
            if (p != null && p.isAlive()) {  
                p.destroy(); 
                try {
                    p.waitFor();  // Wait for the process to terminate
                    outputListenerThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore the interrupted status
                    System.err.println("Failed to wait for the Python process to terminate (or bufferedReader error).");
                }
            }
        
        
            modelBatch.dispose();
            manager.dispose();
	}
	
	@Override
	public void resume () {
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void pause () {
	}
        
}