package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ChunkCombiner {
    private static final int CHUNK_SIZE = 16; // Número de líneas por bloque
    private static final int START_X = -5; // Coordenada inicial X
    private static final int END_X = 4; // Coordenada final X
    private static final int START_Y = -5; // Coordenada inicial Y
    private static final int END_Y = 4; // Coordenada final Y

    public static void main(String[] args) {
        String basePath = "src/main/resources/chunks"; // Directorio base de los archivos chunk
        String outputPath = "src/main/resources/combined_chunks.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Combinar los chunks fila por fila (por coordenada Y)
            for (int y = START_Y; y <= END_Y; y++) {
                // Leer todos los chunks en la fila Y
                List<List<String>> rowChunks = new ArrayList<>();
                for (int x = START_X; x <= END_X; x++) {
                    String chunkFile = String.format("%s/chunk_%d_%d.txt", basePath, x, y);
                    rowChunks.add(readChunkLines(chunkFile));
                }

                // Combinar líneas de la fila Y
                for (int lineIndex = 0; lineIndex < CHUNK_SIZE; lineIndex++) {
                    StringBuilder combinedLine = new StringBuilder();

                    for (List<String> chunk : rowChunks) {
                        if (lineIndex < chunk.size()) {
                            combinedLine.append(chunk.get(lineIndex));
                        } else {
                            combinedLine.append(" ".repeat(64)); // Espaciado si el chunk tiene menos líneas
                        }
                    }

                    writer.write(combinedLine.toString().stripTrailing()); // Escribir línea combinada
                    writer.newLine();
                }
            }

            System.out.println("Chunks combined into " + outputPath);
        } catch (IOException e) {
            System.err.println("Error combining chunks: " + e.getMessage());
        }
    }

    private static List<String> readChunkLines(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading chunk file: " + filePath + " - " + e.getMessage());
            return new ArrayList<>(); // Retorna una lista vacía si ocurre un error
        }
    }
}


