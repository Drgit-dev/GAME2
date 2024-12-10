package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Chunk {
    private int chunkX, chunkY;
    private VolatileImage chunkImage; // Cambiado a VolatileImage
    private static final int TILE_SIZE = 16;
    private static final int TILE_WIDTH = 64;
    private static final int TILE_HEIGHT = 64;

    public Chunk(int chunkX, int chunkY) {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public void loadFromFile(String filePath) {
        System.err.println("LOADING IMAGE FROM FILE");
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found in classpath: " + filePath);
            }
            Scanner scanner = new Scanner(inputStream);

            // Cargar el spritesheet de tiles
            BufferedImage spritesheet = ImageIO.read(new File("src/main/resources/sprites/tiles.png"));

            // Crear una VolatileImage para el chunk
            chunkImage = createVolatileImage(TILE_WIDTH * TILE_SIZE, TILE_HEIGHT * TILE_SIZE);

            // Dibujar tiles en la VolatileImage
            Graphics2D g2d = chunkImage.createGraphics();

            for (int row = 0; scanner.hasNextLine() && row < TILE_SIZE; row++) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length() && col < TILE_SIZE; col++) {
                    char tileChar = line.charAt(col);

                    // Usar TileManager para obtener las coordenadas del tile
                    Rectangle tileCoordinates = TileManager.getTileCoordinates(tileChar);

                    if (tileCoordinates != null) {
                        // Dibujar la imagen del tile desde el spritesheet
                        BufferedImage tile = spritesheet.getSubimage(tileCoordinates.x, tileCoordinates.y, TILE_WIDTH, TILE_HEIGHT);
                        g2d.drawImage(tile, col * TILE_WIDTH, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT, null);
                    } else {
                        System.err.println("Unrecognized tile character: " + tileChar);
                    }
                }
            }
            g2d.dispose();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            e.printStackTrace();
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
            System.out.println("Chunk (" + chunkX + ", " + chunkY + ") was already unloaded.");
        }
    }

    private VolatileImage createVolatileImage(int width, int height) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleVolatileImage(width, height);
    }
}







