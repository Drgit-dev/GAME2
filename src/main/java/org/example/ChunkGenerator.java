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
                    "TTTTTTTTTTTTTTTT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TWWWWWWWWWWWWWWT",
                    "TTTTTTTTTTTTTTTT"
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