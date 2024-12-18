package item;

import entity.Entity;
import main.GamePanel;


public class VisionItem extends Entity {
    public VisionItem(GamePanel gp) {
        super(gp);
        down1 = setupImage("/items/lantern.png");
    }

    public void setup() {
        x = startX;
        y = startY;
    }

    public void collideEffect() {
        super.gp.envManager.lightRadius += 120;
        super.gp.envManager.lightCounter += (4 * gp.FPS);
    }
}
