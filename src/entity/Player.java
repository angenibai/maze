package entity;

import main.GamePanel;
import main.GameState;
import main.KeyHandler;


public class Player extends Entity {
    KeyHandler keyH;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        getPlayerImage();
    }

    public void setup() {
        x = startX;
        y = startY;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        up1 = setupImage("/player/boy_up_1.png");
        up2 = setupImage("/player/boy_up_2.png");
        down1 = setupImage("/player/boy_down_1.png");
        down2 = setupImage("/player/boy_down_2.png");
        left1 = setupImage("/player/boy_left_1.png");
        left2 = setupImage("/player/boy_left_2.png");
        right1 = setupImage("/player/boy_right_1.png");
        right2 = setupImage("/player/boy_right_2.png");
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

                        Entity collided = gp.cChecker.checkItems(this);
                        if (collided != null) {
                            collectItem(collided);
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

    public void collectItem(Entity item) {
        gp.items.remove(item);
        item.collideEffect();
    }
}
