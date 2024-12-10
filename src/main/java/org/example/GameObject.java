package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameObject {
    private BufferedImage sprite; // Imagen del objeto
    private Rectangle spriteCutCoordinates; // Coordenadas de recorte

    // Constructor
    public GameObject(String spritePath, Rectangle spriteCutCoordinates) {
        this.spriteCutCoordinates = spriteCutCoordinates;
        try {
            BufferedImage spritesheet = ImageIO.read(new File(spritePath)); // Cargar el spritesheet
            this.sprite = spritesheet.getSubimage(
                    spriteCutCoordinates.x,
                    spriteCutCoordinates.y,
                    spriteCutCoordinates.width,
                    spriteCutCoordinates.height
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener el sprite
    public BufferedImage getSprite() {
        return sprite;
    }

    // Método para obtener las coordenadas de recorte
    public Rectangle getSpriteCutCoordinates() {
        return spriteCutCoordinates;
    }
}


