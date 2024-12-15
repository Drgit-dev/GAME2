package org.example;
import java.awt.*;
public class GUI {

    public void render(Graphics g2d, int HP, int HPMAX, int MP, int MPMAX, int AMMO, int enemies){
        g2d.setColor(Color.gray);
        g2d.fillRect(15,150,HPMAX,32);
        g2d.setColor(Color.green);
        g2d.fillRect(15,150,HP ,32);
        g2d.setColor(Color.white);
        g2d.drawRect(15,150,HPMAX,32);
        g2d.setColor(Color.gray);
        g2d.fillRect(15,190,MPMAX,32);
        g2d.setColor(Color.blue);
        g2d.fillRect(15,190,MP,32);
        g2d.setColor(Color.white);
        g2d.drawRect(15,190,MPMAX,32);
        g2d.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 30));
        g2d.setColor(Color.BLACK);
        g2d.drawString("HP",18,175);
        g2d.drawString("MP",18,220);
        g2d.drawString("AMMO:  " + AMMO, 15, 250);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(15, 700, 128, 128);
        g2d.drawString("WEAPON", 15, 695);
        g2d.drawString("ENEMIES LEFT"+ enemies, 15, 270);

    }
}
