package org.example;

import java.awt.*;

public class EnemyBullets {
    private double x, y;
    private final double angle;
    private static final int SPEED = 10;
    private static final int BULLET_SIZE = 10;

    public EnemyBullets(int startX, int startY, Point target) {
        this.x = startX;
        this.y = startY;
        this.angle =  Math.atan2(target.y - startY, target.x - startX);


    }
    public void move(double dx, double dy) {
        x += SPEED * Math.cos(angle);
        y += SPEED * Math.sin(angle);
        x -= dx;
        y -= dy;
    }
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.PINK);
        g2d.fillOval((int) x, (int) y, BULLET_SIZE, BULLET_SIZE);
    }
    public double getX() {
        return this.x;
    }
    public double getY() {
        return this.y;
    }

}
