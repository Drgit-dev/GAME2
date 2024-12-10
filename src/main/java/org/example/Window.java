package org.example;

import javax.swing.*;
import java.awt.*;

public class Window {
    private JFrame frame;

    public Window(String title, GamePanel gamePanel) {
        frame = new JFrame(title);

        // Window setup
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Fullscreen without borders
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height); // Fullscreen
        frame.setLocation(0, 0);

        // Add the game panel
        frame.add(gamePanel);
        frame.setVisible(true);
    }

    public void close() {
        frame.dispose();
    }
}