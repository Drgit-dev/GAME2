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
                    "RRRRRRRRRRRRRRRR", // Línea 2
                    "RRRRRRRRRRRRRRRR", // Línea 3
                    "RRRRRRRRRRRRRRRR", // Línea 4
                    "RRRRRRRRRRRRRRRR", // Línea 5
                    "RRRRRRRRRRRRRRRR", // Línea 6
                    "RRRRRRRRRRRRRRRR", // Línea 7
                    "RRRRRRRRRRRRRRRR", // Línea 8
                    "RRRRRRRRRRRRRRRR", // Línea 9
                    "RRRRRRRRRRRRRRRR", // Línea 10
                    "RRRRRRRRRRRRRRRR", // Línea 11
                    "RRRRRRRRRRRRRRRR", // Línea 12
                    "RRRRRRRRRRRRRRRR", // Línea 13
                    "RRRRRRRRRRRRRRRR", // Línea 14
                    "RRRRRRRRRRRRRRRR", // Línea 15
                    "RRRRRRRRRRRRRRRR", // Línea 16
                    "                ", // Línea 17
                    "                ", // Línea 18
                    "                ", // Línea 19
                    "                ", // Línea 20
                    "                ", // Línea 21
                    "                ", // Línea 22
                    "                ", // Línea 23
                    "                ", // Línea 24
                    "                ", // Línea 25
                    "                ", // Línea 26
                    "                ", // Línea 27
                    "                ", // Línea 28
                    "                ", // Línea 29
                    "                ", // Línea 30
                    "                ", // Línea 31
                    "                "  // Línea 32
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