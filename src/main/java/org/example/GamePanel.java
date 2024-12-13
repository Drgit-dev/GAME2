package org.example;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.VolatileImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GamePanel extends JPanel implements Runnable {
    private Set<Point> chunksActivos = new HashSet<>();
    private Map<Point, Chunk> chunkMap = new HashMap<>();
    private final List<Bullet> bullets;  // Lista para almacenar las balas
    int bullet_history = 0;
    private final List<Enemy> enemies;
    private final List<AmmoBox> ammoBoxes ;// Lista para almacenar las balas
    private final List<EnemyBullets> ebullets;
    private int mapX = 0, mapY = 0; // Map offset
    int adjustedX, adjustedY;
    private final int ChunkSize = 16;
    private final int SpriteSize = 64;
    private final int ChunkRadius = 1; // Distancia en chunks desde el jugador para activar
    private final int ChunkSizePixels = ChunkSize * SpriteSize;

    public boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;
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
    private Bow bow;
    private GUI ui;
    int type;

    int[] playerStats = new int[5];


    public GamePanel(int choice) {
        setBackground(Color.WHITE);
        setFocusable(true);
        setDoubleBuffered(true);

        // Inicializamos las listas de balas y enemigos Y cajas de municiones
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        ammoBoxes = new ArrayList<>();
        ebullets = new ArrayList<>();
        int type = choice;

        // Initialize player
        player = new Player(getWidth() / 2, getHeight() / 2,playerStats);
        bow = new Bow(getWidth() / 2, getHeight() / 2);
        playerStats= player.getStats(type);

        ui=new GUI();
        // Add event listeners
        addKeyListener(player.getKeyListener(this));
        addMouseMotionListener(player.getMouseMotionListener(this));
        addMouseMotionListener(bow.getMouseMotionListener(this));

        // Añadimos un MouseListener para detectar clics
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                  if(playerStats[4]>0&&playerStats[2]>0){// if ammo is 0 you cannot shoot (uncomment this for limited bullets
                    // Crear una nueva bala cuando el jugador haga clic
                    Point target = e.getPoint();
                    //System.out.println("Mouse pressed at: " + target);
                     ///System.out.println("Pressed mouse, the bullets should shoot");
                     bullets.add(new Bullet(getWidth() / 2, getHeight() / 2, target));
                     bullet_history += 1;
                     playerStats[2]--;

                     //System.out.println("Bullet created: " + bullets.size() + " bullets in the list.");
                      playerStats[4]--;//reduce the bullet count

                    }
                    if(playerStats[4]<=0) {// so the bullet count is not null
                        playerStats[4] = 0;
                    }
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
        // Dibujar los enemigos
        drawEnemies(g2d);
        drawAmmoBoxes(g2d);
        drawEnemybull(g2d);
        // Draw HUD (FPS, coordinates, etc.)
        drawHUD(g2d);

        // Draw player at the center of the screen
        player.calculateDirection(player.getAngle());
        player.draw(g2d, getWidth() / 2, getHeight() / 2);
        bow.draw(g2d,0,0);
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
                System.out.println("Drawing bullet at: (" + bullet.getX() + ", " + bullet.getY() + ")");
                bullet.draw(g2d); // Dibuja cada bala

            }

    }
    private void drawEnemybull(Graphics2D g2d){
        for(EnemyBullets enemyBullets: ebullets){
            enemyBullets.draw(g2d);
        }

    }
 void drawEnemies(Graphics2D g) {
        for (Enemy enemy : enemies) {
            // Convert absolute enemy position using map offsets
            int screenX = enemy.x - mapX;
            int screenY = enemy.y - mapY;

            // Draw the enemy at the calculated screen position
            enemy.draw(g, screenX, screenY);
        }
    }
    private void spawnAmmoBoxes(){
        Random rand = new Random();
        int x, y;
        x =rand.nextInt(500);
        y =rand.nextInt(500);
        ammoBoxes.add(new AmmoBox(x-mapX,y-mapY));
        System.out.println("AmmoBox spawned at: " + x + ", " + y); // Debug
    }
    private void drawAmmoBoxes(Graphics2D g) {

         for (AmmoBox box : ammoBoxes) {
            box.draw(g,box.x-mapX, box.y-mapY);
        }
    }

    private void checkCollisions() {

        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (bullet.intersects(enemy, mapX, mapY)) {
                    enemyIterator.remove(); // Safely remove enemy
                    bulletIterator.remove();
                    // Safely remove bullet
                    break;  // Exit loop since bullet can only hit one enemy


                }

            }


        }
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString("FPS: " + fps, 10, 30);
        g2d.drawString("Chunk: X " + CurrentChunkX + "   Y " + CurrentChunkY, 10, 70);
        g2d.drawString("Coords: X " + adjustedX + "   Y " + adjustedY, 10, 110);
        g2d.drawString("Angle: " + player.getAngle(), 10, 150);
        g2d.drawString("Bullets: " + bullet_history, 10, 300);
        ui.render(g2d,playerStats[0], playerStats[1], playerStats[2], playerStats[3], playerStats[4]);
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
        updateBullets(moveX,moveY);
        updateEBullets(moveX,moveY);
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

    private void updateBullets(double dx, double dy) {
        bullets.removeIf(bullet -> {
            bullet.move(dx,dy);
            return bullet.isOutOfBounds(getWidth(), getHeight());
        });
    }
    private void updateEBullets(double dx, double dy) {
        ebullets.removeIf(enemyBullets  -> {
            enemyBullets.move(dx,dy);
            return enemyBullets.isOutOfBounds(getWidth(), getHeight());
        });
    }

    private void updateEnemies() {
        int playerWorldX = mapX + getWidth() / 2; // Convert player's screen position to world position
        int playerWorldY = mapY + getHeight() / 2;
        for (Enemy enemy : enemies) {
            enemy.moveTowardsPlayer(playerWorldX, playerWorldY);
        }
    }


    private void updateAmmo(){
        for(AmmoBox box : ammoBoxes){
            int width=getWidth()/2;
            int height=getHeight()/2;
            box.x-=box.getX(mapX)+getWidth()/2;
            box.y-=box.getY(mapY)+getHeight()/2;
        }
    }
    private void spawnEbull(int x, int y){

        Point target=player.getpoint(getWidth()/2,getHeight()/2);
        ebullets.add(new EnemyBullets(x, y, target));
    }
    private void spawnEnemy() {
        Random rand = new Random();
        int x, y;

        int validWidth = Math.max(getWidth(), 1);
        int validHeight = Math.max(getHeight(), 1);
        int validMapX = Math.max(mapX, 0);
        int validMapY = Math.max(mapY, 0);

        // Determine random spawn position (random edge of the screen)
        if (rand.nextBoolean()) {
            x = rand.nextInt(validWidth + validMapX);
            y = rand.nextBoolean() ? validMapY - Enemy.SIZE : validMapY + validHeight;
        } else {
            x = rand.nextBoolean() ? validMapX - Enemy.SIZE : validMapX + validWidth;
            y = rand.nextInt(validHeight + validMapY);
        }
        enemies.add(new Enemy(x, y));

        System.out.println("Enemy spawned at: " + x + ", " + y); // Debug
    }
    private void enemyshoot(){
     for(Enemy enemy: enemies) {
        Timer enemyshoot = new Timer(3000, _ ->spawnEbull(enemy.x-mapX, enemy.y-mapY));
        enemyshoot.start();
        }
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
    public void regenMana(){
        if(playerStats[2]<playerStats[3]) {
            playerStats[2] += 5;
        }
    }

    @Override
    public void run() {

        // Start the enemy spawner timer
        Timer enemySpawner = new Timer(2000, e -> spawnEnemy());
        enemySpawner.start();
        Timer BoxSpawner = new Timer(5000, _ ->spawnAmmoBoxes());
        BoxSpawner.start();
        Timer enemyshooter = new Timer(1000, _ ->enemyshoot());
        enemyshooter.start();
        Timer regenMana = new Timer(2000, _ ->regenMana());
        regenMana.start();



        while (true) {
            updateDeltaTime();
            updateMovement();
            updateEnemies();
            checkCollisions();
            updateAmmo();
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




