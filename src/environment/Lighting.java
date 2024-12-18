package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;

    public Lighting(GamePanel gp, int circleSize) {
        this.gp = gp;
        update(circleSize);
    }

    public void update(int circleSize) {
        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        int centerX = gp.player.x + (gp.tileSize) / 2;
        int centerY = gp.player.y + (gp.tileSize) / 2;

        Color[] color = setupColor();
        float[] fraction = setupFraction();

        RadialGradientPaint gPaint = new RadialGradientPaint(centerX, centerY, ((float) circleSize /2), fraction, color);

        g2.setPaint(gPaint);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.dispose();
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(darknessFilter, 0, 0, null);
        // potentially implement remove filter after level complete
    }


    private Color[] setupColor() {
        Color[] color = new Color[5];

        color[0] = new Color(0,0,0,0f);
        color[1] = new Color(0,0,0,0.25f);
        color[2] = new Color(0,0,0,0.5f);
        color[3] = new Color(0,0,0,0.75f);
        color[4] = new Color(0,0,0,0.96f);

        return color;
    }

    private float[] setupFraction() {
        float[] fraction = new float[5];

        fraction[0] = 0f;
        fraction[1] = 0.25f;
        fraction[2] = 0.5f;
        fraction[3] = 0.75f;
        fraction[4] = 1f;

        return fraction;
    }
}
