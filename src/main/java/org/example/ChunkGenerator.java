package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ChunkGenerator {

    public static void main(String[] args) {
        // Número de chunks a crear
        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                String fileName = "chunk_" + i + "_" + j + ".txt";
                generateChunkFile(fileName);
            }
        }
    }

    // Método para generar un archivo de chunk con el patrón TWWWWWW...
    public static void generateChunkFile(String fileName) {
        File file = new File("src/main/resources/chunks/" + fileName); // Ruta donde se creará el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Crear la matriz de 16x16
            String[] chunkPattern = {
                    "RRRRRRRRRRRRRRRR", // Línea 1
                    "5555555555555555", // Línea 2
                    "6666666666666666", // Línea 3
                    "RRRRRRRRRRRRRRRR", // Línea 4
                    "5555555555555555", // Línea 5
                    "6666666666666666", // Línea 6
                    "RRRRRRRRRRRRRRRR", // Línea 7
                    "5555555555555555", // Línea 8
                    "6666666666666666", // Línea 9
                    "RRRRRRRRRRRRRRRR", // Línea 10
                    "5555555555555555", // Línea 11
                    "6666666666666666", // Línea 12
                    "RRRRRRRRRRRRRRRR", // Línea 13
                    "5555555555555555", // Línea 14
                    "6666666666666666", // Línea 15
                    "RRRRRRRRRRRRRRRR", // Línea 16
                    "ABCDEFGHJKLMNOP", // Línea 17
                    "QRSTUVWXYZabcde", // Línea 18
                    "fghijklmnopqrsuv", // Línea 19
                    "wxyz0123456789!\"", // Línea 20
                    "#$%&'()*+,-./:;<", // Línea 21
                    "?@[]^_`{|}~¡¢£€¤", // Línea 22
                    "¥¦§¨©ª«®¯º±²³´µ¶", // Línea 23
                    "ABCDEFGHJKLMNOP", // Línea 24
                    "QRSTUVWXYZabcde", // Línea 25
                    "fghijklmnopqrsuv", // Línea 26
                    "wxyz0123456789!\"", // Línea 27
                    "#$%&'()*+,-./:;<", // Línea 28
                    "?@[]^_`{|}~¡¢£€¤", // Línea 29
                    "¥¦§¨©ª«®¯º±²³´µ¶", // Línea 30
                    "ABCDEFGHJtLMNOP", // Línea 31
                    "QRSTUVWXYZabcde"  // Línea 32
};

            // Escribir cada línea en el archivo
            for (String line : chunkPattern) {
                writer.write(line);
                writer.newLine(); // Salto de línea
            }

            System.out.println("Archivo creado: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al crear el archivo " + fileName);
        }
    }
}