package olympus.ascension;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.awt.Dimension;
import static java.awt.SystemColor.window;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import olympus.ascension.OlympusAscension;

public class DesktopLauncher {
        private static int width = 960; //Default ratio of W:H is 4:3, and 8:5 for this comp
        private static int height = 600;
        
	public static void main (String[] arg) {
            StartMenu menu = new StartMenu(); 
            menu.getContentPane().setBackground(new java.awt.Color(186, 224, 230));
            
            menu.setVisible(true);

	}
        
        public static void startGame() {
            Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
            config.setForegroundFPS(60);
            config.setTitle("Olympus Ascension");
            config.setWindowedMode(width, height);
            
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
            /*
            // calc the pos for the window to be on the right side of the screen
            int xPos = (1920 - width) - 80;
            int yPos = (1200 - height) - 120; // center vertically

            // Set initial window position
            config.setWindowPosition(xPos, yPos);
            */
            
            config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
            try {
                new Lwjgl3Application(new OlympusAscension(StartMenu.getString_GameMovements(), StartMenu.getGameDifficulty()), config);
            } catch (IOException e) {
                e.printStackTrace();  
            }
        }
        
        
    
}