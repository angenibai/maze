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

    private int GRASS = 0;
    private int WALL = 1;
    private int WATER = 2;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap("/maps/map01.txt");
    }

    public void getTileImage() {
        try {
            tile[GRASS] = new Tile();
            tile[GRASS].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[WALL] = new Tile();
            tile[WALL].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
            tile[WALL].collision = true;

            tile[WATER] = new Tile();
            tile[WATER].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
            tile[WATER].collision = true;

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
