package environment;

import main.GamePanel;

import java.awt.*;

public class EnvironmentManager {
    GamePanel gp;
    Lighting lighting;
    public int defaultLightRadius = 250;
    public int lightRadius = defaultLightRadius;
    public int lightCounter = 0;

    public EnvironmentManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setup() {
        lighting = new Lighting(gp, lightRadius);
    }

    public void update() {
        if (lightCounter > 0) {
            lightCounter--;
        }

        // reset light radius
        if (lightRadius != defaultLightRadius && lightCounter == 0) {
            lightRadius = defaultLightRadius;
        }

        lighting.update(lightRadius);
    }

    public void draw(Graphics2D g2) {
        if (lighting != null) {
            lighting.draw(g2);
        }
    }

    public void reset() {
        lightRadius = defaultLightRadius;
    }
}
