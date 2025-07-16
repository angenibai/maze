package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
    }

    public void getTileImage() {
        tile[Tile.PATH] = new TileBuilder("/tiles/grass.png", gp.tileSize).build();
        tile[Tile.WALL] = new TileBuilder("/tiles/hedge.png", gp.tileSize)
                            .withCollision().build();
    }

    public static class TileBuilder {
        private BufferedImage image;
        private boolean collision = false;

        public TileBuilder(String imagePath, int size) {
            try {
                BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
                image = UtilityTool.scaleImage(originalImage, size, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public TileBuilder withCollision() {
            collision = true;
            return this;
        }

        public Tile build() {
            Tile tile = new Tile();
            tile.image = image;
            tile.collision = collision;

            return tile;
        }
    }

    public void setup() {
        this.mapTileNum = gp.mazeManager.screenTiles;
    }

    public void reset() {
        this.mapTileNum = gp.mazeManager.screenTiles;
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.maxScreenRow; row++) {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                int tileNum = mapTileNum[col][row];

                g2.drawImage(tile[tileNum].image,
                                col*gp.tileSize, row*gp.tileSize,
                                null);
            }
        }
    }
}
