package org.example;

import java.awt.*;

public class Tile extends GameObject {
    private int row, col; // Fila y columna del Tile en el mapa
    private static final String PATH_TO_TILE_SPRITESHEET = "src/main/resources/sprites/tiles.png"; // Path al spritesheet

    public Tile(Rectangle spriteCutCoordinates, int row, int col) {
        super(PATH_TO_TILE_SPRITESHEET, spriteCutCoordinates); // Llama al constructor de GameObject con la ruta del spritesheet y las coordenadas
        this.row = row;
        this.col = col;
    }

    public void draw(Graphics2D g2d, int screenX, int screenY) {
        // Dibujar el tile usando el método draw de la clase GameObject
        g2d.drawImage(
                getSprite(), // Obtiene la imagen del sprite
                screenX, // Coordenada X en pantalla
                screenY, // Coordenada Y en pantalla
                null // Sin observer
        );
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "row=" + row +
                ", col=" + col +
                ", spriteCutCoordinates=" + getSpriteCutCoordinates() + // Muestra las coordenadas de recorte
                '}';
    }
}

