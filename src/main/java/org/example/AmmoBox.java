package org.example;
import java.awt.*;

public class AmmoBox {
    int x;
    int y;
    int Width=50;
    int Height=50;
    boolean isOpened = false;
    public AmmoBox(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g, int screenX, int screenY) {

        if (isOpened) {
            g.setColor(Color.green);
            g.fillRect(x, y, Width, Height);
        }
        else {
            g.setColor(Color.pink);
            g.fillRect(x, y, Width, Height);
        }
    }
    public void drawOpened(Graphics g, int screenX, int screenY) {

    }
    public void openBox() {
        isOpened = true;
    }


    public boolean openedByPlayer(Player player, int mapX, int mapY, int centerX, int centerY) {
        if (isOpened) return false;
        int ammoBoxWorldX = (int) x + mapX;
        int ammoBoxWorldY = (int) y + mapY;

        Rectangle ammoBoxBounds = new Rectangle(ammoBoxWorldX, ammoBoxWorldY, Width, Height);
        Rectangle playerBounds = new Rectangle(centerX - 32, centerY - 32, 64, 64);  // 64 is the player size
        System.out.println("Checking collision:"
                + "\nAmmoBox bounds: " + ammoBoxBounds
                + "\nPlayer bounds: " + playerBounds);
        isOpened = ammoBoxBounds.intersects(playerBounds);
        if (isOpened) {
            System.out.println("AmmoBox was opened!");
        }
        return isOpened;
    }
    public int getX(int mapX) {
        return x+mapX;
    }
    public int getY(int mapY) {
        return y+mapY;
    }

}
