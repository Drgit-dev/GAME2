package org.example;

import java.awt.*;

class Tile {
    private Rectangle bounds;

    public Tile(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean checkCollision(Rectangle playerBounds) {
        return bounds.intersects(playerBounds);
    }
}