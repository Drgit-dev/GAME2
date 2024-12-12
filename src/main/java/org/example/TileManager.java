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
        // Map characters to sprite coordinates (x, y)
        tileMap.put('A', new Point(0, 0)); // Gray Rock
        tileMap.put('B', new Point(1, 0)); // Wooden Wall
        tileMap.put('C', new Point(2, 0)); // Green Tiles
        tileMap.put('D', new Point(3, 0)); // Chimney 1
        tileMap.put('E', new Point(4, 0)); // White Wall
        tileMap.put('F', new Point(5, 0)); // Yellow House
        tileMap.put('G', new Point(6, 0)); // Small Window
        tileMap.put('H', new Point(7, 0)); // Slanted Banners
        tileMap.put('I', new Point(8, 0)); // Well
        tileMap.put('J', new Point(9, 0)); // Diamond Tiles
        tileMap.put('K', new Point(10, 0)); // Rail Stop
        tileMap.put('L', new Point(11, 0)); // Berry Bush
        tileMap.put('M', new Point(12, 0)); // Chest

        tileMap.put('N', new Point(0, 1)); // Small Mushroom
        tileMap.put('O', new Point(1, 1)); // Straight Banners
        tileMap.put('P', new Point(2, 1)); // Green Tiles with Point
        tileMap.put('Q', new Point(3, 1)); // Chimney 2
        tileMap.put('R', new Point(4, 1)); // Grass
        tileMap.put('S', new Point(5, 1)); // Wooden Border
        tileMap.put('T', new Point(6, 1)); // Large Window
        tileMap.put('U', new Point(7, 1)); // ???
        tileMap.put('V', new Point(8, 1)); // Crossed Swords
        tileMap.put('W', new Point(9, 1)); // Square Tiles
        tileMap.put('X', new Point(10, 1)); // Plant Wall
        tileMap.put('Y', new Point(11, 1)); // Statue Torso
        tileMap.put('Z', new Point(12, 1)); // Stacked Logs

        tileMap.put('a', new Point(0, 2)); // Large Plant
        tileMap.put('b', new Point(1, 2)); // Wooden Fence
        tileMap.put('c', new Point(2, 2)); // Cart 1
        tileMap.put('d', new Point(3, 2));
        tileMap.put('e', new Point(4, 2));
        tileMap.put('f', new Point(5, 2));
        tileMap.put('g', new Point(6, 2));
        tileMap.put('h', new Point(7, 2));
        tileMap.put('i', new Point(8, 2));
        tileMap.put('j', new Point(9, 2));
        tileMap.put('k', new Point(10, 2));
        tileMap.put('l', new Point(11, 2));
        tileMap.put('m', new Point(12, 2));

        tileMap.put('n', new Point(0, 3));
        tileMap.put('o', new Point(1, 3));
        tileMap.put('p', new Point(2, 3));
        tileMap.put('q', new Point(3, 3));
        tileMap.put('r', new Point(4, 3));
        tileMap.put('s', new Point(5, 3));
        tileMap.put('t', new Point(6, 3));
        tileMap.put('u', new Point(7, 3));
        tileMap.put('v', new Point(8, 3));
        tileMap.put('w', new Point(9, 3));
        tileMap.put('x', new Point(10, 3));
        tileMap.put('y', new Point(11, 3));
        tileMap.put('z', new Point(12, 3));

        tileMap.put('0', new Point(0, 4));
        tileMap.put('1', new Point(1, 4));
        tileMap.put('2', new Point(2, 4));
        tileMap.put('3', new Point(3, 4));
        tileMap.put('4', new Point(4, 4));
        tileMap.put('5', new Point(5, 4));
        tileMap.put('6', new Point(6, 4));
        tileMap.put('7', new Point(7, 4));
        tileMap.put('8', new Point(8, 4));
        tileMap.put('9', new Point(9, 4));
        tileMap.put('!', new Point(10, 4));
        tileMap.put('"', new Point(11, 4));
        tileMap.put('#', new Point(12, 4));

        tileMap.put('$', new Point(0, 5));
        tileMap.put('%', new Point(1, 5));
        tileMap.put('&', new Point(2, 5));
        tileMap.put('\'', new Point(3, 5)); // Single Quote
        tileMap.put('(', new Point(4, 5));
        tileMap.put(')', new Point(5, 5));
        tileMap.put('*', new Point(6, 5));
        tileMap.put('+', new Point(7, 5));
        tileMap.put(',', new Point(8, 5));
        tileMap.put('-', new Point(9, 5));
        tileMap.put('.', new Point(10, 5));
        tileMap.put('/', new Point(11, 5));
        tileMap.put(':', new Point(12, 5));

        tileMap.put(';', new Point(0, 6));
        tileMap.put('<', new Point(1, 6));
        tileMap.put('=', new Point(2, 6));
        tileMap.put('>', new Point(3, 6));
        tileMap.put('?', new Point(4, 6));
        tileMap.put('@', new Point(5, 6));
        tileMap.put('[', new Point(6, 6));
        tileMap.put('\\', new Point(7, 6)); // Backslash
        tileMap.put(']', new Point(8, 6));
        tileMap.put('^', new Point(9, 6));
        tileMap.put('_', new Point(10, 6));
        tileMap.put('`', new Point(11, 6));
        tileMap.put('{', new Point(12, 6));

        tileMap.put('|', new Point(0, 7));
        tileMap.put('}', new Point(1, 7));
        tileMap.put('~', new Point(2, 7));
        tileMap.put('¡', new Point(3, 7));
        tileMap.put('¢', new Point(4, 7));
        tileMap.put('£', new Point(5, 7));
        tileMap.put('€', new Point(6, 7));
        tileMap.put('¤', new Point(7, 7));

// Added the new symbols
        tileMap.put('¥', new Point(8, 7)); // Yen
        tileMap.put('¦', new Point(9, 7)); // Broken Bar
        tileMap.put('§', new Point(10, 7)); // Section
        tileMap.put('¨', new Point(11, 7)); // Diaeresis
        tileMap.put('©', new Point(12, 7)); // Copyright
        tileMap.put('ª', new Point(0, 8)); // Feminine Ordinal Indicator
        tileMap.put('«', new Point(1, 8)); // Left-Pointing Double Angle Quotation Mark
        tileMap.put('8', new Point(2, 8)); // The Number 8
        tileMap.put('®', new Point(3, 8)); // Registered Trademark
        tileMap.put('¯', new Point(4, 8)); // Macron
        tileMap.put('º', new Point(5, 8)); // Masculine Ordinal Indicator
        tileMap.put('±', new Point(6, 8)); // Plus-Minus Sign
        tileMap.put('²', new Point(7, 8)); // Superscript 2
        tileMap.put('³', new Point(8, 8)); // Superscript 3
        tileMap.put('´', new Point(9, 8)); // Acute Accent
        tileMap.put('µ', new Point(10, 8)); // Micro Sign
        tileMap.put('¶', new Point(11,8));//Pilcrow
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


