package entity;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import environment.LightingProperty;
import main.Direction;
import main.GamePanel;
import main.GameState;
import main.KeyHandler;


public class Player extends Entity {
    public boolean toMove;
    public int playerNum;
    public int playerImageId;
    public Map<Direction, Integer> moveKey;
    public LightingProperty lightingProp;
    KeyHandler keyH;

    public Player(GamePanel gp, int playerNum, KeyHandler keyH) {
        super(gp);
        this.playerNum = playerNum;
        this.playerImageId = playerNum;
        this.keyH = keyH;
        this.lightingProp = new LightingProperty();
        this.toMove = false;

        moveKey = new HashMap<>();
        if (playerNum == 1) {
            moveKey.put(Direction.UP, KeyEvent.VK_W);
            moveKey.put(Direction.DOWN, KeyEvent.VK_S);
            moveKey.put(Direction.LEFT, KeyEvent.VK_A);
            moveKey.put(Direction.RIGHT, KeyEvent.VK_D);
        } else if (playerNum == 2) {
            moveKey.put(Direction.UP, KeyEvent.VK_UP);
            moveKey.put(Direction.DOWN, KeyEvent.VK_DOWN);
            moveKey.put(Direction.LEFT, KeyEvent.VK_LEFT);
            moveKey.put(Direction.RIGHT, KeyEvent.VK_RIGHT);
        }

        getPlayerImage(playerImageId);
    }

    public void setup() {
        x = startX;
        y = startY;
        speed = 4;
        direction = Direction.DOWN;
        toMove = false;
    }

    public void reset() {
        setup();
        lightingProp.reset();
    }

    private void getPlayerImage(int imageId) {
        String pathPrefix = "/player/player" + imageId;

        up1 = setupImage(pathPrefix + "-up1.png");
        up2 = setupImage(pathPrefix + "-up2.png");
        down1 = setupImage(pathPrefix + "-down1.png");
        down2 = setupImage(pathPrefix + "-down2.png");
        left1 = setupImage(pathPrefix + "-left1.png");
        left2 = setupImage(pathPrefix + "-left2.png");
        right1 = setupImage(pathPrefix + "-right1.png");
        right2 = setupImage(pathPrefix + "-right2.png");
    }

    public void togglePlayerImage(int imageId1, int imageId2) {
        playerImageId = playerImageId == imageId1 ? imageId2 : imageId1;
        getPlayerImage(playerImageId);
    }

    public void update() {
        if (gp.gameState != GameState.PLAY) {
            return;
        }

        toMove = true;
        if (keyH.pressedMoveKeys.contains(moveKey.get(Direction.UP))) {
            direction = Direction.UP;
        } else if (keyH.pressedMoveKeys.contains(moveKey.get(Direction.DOWN))) {
            direction = Direction.DOWN;
        } else if (keyH.pressedMoveKeys.contains(moveKey.get(Direction.LEFT))) {
            direction = Direction.LEFT;
        } else if (keyH.pressedMoveKeys.contains(moveKey.get(Direction.RIGHT))) {
            direction = Direction.RIGHT;
        } else {
            toMove = false;
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
        item.collideEffect(this);
    }
}
