package org.example;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.VolatileImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors


public class GamePanel extends JPanel implements Runnable {
    private Set<Point> chunksActivos = new HashSet<>();
    private final Map<Point, Chunk> chunkMap = new HashMap<>();
    private final List<Bullet> bullets;  // Lista para almacenar las balas
    int bullet_history = 0;
    private final List<Enemy> enemies;
    //private final CopyOnWriteArrayList<Enemy> enemies;
    private final List<AmmoBox> ammoBoxes ;// Lista para almacenar las balas
    private final List <HealBox> healBoxes;
    private final List<EnemyBullets> ebullets;
    //private final CopyOnWriteArrayList<EnemyBullets> ebullets;
    private int mapX = 0, mapY = 0; // Map offset
    int adjustedX, adjustedY;
    private final int ChunkSize = 16;
    private final int SpriteSize = 64;
    private final int ChunkSizePixels = ChunkSize * SpriteSize;

    public boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false;
    private int CurrentChunkX, CurrentChunkY; // Chunk actual del jugador
    private int lastChunkX = Integer.MIN_VALUE, lastChunkY = Integer.MIN_VALUE; // Para detectar cambios de chunk

    private int fps = 0; // FPS value
    private int frameCount = 0;
    private long lastTime = System.nanoTime();

    private double deltaTime = 0; // DeltaTime variable (en segundos)
    private long lastUpdateTime = System.nanoTime(); // Último tiempo de actualización para deltaTime
    private final Map<Enemy, Timer> enemyTimers;


    // Creamos un pool de hilos (pool de hilos con 4 hilos en este caso)
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    private final Player player;
    private final Bow bow;
    private final GUI ui;

    int[] playerStats = new int[5];

