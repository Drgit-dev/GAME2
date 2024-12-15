package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Chunk {
    private int chunkX, chunkY;
    private VolatileImage chunkImage;
    private static final int TILE_SIZE = 16;
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;
    private Map<Character, BufferedImage> tileCache = new HashMap<>();
    private List<Tile> collisionTiles = new ArrayList<>();

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

            // Draw decoration tiles (lines 16 to 31)
            for (int row = 0; row < TILE_SIZE && scanner.hasNextLine(); row++) {
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

            // Handle collision tiles (lines 31 and onwards)
// Handle collision tiles (lines 31 and onwards)
            for (int row = 0; scanner.hasNextLine(); row++) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length(); col++) {
                    char tileChar = line.charAt(col);
                    if (tileChar == '1') {
                        Tile collisionTile = new Tile(col * TILE_WIDTH, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
                        collisionTiles.add(collisionTile);
                    }
                }
            }

//            // Ahora imprimimos los tiles guardados en collisionTiles
//            System.out.println("Tiles de colisión almacenados:");
//            for (Tile tile : collisionTiles) {
//                // Obtenemos el objeto Rectangle dentro del tile
//                Rectangle bounds = tile.getBounds();
//
//                // Imprimimos las propiedades del tile utilizando el Rectangle
//                System.out.println("Tile en X: " + bounds.getX() + ", Y: " + bounds.getY() +
//                        ", Ancho: " + bounds.getWidth() + ", Alto: " + bounds.getHeight());
//            }

            g2d.dispose();
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

    public List<Tile> getCollisionTiles() {
        return collisionTiles;
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

    public boolean hasCollision(int tileX, int tileY) {
        // Recorre los tiles de colisión y compara las coordenadas
        for (Tile tile : collisionTiles) {
            Rectangle bounds = tile.getBounds();
            if (bounds.getX() == tileX * TILE_WIDTH && bounds.getY() == tileY * TILE_HEIGHT) {
                return true;
            }
        }
   return false;
    }

    private VolatileImage createVolatileImage(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
    }
}