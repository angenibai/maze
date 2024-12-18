package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    private final int GRASS = 0;
    private final int TREE = 1;
    private final int WATER = 2;
    private final int END = 3;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("/maps/map02.txt");
    }

    public void getTileImage() {
        tile[GRASS] = new TileBuilder("/tiles/grass01.png", gp.tileSize).build();
        tile[TREE] = new TileBuilder("/tiles/tree.png", gp.tileSize)
                            .withCollision().build();
        tile[END] = new TileBuilder("/tiles/floor01.png", gp.tileSize)
                .withEnd().build();
    }

    public static class TileBuilder {
        private BufferedImage image;
        private boolean collision = false;
        private boolean end = false;

        public TileBuilder(String imagePath, int size) {
            try {
                BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
                image = UtilityTool.scaleImage(originalImage, size, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public TileBuilder withCollision() {
            collision = true;
            return this;
        }

        public TileBuilder withEnd() {
            end = true;
            return this;
        }

        public Tile build() {
            Tile tile = new Tile();
            tile.image = image;
            tile.collision = collision;
            tile.end = end;

            return tile;
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxScreenRow; row++) {
                String line = br.readLine();

                for (int col = 0; col < gp.maxScreenCol; col++) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                }
            }

            br.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
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
