package org.example;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Enemy {
    int x, y;
    Random random = new Random();
    private final int SPEED = random.nextInt(10)+2;
    static final int SIZE = 40;

    //private Timer shootingTimer;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveTowardsPlayer(int centerX, int centerY) {
        double angle = Math.atan2(centerY - y, centerX - x);
        x += (int) (SPEED * Math.cos(angle));
        y += (int) (SPEED * Math.sin(angle));
    }

    public void draw(Graphics g, int screenX, int screenY) {
        g.setColor(Color.RED);
        g.fillOval(screenX, screenY, SIZE, SIZE);
    }
}
