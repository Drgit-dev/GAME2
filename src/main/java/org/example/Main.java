package org.example;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("  __  __          _ _                 _   ____  _____  ");
        System.out.println(" |  \\/  |        | (_)               | | |  _ \\|  __ \\ ");
        System.out.println(" | \\  / | ___  __| |_  _____   ____ _| | | |_) | |__) |");
        System.out.println(" | |  | |  __/ (_| | |  __/\\ V / (_| | | | |_) | | \\ \\ ");
        System.out.println(" |_|  |_|\\___|\\__,_|_|\\___| \\_/ \\__,_|_| |____/|_|  \\_\\");
        System.out.println("1.PLAY\n2.EXIT");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                System.out.println("Choose a class\n1.Human\n2.Mage\n3.Ogre");
                int choice = sc.nextInt();
                    GamePanel gamePanel = new GamePanel(choice);
                     Window window = new Window("Game with FPS Counter and Rotating Sprite", gamePanel);
                     Thread gameThread = new Thread(gamePanel);
                     gameThread.start();
                break;
                case 2:
                    System.exit(0);
        }


    }

}