    private boolean f3On = false; // Variable para el estado de F3
    int enemycount=50;
    int killcount=0;
    public GamePanel(int choice) {
        setBackground(Color.WHITE);
        setFocusable(true);
        setDoubleBuffered(true);

        // Inicializamos las listas de balas y enemigos Y cajas de municiones
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        //enemies = new CopyOnWriteArrayList<>();
        ammoBoxes = new ArrayList<>();
        healBoxes = new ArrayList<>();
        //ebullets = new ArrayList<>();
        ebullets = new CopyOnWriteArrayList<>();
        enemyTimers = new HashMap<>();  // To fix enemies shooting after death

        // Initialize player
        player = new Player(getWidth() / 2, getHeight() / 2, playerStats);
        bow = new Bow(getWidth() / 2, getHeight() / 2);
        playerStats= player.getStats(choice);

        ui = new GUI();

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

        // KeyListener para alternar f3On con F3
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F3) {
                    f3On = !f3On; // Cambia entre true y false
                    System.out.println("F3 toggled: " + f3On);
                }
            }
        });
    }
    private void winGame() throws IOException {
        if(enemycount==0&&playerStats[0]>0){
           System.out.println("\n" +
                   "░▒▓█▓▒░░▒▓█▓▒░  ░▒▓██████▓▒░  ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓███████▓▒░  \n" +
                   "░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   "░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   " ░▒▓██████▓▒░  ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   "   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   "   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   "   ░▒▓█▓▒░      ░▒▓██████▓▒░   ░▒▓██████▓▒░         ░▒▓█████████████▓▒░  ░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ \n" +
                   "                                                                                                \n" +
                   "                                                                                                \n");
            FileWriter myWriter = new FileWriter("log.txt");
            myWriter.write("Game won kills: "+killcount+"\n");
            myWriter.close();
            System.exit(0);
        }
    }
    private void loseGame() throws IOException {
        if(playerStats[0]<=0){
            System.out.println("\n" +
                    "░▒▓█▓▒░░▒▓█▓▒░  ░▒▓██████▓▒░  ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░         ░▒▓██████▓▒░   ░▒▓███████▓▒░ ░▒▓████████▓▒░ \n" +
                    "░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░        ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░        ░▒▓█▓▒░        \n" +
                    "░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░        ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░        ░▒▓█▓▒░        \n" +
                    " ░▒▓██████▓▒░  ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░        ░▒▓█▓▒░░▒▓█▓▒░  ░▒▓██████▓▒░  ░▒▓██████▓▒░   \n" +
                    "   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░        ░▒▓█▓▒░░▒▓█▓▒░        ░▒▓█▓▒░ ░▒▓█▓▒░        \n" +
                    "   ░▒▓█▓▒░     ░▒▓█▓▒░░▒▓█▓▒░ ░▒▓█▓▒░░▒▓█▓▒░       ░▒▓█▓▒░        ░▒▓█▓▒░░▒▓█▓▒░        ░▒▓█▓▒░ ░▒▓█▓▒░        \n" +
                    "   ░▒▓█▓▒░      ░▒▓██████▓▒░   ░▒▓██████▓▒░        ░▒▓████████▓▒░  ░▒▓██████▓▒░  ░▒▓███████▓▒░  ░▒▓████████▓▒░ \n" +
                    "                                                                                                               \n" +
                    "                                                                                                               \n");
            FileWriter myWriter = new FileWriter("log.txt");
            myWriter.write("Game Lost kills: "+killcount+"\n");
            myWriter.close();
            System.exit(0);
        }
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
        if (f3On) {
            // Draw active chunks
            drawChunks(g2d);
            // Draw HUD (FPS, coordinates, etc.)
            drawHUD(g2d);
        }
        try {
            winGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            loseGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Dibujar las balas
        drawBullets(g2d);
        // Dibujar los enemigos
        drawEnemies(g2d);
        drawBoxes(g2d);

        drawEnemybull(g2d);

        ui.render(g2d,playerStats[0], playerStats[1], playerStats[2], playerStats[3], playerStats[4],enemycount);

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

            // Draw chunk border with thicker stroke
            g2d.setColor(new Color(255, 0, 0, 255));
            Stroke originalStroke = g2d.getStroke(); // Save the original stroke
            g2d.setStroke(new BasicStroke(3)); // Set a thicker stroke
            g2d.drawRect(chunkScreenX, chunkScreenY, ChunkSizePixels, ChunkSizePixels);
            g2d.setStroke(originalStroke); // Restore the original stroke

            // Label each chunk with larger font
            g2d.setColor(Color.RED);
            Font originalFont = g2d.getFont(); // Save the original font
            g2d.setFont(new Font("Arial", Font.BOLD, 20)); // Set a larger font
            g2d.drawString("Chunk (" + chunk.x + ", " + chunk.y + ")", chunkScreenX + 5, chunkScreenY + 20);
            g2d.setFont(originalFont); // Restore the original font
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
    private void spawnBoxes(){
        Random rand = new Random();
        int x, y;
        x =rand.nextInt(10001) - 5000;  // This generates a random number
        y =rand.nextInt(10001) - 5000;  // between -5000 and 5000
        if (rand.nextBoolean()) {
            ammoBoxes.add(new AmmoBox(x, y));
        }   else {
            healBoxes.add(new HealBox(x, y));
        }
    }
    private void drawBoxes(Graphics2D g) {

         for (AmmoBox box : ammoBoxes) {
             //System.out.println("Drawing AmmoBox at (" + box.getAbsoluteX(mapX) + ", " + box.getAbsoluteY(mapY) + ") State: " + (box.isOpened ? "Opened" : "Closed"));
            box.draw(g, box.getAbsoluteX(mapX), box.getAbsoluteY(mapY));
        }
        for (HealBox box : healBoxes) {
            //System.out.println("Drawing HealBox at (" + box.getAbsoluteX(mapX) + ", " + box.getAbsoluteY(mapY) + ") State: " + (box.isOpened ? "Opened" : "Closed"));
            box.draw(g, box.getAbsoluteX(mapX), box.getAbsoluteY(mapY));
        }
    }

    private void openBoxesAndDeleteIfOpen() {
        long currentTime = System.currentTimeMillis();

        // Handle AmmoBoxes
        Iterator<AmmoBox> ammoBoxIterator = ammoBoxes.iterator();

        while(ammoBoxIterator.hasNext()) {
            AmmoBox box = ammoBoxIterator.next();
            if (box.openedByPlayer(player, mapX, mapY, getWidth() / 2, getHeight() / 2)) {
                if (box.markedForDeletion == 0) {
                    if (!box.ammoRewardGiven) {
                        playerStats[4] += 5;
                        box.ammoRewardGiven = true;
                    }
                    box.markedForDeletion = currentTime;
                } else {
                    if (currentTime - box.markedForDeletion > 20000) {
                        ammoBoxIterator.remove();
                    }
                }
            }
        }

        // Handle HealBoxes
        Iterator<HealBox> healBoxIterator = healBoxes.iterator();
        while(healBoxIterator.hasNext()) {
            HealBox box = healBoxIterator.next();
            if (box.openedByPlayer(player, mapX, mapY, getWidth() / 2, getHeight() / 2)) {
                if (box.markedForDeletion == 0) {
                    if (!box.ammoRewardGiven) {
                        playerStats[0] = Math.min(playerStats[0] + 10, playerStats[1]);
                        box.ammoRewardGiven = true;
                    }
                    box.markedForDeletion = currentTime;
                } else {
                    if (currentTime - box.markedForDeletion > 20000) {
                        healBoxIterator.remove();
                    }
                }

            }
        }
    }

    private void checkenemyhit(){
        Iterator<EnemyBullets> EbulletIterator = ebullets.iterator();
        while(EbulletIterator.hasNext()) {
            EnemyBullets ebullet = EbulletIterator.next();
            if(ebullet.intersects(player,mapX,mapY,getWidth()/2,getHeight()/2)){
                playerStats[0]-=4;

            }
        }
    }


    private void checkCollisions() {

        openBoxesAndDeleteIfOpen();
        Iterator<Bullet> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Iterator<Enemy> enemyIterator = enemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy enemy = enemyIterator.next();
                if (bullet.intersects(enemy, mapX, mapY)) {
                    enemyIterator.remove(); // Safely remove enemy
                    bulletIterator.remove();
                    enemycount--;
                    killcount++;
                    // Stop the associated timer:
                    if (enemyTimers.containsKey(enemy)) {
                        enemyTimers.get(enemy).stop();
                        enemyTimers.remove(enemy);
                    }
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

        // Llamamos al método calculatePlayerTiles() y guardamos las direcciones de colisión
        Set<String> collisionDirections = calculatePlayerTiles();  // Llamamos desde GamePanel y almacenamos las direcciones

        // Impedir movimiento basado en las direcciones de colisión
        if (collisionDirections.contains("up") && moveY < 0) moveY = 0;   // Si hay colisión arriba, no dejamos mover hacia arriba
        if (collisionDirections.contains("down") && moveY > 0) moveY = 0; // Si hay colisión abajo, no dejamos mover hacia abajo
        if (collisionDirections.contains("left") && moveX < 0) moveX = 0; // Si hay colisión izquierda, no dejamos mover hacia la izquierda
        if (collisionDirections.contains("right") && moveX > 0) moveX = 0; // Si hay colisión derecha, no dejamos mover hacia la derecha

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
        updateBullets(moveX, moveY);
        updateEBullets(moveX, moveY);

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

    private Set<String> calculatePlayerTiles() {
        int playerX = adjustedX;
        int playerY = adjustedY;

        // Dimensiones del sprite del jugador
        int playerWidth = SpriteSize; // Ancho del sprite del jugador
        int playerHeight = SpriteSize; // Alto del sprite del jugador

        // Coordenadas específicas para determinar las direcciones de colisión
        Point[] collisionPoints = new Point[] {
                new Point(playerX, playerY - playerHeight / 2), // Punto superior (UP)
                new Point(playerX, playerY + playerHeight / 2), // Punto inferior (DOWN)
                new Point(playerX - playerWidth / 2, playerY),  // Punto izquierdo (LEFT)
                new Point(playerX + playerWidth / 2, playerY)   // Punto derecho (RIGHT)
        };

        // Direcciones correspondientes a los puntos
        String[] directions = { "up", "down", "left", "right" };

        Set<String> collisionDirections = new HashSet<>();

        // Recorremos todos los puntos de colisión para verificar si hay alguna
        for (int i = 0; i < collisionPoints.length; i++) {
            Point point = collisionPoints[i];

            // Calcular el chunk correspondiente
            int chunkX = (point.x < 0) ? (point.x / ChunkSizePixels) - 1 : (point.x / ChunkSizePixels);
            int chunkY = (point.y < 0) ? (point.y / ChunkSizePixels) - 1 : (point.y / ChunkSizePixels);

            // Calcular el tile dentro del chunk
            int tileX = Math.floorMod(point.x, ChunkSizePixels) / SpriteSize;
            int tileY = Math.floorMod(point.y, ChunkSizePixels) / SpriteSize;
            Point chunkPos = new Point(chunkX, chunkY);
            if (chunksActivos.contains(chunkPos)) {
                Chunk chunk = chunkMap.get(chunkPos);
                if (chunk != null && chunk.hasCollision(tileX, tileY)) {
                    collisionDirections.add(directions[i]);
                }
            }
        }

        return collisionDirections;
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

        // Use synchronized block to avoid concurrent modification issues
        synchronized (enemies) {
            for (Iterator<Enemy> iterator = enemies.iterator(); iterator.hasNext(); ) {
                Enemy enemy = iterator.next();

                if (enemy != null) { // Null-check enemy instance
                    try {
                        enemy.moveTowardsPlayer(playerWorldX, playerWorldY);
                    } catch (Exception e) {
                        System.err.println("Error updating enemy: " + enemy);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Encountered null enemy. Removing from list.");
                    iterator.remove(); // Remove null enemies to maintain a clean list
                }
            }
        }
    }


    private void updateBoxes(){
        for(AmmoBox box : ammoBoxes){
            box.x = box.originalX - mapX;
            box.y = box.originalY - mapY;
        }
        for(HealBox box : healBoxes){
            box.x = box.originalX - mapX;
            box.y = box.originalY - mapY;
        }
    }

    private void spawnEbull(int x, int y, Enemy enemy){
        if (enemies.contains(enemy)) { // Check if the enemy is still alive
            Point target= Player.getpoint(getWidth()/2,getHeight()/2);
            ebullets.add(new EnemyBullets(x, y, target));
        }
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
        x = Math.max(-5000, Math.min(x, 5000));
        y = Math.max(-5000, Math.min(y, 5000));
        enemies.add(new Enemy(x, y));

        System.out.println("Enemy spawned at: " + x + ", " + y); // Debug
    }


    private void enemyshoot(){
        for(Enemy enemy: enemies) {
            if (!enemyTimers.containsKey(enemy)) { // Only create a timer if one doesn't exist
                Timer enemyShootTimer = new Timer(3000, _ ->spawnEbull(enemy.x-mapX, enemy.y-mapY, enemy)); // Pass the enemy to spawnEbull
                enemyTimers.put(enemy, enemyShootTimer);
                enemyShootTimer.start();
            }
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

        // Distancia en chunks desde el jugador para activar
        int chunkRadius = 1;
        for (int x = CurrentChunkX - chunkRadius; x <= CurrentChunkX + chunkRadius; x++) {
            for (int y = CurrentChunkY - chunkRadius; y <= CurrentChunkY + chunkRadius; y++) {
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
        Timer BoxSpawner = new Timer(5000, _ ->spawnBoxes());
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
            checkenemyhit();
            updateBoxes();
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




