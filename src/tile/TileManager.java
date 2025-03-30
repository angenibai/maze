package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    public MazeGenerator mazeGen;

    private final int GRASS = 0;
    private final int TREE = 1;
    private final int END = 3;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        tile[GRASS] = new TileBuilder("/tiles/grass.png", gp.tileSize).build();
        tile[TREE] = new TileBuilder("/tiles/hedge.png", gp.tileSize)
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

    /** Generate map */
    public void loadMap() {
        int numCols = (gp.maxScreenCol - 1) / 2;
        int numRows = (gp.maxScreenRow - 1) / 2;
        this.mazeGen = new MazeGenerator(numRows, numCols);

        for (int r = 0; r < gp.maxScreenRow; r++) {
            for (int c = 0; c < gp.maxScreenCol; c++) {
                boolean isWall = true;

                // check horizontal tunnels
                if (r > 0 && r < numRows * 2 && r % 2 == 1) {
                    if (c > 1) {
                        // check if the cell on the west side extends into the east
                        int cellR = (r - 1) / 2;
                        int cellC = (c - 2) / 2;
                        if (mazeGen.cells[cellR][cellC].E) {
                            isWall = false;
                        }
                    }

                    if (c < numCols * 2 - 1) {
                        // check if the cell on the east side extends into the west
                        int cellR = (r - 1) / 2;
                        int cellC = (c + 1) / 2;
                        if (mazeGen.cells[cellR][cellC].W) {
                            isWall = false;
                        }
                    }
                }

                // check vertical tunnels
                if (c > 0 && c < numCols * 2 && c % 2 == 1) {
                    if (r > 1) {
                        // check if the cell on the north side extends into the south
                        int cellR = (r - 2) / 2;
                        int cellC = (c - 1) / 2;
                        if (mazeGen.cells[cellR][cellC].S) {
                            isWall = false;
                        }
                    }

                    if (r < numRows * 2 - 1) {
                        // check if the cell on the south side extends into the north
                        int cellR = (r + 1) / 2;
                        int cellC = (c - 1) / 2;
                        if (mazeGen.cells[cellR][cellC].N) {
                            isWall = false;
                        }
                    }
                }

                mapTileNum[c][r] = isWall ? TREE : GRASS;
            }
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
