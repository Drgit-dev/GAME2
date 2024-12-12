package org.example;

import org.w3c.dom.css.Rect;

import java.awt.*;

public class Bullet {
    private double x, y;
    private final double angle;
    private static final int SPEED = 10;
    private static final int BULLET_SIZE = 10;

    public Bullet(int startX, int startY, Point target) {
        this.x = startX;
        this.y = startY;
        this.angle = Math.atan2(target.y - startY, target.x - startX);
    }

    public void move(double dx, double dy) {
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);
        x -= dx;
        y -= dy;
    }

    public boolean isOutOfBounds(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.fillOval((int) x, (int) y, BULLET_SIZE, BULLET_SIZE);
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
