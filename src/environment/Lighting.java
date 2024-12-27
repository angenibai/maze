package environment;

import main.GamePanel;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;

    public Lighting(GamePanel gp) {
        this.gp = gp;
        update();
    }

    public void update() {
        int p1Radius = gp.player1.lightingProp.radius;
        int p2Radius = gp.player2.lightingProp.radius;

        darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f));

        setCircle(g2, gp.player1.x, gp.player1.y, p1Radius);
        setCircle(g2, gp.player2.x, gp.player2.y, p2Radius);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        g2.dispose();
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(darknessFilter, 0, 0, null);
        // potentially implement remove filter after level complete
    }

    private int getCenter(int val) {
        return val + (gp.tileSize) / 2;
    }

    private void setCircle(Graphics2D g2, int playerX, int playerY, int radius) {
        int centerX = getCenter(playerX);
        int centerY = getCenter(playerY);

        RadialGradientPaint gradient = new RadialGradientPaint(
                centerX, centerY,
                radius,
                setupFraction(), setupColor()
        );
        g2.setPaint(gradient);

        int leftX = getCenter(playerX) - radius;
        int topY = getCenter(playerY) - radius;

        Ellipse2D circle = new Ellipse2D.Double(leftX, topY, radius*2, radius*2);
        g2.fill(circle);
    }


    private Color[] setupColor() {
        Color[] color = new Color[5];

        // inverted because it is applied with inverted settings
        color[4] = new Color(0,0,0,0.05f);
        color[3] = new Color(0,0,0,0.25f);
        color[2] = new Color(0,0,0,0.5f);
        color[1] = new Color(0,0,0,0.75f);
        color[0] = new Color(0,0,0,0.96f);

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
