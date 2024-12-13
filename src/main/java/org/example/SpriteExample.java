package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteExample extends JPanel {
    private BufferedImage spriteSheet;  // Aquí se carga el spritesheet completo
    private BufferedImage buffer;      // Aquí se almacena la imagen reorganizada
    private int spriteWidth = 16;      // Ancho de cada sprite
    private int spriteHeight = 16;     // Alto de cada sprite

    public SpriteExample(String spriteOrder) {
        try {
            // Carga el spritesheet desde el archivo
            spriteSheet = ImageIO.read(new File("src/main/resources/tiles.png"));

            // Divide el spritesheet en sprites individuales según el string de entrada
            int spriteCount = spriteSheet.getWidth() / spriteWidth; // Asume sprites en una fila
            String[] spriteIndices = spriteOrder.split(",");

            // Crear un nuevo BufferedImage para almacenar la reorganización
            buffer = new BufferedImage(
                    spriteIndices.length * spriteWidth,
                    spriteHeight,
                    BufferedImage.TYPE_INT_ARGB
            );

            // Dibujar los sprites en el orden definido dentro del buffer
            Graphics g = buffer.getGraphics();
            for (int i = 0; i < spriteIndices.length; i++) {
                int spriteIndex = Integer.parseInt(spriteIndices[i].trim()); // Convertir índice a entero
                if (spriteIndex >= 0 && spriteIndex < spriteCount) {
                    BufferedImage sprite = spriteSheet.getSubimage(
                            spriteIndex * spriteWidth, 0, spriteWidth, spriteHeight
                    );
                    g.drawImage(sprite, i * spriteWidth, 0, null);
                } else {
                    System.out.println("Índice fuera de rango: " + spriteIndex);
                }
            }
            g.dispose(); // Liberar los recursos gráficos
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el buffer reorganizado en la pantalla
        if (buffer != null) {
            g.drawImage(buffer, 50, 50, null); // Dibuja el buffer en la posición (50, 50)
        }
    }

    public static void main(String[] args) {
        // Configuración de la ventana
        JFrame frame = new JFrame("Sprite Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Definir el orden de los sprites en un string (ejemplo: "3,1,4,0")
        String spriteOrder = "2,1,3,0";

        SpriteExample panel = new SpriteExample(spriteOrder);
        frame.add(panel);

        frame.setVisible(true);
    }
}


