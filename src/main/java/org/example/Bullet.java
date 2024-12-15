package org.example;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Bullet {
    private double x, y;
    private final double angle;
    private static final int SPEED = 10;
    private static final int BULLET_SIZE = 40;
    private BufferedImage bulletImage;

    public Bullet(int startX, int startY, Point target) {
        this.x = startX;
        this.y = startY;
        this.angle = Math.atan2(target.y - startY, target.x - startX);
        try {
            bulletImage = ImageIO.read(new File("src/main/resources/sprites/bow/arrow.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move(double dx, double dy) {
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);
        x -= dx;
        y -= dy;
    }

    public boolean isOutOfBounds(int width, int height) {
        return x <= 0 || x >= width || y <= 0 || y >= height;
    }

    public void draw(Graphics2D g2d) {
        if (bulletImage != null) {
            AffineTransform originalTransform = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(angle+Math.PI);
            g2d.drawImage(bulletImage, -BULLET_SIZE / 2, -BULLET_SIZE / 2, BULLET_SIZE, BULLET_SIZE, null);
            g2d.setTransform(originalTransform);
        } else {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) x, (int) y, BULLET_SIZE, BULLET_SIZE);
        }
    }

    public boolean intersects(Enemy enemy, int mapX, int mapY) {

        int bulletWorldX = (int) x + mapX;
        int bulletWorldY = (int) y + mapY;
        // Calculate bounding rectangle for the bullet
        Rectangle bulletBounds = new Rectangle(bulletWorldX, bulletWorldY, BULLET_SIZE, BULLET_SIZE);

        // Calculate bounding rectangle for the new enemy
        Rectangle enemyBounds = new Rectangle(enemy.x, enemy.y, Enemy.SIZE, Enemy.SIZE);


        // Return true if the rectangle intersects
        return bulletBounds.intersects(enemyBounds);
    }

    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }
}
