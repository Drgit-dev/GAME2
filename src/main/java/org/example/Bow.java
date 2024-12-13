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

public class Bow {
    public final int spriteWidth = 15, spriteHeight = 80;
    private int spriterow;
    private int spritecol;
    private static final String PATH_TO_TILE_SPRITESHEET = "";
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
        // Guardar la transformación original para restaurarla después
        AffineTransform originalTransform = g2d.getTransform();

        // Rotar alrededor del centro de la pantalla
        g2d.rotate(angle, centerX, centerY); // Rotar alrededor del centro especificado

        // Dibujar un rectángulo representando el arco directamente en el centro
        g2d.setColor(Color.BLUE); // Cambiar el color del arco
        g2d.fillRect(centerX - spriteWidth / 2, centerY - spriteHeight / 2, spriteWidth, spriteHeight); // Dibujar centrado

        // Restaurar la transformación original
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