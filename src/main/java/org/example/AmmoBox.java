package org.example;
import java.awt.*;

public class AmmoBox {
    int x;
    int y;
    int Width=50;
    int Height=50;

    public AmmoBox(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g, int screenX, int screenY) {
        g.setColor(Color.yellow);
        g.fillRect(x, y, Width, Height);
    }
    public int getX(int mapX) {
        return x+mapX;
    }
    public int getY(int mapY) {
        return y+mapY;
    }

}
