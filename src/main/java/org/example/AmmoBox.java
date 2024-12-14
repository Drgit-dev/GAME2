package org.example;
import java.awt.*;

public class AmmoBox {
    int x;
    int y;
    int originalX;
    int originalY;
    int Width=50;
    int Height=50;
    boolean isOpened = false;
    boolean ammoRewardGiven = false;
    public AmmoBox(int x, int y) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;
    }
    public void draw(Graphics g, int screenX, int screenY) {

        if (isOpened) {
            g.setColor(Color.cyan);
            g.fillRect(x, y, Width, Height);
        }
        else {
            g.setColor(Color.magenta);
            g.fillRect(x, y, Width, Height);
        }
    }

    public void openBox() {
        isOpened = true;
    }


    public boolean openedByPlayer(Player player, int mapX, int mapY, int centerX, int centerY) {
        if (isOpened) return true;
        int ammoBoxWorldX = (int) x + mapX;
        int ammoBoxWorldY = (int) y + mapY;

        Rectangle ammoBoxBounds = new Rectangle(ammoBoxWorldX, ammoBoxWorldY, Width, Height);
        Rectangle playerBounds = new Rectangle(centerX+mapX, centerY+mapY, 64, 64);  // 64 is the player size
        System.out.println("Checking collision:"
                + "\nAmmoBox bounds: " + ammoBoxBounds
                + "\nPlayer bounds: " + playerBounds);
        isOpened = ammoBoxBounds.intersects(playerBounds);
        if (isOpened) {
            System.out.println("AmmoBox was opened!");
        }
        return isOpened;
    }
    public int getAbsoluteX(int mapX) {
        return x+mapX;
    }
    public int getAbsoluteY(int mapY) {
        return y+mapY;
    }

}
