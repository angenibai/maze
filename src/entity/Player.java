package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.GameState;
import main.KeyHandler;
import main.UtilityTool;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        solidArea = new Rectangle();
        solidArea.x = (int) Math.round(2.7 * gp.scale);
        solidArea.y = solidArea.x * 2;
        solidArea.width = (int) Math.round(3 * solidArea.x);
        solidArea.height = solidArea.width;

        getPlayerImage();
    }

    public void setup() {
        x = startX;
        y = startY;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setup("boy_up_1");
        up2 = setup("boy_up_2");
        down1 = setup("boy_down_1");
        down2 = setup("boy_down_2");
        left1 = setup("boy_left_1");
        left2 = setup("boy_left_2");
        right1 = setup("boy_right_1");
        right2 = setup("boy_right_2");
    }

    public BufferedImage setup(String imgName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+imgName+".png"));
            image = UtilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public void update() {
        if (gp.gameState != GameState.PLAY) {
            return;
        }

        if (keyH.upPressed || keyH.downPressed ||
            keyH.leftPressed || keyH.rightPressed) {
                if (keyH.upPressed) {
                    direction = "up";
                } else if (keyH.downPressed) {
                    direction = "down";
                } else if (keyH.leftPressed) {
                    direction = "left";
                } else if (keyH.rightPressed) {
                    direction = "right";
                }

                collisionOn = false;
                gp.cChecker.checkBoundary(this);
                
                if (!collisionOn) {
                    gp.cChecker.checkTile(this);
                    if (!collisionOn) {
                        switch (direction) {
                            case "up":
                                y -= speed;
                                break;
                            case "down":
                                y += speed;
                                break;
                            case "left":
                                x -= speed;
                                break;
                            case "right":
                                x += speed;
                                break;
                        }
                    }
                }

                gp.cChecker.checkEnd(this);
        
                // walking animation
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
        }

    }

    public void reset() {
        setup();
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
}
