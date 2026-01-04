package olympus.ascension;

import com.badlogic.gdx.Gdx;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static olympus.ascension.OlympusAscension.currentscore;
import static olympus.ascension.OlympusAscension.highscore;

public class endGame {

    public static void gameOver() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Over!");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setAlwaysOnTop(true);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(new Color(173, 216, 230));

            JLabel gameOverLabel = new JLabel("Game Over!", JLabel.CENTER);
            gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalGlue());
            panel.add(gameOverLabel);

            JLabel highScoreLabel = new JLabel("High Score: " + highscore, JLabel.CENTER);
            JLabel currentScoreLabel = new JLabel("Current Score: " + currentscore, JLabel.CENTER);
            highScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            currentScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(highScoreLabel);
            panel.add(currentScoreLabel);

            JButton restartButton = new JButton("Restart");
            restartButton.setMaximumSize(new Dimension(150, 40));
            restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            restartButton.setBackground(new Color(255, 99, 71));
            restartButton.setOpaque(true);
            restartButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            restartButton.addActionListener(e -> restartGame());

            panel.add(restartButton);
            panel.add(Box.createVerticalGlue());

            frame.add(panel);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
        });
        Gdx.app.exit();
    }

    private static void restartGame() {
        try {
            // Get current Java executable path
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String classPath = System.getProperty("java.class.path");
            String mainClass = "olympus.ascension.DesktopLauncher";

            // restart the game
            ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classPath, mainClass);
            builder.start();

            // exit current application
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
