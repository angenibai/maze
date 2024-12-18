package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class UtilityTool {
    private UtilityTool() {}

    public static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        int imgType = 2;
        BufferedImage scaled = new BufferedImage(width, height, imgType);
        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaled;
    }
}
