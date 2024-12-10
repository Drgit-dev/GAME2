package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    private Set<Point> chunksActivos = new HashSet<>();
    private Map<Point, Chunk> chunkMap = new HashMap<>();
    private List<Bullet> bullets;  // Lista para almacenar las balas

    private int mapX = 0, mapY = 0; // Map offset
    int adjustedX, adjustedY;
    private final int ChunkSize = 16;
    private final int SpriteSize = 64;
    private final int ChunkRadius = 1; // Distancia en chunks desde el jugador para activar
    private final int ChunkSizePixels = ChunkSize * SpriteSize;

    private boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;
    private int CurrentChunkX, CurrentChunkY; // Chunk actual del jugador
    private int lastChunkX = Integer.MIN_VALUE, lastChunkY = Integer.MIN_VALUE; // Para detectar cambios de chunk

    private int fps = 0; // FPS value
    private int frameCount = 0;
    private long lastTime = System.nanoTime();

    private double deltaTime = 0; // DeltaTime variable (en segundos)
    private long lastUpdateTime = System.nanoTime(); // Último tiempo de actualización para deltaTime

    // Creamos un pool de hilos (pool de hilos con 4 hilos en este caso)
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private Player player;

    public GamePanel() {
        setBackground(Color.WHITE);
        setFocusable(true);
        setDoubleBuffered(true);

        // Inicializamos la lista de balas
        bullets = new ArrayList<>();

        // Initialize player
        player = new Player(getWidth() / 2, getHeight() / 2);

        // Add event listeners
        addKeyListener(player.getKeyListener(this));
        addMouseMotionListener(player.getMouseMotionListener(this));

        // Añadimos un MouseListener para detectar clics
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Crear una nueva bala cuando el jugador haga clic

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long startTime = System.nanoTime();
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother visuals
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Dibujar los tiles
        drawTiles(g2d);

        // Draw active chunks
        drawChunks(g2d);

        // Dibujar las balas
        drawBullets(g2d);

        // Draw HUD (FPS, coordinates, etc.)
        drawHUD(g2d);

        // Draw player at the center of the screen
        player.calculateDirection(player.getAngle());
        player.draw(g2d, getWidth() / 2, getHeight() / 2);
        long endTime = System.nanoTime();
        long renderTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Render time: " + renderTime);
    }

    private void drawChunks(Graphics2D g2d) {
        // Draw each active chunk
        for (Point chunk : chunksActivos) {
            int chunkScreenX = chunk.x * ChunkSizePixels - mapX;
            int chunkScreenY = chunk.y * ChunkSizePixels - mapY;

            // Draw chunk border
            g2d.setColor(new Color(100, 100, 100, 100));
            g2d.drawRect(chunkScreenX, chunkScreenY, ChunkSizePixels, ChunkSizePixels);

            // Label each chunk with its coordinates
            g2d.setColor(Color.RED);
            g2d.drawString("Chunk (" + chunk.x + ", " + chunk.y + ")", chunkScreenX + 5, chunkScreenY + 20);
        }
    }

    private void drawBullets(Graphics2D g2d) {
        for (Bullet bullet : bullets) {
            //bullet.draw(g2d); // Dibuja cada bala
        }
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + fps, 10, 30);
        g2d.drawString("Chunk: X " + CurrentChunkX + "   Y " + CurrentChunkY, 10, 70);
        g2d.drawString("Coords: X " + adjustedX + "   Y " + adjustedY, 10, 110);
        g2d.drawString("Angle: " + player.getAngle(), 10, 150);
    }

    private void drawTiles(Graphics2D g2d) {
        for (Point chunkPos : chunksActivos) {
            Chunk chunk = chunkMap.get(chunkPos);
            if (chunk == null) continue;

            VolatileImage chunkImage = chunk.getChunkImage();
            if (chunkImage == null) continue;

            // Manejar pérdida de contenido de VolatileImage
            if (chunkImage.contentsLost()) {
                System.err.println("VolatileImage contents lost! Redrawing chunk.");
                continue;
            }

            int chunkScreenX = chunkPos.x * ChunkSizePixels - mapX;
            int chunkScreenY = chunkPos.y * ChunkSizePixels - mapY;

            g2d.drawImage(chunkImage, chunkScreenX, chunkScreenY, null);
        }
    }

    private void updateMovement() {
        int speed = 650;
        int moveX = 0, moveY = 0;

        // Detect which direction is pressed
        if (upPressed) moveY -= 1;
        if (downPressed) moveY += 1;
        if (leftPressed) moveX -= 1;
        if (rightPressed) moveX += 1;

        // Normalize diagonal movement
        double magnitude = Math.sqrt(moveX * moveX + moveY * moveY);
        if (magnitude > 0) {
            moveX = (int) (moveX / magnitude * speed * deltaTime);
            moveY = (int) (moveY / magnitude * speed * deltaTime);
        }

        // Now we scale the movement with deltaTime to ensure smooth and consistent movement
        mapX += moveX;
        mapY += moveY;

        // Update current chunk
        updateChunks();
        System.out.println("Updatemovement");

        //Separar esto que es nuevo
        // Check if player is moving (either moveX or moveY is not zero)
        boolean isMoving = moveX != 0 || moveY != 0;

        // Update walking status
        if (isMoving) {
            player.calculateWalkingFrame();
        } else {
            // If the player is not moving, continue updating the walking frame until spritecol is 0
            // This ensures the animation continues until it completes one full cycle
            while (player.getSpritecol() != 0) {
                player.calculateWalkingFrame();
            }
        }
    }

    private void updateBullets() {

    }

    private void updateChunks() {
        adjustedX = mapX + getWidth() / 2;
        adjustedY = mapY + getHeight() / 2;

        CurrentChunkX = (adjustedX < 0) ? (adjustedX / ChunkSizePixels) - 1 : (adjustedX / ChunkSizePixels);
        CurrentChunkY = (adjustedY < 0) ? (adjustedY / ChunkSizePixels) - 1 : (adjustedY / ChunkSizePixels);

        if (CurrentChunkX != lastChunkX || CurrentChunkY != lastChunkY) {
            lastChunkX = CurrentChunkX;
            lastChunkY = CurrentChunkY;
            updateActiveChunks();
        }
    }

    private void updateActiveChunks() {
        Set<Point> nuevosChunks = new HashSet<>();

        for (int x = CurrentChunkX - ChunkRadius; x <= CurrentChunkX + ChunkRadius; x++) {
            for (int y = CurrentChunkY - ChunkRadius; y <= CurrentChunkY + ChunkRadius; y++) {
                nuevosChunks.add(new Point(x, y));
            }
        }

        for (Point chunk : nuevosChunks) {
            if (!chunksActivos.contains(chunk)) {
                cargarChunk(chunk);
            }
        }

        for (Point chunk : chunksActivos) {
            if (!nuevosChunks.contains(chunk)) {
                descargarChunk(chunk);
            }
        }

        chunksActivos = nuevosChunks;
    }

    public void cargarChunk(Point chunkPos) {
        executorService.submit(() -> {
            Chunk chunk = new Chunk(chunkPos.x, chunkPos.y);
            String filePath = "chunks/chunk_" + chunkPos.x + "_" + chunkPos.y + ".txt";
            try {
                chunk.loadFromFile(filePath);
                synchronized (chunkMap) {
                    chunkMap.put(chunkPos, chunk);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar el chunk: " + filePath);
                e.printStackTrace();
            }
        });
    }

    private void descargarChunk(Point chunkPos) {
        System.out.println("Descargar chunk (" + chunkPos.x + ", " + chunkPos.y + ")");
        synchronized (chunkMap) {
            Chunk chunk = chunkMap.remove(chunkPos);
            if (chunk != null) {
                chunk.unload();
                System.out.println("Chunk (" + chunkPos.x + ", " + chunkPos.y + ") descargado.");
            } else {
                System.out.println("Chunk (" + chunkPos.x + ", " + chunkPos.y + ") no está cargado.");
            }
        }
    }

    private void updateFPS() {
        frameCount++;
        long currentTime = System.nanoTime();
        if (currentTime - lastTime >= 1_000_000_000) {
            fps = frameCount;
            frameCount = 0;
            lastTime = currentTime;
        }
    }

    private void updateDeltaTime() {
        long currentTime = System.nanoTime();
        deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
        lastUpdateTime = currentTime;
    }

    @Override
    public void run() {
        while (true) {
            updateDeltaTime();
            updateMovement();
            updateBullets();
            SwingUtilities.invokeLater(this::repaint);

            updateFPS();

            try {
                Thread.sleep(16); // Target ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUpPressed(boolean upPressed) { this.upPressed = upPressed; }
    public void setDownPressed(boolean downPressed) { this.downPressed = downPressed; }
    public void setLeftPressed(boolean leftPressed) { this.leftPressed = leftPressed; }
    public void setRightPressed(boolean rightPressed) { this.rightPressed = rightPressed; }
}




