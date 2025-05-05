package environment;

import main.GamePanel;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
         lighting = new Lighting(gp);
    }

    public void update() {
        if (lighting != null) {
            gp.player1.lightingProp.update();
            gp.player2.lightingProp.update();
            lighting.update();
        }

    }

    public void draw(Graphics2D g2) {
        if (lighting != null) {
            lighting.draw(g2);
        }
    }

    public void reset() {
        if (lighting != null) {
            gp.player1.lightingProp.reset();
            gp.player2.lightingProp.reset();
        }

    }
}
