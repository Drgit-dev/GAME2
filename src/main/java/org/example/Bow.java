package org.example;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.lang.Math;

public class Bow {
    public final int spriteWidth = 30, spriteHeight = 70;
    private int spriterow;
    private int spritecol;
    private static final String PATH_TO_TILE_SPRITESHEET = "src/main/resources/sprites/bow/bow_0.png";
    private double angle = 0; // Rotation angle
    private BufferedImage spriteSheet;

    public Bow(int startX, int startY) {
        try {
            spriteSheet = ImageIO.read(new File(PATH_TO_TILE_SPRITESHEET));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d, int centerX, int centerY) {

        //will use trignometry to make the bow offset a bit from the character
        int offset = 30;
        double dx = Math.cos(angle) * offset;
        double dy = Math.sin(angle) * offset;

        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(angle-Math.PI, centerX+dx, centerY+dy);
        g2d.drawImage(
                spriteSheet,
                (int) (centerX + dx - spriteWidth / 2),
                (int) (centerY + dy - spriteHeight / 2),
                spriteWidth,spriteHeight, null);
        g2d.setTransform(originalTransform);
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
}