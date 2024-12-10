package org.example;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TileManager {
    // Definir el tamaño del tile
    public static final int TILE_SIZE = 64; // Puedes ajustar este valor según lo necesites

    // Mapa para mapear caracteres a coordenadas (x, y) en el spritesheet
    private static final Map<Character, Point> tileMap = new HashMap<>();

    static {
        // Mapear caracteres a coordenadas del spritesheet (x, y)
        tileMap.put('T', new Point(6, 4)); // Terreno
        tileMap.put('W', new Point(5, 4)); // Agua
        tileMap.put('V', new Point(0, 1)); // Vacío, etc.

        // Puedes agregar más tiles aquí...
    }

    // Método para obtener las coordenadas (x, y) de un tile multiplicadas por TILE_SIZE
    public static Rectangle getTileCoordinates(char tileChar) {
        // Obtener las coordenadas base (x, y) del mapa
        Point tileCoordinates = tileMap.get(tileChar);

        // Si el tile no es reconocido, retornar un Rectangle vacío
        if (tileCoordinates == null) {
            return null;
        }

        // Multiplicar por TILE_SIZE y devolver un Rectangle con las coordenadas escaladas
        return new Rectangle(tileCoordinates.x * TILE_SIZE, tileCoordinates.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }
}


