package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

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
        try {
            tile[GRASS] = new Tile();
            tile[GRASS].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass01.png"));

            tile[TREE] = new Tile();
            tile[TREE].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[TREE].collision = true;

            tile[WATER] = new Tile();
            tile[WATER].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[WATER].collision = true;

            tile[END] = new Tile();
            tile[END].image = ImageIO.read(getClass().getResourceAsStream("/tiles/floor01.png"));
            tile[END].collision = false;
            tile[END].end = true;

        } catch(IOException e) {
            e.printStackTrace();
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
                                gp.tileSize, gp.tileSize,
                                null);
            }
        }
    }
}
