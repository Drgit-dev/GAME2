package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Chunk {
    private int chunkX, chunkY;
    private VolatileImage chunkImage; // Cambiado a VolatileImage
    private static final int TILE_SIZE = 16;
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;
    private Map<Character, BufferedImage> tileCache = new HashMap<>();

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public void loadFromFile(String filePath) {
        try (Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(filePath))) {
            if (scanner == null) {
                throw new FileNotFoundException("File not found in classpath: " + filePath);
            }

            // Load the tile spritesheet
            BufferedImage spritesheet = ImageIO.read(new File("src/main/resources/sprites/tiles.png"));

            // Create a VolatileImage for the chunk
            chunkImage = createVolatileImage(TILE_WIDTH * TILE_SIZE, TILE_HEIGHT * TILE_SIZE);

            // Draw background tiles (lines 0 to 15)
            Graphics2D g2d = chunkImage.createGraphics();
            for (int row = 0; row < TILE_SIZE; row++) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length() && col < TILE_SIZE; col++) {
                    char tileChar = line.charAt(col);
                    Rectangle tileCoordinates = TileManager.getTileCoordinates(tileChar);

                    if (tileCoordinates != null) {
                        BufferedImage tile = spritesheet.getSubimage(tileCoordinates.x, tileCoordinates.y, TILE_WIDTH, TILE_HEIGHT);
                        g2d.drawImage(tile, col * TILE_WIDTH, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, null);
                    } else {
                        System.err.println("Unrecognized tile character: " + tileChar);
                    }
                }
            }

            // Clear the Graphics2D context for decoration tiles
            //g2d.dispose();

            // Draw decoration tiles (lines 16 to 31)
            g2d = chunkImage.createGraphics();
            for (int row = 0; row < TILE_SIZE && scanner.hasNextLine(); row++) { // Start from 0
                String line = scanner.nextLine();
                for (int col = 0; col < line.length() && col < TILE_SIZE; col++) {
                    char tileChar = line.charAt(col);
                    Rectangle tileCoordinates = TileManager.getTileCoordinates(tileChar);

                    if (tileCoordinates != null) {
                        //System.out.println(tileCoordinates.x + ", " + tileCoordinates.y+" char: "+tileChar+" Position: "+row+" "+col);
                        BufferedImage tile = spritesheet.getSubimage(tileCoordinates.x, tileCoordinates.y, TILE_WIDTH, TILE_HEIGHT);
                        g2d.drawImage(tile, col * TILE_WIDTH, row  * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, null); // Adjust y-coordinate
                    } else {
                        System.err.println("Unrecognized tile character: " + tileChar);
                    }
                }
            }

            for (int row = 0; row < TILE_SIZE && scanner.hasNextLine(); row++) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length(); col++) {
                    char tileChar = line.charAt(col);
                    //System.out.println("Line " + (31 + row) + ", Column " + col + ": Character '" + tileChar + "'");
                }
            }
            //g2d.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VolatileImage getChunkImage() {
        if (chunkImage == null) {
            System.err.println("Chunk image is not loaded!");
        }
        return chunkImage;
    }

    public void unload() {
        if (chunkImage != null) {
            chunkImage.flush();
            chunkImage = null;
            System.out.println("Chunk (" + chunkX + ", " + chunkY + ") has been unloaded.");
        } else {
            System.err.println("Chunk (" + chunkX + ", " + chunkY + ") was already unloaded.");
        }
    }

    private VolatileImage createVolatileImage(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
}
}





