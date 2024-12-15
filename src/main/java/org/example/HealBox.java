package org.example;

import java.awt.image.BufferedImage;

public class HealBox extends AmmoBox {

    public HealBox(int x, int y) {
        super(x, y);
    }

    @Override     // Method to get a specific section of the sprite sheet
    public BufferedImage getSubImage(boolean isOpened) {

        int spriteIndex = isOpened ? 3 : 1; // Use 1 for closed, 3 for opened
        //int row = isOpened ? 1 : 0
        return spriteSheet.getSubimage(spriteIndex * Width, 0 * Height, Width, Height);
    }
}
