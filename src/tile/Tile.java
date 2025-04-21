package tile;

import java.awt.image.BufferedImage;

public class Tile {
    public static final int PATH = 0;
    public static final int WALL = 1;

    public BufferedImage image;
    public boolean collision = false;
    public boolean end = false;
}
