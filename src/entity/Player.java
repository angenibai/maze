package entity;

import main.Direction;
import main.GamePanel;
import main.GameState;


public class Player extends Entity {
    public boolean toMove;
    public int playerNum;

    public Player(GamePanel gp, int playerNum) {
        super(gp);
        this.playerNum = playerNum;
        this.toMove = false;

        getPlayerImage();
    }

    public void setup() {
        x = startX;
        y = startY;
        speed = 4;
        direction = Direction.DOWN;
        toMove = false;
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

        if (toMove) {
            collisionOn = false;
                gp.cChecker.checkBoundary(this);
                
                if (!collisionOn) {
                    gp.cChecker.checkTile(this);
                    if (!collisionOn) {
                        switch (direction) {
                            case UP:
                                y -= speed;
                                break;
                            case DOWN:
                                y += speed;
                                break;
                            case LEFT:
                                x -= speed;
                                break;
                            case RIGHT:
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
