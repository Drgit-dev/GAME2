package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double angle = 0; // Rotation angle (now unused)
    private final int spriteWidth = 64, spriteHeight = 64; // Changed sprite size to 64x64
    private int spriterow;
    private int spritecol;
    private static final String PATH_TO_TILE_SPRITESHEET =
            "src/main/resources/player/player_sprites.png"; // Path to the sprite sheet
    private BufferedImage spriteSheet;
    private int numberofframes = 3;
    int type=0;
    int[] stats =new int[5];

    public Player(int startX, int startY, int[] stats) {
        this.stats=getStats(type);
        // Load the sprite sheet image
        try {
            spriteSheet = ImageIO.read(new File(PATH_TO_TILE_SPRITESHEET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d, int centerX, int centerY) {
        // Calculate the subimage to draw from the sprite sheet based on spritecol and spriterow
        int x = spritecol * 64; // 64 is the size of the sprite in the sprite sheet
        int y = spriterow * 64;

        // Extract the subimage from the sprite sheet
        BufferedImage sprite = spriteSheet.getSubimage(x, y, 64, 64); // 64x64 sprite size

        // Draw the sprite image at the center of the screen without rotation
        g2d.translate(centerX, centerY); // Move to the center of the screen
        g2d.drawImage(sprite, -spriteWidth / 2, -spriteHeight / 2, null); // Draw the sprite
    }

    public MouseMotionAdapter getMouseMotionListener(JPanel panel) {
        return new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int centerX = panel.getWidth() / 2;
                int centerY = panel.getHeight() / 2;
                double deltaX = e.getX() - centerX;
                double deltaY = e.getY() - centerY;
                angle = Math.atan2(deltaY, deltaX); // Update the angle based on mouse movement
            }
        };
    }

    public void calculateWalkingFrame() {
        // Cycle through sprite columns (spritecol) and reset it to 0 when it reaches 2
        if (spritecol == numberofframes-1) {
            spritecol = 0;
        } else {
            spritecol++;
        }
    }

    public int getAngle() {
        double angleInDegrees = Math.toDegrees(angle);
        int adjustedAngle = (angleInDegrees < 0)
                ? (int) Math.abs(angleInDegrees)  // If the angle is negative, convert to absolute
                : (int) (360 - angleInDegrees);   // If the angle is positive, subtract from 360
        return adjustedAngle;
    }

    public void calculateDirection(int adjustedAngle) {
        // Determine the direction based on the angle and update the spriterow
        if (adjustedAngle >= 300 || adjustedAngle <= 60) {
            spriterow = 0; // Right
        } else if (adjustedAngle > 60 && adjustedAngle <= 120) {
            spriterow = 1; // Up
        } else if (adjustedAngle > 120 && adjustedAngle <= 240) {
            spriterow = 2; // Left
        } else if (adjustedAngle > 240 && adjustedAngle < 300) {
            spriterow = 3; // Down
        } else {
            System.err.println("Error: ERM WHAT THE RIZMA.");
        }
    }

    @Override
    public String toString() {
        return "Player{" +
                "spriterow=" + spriterow +
                ", spritecol=" + spritecol +
                '}';
    }

    public KeyAdapter getKeyListener(GamePanel gamePanel) {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) gamePanel.setUpPressed(true);
                if (key == KeyEvent.VK_S) gamePanel.setDownPressed(true);
                if (key == KeyEvent.VK_A) gamePanel.setLeftPressed(true);
                if (key == KeyEvent.VK_D) gamePanel.setRightPressed(true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) gamePanel.setUpPressed(false);
                if (key == KeyEvent.VK_S) gamePanel.setDownPressed(false);
                if (key == KeyEvent.VK_A) gamePanel.setLeftPressed(false);
                if (key == KeyEvent.VK_D) gamePanel.setRightPressed(false);
            }
        };
    }

    public int getSpritecol() {
        return spritecol;
    }
    public int[] getStats(int type) {
        switch (type) {
            case 0:
                stats[0] = 200;
                stats[1] = 200;
                stats[2] = 50;
                stats[3] = 50;
                stats[4] = 0;
                break;
            case 1:
                stats[0] = 150;
                stats[1] = 150;
                stats[2] = 100;
                stats[3] = 100;
                stats[4] = 2;
                break;
            case 2:
                stats[0] = 250;
                stats[1] = 250;
                stats[2] = 20;
                stats[3] = 20;
                stats[4] = 2;
                break;

        }
        return stats;

    }

}
