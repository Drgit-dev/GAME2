package org.example;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AmmoBox {
    int x;
    int y;
    int originalX;
    int originalY;
    int Width=64;
    int Height=64;
    boolean isOpened = false;
    boolean ammoRewardGiven = false;
    long markedForDeletion = 0;

    private static BufferedImage spriteSheet;

    public AmmoBox(int x, int y) {
        this.x = x;
        this.y = y;
        this.originalX = x;
        this.originalY = y;

        LoadChestImage();
    }

    private void LoadChestImage() {
        if (spriteSheet == null) {
            try {
                spriteSheet = ImageIO.read(new File("src/main/resources/sprites/chest.png"));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not load chest sprite sheet!");
            }
        }
    }
    // Method to get a specific section of the sprite sheet
    public BufferedImage getSubImage(boolean isOpened) {

        int spriteIndex = isOpened ? 2 : 0; // Use 0 for closed, 2 for opened
        //int row = isOpened ? 1 : 0
        return spriteSheet.getSubimage(spriteIndex * Width, 0 * Height, Width, Height);
    }

    public void draw(Graphics g, int screenX, int screenY) {
        if (spriteSheet != null) {
            BufferedImage currentSprite = isOpened ? getSubImage(true) : getSubImage(false);
            g.drawImage(currentSprite, x, y, null);
        } else {
            if (isOpened) {
                g.setColor(Color.cyan);
                g.fillRect(x, y, Width, Height);
            } else {
                g.setColor(Color.magenta);
                g.fillRect(x, y, Width, Height);
            }
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
