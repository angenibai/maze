package entity;

import main.Direction;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {
    public GamePanel gp;

    public int startX, startY;
    public int x, y;
    public int speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public Direction direction = Direction.DOWN;

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
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(filePath)));
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
        BufferedImage image = switch (direction) {
            case UP -> (spriteNum == 1) ? up1 : up2;
            case DOWN -> (spriteNum == 1) ? down1 : down2;
            case LEFT -> (spriteNum == 1) ? left1 : left2;
            case RIGHT -> (spriteNum == 1) ? right1 : right2;
        };

        g2.drawImage(image, x, y, null);
    }

    public void reset() {
        setup();
    }

    public void collideEffect(Player player) {
        System.out.println("collideEffect() not implemented");
    }
}
