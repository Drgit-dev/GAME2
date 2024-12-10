package org.example;

public class Main {
    public static void main(String[] args) {
        GamePanel gamePanel = new GamePanel();
        Window window = new Window("Game with FPS Counter and Rotating Sprite", gamePanel);
        Thread gameThread = new Thread(gamePanel);
        gameThread.start();
    }
}