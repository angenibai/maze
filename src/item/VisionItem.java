package item;

import entity.Entity;
import entity.Player;
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

    public void collideEffect(Player player) {
        player.lightingProp.radius += 60;
        player.lightingProp.counter += (4 * gp.FPS);
    }
}
