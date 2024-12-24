package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {
    public GamePanel gp;

    public int startX, startY;
    public int x, y;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionOn = false;

    public Entity(GamePanel gp) {
        this.gp = gp;
        solidArea = createSolidArea();
    }

    public BufferedImage setupImage(String filePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(filePath));
            image = UtilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public Rectangle createSolidArea() {
        Rectangle area = new Rectangle();
        area.x = (int) Math.round(2.7 * gp.scale);
        area.y = area.x * 2;
        area.width = (int) Math.round(3 * area.x);
        area.height = area.width;
        return area;
    }

    public void setup() {
        System.out.println("setup() not implemented");
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }

        g2.drawImage(image, x, y, null);
    }

    public void reset() {
        setup();
    }

    public void collideEffect() {
        System.out.println("collideEffect() not implemented");
    }
}
